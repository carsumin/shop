package com.example.shop.common;

import lombok.Data;

// @Data - getter 자동 생성
// 스프링의 공식 ResponseEntity(HTTP 응답)와 지금 ResponseEntity(커스텀 DTO)는 이름만 같지 완전히 다른 목적의 클래스
// 제네릭 클래스 - 어떤 타입이든 data로 받을 수 있음 (Member, List<Member>, Void 등)
@Data
public class ResponseEntity<T> {
    // 불변 필드
    private final int status; // HTTP 상태 코드 (200, 201, 400 등)
    private final T data; // 실제 응답 데이터 (Member, List<Member>, null 등)
    private final long count; // 반환 데이터 개수

    // setter 없으니 불변 객체, 응답이 중간에 바뀌지 않아서 안전함
    public ResponseEntity(int value, T all, long count) {
        this.status = value;
        this.data = all;
        this.count = count;
    }
}