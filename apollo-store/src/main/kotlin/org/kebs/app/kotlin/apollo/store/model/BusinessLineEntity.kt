///*
// *
// *  *
// *  *
// *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
// *  *  *
// *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
// *  *  *    you may not use this file except in compliance with the License.
// *  *  *    You may obtain a copy of the License at
// *  *  *
// *  *  *       http://www.apache.org/licenses/LICENSE-2.0
// *  *  *
// *  *  *    Unless required by applicable law or agreed to in writing, software
// *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
// *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  *  *   See the License for the specific language governing permissions and
// *  *  *   limitations under the License.
// *  *
// *
// */
//
//package org.kebs.app.kotlin.apollo.store.model
//
//import org.springframework.data.annotation.CreatedBy
//import org.springframework.data.annotation.CreatedDate
//import org.springframework.data.annotation.LastModifiedBy
//import org.springframework.data.annotation.LastModifiedDate
//import java.io.Serializable
//import java.sql.Timestamp
//import javax.persistence.*
//
//
//@Entity(name = "cfg_kebs_business_lines")
//data class BusinessLineEntity(
//    @Id
//    @Column(name = "id", unique = true, nullable = false)
//    @SequenceGenerator(name = "cfg_kebs_business_lines_seq_gen", sequenceName = "cfg_kebs_business_lines_seq", allocationSize = 1)
//    @GeneratedValue(generator = "cfg_kebs_business_lines_seq_gen", strategy = GenerationType.SEQUENCE)
//    var id: Long = 0L,
//    @Column(name = "name", nullable = false)
//    var name: String = "",
//    @Column(name = "var_field1", nullable = true)
//    var varField1: String = "",
//    @Column(name = "var_field2", nullable = true)
//    var varField2: String = "",
//    @Column(name = "var_field3", nullable = true)
//    var varField3: String = "",
//    @Column(name = "var_field4", nullable = true)
//    var varField4: String = "",
//    @Column(name = "var_field5", nullable = true)
//    var varField5: String = "",
//    @Column(name = "var_field6", nullable = true)
//    var varField6: String = "",
//    @Column(name = "var_field7", nullable = true)
//    var varField7: String = "",
//    @Column(name = "var_field8", nullable = true)
//    var varField8: String = "",
//    @Column(name = "var_field9", nullable = true)
//    var varField9: String = "",
//    @Column(name = "var_field10", nullable = true)
//    var varField10: String = "",
//    @Column(name = "created_by", nullable = true)
//    @CreatedBy
//    var createdBy: Int? = null,
//    @Column(name = "created_on", nullable = true)
//    @CreatedDate
//    var createdOn: Timestamp? = null,
//    @Column(name = "modified_by", nullable = true)
//    @LastModifiedBy
//    var modifiedBy: Int? = null,
//    @Column(name = "modified_date", nullable = true)
//    @LastModifiedDate
//    var modifiedDate: Timestamp? = null
//) : Serializable
