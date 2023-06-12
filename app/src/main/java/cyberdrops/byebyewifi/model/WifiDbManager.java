package cyberdrops.byebyewifi.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Classe astratta per il DBMANAGER, tiene conto della versione, della tabella passata come classe,
 * utilizza il pattern Singleton per avere una ed una sola istanza di quel DBMANAGER.
 */
@Database(entities = {WifiParameter.class}, version = 1, exportSchema = true)
public abstract class WifiDbManager extends RoomDatabase {
    private static WifiDbManager wifiDbManager = null;

    public static synchronized WifiDbManager getWifiDbManagerInstance(Context context){
        if (wifiDbManager == null){
            wifiDbManager = Room.databaseBuilder(context.getApplicationContext(), WifiDbManager.class, "wifi_parameters").build();
        }
        return wifiDbManager;
    }

    /**
     * Getter per il dao del db e della tabella specificati.
     * @return tipo DaoWifiParameters
     */
    public abstract DaoWifiParameters getDaoWifiParameters();
}
