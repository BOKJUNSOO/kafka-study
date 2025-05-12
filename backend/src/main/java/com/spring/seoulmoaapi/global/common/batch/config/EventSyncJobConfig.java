package com.spring.seoulmoaapi.global.common.batch.config;

import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.global.common.batch.dto.EventSync;
import com.spring.seoulmoaapi.global.common.batch.processor.EventSyncProcessor;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class EventSyncJobConfig {

    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;
    private final EventSyncProcessor processor;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public JdbcCursorItemReader<EventSync> eventSyncReader() {
        return new JdbcCursorItemReaderBuilder<EventSync>()
                .name("eventSyncReader")
                .dataSource(dataSource)
                .sql("SELECT * FROM datawarehouse.event_sync")
                .rowMapper(new org.springframework.jdbc.core.BeanPropertyRowMapper<>(EventSync.class))
                .build();
    }

    @Bean
    public JpaItemWriter<Event> eventWriter() {
        return new JpaItemWriterBuilder<Event>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true) //JpaItemWriter 는 기본적으로 persist 가 아니라 merge 를 씁니다.  persist → 완전 새로운 것만 처리  merge → 식별자가 있으면 select 후 update / 없으면 insert  -> Builder 로 생성하고 id 없이 주면 "식별자가 null" 상태라 persist로 가야 하는데, JPA 내부에서는 이걸 merge로 타버릴 수 있음 → insert가 안 됨 (무시됨) -> 관련설정문
                .build();
    }

    @Bean
    public Step eventSyncStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("eventSyncStep", jobRepository)
                .<EventSync, Event>chunk(1000, transactionManager)
                .reader(eventSyncReader())
                .processor(processor)
                .writer(eventWriter())
                .build();
    }

    @Bean
    public Job eventSyncJob(JobRepository jobRepository, Step eventSyncStep, Step updateCategoryIdStep) {
        return new JobBuilder("eventSyncJob", jobRepository)
                .start(eventSyncStep)
                .next(updateCategoryIdStep)
                .build();
    }

    @Bean
    public Step updateCategoryIdStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("updateCategoryIdStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    jdbcTemplate.update("""
                    UPDATE datawarehouse."event" e
                    SET category_id = c.category_id
                    FROM datawarehouse.category c
                    WHERE e.category_name = c.name
                """);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}