import cv2
import socket
import time

serverAddressPort = ('', 12345)
bufferSize = 65536
client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
time_start = time.time()

while True:
    image, address = client_socket.recvfrom(bufferSize)
    cv2.imshow("Frame", image)
    if time_start + 30 > time.time():
        break
