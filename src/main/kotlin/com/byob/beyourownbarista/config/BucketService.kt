package com.byob.beyourownbarista.config

import io.github.bucket4j.Bucket
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap


@Service
class BucketService {
    private val cache: MutableMap<String, Bucket> = ConcurrentHashMap()

    fun resolveBucket(apiKey: String): Bucket {
        return cache.computeIfAbsent(
            apiKey
        ) { createNewBucket() }
    }

    fun createNewBucket() =  Bucket.builder()
        .addLimit { limit -> limit.capacity(50).refillGreedy(10, Duration.ofSeconds(1)) }
        .build()
}