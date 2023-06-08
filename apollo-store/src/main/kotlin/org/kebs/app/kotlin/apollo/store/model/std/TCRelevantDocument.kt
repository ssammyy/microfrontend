package org.kebs.app.kotlin.apollo.store.model.std

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*


@Entity
@Table(name = "TC_RELEVANT_DOCUMENT")
class TCRelevantDocument {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: String = ""

    var name: String = ""

    var type: String = ""

    var itemId: String=""

    var groupId: String=""

    @Lob
    lateinit var data: ByteArray
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TCRelevantDocument

        if (id != other.id) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (itemId != other.itemId) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + itemId.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "TCRelevantDocument(id='$id', name='$name', type='$type', itemId='$itemId', data=${data.contentToString()})"
    }


}
