package com.news.controller.webservices;

import android.app.Dialog;

import com.news.controller.CommonUtils;
import com.news.controller.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiCallBack implements Callback<HttpResponse> {

    public abstract void onSuccess(HttpResponse httpResponse);
    public abstract void onFail(Call<HttpResponse> httpResponseCall, Throwable throwable);
    private Dialog dialog= null;

    protected ApiCallBack() {
        dialog = CommonUtils.apiDialog(Constants.CONTEXT);
       
    }

    @Override
    public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {

        if (response.isSuccessful()){
            dialog.dismiss();
            onSuccess(response.body());
        }

    }

    @Override
    public void onFailure(Call<HttpResponse> call, Throwable t) {
        dialog.dismiss();
        onFail(call, t);
    }
}
