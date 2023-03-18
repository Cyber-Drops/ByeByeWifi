package cyberdrops.byebyewifi.recyclehorder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cyberdrops.byebyewifi.R;

public class ApRecycleHolder extends RecyclerView.ViewHolder {

    private TextView ssidTextView;
    private TextView bssidTextView;
    private TextView pwrSignalTextView;
    private View view;

    public ApRecycleHolder(@NonNull View itemView) {
        super(itemView);
        ssidTextView = (TextView) itemView.findViewById(R.id.ssidTextViewID);
        bssidTextView = (TextView) itemView.findViewById(R.id.bsidTextViewID);
        pwrSignalTextView = (TextView) itemView.findViewById(R.id.pwrSignalTextViewID);
        view = itemView;
    }
}
