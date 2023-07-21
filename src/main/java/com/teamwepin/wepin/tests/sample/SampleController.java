package com.teamwepin.wepin.tests.sample;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "sample", description = "sample 관련 api")
@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/samples")
    @Operation(summary = "모든 sample 조회", description = "모든 sample 조회")
    public List<Sample> getSamples() {
        return sampleService.findSamples();
    }

    @PostMapping("/samples")
    @Operation(summary = "sample 저장", description = "sample 저장")
    public Long saveSample(@Parameter(description = "sample 상세 정보", required = true) @RequestBody Sample sample) {
        return sampleService.saveSample(sample);
    }

}
