#pip install "picamera[array]"
import socket
from threading import Thread, Lock
from picamera import PiCamera
from picamera.array import PiRGBArray
import time
import sys


class VideoSender(Thread):

    def __init__(self, client_address):
        Thread.__init__(self)
        self._client_address = client_address
        self._camera = None
        self._camera_framerate = 24
        self._camera_resolution = (640, 480)
        self._socket = None
        self._initialize_camera()
        self._initialize_socket()
        self._stopped = False
        self._paused = False
        self._stop_signal_lock = Lock()
        self._pause_signal_lock = Lock()

    def _initialize_camera(self):
        self._camera = PiCamera()
        self._camera.resolution = self._camera_resolution
        self._camera.framerate = self._camera_framerate

    def _initialize_socket(self):
        try:
            self.__socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        except socket.error as msg:
            print(f'Failed binding specified interface {self._client_address[0]} and port {self._client_address[1]} error {msg}')
            sys.exit(-1)

    def stop(self):
        self._stop_signal_lock.acquire()
        self._stopped = True
        self._stop_signal_lock.release()

    def pause(self):
        self._pause_signal_lock.acquire()
        self._paused = True
        self._pause_signal_lock.release()

    def resume(self):
        self._pause_signal_lock.acquire()
        self._paused = False
        self._pause_signal_lock.release()

    def _is_stopped(self):
        self._stop_signal_lock.acquire()
        result = self._stopped
        self._stop_signal_lock.release()
        return result

    def _is_paused(self):
        self._pause_signal_lock.acquire()
        result = self._paused
        self._pause_signal_lock.release()
        return result

    def _send_image(self, image):
        self._socket.sendto(image, self._client_address)

    def _stream(self):
        raw_capture = PiRGBArray(self._camera, self._camera_resolution)
        time.sleep(0.1)
        for frame in self._camera.capture_continuous(raw_capture, format="jpg", use_video_port=True):
            image = frame.array
            if not self._is_paused():
                self._send_image(image)
            raw_capture.truncate(0)
            if self._is_stopped():
                break

    def run(self):
        self._stream()
