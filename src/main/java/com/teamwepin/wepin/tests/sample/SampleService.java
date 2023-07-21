package com.teamwepin.wepin.tests.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    @Transactional
    public Long saveSample(Sample sample) {
        sampleRepository.save(sample);
        return sample.getId();
    }

    @Transactional(readOnly = true)
    public List<Sample> findSamples() {
        return sampleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Sample findOne(Long sampleId) {
        return sampleRepository.findById(sampleId)
                .orElseThrow(SampleNotFoundException::new);
    }

}
