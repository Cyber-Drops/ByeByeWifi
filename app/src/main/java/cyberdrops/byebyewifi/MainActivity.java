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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

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
                System.out.println("TRDTBTN");
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