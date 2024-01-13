# heartrate-mvp

This project contains all relevant configurations that are needed for building Heart Rate MVP.

Client-equipment:
* Arduino UNO https://arduino.ua/ru/prod2610-arduino-uno-r3-ch340
* AD 8232 https://arduino.ua/ru/prod4139-modyl-ad8232-dlya-ekg
* Ethernet Shield W5100 https://arduino.ua/ru/prod391-w5100-ethernet-shield

Backend-equipment:
* Raspberry PI4 (or cloud-based solution)

Tech stack for server side:
* MQTT - **Hive MQ**. Can be installed in native mode (https://docs.hivemq.com/hivemq/latest/user-guide/install-hivemq.html) or as a part of docker-compose (in emulation mode, but it affects performance)
* DB - **Influx DB** (part of existing docker-compose)
* Spring Boot
* Apache Camel

Pre-requisites that must be installed on server side:
* Java 17
* Maven 3.9.3
* Docker (https://docs.docker.com/engine/install/)

Steps to run BE service:
* clone git repository
* build project
  ```
  mvn clean package
  ```
* run the application from IDEA or via command
  ```
  java -jar target/heart-rate-0.0.1.jar
  ```
  or use mvn plugin
  ```
  mvn spring-boot:run
  ```
  or build a docker image
  ```
  sudo docker build --tag be-service .
  ```
  and remove comments in the docker-compose file. In this case BE service will start as a part of docker compose
  
  To start docker-compose run:
  ```
  docker compose up
  ```

To visualize ECG in influx DB you can use this query:

```
from(bucket: "heartrate")
  |> range(start: 0)  // Start from 1 day ago, assuming your data is within this range
  |> filter(fn: (r) => r["_measurement"] == "heart_rate")
  |> filter(fn: (r) => r["_field"] == "amplitude")
  |> sort(columns: ["_time"])
```
