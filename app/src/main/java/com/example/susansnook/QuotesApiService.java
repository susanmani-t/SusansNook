package com.example.susansnook;

import retrofit2.Call;
import retrofit2.http.GET;

// Retrofit interface for Quotable API
public interface QuotesApiService {

    // Endpoint: https://api.quotable.io/random
    @GET("random")
    Call<QuoteResponse> getRandomQuote();
}
