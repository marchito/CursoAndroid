package br.mp.mpgo.cursoandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.mp.mpgo.cursoandroid.database.CirculoDbHelper;
import br.mp.mpgo.cursoandroid.database.PosicaoDbHelper;
import br.mp.mpgo.cursoandroid.util.Conectividade;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ClusterManager mClusterManager;

    private PosicaoDbHelper dbPositionHelper;
    private CirculoDbHelper dbCirleHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        dbPositionHelper = new PosicaoDbHelper(this);
        dbCirleHelper = new CirculoDbHelper(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConnected(Bundle bundle) {
        if (
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)  !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }


        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        if(Conectividade.haveConnectivity(this)) {
            Call<Data> call = ((CoreApplication) getApplication()).service.searchPositions();
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {

                    if(response.body().posicoes.size() > 0){
                        dbPositionHelper.clear();
                        dbPositionHelper.createMany(response.body().posicoes);
                        addMarkersToCluster(response.body().posicoes);
                    }

                    mClusterManager = new ClusterManager<MyItem>(MapsActivity.this, mMap);

                    for (Poligono poligono : response.body().poligonos) {

                        List<LatLng> points = new ArrayList<LatLng>();
                        for (Ponto ponto : poligono.pontos) {
                            points.add(new LatLng(ponto.latitude, ponto.longitude));
                        }
                        mMap.addPolygon(new PolygonOptions()
                                .addAll(points)
                                .strokeColor(Color.RED)
                                .fillColor(Color.BLUE));
                    }

                    if(response.body().circulos.size() > 0){
                        dbCirleHelper.clear();
                        dbCirleHelper.createMany(response.body().circulos);
                        addCirclesToMap(response.body().circulos);
                    }

                    mMap.setOnCameraChangeListener(mClusterManager);
                    mMap.setOnMarkerClickListener(mClusterManager);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mClusterManager.getMarkerCollection().getMarkers().iterator().next().getPosition(), 6));
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Log.e("CURSO", "Pepino: " + t.getLocalizedMessage());
                }
            });
        }else{
            List<Posicao> posicoes = dbPositionHelper.read();
            addMarkersToCluster(posicoes);
            List<Circulo> circulos = dbCirleHelper.read();
            addCirclesToMap(circulos);

        }


        if(mLastLocation != null)
        {
            LatLng eu = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MyItem offsetItem = new MyItem(eu.latitude, eu.longitude);
            mClusterManager.addItem(offsetItem);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eu, 6));
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void addMarkersToCluster(List<Posicao> posicoes){
        for (Posicao posicao : posicoes) {
            MyItem offsetItem = new MyItem(posicao.latitude, posicao.longitude);
            mClusterManager.addItem(offsetItem);
        }
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
    }

    private void addCirclesToMap(List<Circulo> circulos){
        for (Circulo circulo : circulos) {
            dbCirleHelper.create(circulo);
            LatLng center = new LatLng(circulo.latitude, circulo.longitude);
            mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(circulo.raio)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE));

        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {
        LatLng newPos = new LatLng(location.getLatitude(), location.getLongitude());
        MyItem offsetItem = new MyItem(newPos.latitude, newPos.longitude);
        mClusterManager.addItem(offsetItem);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPos, 6));
    }

}

