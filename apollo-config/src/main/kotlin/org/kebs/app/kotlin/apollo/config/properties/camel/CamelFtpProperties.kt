package org.kebs.app.kotlin.apollo.config.properties.camel;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EncryptablePropertySource(value = ["file:\${CONFIG_PATH}/camel-sftp.properties"])
public class CamelFtpProperties {
    @Value("\${org.app.properties.camel.sftp.scheme:sftp}")
    var scheme = "sftp"

    @Value("\${org.app.properties.camel.sftp.host}")
    var host = "10.10.0.127"

    @Value("\${org.app.properties.camel.sftp.port}")
    var port = 922

    @Value("\${org.app.properties.camel.sftp.path}")
    var path = "/C/mhxapps/inbound"

    @Value("\${org.app.properties.camel.sftp.user.name}")
    var userName = "kebs\\bsk"

    @Value("\${org.app.properties.camel.sftp.password}")
    var password = "1ntegrat10n@!234"

    @Value("\${org.app.properties.camel.sftp.passive.mode}")
    var passiveMode = "false"

    @Value("\${org.app.properties.camel.sftp.ant.include}")
    var antInclude = "*.xml"

    @Value("\${org.app.properties.camel.sftp.initial.delay}")
    var initialDelay = "10s"

    @Value("\${org.app.properties.camel.sftp.delay}")
    var delay = "50"

    @Value("\${org.app.properties.camel.sftp.move.failed}")
    var moveFailed = "/C/mhxapps/inbound/failed"

    @Value("\${org.app.properties.camel.sftp.move}")
    var move = "/C/mhxapps/inbound/processed"

    @Value("\${org.app.properties.camel.sftp.pre.move}")
    var preMove = "/C/mhxapps/inbound/in-progress"

    @Value("\${org.app.properties.camel.sftp.read.locked}")
    var readLock = "changed"

    @Value("\${org.app.properties.camel.sftp.read.locked.min.age}")
    var readLockMinAge = "1m"

    @Value("\${org.app.properties.camel.sftp.read.locked.timeout}")
    var readLockTimeout = "70000"

    @Value("\${org.app.properties.camel.sftp.read.lock.check.interval}")
    var readLockCheckInterval = "5000"

    @Value("\${org.app.properties.camel.sftp.step.wise}")
    var stepwise = "false"

    @Value("\${org.app.properties.camel.sftp.use.user.known.hosts.file}")
    var useUserKnownHostsFile = "false"
    @Value("\${org.app.properties.camel.sftp.log.level}")
    var logLevel = "WARN"
    @Value("\${org.app.properties.camel.sftp.upload.path}")
    var uploadDirectory = "\\C\\mhxapps\\outbound"
    @Value("\${org.app.properties.camel.sftp.outbound:outbound}")
    var outboundDirectory = ""

}
