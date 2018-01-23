package medlatec.listview.sync;
 
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.Patient;
import medlatec.listview.object.TestCode;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class TestSyncAdapter extends AbstractThreadedSyncAdapter 
{
    
    private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION = "http://syn.medlatec.vn/Insert_TN";
	private static final String METHOD_NAME = "Insert_TN";
	
	/////////////////////////////////////////////
	
	private static final String NAMESPACE_R = "http://syn.medlatec.vn/";
	private static final String URL_R = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION_R = "http://syn.medlatec.vn/Insert_TestCode_TN";
	private static final String METHOD_NAME_R = "Insert_TestCode_TN";
	
	/////////////////////////////////////////////
	
	ContentResolver mContentResolver;
 
    public TestSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }
    
    public TestSyncAdapter( Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
       
    }
    
    
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("udinic", "onPerformSync for account[" + account.name + "]");
        
        
        DatabaseHandler db = new DatabaseHandler(this.getContext());
      //getBaseContext(), getApplicationContext(0
        Log.d("CONTENTTTTTTTTTTTTTTTTTTT", "TTTTTTTTTTTT");
        db.showSumPatient();
        
        
        
        
    }
    
    private class AsyncCallWS extends AsyncTask<Patient, String, String> {
        @Override
        protected String doInBackground(Patient... params) {
          //  Log.i(TAG, "doInBackground");
        	//txtMain.setText("haha2");
    		String response = "";
    		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);       
           
            //Use this to add parameters
    		request.addProperty("sid", params[0].getSid());
    		request.addProperty("address", params[0].getAddress());
    		request.addProperty("phone", "0");
    		request.addProperty("name", params[0].getPatientname());
    		request.addProperty("sex", params[0].getSex());
    		request.addProperty("cmnd", "123456789");
    		request.addProperty("age", params[0].getAge());
    		request.addProperty("chietkhau", 100);
    		request.addProperty("tiendilai", 0);
    		request.addProperty("email", "test@yahoo.com");
    		request.addProperty("ckEmail", 0);
    		request.addProperty("doctor", "8888");
    		
    		
            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
           
            try {
                  HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                 
                  //this is the actual part that will call the webservice
                  androidHttpTransport.call(SOAP_ACTION, envelope);
                 
                  // Get the SoapResult from the envelope body.
                  
                  SoapObject result = (SoapObject)envelope.bodyIn;

                  Log.d("PATIENT RESULT", String.valueOf(result));
                  Log.d("PATIENT RESULT COUNT", "" + result.getPropertyCount());
                  
                  if(result != null)
                  {
                        //Get the first property and change the label text
                	  response = result.getProperty(0).toString();
                	  
                	  Log.i("PATIENT RETURN", " " + response);
                  }
                  else
                  {
                	 // txtMain.setText("none");
                    //  Toast.makeText(getApplicationContext(), "No Response",Toast.LENGTH_LONG).show();
                	  
                	  Log.i("PATIENT RETURN", "NO RESULT");
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
            
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Log.i("PATIENT RESULT", " " + result);
        	//txtMain.setText(result);
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

	private class AsyncCallWS_R extends AsyncTask<TestCode, String, String> {
        @Override
        protected String doInBackground(TestCode... params) {
          //  Log.i("DOIT", "DOIT");
        	//txtMain.setText("haha2");
    		String response = "xx";
    		SoapObject request = new SoapObject(NAMESPACE_R, METHOD_NAME_R);       
           
            //Use this to add parameters
    		request.addProperty("sid", params[0].getSid());
    		request.addProperty("testcode", params[0].getTestCode());
    		request.addProperty("testname", params[0].getTestName());
    		request.addProperty("price", params[0].getPrice());
    		request.addProperty("nRange", "");
    		request.addProperty("nRangeF", "");
    		request.addProperty("testHead", 0);
    		request.addProperty("category", params[0].getCategory());
    		request.addProperty("lower", 0);
    		request.addProperty("higher", 0);
    		request.addProperty("profile", 0);
    		request.addProperty("bold", 0);
    		//request.addProperty("cost", params[0].getPrice());
    		request.addProperty("cost", 1);
    		request.addProperty("type", 0);
    		request.addProperty("unit", 0);
    		
    		
    		
            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
           
            try {
                  HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_R);
                 
                  //this is the actual part that will call the webservice
                  androidHttpTransport.call(SOAP_ACTION_R, envelope);
                 
                  // Get the SoapResult from the envelope body.
                  SoapObject result = (SoapObject)envelope.bodyIn;

                  if(result != null)
                  {
                        //Get the first property and change the label text
                	  response = result.getProperty(0).toString();
                  }
                  else
                  {
                	 // txtMain.setText("none");
                    //  Toast.makeText(getApplicationContext(), "No Response",Toast.LENGTH_LONG).show();
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
            
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
           // Log.i(TAG, "onPostExecute");
        	//txtMain.setText(result);
        //	Log.i("RESULT RESULT", " " + result);
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