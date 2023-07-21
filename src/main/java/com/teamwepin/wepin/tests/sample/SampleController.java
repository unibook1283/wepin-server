package com.teamwepin.wepin.tests.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/samples")
    public List<Sample> getSamples() {
        return sampleService.findSamples();
    }

    @PostMapping("/samples")
    public Long saveSample(@RequestBody Sample sample) {
        return sampleService.saveSample(sample);
    }

    @GetMapping("/samples/{sampleId}")
    public Sample getSample(@PathVariable Long sampleId) {
        return sampleService.findOne(sampleId);
    }

}
