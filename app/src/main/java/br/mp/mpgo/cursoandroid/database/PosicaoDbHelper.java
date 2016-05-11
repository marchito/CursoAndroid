package br.mp.mpgo.cursoandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.mp.mpgo.cursoandroid.Posicao;

/**
 * Created by pedrorcagarcia on 11/05/16.
 */
public class PosicaoDbHelper extends SQLiteOpenHelper{
    public static final int DB_VERSION = 1;
    private Posicao posicao;
    private SQLiteDatabase db;

    public PosicaoDbHelper(Context ctx){
        super(ctx, PosicaoContract.TABLE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Ciclo de vida do App
        //Roda uma vez na execucao

        db.execSQL(PosicaoContract.SQL_CREATE_ENTRIES);
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

    public void create(Posicao posicao){

        if(!db.isOpen() || db.isReadOnly()){
            db = getWritableDatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put(PosicaoContract.COLUMN_NAME_NAME, posicao.name);
        cv.put(PosicaoContract.COLUMN_NAME_LAT, posicao.latitude);
        cv.put(PosicaoContract.COLUMN_NAME_NAME, posicao.longitude);

        db.insert(PosicaoContract.TABLE_NAME, null, cv);

        db.close();
    }

    public List<Posicao> read(){

        if(!db.isOpen()){
            db = getReadableDatabase();
        }

        List<Posicao> posicoes = new ArrayList<>();
        Cursor cursor = db.query(PosicaoContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PosicaoContract.COLUMN_NAME_NAME);

        while(cursor.moveToNext()){
            posicoes.add(new Posicao(
                    cursor.getInt(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_ENTRY_ID)),
                    cursor.getString(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_LAT)),
                    cursor.getDouble(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_LNG))
                    ));
        }
        return posicoes;
    }

    public void update(Posicao posicao){

        if(!db.isOpen() || db.isReadOnly()){
            db = getWritableDatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put(PosicaoContract.COLUMN_NAME_NAME, posicao.name);
        cv.put(PosicaoContract.COLUMN_NAME_LAT, posicao.latitude);
        cv.put(PosicaoContract.COLUMN_NAME_NAME, posicao.longitude);

        db.update(PosicaoContract.TABLE_NAME,
                cv,
                PosicaoContract.COLUMN_NAME_ENTRY_ID + " = ",
                new String[]{"" + posicao.id});

        db.close();

    }

    public void delete(Posicao posicao){
        if(!db.isOpen() || db.isReadOnly()){
            db = getWritableDatabase();
        }

        db.delete(PosicaoContract.TABLE_NAME,
                PosicaoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"" + posicao.id});

        db.close();
    }

    public void clear(){
        if(!db.isOpen() || db.isReadOnly()){
            db = getWritableDatabase();
        }

        db.delete(PosicaoContract.TABLE_NAME,
                null,
                null);

        db.close();
    }
}
