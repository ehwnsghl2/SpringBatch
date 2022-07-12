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
class TaskletJobConfig(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun ExampleJob(): Job =
        jobBuilderFactory["taskletJob"]
            .start(TaskStep())
            .build()

    @Bean
    fun TaskStep(): Step =
        stepBuilderFactory.get("taskletStep")
            .tasklet { contribution, chunkContext ->

                //비즈니스 로직
                for (index in 0..9) {
                    println("[index] = $index")
                }

                RepeatStatus.FINISHED
            }
            .build()


}