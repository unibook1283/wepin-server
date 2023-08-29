package com.teamwepin.wepin.global.response;

import com.teamwepin.wepin.global.response.dto.ListResult;
import com.teamwepin.wepin.global.response.dto.SingleResult;
import com.teamwepin.wepin.global.response.dto.SuccessResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    // 성공 결과
    public SuccessResult getSuccessResult() {
        return new SuccessResult();
    }

    // 단일 결과
    public <T> SingleResult<T> getSingleResult(T data) {
        return new SingleResult<>(data);
    }

    // 복수 결과
    public <T> ListResult<T> getListResult(List<T> data) {
        return new ListResult<>(data);
    }

}
