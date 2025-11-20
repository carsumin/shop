package com.example.shop.payment.client;

import com.example.shop.payment.application.dto.PaymentCommand;
import com.example.shop.payment.client.dto.TossPaymentResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TossPaymentClient {

    private static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final RestTemplate restTemplate;

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    // 토스에서 컨펌이 완료된 상태
    public TossPaymentResponse confirm(PaymentCommand command) {

        // secretKey가 null이면 예외 발생함으로, 예외처리 필수
        if (secretKey == null) {
            throw new IllegalStateException("Toss secret key is not configured");
        }

        // 토스에 요청할 Header를 만듦
        HttpHeaders headers = createHeaders();

        // body를 PaymentCommand를 사용하려면 현재 패키지인 client에 dto를 생성하고 복사해서 쓰는게 DDD 설계에 더 맞음
        // HttpEntity<PaymentCommand> entity = new HttpEntity<>(command, headers);
        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", command.paymentKey());
        body.put("orderId", command.orderId());
        body.put("amount", command.amount());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // 로그 확인하기 위해서 예외처리 해줌
        // 그냥 throw로 던지면 AOP에서 처리
        try {
            // 응답이 오는 형태 (토스에서 제공) 그대로 class로 넣어줌
            return restTemplate.postForObject(CONFIRM_URL, entity, TossPaymentResponse.class);
        } catch (HttpStatusCodeException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            String responseBody = ex.getResponseBodyAsString();
            throw new IllegalStateException("Toss confirm failed (" + statusCode + "): " + responseBody, ex);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders(); // 헤더 생성
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = secretKey + ":";
        // secretkey base64 Encode
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        // Authorization header에 인코딩 값 입력
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
        return headers;
    }
}
