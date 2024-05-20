package com.byob.beyourownbarista.api.coffee

import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/coffee")
class CoffeeController(private val coffeeRepository: CoffeeRepository) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun getAllCoffees(): List<Coffee> {
        logger.info { "Fetching all coffees" }
        return coffeeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getCoffee(@PathVariable id: Long): Coffee {
        logger.info { "Fetching coffee with id $id" }
        return coffeeRepository.findById(id).orElseThrow {
            val errorCode = HttpStatus.NOT_FOUND
            MDC.put("RESPONSE_CODE", errorCode.value().toString())
            logger.error { "Coffee with id $id not found" }
            ResponseStatusException(errorCode, "Coffee not found")
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCoffee(@RequestBody coffee: Coffee): Coffee {
        logger.info { "Creating new coffee: $coffee" }
        return coffeeRepository.save(coffee)
    }

    @PutMapping("/{id}")
    fun updateCoffee(@PathVariable id: Long, @RequestBody coffee: Coffee): Coffee {
        logger.info { "Updating coffee with id $id" }
        return coffeeRepository.findById(id).map {
            coffeeRepository.save(coffee.copy(id = id))
        }.orElseThrow {
            val errorCode = HttpStatus.NOT_FOUND
            MDC.put("RESPONSE_CODE", errorCode.value().toString())
            logger.error { "Coffee with id $id not found" }
            ResponseStatusException(errorCode, "Coffee not found")
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCoffee(@PathVariable id: Long) {
        logger.info { "Deleting coffee with id $id" }
        coffeeRepository.findById(id).map {
            coffeeRepository.deleteById(id)
        }.orElseThrow {
            val errorCode = HttpStatus.NOT_FOUND
            MDC.put("RESPONSE_CODE", errorCode.value().toString())
            logger.error { "Coffee with id $id not found" }
            ResponseStatusException(errorCode, "Coffee not found")
        }
    }
}