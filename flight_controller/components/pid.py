import time


class PID:

    def __init__(self, kp=2.0, ki=0.0, kd=0.0, set_point=0.0, sample_time=0.05, output_max=500.0, output_min=-500.0):
        self._kp, self._ki, self._kd = kp, ki, kd
        self._set_point = set_point
        self._output_max = output_max
        self._output_min = output_min
        self._sample_time = sample_time
        self._proportional = 0
        self._integral = 0
        self._derivative = 0
        self._last_time = time.time()
        self._last_output = None
        self._last_error = None

    def __call__(self, current_value):
        current_time = time.time()
        dt = current_time - self._last_time if current_time - self._last_time else 1e-16
        if dt < self._sample_time and self._last_output is not None:
            return self._last_output
        error = self._set_point - current_value
        d_error = error - self._last_error if self._last_error is not None else 0
        self._proportional = self._kp * error
        self._integral += self._ki * error * dt
        self._integral = max(min(self._integral, self._output_max), self._output_min)
        self._derivative = -self._kd * d_error / dt
        output = self._proportional + self._integral + self._derivative
        output = max(min(output, self._output_max), self._output_min)
        self._last_error = error
        self._last_output = output
        self._last_time = current_time
        return output

    def set_point(self, set_point):
        self._set_point = set_point

    def set_components(self, kp=0.0, ki=0.0, kd=0.0):
        self._kp, self._ki, self._kd = kp, ki, kd

    def set_limits(self, output_max=1.0, output_min=-1.0):
        self._output_max, self._output_min = output_max, output_min

    def set_sample_time(self, sample_time=0.01):
        self._sample_time = sample_time

    def reset(self):
        self._proportional = 0
        self._integral = 0
        self._derivative = 0
        self._last_time = time.time()
        self._last_output = None
        self._last_error = None
