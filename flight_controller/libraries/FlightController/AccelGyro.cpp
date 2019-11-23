#include "AccelGyro.h"
#include "Arduino.h"
#include "math.h"
#include "Wire.h"

AccelGyro::AccelGyro(void) {
	_address = 0x68;
}

AccelGyro::AccelGyro(int address) {
	_address = address;
}

void AccelGyro::Initialize() {
	_accelCorrection.x = 0.0;
	_accelCorrection.y = 0.0;
	_accelCorrection.z = 0.0;
	_gyroCorrection.x = 0.0;
	_gyroCorrection.y = 0.0;
	_gyroCorrection.z = 0.0;
	_compFilterAlpha = 0.04;
	_prevMeasurementTime = millis();
	SetAccelSensitivity(1);
	SetGyroSensitivity(1);
	I2cWriteData(POWER_REG, 0x00);
	delay(100);
	_rotationGyro = GetRotationAccel();
}

void AccelGyro::I2cWriteData(int address, byte data) {
	Wire.beginTransmission(_address);
	Wire.write(address);
	Wire.write(data);
	Wire.endTransmission(true);
}

void AccelGyro::I2cReadData(int address, char* output, int numberOfBytes) {
	Wire.beginTransmission(_address);
	Wire.write(address);
	Wire.endTransmission(false);
	Wire.requestFrom(address, numberOfBytes, true);

	for (int i = 0; i < numberOfBytes; i++) {
		output[i] = Wire.read();
	}
}

byte AccelGyro::I2cReadOneByte(int address) {
	Wire.beginTransmission(_address);
	Wire.write(address);
	Wire.endTransmission(false);
	Wire.requestFrom(address, 1, true);
	return Wire.read();
}

bool AccelGyro::IsConnected() {
	byte checkConnValue = I2cReadOneByte(CHECK_CONN_REG);
	return checkConnValue == _address;
}

RawSensorData AccelGyro::GetRawData(int dataRegister) {
	char rawData[6];
	RawSensorData rawSensorData;
	I2cReadData(dataRegister, rawData, 6);
	rawSensorData.x = (rawData[0] << 8 | rawData[1]);
	rawSensorData.y = (rawData[2] << 8 | rawData[3]);
	rawSensorData.z = (rawData[4] << 8 | rawData[5]);
	return rawSensorData;
}

SensorData AccelGyro::GetData(int dataRegister) {
	SensorData sensorData;
	const float* scaleModifiers = dataRegister == ACCEL_CONF_REG ? _accelScaleModifiers : _gyroScaleModifiers;
	RawSensorData rawSensorData = GetRawData(dataRegister);
	sensorData.x = rawSensorData.x / scaleModifiers[_accelSensitivityLevel];
	sensorData.y = rawSensorData.y / scaleModifiers[_accelSensitivityLevel];
	sensorData.z = rawSensorData.z / scaleModifiers[_accelSensitivityLevel];
	return sensorData;
}

Rotation AccelGyro::GetRotationAccel() {
	Rotation accelRotation;
	SensorData accelData = GetData(ACCEL_DATA_REG);
	accelData.x = accelData.x - _accelCorrection.x;
	accelData.y = accelData.y - _accelCorrection.y;
	accelData.z = accelData.z - _accelCorrection.z;
	accelRotation.x = atan2(accelData.y, sqrt(pow(accelData.x, 2) + pow(accelData.z, 2))) * RAD_TO_DEG;
	accelRotation.y = atan2(accelData.x, sqrt(pow(accelData.y, 2) + pow(accelData.z, 2))) * RAD_TO_DEG;
	accelRotation.z = 0;
	return accelRotation;
}

Rotation AccelGyro::GetRotationGyro() {
	Rotation gyroRotation;
	unsigned long currentTime, elapsedTime;
	SensorData gyroData = GetData(GYRO_DATA_REG);
	gyroData.x = gyroData.x - _gyroCorrection.x;
	gyroData.y = gyroData.y - _gyroCorrection.y;
	gyroData.z = gyroData.z - _gyroCorrection.z;
	currentTime = millis();
	elapsedTime = currentTime - _prevMeasurementTime;
	_rotationGyro.x += gyroData.x * elapsedTime;
	_rotationGyro.y += gyroData.y * elapsedTime;
	_rotationGyro.z += gyroData.z * elapsedTime;
	_prevMeasurementTime = currentTime;
	return _rotationGyro;
}

Rotation AccelGyro::GetRotation() {
	Rotation rotationAccel, rotationGyro;
	Rotation rotation;
	rotationAccel = GetRotationAccel();
	rotationGyro = GetRotationGyro();
	rotation.x = (1 - _compFilterAlpha) * rotationGyro.x + _compFilterAlpha * rotationAccel.x;
	rotation.y = (1 - _compFilterAlpha) * rotationGyro.y + _compFilterAlpha * rotationAccel.y;
	rotation.z = rotationGyro.z;
}

void AccelGyro::SetGyroSensitivity(int sensitivityLevel) {
	_gyroSensitivityLevel = sensitivityLevel;
	I2cWriteData(GYRO_CONF_REG, _gyroSensitivityLevel << 3);
}

void AccelGyro::SetAccelSensitivity(int sensitivityLevel) {
	_accelSensitivityLevel = sensitivityLevel;
	I2cWriteData(ACCEL_CONF_REG, _accelSensitivityLevel << 3);
}

void AccelGyro::SetAccelCorrection(Correction correction) {
	_accelCorrection.x = correction.x;
	_accelCorrection.y = correction.y;
	_accelCorrection.z = correction.z;
}

void AccelGyro::SetGyroCorrection(Correction correction) {
	_gyroCorrection.x = correction.x;
	_gyroCorrection.y = correction.y;
	_gyroCorrection.z = correction.z;
}

Correction AccelGyro::GetAccelCorrection() {
	return _accelCorrection;
}

Correction AccelGyro::GetGyroCorrection() {
	return _gyroCorrection;
}

void AccelGyro::Calibrate() {
	SensorData accelData, gyroData, totalAccelData, totalGyroData;
	totalAccelData.x = 0;
	totalAccelData.y = 0;
	totalAccelData.z = 0;
	totalGyroData.x = 0;
	totalGyroData.y = 0;
	totalGyroData.z = 0;
	_rotationGyro.x = 0;
	_rotationGyro.y = 0;
	_rotationGyro.z = 0;
	for (int i = 0; i < CALIBRATION_SAMPLES; i++) {
		accelData = GetData(ACCEL_DATA_REG);
		gyroData = GetData(GYRO_DATA_REG);
		totalAccelData.x += accelData.x;
		totalAccelData.y += accelData.y;
		totalAccelData.z += accelData.z;
		totalGyroData.x += gyroData.x;
		totalGyroData.y += gyroData.y;
		totalGyroData.z += gyroData.z;
	}
	_accelCorrection.x = totalAccelData.x / CALIBRATION_SAMPLES;
	_accelCorrection.y = totalAccelData.y / CALIBRATION_SAMPLES;
	_accelCorrection.z = (totalAccelData.z / CALIBRATION_SAMPLES) - 1;
	_gyroCorrection.x = totalGyroData.x / CALIBRATION_SAMPLES;
	_gyroCorrection.y = totalGyroData.y / CALIBRATION_SAMPLES;
	_gyroCorrection.z = totalGyroData.z / CALIBRATION_SAMPLES;
}