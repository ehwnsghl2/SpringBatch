package com.brandjunhoe.springbatch.chunk

import com.brandjunhoe.springbatch.Member
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource


@Configuration
@EnableBatchProcessing
class JdbcJobConfig(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory,
) {

    val dataSource: DataSource? = null

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
    fun reader(): JdbcPagingItemReader<Member> {
        val parameterValues: MutableMap<String, Any> = HashMap()
        parameterValues["amount"] = "10000"

        return JdbcPagingItemReader<Member>().apply {
            pageSize = 10
            setFetchSize(10)
            dataSource?.let { setDataSource(it) }

            setRowMapper(BeanPropertyRowMapper())
            setQueryProvider(customQueryProvider())
            setParameterValues(parameterValues)


        }

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
    fun writer(): JdbcBatchItemWriter<Member> =
        JdbcBatchItemWriterBuilder<Member>().apply {
            dataSource?.let { dataSource(it) }
            sql("UPDATE MEMBER SET AMOUNT = :amount WHERE ID = :id")
        }.beanMapped().build()


    @Throws(Exception::class)
    fun customQueryProvider(): PagingQueryProvider =
        SqlPagingQueryProviderFactoryBean().apply {

            val sortKey: MutableMap<String, Order> = HashMap()

            sortKey["id"] = Order.ASCENDING

            dataSource?.let { setDataSource(it) }
            setSelectClause("SELECT ID, NAME, EMAIL, NICK_NAME, STATUS, AMOUNT ")
            setFromClause("FROM MEMBER ")
            setWhereClause("WHERE AMOUNT >= :amount")
            setSortKeys(sortKey)

        }.`object`


}
