package com.tejnal.springasync.tejnalspringasyncapp.service;

import com.tejnal.springasync.tejnalspringasyncapp.service.exception.ExternalApiIntegrationException;
import com.tejnal.springasync.tejnalspringasyncapp.service.model.Country;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CountryClientImpl implements CountryClient {

  RestTemplate restTemplate = new RestTemplate();

  @Value("${country.apiEndpoint}")
  private String countriesEndPoint;

  @Override
  public CompletableFuture<List<Country>> getCountriesByLanguage(String language) {

    try {

      var url = countriesEndPoint + "language";

      /* UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
              .queryParam("fields","name");

      String uriBuilder = builder.build().encode().toUriString();*/

        Country[] response = buildUrl(url);

        return CompletableFuture.completedFuture(Arrays.asList(response));

    } catch (HttpClientErrorException | HttpServerErrorException e) {

      throw new ExternalApiIntegrationException(e.getMessage(), e);
    }
  }

    private Country[] buildUrl(String url) {
        final URI uri =
            UriComponentsBuilder.fromHttpUrl(url)
                .path("/")
                .queryParam("fields", "name")
                .build()
                .encode()
                .toUri();
        return restTemplate.getForObject(uri, Country[].class);
    }

    @Override
  public CompletableFuture<List<Country>> getCountriesByRegion(String region) {
    try {
      var url = countriesEndPoint + "region";

        Country[] response = buildUrl(url);

        return CompletableFuture.completedFuture(Arrays.asList(response));
    } catch (HttpClientErrorException | HttpServerErrorException e) {

      throw new ExternalApiIntegrationException(e.getMessage(), e);
    }
  }
}
