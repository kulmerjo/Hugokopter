import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)


class Motor:

    MAX_PULSE = 1000
    MIN_PULSE = 0

    def __init__(self, port, pi):
        self._port = port
        self._pulse = 0
        self._pi = pi
        self._pi.set_servo_pulsewidth(self._port, self._pulse)

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
        self._pulse = int(pulse) + 1000
        self._pi.set_servo_pulsewidth(self._port, self._pulse)
        return self._pulse

    def dispose(self):
        self._pi.set_servo_pulsewidth(self._port, 0)
