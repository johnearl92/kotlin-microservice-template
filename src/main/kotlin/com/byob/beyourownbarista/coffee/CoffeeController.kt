package com.byob.beyourownbarista.coffee

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/coffee")
class CoffeeController(private val coffeeRepository: CoffeeRepository) {
    @GetMapping
    fun getAllCoffees(): List<Coffee> = coffeeRepository.findAll()

    @GetMapping("/{id}")
    fun getCoffee(@PathVariable id: Long): Coffee = coffeeRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Coffee not found") }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCoffee(@RequestBody coffee: Coffee) = coffeeRepository.save(coffee)

    @PutMapping("/{id}")
    fun updateCoffee(@PathVariable id: Long, @RequestBody coffee: Coffee): Coffee = coffeeRepository.findById(id).map {
        coffeeRepository.save(coffee.copy(id = id))
    }.orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Coffee not found") }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCoffee(@PathVariable id: Long) = coffeeRepository.findById(id).map {
        coffeeRepository.deleteById(id)
    }.orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Coffee not found") }
}