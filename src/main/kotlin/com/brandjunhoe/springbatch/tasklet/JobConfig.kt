package com.brandjunhoe.springbatch.tasklet

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class JobConfig(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun ExampleJob(): Job =
        jobBuilderFactory["exampleJob"]
            .start(Step())
            .build()

    @Bean
    fun Step(): Step =
        stepBuilderFactory.get("step")
            .tasklet { contribution, chunkContext ->
                println("Step!")
                RepeatStatus.FINISHED
            }
            .build()

}