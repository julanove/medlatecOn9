package medlatec.listview.adapter;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.DumbInfo;
import medlatec.listview.Front_Activity;
import medlatec.listview.GetspleActivity;
import medlatec.listview.MainActivity;
import medlatec.listview.PatientInformation;
import medlatec.listview.R;
import medlatec.listview.SyncActivity;
import medlatec.listview.R.id;
import medlatec.listview.R.layout;
import medlatec.listview.auth.MarshalDouble;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import medlatec.listview.testcode.TestCode_HoaSinh;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class AdapterSync extends ArrayAdapter<Patient> 
{
	private Context appContext = null;
	private ArrayList<Patient> items = null;
	DatabaseHandler myDbHelper = null;
	
	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION = "http://syn.medlatec.vn/Insert_Sayonara";
	private static final String METHOD_NAME = "Insert_Sayonara";
	

	public AdapterSync(Context context, int textViewResourceId, ArrayList<Patient> items)
{
	super(context,textViewResourceId,items);
	this.appContext = context;
	this.items=items;
	
	///////////////////////////////////////////////
	
	myDbHelper = new DatabaseHandler(context);
 	myDbHelper.initialise();
}

	private class SomeTask extends AsyncTask<Patient, String, String> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(appContext);
	
	    @Override
	    protected void onPreExecute()
	    {
	        Dialog.setMessage("Cập nhật dịch vụ, xin chờ ...! ");
	        Dialog.show();
	        Dialog.setCancelable(false);
	        Dialog.setCanceledOnTouchOutside(false);
	    }
	
	    @Override
	    protected String doInBackground(Patient... params) 
	    {
	    	String response = "";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);      
	        //Use this to add parameters
			
			request.addProperty("pid", params[0].getPid());
    		request.addProperty("sid", params[0].getSid());
    		request.addProperty("address", params[0].getAddress());
    		request.addProperty("phone", params[0].getPhone());
    		request.addProperty("name", params[0].getPatientname());
    		request.addProperty("sexAge", params[0].getSex() + "_" + params[0].getAge());
    		request.addProperty("chietkhau", params[0].getSumpertage());
    		
    		request.addProperty("tiendilai", params[0].getTiendilai());
    		request.addProperty("ckEmail", 0);
    		request.addProperty("doctorObject", params[0].getDoctorID() + "_" + params[0].getObjectID());
    		request.addProperty("quan", params[0].getQuan());
    		request.addProperty("ghichuDiagnostic", params[0].getCommend() + "_" + params[0].getDiagnostic()); // lay tam chan doan dang k lam j cho commend

    		String testCodes = myDbHelper.getAllResultForSyncFinal(params[0].getSid());
    		request.addProperty("testCode", testCodes); 
    		request.addProperty("random", params[0].getRandom());
    		request.addProperty("email", params[0].getEmail());
    		request.addProperty("pos", params[0].getPos());
    		request.addProperty("tthai", params[0].getTuvan());
    		request.addProperty("ivcTime", params[0].getIntime());
			
			
	        //Declare the version of the SOAP request
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	       
	        MarshalDouble md = new MarshalDouble();
	        md.register(envelope);
	        
	        MarshalFloat ft = new MarshalFloat();
	        ft.register(envelope);
	        
	        envelope.setOutputSoapObject(request);
	        envelope.dotNet = true;
	       
	        try {
	              HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 13000);
	             
	              //this is the actual part that will call the webservice
	              androidHttpTransport.call(SOAP_ACTION, envelope);
	             
	              // Get the SoapResult from the envelope body.
	              
	              if (envelope.bodyIn instanceof SoapFault) 
	              {
	            	    String str= ((SoapFault) envelope.bodyIn).faultstring;
	            	    Log.i("", str);
	            	    
	                    //myDbHelper.addError(str, str);
	            	
	              } 
	              else 
	              {
	            		SoapObject result = (SoapObject)envelope.bodyIn;
	                    
	                    if(result != null)
	                    {
	                          //Get the first property and change the label text
	                  	  response = result.getProperty(0).toString();
	                  	  
	                  	  Log.i("ĐBT - PATIENT RETURN", " " + response);
	                    }
	            	}
	              
	        } catch (Exception e) {
	              e.printStackTrace();
	              
	              
	        }
	        
	        return response;
	    }
	
	    @Override
	    protected void onPostExecute(String result)
    {
    	String[] badDay = result.split("&");
    	
    	if (result.startsWith("1"))
    	  {
    		 // myDbHelper.updatePatientForSync(badDay[1], 1);
    		  
    		  if (badDay.length > 2)
    		  {
    			  myDbHelper.updatePatientForSync(badDay[1], 1);
    			  
        		  for (int i = 2; i <= badDay.length - 1; i++)
        		  {
        			  myDbHelper.updateResultStatusForSyncFinal(badDay[1], badDay[i]);
        		  }
    		  }
    	  }
    	  else if (result.startsWith("9"))
    	  {
    		  myDbHelper.updatePatientForSync(badDay[1], 9);
    		  myDbHelper.updateResultStatusForSync(badDay[1], 9);
    	  }
        // after completed finished the progressbar
        Dialog.dismiss();
    }
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;
		if (v == null) 
		{
			LayoutInflater vi = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.itemsync, null);
		}
		
		Patient o = items.get(position);
		
		if (o != null) 
		{
			TextView name = (TextView) v.findViewById(R.id.lst_item_Name);
			TextView address = (TextView) v.findViewById(R.id.lst_item_Address);
			TextView phone = (TextView) v.findViewById(R.id.lst_item_Phone);
			TextView status = (TextView) v.findViewById(R.id.lst_item_Status);
			TextView reason = (TextView) v.findViewById(R.id.lst_item_Reason);
	
	
			Button btnSync = (Button)v.findViewById(R.id.lstSycn);
			btnSync.setTag(position);
			btnSync.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				//Section 3. remove rows in list
				String pos = view.getTag().toString();
				int _position = Integer.parseInt(pos);
				
				////
				myDbHelper.updatePatientForSync(items.get(_position).getSid(), 0);
				myDbHelper.updateResultReSync(items.get(_position).getSid(), 0);
				////
			}
			});
	
			Button btnHand = (Button)v.findViewById(R.id.lstHandMade);
			btnHand.setTag(position);
			btnHand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				//Section 3. remove rows in list
				String pos = view.getTag().toString();
				int _position = Integer.parseInt(pos);
				String sid = items.get(_position).getSid();
			
				Patient x =  myDbHelper.getAllPatient(sid, 0).get(0);
				new SomeTask().execute(x);
			}
			});
			
			Button btnBanGiao = (Button)v.findViewById(R.id.lstBanGiao);
			btnBanGiao.setTag(position);
			btnBanGiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				//Section 3. remove rows in list
				String pos = view.getTag().toString();
				int _position = Integer.parseInt(pos);
				String sid = items.get(_position).getSid();
			
				Toast.makeText(appContext, "Test thử " + sid, Toast.LENGTH_SHORT).show();
				
				Intent intent2 = new Intent(appContext, GetspleActivity.class);
			    intent2.putExtra("sid", sid);
			    intent2.putExtra("name", items.get(_position).getPatientname());
			    appContext.startActivity(intent2);
				
			}
			});
	
			if (name != null) 
			{
				name.setText(o.getPatientname() + " - " + o.getSid());
			}
		
			if(address != null)
			{
				address.setText(o.getAddress());
			}
			
			if(phone != null)
			{
				phone.setText(o.getAge() + " - " + (o.getSex().equals("M") ? "Nam" : "Nữ"));
			}
			
			if(status != null)
			{
				status.setText(o.getDiagnostic());
			}
		
			if(reason != null)
			{
				switch (o.getReason()) {
				
				case 0: reason.setText("Chưa trả kết quả - " + o.getsReason()); break;
				
				case 1: reason.setText("Đã trả kết quả - " + o.getsReason()); break;
					
				case 2: reason.setText("Gửi người thân- " + o.getsReason()); break;
					
				case 3: reason.setText("Nhét cửa - " + o.getsReason()); break;
					
				case 4: reason.setText("Lý do khác - " + o.getsReason()); break;
					
				default: break;
				
				}
			}
		}
		
		return v;
	}
}