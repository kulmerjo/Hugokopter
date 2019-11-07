import smbus
import math


def dist(a, b):
    return math.sqrt((a * a) + (b * b))


def get_y_rotation(accel_x_scaled, accel_y_scaled, accel_z_scaled):
    radians = math.atan2(accel_x_scaled, dist(accel_y_scaled, accel_z_scaled))
    return -math.degrees(radians)


def get_x_rotation(accel_x_scaled, accel_y_scaled, accel_z_scaled):
    radians = math.atan2(accel_y_scaled, dist(accel_x_scaled, accel_z_scaled))
    return math.degrees(radians)


class Gyroscope:

    ACCEL_SCALE_MODIFIER_2G = 16384.0
    ACCEL_SCALE_MODIFIER_4G = 8192.0
    ACCEL_SCALE_MODIFIER_8G = 4096.0
    ACCEL_SCALE_MODIFIER_16G = 2048.0

    ACCEL_RANGE_2G = 0x00
    ACCEL_RANGE_4G = 0x08
    ACCEL_RANGE_8G = 0x10
    ACCEL_RANGE_16G = 0x18

    PWR_MGMT_1 = 0x6B

    ACCEL_X_REG = 0x3B
    ACCEL_Y_REG = 0x3D
    ACCEL_Z_REG = 0x3F

    ACCEL_CONFIG = 0x1C

    def __init__(self, address, bus=1):
        self._address = address
        self._bus = smbus.SMBus(bus)

    def init(self):
        self._bus.write_byte_data(self._address, self.PWR_MGMT_1, 0x00)

    def _read_i2c_word(self, register):
        high = self._bus.read_byte_data(self._address, register)
        low = self._bus.read_byte_data(self._address, register + 1)

        value = (high << 8) + low

        if value >= 0x8000:
            return -((65535 - value) + 1)
        else:
            return value

    def set_accel_range(self, accel_range):
        self._bus.write_byte_data(self._address, self.ACCEL_CONFIG, 0x00)
        self._bus.write_byte_data(self._address, self.ACCEL_CONFIG, accel_range)

    def read_accel_range(self):
        raw_data = self._bus.read_byte_data(self._address, self.ACCEL_CONFIG)
        return raw_data

    def get_accel_data(self):
        x = self._read_i2c_word(self.ACCEL_X_REG)
        y = self._read_i2c_word(self.ACCEL_Y_REG)
        z = self._read_i2c_word(self.ACCEL_Z_REG)

        accel_range = self.read_accel_range()

        if accel_range == self.ACCEL_RANGE_2G:
            accel_scale_modifier = self.ACCEL_SCALE_MODIFIER_2G
        elif accel_range == self.ACCEL_RANGE_4G:
            accel_scale_modifier = self.ACCEL_SCALE_MODIFIER_4G
        elif accel_range == self.ACCEL_RANGE_8G:
            accel_scale_modifier = self.ACCEL_SCALE_MODIFIER_8G
        elif accel_range == self.ACCEL_RANGE_16G:
            accel_scale_modifier = self.ACCEL_SCALE_MODIFIER_16G
        else:
            print("Unkown range - accel_scale_modifier set to self.ACCEL_SCALE_MODIFIER_2G")
            accel_scale_modifier = self.ACCEL_SCALE_MODIFIER_2G

        x = x / accel_scale_modifier
        y = y / accel_scale_modifier
        z = z / accel_scale_modifier

        return {'x': x, 'y': y, 'z': z}

    def get_rotation(self):
        accel_data = self.get_accel_data()
        x_rotation = get_x_rotation(accel_data["x"], accel_data["y"], accel_data["z"])
        y_rotation = get_y_rotation(accel_data["x"], accel_data["y"], accel_data["z"])
        return x_rotation, y_rotation
