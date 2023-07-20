package com.teamwepin.wepin.tests.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
