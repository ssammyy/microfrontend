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

package org.kebs.app.kotlin.apollo.config.properties.jpa

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
@EncryptablePropertySource("file:\${CONFIG_PATH}/persistence-jpa.properties")
class JpaConnectionProperties {


    @Value("\${org.app.properties.jpa.jdbc.driver.class.name}")
    val jdbcDriverClassName: String = ""

    @Value("\${org.app.properties.jpa.jdbc.url}")
    val jdbcUrl: String = ""

    @Value("\${org.app.properties.jpa.jdbc.user}")
    val jdbcUser: String = ""

    @Value("\${org.app.properties.jpa.jdbc.schema}")
    val jdbcSchema: String = ""

    @Value("\${org.app.properties.jpa.jdbc.pass}")
    val jdbcPass: String = ""

    @Value("\${org.app.properties.jpa.dialect}")
    val dialect: String = ""

    @Value("\${org.app.properties.jpa.show.sql}")
    val showSql: String = ""

    @Value("\${org.app.properties.jpa.hbm2ddl.auto}")
    val hbm2ddlAuto: String = ""

    @Value("\${org.app.properties.jpa.pool.connection.customizer.classname}")
    val poolConnectionCustomizerClassname: String = ""

    @Value("\${org.app.properties.jpa.generate.statistics}")
    val generateStatistics: String = ""

    @Value("\${org.app.properties.jpa.spring.component.scan}")
    val springComponentScan: String = ""

    @Value("\${org.app.properties.jpa.spring.enable.jpa.repositories}")
    val springEnableJpaRepositories: String = ""

    @Value("\${org.app.properties.jpa.spring.packages.to.scan}")
    val springPackagesToScan: String = ""

    @Value("\${org.app.properties.jpa.hibernate.id.newgenerator.mappings}")
    val hibernateIdNewgeneratorMappings: String = ""

    @Value("\${org.app.properties.jpa.spring.jpa.hibernate.naming-strategy}")
    val springJpaHibernateNamingStrategy: String = ""

    @Value("\${org.app.properties.jpa.hikari.connection.timeout}")
    val hikariConnectionTimeout: Long = 0L

    @Value("\${org.app.properties.jpa.hikari.idle.timeout}")
    val hikariIdleTimeout: Long = 0L

    @Value("\${org.app.properties.jpa.hikari.max.lifetime}")
    val hikariMaxLifetime: Long = 0L

    @Value("\${org.app.properties.jpa.hikari.validation.timeout}")
    val hikariValidationTimeout: Long = 0L

    @Value("\${org.app.properties.jpa.hikari.minimum.idle.pool.size}")
    var minimumIdle: Int? = null

    @Value("\${org.app.properties.jpa.hikari.maximum.pool.size}")
    val hikariMaximumPoolSize: Int = 0

    @Value("\${org.app.properties.jpa.hikari.pool.name}")
    val hikariPoolName: String = ""

    @Value("\${org.app.properties.jpa.hikari.leak.detection.threshold}")
    val hikariLeakDetectionThreshold: Long = 0L

    @Value("\${org.app.properties.jpa.cache.prep.stmts}")
    val cachePrepStmts: Boolean = false

    @Value("\${org.app.properties.jpa.prep.stmt.cache.size}")
    val prepStmtCacheSize: Int = 0

    @Value("\${org.app.properties.jpa.prep.stmt.cache.sqllimit}")
    val prepStmtCacheSqllimit: Int = 0

    @Value("\${org.app.properties.jpa.hibernate.jdbc.lob.non.contextual.creation}")
    val jdbcLobNonContextualCreation: String = ""

    //    @Value("\${org.app.properties.jpa.hibernate.schema.apollo}")
//    val schemaLetostore: String = ""
    @Value("\${org.app.properties.jpa.database.type}")
    val databaseType: String = ""
}

