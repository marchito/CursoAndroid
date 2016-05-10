package br.mp.mpgo.cursoandroid;

import android.app.Application;


import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by pedrorcagarcia on 09/05/16.
 */
public class CoreApplication extends Application {

    public MyInterfaceRetrofit service;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor( new LoggingInterceptor() ).build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.mocky.io/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        service = retrofit.create(MyInterfaceRetrofit.class);

    }
}

