package com.byob.beyourownbarista.logging

import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.util.UUID
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class MDCLoggingFilter : Filter {
    override fun init(filterConfig: FilterConfig?) {}

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest && response is HttpServletResponse) {
            MDC.put("REQUEST_METHOD", request.method)
            MDC.put("REQUEST_URI", request.requestURI)
            MDC.put("TRACE_ID", UUID.randomUUID().toString())
            try {
                chain.doFilter(request, response)
            } finally {
                MDC.clear()
            }
        } else {
            chain.doFilter(request, response)
        }
    }
}