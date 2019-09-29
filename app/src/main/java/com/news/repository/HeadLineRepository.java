package com.news.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.news.controller.webservices.APIInterface;
import com.news.controller.webservices.ApiCallBack;
import com.news.controller.webservices.HttpResponse;
import com.news.controller.webservices.RestClient;
import com.news.model.NewsModel;

import retrofit2.Call;

public class HeadLineRepository {
   
    private APIInterface apiService;

    private static class SingletonHelper
    {
        private static final HeadLineRepository INSTANCE = new HeadLineRepository();
    }

    public static HeadLineRepository getInstance()
    {

        return SingletonHelper.INSTANCE;
    }

    public HeadLineRepository()
    {

        apiService= RestClient.getInstance().create(APIInterface.class);
    }

    public LiveData<NewsModel> getHeadLine() {

        final MutableLiveData<NewsModel> data = new MutableLiveData<>();
        apiService.getResponse().enqueue(new ApiCallBack() {
            @Override
            public void onSuccess(HttpResponse response) {

                data.setValue(response);
            }
            @Override
            public void onFail(Call<HttpResponse> httpResponseCall, Throwable throwable) {


                data.setValue(null);
            }
        });
        return data;
    }
}
