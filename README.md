# heartrate-mvp

This project contains all relevant configurations that are needed for building Heart Rate MVP.

Client-equipment:
* Arduino UNO https://arduino.ua/ru/prod2610-arduino-uno-r3-ch340
* AD 8232 https://arduino.ua/ru/prod4139-modyl-ad8232-dlya-ekg
* Ethernet Shield W5100 https://arduino.ua/ru/prod391-w5100-ethernet-shield

Backend-equipment:
* Raspberry PI4 (or cloud-based solution)

Tech stack for server-side:
* MQTT - Hive MQ. Can be installed in [native mode] (https://link-url-here.org](https://docs.hivemq.com/hivemq/latest/user-guide/install-hivemq.html) or as a part of docker-compose (in emulation mode, but it affects performance)
* DB - Influx DB (part of docker-compose)
* Spring Boot
* Apache Camel

To visualize ECG in influx DB you can use this query:

```
from(bucket: "heartrate")
  |> range(start: 0)  // Start from 1 day ago, assuming your data is within this range
  |> filter(fn: (r) => r["_measurement"] == "heart_rate")
  |> filter(fn: (r) => r["_field"] == "amplitude")
  |> sort(columns: ["_time"])
```
