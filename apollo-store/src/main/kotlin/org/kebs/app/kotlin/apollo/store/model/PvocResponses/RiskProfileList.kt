package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.kebs.app.kotlin.apollo.store.model.RiskProfileDataEntity
import org.springframework.stereotype.Component
import java.util.*
import javax.xml.bind.annotation.XmlRootElement

@Component
class RiskProfileList {
     var profile: MutableList<RiskProfileResponse> = ArrayList<RiskProfileResponse>()
}