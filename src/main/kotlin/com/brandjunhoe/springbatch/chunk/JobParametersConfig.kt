package com.brandjunhoe.springbatch.chunk

import com.brandjunhoe.springbatch.Member
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException
import org.springframework.batch.core.repository.JobRestartException
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.EntityManagerFactory

@Configuration
@EnableBatchProcessing
class JobParametersConfig(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory,
) {

    val entityManagerFactory: EntityManagerFactory? = null

    @Bean
    @Throws(Exception::class)
    fun ExampleJob(): Job =
        jobBuilderFactory["exampleJob"]
            .start(Step())
            .build()


    @Bean
    @JobScope
    @Throws(Exception::class)
    fun Step(): Step =
        stepBuilderFactory["Step"]
            .chunk<Member, Member>(10)
            .reader(reader(null))
            .processor(processor(null))
            .writer(writer(null))
            .build()


    @Bean
    @StepScope
    @Throws(Exception::class)
    fun reader(@Value("#{jobParameters[date]}") date: String?): JpaPagingItemReader<Member> {

        println("jobParameters value : $date")

        val parameterValues: MutableMap<String, Any> = HashMap()
        parameterValues["amount"] = "10000"

        return JpaPagingItemReaderBuilder<Member>().apply {
            pageSize(10)
            parameterValues(parameterValues)
            queryString("SELECT p FROM Member p WHERE p.amount >= :amount ORDER BY id ASC")
            entityManagerFactory?.let { entityManagerFactory(it) }
            name("JpaPagingItemReader")
        }.build()

    }

    @Bean
    @StepScope
    fun processor(@Value("#{jobParameters[date]}") date: String?): ItemProcessor<Member, Member> =
        ItemProcessor<Member, Member> { member ->
            member.amount = member.amount.plus(1000)
            member
        }

    @Bean
    @StepScope
    fun writer(@Value("#{jobParameters[date]}") date: String?): JpaItemWriter<Member> =
        JpaItemWriterBuilder<Member>().apply {
            entityManagerFactory?.let { entityManagerFactory(it) }
        }.build()


}
