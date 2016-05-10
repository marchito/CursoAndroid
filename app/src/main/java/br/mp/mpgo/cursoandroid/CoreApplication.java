package br.mp.mpgo.cursoandroid;

import android.app.Application;


import com.facebook.FacebookSdk;

/**
 * Created by pedrorcagarcia on 09/05/16.
 */
public class CoreApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
