from gyroscope import Gyroscope
from pid import PID


class Stabilizer():
    GYROSCOPE_ADDRESS = 0x68

    MAX_ROT_X = 0.2
    MIN_ROT_X = -0.2

    MAX_ROT_Y = 0.2
    MIN_ROT_Y = -0.2

    def __init__(self):
        self._gyroscope = Gyroscope(self.GYROSCOPE_ADDRESS)
        try:
            self._gyroscope.init()
        except IOError:
            print("Cannot init gyroscope")
        self._pitch_pid = PID(3.5, 0, 3.1)
        self._roll_pid = PID(1.8, 0.1, 0.7)
        self._target_rot_x = 0.0
        self._target_rot_y = 0.0

    def set_target_rot_x(self, target_pitch):
        self._target_rot_x = max(min(target_pitch, self.MAX_ROT_X), self.MIN_ROT_X)
        self._pitch_pid.set_point(self._target_rot_x)

    def set_target_rot_y(self, target_roll):
        self._target_rot_y = max(min(target_roll, self.MAX_ROT_Y), self.MIN_ROT_Y)
        self._roll_pid.set_point(self._target_rot_y)

    def stabilize(self):
        try:
            rotation_x, rotation_y = self._gyroscope.get_rotation()
        except IOError:
            print("Cannot get gyro data")
            return False
        _pitch_value = self._pitch_pid(rotation_y)
        #_roll_value = self._roll_pid.update(rotation_x)
        return 0, _pitch_value, 0
