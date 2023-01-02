package com.codehunter.springcitrussexample.config;

import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfig {
  public static final String API_URL = "http://localhost:7080/api";

  @Bean
  public HttpClient appClient() {
    //        Map<String, String> envMap = System.getenv();
    //        var apiUrl = envMap.get(HOST_ENV_VAR);
    return new HttpClientBuilder().requestUrl(API_URL).contentType("application/json").build();
  }
}
