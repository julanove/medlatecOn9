package medlatec.listview.sync;

import medlatec.listview.auth.Constants;
import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AuthenticatorService extends Service {
    
    // Instance field that stores the authenticator object
    private Authenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }
    
    public static Account GetAccount() {

        final String accountName = "sync";
        return new Account(accountName, Constants.ACCOUNT_TYPE);
    }
    
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
