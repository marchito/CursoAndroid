package br.mp.mpgo.cursoandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.mp.mpgo.cursoandroid.Circulo;
import br.mp.mpgo.cursoandroid.Posicao;

/**
 * Created by pedrorcagarcia on 11/05/16.
 */
public class CirculoDbHelper extends CursoAndroidDb{

    public CirculoDbHelper(Context ctx){
        super(ctx);
    }

    public void create(Circulo circulo){

        if(!db.isOpen() || db.isReadOnly()){
            db = getWritableDatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put(CirculoContract.COLUMN_NAME_RAIO, circulo.raio);
        cv.put(CirculoContract.COLUMN_NAME_LAT, circulo.latitude);
        cv.put(CirculoContract.COLUMN_NAME_LNG, circulo.longitude);

        db.insert(CirculoContract.TABLE_NAME, null, cv);

        db.close();
    }

    public List<Circulo> read(){

        if(!db.isOpen()){
            db = getReadableDatabase();
        }

        List<Circulo> circulos = new ArrayList<>();
        Cursor cursor = db.query(CirculoContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while(cursor.moveToNext()){
            circulos.add(new Circulo(
                    cursor.getInt(cursor.getColumnIndex(CirculoContract.COLUMN_NAME_ENTRY_ID)),
                    cursor.getDouble(cursor.getColumnIndex(CirculoContract.COLUMN_NAME_RAIO)),
                    cursor.getDouble(cursor.getColumnIndex(CirculoContract.COLUMN_NAME_LAT)),
                    cursor.getDouble(cursor.getColumnIndex(CirculoContract.COLUMN_NAME_LNG))
                    ));
        }
        return circulos;
    }

    public void update(Circulo circulo){

        if(!db.isOpen() || db.isReadOnly()){
            db = getWritableDatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put(CirculoContract.COLUMN_NAME_RAIO, circulo.raio);
        cv.put(CirculoContract.COLUMN_NAME_LAT, circulo.latitude);
        cv.put(CirculoContract.COLUMN_NAME_LNG, circulo.longitude);

        db.update(CirculoContract.TABLE_NAME,
                cv,
                CirculoContract.COLUMN_NAME_ENTRY_ID + " = ",
                new String[]{"" + circulo.id});

        db.close();

    }

    public void delete(Circulo circulo){
        if(!db.isOpen() || db.isReadOnly()){
            db = getWritableDatabase();
        }

        db.delete(CirculoContract.TABLE_NAME,
                CirculoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"" + circulo.id});

        db.close();
    }

    public void clear(){
        if(!db.isOpen() || db.isReadOnly()){
            db = getWritableDatabase();
        }

        db.delete(CirculoContract.TABLE_NAME,
                null,
                null);

        db.close();
    }

    public void createMany(List<Circulo> circulos){

        if(!db.isOpen() || db.isReadOnly()){
            db = getWritableDatabase();
        }

        for(Circulo circulo : circulos){
            ContentValues cv = new ContentValues();
            cv.put(CirculoContract.COLUMN_NAME_RAIO, circulo.raio);
            cv.put(CirculoContract.COLUMN_NAME_LAT, circulo.latitude);
            cv.put(CirculoContract.COLUMN_NAME_LNG, circulo.longitude);

            db.insert(CirculoContract.TABLE_NAME, null, cv);
        }


        db.close();
    }

}
