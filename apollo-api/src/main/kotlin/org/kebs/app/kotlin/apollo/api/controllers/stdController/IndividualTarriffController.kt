package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.IndividualTarriffService
import org.kebs.app.kotlin.apollo.store.model.std.IndividualTarriff
import org.kebs.app.kotlin.apollo.store.model.std.InstitutionTarriff
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/tarriff/individual")
@CrossOrigin(origins = ["http://localhost:4200"])
class IndividualTarriffController(private val individualTarriffService: IndividualTarriffService) {
    @GetMapping("/getAllTarriffs")
    fun getTarriffs(): MutableList<IndividualTarriff> =
            individualTarriffService.getTarriffs()

    @PostMapping("/addTrarriff")
    fun addTask(@Valid @RequestBody individualTarriff: IndividualTarriff): ResponseEntity<IndividualTarriff> =
            individualTarriffService.addTarriff(individualTarriff)

    @PostMapping("/getTarriffById")
    fun getTaskById(@RequestBody newTarriff: InstitutionTarriff): ResponseEntity<IndividualTarriff>? =
            individualTarriffService.getTarriffById(newTarriff.taskID!!)

    @PutMapping("/editTarriff")
    fun updateTaskById(@Valid @RequestBody newTarriff: InstitutionTarriff): ResponseEntity<IndividualTarriff>? =
            individualTarriffService.editTarriff(newTarriff.taskID, newTarriff)

    @PostMapping("/deleteTarriff")
    fun deleteTask(@RequestBody newTarriff: InstitutionTarriff): ResponseEntity<Void> =
            individualTarriffService.deleteTarriff(newTarriff.taskID!!)
}