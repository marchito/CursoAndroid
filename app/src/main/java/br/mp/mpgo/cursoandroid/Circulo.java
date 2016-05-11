package br.mp.mpgo.cursoandroid;

/**
 * Created by pedrorcagarcia on 10/05/16.
 */
public class Circulo {
    public int id;
    public double latitude;
    public double longitude;
    public double raio;

    public Circulo(int id, double raio, double lat, double lng) {

        this.id = id;
        this.raio = raio;
        this.latitude =  lat;
        this.longitude = lng;
    }
}
