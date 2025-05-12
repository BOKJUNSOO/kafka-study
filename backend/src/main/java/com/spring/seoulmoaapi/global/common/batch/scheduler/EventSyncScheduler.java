package com.spring.seoulmoaapi.global.common.batch.scheduler;

import com.spring.seoulmoaapi.global.common.batch.entity.BatchStatus;
import com.spring.seoulmoaapi.global.common.batch.repository.BatchStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSyncScheduler {

    private final JobLauncher jobLauncher;
    private final Job eventSyncJob;
    private final BatchStatusRepository batchStatusRepository;

    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul") // 매일 새벽 3시
    public void runEventSyncJob() {
        log.info("EventSyncBatch 실행 시작");
        LocalDateTime before = LocalDateTime.now();
        List<BatchStatus> latest = batchStatusRepository.findByStatusOrderByExecuteTimeDesc(PageRequest.of(0, 1));

        if (latest.isEmpty()) {
            log.info("아직 event_sync가 업데이트 되지 않았습니다");
            return;
        }

        BatchStatus batchStatus = latest.get(0);

        if (batchStatus.getStatus() != 0) {
            log.info("이미 실행되었으므로 종료합니다.");
            return;
        }

        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(eventSyncJob, params);

            // Batch Job 성공 여부 확인
            if (execution.getStatus().isUnsuccessful()) {
                log.error("Batch 실행 실패 → status 변경하지 않음");
                return;
            }

            // 성공한 경우만 status 업데이트
            batchStatus.setStatus(1);
            batchStatusRepository.save(batchStatus);

            log.info("EventSyncJob 성공");

        } catch (Exception e) {
            log.error("EventSyncJob 실패 : ", e);
        } finally {
            LocalDateTime after = LocalDateTime.now();
            Duration duration = Duration.between(before, after);
            log.info("EventSyncJob 종료 (총 소요 시간: {}초 {}ms)", duration.getSeconds(), duration.toMillisPart());
        }
    }
}