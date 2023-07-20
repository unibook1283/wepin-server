package com.teamwepin.wepin.tests.sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SampleServiceTest {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private SampleRepository sampleRepository;

    @BeforeEach
    void setupDatabase() {
        Sample sample = new Sample();
        sample.setName("qwer");
        sampleRepository.save(sample);
    }

    @Test
    void saveSample() {
        Sample sample = new Sample();
        sample.setName("asdf");
        Long sampleId = sampleService.saveSample(sample);

        Optional<Sample> optionalSample = sampleRepository.findById(sampleId);

        assertThat(optionalSample)
                .isNotEmpty();
    }

    @Test
    void findSamples() {
        List<Sample> samples = sampleService.findSamples();

        assertThat(samples)
                .hasSize(1);
    }

    @Test
    void findOne() {
    }
}