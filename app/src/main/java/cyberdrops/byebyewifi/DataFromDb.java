package cyberdrops.byebyewifi;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cyberdrops.byebyewifi.model.WifiDbManager;
import cyberdrops.byebyewifi.model.WifiParameter;

public class DataFromDb extends AppCompatActivity implements View.OnClickListener {

    private Context applicationContext = null;
    private List<WifiParameter> wifiParameters = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_from_db);
        this.applicationContext = this.getApplicationContext();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    ActivityResultLauncher<String> getmGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            System.out.println("ENTRATO");
            System.out.println(result.getPath());
        }
    });

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.exportToFileFromDBTextBTNID -> {
                 exportToFile();
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
                 resetDataDb();
                 break;
             }
             default -> Toast.makeText(applicationContext, R.string.makeAchoose, Toast.LENGTH_SHORT).show();
         }
    }
    private boolean exportToFile(){
        setWifiParameters();
        /*
        if (wifiParameters != null && wifiParameters.size() > 0 ){

        }
         */
        String externaleStorageState = Environment.getExternalStorageState();
        System.out.println(externaleStorageState);
        if (externaleStorageState.equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY)){
            //TODO READONLY
        }else{
            //TODO WRITE AND READ
        }
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.setType("*/*");
        //intent.putExtra(Intent.EXTRA_TITLE, "invoice.txt");
        //intent.getCharSequenceExtra("path");
        //startActivityForResult(intent,1);

        getmGetContent.launch("*/*");
        System.out.println(applicationContext.getExternalFilesDir("/storage"));
        Toast.makeText(applicationContext, externaleStorageState + "\n" + applicationContext.getExternalFilesDir(null), Toast.LENGTH_SHORT).show();
        return true;
    }
    private void setWifiParameters(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                wifiParameters = WifiDbManager.getWifiDbManagerInstance(applicationContext).getDaoWifiParameters().getAllParameters();
                for (WifiParameter wifiParameter : wifiParameters) {
                    System.out.println(wifiParameter);
                }
            }
        });
    }
    private boolean sendFileToEmail(){
        return true;
    }
    private boolean sendFileToWhatsUp(){
        return true;
    }
    private boolean resetDataDb(){
        return true;
    }
}