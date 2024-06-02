package com.byob.beyourownbarista.logging


import com.byob.beyourownbarista.config.BucketService
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ThrottlingFilter (private val bucketService: BucketService) : Filter {
    override fun init(filterConfig: FilterConfig?) {
        // No initialization necessary
    }

    @Throws(Exception::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        if (servletRequest !is HttpServletRequest || servletResponse !is HttpServletResponse) {
            filterChain.doFilter(servletRequest, servletResponse)
            return
        }

        val apiKey = servletRequest.getHeader("X-api-key")
        if (apiKey.isNullOrBlank()) {
            respondWithBadRequest(servletResponse, "missing X-api-key")
            return
        }

        val tokenBucket = bucketService.resolveBucket(apiKey)
        val probe = tokenBucket.tryConsumeAndReturnRemaining(1)

        if (probe.isConsumed) {
            servletResponse.addHeader("X-Rate-Limit-Remaining", probe.remainingTokens.toString())
            filterChain.doFilter(servletRequest, servletResponse)
        } else {
            respondWithTooManyRequests(servletResponse, probe.nanosToWaitForRefill)
        }
    }

    override fun destroy() {
        // No resources to clean up
    }

    private fun respondWithBadRequest(response: HttpServletResponse, message: String) {
        response.apply {
            status = HttpStatus.BAD_REQUEST.value()
            contentType = "text/plain"
            writer.append(message)
        }
    }

    private fun respondWithTooManyRequests(response: HttpServletResponse, nanosToWaitForRefill: Long) {
        response.apply {
            status = HttpStatus.TOO_MANY_REQUESTS.value()
            setHeader("X-Rate-Limit-Retry-After-Seconds", TimeUnit.NANOSECONDS.toSeconds(nanosToWaitForRefill).toString())
            contentType = "text/plain"
            writer.append("Too many requests")
        }
    }
}