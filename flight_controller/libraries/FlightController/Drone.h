#ifndef Drone_h
#define Drone_h

#include <Wire.h>
#include <Servo.h>
#include <AccelGyro.h>
#include "Arduino.h"
#include <PID.h>



const float kp_pitch = 3;
const float ki_pitch = 0.0;
const float kd_pitch = 0.3;
const double kp_roll = 1;
const double ki_roll = 0;
const double kd_roll = 0;
const double kp_yaw = 1;
const double ki_yaw = 0;
const double kd_yaw = 0;
///////////////////////////////////////////////



class Drone {
    public:
        void initialize();
        void droneLoop();
        void setThrottle(int speed);
        void setDirection(int command, int drone_speed);
    private:
        Servo right_prop;
        Servo left_prop;
        Servo left_prop2;
        Servo right_prop2;
        AccelGyro accelGyro;
        PID pid_pitch, pid_roll, pid_yaw;
        int throttle = 1000;
        int motorLeft, motorLeft2, motorRight, motorRight2;
        int isWorking = 0;
        int state = 0;
        void setMotorsWhenDronIsOff();
        void setMotorsWhenDronIsOn(Rotation rotation);
        void setMotorPowers();
        void clampValue(int &value);
        void printAllMotors();
};
#endif