package cyberdrops.byebyewifi;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cyberdrops.byebyewifi.model.WifiDbManager;
import cyberdrops.byebyewifi.model.WifiParameter;

/**
 * Classe che implementa la logia dell'activity_data_from_db, implementa View.OnClickListner.
 */
public class DataFromDb extends AppCompatActivity implements View.OnClickListener {

    private Context applicationContext = null;
    private List<WifiParameter> wifiParameters = null;
    private boolean isAvailable = false;
    private boolean isWritable = false;
    private boolean isReadable = false;
    private boolean esitoExportToFile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_from_db);
        this.applicationContext = this.getApplicationContext();
    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.exportToFileFromDBTextBTNID -> {
                 setWifiParametersFromDb();
                 break;
             }
             case R.id.sendFileToEmailFromDBTextBTNID -> {
                 sendFileToEmail();
                 break;
             }
             case R.id.sendFileToWhatsUpFromDBTextBTNID -> {
                 sendFileToWhatsUp();
                 break;
             }
             case R.id.resetDBTextBTNID -> {
                 boolean deleteAllTab = resetDataDb();
                 new Handler(Looper.getMainLooper()).post(new Runnable() {
                     @Override
                     public void run() {
                         if(deleteAllTab){
                             Toast.makeText(applicationContext, "ALL TABLE DELETED", Toast.LENGTH_SHORT).show();
                         }else {
                             Toast.makeText(applicationContext, "ERROR TO RESET DB", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
                 break;
             }
             default -> Toast.makeText(applicationContext, R.string.makeAchoose, Toast.LENGTH_SHORT).show();
         }
    }

    /**
     * Verifica lo storage esterno del device tramite il metodo cheackExternalStorageState(), se è
     * scrivibile e la lista WifiParameters contiene qualcosa scrive i parametri in un file di testo,
     * tramite il metodo writeFile(). Tramite un Handler ed un Looper crea e visualizza un Toast per
     * l'esito.
     * @param wifiParameters Lista di oggetti del tipo WifiParameters
     * @return boolean
     */
    private boolean exportToFile(List<WifiParameter> wifiParameters){
        cheackExternalStorageState();
        if (isAvailable && isWritable){
            if (wifiParameters != null && wifiParameters.size() > 0 ){
                esitoExportToFile = writeFile(wifiParameters);
            }
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String esitoString;
                if (esitoExportToFile){
                    esitoString = "SALVATO IN DOWNLOAD";
                }else {
                    esitoString = "ERRORE DI SALVATAGGIO";
                }
                Toast.makeText(applicationContext, esitoString, Toast.LENGTH_SHORT).show();
            }
        });
        System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        return true;
    }

    /**
     * Legge tutti i parametri wifi dal db e passa una lista di oggetti WifiParameters al metodo
     * per l'esportazione su file di testo exportToFile().
     */
    private void setWifiParametersFromDb(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                wifiParameters = WifiDbManager.getWifiDbManagerInstance(applicationContext).getDaoWifiParameters().getAllParameters();
                exportToFile(wifiParameters);
            }
        });
        executorService.shutdown();
    }

    /**
     * Metodo per il controllo dello storage esterno al device, verifica lo stato dello storage e
     * setta gli attributi isAvailable, isReadable, isWritable (di default 3 false), Mounted (3 true),
     * Mounted_Read_Only(2 true).
     */
    private void cheackExternalStorageState(){
        String externaleStorageState = Environment.getExternalStorageState();
        if (externaleStorageState.equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
            System.out.println("UNO");
            isAvailable = true;
            isReadable = true;
            isWritable = true;
        }else if (externaleStorageState.equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY)){
            System.out.println("DUE");
            isAvailable = true;
            isReadable = true;
        }else {
            System.out.println("TRE");

            return;
        }
    }

    /**
     * Recupera la directory Download dello storage esterno, crea quì un nuovo file txt, scrive il
     * file tramite un FileWriter()
     * @param wifiParameters
     * @return boolean attributo false di default, true succes or false failed.
     */
    private boolean writeFile(List<WifiParameter> wifiParameters){
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(folder, "AccesPoint_Localized.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            for (WifiParameter wifiParameter:wifiParameters) {
                fileWriter.write(wifiParameter.toString()+System.getProperty("line.separator"));
                fileWriter.flush();
            }
            fileWriter.close();
            Log.e("DATA FROM DB", "Scrittura avvenuta con successo");
            esitoExportToFile = true;
        } catch (FileNotFoundException e) {
            return esitoExportToFile;
        } catch (IOException e) {
            Log.e("DATA FROM DB", "IOException");
            return esitoExportToFile;
        }
        return esitoExportToFile;
    }
    private boolean sendFileToEmail(){
        return true;
    }
    private boolean sendFileToWhatsUp(){
        return true;
    }

    /**
     * Metodo per il reset dei dati sul db, crea un Thread e ripulisce il db usando il metodo
     * clearAllTables(), di deafult nella libreria RoomDataBase.
     * @return boolean true per esito positivo
     */
    private boolean resetDataDb(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                WifiDbManager.getWifiDbManagerInstance(applicationContext).clearAllTables();
            }
        });
        executorService.shutdown();
        return true;
    }
}