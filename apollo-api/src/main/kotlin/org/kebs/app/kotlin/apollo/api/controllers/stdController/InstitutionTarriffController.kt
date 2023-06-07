package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.InstitutionTarriffService
import org.kebs.app.kotlin.apollo.store.model.std.InstitutionTarriff
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/tarriff/institution")
@CrossOrigin(origins = ["http://localhost:4200"])
class InstitutionTarriffController(private val institutionTarriffService: InstitutionTarriffService) {

    @GetMapping("/getAllTarriffs")
    fun getTarriffs(): List<InstitutionTarriff> =
            institutionTarriffService.getTarriffs()

    @PostMapping("/addTrarriff")
    fun addTask(@Valid @RequestBody institutionTarriff: InstitutionTarriff): ResponseEntity<InstitutionTarriff> =
            institutionTarriffService.addTarriff(institutionTarriff)

    @PostMapping("/getTarriffById")
    fun getTaskById(@RequestBody newTarriff: InstitutionTarriff): ResponseEntity<InstitutionTarriff> =
            institutionTarriffService.getTarriffById(newTarriff.taskID!!)

    @PutMapping("/editTarriff")
    fun updateTaskById(@Valid @RequestBody newTarriff: InstitutionTarriff): ResponseEntity<InstitutionTarriff> =
            institutionTarriffService.editTarriff(newTarriff.taskID, newTarriff)

    @PostMapping("/deleteTarriff")
    fun deleteTask(@RequestBody newTarriff: InstitutionTarriff): ResponseEntity<Void> =
            institutionTarriffService.deleteTarriff(newTarriff.taskID!!)
}