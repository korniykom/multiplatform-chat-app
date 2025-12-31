package com.korniykom.kotlin_chat.api.config

import com.korniykom.kotlin_chat.domain.exception.RateLimitException
import com.korniykom.kotlin_chat.infra.rate_limiting.IpRateLimiter
import com.korniykom.kotlin_chat.infra.rate_limiting.IpResolver
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration

@Component
class IpRateLimitInterceptor(
    private val ipRateLimiter: IpRateLimiter,
    private val ipResolver: IpResolver,
    @param:Value("\${rate-limit.ip.apply-limit}")
    private val applyLimit: Boolean
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && applyLimit) {
            val annotation = handler.getMethodAnnotation(IpRateLimit::class.java)
            if (annotation != null) {
                val clientIp = ipResolver.getClientIp(request)

                return try {
                    ipRateLimiter.withIpRateLimit(
                        ipAddress = clientIp,
                        resetsInDuration = Duration.of(
                            annotation.duration,
                            annotation.timeUnit.toChronoUnit()
                        ),
                        maxRequestsPerIp = annotation.requests,
                        action = { true }
                    )
                } catch (e: RateLimitException) {
                    response.sendError(429)
                    false
                }
            }
        }
        return true
    }
}