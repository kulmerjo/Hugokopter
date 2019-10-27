from flight_controller.flight_controller import FlightController
from time import sleep

if __name__ == "__main__":
    flightController = FlightController()
    flightController.start()
    flightController.calibrate()
    flightController.turn_on()
    sleep(10)
    flightController.set_throttle(200)
    key = raw_input("Input")
    flightController.turn_off()
    flightController.stop()
    flightController.join()