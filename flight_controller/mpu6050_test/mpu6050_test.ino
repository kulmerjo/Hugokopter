#include <AccelGyro.h>

AccelGyro accelGyro;
Rotation rotation;

void setup() {
  accelGyro.Initialize();
  Serial.begin(9600);
  Serial.println("Rotation:");
  Serial.println("x\ty\tz");
}

void loop() {
  if(Serial.available()){
    if(Serial.read() == 1){
      accelGyro.Calibrate();
    }
  }
  rotation = accelGyro.GetRotation();
  Serial.print(rotation.x);
  Serial.print("\t");
  Serial.print(rotation.y);
  Serial.print("\t");
  Serial.println(rotation.z);
}
