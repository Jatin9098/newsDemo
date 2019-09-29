package com.news.controller.helper;

import android.app.Application;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

public class NewsApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        new Instabug.Builder(this, "99e0d90491d1ec0c930429461f9d71c8")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT)
                .build();

        Instabug.identifyUser("user name", "jaeriefdjf@nfjd");
    }
}
