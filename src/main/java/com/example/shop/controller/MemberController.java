package com.example.shop.controller;

import com.example.shop.common.ResponseEntity;
import com.example.shop.member.Member;
import com.example.shop.member.MemberRequest;
import com.example.shop.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

// @RestController - JSON 기반 API 제공하는 컨트롤러, 모든 메서드가 자동으로 JSON 응답
// @RequestMapping("${api.v1}/members") - 모든 요청에 기본 URL prefix 제공, api.v1 = /api/v1 이면 /api/v1/members
@RestController
@RequestMapping("${api.v1}/members")
public class MemberController {

    // 컨트롤러는 Repository를 직접 쓰지 않음 (Controller -> Service -> Repository)
    // 컨트롤러는 오직 HTTP 요청/응답만 처리, 비즈니스 로직 절대 X

    // 생성자 주입
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 목록 조회
    @Operation(
            summary = "회원 목록 조회",
            description = "public.member 테이블에 저장된 모든 회원을 조회한다."
    )
    @GetMapping
    public ResponseEntity<List<Member>> findAll() {
        return memberService.findAll(); // Repository 통해 DB 조회
    }

    // 회원 등록
    @Operation(
            summary = "회원 등록",
            description = "요청으로 받은 회원 정보를 public.member 테이블에 저장한다."
    )
    @PostMapping
    public ResponseEntity<Member> create(@RequestBody MemberRequest request) { // 클라이언트가 보내는 JSON을 MemberRequest DTO에 매핑
        return memberService.create(request);
    }

    // 회원 수정
    @Operation(
            summary = "회원 수정",
            description = "요청으로 받은 회원 정보를 public.member 테이블에 수정한다."
    )
    @PutMapping("{id}")
    public ResponseEntity<Member> update(@RequestBody MemberRequest request,
                                         @PathVariable String id) { // URL의 "{id}" 값을 String으로 받아옴
        // 받아온 id값을 서비스에서 UUID로 변환
        return memberService.update(id, request);
    }

    // 회원 삭제
    @Operation(
            summary = "회원 정보 삭제",
            description = "요청으로 받은 회원 정보를 public.member 테이블에서 삭제한다."
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        return memberService.delete(id);
    }

}
