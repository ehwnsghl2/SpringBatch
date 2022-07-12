package com.brandjunhoe.springbatch.chunk

import com.brandjunhoe.springbatch.Member
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory


@Configuration
@EnableBatchProcessing
class JpaJobConfig(
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
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()


    @Bean
    @StepScope
    @Throws(Exception::class)
    fun reader(): JpaPagingItemReader<Member> {
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
    fun processor(): ItemProcessor<Member, Member> =
        ItemProcessor<Member, Member> { member ->
            member.amount = member.amount.plus(1000)
            member
        }

    @Bean
    @StepScope
    fun writer(): JpaItemWriter<Member> =
        JpaItemWriterBuilder<Member>().apply {
            entityManagerFactory?.let { entityManagerFactory(it) }
        }.build()


}
