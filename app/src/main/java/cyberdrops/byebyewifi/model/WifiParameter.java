package cyberdrops.byebyewifi.model;

import androidx.annotation.NonNull;

public class WifiParameter {
    private String ssid;
    private String bssid;
    private String pwrSignal;

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
