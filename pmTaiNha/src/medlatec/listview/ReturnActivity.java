package medlatec.listview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import medlatec.listview.adapter.MyCustomBaseAdapter;
import medlatec.listview.adapter.AdapterDumb;
import medlatec.listview.adapter.AdapterPatient;
import medlatec.listview.adapter.AdapterSync;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import medlatec.listview.sync.TestSecService;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReturnActivity extends ListActivity {

	ArrayList<Patient> newList = null;

	DatabaseHandler myDbHelper = null;
	
	private AdapterSync newAdpt = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_return);
		
		myDbHelper = new DatabaseHandler(this);
	 	myDbHelper.initialise();
	 	
	 	////////// 
	 	
	 	
		newList = new ArrayList<Patient>();
		
		newList = (ArrayList<Patient>) myDbHelper.getAllSyncedPatient();
		Log.d("SUM SYNCED PATIENT", "_" + newList.size());
		
		//////////////////
		
		ArrayList<TestCode> listTestCode;
		String tinhtrangSync = "Số lượng dịch vụ đã đồng bộ: ";
		int countSynced = 0;
		String status = "";
		
		for (int i = 0; i < newList.size(); i++)
		{
			tinhtrangSync = "Tình trạng: ";
			countSynced = 0;
			listTestCode = (ArrayList<TestCode>)myDbHelper.getAllResultSync(newList.get(i).getSid());
			
			for (int y = 0; y < listTestCode.size(); y++)
			{
				if (listTestCode.get(y).getTrangthai() == 5)
					countSynced++;
			}
			
			tinhtrangSync  = tinhtrangSync + "" + countSynced + "/" + listTestCode.size();
			
			if (newList.get(i).getTrangthai() == 0)
			{
				status = " [ĐANG ĐỒNG BỘ]";
			}
			else if ((newList.get(i).getTrangthai() == 9))
			{
				status = " [SID ĐÃ TỒN TẠI]";
			}
			else if ((newList.get(i).getTrangthai() == 1))
			{
				status = " [ĐÃ ĐỒNG BỘ]";
			}
			
			newList.get(i).setDiagnostic(tinhtrangSync + status);
			newList.get(i).setPatientname((i + 1) + ". " + newList.get(i).getPatientname());
		}
		
		 
		/////////////////
		
		newAdpt = new AdapterSync(this, R.layout.itemsync, newList);
		setListAdapter(this.newAdpt);
		
		//////////////////////////////////////////////////////////////////
		
		
		
		
		
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		myDbHelper.clearFrag();
		
		if (isMyServiceRunning() == false)
		{
			Intent ServiceIntent = new Intent(this, TestSecService.class);
	        this.startService(ServiceIntent);
	        Log.d("Đã khởi động lại service", "GO SERVICE GO");
		}
	}



	private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("medlatec.listview.sync.TestSecService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{   
		Patient item = (Patient) l.getAdapter().getItem(position);
		ArrayList<Patient> x = (ArrayList<Patient>)myDbHelper.getAllPatient(item.getSid(), 0);
		
		ArrayList<TestCode> listTestCode = (ArrayList<TestCode>)myDbHelper.getAllResultSync(item.getSid());
		String debugString = "";
		
		for (int i = 0; i < listTestCode.size(); i++)
		{
			debugString += "\n " + listTestCode.get(i).getTestCode() + "_" +  listTestCode.get(i).getTrangthai();
		}
		
		
		Toast.makeText(ReturnActivity.this, "" + item.getSid() + "" + x.get(0).getTrangthai() + " " + debugString , Toast.LENGTH_SHORT).show();
        
		
		//////////////
		
		ArrayList<TestCode> listTestCodeAll = (ArrayList<TestCode>)myDbHelper.getAllResult("");
		
		for (int i = 0; i < listTestCodeAll.size(); i++)
		{
			Log.d("XXXXXXX", "" + listTestCodeAll.get(i).getSid() + "_" + listTestCodeAll.get(i).getTestCode() + "_" +  listTestCodeAll.get(i).getTrangthai());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

}
