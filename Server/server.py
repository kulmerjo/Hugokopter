import socket


class Server:

    def __init__(self):
        self.__config = "config/server_config.json"
        self.__socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

