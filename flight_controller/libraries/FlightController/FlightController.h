#ifndef AccelGyro_h
#define AccelGyro_h

#include "Arduino.h"
#include "AccelGyro.h"

class FlightController {
public:
	FlightController();
private:
	AccelGyro _accelGyro;
};
#endif