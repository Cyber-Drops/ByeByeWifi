package cyberdrops.byebyewifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * Class MainActivity, logica della schermata principale,
 * collegata al layout grafico activity_main.xml
 * Implementa l'interfaccia View.OnClickListner, e fa l'override del suo metodo onClick
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Override del metodo onCreate, creo l'app, inizio allocazione risorse,
     * invoca il metodo setContentView(), passando come parametro il layout da visualizzare.
     * Nasconde la action bar invocando il metodo getSupportActionBar().hide().
     * @param savedInstanceState Il savedInstanceState è un riferimento a un oggetto Bundle che
     *                           viene passato nel metodo onCreate di ogni attività Android.
     *                           Le attività hanno la possibilità, in circostanze speciali, di
     *                           ripristinarsi a uno stato precedente utilizzando i dati archiviati
     *                           in questo pacchetto.  Se non sono disponibili dati dell'istanza,
     *                           saveInstanceState sarà null.  Ad esempio, il savedInstanceState sarà
     *                           sempre nullo la prima volta che un'attività viene avviata, ma
     *                           potrebbe non essere nullo se un'attività viene distrutta durante la rotazione.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    /**
     * Override metodo onStart, caricamento dell'interfaccia grafica, visibile ma non utilizzabile.
     * Crea una lista di permessi per l'utilizzo corretto dell'applicazione e li richiede all'utente,
     * se non sono già stati richiesti.
     */
    @Override
    protected void onStart() {
        super.onStart();
        List<String> permissions = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED){
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.INTERNET);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WAKE_LOCK);
        }
        if (!permissions.isEmpty()){
            String[] permissionsArr = new String[permissions.size()];
            permissionsArr = permissions.toArray(permissionsArr);
            ActivityCompat.requestPermissions(this, permissionsArr, 123);
        }

    }

    /**
     * Override del metodo onClick dell'interfaccia View.OnClickListner.
     * gestisce l'evento del click, recuperando l'id dell'elemento grafico premuto, tramite il quale
     * crea diversi oggetti di tipo Intent passando il contesto attuale e la classe da richiamare,
     * così da passare ad una nuova activity tramite il metodo startActivity che accetta come
     * parametro un Intent, che avrà un'altra grafica ed un altra logica.
     * @param view oggetto di tipo View, che rende accessibili le risorse grafiche per
     *             l'interazione.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.wiFiTextBTNID:
                Intent wifiLocalizerIntent = new Intent(this,WifiLocalizer.class);
                startActivity(wifiLocalizerIntent);
                break;
            case R.id.distanceAPTextBTNID:
                System.out.println("SCNDBTN");
                break;
            case R.id.dictionaryAttackTextBTNID:
                Intent dictionaryAttackIntent = new Intent(this, DictionaryAttack.class);
                startActivity(dictionaryAttackIntent);
                break;
            case R.id.wifiAnalyzerTextBTNID:
                System.out.println("FRTBTN");
                break;
            case R.id.dataFromDBTextBTNID:
                Intent dataFromDbIntent = new Intent(this,DataFromDb.class);
                startActivity(dataFromDbIntent);
                break;
            default:
                System.out.println("DEFAULT: "+view.getId());
        }

    }
}