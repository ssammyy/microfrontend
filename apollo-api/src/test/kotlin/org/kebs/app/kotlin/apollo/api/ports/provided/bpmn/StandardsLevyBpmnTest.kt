package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class StandardsLevyBpmnTest {

    @Autowired
    lateinit var bpm: StandardsLevyBpmn

    @Test
    fun startSlRegistrationProcessTest() {
        bpm.startSlSiteVisitProcess(64, 54)
    }
}