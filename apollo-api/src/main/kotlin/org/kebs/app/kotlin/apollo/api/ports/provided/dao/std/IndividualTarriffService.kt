package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.kebs.app.kotlin.apollo.store.model.std.IndividualTarriff
import org.kebs.app.kotlin.apollo.store.model.std.InstitutionTarriff
import org.kebs.app.kotlin.apollo.store.repo.std.IndividualTarriffRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class IndividualTarriffService(private val individualTarriffRepository: IndividualTarriffRepository) {
    fun getTarriffs(): MutableList<IndividualTarriff> =
            individualTarriffRepository.findAll()

    fun addTarriff(individualTarriff: IndividualTarriff): ResponseEntity<IndividualTarriff> =
            ResponseEntity.ok(individualTarriffRepository.save(individualTarriff))

    fun getTarriffById(id: Long): ResponseEntity<IndividualTarriff>? =
            individualTarriffRepository.findById(id).map { task ->
                ResponseEntity.ok(task)
            }.orElse(ResponseEntity.notFound().build())

    fun editTarriff(id: Long?, newTarriff: InstitutionTarriff): ResponseEntity<IndividualTarriff>? =
            individualTarriffRepository.findById(id!!).map { currentTask ->
                val updatedTask: IndividualTarriff =
                        currentTask
                                .copy(
                                        subscriptionFee = newTarriff.subscriptionFee,
                                        libraryDepositFee = newTarriff.libraryDepositFee,
                                        totalFee = newTarriff.totalFee
                                )
                ResponseEntity.ok().body(individualTarriffRepository.save(updatedTask))
            }.orElse(ResponseEntity.notFound().build())

    fun deleteTarriff(taskId: Long): ResponseEntity<Void> =
            individualTarriffRepository.findById(taskId).map { task ->
                individualTarriffRepository.delete(task)
                ResponseEntity<Void>(HttpStatus.ACCEPTED)
            }.orElse(ResponseEntity.notFound().build())

}