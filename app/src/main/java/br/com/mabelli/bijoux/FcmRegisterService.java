package br.com.mabelli.bijoux;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by feliciano on 24/06/17.
 */

public class FcmRegisterService  extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        sharedPreferences.edit().putString("token", token).apply();
    }
}
