import video_sender


client_address = ("127.0.0.1", 12345)

vs = video_sender.VideoSender(client_address)
vs.start()
input("Press enter to exit.\n")
vs.stop()
vs.join()