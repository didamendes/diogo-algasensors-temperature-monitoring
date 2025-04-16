package br.com.diogomendes.algasensors.temperature.monitoring.domain.repository;

import br.com.diogomendes.algasensors.temperature.monitoring.domain.model.SensorAlert;
import br.com.diogomendes.algasensors.temperature.monitoring.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorAlertRepository extends JpaRepository<SensorAlert, SensorId> {
}
