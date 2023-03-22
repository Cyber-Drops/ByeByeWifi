package cyberdrops.byebyewifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cyberdrops.byebyewifi.model.WifiDbManager;
import cyberdrops.byebyewifi.model.WifiParameter;

public class DataFromDb extends AppCompatActivity {

    private List<WifiParameter> wifiParameters = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_from_db);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Context context = this.getApplicationContext();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                wifiParameters = WifiDbManager.getWifiDbManagerInstance(context).getDaoWifiParameters().getAllParameters();
                for (WifiParameter wifiParameter : wifiParameters) {
                    System.out.println(wifiParameter);
                }
            }
        });
    }
}