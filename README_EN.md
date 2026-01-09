# SmartIoTLightingController

[한국어](./README.md)

IoT-based Smart Lighting System for Energy Saving

Developed in Q2 2019 / 2nd Place at Woosong University 1st National High School SW Club Competition

## Overview

An IoT system that automatically controls lighting based on indoor brightness and occupancy. Reduces unnecessary energy consumption and allows remote monitoring/control via smartphone app.

## Features

- Auto-adjusts number of lights based on ambient brightness (CDS sensor)
- Detects people entering/exiting with IR sensors, turns off lights when room is empty
- Measures real-time power consumption (INA219 current sensor)
- Remote monitoring and manual control via smartphone app

## System Architecture

![System Architecture](https://github.com/Neibce/SmartLightingController/assets/18096595/2d6e1e09-381f-40c7-be49-4a78a26b2fb5)

| Component | Description |
|-----------|-------------|
| NodeMCU (ESP8266) | Sensor data collection & light control |
| Raspberry Pi | Node.js TCP socket relay server |
| Android App | Real-time monitoring & remote control |

## Hardware

- ESP8266 NodeMCU v3
- CDS Light Sensor (GL5537)
- IR Transmitter/Receiver (940nm)
- Current Sensor (INA219)
- NPN Transistor (2N2222)
- White LED

## Screenshots

| Circuit Diagram | App UI |
|:---:|:---:|
| ![Circuit](https://github.com/Neibce/SmartLightingController/assets/18096595/b69148f8-df2f-48ab-9683-f22e67eb392d) | ![App](https://github.com/Neibce/SmartLightingController/assets/18096595/479e4382-2dd3-4102-bee3-eede00262368) |

| Demo |  |  |
|:---:|:---:|:---:|
| ![1](https://github.com/Neibce/SmartLightingController/assets/18096595/747285d8-b730-4412-b859-f71f4c145ccf) | ![2](https://github.com/Neibce/SmartLightingController/assets/18096595/599a26bd-f621-40bc-a1d0-12c1640c9fdc) | ![3](https://github.com/Neibce/SmartLightingController/assets/18096595/10c980ab-e4ca-4d34-aa75-40eab1802a6b) |
| ![4](https://github.com/Neibce/SmartLightingController/assets/18096595/0155b78b-b3fa-4049-ba6c-5a3f4c712fb5) | ![5](https://github.com/Neibce/SmartLightingController/assets/18096595/d3e55886-404e-468b-b0e2-6bd6a89479f9) | |

## Tech Stack

- NodeMCU (Lua)
- Node.js (Relay Server)
- Android (Java)

## Project Structure

```
├── Android/          # Android app
├── NodeMCU/          # ESP8266 firmware (Lua)
└── RelayServer/      # Node.js relay server
```
