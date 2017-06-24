package br.com.mabelli.bijoux;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by feliciano on 24/06/17.
 */

public class FcmRegisterService  extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase.getInstance().getReference().child("fcmkey/FcmToken")
                .setValue(token);
    }
}
