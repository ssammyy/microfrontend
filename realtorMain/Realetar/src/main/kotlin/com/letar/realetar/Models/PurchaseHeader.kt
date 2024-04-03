package com.letar.realetar.Models

import com.fasterxml.jackson.annotation.JsonManagedReference
import lombok.Data
import org.springframework.boot.autoconfigure.security.SecurityProperties
import java.time.LocalDate
import java.util.stream.BaseStream
import javax.persistence.*

@Entity
@Table(name="purchaseHeader")
@Data
 class PurchaseHeader : BaseEntity(){
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    var vendor: Vendor? = null;
    var totalAmount: Double? = null;
    var amountIncludingVAT: Double? = null;
    var docDate: LocalDate? = null;
    var expectedDeliveryDate: LocalDate? = null
    var docStatus: String? = "open"
    @JsonManagedReference
    @OneToMany(mappedBy = "purchaseHeader", fetch = FetchType.LAZY)
    var purchaseLines: List<PurchaseLine>? = null
}
