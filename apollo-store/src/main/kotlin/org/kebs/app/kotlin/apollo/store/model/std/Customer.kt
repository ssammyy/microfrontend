package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name = "CUSTOMER_TRIAL")
class Customer(val name: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0


}
