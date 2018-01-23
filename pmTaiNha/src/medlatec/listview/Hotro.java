package medlatec.listview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.adapter.AdapterHotro;
import medlatec.listview.adapter.AdapterDumb;
import medlatec.listview.adapter.AdapterSync;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



public class Hotro extends ListActivity {

	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION = "http://syn.medlatec.vn/GetWhor";
	private static final String METHOD_NAME = "GetWhor";
	
	private static final String SOAP_ACTION3 = "http://syn.medlatec.vn/TraKetQua";
	private static final String METHOD_NAME3 = "TraKetQua";
	
	ArrayList<Patient> newList = null;
	SimpleAdapter adpt = null;
	private Button btnSave = null;
	private EditText txt2Name = null;
	private AdapterHotro newAdpt = null;
	DatabaseHandler myDbHelper = null;
	
	AlertDialog.Builder alert;
	Spinner spinnercategory; 
	String chosenSid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotro);
		
		myDbHelper = new DatabaseHandler(this);
	 	myDbHelper.initialise();
	 	
	 	//////////
		
		newList = new ArrayList<Patient>();
		newList = (ArrayList<Patient>) myDbHelper.getAllReason_HoTro();
		
		newAdpt = new AdapterHotro(this, R.layout.itemhotro, newList);
		setListAdapter(this.newAdpt);
		
		txt2Name = (EditText)findViewById(R.id.txtCV);
		btnSave = (Button)findViewById(R.id.btnSave);
		
		//
		
		btnSave.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				//newList = new ArrayList<PersonalInfo>();
				//PersonalInfo info = new PersonalInfo();
				
				String maCanBo = txt2Name.getText().toString();
				myDbHelper.updateMaCanBo(maCanBo);
				
				AsyncCallWS x = new AsyncCallWS();
				x.execute("");

				

			}
		});
	}
	
	private class AsyncCallWS extends AsyncTask<String, String, String> {
        @SuppressWarnings("deprecation")
		@Override
        protected String doInBackground(String... params) {
          //  Log.i(TAG, "doInBackground");
        	//txtMain.setText("haha2");
    		String response = "";
    		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);       
          
            //Use this to add parameters
    		request.addProperty("usergettest", String.valueOf(txt2Name.getText()));
    		
    		
            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           
            
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
           
            try {
                  HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 6000);
                 
                  //this is the actual part that will call the webservice
                  androidHttpTransport.call(SOAP_ACTION, envelope);
                 
                  // Get the SoapResult from the envelope body.
                  
                  SoapObject result = (SoapObject)envelope.bodyIn;
                  
                  if(result != null)
                  {
                        //Get the first property and change the label text
                	  response = result.getProperty(0).toString();
                	  
                	  Log.d("KET QUA 1", " " + response);
                	  //Log.d("KET QUA 2", " " + result.getProperty(1).toString());
                  }
                  else
                  {Log.d("KHONG KET QUA", "NO RESULT");}
                  
                  
                  ///////////
                  
                
                  
            } catch (Exception e) {
                  e.printStackTrace();
                  Log.d("KHONG KET QUA", "CO LOI XAY RA");
            }
            
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

        	try
        	{
        		if (result.trim().equals("") == false && result.trim().equals("anyType{}") == false)
        		{
        		
	        		JSONArray mJsonArray = new JSONArray(result);
	        		JSONObject mJsonObject = new JSONObject();
	        		newList = new ArrayList<Patient>();
		            Patient info = new Patient();
	            
	        		for (int i = 0; i < mJsonArray.length(); i++)
	        		{
	        		    mJsonObject = mJsonArray.getJSONObject(i);
	        		    
	        		    info = new Patient();
		 				info.setSid(mJsonObject.getString("SID"));
			            info.setPatientname(mJsonObject.getString("PatientName"));
						info.setPhone(mJsonObject.getString("Phone"));
						info.setAddress(mJsonObject.getString("Address"));
						
						newList.add(info);
						newAdpt.add(info);
	        		}
	        		
	 				newAdpt.notifyDataSetChanged();
        		}
	        	
	        	Dialog.dismiss();
        	}
        	catch(Exception ex)
        	{
        		Toast.makeText(Hotro.this, new String("Có lỗi xảy ra" + ex.getMessage()).toUpperCase(), Toast.LENGTH_SHORT).show();
        	}
        }

        private ProgressDialog Dialog = new ProgressDialog(Hotro.this);
        @Override
	    protected void onPreExecute()
	    {
	        Dialog.setMessage("Cập nhật dịch vụ, xin chờ ...! ");
	        Dialog.show();
	        Dialog.setCancelable(false);
	        Dialog.setCanceledOnTouchOutside(false);
	    }

        @Override
        protected void onProgressUpdate(String... values) {
          //  Log.i(TAG, "onProgressUpdate");
        }

    }

	private class CheckTask extends AsyncTask<String, String, String> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(Hotro.this);

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
	    	Log.d("KETQUAHOTRO", "" + result);
	    	
	    	String[] jkl = result.split("_");
	    	
	    	
	    	
	    	if(jkl[0].equals("insert"))
	    	{
	    		Toast.makeText(Hotro.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
	    		if (jkl.length == 4)
	    		{
	    			myDbHelper.updateReason_HoTro(jkl[1], Integer.parseInt(jkl[2]), jkl[3]);
	    		}
	    		else
	    		{
	    			myDbHelper.updateReason_HoTro(jkl[1], Integer.parseInt(jkl[2]), "");
	    		}
				initData();
				newAdpt = new AdapterHotro(Hotro.this, R.layout.itemhotro, newList);
				setListAdapter(newAdpt);
	    		
	    	}
	    	else if(jkl[0].equals("update"))
	    	{
	    		Toast.makeText(Hotro.this, "Cập nhật lại thành công", Toast.LENGTH_SHORT).show();
	    		if (jkl.length == 4)
	    		{
	    			myDbHelper.updateReason_HoTro(jkl[1], Integer.parseInt(jkl[2]), jkl[3]);
	    		}
	    		else
	    		{
	    			myDbHelper.updateReason_HoTro(jkl[1], Integer.parseInt(jkl[2]), "");
	    		}
				initData();
				newAdpt = new AdapterHotro(Hotro.this, R.layout.itemhotro, newList);
				setListAdapter(newAdpt);
	    	}
	    	else
	    	{
	    		Toast.makeText(Hotro.this, "Có lỗi xảy ra, không thể cập nhật. Xin vui lòng thử lại", Toast.LENGTH_SHORT).show();
	    	}
	    	
	        // after completed finished the progressbar
	        Dialog.dismiss();
	    }
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hotro, menu);
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{   
		
		Patient paObject = (Patient) l.getAdapter().getItem(position);
	    chosenSid = paObject.getSid();
		
		//////////////////////////////////////////////////////////////////////////////
		
		if (chosenSid.equals("") == false)
		{
			
			myDbHelper.addReason_HoTro(paObject, -1, "");
			
			////////////////
			
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
					String value = input.getText().toString();
					
					int reason = spinnercategory.getSelectedItemPosition();
					
					new CheckTask().execute(new String[] {chosenSid, "" + reason,  value});
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
}
