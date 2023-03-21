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
        holder.getPwrSignalTextView().setText(wifiParameters.get(position).getPwrSignal());
        holder.getSsidTextView().setText(wifiParameters.get(position).getSsid());
        holder.getBssidTextView().setText(wifiParameters.get(position).getBssid());
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
