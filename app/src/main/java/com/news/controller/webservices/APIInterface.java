package com.news.controller.webservices;

import com.news.controller.Constants;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {


    @GET(Constants.ApiConstants.NEWS_URL)
    Call<HttpResponse> getResponse();
}
