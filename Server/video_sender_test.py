import video_sender

vs = video_sender.VideoSender()
vs.start()
input("Press enter to exit.\n")
vs.stop()
vs.join()