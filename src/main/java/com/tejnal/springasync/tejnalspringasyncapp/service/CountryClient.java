package com.tejnal.springasync.tejnalspringasyncapp.service;


import com.tejnal.springasync.tejnalspringasyncapp.service.model.Country;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CountryClient {
    // this is used for asynchronous programming in java, it runs on separate Thread and informs main thread
    CompletableFuture<List<Country>> getCountriesByLanguage(String language);

    CompletableFuture<List<Country>> getCountriesByRegion(String region);



}
