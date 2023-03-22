package cyberdrops.byebyewifi.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.util.EthiopicCalendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.telecom.StatusHints;

import androidx.annotation.NonNull;

public class GpsTracker extends Service implements LocationListener {
    private Context context;
    private LocationManager locationManager = null;
    Location location = null;

    private boolean isNetworkPEnabled = false;
    private boolean isGpsPEnabled = false;

    private static final long TIME_TO_UPDATE_LOCATION  = 30000;
    private static final long DISTANCE_TO_UPDATE_LOCATION  = 4;

    private double latitude;
    private double longitude;

    public GpsTracker(Context context) {
        this.context = context;
        this.initGpsTracker();
    }

    private void initGpsTracker(){
        this.locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        this.isNetworkPEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        this.isGpsPEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isNetworkPEnabled){
            enableNetworkProvider();
        }
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

    @SuppressLint("MissingPermission") //Richiesto nella MainActivity
    private void enableNetworkProvider(){
        //TODO procedura di attivazione network, se non attivi esci
        if (isNetworkPEnabled){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_TO_UPDATE_LOCATION, DISTANCE_TO_UPDATE_LOCATION, this );
        }
    }
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
}