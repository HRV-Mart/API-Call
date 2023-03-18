package com.hrv.mart.apicall

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient.Builder
import reactor.core.publisher.Mono

@Service
class APICaller(
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
}