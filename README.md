# heartrate-mvp

This project contains all relevant configurations that are needed for building Heart Rate MVP.

Client-equipment:
* Arduino UNO https://arduino.ua/ru/prod2610-arduino-uno-r3-ch340
* AD 8232 https://arduino.ua/ru/prod4139-modyl-ad8232-dlya-ekg
* Ethernet Shield https://arduino.ua/ru/prod391-w5100-ethernet-shield

Backend-equipment:
* Raspberry PI4 (or cloud-based solution)

Tech stack for server-side:
* MQTT - Hive MQ. Can be installed in native mode https://docs.hivemq.com/hivemq/latest/user-guide/install-hivemq.html or as a part of docker-compose (in emulation mode, but it affects performance)
* DB - Influx DB (part of docker-compose)
* Spring Boot
* Apache Camel
