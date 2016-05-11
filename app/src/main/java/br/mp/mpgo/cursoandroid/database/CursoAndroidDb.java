package br.mp.mpgo.cursoandroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pedrorcagarcia on 11/05/16.
 */
public class CursoAndroidDb extends SQLiteOpenHelper {

    public SQLiteDatabase db = getWritableDatabase();
    public static final int DB_VERSION = 1;

    public CursoAndroidDb(Context context) {
        super(context, "CursoAndroidDB", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Ciclo de vida do App
        //Roda uma vez na execucao

        db.execSQL(PosicaoContract.SQL_CREATE_ENTRIES);
        db.execSQL(CirculoContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Muda versao do DB pra cima
        //Novas tabelas etc
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        //Muda versao do DB pra baixo
    }
}
