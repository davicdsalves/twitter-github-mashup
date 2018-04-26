package com.mashup.challenge.configuration;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by davi on 5/28/17.
 */
@Configuration
public class HttpClientConfig {

    @Bean
    public RestTemplate githubRestTemplate(GitHubProperties properties) {
        HttpClient httpClient = httpClientBuilder(properties.getMaxConnections()).build();
        return buildRestTemplate(httpClient, properties.getTimeout());
    }

    private RestTemplate buildRestTemplate(HttpClient httpClient, int timeout) {
        ClientHttpRequestFactory factory = clientHttpRequestFactory(httpClient, timeout);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }

    private HttpClientBuilder httpClientBuilder(int maxConnections) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxConnections);
        cm.setDefaultMaxPerRoute(maxConnections);

        return HttpClientBuilder.create().setConnectionManager(cm);
    }

    private ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient, int timeout) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(timeout);
        factory.setConnectionRequestTimeout(timeout);
        factory.setReadTimeout(timeout);
        return factory;
    }
}
