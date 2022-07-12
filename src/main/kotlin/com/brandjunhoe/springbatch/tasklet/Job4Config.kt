package com.brandjunhoe.springbatch.tasklet

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Member
import java.sql.SQLException


@Configuration
@EnableBatchProcessing
class Job4Config(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    @JobScope
    @Throws(Exception::class)
    fun StepSkip(): Step {
        return stepBuilderFactory["Step"]
            .startLimit(3) // 해당 Step이 실패 이후 재 시작 가능 횟수. 이후 실행에서는 Exception 발생
            .chunk<Member, Member>(10)
            .reader(reader(null))
            .processor(processor(null))
            .writer(writer(null))
            .faultTolerant()
            .skipLimit(1) // skip 허용 횟수, 해당 횟수 초과시 Error 발생, Skip 사용시 필수 설정
            .skip(NullPointerException::class) // NullPointerException에 대해선 Skip
            .noSkip(SQLException::class) // SQLException 대해선 noSkip
            //.skipPolicy(CustomSkipPolilcy()) // 사용자가 커스텀하여 Skip Policy 가능
            .build()
    }

    @Bean
    @JobScope
    @Throws(Exception::class)
    fun StepRetry(): Step {
        return stepBuilderFactory["Step"]
            .startLimit(3) // 해당 Step이 실패 이후 재 시작 가능 횟수. 이후 실행에서는 Exception 발생
            .chunk<Member, Member>(10)
            .reader(reader(null))
            .processor(processor(null))
            .writer(writer(null))
            .faultTolerant()
            .retryLimit(1) // retry 횟수, retry 사용시 필수 설정, 해당 Retry 이후 Exception시 Fail 처리
            .retry(SQLException::class) // SQLException에 대해선 Retry 수행
            .noRetry(NullPointerException::class) // NullPointerException에 no Retry
            //.retryPolicy(CustomSkipPolilcy()) // 사용자가 커스텀하여 Retry Policy 가능
            .build()
    }

    @Bean
    @JobScope
    @Throws(Exception::class)
    fun StepNoRollback(): Step {
        return stepBuilderFactory["Step"]
            .startLimit(3) // 해당 Step이 실패 이후 재 시작 가능 횟수. 이후 실행에서는 Exception 발생
            .chunk<Member, Member>(10)
            .reader(reader(null))
            .processor(processor(null))
            .writer(writer(null))
            .faultTolerant()
            .noRollback(NullPointerException::class) // NullPointerException 발생  rollback이 되지 않게 설정
            .build()
    }


}