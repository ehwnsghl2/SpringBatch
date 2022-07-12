package com.brandjunhoe.springbatch.tasklet

import com.brandjunhoe.springbatch.tasklet.BusinessTasklet
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class TaskletBusinessJobConfig(
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
            .tasklet(BusinessTasklet())
            .build()


}