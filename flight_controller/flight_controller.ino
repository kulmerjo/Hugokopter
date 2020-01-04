#include <Wire.h>
#include <Servo.h>
#include <AccelGyro.h>
#include <PID.h>


Drone drone;


void setup() {
  Wire.begin(0x13);
  Serial.begin(9600);
  drone.initialize();
}

void loop() {
  onCommand();
  drone.droneLoop();  
}

void onCommand() {
  if (Wire.available() >= 2) {
    int drone_command = (int)Wire.read();  
    int drone_speed = (int)Wire.read();
    drone.setDirection(drone_command, drone_speed);
  }
  
  
}
