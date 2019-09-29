package com.news.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.news.model.NewsModel;
import com.news.repository.HeadLineRepository;

public class HeadLineViewModel extends AndroidViewModel {

    LiveData<NewsModel> newsResponseObservable;

    public HeadLineViewModel(@NonNull Application application)
    {
        super(application);
        newsResponseObservable = HeadLineRepository.getInstance().getHeadLine();
    }

    private static final MutableLiveData MUTABLE_LIVE_DATA = new MutableLiveData();{
        MUTABLE_LIVE_DATA.setValue(null);
    }

    public LiveData<NewsModel> getNewsResponseObservable() {

        return newsResponseObservable;
    }
}
