from components.motor import Motor
from components.gyroscope import Gyroscope
from components.stabilizer import Stabilizer
from time import sleep
import pigpio
import time
import threading


class FlightController:

    GYROSCOPE_ADDRESS = 0x68

    def __init__(self, lf_motor=12, rf_motor=13, lb_motor=18, rb_motor=19):
        #threading.Thread.__init__(self)
        self._motors = {
            "lf": Motor(lf_motor),
            "rf": Motor(rf_motor),
            "lb": Motor(lb_motor),
            "rb": Motor(rb_motor)
        }
        self._gyroscope = Gyroscope(self.GYROSCOPE_ADDRESS)
        try:
            self._gyroscope.init()
        except IOError:
            print("Cannot initialize gyroscope")
        self._pitch = 0
        self._roll = 0
        self._throttle = 0
        self._yaw = 0
        self._is_calibrated = False
        self._stabilizer = Stabilizer()
        self._exit_flag = False
        self._is_turned_on = False
        self._last_motors_pulses = {
            "lf": 0,
            "rf": 0,
            "lb": 0,
            "rb": 0
        }
        self._time_on = 0

    def turn_on(self):
        self._is_turned_on = True
        self._time_on = time.time()

    def turn_off(self):
        self._is_turned_on = False
        for motor in self._motors.values():
            motor.set_pulse_percentage(0.0)
        self._pitch = 0
        self._roll = 0
        self._throttle = 0
        self._yaw = 0

    def stop(self):
        self._exit_flag = True

    def _update_motors(self):
        if not self._is_calibrated:
            return
        if not self._is_turned_on:
            return
        if time.time() > self._time_on+5:
            self.turn_off()
            self.stop()
            return
        values = self._stabilizer.stabilize()
        if not values:
            return
        _, self._pitch, _ = values
        motors_pulses = self._calculate_motor_pulses()
        for key, value in motors_pulses.items():
            value = max(min(value, 1000), 0)
            if self._last_motors_pulses[key] == value:
                continue
            self._motors[key].set_pulse(value)
            self._last_motors_pulses[key] = value

    def set_throttle(self, throttle):
        self._throttle = throttle

    def _calculate_motor_pulses(self):
        return {
            "lf": self._throttle + self._pitch + self._roll + self._yaw,
            "rf": self._throttle + self._pitch - self._roll - self._yaw,
            "lb": self._throttle - self._pitch + self._roll - self._yaw,
            "rb": self._throttle - self._pitch - self._roll + self._yaw
        }

    def run(self):
        while not self._exit_flag:
            self._update_motors()
        self.dispose()

    def dispose(self):
        for motor in self._motors.values():
            motor.dispose()

    def calibrate(self):
        raw_input("Disconnect battery and press enter")
        for motor in self._motors.values():
            motor.set_pulse_percentage(1.0)
        raw_input("Connect battery")
        for motor in self._motors.values():
            motor.set_pulse_percentage(0.0)
        raw_input("Press enter after signal")
        self._is_calibrated = True
