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
import android.widget.CheckBox;
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

/**
 * Classe publica, gestisce la logice associata al layout grafico activity_wifi_localizer,
 * si occupa di gestire l'interfaccia wifi, effettua delle scansioni seriate raccogliendo
 * dati specifici sulle reti wifi, gestisce la batteria per effettuare più scansioni possibili,
 * gestisce il servizio gps per fornire una geolocalizzazione delle reti scnaionate, gestisce una
 * recycle view tramite adapter custom (apRecycleAdapter) per la visualizzazione dei dati, se
 * necessario da il comando di salvataggio dei dati sul db.
 */
public class WifiLocalizer extends AppCompatActivity {

    private ApRecycleAdapter apRecycleAdapter; //adapter custom
    private RecyclerView wifiLocalizerRecyclerView; //recycleView
    private WifiScanReceiver wifiScanReceiver;
    private IntentFilter wifiIntentFilter;
    private WifiManager wifiManager;
    private WifiManager.WifiLock wifiLock;
    private PowerManager.WakeLock powerManagerLock;
    private List<ScanResult> scanResultList;
    private List<WifiParameter> wifiParameters;
    private List<WifiParameter> totalWifiParameters;
    private HashMap<String, WifiParameter> wifiParameterHashMap;
    private HashMap<String, WifiParameter> totalWifiParameterHashMap;
    private PowerManager powerManager;
    private TextView gnssStatusTextview;
    private GpsTracker gpsTracker = null;
    private Boolean upgradeDb = false;
    private Boolean scanStart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_localizer);
        //setting della proprietà della finestra per mantenerla viva, no standby
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();
        initWifiLocalizer(); //Inizializzo il mio wifilocalizer
    }

    /**
     * Imposta un listner slu click della risorsa Btn deputata allo start e stop.
     * alla pressione usa il metodo isstarte() al quale passa la view (riferita al btn cliccato),
     * il quale ritorna un boolen
     */
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
        unregisterReceiver(wifiScanReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(wifiScanReceiver);
        if (wifiLock.isHeld()){
            wifiLock.release();
        }
        if (powerManagerLock.isHeld()){
            powerManagerLock.release();
        }
        gpsTracker.stopSelf();
    }

    /**
     * metodo che inizializza tutte le funzioni base per il wifilocalizer:
     * Inizializza il power managment;
     * Inizializza il modulo wifi;
     * Inizializza il modulo gps.
     */
    private void initWifiLocalizer(){
        initPowerManagment();
        initWifi();
        initTrackerFunctions();
    }

    /**
     *Recupera la textView del btn cliccato, tramite la view passata come parametro,1)se è uguale alla
     * risorsa string btn_start setta il testo tramite la risorsa btm_stop, usa il metodo resetUI(),
     * imposta l'attributo boolean scanStart su true, crea e visualizza un Toast, lancia il metodo
     * startWifiScann() e ritorna true; 2)se è uguale alla stringa btn_stop fa l'operazione inversa(
     * setta il testo con la stringa btn_stop, imposta scanStart su false, recupera la checkBox per
     * il salvataggio suol DB, isChecked()->avvio singleThread ed aggiungo all'HashMap totalWifiParameters
     * tutti i valori contenuti in HashMap totalWifiParameterHashMap e lo passo al metodo saveWifiParameterstoDb,
     * chiudo il thread, creo e visualizzo Toast)
     * @param view Tipo View, del contesto attuale, per il focus dei componenti.
     * @return boolean, true a scansione avviata o false a scansione fermata.
     */
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
            stopGpsTracker();
            CheckBox saveToDbCheckBox = (CheckBox) findViewById(R.id.checkBox2);
            if (saveToDbCheckBox.isChecked()){
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                totalWifiParameters.addAll(totalWifiParameterHashMap.values());
                executorService.execute(()->saveWifiParametersToDb(totalWifiParameters));
                executorService.shutdown();
            }
            Toast.makeText(getApplicationContext(),"Scansione Arrestata", Toast.LENGTH_SHORT).show();
            }
        return false;
    }

    /**
     * Crea un SingleThread, nel quale avvia la scansione, finchè l'attributo scanStart è true. Ad
     * ogni suo avvio, ripulisce la UI se ha dei parametri memorizzati nell'attributo wifiParameters.
     * Registra un BroadcastReceiver nel thread principale dell'applicazione. Recupera i parametri
     * wi-fi scansioonati tramite il metodo getWifiParameters(). Creo la recycleview custom, creo
     * l'adapter custom passandogli il contesto e la lista wifiParameters. Tremite il metodo post()
     * del thread setto l'adapter nella recycleview e setto il layoutManager alla recycleview.
     * Se ho agganciato un segnale gps, setto la textView del gps da Down a Up. Mando il sistema in
     * sleep per 30 secondi, il tempo limite per le scansioni con app in primo piano. Chiudo il thread
     * e ripulisco la UI.
     * l'adapter custom
     */
    //TODO errata formulazione ed uso del broadcastreceiver e intentservice, ristrutturare, ritorna null nel testIntent
    private void startWifiScann(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (scanStart) {
                    if (wifiParameters.size() > 0){
                        resetUI();
                    }
                    wifiManager.startScan();
                    Log.i("START_SCANN deprecated","ESEGUITA");
                    Intent testIntent = getApplicationContext().registerReceiver(wifiScanReceiver, wifiIntentFilter); //ritorna null anzichè l'intent per il broadcastreceiver
                    Log.w("INTENT TROVATO: ", String.valueOf(testIntent));
                    wifiParameters = getWifiParameters();
                    System.out.println("---------------->AVVIO");
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

    /**
     * Recupera la lista delle scansioni dal wifiManager con il metodo getScanResults(), tramite i
     * parametri delle scansioni, setta gli attributi dell'oggetto WifiParameter, ognuno di essi
     * viene aggiunto all'HashMap wifiParameterHashMap(nome rete, parametri), a sua volta l'hashMap
     * è aggiunto all'HashMap dei parametri totali con la stessa formulazione(nome rete, parametri).
     * Aggiungo alla lista wifiParameters i valori contenuti all'interno dell'HashMap
     * wifiParametersHashMap.
     * @return tipo lista di WifiParametersHashMap, contenenti tutti i parametri delle reti scansionati
     */
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

    /**
     * Aggiorno il DB con i parametri del wifi recuperati dalla lista WifiParameters, uso i metodi
     * getDaoWifiParameters().updateWifiParameters. Oppure inserisco i dati nel Db con il metodo
     * getDaoWifiParameters().saveWifiParameters().
     * @param wifiParameters lista dei parametri recuperati dalle scansioni.
     */
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

    /**
     * Ripulisce la UI, eliminando tutti gli elementi salvati all'interno di wifiParameters,
     * wifiParametersHashMap, totalWifiParameters e totalWifiParameterHashMap, se wifiParameters e
     * wifiParametersHashMap contengono qualcosa (significa che hanno scansionato wifi).
     */
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

    /**
     * Metodo per impostare la sezione wifi.
     * Inizializza gli attributi implicati nella scansione e raccolta dati inerente al modulo wifi:
     * tra questi ci sono oggetti di tipo ArrayList ed HashMap, i qali gestiscono la raccolta dei
     * dati;
     * WifiManager richiamo il servizio di sistema, per la gestione di tutti gli aspetti legati alla
     * connettività wifi.
     * wifiScanReceiver, inizializzo il mio broadcastreceiver castom dedicato al wifi (ascoltatore)
     * wifiIntentFilter, inizializzo un filtro per il mio ascoltatore Wifi, che verrà chiamato quando
     * le richieste di scansione sono completate(SCAN_RESULTS_AVAILABLE_ACTION), lo applico al wifiManager
     * wifiLock, creao un lock per una scansione sempre discponibile e lo applico al wifimanager.
     * Verifico se il wifi è On, altrimenti lo setto su On
     */
    private void initWifi(){
        wifiParameterHashMap = new HashMap<>();
        totalWifiParameterHashMap = new HashMap<>();
        wifiParameters = new ArrayList<>();
        totalWifiParameters = new ArrayList<>();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiScanReceiver = new WifiScanReceiver();
        wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiLock =  wifiManager.createWifiLock(WifiManager.ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        if (!wifiLock.isHeld()){
            wifiLock.acquire();
        }
    }

    /**
     * Metodo per impostare le funzioni di alimentazione:
     * richiama il servizio di sistema PowerManager, e imposta un lock su di esso con il flag
     * no sleep, inibendo il risparmio energetico.
     */
    private void initPowerManagment(){
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        powerManagerLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Power:No sleep");
        if (!powerManagerLock.isHeld()){ //Restituisce true se il wakelock è stato acquisito
            powerManagerLock.acquire(10*60*1000L /*10 minutes*/);//Acquisisce il wakelock con un timeout.
        }
    }

    /**
     * Metodo per impostare le funzioni del gps.
     * Istanzion un GpsTracker e setto la proprietà per selezionare il miglior provider di locazione,
     * gps, umts ecc... in automatico
     */
    private void initTrackerFunctions(){
        this.gpsTracker = new GpsTracker(this);
        gpsTracker.setBestProvider();
    }
    private void stopGpsTracker(){
        this.gpsTracker.unsetGnssStatusListner();
    }

}