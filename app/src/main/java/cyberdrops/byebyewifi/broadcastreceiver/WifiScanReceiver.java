package cyberdrops.byebyewifi.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiScanReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
        if (success){
            Log.i("WIFISCANN", "NUOVA RISULTATI IN SCANSIONE");
        }else {
            Log.i("WIFISCANN", "VECCHI RISULTATI IN SCANSIONE");
        }
    }
}
