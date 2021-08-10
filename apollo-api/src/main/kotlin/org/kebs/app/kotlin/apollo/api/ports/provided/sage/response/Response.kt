package org.kebs.app.kotlin.apollo.api.ports.provided.sage.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Date
import java.sql.Timestamp

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */
class Header {
    var messageID: String? = null
    var statusCode: String? = null
    var statusDescription: String? = null
}

class Response {
    @JsonProperty("DocumentNo")
    var documentNo: String? = null

    @JsonProperty("ResponseDate")
    var responseDate: Timestamp? = null
}

class RootResponse {
    var header: Header? = null
    var response: Response? = null
}

