package medlatec.listview;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class GetGPS extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_get_gps);
		
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	}

	public class MyLocationListener implements LocationListener

	{

	@Override

	public void onLocationChanged(Location loc)

	{

	loc.getLatitude();

	loc.getLongitude();

	String Text = "My current location is: " + "Latitud = " + loc.getLatitude() + "Longitud = " + loc.getLongitude();


	Toast.makeText( getApplicationContext(),

	Text,

	Toast.LENGTH_SHORT).show();

	}


	@Override

	public void onProviderDisabled(String provider)

	{

	Toast.makeText( getApplicationContext(), "Gps Disabled",

	Toast.LENGTH_SHORT ).show();

	}


	@Override

	public void onProviderEnabled(String provider)

	{

	Toast.makeText( getApplicationContext(), "Gps Enabled",

	Toast.LENGTH_SHORT).show();

	}


	@Override

	public void onStatusChanged(String provider, int status, Bundle extras)

	{


	}

	}/* End of Class MyLocationListener */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_g, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
