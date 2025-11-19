package com.example.shop.service;

import com.example.shop.common.ResponseEntity;
import com.example.shop.member.Member;
import com.example.shop.member.MemberRepository;
import com.example.shop.member.MemberRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    // MemberRepository 를 주입 받아서 서비스 내에서 DB를 사용
    // private final -> 생성자로만 주입 가능하게 해서 불변성 보장, @Autowired 보다 생성자 주입이 더 좋은 구조
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 목록 조회
    public ResponseEntity<List<Member>> findAll() {
        List<Member> members = memberRepository.findAll(); // DB의 모든 member 조회
        int count = members.size(); // 리스트 크기
        // 응답객체 ResponseEntity 형식에 맞춰 반환
        return new ResponseEntity<>(HttpStatus.OK.value(), members, count);
    }

    // 회원 등록
    public ResponseEntity<Member> create(MemberRequest request) {
        // request DTO 받아서 엔티티로 변환
        Member member = new Member(
                UUID.randomUUID(), // PK는 UUID로 생성
                request.email(),
                request.name(),
                request.password(),
                request.phone(),
                request.saltKey(),
                request.flag()
        );

        Member saved = memberRepository.save(member); // JPA가 INSERT 쿼리 자동 생성, saved에는 DB에 실제 저장된 결과가 들어옴
        // 단건 저장이므로 count = 1, 등록과 수정은 결과로 하나의 데이터 객체 (Memeber 엔티티)가 생기기 때문에 1 리턴
        return new ResponseEntity<>(HttpStatus.OK.value(), saved, 1);
    }

    // 회원 수정
    // 회원 등록과 비교했을 때 차이 - PK가 있으면 UPDATE, PK가 없으면 INSERT
    public ResponseEntity<Member> update(String id, MemberRequest request) {
        Member member = new Member(
                UUID.fromString(id),
                request.email(),
                request.name(),
                request.password(),
                request.phone(),
                request.saltKey(),
                request.flag()
        );

        Member saved = memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.OK.value(), saved, 1);
    }

    // 회원 삭제
    // ResponseEntity<Void> - 응답 바디에 실제 데이터 안넣음
    public ResponseEntity<Void> delete(String id) {
        memberRepository.deleteById(UUID.fromString(id));
        // 삭제는 데이터가 없으니 null, 반환가능 엔티티도 없으니 0 리턴
        return new ResponseEntity<>(HttpStatus.OK.value(), null, 0);
    }
}
