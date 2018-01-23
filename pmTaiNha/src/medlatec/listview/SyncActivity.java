package medlatec.listview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.adapter.MyCustomBaseAdapter;
import medlatec.listview.adapter.AdapterDumb;
import medlatec.listview.adapter.AdapterPatient;
import medlatec.listview.adapter.AdapterSync;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import medlatec.listview.sync.TestSecService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SyncActivity extends ListActivity {

	ArrayList<Patient> newList = null;

	DatabaseHandler myDbHelper = null;
	
	private AdapterSync newAdpt = null;
	
	AlertDialog.Builder alert;
	
	Spinner spinnercategory; 
	
	String chosenSid;
	
	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";
	private static final String SOAP_ACTION3 = "http://syn.medlatec.vn/TraKetQua";
	private static final String METHOD_NAME3 = "TraKetQua";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync);
		
		myDbHelper = new DatabaseHandler(this);
	 	myDbHelper.initialise();
	 	
	 	////////// 
	 	
		initData();
		 
		/////////////////
		
		newAdpt = new AdapterSync(this, R.layout.itemsync, newList);
		setListAdapter(this.newAdpt);
		
		//////////////////////////////////////////////////////////////////
	}
	
	public void initData()
	{
		newList = new ArrayList<Patient>();
		newList = (ArrayList<Patient>) myDbHelper.getAllSyncedPatient();
		
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

	private class CheckTask extends AsyncTask<String, String, String> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(SyncActivity.this);

	    @Override
	    protected void onPreExecute()
	    {
	        Dialog.setMessage("Cập nhật dịch vụ, xin chờ ...! ");
	        Dialog.show();
	        Dialog.setCancelable(false);
	        Dialog.setCanceledOnTouchOutside(false);
	    }

	    @Override
	    protected String doInBackground(String... params) 
	    {
	    	String response = "";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);     
	        //Use this to add parameters
			
			request.addProperty("sid", params[0]);
			request.addProperty("reason", params[1]);
			request.addProperty("description", params[2]);
			request.addProperty("userI", Front_Activity.edUserID);
			
	        //Declare the version of the SOAP request
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

	        envelope.setOutputSoapObject(request);
	        envelope.dotNet = true;
	       
	        try {
	              HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 13000);
	             
	              //this is the actual part that will call the webservice
	              androidHttpTransport.call(SOAP_ACTION3, envelope);
	             
	              // Get the SoapResult from the envelope body.
	              
	              if (envelope.bodyIn instanceof SoapFault) 
	              {
	            	    String str= ((SoapFault) envelope.bodyIn).faultstring;
	            	    Log.i("", str);
	              } 
	              else 
	              {
	            		SoapObject result = (SoapObject)envelope.bodyIn;
	                    
	                    if(result != null)
	                    {
                          //Get the first property and change the label text
	                  	  response = result.getProperty(0).toString();
	                  	  
	                  	  Log.i("XCBT - PATIENT RETURN", " " + response);
	                    }
	            	}
	              
	        } catch (Exception e) {
	              e.printStackTrace();
	              
	              
	        }
	        
	        return response + "_" + params[0] + "_" + params[1] + "_" + params[2];
	    }

	    @Override
	    protected void onPostExecute(String result)
	    {
	    	String[] jkl = result.split("_");
	    	
	    	if(jkl[0].equals("insert"))
	    	{
	    		Toast.makeText(SyncActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
	    		
	    		if (jkl.length == 4)
	    		{
	    			myDbHelper.updateReason(jkl[1], Integer.parseInt(jkl[2]), jkl[3]);
	    		}
	    		else
	    		{
	    			myDbHelper.updateReason(jkl[1], Integer.parseInt(jkl[2]), "");
	    		}
				
				///////////////////
				
				initData();
				newAdpt = new AdapterSync(SyncActivity.this, R.layout.itemsync, newList);
				setListAdapter(newAdpt);
	    		
	    	}
	    	else if(jkl[0].equals("update"))
	    	{
	    		Toast.makeText(SyncActivity.this, "Cập nhật lại thành công", Toast.LENGTH_SHORT).show();
	    		//allowKado = 1;
	    		//fillSelected(-1, 0);
	    		myDbHelper.updateReason(jkl[1], Integer.parseInt(jkl[2]), jkl[3]);
				
				///////////////////
				
				initData();
				newAdpt = new AdapterSync(SyncActivity.this, R.layout.itemsync, newList);
				setListAdapter(newAdpt);
	    	}
	    	else
	    	{
	    		Toast.makeText(SyncActivity.this, "Có lỗi xảy ra, không thể cập nhật. Xin vui lòng thử lại", Toast.LENGTH_SHORT).show();
	    		//allowKado = 0;
	    	}
	    	
	        // after completed finished the progressbar
	        Dialog.dismiss();
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
		Patient paObject = (Patient) l.getAdapter().getItem(position);
		chosenSid = paObject.getSid();
		
		//////////////////////////////////////////////////////////////////////////////
		
		if (chosenSid.equals("") == false)
		{
			alert = new AlertDialog.Builder(this);
			
			alert.setTitle("Check tình trạng trả kết quả");
			
			alert.setMessage(paObject.getSid() + " - " + paObject.getPatientname());
			
			LayoutInflater li = LayoutInflater.from(this);
		    final View dialogView = li.inflate(R.layout.reason, null);

		    final EditText input = (EditText) dialogView
		            .findViewById(R.id.etReason);

		    spinnercategory = (Spinner) dialogView.findViewById(R.id.viewSpin);
		    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		            this, R.array.reason, android.R.layout.simple_spinner_item);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    spinnercategory.setAdapter(adapter);
		    spinnercategory.setOnItemSelectedListener(new OnItemSelectedListener() {

		        @Override
		        public void onItemSelected(AdapterView<?> arg0, View arg1,
		                int arg2, long arg3) {
		            // TODO Auto-generated method stub
		           // position = arg2 + 1 ;

		        }

		        @Override
		        public void onNothingSelected(AdapterView<?> arg0) {
		            // TODO Auto-generated method stub

		        }
		    });
			
		    
			
			//////////////////////////////////////////////////////////////////////////////

			
			
			alert.setView(dialogView);
	
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				
				
				try
				{
					
					/*String value = input.getText().toString();
					
					int reason = spinnercategory.getSelectedItemPosition();
					
					myDbHelper.updatePatientReason(chosenSid, reason, value);
					
					///////////////////
					
					initData();
					newAdpt = new MyCustomBaseAdapterSync(SyncActivity.this, R.layout.itemsync, newList);
					setListAdapter(newAdpt);*/
					
					String value = input.getText().toString();
					
					int reason = spinnercategory.getSelectedItemPosition();
					
					new CheckTask().execute(new String[] {chosenSid, "" + reason, value});
					
					//myDbHelper.updatePatientReason(chosenSid, reason, value);
					
					///////////////////
					
					/*initData();
					newAdpt = new MyCustomBaseAdapterSync(SyncActivity.this, R.layout.itemsync, newList);
					setListAdapter(newAdpt);*/
					
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			  }
			});
	
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
		    
			alert.show();
		}
	}
	
	/*@Override
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
		
		
		Toast.makeText(SyncActivity.this, "" + item.getSid() + "" + x.get(0).getTrangthai() + " " + debugString , Toast.LENGTH_SHORT).show();
        
		
		//////////////
		
		ArrayList<TestCode> listTestCodeAll = (ArrayList<TestCode>)myDbHelper.getAllResult("");
		
		for (int i = 0; i < listTestCodeAll.size(); i++)
		{
			Log.d("XXXXXXX", "" + listTestCodeAll.get(i).getSid() + "_" + listTestCodeAll.get(i).getTestCode() + "_" +  listTestCodeAll.get(i).getTrangthai());
		}
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sync, menu);
		return true;
	}

}
