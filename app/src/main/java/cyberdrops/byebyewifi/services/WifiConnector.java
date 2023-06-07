package cyberdrops.byebyewifi.services;

import static android.content.Context.WIFI_SERVICE;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe per la gestione della connessione wifi del dispositivo
 * networkSuggestions privato, tipo lista di WifiNetworkSuggestion
 * context privato, tipo Context
 * wifiManager privato, tipo WifiManager
 */
public class WifiConnector {

    private List<WifiNetworkSuggestion> networkSuggestions; //Lista WifiNetworkSuggestion, oggetto per fornire una rete wifi da prendere in considerazione per la connessione automatica
    private Context context; //Contesto activity chiamante
    private WifiManager wifiManager = null; //SystemService WIFI_SERVICE
    /**
     * Costruttore, inizializza gli attributi privati context con il Context dell'activity e
     * networkSuggestions con una lista vuota.
     * @param context tipo Context, contesto activity chiamante
     */
    public WifiConnector(Context context){
        this.context = context;
        this.networkSuggestions = new ArrayList<>();
    }

    /**
     * Metodo Setter, inizializza settandolo, il parametro privato wifiManager con il tipo WifiManager dopo customizzazione,
     * richiamando dal context il SystemService WIFI_SERVICE
     */
    public void setWifiManager() {
        this.wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
    }

    /**
     * Metodo Getter, restituisce l'attributo privato wifiManager
     * @return wifiManager, attributo privato, tipo WifiManager (SystemService-WIFI_SERVICE)
     */
    public WifiManager getWifiManager() {
        return wifiManager;
    }

    /**
     * Metodo che aggiunge un oggetto WifiNetworkSuggestion all' attributo privato lista
     * @param networkSuggestions tipo WifinetworkSuggestion
     */
    public void putNetworkSuggestion(WifiNetworkSuggestion networkSuggestions) {
        this.networkSuggestions.add(networkSuggestions);
    }

    /**
     * Metodo getter
     * @return l'attributo privato Lista di oggetti WifiNetworkSuggestion
     */
    public List<WifiNetworkSuggestion> getNetworkSuggestions() {
        return networkSuggestions;
    }
}
