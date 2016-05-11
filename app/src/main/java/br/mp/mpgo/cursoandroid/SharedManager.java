package br.mp.mpgo.cursoandroid;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pedrorcagarcia on 11/05/16.
 */
public class SharedManager {
    private static final String DEFAULT_CONF = "default_conf";

    public static void saveBoolean(Context ctx, String name, String key, boolean value){
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putBoolean(key, value);
        spEdit.apply();
    }

    public static void saveBoolean(Context ctx, String key, boolean value){
        saveBoolean(ctx, DEFAULT_CONF, key, value);
    }

    public static boolean getBoolean(Context ctx, String name, String key){
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static boolean getBoolean(Context ctx, String key){
        return getBoolean(ctx, DEFAULT_CONF, key);
    }

}
