package medlatec.listview.sync;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.PatientInformation;
import medlatec.listview.auth.Constants;
import medlatec.listview.auth.MarshalDouble;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import android.accounts.Account;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends IntentService {
	
	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	

	
	private static final String SOAP_ACTION_U = "http://syn.medlatec.vn/selectVersion";
	private static final String METHOD_NAME_U = "selectVersion";
	
	//
	
	DatabaseHandler myDbHelper;
	//
	ResultReceiver receiver;
	
    public UpdateService() {
    	 super("TestService");
		// TODO Auto-generated constructor stub
    	 
    	 myDbHelper = new DatabaseHandler(this);
    	 myDbHelper.initialise();
	}
    
    public boolean isOnline() 
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        
        if (netInfo != null && netInfo.isConnected()) 
        {
            return true;
        }
        return false;
    }

	@Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent

		
		    receiver = (ResultReceiver) workIntent.getParcelableExtra("receiver");
			while(true)
			{
				Log.d("START", "START");
				
				try
				{
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (isOnline())
				{
		        	new AsyncCallWS_U().execute();
			        
			        Log.d("RUNNING", "RUNNING");
				}
			}
		
		
    }
	
	
	private class AsyncCallWS_U extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
        	//txtMain.setText("haha2");
    		String response = "";
    		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_U);       
           
            //Use this to add parameters

    		
            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //
            MarshalDouble md = new MarshalDouble();
            md.register(envelope);
            //
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
           
            try {

                  HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 6000);
                 
                  //this is the actual part that will call the webservice
                  androidHttpTransport.call(SOAP_ACTION_U, envelope);
                 
                  // Get the SoapResult from the envelope body.
                  SoapObject result = (SoapObject)envelope.bodyIn;

                  if(result != null)
                  {
                        //Get the first property and change the label text
                	  response = result.getProperty(0).toString();
                	  
                	  Log.d("KET QUA UPDATE", " " + response);
                	  
                	  /*Bundle resultData = new Bundle();
                	  resultData.putInt("progress" ,100);
                      receiver.send(1, resultData);*/
                	  
                  }
                  else
                  {Log.d("KHONG KET QUA UPDATE", "NO RESULT");}
                  
                  
            } catch (Exception e) {
                  e.printStackTrace();
            }
            
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
           
        	try
        	{
	        	/*String isUpdate = "";
	        	String isVersion = "";
	        	String isLink = "";
	        
	 			String[] solB = result.split(";");
	 		
	 			isUpdate = solB[0];
	 			isVersion = solB[2];
	 			isLink = solB[1];
	 			
	 			myDbHelper.updateUpdate(isVersion, isUpdate, isLink);
	 			
	 			String[] xx = myDbHelper.getUpdateLink();
	 			
	 			if (xx[2].equals(isVersion.trim()) == false && isUpdate.equals("1"))
	 			{
	 				Log.d("", "" + xx[0] + "-" + xx[1] + "-" + xx[2]);
	 				
	 				Bundle resultData = new Bundle();
              	 	resultData.putString("progress" , "" + isLink);
                    receiver.send(1, resultData);
	 			}
	 			else
	 			{
	 				Log.d("", "Ko cần update");
	 			}*/
        	}
        	catch(Exception ex)
        	{
        		ex.printStackTrace();
        	}
        }

        @Override
        protected void onPreExecute() {
          //  Log.i(TAG, "onPreExecute");
        	
        	
        	
        }

        @Override
        protected void onProgressUpdate(String... values) {
          //  Log.i(TAG, "onProgressUpdate");
        }

    }

}
