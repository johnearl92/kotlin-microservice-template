package com.byob.beyourownbarista.api.coffee

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CoffeeRepository : JpaRepository<Coffee, Long> {

}