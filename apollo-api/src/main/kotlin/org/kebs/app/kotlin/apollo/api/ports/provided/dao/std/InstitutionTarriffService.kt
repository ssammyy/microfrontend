package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.kebs.app.kotlin.apollo.store.model.std.InstitutionTarriff
import org.kebs.app.kotlin.apollo.store.repo.std.InstitutionTarriffRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class InstitutionTarriffService(private val institutionTarriffRepository: InstitutionTarriffRepository) {

    fun getTarriffs(): List<InstitutionTarriff> =
            institutionTarriffRepository.findAll()

    fun addTarriff(institutionTarriff: InstitutionTarriff): ResponseEntity<InstitutionTarriff> =
            ResponseEntity.ok(institutionTarriffRepository.save(institutionTarriff))

    fun getTarriffById(id: Long): ResponseEntity<InstitutionTarriff> =
            institutionTarriffRepository.findById(id).map { task ->
                ResponseEntity.ok(task)
            }.orElse(ResponseEntity.notFound().build())

    fun editTarriff(id: Long?, newTarriff: InstitutionTarriff): ResponseEntity<InstitutionTarriff> =
            institutionTarriffRepository.findById(id!!).map { currentTask ->
                val updatedTask: InstitutionTarriff =
                        currentTask
                                .copy(
                                        classifications = newTarriff.classifications,
                                        subscriptionFee = newTarriff.subscriptionFee,
                                        libraryDepositFee = newTarriff.libraryDepositFee,
                                        totalFee = newTarriff.totalFee
                                )
                ResponseEntity.ok().body(institutionTarriffRepository.save(updatedTask))
            }.orElse(ResponseEntity.notFound().build())

    fun deleteTarriff(taskId: Long): ResponseEntity<Void> =
            institutionTarriffRepository.findById(taskId).map { task ->
                institutionTarriffRepository.delete(task)
                ResponseEntity<Void>(HttpStatus.ACCEPTED)
            }.orElse(ResponseEntity.notFound().build())


}


