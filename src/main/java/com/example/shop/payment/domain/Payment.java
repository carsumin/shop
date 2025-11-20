package com.example.shop.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Entity
@Table(name = "\"payment\"", schema = "public")
public class Payment {

    @Id
    private UUID id;

    @Column(name = "payment_key", nullable = false, unique = true, length = 200)
    private String paymentKey;

    @Column(name = "order_id", nullable = false, length = 100)
    private String orderId;

    @Column(name = "total_amount", nullable = false)
    private Long amount;

    @Column(name = "method", length = 50)
    private String method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Payment() {
    }

    // 생성 로직
    private Payment(String paymentKey, String orderId, Long amount) {
        this.id = UUID.randomUUID();
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.READY; // 처음 생성될 때 상태는 READY (아직 토스 승인 완료 상태가 아님, 승인 전에 생성만 하는 개념)
    }

    public static Payment create(String paymentKey, String orderId, Long amount) {
        return new Payment(paymentKey, orderId, amount);
    }

    // 상태 전이 로직
    // markConfirmed - 토스에서 결제 승인 성공했을 때 호출함
    public void markConfirmed(String method, LocalDateTime approvedAt, LocalDateTime requestedAt) {
        this.status = PaymentStatus.CONFIRMED; // 토스에서 결제 승인 성공했을 때 상태
        this.method = method; // 카드, 간편결제 등
        this.approvedAt = approvedAt; // 승인시간
        this.requestedAt = requestedAt; // 요청시간
        this.failReason = null;
    }

    // markFailed - 실패 케이스 기록용
    public void markFailed(String failReason) {
        this.status = PaymentStatus.FAILED;
        this.failReason = failReason;
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
        createdAt = now;
        updatedAt = now;
        if (status == null) {
            status = PaymentStatus.READY;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
