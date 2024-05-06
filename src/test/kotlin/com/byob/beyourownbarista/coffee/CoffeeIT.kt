package com.byob.beyourownbarista.coffee

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CoffeeIT : BehaviorSpec() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var coffeeRepository: CoffeeRepository

    val mapper = jacksonObjectMapper()

    init {

        beforeSpec {
            coffeeRepository.deleteAll()
        }

        Given("a coffee with name 'Espresso' and price 2.5") {
            val name = "Espresso"
            val price = 2.5
            var coffee = Coffee(name = name, price = price)

            When("creating a new coffee") {
                coffee = mockMvc.post("/coffee") {
                    contentType = MediaType.APPLICATION_JSON
                    content = mapper.writeValueAsString(coffee)
                }.andExpect {
                    status { isCreated() }
                }.andReturn().response.contentAsString.let {
                    mapper.readValue(it, Coffee::class.java)
                }
                Then("coffee is created with new id") {
                    coffee.id shouldNotBe null
                    coffee.name shouldBe name
                    coffee.price shouldBe price
                }
            }

            When("getting all coffees") {
                val allCoffees = mockMvc.get("/coffee") {
                    contentType = MediaType.APPLICATION_JSON
                }.andExpect {
                    status { isOk() }
                }.andReturn().response.contentAsString.let {
                    mapper.readValue(it, Array<Coffee>::class.java)
                }

                Then("the coffee should be in the list") {
                    allCoffees shouldContain coffee
                }
            }

            When("getting the coffee by id") {
                and("the id is valid") {
                    val coffeeById = mockMvc.get("/coffee/${coffee.id}") {
                        contentType = MediaType.APPLICATION_JSON
                    }.andExpect {
                        status { isOk() }
                    }.andReturn().response.contentAsString.let {
                        mapper.readValue(it, Coffee::class.java)
                    }

                    Then("the coffee shoulld be returned") {
                        coffeeById shouldBe coffee
                    }
                }

                and("the id is not valid") {
                    val request = mockMvc.get("/coffee/2")

                    Then("no coffee should be returned") {
                        request.andExpect { status { isNotFound() } }
                    }
                }
            }

            When("updating the coffee") {
                val updatedCoffee = coffee.copy(price = 3.0)
                and("the coffee id is valid") {
                    val updatedFromDb = mockMvc.put("/coffee/${coffee.id}") {
                        contentType = MediaType.APPLICATION_JSON
                        content = mapper.writeValueAsString(updatedCoffee)
                    }.andExpect {
                        status { isOk() }
                    }.andReturn().response.contentAsString.let {
                        mapper.readValue(it, Coffee::class.java)
                    }

                    Then("the coffee should be updated") {
                        updatedFromDb shouldBe updatedCoffee
                    }
                }
                and("the coffee id is invalid") {
                    val result = mockMvc.put("/coffee/2") {
                        contentType = MediaType.APPLICATION_JSON
                        content = mapper.writeValueAsString(updatedCoffee)
                    }

                    Then("no coffee should updated") {
                        result.andExpect { status { isNotFound() } }
                    }
                }

            }

            When("deleting a coffee") {
                and("the coffee id is valid") {
                    mockMvc.delete("/coffee/${coffee.id}").andExpect { status { isNoContent() } }
                    Then("coffee should be deleted") {
                        mockMvc.get("/coffee/${coffee.id}").andExpect { status { isNotFound() } }
                    }
                }

                and("the coffee id is invalid") {
                    val resultAction = mockMvc.delete("/coffee/2")
                    Then("no coffee should be deleted") {
                        resultAction.andExpect { status { isNotFound() } }
                    }
                }
            }
        }
    }
}