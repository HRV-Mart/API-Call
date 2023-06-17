package com.hrv.mart.apicall

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient.Builder
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import kotlin.reflect.full.findAnnotation

@Service
class APICaller(
    @Autowired
    private val webClientBuilder: Builder
)
{
    fun <T>getData(
        path: String,
        responseClassType: Class<T>,
        response: ServerHttpResponse?
    ): Mono<T> {
        val webClient = webClientBuilder.baseUrl(path)
            .build()
        return webClient.get()
            .retrieve()
            .bodyToMono(responseClassType)
            .onErrorResume {
                setResponse(response, it as WebClientResponseException, responseClassType)
            }
    }
    fun <T, U : Any>postRequest(
        path: String,
        responseClassType: Class<T>,
        requestBody: U,
        response: ServerHttpResponse
    ): Mono<T> {
        val webClient = webClientBuilder.baseUrl(path)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        return webClient
            .post()
            .body(Mono.just(requestBody), requestBody::class.java)
            .retrieve()
            .bodyToMono(responseClassType)
            .onErrorResume {
                setResponse(response, it as WebClientResponseException, responseClassType)
            }
    }
    fun <T, U : Any>putRequest(
        path: String,
        responseClassType: Class<T>,
        requestBody: U,
        response: ServerHttpResponse
    ): Mono<T> {
        val webClient = webClientBuilder.baseUrl(path)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        return webClient
            .put()
            .body(Mono.just(requestBody), requestBody::class.java)
            .retrieve()
            .bodyToMono(responseClassType)
            .onErrorResume {
                setResponse(response, it as WebClientResponseException, responseClassType)
            }
    }
    fun <T>deleteData(
        path: String,
        responseClassType: Class<T>,
        response: ServerHttpResponse
    ): Mono<T> {
        val webClient = webClientBuilder.baseUrl(path)
            .build()
        return webClient.delete()
            .retrieve()
            .bodyToMono(responseClassType)
            .onErrorResume {
                setResponse(response, it as WebClientResponseException, responseClassType)
            }
    }
    private fun <T >setResponse(response: ServerHttpResponse?, error: WebClientResponseException, responseClassType: Class<T>,): Mono<T> {
        if (response != null) {
            response.statusCode = error.statusCode
        }
        return if (responseClassType::class.java == String::class.java) {
            val message = (error.message ?: "") as T
            Mono.just(message)
        } else {
            Mono.empty()
        }
    }
}