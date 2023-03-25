package cyberdrops.byebyewifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.EthiopicCalendar;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.sax.ElementListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cyberdrops.byebyewifi.adapter.ApRecycleAdapter;
import cyberdrops.byebyewifi.broadcastreceiver.WifiScanReceiver;
import cyberdrops.byebyewifi.model.WifiDbManager;
import cyberdrops.byebyewifi.model.WifiParameter;
import cyberdrops.byebyewifi.recyclehorder.ApRecycleHolder;
import cyberdrops.byebyewifi.services.GpsTracker;

public class WifiLocalizer extends AppCompatActivity {

    private ApRecycleAdapter apRecycleAdapter;
    private RecyclerView wifiLocalizerRecyclerView;
    private TextView gnssStatusTextview;
    private GpsTracker gpsTracker = null;
    private WifiScanReceiver wifiScanReceiver;
    private IntentFilter wifiIntentFilter;
    private WifiManager wifiManager;
    private WifiManager.WifiLock wifiLock;
    private PowerManager.WakeLock powerManagerLock;
    private List<ScanResult> scanResultList;
    private List<WifiParameter> wifiParameters;
    private List<WifiParameter> totalWifiParameters;
    private Boolean upgradeDb = false;
    private Boolean scanStart = false;
    private HashMap<String, WifiParameter> wifiParameterHashMap;
    private HashMap<String, WifiParameter> totalWifiParameterHashMap;
    private PowerManager powerManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_localizer);
        getWindow(). addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();
        initWifiLocalizer();
    }
    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.start_stopWifiLocalizerBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean startStop = isStarted(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifiScanReceiver,wifiIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(wifiScanReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanReceiver);
        if (wifiLock.isHeld()){
            wifiLock.release();
        }
        if (powerManagerLock.isHeld()){
            powerManagerLock.release();
        }
        gpsTracker.stopSelf();
    }

    @SuppressLint("InvalidWakeLockTag")
    private void initWifiLocalizer(){
        //TODO inizializzazione oggetto GpsTracker
        this.gpsTracker = new GpsTracker(this);
        gpsTracker.setBestProvider();
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        powerManagerLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "No sleep");
        wifiParameterHashMap = new HashMap<>();
        totalWifiParameterHashMap = new HashMap<>();
        wifiParameters = new ArrayList<>();
        totalWifiParameters = new ArrayList<>();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiScanReceiver = new WifiScanReceiver();
        wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        //wifiManager.setWifiEnabled(true);
        wifiLock =  wifiManager.createWifiLock(WifiManager.ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE);
        if (!powerManagerLock.isHeld()){
            powerManagerLock.acquire(10*60*1000L /*10 minutes*/);
        }
        if (!wifiLock.isHeld()){
            wifiLock.acquire();
        }
    }

    private boolean isStarted(View view){
        TextView textViewBTN = findViewById(view.getId());
        if (textViewBTN.getText().toString().strip().equalsIgnoreCase(getResources().getString(R.string.wifi_localizer_btn_start))){
            textViewBTN.setText(getResources().getString(R.string.wifi_localizer_btn_stop));
            resetUI();
            scanStart = true;
            Toast.makeText(getApplicationContext(),"Scansione Avviata", Toast.LENGTH_SHORT).show();
            startWifiScann();
            return true;
        }else {
            textViewBTN.setText(getResources().getString(R.string.wifi_localizer_btn_start));
            scanStart = false;
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            totalWifiParameters.addAll(totalWifiParameterHashMap.values());
            executorService.execute(()->saveWifiParametersToDb(totalWifiParameters));
            executorService.shutdown();
            Toast.makeText(getApplicationContext(),"Scansione Arrestata", Toast.LENGTH_SHORT).show();
            }
        return false;
    }
    private void startWifiScann(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (scanStart) {
                    if (wifiParameters.size() > 0){
                        resetUI();
                    }
                    //wifiManager.startScan();
                    getApplicationContext().registerReceiver(wifiScanReceiver, wifiIntentFilter);
                    wifiParameters = getWifiParameters();
                    System.out.println("---------------->AVVIO");
                    //saveWifiParametersToDb(wifiParameters);
                    //RecyclerView wifiLocalizerRecyclerView = (RecyclerView) findViewById(R.id.wifi_localizer_recyclerview); //Modifica del 22/03/23
                    wifiLocalizerRecyclerView = (RecyclerView) findViewById(R.id.wifi_localizer_recyclerview);
                    apRecycleAdapter = new ApRecycleAdapter(wifiParameters, getApplicationContext());
                    wifiLocalizerRecyclerView.post(()->wifiLocalizerRecyclerView.setAdapter(apRecycleAdapter));
                    wifiLocalizerRecyclerView.post(()->wifiLocalizerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext())));
                    if (gpsTracker.getSatellitesStatus().toString().contains("true")){
                        gnssStatusTextview = (TextView)findViewById(R.id.gnssStatusNumberLBL);
                        gnssStatusTextview.post(()->gnssStatusTextview.setText("UP"));
                    }
                    SystemClock.sleep(30000);//PER APP IN PRIMO PIANO LIMITAZIONE IMPOSTA A 30 SECONDI A SCANSIONE
                    //PER APP IN BACKGROUND 30 MINUTI A SCANSIONE (LIMITE IMPOSTO DAGLI SVILUPPATORI)
                    //CON SCHERMO BLOCCATO PASSA A 30 MINUTI, COME RIATTIVO LO SCHERMO RIPASSA A 30 SECONDI
            }
        }
        });
        executorService.shutdown();
       resetUI();
    }
    @SuppressLint("MissingPermission")
    private List<WifiParameter> getWifiParameters(){
        scanResultList = wifiManager.getScanResults();
        for (ScanResult scanResult : scanResultList) {
            WifiParameter wifiParameter = new WifiParameter();
            wifiParameter.setSsid(scanResult.SSID);
            wifiParameter.setBssid(scanResult.BSSID);
            wifiParameter.setPwrSignal(String.valueOf(scanResult.level));
            wifiParameter.setFrequency(String.valueOf(scanResult.frequency));
            wifiParameter.setEncryption(scanResult.capabilities);
            wifiParameter.setLatitude(String.valueOf(gpsTracker.getLatitude()));
            wifiParameter.setLongitude(String.valueOf(gpsTracker.getLongitude()));
            System.out.println(gpsTracker.getLatitude());
            System.out.println(gpsTracker.getLongitude());
            wifiParameterHashMap.put(scanResult.BSSID,wifiParameter);
            totalWifiParameterHashMap.put(scanResult.BSSID,wifiParameter);
        }
        wifiParameters.addAll(wifiParameterHashMap.values());
        return wifiParameters;
    }

    private void saveWifiParametersToDb(List<WifiParameter> wifiParameters){
        //TODO return boolean per salvataggio andato a buon fine
        if (upgradeDb){
            Log.i("Database", "upgrade ap to db-> "+wifiParameters.size());
            for (WifiParameter wifiParameter : wifiParameters) {
                WifiDbManager.getWifiDbManagerInstance(this).getDaoWifiParameters().updateWifiParameters(wifiParameter);
            }
        }else {
            Log.i("Database", "save ap to db-> "+wifiParameters.size());
            for (WifiParameter wifiParameter : wifiParameters) {
                WifiDbManager.getWifiDbManagerInstance(this).getDaoWifiParameters().saveWifiParameters(wifiParameter);
            }
            upgradeDb = true;
        }
        resetUI();
    }
    private void resetUI(){
        System.out.println("------------------>CLEAR");
        if (wifiParameters != null && wifiParameterHashMap != null && wifiParameters.size() > 0 && wifiParameterHashMap.size() > 0){
            wifiParameters.clear();
            wifiParameterHashMap.clear();
            if (!scanStart){
                totalWifiParameters.clear();
                totalWifiParameterHashMap.clear();
            }
        }else {
            Log.w("RESET_UI","WIFI OBJECTS IS EMPTY OR NULL");
        }

    }

}