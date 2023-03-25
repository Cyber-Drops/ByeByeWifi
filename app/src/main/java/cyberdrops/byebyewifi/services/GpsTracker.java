package cyberdrops.byebyewifi.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class GpsTracker extends Service implements LocationListener  {
    //TODO in fase di release sostituire il parametro della distanza di misurazione minima di 0 con una costante positiva
    private Context context;
    private LocationManager locationManager = null;
    private Location location = null;
    private String bestProvider;
    GnssStatus.Callback mGnssStatusCallback;
    private boolean isNetworkPEnabled = false;
    private boolean isGpsPEnabled = false;
    StringBuilder satellitesStatus = new StringBuilder();

    private static final long TIME_TO_UPDATE_LOCATION  = 15000;
    private static final long DISTANCE_TO_UPDATE_LOCATION  = 3;

    private double latitude;
    private double longitude;

    public GpsTracker(Context context) {
        this.context = context;
        this.initGpsTracker();
    }

    @SuppressLint("MissingPermission")
    private void initGpsTracker(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        this.locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        this.bestProvider  = locationManager.getBestProvider(criteria, true);
        Log.i("BEST PROVIDER: ", this.bestProvider);
        setGnssStatusListner();
        this.locationManager.registerGnssStatusCallback(mGnssStatusCallback);
        this.isNetworkPEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        this.isGpsPEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        /*
        if (!isNetworkPEnabled){
            enableNetworkProvider();
        }
        */
        if (!isGpsPEnabled) {
            enableGpsProvider();
        }

    }


    @SuppressLint("MissingPermission")
    public void setGpsLocation(){
        if (locationManager != null){
            this.location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null){
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
    }
    @SuppressLint("MissingPermission")
    public void setBestProvider(){
        this.locationManager.requestLocationUpdates(bestProvider,0,0,this);

    }
/*
    @SuppressLint("MissingPermission") //Richiesto nella MainActivity
    private void enableNetworkProvider(){
        //TODO procedura di attivazione network, se non attivi esci
        if (isNetworkPEnabled){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_TO_UPDATE_LOCATION, 0, this );
        }
    }

 */
    private void enableGpsProvider(){
        //TODO procedura di attivazione Gps, se non attivi esci
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        System.out.println("------------------->>>>CALLBACK LISTNER<<<------------------------------");
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isNetworkPEnabled() {
        return isNetworkPEnabled;
    }

    public boolean isGpsPEnabled() {
        return isGpsPEnabled;
    }

    private void setGnssStatusListner() {
        mGnssStatusCallback = new GnssStatus.Callback() {
            @Override
            public void onStarted() {
                super.onStarted();
            }

            @Override
            public void onStopped() {
                super.onStopped();
            }

            @Override
            public void onFirstFix(int ttffMillis) {
                super.onFirstFix(ttffMillis);
            }

            @Override
            public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
                super.onSatelliteStatusChanged(status);
                satellitesStatus = new StringBuilder();
                Log.i("GNSS TOTAL: ",  String.valueOf(status.getSatelliteCount()));
                for (int i = 0; i < status.getSatelliteCount(); i++){
                    satellitesStatus.append(status.usedInFix(i)).append(" ");
                }
                if (satellitesStatus.toString().contains("true")){
                    Log.i("GNSS USED: ",  satellitesStatus.toString());
                }
                //Toast.makeText(context, satellitesStatus.toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }
    public StringBuilder getSatellitesStatus() {
        return satellitesStatus;
    }
}