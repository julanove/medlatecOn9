package medlatec.listview;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.auth.MarshalDouble;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.connect.DatabaseHandlerTestCode;
import medlatec.listview.object.TestCode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GoiKham extends Activity {

	DatabaseHandler myDbHelperMain;
	DatabaseHandlerTestCode myDbHelper;
	TextView tvAll;
	String[] testCodeSader;
	
	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION = "http://syn.medlatec.vn/KSK_GetGoiKSK";
	private static final String METHOD_NAME = "KSK_GetGoiKSK";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goi_kham);
		
		myDbHelper = new DatabaseHandlerTestCode(this);
		myDbHelper.initialise();
		
		myDbHelperMain = new DatabaseHandler(this);
		myDbHelperMain.initialise();
	}
	
	public void chooseTestCode(TextView tvChoose, String[] testCodes)
	{
		String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
		testCodeSader = testCodes;
		
		//Toast.makeText(GoiKham.this, "" + testCodes[0], Toast.LENGTH_SHORT).show();
		
		
		
		if (tvAll.getCurrentTextColor() == Color.RED)
    	{
			new SomeTask().execute(new String[] {sid, testCodes[0], "red"});
    	}
		else
		{
			new SomeTask().execute(new String[] {sid, testCodes[0], "blue"});
		}
	}
	
	private class SomeTask extends AsyncTask<String, Void, Integer> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(GoiKham.this);

	    @Override
	    protected void onPreExecute()
	    {
	        Dialog.setMessage("Cập nhật dịch vụ, xin chờ ...! ");
	        Dialog.show();
	        Dialog.setCancelable(false);
	        Dialog.setCanceledOnTouchOutside(false);
	    }

	    @Override
	    protected Integer doInBackground(String... params) 
	    {
	    	int fallOut = 0;
	    	
	    	if (params[2].equals("blue"))
	    	{
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);    
		        //Use this to add parameters
				request.addProperty("ID", params[1]);
				
		        //Declare the version of the SOAP request
		        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        
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
		              } 
		              else 
		              {
		            		SoapObject result = (SoapObject)envelope.bodyIn;
		                    
		                    if(result != null)
		                    {
		                  	   String response = result.getProperty(0).toString();
		                  	   
		                  	   if (response.equals("") == false)
		                  	   {
		                  		   fallOut = 1;
		                  		   String[] firstStr = response.split("&");
		                  		   String[] secondStr;
		                  		   TestCode item;
		                  		   for (int i = 0; i < firstStr.length; i++)
		                  		   {
		                  			 secondStr = firstStr[i].split("!");  
		                  			 item = myDbHelper.selectOneTestCode(secondStr[0].trim());
		         	    			 item.setSid(params[0]);
		         	    			 item.setPrice(secondStr[1]);
		         					 myDbHelperMain.addResult(item);
		                  		   }
		                  	   }
		                    }
		            	}
		              
		        } catch (Exception e) {
		              e.printStackTrace();
		        }
	    	}
	    	else
	    	{
	    		myDbHelperMain.deleteAllResult(params[0]);
	    		fallOut = 2;
	    	}
	        
	        return fallOut;
	    }

	    @Override
	    protected void onPostExecute(Integer result)
	    {
	        if(result==0)
	        {
	        	Toast.makeText(GoiKham.this, "Có lỗi xảy ra, xin kiểm tra lại", Toast.LENGTH_SHORT).show();
	        	tvAll.setTextColor(Color.BLACK);
	        	PatientInformation.ObjectID = "";
	        }
	        else if (result == 1)
	        {
	        	PatientInformation.ObjectID = testCodeSader[0];
	        	Toast.makeText(GoiKham.this, "Chọn gói khám thành công - " + PatientInformation.ObjectID, Toast.LENGTH_SHORT).show();
	        	tvAll.setTextColor(Color.RED);
	        }
	        else if (result == 2)
	        {
	        	Toast.makeText(GoiKham.this, "Bỏ toàn bộ gói khám cùng dịch vụ thành công", Toast.LENGTH_SHORT).show();
	        	tvAll.setTextColor(Color.BLACK);
	        	PatientInformation.ObjectID = "";
	        }
	        ////
	        String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
	        String tits = "[CÁN BỘ: " + Front_Activity.edRealName.toUpperCase() + "] - TỔNG TIỀN DỊCH VỤ: " + myDbHelperMain.getSumResult(sid);
	        tits += "\nTIN NHẮN CHƯA ĐỌC: " + myDbHelperMain.getNonReadDumb();
	        MainActivity.txtSum.setText(tits);
	        // after completed finished the progressbar
	        Dialog.dismiss();
	    }
	}
	
	public void onClick (View view)
	{
		switch(view.getId()) {
		
        case R.id.tv11:
        	tvAll = (TextView) findViewById(R.id.tv11);
        	chooseTestCode(tvAll, new String[] {"PTE15P10"});
        	break;
        	
        case R.id.tv12:
        	tvAll = (TextView) findViewById(R.id.tv12);
        	chooseTestCode(tvAll, new String[] { "PTE15P15" });
        	break;
        	
        case R.id.tv21:
        	tvAll = (TextView) findViewById(R.id.tv21);
        	chooseTestCode(tvAll, new String[] {"PNAM153P10"});
        	break;
        	
        case R.id.tv22:
        	tvAll = (TextView) findViewById(R.id.tv22);
        	chooseTestCode(tvAll, new String[] { "PNAM153P15" });
        	break;
        	
        case R.id.tv31:
        	tvAll = (TextView) findViewById(R.id.tv31);
        	chooseTestCode(tvAll, new String[] {"PNU1530P10"});
        	break;
        	
        case R.id.tv32:
        	tvAll = (TextView) findViewById(R.id.tv32);
        	chooseTestCode(tvAll, new String[] { "PNU1530P15" });
        	break;
        	
        case R.id.tv41:
        	tvAll = (TextView) findViewById(R.id.tv41);
        	chooseTestCode(tvAll, new String[] {"PNAM35P10"});
        	break;
        	
        case R.id.tv42:
        	tvAll = (TextView) findViewById(R.id.tv42);
        	chooseTestCode(tvAll, new String[] { "PNAM35P15" });
        	break;
        	
        case R.id.tv51:
        	tvAll = (TextView) findViewById(R.id.tv51);
        	chooseTestCode(tvAll, new String[] {"PNU3050P10"});
        	break;
        	
        case R.id.tv52:
        	tvAll = (TextView) findViewById(R.id.tv52);
        	chooseTestCode(tvAll, new String[] { "PNU3050P15" });
        	break;
        	
        case R.id.tv61:
        	tvAll = (TextView) findViewById(R.id.tv61);
        	chooseTestCode(tvAll, new String[] {"PNAM50P10"});
        	break;
        	
        case R.id.tv62:
        	tvAll = (TextView) findViewById(R.id.tv62);
        	chooseTestCode(tvAll, new String[] { "PNAM50P15" });
        	break;
        	
        case R.id.tv71:
        	tvAll = (TextView) findViewById(R.id.tv71);
        	chooseTestCode(tvAll, new String[] {"PNU50P10"});
        	break;
        	
        case R.id.tv72:
        	tvAll = (TextView) findViewById(R.id.tv72);
        	chooseTestCode(tvAll, new String[] { "PNU50P15 " });
        	break;
        	
        	////////////////////////////////////////////////////////////// P 2
        	
        
        case R.id.te11:
        	tvAll = (TextView) findViewById(R.id.te11);
        	chooseTestCode(tvAll, new String[] {"PKTEM15P10"});
        	break;
        	
        case R.id.te12:
        	tvAll = (TextView) findViewById(R.id.te12);
        	chooseTestCode(tvAll, new String[] { "PKTEM15P15" });
        	break;
        	
        case R.id.te21:
        	tvAll = (TextView) findViewById(R.id.te21);
        	chooseTestCode(tvAll, new String[] {"PKN1530P10"});
        	break;
        	
        case R.id.te22:
        	tvAll = (TextView) findViewById(R.id.te22);
        	chooseTestCode(tvAll, new String[] { "PKN1530P15" });
        	break;
        	
        case R.id.te31:
        	tvAll = (TextView) findViewById(R.id.te31);
        	chooseTestCode(tvAll, new String[] {"PKNU153P10"});
        	break;
        	
        case R.id.te32:
        	tvAll = (TextView) findViewById(R.id.te32);
        	chooseTestCode(tvAll, new String[] { "PKNU153P15" });
        	break;
        	
        case R.id.te41:
        	tvAll = (TextView) findViewById(R.id.te41);
        	chooseTestCode(tvAll, new String[] {"PKNAM35P10"});
        	break;
        	
        case R.id.te42:
        	tvAll = (TextView) findViewById(R.id.te42);
        	chooseTestCode(tvAll, new String[] { "PKNAM35P15" });
        	break;
        	
        case R.id.te51:
        	tvAll = (TextView) findViewById(R.id.te51);
        	chooseTestCode(tvAll, new String[] {"PKNU35P10"});
        	break;
        	
        case R.id.te52:
        	tvAll = (TextView) findViewById(R.id.te52);
        	chooseTestCode(tvAll, new String[] { "PKNU35P15" });
        	break;
        	
        case R.id.te61:
        	tvAll = (TextView) findViewById(R.id.te61);
        	chooseTestCode(tvAll, new String[] {"PKNAM50P10"});
        	break;
        	
        case R.id.te62:
        	tvAll = (TextView) findViewById(R.id.te62);
        	chooseTestCode(tvAll, new String[] { "PKNAM50P15" });
        	break;
        	
        case R.id.te71:
        	tvAll = (TextView) findViewById(R.id.te71);
        	chooseTestCode(tvAll, new String[] {"PKNU50P10"});
        	break;
        	
        case R.id.te72:
        	tvAll = (TextView) findViewById(R.id.te72);
        	chooseTestCode(tvAll, new String[] { "PKNU50P15" });
        	break;

      
        	
        default:
        	break;
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.goi_kham, menu);
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
