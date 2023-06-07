package cyberdrops.byebyewifi;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Bundle;

import cyberdrops.byebyewifi.services.WifiConnector;

public class DictionaryAttack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_attack);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WifiConnector wifiConnector = new WifiConnector(getApplicationContext());
        wifiConnector.setWifiManager();
        WifiNetworkSuggestion suggestion = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) { //api 29 in su
            suggestion = new WifiNetworkSuggestion.Builder().setSsid("casa-5").setWpa2Passphrase("TEMPESTA-home-2013").build();
            wifiConnector.putNetworkSuggestion(suggestion);
            int status = wifiConnector.getWifiManager().addNetworkSuggestions(wifiConnector.getNetworkSuggestions());
            System.out.println("STATUS++ "+status);
            if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                System.out.println("STATUS-- "+status);
            }
        }



    }
}