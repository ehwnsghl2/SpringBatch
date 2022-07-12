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
class Job2Config(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun ExampleJob(): Job =
        jobBuilderFactory["exampleJob"]
            .start(startStep())
            .next(nextStep())
            .next(lastStep())
            .build()

    @Bean
    fun startStep(): Step =
        stepBuilderFactory.get("startStep")
            .tasklet { contribution, chunkContext ->
                println("Start Step!")
                RepeatStatus.FINISHED
            }
            .build()

    @Bean
    fun nextStep(): Step =
        stepBuilderFactory.get("nextStep")
            .tasklet { contribution, chunkContext ->
                println("Next Step!")
                RepeatStatus.FINISHED
            }
            .build()

    @Bean
    fun lastStep(): Step =
        stepBuilderFactory.get("lastStep")
            .tasklet { contribution, chunkContext ->
                println("Last Step!")
                RepeatStatus.FINISHED
            }
            .build()


}