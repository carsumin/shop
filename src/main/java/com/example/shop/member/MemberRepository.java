package com.example.shop.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// @Repository - DB 접근 객체임을 알려주는 어노테이션, 생략해도 작동함, 붙이는게 일반적
// extends JpaRepository<Member, UUID> - Member 엔티티를 다룬다, PK타입은 UUID, 이 조합에 맞는 CRUD 구현 자동 제공
// MemberRepository가 자동으로 제공하는 기능들 - 기본 CRUD, 자동 쿼리 생성 등
@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

}