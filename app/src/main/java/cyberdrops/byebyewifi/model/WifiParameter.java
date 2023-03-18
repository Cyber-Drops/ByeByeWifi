package cyberdrops.byebyewifi.model;

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
}
