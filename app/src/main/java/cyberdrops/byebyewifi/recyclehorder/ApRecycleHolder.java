package cyberdrops.byebyewifi.recyclehorder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cyberdrops.byebyewifi.R;

/**
 * Holder custom che estende un RecycleView.ViewHolder
 */
public class ApRecycleHolder extends RecyclerView.ViewHolder {

    private TextView ssidTextView;
    private TextView bssidTextView;
    private TextView pwrSignalTextView;
    private TextView frequencyTextView;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private View view;

    /**
     * Costruttore setta gli attributi di classe con tutti i riferimenti ai componenti grafici del
     * layout che viene usato per definire la recycleview.
     * @param itemView oggetto di tipo View, fa riferimento alla view della recycleView,quindi al
     *                 layout grafico, così che si possa accedere ai suoi componenti
     */
     public ApRecycleHolder(@NonNull View itemView) {
        super(itemView);
        ssidTextView = (TextView) itemView.findViewById(R.id.ssidTextViewID);
        bssidTextView = (TextView) itemView.findViewById(R.id.bsidTextViewID);
        pwrSignalTextView = (TextView) itemView.findViewById(R.id.pwrSignalTextViewID);
        frequencyTextView = (TextView) itemView.findViewById(R.id.frequencyTextViewID);
        latitudeTextView = (TextView) itemView.findViewById(R.id.latitudeTextViewID);
        longitudeTextView = (TextView) itemView.findViewById(R.id.longitudeTextViewID);
        view = itemView;
    }
//Definisco per ogni attributo i metodi di setter e di getter
    public TextView getSsidTextView() {
        return ssidTextView;
    }

    public void setSsidTextView(TextView ssidTextView) {
        this.ssidTextView = ssidTextView;
    }

    public TextView getBssidTextView() {
        return bssidTextView;
    }

    public void setBssidTextView(TextView bssidTextView) {
        this.bssidTextView = bssidTextView;
    }

    public TextView getPwrSignalTextView() {
        return pwrSignalTextView;
    }

    public void setPwrSignalTextView(TextView pwrSignalTextView) {
        this.pwrSignalTextView = pwrSignalTextView;
    }

    public TextView getFrequencyTextView() {
        return frequencyTextView;
    }

    public void setFrequencyTextView(TextView frequencyTextView) {
        this.frequencyTextView = frequencyTextView;
    }

    public TextView getLatitudeTextView() {
        return latitudeTextView;
    }

    public void setLatitudeTextView(TextView latitudeTextView) {
        this.latitudeTextView = latitudeTextView;
    }

    public TextView getLongitudeTextView() {
        return longitudeTextView;
    }

    public void setLongitudeTextView(TextView longitudeTextView) {
        this.longitudeTextView = longitudeTextView;
    }
}
