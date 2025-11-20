package com.example.shop.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Schema(description = "유저 정보")
@Data
@Entity
@Table(name = "\"member\"", schema = "public")
public class Member {

    @Schema(description = "유저의 UUID")
    @Id
    private UUID id;

    @Schema(description = "유저의 email")
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Schema(description = "유저명")
    @Column(name = "\"name\"", length = 20) // Postgre 에서 생성할때 "name"으로 생성했기 때문에
    private String name;

    @Schema(description = "비밀번호")
    @Column(name = "\"password\"", nullable = false, length = 100)
    private String password;

    @Schema(description = "핸드폰번호")
    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Schema(description = "등록자")
    @Column(name = "reg_id", nullable = false)
    private UUID regId;

    @Schema(description = "등록일시")
    @Column(name = "reg_dt", nullable = false)
    private LocalDateTime regDt;

    @Schema(description = "수정자")
    @Column(name = "modify_id", nullable = false)
    private UUID modifyId;

    @Schema(description = "수정일시")
    @Column(name = "modify_dt", nullable = false)
    private LocalDateTime modifyDt;

    @Column(name = "saltkey", nullable = false, length = 14)
    private String saltKey;

    @Schema(description = "사용자상태")
    @Column(name = "flag", length = 20)
    private String flag;

    // 생성자 오버로딩
    // 1. 기본 생성자 필수 - JPA가 리플렉션으로 객체를 만들 때 필요
    public Member(){}

    // 2. 외부에서 이미 UUID 타입으로 id를 가지고 있을 때 사용
    private Member(UUID id,
                  String email,
                  String name,
                  String password,
                  String phone,
                  String saltKey,
                  String flag) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.saltKey = saltKey;
        this.flag = flag;
    }

    public static Member create(String email,
                                String name,
                                String password,
                                String phone,
                                String saltKey,
                                String flag) {
        return new Member(UUID.randomUUID(), email, name, password, phone, saltKey, flag);
    }

    public void updateInformation(String email,
                                  String name,
                                  String password,
                                  String phone,
                                  String saltKey,
                                  String flag) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.saltKey = saltKey;
        this.flag = flag;
    }

    // 생명주기 콜백 - INSERT 전에 자동 호출
    // insert 전에 필요한 값들 자동으로 세팅해서 null 안들어가게 하는 역할
    @PrePersist
    public void prePersist() {
        if (regId == null) {
            // id 이미 세팅되어있으면 그거 쓰고 아니면 UUID 생성
            regId = id != null ? id : UUID.randomUUID();
        }
        if (modifyId == null) {
            // 없으면 regId씀 (처음 등록자가 수정자도 됨)
            modifyId = regId;
        }
        if (regDt == null) {
            // 비어있으면 현재시간
            regDt = LocalDateTime.now();
        }
        if (modifyDt == null) {
            // 비어있으면 현재시간
            modifyDt = LocalDateTime.now();
        }
        if (id == null) {
            //id 비어있으면 UUID 생성
            id = UUID.randomUUID();
        }
    }

    // UPDATE 전에 자동 호출
    @PreUpdate
    public void preUpdate() {
        modifyDt = LocalDateTime.now(); // 항상 최신 수정 시간으로 덮어씀
        if (modifyId == null) {
            // 비어 있으면 현재 유저 id로 세팅
            modifyId = id;
        }
    }

}
