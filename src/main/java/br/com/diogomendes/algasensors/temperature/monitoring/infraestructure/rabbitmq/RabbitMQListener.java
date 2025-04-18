package br.com.diogomendes.algasensors.temperature.monitoring.infraestructure.rabbitmq;

import br.com.diogomendes.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import br.com.diogomendes.algasensors.temperature.monitoring.domain.service.SensorAlertService;
import br.com.diogomendes.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static br.com.diogomendes.algasensors.temperature.monitoring.infraestructure.rabbitmq.RabbitMQConfig.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final SensorAlertService alertService;
    private final TemperatureMonitoringService service;

    @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
    @SneakyThrows
    public void handleProcessTemperature(@Payload TemperatureLogData temperatureLogData) {
        service.processTemperatureReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5));
    }

    @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
    @SneakyThrows
    public void handleAlerting(@Payload TemperatureLogData temperatureLogData) {
        alertService.handleAlert(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5));
    }

}
