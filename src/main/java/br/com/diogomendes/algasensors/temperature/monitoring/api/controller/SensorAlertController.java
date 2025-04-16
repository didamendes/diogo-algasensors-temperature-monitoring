package br.com.diogomendes.algasensors.temperature.monitoring.api.controller;

import br.com.diogomendes.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import br.com.diogomendes.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import br.com.diogomendes.algasensors.temperature.monitoring.domain.model.SensorAlert;
import br.com.diogomendes.algasensors.temperature.monitoring.domain.model.SensorId;
import br.com.diogomendes.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {

    private final SensorAlertRepository repository;

    @GetMapping
    public SensorAlertOutput getDetail(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = findById(sensorId);

        return getSensorAlertOutput(sensorAlert);
    }

    @PutMapping
    public SensorAlertOutput createOrUpdate(@PathVariable TSID sensorId,
                                            @RequestBody SensorAlertInput input) {
        SensorAlert sensorAlert = findByIdOrDefault(sensorId);
        sensorAlert.setMinTemperature(input.getMinTemperature());
        sensorAlert.setMaxTemperature(input.getMaxTemperature());
        repository.saveAndFlush(sensorAlert);

        return getSensorAlertOutput(sensorAlert);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = findById(sensorId);
        repository.delete(sensorAlert);
    }

    private static SensorAlertOutput getSensorAlertOutput(SensorAlert sensorAlert) {
        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .minTemperature(sensorAlert.getMinTemperature())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .build();
    }

    private SensorAlert findById(TSID sensorId) {
        return repository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    private SensorAlert findByIdOrDefault(TSID sensorId) {
        return repository.findById(new SensorId(sensorId))
                .orElse(SensorAlert.builder()
                        .id(new SensorId(sensorId))
                        .minTemperature(null)
                        .maxTemperature(null)
                        .build());
    }

}
