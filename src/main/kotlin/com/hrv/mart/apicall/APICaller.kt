package com.hrv.mart.apicall

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient.Builder
import reactor.core.publisher.Mono

@Service
class APICaller(
    @Autowired
    private val webClientBuilder: Builder
)
{
    fun <T>getData(path: String, classType: Class<T>): Mono<T> {
        val webClient = webClientBuilder.baseUrl(path)
            .build()
        return webClient.get()
            .retrieve()
            .bodyToMono(classType)
    }
    fun <T, U : Any>postRequest(path: String, responseClassType: Class<T>, requestBody: U): Mono<T> {
        val webClient = webClientBuilder.baseUrl(path)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        return webClient
            .post()
            .body(Mono.just(requestBody), requestBody::class.java)
            .retrieve()
            .bodyToMono(responseClassType)
    }
    fun <T, U : Any>putRequest(path: String, responseClassType: Class<T>, requestBody: U): Mono<T> {
        val webClient = webClientBuilder.baseUrl(path)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
        return webClient
            .put()
            .body(Mono.just(requestBody), requestBody::class.java)
            .retrieve()
            .bodyToMono(responseClassType)
    }
    fun <T>deleteData(path: String, classType: Class<T>): Mono<T> {
        val webClient = webClientBuilder.baseUrl(path)
            .build()
        return webClient.delete()
            .retrieve()
            .bodyToMono(classType)
    }
}