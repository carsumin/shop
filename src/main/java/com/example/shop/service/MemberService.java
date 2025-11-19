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


    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 목록 조회
    public ResponseEntity<List<Member>> findAll() {
        List<Member> members = memberRepository.findAll();
        int count = members.size();
        return new ResponseEntity<>(HttpStatus.OK.value(), members, count);
    }

    // 회원 등록
    public ResponseEntity<Member> create(MemberRequest request) {
        Member member = new Member(
                UUID.randomUUID(),
                request.email(),
                request.name(),
                request.password(),
                request.phone(),
                request.saltKey(),
                request.flag()
        );

        Member saved = memberRepository.save(member);
        // 단건 저장이므로 count = 1
        return new ResponseEntity<>(HttpStatus.OK.value(), saved, 1);
    }

    // 회원 수정
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
    public ResponseEntity<Void> delete(String id) {
        memberRepository.deleteById(UUID.fromString(id));
        // 삭제는 데이터가 없으니 data = null, count = 0 으로 전달
        return new ResponseEntity<>(HttpStatus.OK.value(), null, 0);
    }
}
