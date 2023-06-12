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

/**
 * Classe che estende Service ed implementa LacationListner, per la gesione dei servizi legati alla
 * geolocalizzazione.
 */
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

    /**
     * Costruttore del GpsTracker, si oppuca della fase di inizializzaizone del GpsTracker, tramite
     * il metodo initGpsTracker().
     * @param context tipo Context, il contesto del chiamante
     */
    public GpsTracker(Context context) {
        this.context = context;
        this.initGpsTracker();
    }

    /**
     * Inizializza l'attributo locationManager richiamando il servizio di sistema
     * LOCATION_SERVICE, tramite il quale accediamo ai servizi di geolocalizzaizone.
     * Inizializza l'attributo bestProvider, richiedendo il miglior provider disponibile, tramite l'uso
     * dei criteri con l'oggetto Criteria (Deprecato api34). Imposta GnssStausListner trmite il metodo.
     * Registra una chiamata di status Gnss tramite il LacationManager, sempre tramite questo imposta
     * il provider sia Network che Gps.
     *
     */
        @SuppressLint("MissingPermission")
    private void initGpsTracker(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        this.locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        //Log.i("BEST PROVIDER: ", this.bestProvider);
        setGnssStatusListner();
        this.locationManager.registerGnssStatusCallback(mGnssStatusCallback);
        this.bestProvider  = locationManager.getBestProvider(criteria, true);
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

    /**
     * Setting dell'ultima posizione conosciuta(rilevata).
     */
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

    /**
     * setting del miglior provider e dei tempi di acquisizione del gps
     */
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

    /**
     *metodo di callback che consente l'associazione con il servizio.
     * @param intent tipo Intent
     * @return tipo IBinder definisce un interfaccia con la quale i client possono interagire con il
     * servizio.
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Metodo di callback, della classe anonima LocationListner, chiamato quando il device riceve
     * una Location Aggiornata.
     * setta gli attributi latitude e longitude
     * @param location tipo Location.
     */
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

    /**
     * Setta un GnssStatusListner, creando un oggetto GnssStatus.Callback e chiamando i metodi che
     * implementa. onStarted() chaimato quando il sistema Gnss è stato attivato. onStopped() chiamato
     * quando il sistema GNSS si è fermato. onFirstFix(int ttffMillis) Chiamato quando il sistema
     * GNSS ha ricevuto il primo fix dall'avvio. onSatelliteStatusChanged(GnssStatus status)
     * Chiamato periodicamente per segnalare lo stato del satellite GNSS. Inizializza l'attributo
     * satellitesStatus, aggiungendo tutti i satelliti disponibili e discriminando quelli in uso.
     */
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
    public void unsetGnssStatusListner(){
        this.locationManager.unregisterGnssStatusCallback(mGnssStatusCallback);
    }

    /**
     * Metodo di getter
     * @return tipo StringBuilder, lo stato dei satelliti e quelli agganciati
     */
    public StringBuilder getSatellitesStatus() {
        return satellitesStatus;
    }
}