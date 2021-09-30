/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.store.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.hibernate.search.jpa.FullTextEntityManager
import org.hibernate.search.jpa.Search
import org.kebs.app.kotlin.apollo.config.properties.jpa.JpaConnectionProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.persistence.EntityManager
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = ["org.kebs.app.kotlin.apollo.store.repo"],
        entityManagerFactoryRef = "entityManagerFactoryBean"
)

class HibernateJpaConfiguration(private val jcp: JpaConnectionProperties) {

    @Bean
    fun exceptionTranslation(): PersistenceExceptionTranslationPostProcessor {
        return PersistenceExceptionTranslationPostProcessor()
    }

    private fun hibernateAdditionalProperties(): Properties {
        val results = Properties()
        results.setProperty("hibernate.dialect", jcp.dialect)
        results.setProperty("hibernate.hbm2ddl.auto", jcp.hbm2ddlAuto)
        results.setProperty("hibernate.show_sql", jcp.showSql)
        results.setProperty("hibernate.format_sql", jcp.showSql)
        results.setProperty("hibernate.use_sql_comments", jcp.showSql)
        results.setProperty("hibernate.generate_statistics", jcp.generateStatistics)
        results.setProperty("hibernate.id.new_generator_mappings", jcp.hibernateIdNewgeneratorMappings)
        results.setProperty("hibernate.jdbc.lob.non_contextual_creation", jcp.jdbcLobNonContextualCreation)
        results.setProperty("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults", "false")
        return results
    }

    @Bean(name = ["datasource"])
    @Primary
    fun dataSource(): DataSource {


        val config = HikariConfig()
        var hc = HikariDataSource()
        try {
            config.jdbcUrl = jcp.jdbcUrl
            config.username = jcp.jdbcUser
            config.password = jcp.jdbcPass
//            config.schema = jcp.jdbcSchema

            config.connectionTimeout = jcp.hikariConnectionTimeout
            config.idleTimeout = jcp.hikariIdleTimeout
            config.maxLifetime = jcp.hikariMaxLifetime
            config.validationTimeout = jcp.hikariValidationTimeout
            jcp.minimumIdle?.let { config.minimumIdle = it }
            config.maximumPoolSize = jcp.hikariMaximumPoolSize
            config.poolName = jcp.hikariPoolName
//            config.leakDetectionThreshold = jcp.hikariLeakDetectionThreshold
            config.addDataSourceProperty("cachePrepStmts", jcp.cachePrepStmts)
            config.addDataSourceProperty("prepStmtCacheSize", jcp.prepStmtCacheSize)
            config.addDataSourceProperty("prepStmtCacheSqlLimit", jcp.prepStmtCacheSqllimit)

            hc = HikariDataSource(config)
        } catch (e: Exception) {

            KotlinLogging.logger { }.error { e }

        }

        return hc

    }


//    @Bean
//    fun sessionFactory(): LocalSessionFactoryBean {
//        val sessionFactory = LocalSessionFactoryBean()
//        sessionFactory.setDataSource(dataSource())
//        sessionFactory.setPackagesToScan(jcp.springPackagesToScan)
//        sessionFactory.hibernateProperties = hibernateAdditionalProperties()
//        return sessionFactory
//    }


    @Bean
    @Primary
    fun entityManagerFactoryBean(): LocalContainerEntityManagerFactoryBean {
        try {
            val vendorAdapter = HibernateJpaVendorAdapter()
//            TODO("Why are the true repeated here and yet already set up above")
//            vendorAdapter.setGenerateDdl(jcp.showSql)
            vendorAdapter.setDatabase(Database.ORACLE)
//            vendorAdapter.setShowSql(jcp.showSql)

            val em = LocalContainerEntityManagerFactoryBean()
            em.setJpaProperties(hibernateAdditionalProperties())
            em.setPackagesToScan(jcp.springPackagesToScan)
            em.dataSource = dataSource()
            em.jpaVendorAdapter = vendorAdapter
            return em
        } catch (e: Exception) {

            KotlinLogging.logger { }.error { e }

            return LocalContainerEntityManagerFactoryBean()
        }

    }


    @Bean
    fun transactionManager(): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactoryBean().getObject()
//        transactionManager.sessionFactory = sessionFactory().`object`
        return transactionManager

    }

//    override fun annotationDrivenTransactionManager(): TransactionManager {
//        val transactionManager = JpaTransactionManager()
//        transactionManager.entityManagerFactory = entityManagerFactoryBean().getObject()
//        return transactionManager
//    }

}
