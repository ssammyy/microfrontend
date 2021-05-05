package org.kebs.app.kotlin.apollo.api.handlers

import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.IInvoiceRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.util.stream.Collectors
import java.util.stream.IntStream

@Component
class InvoiceHandlers(
        private val invoiceRepository: IInvoiceRepository,
        private val serviceMapsRepo: IServiceMapsRepository,
        applicationMapProperties: ApplicationMapProperties
) {
    final val appId = applicationMapProperties.mapPermitApplication
    private final val invoiceHomePage = "quality-assurance/customer/my-invoices"
    final val errors = mutableMapOf<String, String>()

    fun getAllInvoices(req: ServerRequest): ServerResponse {
        return serviceMapsRepo.findByIdAndStatus(appId, 1)
                ?.let { map ->


                    var currentPage: Int? = null
                    var pageSize: Int? = null
                    req.param("page").ifPresent { p -> currentPage = p.toIntOrNull() }
                    req.param("size").ifPresent { p -> pageSize = p.toIntOrNull() }
                    val pages: Pageable? = (pageSize ?: map.uiPageSize)?.let { PageRequest.of(currentPage ?: 0, it) }
                    req.attributes()["invoiceEntity"] = pages?.let { invoiceRepository.findByStatus(0, it) }

                    val totalRecords: Int? = invoiceRepository.findAllByStatus(0)?.size
//                    var pageNumbers: List<Int>? = null



//                            ?.let { pages ->

                    val pageNumbers = totalRecords?.let {
                        IntStream.rangeClosed(1, it)
                            .boxed().collect(Collectors.toList())
                    }
                    req.attributes()["totalPages"] = pageNumbers ?: 1
//                    req.attributes()["pageNumbers"] = pageNumbers

//                            }
                    req.attributes()["errors"] = errors
                    req.attributes()["hasErrors"] = errors.isEmpty()

                    ServerResponse.ok().render(
                            invoiceHomePage, req.attributes()
                    )
                }
                ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")

    }
}

