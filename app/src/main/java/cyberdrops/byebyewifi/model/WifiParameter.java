package cyberdrops.byebyewifi.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wifi_parameters")
public class WifiParameter {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idParameters")
    private int idWifiParameters;
    @ColumnInfo(name = "ssid")
    private String ssid;
    @ColumnInfo(name = "bssid")
    private String bssid;
    @ColumnInfo(name = "pwrSignal")
    private String pwrSignal;
    @ColumnInfo(name = "frequency")
    private String frequency;
    @ColumnInfo(name = "encryption")
    private String encryption;
    @ColumnInfo(name = "latitude")
    private String latitude;
    @ColumnInfo(name = "longitude")
    private String longitude;

    public int getIdWifiParameters() {
        return idWifiParameters;
    }

    public void setIdWifiParameters(int idWifiParameters) {
        this.idWifiParameters = idWifiParameters;
    }
    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getPwrSignal() {
        return pwrSignal;
    }

    public void setPwrSignal(String pwrSignal) {
        this.pwrSignal = pwrSignal;
    }

    @NonNull
    @Override
    public String toString() {
        String wifiParameterFormat = """
                ssid:%s bssid:%s pwrSignal:%s frequency:%s encryption:%s latitude:%s longitude:%s""";
        return String.format(wifiParameterFormat,ssid,bssid,pwrSignal,frequency,encryption,latitude,longitude);
    }
}
