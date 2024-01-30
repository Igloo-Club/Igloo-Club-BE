package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.NungilStatus;
import com.iglooclub.nungil.dto.*;
import com.iglooclub.nungil.service.NungilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/nungil")
@RequiredArgsConstructor
public class NungilController {
    private final NungilService nungilService;
    @PostMapping("/recommend")
    public ResponseEntity<NungilResponse> recommendMember( Principal principal, @RequestBody ProfileRecommendRequest request){
        NungilResponse nungilResponse = nungilService.recommendMember(principal, request);
        if (nungilResponse == null){
            //추천할 사용자가 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(nungilResponse);
    }

    @GetMapping("/nungils")
    public ResponseEntity<Slice<NungilSliceResponse>> getNungilsByMemberAndStatus(Principal principal, @RequestBody NungilRequest request, @RequestParam NungilStatus status){
        Slice<NungilSliceResponse> nungilPageResponses = nungilService.getNungilSliceByMemberAndStatus(principal, request, status);
        return ResponseEntity.ok(nungilPageResponses);
    }


    @GetMapping("/detail")
    public ResponseEntity<NungilResponse> getNungilDetail(Principal principal, @RequestBody NungilDetailRequest request){
        NungilResponse nungilResponse = nungilService.getNungilDetail(request);
        return ResponseEntity.ok(nungilResponse);
    }



}
