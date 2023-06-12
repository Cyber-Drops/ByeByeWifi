package cyberdrops.byebyewifi.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.zip.Inflater;

import cyberdrops.byebyewifi.R;
import cyberdrops.byebyewifi.model.WifiParameter;
import cyberdrops.byebyewifi.recyclehorder.ApRecycleHolder;

/**
 * Definisco un adapter custom, che estende un RecycleView Adapter (gli passo un Holder custom)
 */
public class ApRecycleAdapter extends RecyclerView.Adapter<ApRecycleHolder>{

    private List<WifiParameter> wifiParameters;

    private Context context;

    //private ClickListner clickListner;

    /**
     * Costruttore RecycleAdapter custom al quale passo
     * @param wifiParameters
     * @param context
     */
    public ApRecycleAdapter(List<WifiParameter> wifiParameters, Context context){
        this.wifiParameters = wifiParameters;
        this.context = context;
    }

    /**
     * Metodo di callback che istanzia l'holder (una sola volta), quando viene istanziato l'adapter.
     * All'interno con un inflater creo l'oggetto View basato sul layout custom di cui ho definito i
     * componenti nel ViewHolder custom.
     * @param parent Oggetto ViewGroup in cui verrà aggiunta la nuova vista dopo che è stata associata
     *       una posizione dell'adattatore.
     * @param viewType Il tipo di visualizzazione della nuova visualizzazione..
     *
     * @return Holder Custom sulla base della view creata.
     */
    @NonNull
    @Override
    public ApRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ap_layout_recycleview,parent,false);
        return new ApRecycleHolder(view);
    }

    /**
     *Metodo di callback chiamato ogni volta che si dovranno customizzare (cambiare) le informazioni
     * dell'interfaccia grafica, tramite il riferimento alla posizione dell'oggetto grafico da
     * cambiare.
     * Aggiorna gli elementi grafici sulla base delle informazioni recuperate dalla lista passata
     * wifiParameters (scrive a schermo i parametri delle scansioni e la geolocalizzazione)
     * @param holder Il ViewHolder che deve essere aggiornato per rappresentare il contenuto di
     *       elemento nella posizione data nel set di dati.
     * @param position La posizione dell'elemento all'interno del set di dati dell'adattatore.
     */
    @Override
    public void onBindViewHolder(@NonNull ApRecycleHolder holder, int position) {
        //TODO CREARE METODO GESTIONE TEXTSIZE
        //Gestiisco la posizione del testo e la lunghezza delle varie stringhe da visualizzare
        int endSubstringSsid = 0;
        int endSubstringLat = 0;
        int endSubstringLong = 0;
        if ( wifiParameters.get(position).getLatitude().length() >= 5 && wifiParameters.get(position).getLongitude().length() >= 5){
            endSubstringLat = 5;
            endSubstringLong = 5;
        }else {
            endSubstringLat = wifiParameters.get(position).getLatitude().length();
            endSubstringLong = wifiParameters.get(position).getLongitude().length();
        }
        if (wifiParameters.get(position).getSsid().length() >= 5){
            endSubstringSsid = 5;
        }else {
            endSubstringSsid = wifiParameters.get(position).getSsid().length();
        }
        /*Recuper tramite il custom holder gli elementi del layout da modificare sulla base dei dati
        della lista passata all'adapter*/
        holder.getPwrSignalTextView().setText(wifiParameters.get(position).getPwrSignal());
        holder.getSsidTextView().setText(wifiParameters.get(position).getSsid().substring(0,endSubstringSsid));
        holder.getBssidTextView().setText(wifiParameters.get(position).getBssid());
        holder.getLatitudeTextView().setText(wifiParameters.get(position).getLatitude().substring(0,endSubstringLat));
        holder.getFrequencyTextView().setText(wifiParameters.get(position).getFrequency());
        holder.getLongitudeTextView().setText(wifiParameters.get(position).getLongitude().substring(0,endSubstringLong));
    }

    /**
     * Getter degli elementi da visualizzare
     * @return un intero, la grandezza della lista
     */
    @Override
    public int getItemCount() {
        return wifiParameters.size();
    }

    /**
     * Metodo di callback chiamato da RecyclerView quando inizia a osservare questo Adapter.
     * Stesso adattatore può essere osservato da più RecyclerView.
     * @param recyclerView L'istanza di RecyclerView che ha iniziato a osservare questo adattatore.
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
