package com.brandjunhoe.springbatch.tasklet

import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class Job3Config(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun ExampleJob(): Job {
        return jobBuilderFactory["exampleJob"]
            .start(startStep())
            .on("FAILED")
            .to(failOverStep()) // failOver Step을 실행 시킨다.
            .on("*") // failOver Step의 결과와 상관없이
            .to(writeStep()) // write Step을 실행 시킨다.
            .on("*") // write Step의 결과와 상관없 이
            .end() // Flow를 종료시킨다.

            .from(startStep()) //startStep이 FAILED가 아니고
            .on("COMPLETED") // COMPLETED일 경우
            .to(processStep()) // process Step을 실행 시킨다
            .on("*") // process Step의 결과와 상관없이
            .to(writeStep()) // write Step을 실행 시킨다.
            .on("*") // wrtie Step의 결과와 상관없이
            .end() // Flow를 종료 시킨다.

            .from(startStep()) // startStep의 결과가 FAILED, COMPLETED가 아닌
            .on("*") // 모든 경우
            .to(writeStep()) // write Step을 실행시킨다.
            .on("*") // write Step의 결과와 상관없이
            .end() // Flow를 종료시킨다.

            .end()
            .build()
    }

    @Bean
    fun startStep(): Step {
        return stepBuilderFactory["startStep"]
            .tasklet { contribution: StepContribution, chunkContext: ChunkContext ->
                println("Start Step!")
                val result = "COMPLETED"
                //String result = "FAIL";
                //String result = "UNKNOWN";

                //Flow에서 on은 RepeatStatus가 아닌 ExitStatus를 바라본다.
                when (result) {
                    "COMPLETED" -> contribution.exitStatus = ExitStatus.COMPLETED
                    "FAIL" -> contribution.exitStatus = ExitStatus.FAILED
                    "UNKNOWN" -> contribution.exitStatus = ExitStatus.UNKNOWN
                }
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun failOverStep(): Step {
        return stepBuilderFactory["nextStep"]
            .tasklet { contribution: StepContribution, chunkContext: ChunkContext ->
                println("FailOver Step!")
                RepeatStatus.FINISHED
            }
            .build()
    }

    @Bean
    fun processStep(): Step {
        return stepBuilderFactory["processStep"]
            .tasklet { contribution: StepContribution, chunkContext: ChunkContext ->
                println("Process Step!")
                RepeatStatus.FINISHED
            }
            .build()
    }


    @Bean
    fun writeStep(): Step {
        return stepBuilderFactory["writeStep"]
            .tasklet { contribution: StepContribution, chunkContext: ChunkContext ->
                println("Write Step!")
                RepeatStatus.FINISHED
            }
            .build()
    }


}