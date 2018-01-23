package medlatec.listview;


import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class ReceiptMain extends TabActivity implements OnTabChangeListener {

	private static final String LIST1_TAB_TAG = "Thống kê BN"; 
    private static final String LIST3_TAB_TAG = "In hóa đơn";
    
    public static TabHost tabHost;
    private Intent profileIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_doctor_printer);
		
		tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);
        
        TabSpec profileSpec = tabHost.newTabSpec(LIST1_TAB_TAG);
        profileSpec.setIndicator(LIST1_TAB_TAG);
        profileIntent = new Intent(this, ReceiptTK.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);

        ///////////////////////////////////////////////////////////////
        
        profileSpec = tabHost.newTabSpec(LIST3_TAB_TAG);
        profileSpec.setIndicator(LIST3_TAB_TAG);
        profileIntent = new Intent(this, ReceiptPrinter.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doctor_printer, menu);
		return true;
	}
	
public void onTabChanged(String tabName) {

        
        if(tabName.equals(LIST1_TAB_TAG) == false) 
        {
        	String doctorID = String.valueOf(ReceiptTK.txt2Name.getText());
           
            if (doctorID.equals(""))
            {
            	Toast.makeText(ReceiptMain.this, "Chưa nhập mã bác sĩ", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        		return;
            }
        }
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
