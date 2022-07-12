package com.brandjunhoe.springbatch.tasklet

import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus


class BusinessTasklet : Tasklet, StepExecutionListener {


    override fun beforeStep(stepExecution: StepExecution) {
        println("Before Step Start!")
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus {
        println("After Step Start!")
        return ExitStatus.COMPLETED
    }

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {

        for (index in 0..9) {
            println("[index] = $index")
        }

        return RepeatStatus.FINISHED

    }

}