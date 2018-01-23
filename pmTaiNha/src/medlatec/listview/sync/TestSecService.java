package medlatec.listview.sync;

import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.DumbInfo;
import medlatec.listview.Front_Activity;
import medlatec.listview.MainActivity;
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
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TestSecService extends IntentService {
	
	// "" for securiy
	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION = "http://syn.medlatec.vn/Insert_Sayonara";
	private static final String METHOD_NAME = "Insert_Sayonara";
	
	
	DatabaseHandler myDbHelper;
	
    public TestSecService() {
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
    
    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	//TestSecService();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }*/

	@Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
		
		
			while(true)
			{
				Log.d("START", "START");
				
				try
				{
					Thread.sleep(60000);
				} 
					catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				
				if (isOnline())
				{
					//new AsyncCallWTD().execute(myDbHelper.getCurrentCanBo());
					//Calendar now = Calendar.getInstance();
					//int currentMinute = (now.get(Calendar.HOUR_OF_DAY) * 60) + now.get(Calendar.MINUTE);
					
					////////////
					
					List<Patient> listPatient = myDbHelper.getAllPatientForSync();
						        
			        Log.d("SYNCED PATIENT", "" + listPatient.size());
			        for (int i = 0; i < listPatient.size(); i ++)
			        {
			        	Log.d("", listPatient.get(i).getSid() + "_" + listPatient.get(i).getPatientname());
			        	/*if (currentMinute - listPatient.get(i).getIntime() > 4)
			        	{*/
			        	if(myDbHelper.getAllResultForSyncFinal(listPatient.get(i).getSid()).equals("") == false)
			        		new AsyncCallWS().execute(listPatient.get(i));
			        	//}
			        	
			        	try
						{
							Thread.sleep(13000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			        
			        ////////////////////////////////////////////////////////////
			        
			        // Tinhtrang = 1 | ttIn | Da Doc
			        // Tinhtrang = 2 | ttLayMau | Dalay
			        
			        List<PersonalInfo> listDumb = myDbHelper.getAllDumbForUpdate();
			        Log.d("SYNCED TINHAN", "" + listDumb.size());
			        
			        
			        for (int i = 0; i < listDumb.size(); i ++)
			        {
			        	Log.d("UPDATE TIN NHAN", listDumb.get(i).getMaLH() + "_" + listDumb.get(i).getTinhtrang());
		        		new AsyncCallWUpdateTD().execute(listDumb.get(i));
			        	
			        	try
						{
							Thread.sleep(13000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			        
		        	////////////////////////////////////////////////////////////
			        
			        
			        Log.d("RUNNING", "RUNNING");
			        
			        
				}
			}
    }
	
	private class AsyncCallWS extends AsyncTask<Patient, String, String> {
        @Override
        protected String doInBackground(Patient... params) {
          //  Log.i(TAG, "doInBackground");
        	//txtMain.setText("haha2");
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
                		
                		Log.d("PATIENT RESULT", String.valueOf(result));
                        Log.d("PATIENT RESULT COUNT", "" + result.getPropertyCount());
                        
                        if(result != null)
                        {
                              //Get the first property and change the label text
                      	  response = result.getProperty(0).toString();
                      	  
                      	  Log.i("PATIENT RETURN", " " + response);
                      	  
                      	  
                        }
                	}
                  
            } catch (Exception e) {
                  e.printStackTrace();
                  
                  
            }
            
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            
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
        		  
        		  // 5 means these result is fkin fine. their only father has sent them command to go forward
        		  // ok now I will tell u how you work.
        		  // first u will be waitting to be chosen. At dat time, u have no father!
        		  // After dat u will be assigned 2. 
        		  // Then come your father, your only father. He will name all of u 0
        		  // That means u all have aleady prepared to send out, to become a men
        		  // And dat day has finally come. Your father is old, he gone first leaving u all behind
        		  // But his death is not meaningless. 
        		  // God have been waitting for his arrival. God name his son - your father 1 and all of u - FIVE!
        		  // THE END!
        	  }
        	  else if (result.startsWith("9"))
        	  {
        		  myDbHelper.updatePatientForSync(badDay[1], 9);
        		  myDbHelper.updateResultStatusForSync(badDay[1], 9);
        	  }
        	
        }

        @Override
        protected void onPreExecute() 
        {
          //  Log.i(TAG, "onPreExecute");
        }

        @Override
        protected void onProgressUpdate(String... values) {
          //  Log.i(TAG, "onProgressUpdate");
        }

    }

	private class AsyncCallWTD extends AsyncTask<String, String, String> {
        @SuppressWarnings("deprecation")
		@Override
        protected String doInBackground(String... params) {
          //  Log.i(TAG, "doInBackground");
        	//txtMain.setText("haha2");
    		String response = "";
    		SoapObject request = new SoapObject(NAMESPACE, "selectTDInfo2");       
           
    		//////////////////
    		
    		//Log.d("TIME", "" + strFrom  + "_" + strTo);
    	
    		Log.d("NAMECB", "" + params[0]);
    		request.addProperty("name", params[0]);
    		
            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           
            
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
           
            try {
                  HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 6000);
                 
                  //this is the actual part that will call the webservice
                  androidHttpTransport.call("http://syn.medlatec.vn/selectTDInfo2", envelope);
                 
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

        	if (result.trim().equals("") == false && result.trim().equals("anyType{}") == false)
        	{
	            Log.d("","" + result);
	 			PersonalInfo info = new PersonalInfo();
	 			String[] solB = result.split(";");
	 			Log.d("","" + result);
	 		
	 			for (int i = 0; i < solB.length; i++)
	 			{
	 				Log.d("", "" + solB[i]);
	 				
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
					myDbHelper.addDumb(info);
	 			}
	 			
	 			String tits = "[CÁN BỘ: " + Front_Activity.edRealName.toUpperCase() + "] - TỔNG TIỀN DỊCH VỤ: 0";
	 	        tits += "\nTIN NHẮN CHƯA ĐỌC: " + myDbHelper.getNonReadDumb();
	 	        MainActivity.txtSum.setText(tits);
        	}
        	else
        	{
        		Log.d("HIEN TAI KO CO DIA CHI", "HIEN TAI KO CO DIA CHI");
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

	private class AsyncCallWUpdateTD extends AsyncTask<PersonalInfo, String, String> {
        @SuppressWarnings("deprecation")
		@Override
        protected String doInBackground(PersonalInfo... params) {
          //  Log.i(TAG, "doInBackground");
        	//txtMain.setText("haha2");
    		String response = "";
    		SoapObject request = new SoapObject(NAMESPACE, "UpdateTDInfo_Sayonara");       
           
            //Use this to add parameters
    		request.addProperty("maLH", params[0].getMaLH());
    		request.addProperty("type", params[0].getTinhtrang());
    		request.addProperty("SID", params[0].getSID());
    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Calendar now = Calendar.getInstance();
    		request.addProperty("now", dateFormat.format(now.getTime()));
    		
            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
           
            try {
                  HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 6000);
                 
                  //this is the actual part that will call the webservice
                  androidHttpTransport.call("http://syn.medlatec.vn/UpdateTDInfo_Sayonara", envelope);
                 
                  // Get the SoapResult from the envelope body.
                  
                  SoapObject result = (SoapObject)envelope.bodyIn;
                  
                  if(result != null)
                  {
                        //Get the first property and change the label text
                	  response = result.getProperty(0).toString();
                	  
                	  Log.d("KET QUA UPDATE TD", " " + response);
                	  //Log.d("KET QUA 2", " " + result.getProperty(1).toString());
                  }
                  else
                  {Log.d("KHONG KET QUA UPDATE TD", "NO RESULT");}
                  
                  ///////////
                  
            } catch (Exception e) {
                  e.printStackTrace();
            }
            
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

        	String[] silentDay = result.split("_");
        	
        	if (result.startsWith("1") && silentDay[2].equals("1"))
        	  {
        		 	myDbHelper.updateDumbAss(silentDay[1], 1);
        	  }
        	  else if (result.startsWith("2") && silentDay[2].equals("1"))
        	  {
        		  myDbHelper.updateDumbAss(silentDay[1], 2);
        	  }
        	  else if (result.startsWith("3") && silentDay[2].equals("1"))
        	  {
        		  myDbHelper.updateDumbAss(silentDay[1], 3);
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
