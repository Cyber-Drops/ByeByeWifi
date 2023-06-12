package cyberdrops.byebyewifi.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interfaccia Dao che implementa i metodi per la tabella wifi_parameter
 */
@Dao
public interface DaoWifiParameters {

    @Insert
    void saveWifiParameters(WifiParameter wifiParameter);

    @Update
    void updateWifiParameters(WifiParameter wifiParameter);

    @Delete
    void deleteWifiParameters(WifiParameter wifiParameter);
    @Query("delete from wifi_parameters where idParameters = :idWifiParameters")
    void deleteWifiParameters(Integer idWifiParameters);

    @Query("select * from wifi_parameters")
    List<WifiParameter> getAllParameters();
}
