package org.kebs.app.kotlin.apollo.api.handlers.reports

import lombok.Builder
import lombok.Getter
import lombok.Setter
import java.math.BigDecimal
import java.util.*

@Getter
@Setter
@Builder
class FilmSearchCriteria {
    private val minRentalRate: Optional<BigDecimal>? = null
    private val maxRentalRate: Optional<BigDecimal>? = null
    private val releaseYear: Optional<Long>? = null
    private val categories: Set<String>? = null

}
