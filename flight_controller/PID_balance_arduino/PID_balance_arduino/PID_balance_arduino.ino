#include <Wire.h>
#include <Servo.h>
#include <AccelGyro.h>
#include <Drone.h>
#include <PID.h>



extern TwoWire Wire1;

Drone drone;

void setup() {
  Wire.begin();
  Wire1.begin(0x13);
  readCommands();
  Serial.begin(9600);
  drone.initialize();   
}

void loop() {
  readCommands()
  drone.droneLoop()

}

void readCommands() { 
  if (Wire1.available() >= 2) {
    char sent_command = Wire1.read();  
    char sent_speed = Wire1.read();
    drone.setThrottle((int)sent_speed);
    drone.setDirection((int)sent_command);
  }
}
