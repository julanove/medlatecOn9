package medlatec.listview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.adapter.AdapterDumb;
import medlatec.listview.auth.MarshalDouble;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestSerial;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class DumbInfo extends ListActivity 
{
/** Called when the activity is first created. */
ArrayList<PersonalInfo> newList = null;

SimpleAdapter adpt = null;

private Button btnSave = null;

private EditText txt2Name = null;
//private EditText txt2Mobile = null;
//private EditText txt2Phone = null;

private AdapterDumb newAdpt = null;

DatabaseHandler myDbHelper = null;

/////////


private static final String NAMESPACE = "http://syn.medlatec.vn/";
private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
private static final String SOAP_ACTION = "http://syn.medlatec.vn/LayLichHen";
private static final String METHOD_NAME = "LayLichHen";


/*private static final String SOAP_ACTION = "http://syn.medlatec.vn/Insert_Final_XX";
private static final String METHOD_NAME = "Insert_Final_XX";*/

////////

@Override
	public void onCreate(Bundle savedInstanceState) 
{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_dumb_info);
	
	////
	
	Log.d("CREATE ", "CREATE");
	
	/////////
	
	myDbHelper = new DatabaseHandler(this);
 	myDbHelper.initialise();
 	
 	
 	//////////
	
	newList = new ArrayList<PersonalInfo>();
	newList = (ArrayList<PersonalInfo>) myDbHelper.getAllDumb(0);
	Log.d("ALL DUMB", "" + newList.size());
	
	
	
	newAdpt = new AdapterDumb(this, R.layout.itemnhanmau, newList);
	setListAdapter(this.newAdpt);
	
	txt2Name = (EditText)findViewById(R.id.txtCV);
	btnSave = (Button)findViewById(R.id.btnSave);
	txt2Name.setText("" + myDbHelper.getCurrentCanBo());
	
	Log.d("NAME CB", "" + myDbHelper.getCurrentCanBo());
	
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{        
        PersonalInfo item = (PersonalInfo) l.getAdapter().getItem(position);
        Intent intent = new Intent(DumbInfo.this, MainActivity.class);
	    //startActivity(intent);
	    
	    Bundle bundle = new Bundle();
	    //String[] getName = String.valueOf(item.GetName()).split(".");
	    bundle.putString("name", item.GetName());
	    bundle.putString("mobile", item.GetMobile());
	    bundle.putString("phone", item.getPhone());
	    bundle.putString("malh", item.getMaLH());
	    bundle.putString("namsinh", item.getNamsinh());
	    bundle.putString("gioitinh", item.getGioitinh());
	    //bundle.putString("yeucauXN", item.getYeucauXN());
	    
	    intent.putExtra("MyPackage", bundle);
	    startActivity(intent);
	}
	
	private class AsyncCallWS extends AsyncTask<String, String, String> {
        @SuppressWarnings("deprecation")
		@Override
        protected String doInBackground(String... params) {
          //  Log.i(TAG, "doInBackground");
        	//txtMain.setText("haha2");
    		String response = "";
    		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);       
          

    		Calendar c = Calendar.getInstance();

    		
    		Date dFrom = c.getTime();
    		Date dTo = c.getTime();
    		SimpleDateFormat from;
    		SimpleDateFormat to;
    		
    		if (dFrom.getHours() <= 12)
    		{
    			dFrom.setDate(dTo.getDate() - 1);
    			from = new SimpleDateFormat("yyyy-dd-MM 21:00:00");
    			to = new SimpleDateFormat("yyyy-dd-MM 12:00:00");
    		}
    		else
    		{
    			from = new SimpleDateFormat("yyyy-dd-MM 12:00:00");
    			to = new SimpleDateFormat("yyyy-dd-MM 23:00:00");
    		}
    		
    		String strFrom = from.format(dFrom);
    		String strTo = to.format(dTo);
    		
    		//Log.d("TIME GET DC", "" + strFrom  + "_" + strTo);
    		
    		//////////////////

    		
            //Use this to add parameters
    		request.addProperty("from", strFrom);
    		request.addProperty("to", strTo);
    		request.addProperty("name", String.valueOf(txt2Name.getText()));
    		
    		
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
	        		newList = new ArrayList<PersonalInfo>();
	        		PersonalInfo info = new PersonalInfo();
	            
	        		for (int i = 0; i < mJsonArray.length(); i++)
	        		{
        				mJsonObject = mJsonArray.getJSONObject(i);
	        		    
	        		    info = new PersonalInfo();
		 				info.setMaLH(mJsonObject.getString("MaLH"));
			            info.SetName((i + 1) + ": " + mJsonObject.getString("KhachHang"));
						info.SetMobile(mJsonObject.getString("soDT"));
						info.setPhone(mJsonObject.getString("diaChi"));
						info.setGiohen1(mJsonObject.getString("gioHen1"));
						info.setGiohen2(mJsonObject.getString("gioHen2"));
						info.setNamsinh(mJsonObject.getString("namSinh"));
						info.setGioitinh(mJsonObject.getString("gioiTinh"));
	        		}
	        		
		            /*newList = new ArrayList<PersonalInfo>();
		 			PersonalInfo info = new PersonalInfo();
		 			String[] solB = result.split(";");
		 		
		 			for (int i = 0; i < solB.length; i++)
		 			{
		 				if (solB[i].trim().equals(""))
		 					continue;
		 				
		 				String[] kyK = solB[i].trim().replace("\n", "").split("_");
		 				info = new PersonalInfo();
		 				info.setMaLH(kyK[0]);
			            info.SetName((i + 1) + ": " + kyK[1]);
						info.SetMobile(kyK[2] );
						info.setPhone(kyK[3]);
						info.setGiohen1(kyK[4]);
						info.setGiohen2(kyK[5]);
						info.setNamsinh(kyK[6]);
						info.setGioitinh(kyK[7]);
						
						if (myDbHelper.addDumb(info) == 1)
						{
							newList.add(info);
							newAdpt.add(info);
						}
		 			}
		 			
		 			newAdpt.notifyDataSetChanged();*/
	        	}
	        	else
	        	{
	        		Toast.makeText(DumbInfo.this, "Hiện tại không có địa chỉ nào cho cán bộ này", Toast.LENGTH_SHORT).show();
	        	}
	        	
	        	Dialog.dismiss();
        	}
        	catch(Exception ex)
        	{
        		Toast.makeText(DumbInfo.this, "Có lỗi xảy ra: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        		Dialog.dismiss();
        	}
        }

        private ProgressDialog Dialog = new ProgressDialog(DumbInfo.this);
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
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        
        case R.id.menu_clear:
            Toast.makeText(DumbInfo.this, "Clear 2", Toast.LENGTH_SHORT).show();
            //clear();
            myDbHelper.deleteAllDumb("x");
            return true;
            
        case R.id.menu_clone:
            
        	newList = (ArrayList<PersonalInfo>) myDbHelper.getAllDumb(1);
        	
        	Log.d("CLONE SIZE: ", "" + newList.size());
        	
        	newAdpt.clear();
        	for (PersonalInfo x : newList) 
        	{
        		newAdpt.add(x);
			}
			newAdpt.notifyDataSetChanged();
        	
            return true;
            
        case R.id.menu_all:
            
        	newList = (ArrayList<PersonalInfo>) myDbHelper.getAllDumb(0);
        	newAdpt.clear();
        	for (PersonalInfo x : newList)
        	{
        		newAdpt.add(x);
			}
			newAdpt.notifyDataSetChanged();
        	
            return true;
 
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu3, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d("RESUME", "RESUME");
	}
	
	
	
	
}