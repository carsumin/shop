package com.example.shop.settlement.presentation;

//import com.example.shop.common.ResponseEntity;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST 엔드포인트로 정산 배치를 수동 실행하는 컨트롤러.
 * /run/all은 전체 판매자, /run/seller는 특정 판매자만 처리한다.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class SettlementJobController {

    private final JobLauncher jobLauncher;
    private final Job sellerSettlementJob;

    @PostMapping("/run/all")
    public ResponseEntity<String> runAll() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis()) // 같은값이 두번 던져지면 잡이 실행이 안되기 때문에 타임스탬프 넣어줘야함
                .toJobParameters();
        jobLauncher.run(sellerSettlementJob, params);
        return ResponseEntity.ok("Settlement job (all sellers) started");
    }

    @PostMapping("/run/seller")
    public ResponseEntity<String> runSeller(@RequestParam("sellerId") String sellerId) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("sellerId", sellerId)
                .toJobParameters();
        jobLauncher.run(sellerSettlementJob, params);
        return ResponseEntity.ok("Settlement job started for seller " + sellerId);
    }

}
