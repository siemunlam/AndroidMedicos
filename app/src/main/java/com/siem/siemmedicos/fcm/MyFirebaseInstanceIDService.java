package com.siem.siemmedicos.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.siem.siemmedicos.utils.PreferencesHelper;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
        //Log.i("123456789", "Aca: " + refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        PreferencesHelper preferences = PreferencesHelper.getInstance();
        preferences.setFirebaseToken(token);
    }

}
