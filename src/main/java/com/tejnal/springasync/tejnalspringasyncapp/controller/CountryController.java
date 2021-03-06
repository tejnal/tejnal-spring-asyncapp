package com.tejnal.springasync.tejnalspringasyncapp.controller;

import com.tejnal.springasync.tejnalspringasyncapp.service.CountryClient;
import com.tejnal.springasync.tejnalspringasyncapp.service.model.Country;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@Api(value = "CountryResource")
public class CountryController {

  private final CountryClient countryClient;

  public CountryController(CountryClient countryClient) {
    this.countryClient = countryClient;
  }

  @ApiOperation(
      httpMethod = "GET",
      value = "Get all European and French speaking countries",
      response = String.class,
      responseContainer = "List")
  @ApiResponses(
      value = {
        @ApiResponse(code = 404, message = "Countries not found"),
        @ApiResponse(code = 500, message = "The countries could not be fetched")
      })
  @GetMapping("")
  public List<String> getAllEuropeanFrenchSpeakingCountries() throws Throwable {

    CompletableFuture<List<Country>> countriesByLanguageFuture =
        countryClient.getCountriesByLanguage("fr");
    CompletableFuture<List<Country>> countriesByRegionFuture =
        countryClient.getCountriesByRegion("europe");

    List<String> europeanFrenchSpeakingCountries;

    try {
      europeanFrenchSpeakingCountries =
          new ArrayList<>(
              countriesByLanguageFuture.get().stream()
                  .map(Country::getName)
                  .collect(Collectors.toList()));
      europeanFrenchSpeakingCountries.retainAll(
          countriesByRegionFuture.get().stream()
              .map(Country::getName)
              .collect(Collectors.toList()));

    } catch (Throwable e) {
      throw e.getCause();
    }

    return europeanFrenchSpeakingCountries;
  }
}
