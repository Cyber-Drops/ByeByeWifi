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

public class ApRecycleAdapter extends RecyclerView.Adapter<ApRecycleHolder>{

    private List<WifiParameter> wifiParameters;

    private Context context;

    //private ClickListner clickListner;

    public ApRecycleAdapter(List<WifiParameter> wifiParameters, Context context){
        this.wifiParameters = wifiParameters;
        this.context = context;
    }

    @NonNull
    @Override
    public ApRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ap_layout_recycleview,parent,false);
        return new ApRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApRecycleHolder holder, int position) {
        //TODO CREARE METODO GESTIONE TEXTSIZE
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
        holder.getPwrSignalTextView().setText(wifiParameters.get(position).getPwrSignal());
        holder.getSsidTextView().setText(wifiParameters.get(position).getSsid().substring(0,endSubstringSsid));
        holder.getBssidTextView().setText(wifiParameters.get(position).getBssid());
        holder.getLatitudeTextView().setText(wifiParameters.get(position).getLatitude().substring(0,endSubstringLat));
        holder.getFrequencyTextView().setText(wifiParameters.get(position).getFrequency());
        holder.getLongitudeTextView().setText(wifiParameters.get(position).getLongitude().substring(0,endSubstringLong));
    }

    @Override
    public int getItemCount() {
        return wifiParameters.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
