package com.heartrate.adapter.route;

import java.time.Instant;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.heartrate.dto.PointDto;
import com.heartrate.facade.HeartRateFacade;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

@Component
public class HeartRateRoute extends RouteBuilder {

    public void configure() {
        from("paho-mqtt5:{{mqtt.topic}}?brokerUrl={{mqtt.broker.url}}:{{mqtt.broker.port}}&clientId={{mqtt.client}}")
                .log("${body}")
                .unmarshal().json(PointDto.class)
                .process(exchange -> {
                    PointDto mqttMessage = exchange.getIn().getBody(PointDto.class);
                    Point point = Point
                            .measurement("heart_rate")
                            .addField("amplitude", mqttMessage.getAmplitude())
                            .time(Instant.ofEpochMilli(mqttMessage.getTime()), WritePrecision.MS); //TODO in the future we should have real time from Arduino board
                    exchange.getIn().setBody(point);
                })
        .to("influxdb2://connectionBean?org={{influx.org}}&bucket={{influx.bucket}}");
        
        restConfiguration()
                .component("jetty")
                .host("{{jetty.host}}")
                .port("{{jetty.port}}")
                .contextPath("/api");

        rest("/mock")
                .get("/run")
                .to("direct:mockUploadInterfaceRoute");

        from("direct:mockUploadInterfaceRoute")
                .bean(HeartRateFacade.class, "loadMock")
                .log("${body}")
                .split(body())
                .marshal().json()
                .to("paho-mqtt5:{{mqtt.topic}}?brokerUrl={{mqtt.broker.url}}:{{mqtt.broker.port}}");
    }

}