package cyberdrops.byebyewifi.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WifiParameter.class}, version = 1, exportSchema = true)
public abstract class WifiDbManager extends RoomDatabase {
    private static WifiDbManager wifiDbManager = null;

    public static synchronized WifiDbManager getWifiDbManagerInstance(Context context){
        if (wifiDbManager == null){
            wifiDbManager = Room.databaseBuilder(context.getApplicationContext(), WifiDbManager.class, "wifi_parameters").build();
        }
        return wifiDbManager;
    }

    public abstract DaoWifiParameters getDaoWifiParameters();
}
