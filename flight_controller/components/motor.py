import RPi.GPIO as GPIO
from time import sleep

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)


class Motor:

    MAX_PULSE = 1000
    MIN_PULSE = 0
    PULSE_SHIFT = 1000
    FREQUENCY = 250
    PULSE_SCALE_MODIFIER = 1000.0

    def __init__(self, port):
        self._port = port
        self._pulse = 0
        GPIO.setup(port, GPIO.OUT)
        self._motor = GPIO.PWM(self._port, self.FREQUENCY)
        self._motor.start(0)

    def set_pulse_percentage(self, percentage):
        if 0 > percentage > 1:
            print("Wrong percentage value. Percentage value should be in range from 0 to 1.")
            return False
        return self.set_pulse((self.MAX_PULSE - self.MIN_PULSE) * percentage)

    def get_pulse(self):
        return self._pulse

    def set_pulse(self, pulse):
        if self.MIN_PULSE > pulse > self.MAX_PULSE:
            print("Wrong pulse value. Pulse value should be in range from 1000 to 2000.")
            return False
        self._pulse = int(pulse) + self.PULSE_SHIFT
        pwm_duration = (1.0 / self.FREQUENCY) * self.PULSE_SCALE_MODIFIER
        pulse_duration = self._pulse / self.PULSE_SCALE_MODIFIER
        duty_cycle = pulse_duration / pwm_duration
        self._motor.ChangeDutyCycle(duty_cycle * 100)
        return self._pulse

    def dispose(self):
        self._motor.stop()


if __name__ == "__main__":
    motor = Motor(12)
    raw_input("Disconnect battery and press enter")
    for i in range(150):
        motor.set_pulse(i)
        sleep(0.05)
    for i in range(150):
        motor.set_pulse(150-i)
    sleep(2)
    motor.dispose()
