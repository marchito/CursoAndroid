package br.mp.mpgo.cursoandroid;

public class Posicao {

    public int id;
    public String name;
    public double latitude;
    public double longitude;

    public Posicao(int id, String name, double latitude, double longitude){
        this.id = id;
        this.name = name;
        this.latitude =  latitude;
        this.longitude = longitude;
    }
}
