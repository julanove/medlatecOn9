package medlatec.listview.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TestSyncService extends Service {
	 
    private static final Object sSyncAdapterLock = new Object();
    private static TestSyncAdapter sSyncAdapter = null;
 
    public TestSyncService()
    {
    	super();
    }
    
    @Override
    public void onCreate() {
    	
    	super.onCreate();
    	
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null)
                sSyncAdapter = new TestSyncAdapter(getApplicationContext(), true);
            
            Log.d("CREATE", "OK");
        }
        
        Log.d("CREATE", "FAILD");
    }
 
    @Override
    public IBinder onBind(Intent intent) {
    	Log.d("CREATE", "BIND");
        return sSyncAdapter.getSyncAdapterBinder();
        
    }
}