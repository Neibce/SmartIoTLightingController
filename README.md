# SmartIoTLightingController

[English](./README_EN.md)

IoT 기반 절전 스마트 전등 시스템

2019년 2분기 개발 / 우송대학교 제1회 전국 고교 SW동아리 경진대회 2등 수상

## 개요

실내 조도와 인원수를 감지하여 전등을 자동으로 제어하는 IoT 시스템. 불필요한 전등 사용을 줄여 에너지를 절약하고, 스마트폰 앱으로 원격 제어 및 모니터링 가능.

## 기능

- 조도센서(CDS)로 실내 밝기에 따라 전등 개수 자동 조절
- 적외선 센서로 출입 인원 감지, 실내에 사람이 없으면 자동 소등
- 전류 센서(INA219)로 실시간 전력 사용량 측정
- 스마트폰 앱으로 상태 확인 및 수동 제어

## 시스템 구성

![시스템 구성도](https://github.com/Neibce/SmartLightingController/assets/18096595/2d6e1e09-381f-40c7-be49-4a78a26b2fb5)

| 구성요소 | 설명 |
|---------|------|
| NodeMCU (ESP8266) | 센서 데이터 수집 및 전등 제어 |
| 라즈베리파이 | Node.js TCP 소켓 릴레이 서버 |
| 안드로이드 앱 | 실시간 모니터링 및 원격 제어 |

## 하드웨어

- ESP8266 NodeMCU v3
- CDS 조도센서 (GL5537)
- 적외선 발광/수광부 (940nm)
- 전류 센서 (INA219)
- NPN 트랜지스터 (2N2222)
- White LED

## 스크린샷

| 회로 구성도 | 앱 화면 |
|:---:|:---:|
| ![회로 구성도](https://github.com/Neibce/SmartLightingController/assets/18096595/b69148f8-df2f-48ab-9683-f22e67eb392d) | ![앱 화면](https://github.com/Neibce/SmartLightingController/assets/18096595/479e4382-2dd3-4102-bee3-eede00262368) |

| 작동 모습 |  |  |
|:---:|:---:|:---:|
| ![1](https://github.com/Neibce/SmartLightingController/assets/18096595/747285d8-b730-4412-b859-f71f4c145ccf) | ![2](https://github.com/Neibce/SmartLightingController/assets/18096595/599a26bd-f621-40bc-a1d0-12c1640c9fdc) | ![3](https://github.com/Neibce/SmartLightingController/assets/18096595/10c980ab-e4ca-4d34-aa75-40eab1802a6b) |
| ![4](https://github.com/Neibce/SmartLightingController/assets/18096595/0155b78b-b3fa-4049-ba6c-5a3f4c712fb5) | ![5](https://github.com/Neibce/SmartLightingController/assets/18096595/d3e55886-404e-468b-b0e2-6bd6a89479f9) | |

## 사용 기술

- NodeMCU (Lua)
- Node.js (릴레이 서버)
- Android (Java)

## 프로젝트 구조

```
├── Android/          # 안드로이드 앱
├── NodeMCU/          # ESP8266 펌웨어 (Lua)
└── RelayServer/      # Node.js 릴레이 서버
```
