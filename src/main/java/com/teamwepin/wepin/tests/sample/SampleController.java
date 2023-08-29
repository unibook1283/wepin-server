package com.teamwepin.wepin.tests.sample;

import com.teamwepin.wepin.global.response.ResponseService;
import com.teamwepin.wepin.global.response.dto.ListResult;
import com.teamwepin.wepin.global.response.dto.SingleResult;
import com.teamwepin.wepin.global.response.dto.SuccessResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "sample", description = "sample 관련 api")
@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;
    private final ResponseService responseService;

    @GetMapping("/samples")
    @Operation(summary = "모든 sample 조회", description = "모든 sample 조회")
    public ListResult<Sample> getSamples() {
        return responseService.getListResult(sampleService.findSamples());
    }

    @PostMapping("/samples")
    @Operation(summary = "sample 저장", description = "sample 저장")
    public SuccessResult saveSample(@Parameter(description = "sample 상세 정보", required = true) @RequestBody Sample sample) {
        sampleService.saveSample(sample);
        return responseService.getSuccessResult();
    }

    @GetMapping("/samples/{sampleId}")
    public SingleResult<Sample> getSample(@PathVariable Long sampleId) {
        return responseService.getSingleResult(sampleService.findOne(sampleId));
    }

}
