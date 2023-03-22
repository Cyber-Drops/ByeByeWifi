package cyberdrops.byebyewifi.model;

import androidx.annotation.NonNull;

public class WifiParameter {
    private String ssid;
    private String bssid;
    private String pwrSignal;
    private String frequency;
    private String encryption;
    private String latitude;
    private String longitude;

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
                ssid:%s
                bssid:%s
                pwrSignal:%s""";
        return String.format(wifiParameterFormat,ssid,bssid,pwrSignal);
    }
}
