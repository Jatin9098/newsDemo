package com.news.controller;

import android.content.Context;

public class Constants {
    public static final boolean DEBUG_MODE = false;
    public static final String READ_NEWS_INFO = "read_selected_news" ;
    public static Context CONTEXT = null;

    public class ApiConstants{
        public static final String NEWS_URL = "v2/top-headlines?country=in&apiKey=653b91d615ae4c22a53db8c959fb7226&pageSize=100" ;
    }
}
