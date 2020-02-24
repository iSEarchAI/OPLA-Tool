package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.opla.entity.metric.SvcMetric;
import br.ufpr.dinf.gres.persistence.repository.SvcMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class SvcMetricService extends BaseService<SvcMetric> {

    public SvcMetricService(SvcMetricRepository repository) {
        super(repository);
    }
}