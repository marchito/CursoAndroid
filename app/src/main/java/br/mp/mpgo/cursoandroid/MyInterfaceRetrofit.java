package br.mp.mpgo.cursoandroid;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pedrorcagarcia on 10/05/16.
 */
public interface MyInterfaceRetrofit {
    @GET("57322cda0f0000550aead71c")
    Call<Data> searchPositions();

}
