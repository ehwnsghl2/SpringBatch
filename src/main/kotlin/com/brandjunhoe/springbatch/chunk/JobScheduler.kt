package com.brandjunhoe.springbatch.chunk

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException
import org.springframework.batch.core.repository.JobRestartException
import java.text.SimpleDateFormat
import java.util.*

class JobScheduler(
    val jobLauncher: JobLauncher,
    val job: Job
) {

    @Throws(
        JobParametersInvalidException::class,
        JobExecutionAlreadyRunningException::class,
        JobRestartException::class,
        JobInstanceAlreadyCompleteException::class
    )
    fun jobSchdueled() {

        val jobParametersMap: MutableMap<String, JobParameter> = HashMap()
        val format1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
        val time = Date()
        val time1 = format1.format(time)
        jobParametersMap["date"] = JobParameter(time1)

        val jobParameters = JobParameters(jobParametersMap)
        val jobExecution = jobLauncher.run(job, jobParameters)

        while (jobExecution.isRunning) {
            println("...")
        }

        println("Job Execution: " + jobExecution.status)
        println("Job getJobConfigurationName: " + jobExecution.jobConfigurationName)
        println("Job getJobId: " + jobExecution.jobId)
        println("Job getExitStatus: " + jobExecution.exitStatus)
        println("Job getJobInstance: " + jobExecution.jobInstance)
        println("Job getStepExecutions: " + jobExecution.stepExecutions)
        println("Job getLastUpdated: " + jobExecution.lastUpdated)
        println("Job getFailureExceptions: " + jobExecution.failureExceptions)

    }

}