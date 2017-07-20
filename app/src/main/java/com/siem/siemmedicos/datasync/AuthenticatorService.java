package com.siem.siemmedicos.datasync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    Authenticator mAuthenticator;

    public AuthenticatorService() {
        super();
        mAuthenticator = new Authenticator(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

}