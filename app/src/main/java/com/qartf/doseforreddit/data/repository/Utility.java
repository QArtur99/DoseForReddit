package com.qartf.doseforreddit.data.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility implements DataRepository.Utility{

    private Context context;

    public Utility(Context context){
        this.context = context;
    }

    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
