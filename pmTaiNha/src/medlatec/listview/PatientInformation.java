package medlatec.listview;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.adapter.FilterWithSpaceAdapter;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.connect.DatabaseHandlerDoctor;
import medlatec.listview.connect.DatabaseHandlerPID;
import medlatec.listview.object.Doctor;
import medlatec.listview.object.Patient;
import medlatec.listview.sync.TestSecService;

import com.jwetherell.quick_response_code.CaptureActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PatientInformation extends Activity {

	public static EditText etNgay;
	public static EditText etSID;
	public static TextView etTime;
	public static EditText etName;
	public static EditText etMonth;
	public static EditText etDay;
	public static EditText etCanbo;
	public static EditText etBOD;
	public static Spinner etSex;
	public static EditText etAddress;
	public static EditText etPhone;
	public static AutoCompleteTextView etDoctor;
	public static EditText etChietKhau;
	public static EditText etDoctorID;
	public static EditText etCHanDoan;
	public static AutoCompleteTextView etLyDo;
	public static EditText etLyDoE;
	public static EditText etEmail;
	public static Spinner etDiLai;
	public static Spinner etGG;
	public static CheckBox etCS;
	public static CheckBox etTV;
	public static EditText etPID;
	public static AutoCompleteTextView txtH;
	public static String CanboID;
	//
	public static String maLH;
	public static String ObjectID = "";
	public static int isInsert = 1;
	//
	public static String yeucauXN = "";
	//
	DatabaseHandler myDbHelper = null;
	DatabaseHandlerPID myDBPID = null;
	DatabaseHandlerDoctor myDBDoc = null;
	
	///////
	
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	
	/////////////////////////////////////////////
	
	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION = "http://syn.medlatec.vn/SelectPID";
	private static final String METHOD_NAME = "SelectPID";

	///////////////////////////////
	
	List<Doctor> xxx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientx_activity);
		
		myDbHelper = new DatabaseHandler(this);
		myDbHelper.initialise();
		
		myDBPID = new DatabaseHandlerPID(this);
		myDBPID.initialise();
		
		myDBDoc = new DatabaseHandlerDoctor(this);
		myDBDoc.initialise();
		
		init();
		
		Calendar c = Calendar.getInstance(); 
	
	}
	
	
	public void init()
	{
		////////// Init
		
		etTime = (TextView) findViewById(R.id.etTime);
		etSID = (EditText) findViewById(R.id.etSID);
		etName = (EditText) findViewById(R.id.etPatientName);
		
		etMonth = (EditText)findViewById(R.id.etMonth);
		etDay = (EditText) findViewById(R.id.etDay);
		etCanbo = (EditText) findViewById(R.id.etCanbo);
		etBOD = (EditText) findViewById(R.id.etBOD);
		etSex = (Spinner) findViewById(R.id.etSex);
		etAddress = (EditText) findViewById(R.id.etAddress);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etDoctor = (AutoCompleteTextView) findViewById(R.id.etDoctor);
		etDoctorID = (EditText) findViewById(R.id.etDoctor2);
		etChietKhau = (EditText) findViewById(R.id.etChietKhau);
		etCHanDoan = (EditText) findViewById(R.id.etChanDoan);
		etLyDo = (AutoCompleteTextView) findViewById(R.id.etLyDo);
		etLyDoE = (EditText) findViewById(R.id.etLydoE);
		txtH = (AutoCompleteTextView) findViewById(R.id.cbH);
		etDiLai = (Spinner) findViewById(R.id.etDiLai);
		etGG = (Spinner) findViewById(R.id.etGG);
		etCS = (CheckBox) findViewById(R.id.etCS);
		etTV = (CheckBox) findViewById(R.id.etTV);
		etPID = (EditText) findViewById(R.id.etPID);
		etEmail = (EditText) findViewById(R.id.etEmail);
		
		///////// GET CURRENT DATE
		
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
		String formattedDate = df.format(c.getTime());
		
		/////////////////////////////////////////////////
		
		etTime.setText(formattedDate);
		etTime.setEnabled(false);
		////////
		
		String[] stHuyen = new String[] {"Quận Ba Đình",
				 "Quận Tây Hồ",
				 "Quận Hoàn Kiếm",
				 "Quận Hai Bà Trưng",
				 "Quận Đống Đa",
				 "Quận Thanh Xuân",
				 "Quận Hoàng Mai",
				 "Quận Long Biên",
				 "Quận Cầu Giấy",
				 "Huyện Sóc Sơn",
				 "Huyện Đông Anh",
				 "Huyện Gia Lâm",
				 "Huyện Từ Liêm"};
		 ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,stHuyen);
		 txtH.setAdapter(adapter2);
		 
		 /////////
		 
		 String[] stDiag = myDbHelper.getAllDiag();
		 ArrayAdapter adapter3 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,stDiag);
		 etLyDo.setAdapter(adapter3);
		 
		 /////////
		 
		 xxx =  myDBDoc.getAllDoctor();
		 
		 String[] stDoc = new String[xxx.size()];
		 for (int i =0; i < xxx.size(); i++)
		 {
			 stDoc[i] = ((Doctor)xxx.get(i)).getName();
		 }
		 
		// ArrayAdapter adapterDoc = new ArrayAdapter(this,android.R.layout.simple_list_item_1,stDoc);
		// etDoctor.setAdapter(adapterDoc);
		 
		 
		 FilterWithSpaceAdapter<String> adapter1;
		 adapter1 = new FilterWithSpaceAdapter<String>(PatientInformation.this,
		            android.R.layout.simple_dropdown_item_1line, stDoc);
		 etDoctor.setAdapter(adapter1);
		 
		///////////
		 
        List<String> list = new ArrayList<String>();
        list.add("- Chọn giới tính -");
        list.add("Nam");
        list.add("Nữ");
         
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);            
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etSex.setAdapter(dataAdapter);
        
        ///////////
		 
       List<String> listDL = new ArrayList<String>();
       listDL.add("0");
       listDL.add("3000");
       listDL.add("5000");
       listDL.add("10000");
       listDL.add("20000");
       listDL.add("50000");
       listDL.add("200000");
       
       ArrayAdapter<String> dataAdapterDL = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listDL);            
       dataAdapterDL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       etDiLai.setAdapter(dataAdapterDL);
       etDiLai.setSelection(3);     
	        
       /*/////////// Event
       1/ Thẻ mệnh giá 100,000 VNĐ (giảm trực tiếp khi KH có thẻ)
       2/ Thẻ mệnh giá 200,000 VNĐ (giảm trực tiếp khi KH có thẻ)
       3/ Thẻ mệnh giá 500,000 VNĐ (giảm trực tiếp khi KH có thẻ)
       4/ Thẻ mệnh giá 1,000,000 VNĐ (giảm trực tiếp khi KH có thẻ)
       5/ Thẻ mệnh giá 200,000 VNĐ (giảm khi KH sử dụng dịch vụ trên 1 triệu)*/
       
       List<String> listGG = new ArrayList<String>();
       listGG.add("- Không có thẻ -");
       listGG.add("- Thẻ mệnh giá 100,000 VNĐ -");
       listGG.add("- Thẻ mệnh giá 200,000 VNĐ -");
       listGG.add("- Thẻ mệnh giá 500,000 VNĐ -");
       listGG.add("- Thẻ mệnh giá 1,000,000 VNĐ -");
       listGG.add("- Thẻ 200,000 VNĐ (trên 1 triệu) -");
       
       ArrayAdapter<String> dataAdapterGG = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listGG);            
       dataAdapterGG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       etGG.setAdapter(dataAdapterGG);
       etGG.setSelection(0);     
	        
	   /////////// Event
        
        //etPID.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        
        etPID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
		        {
					if (etPID.getText().toString().trim().length() > 3)
					{
						AsyncCallWS x = new AsyncCallWS();
						x.execute("");
					}
		        }
			}
		});
      
        etBOD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if (!hasFocus)
		        {
					String strBOD = String.valueOf(etBOD.getText());
            		
            		if (strBOD.length() < 4)
            		{
            			etBOD.setText("" + (2015 - Integer.parseInt(strBOD)));
            		}
		        }
			}
		});
        
        etDoctorID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
			
				if (!arg1)
		        {
					Doctor xxy = myDBDoc.selectOneDocs(String.valueOf(etDoctorID.getText()));
					if (xxy != null)
					{
						etDoctor.setText(xxy.getName());
					}
		        }
				
			}
		});
        
        etLyDo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

            	String xxx = myDbHelper.getOneDiag(etLyDo.getText().toString());
				if (xxx != null)
				{
					etLyDoE.setText(xxx);
				}
               
            }
        });

		etSID.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
				Editor editor= getSharedPreferences("SID", 0).edit();
			    editor.putString("SID", etTime.getText().toString() + "-" + etSID.getText().toString());
			    editor.commit();
			    Log.d("CONTENT", "" + etSID.getText().toString());
			    ObjectID = "";
			}
		});
		
		etSID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if (!hasFocus)
		        {
					String xid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
					List<Patient> arPatient = myDbHelper.getAllPatient(xid, 0);
					if (arPatient.size() > 0)
					{
						Patient lPatient = arPatient.get(0);
						etPID.setText("" + lPatient.getPid());
						etSID.setText("" + lPatient.getSid().split("-")[1]);
						etName.setText("" + lPatient.getPatientname());
						etPhone.setText("" + lPatient.getPhone());
						etAddress.setText("" + lPatient.getAddress());
						etDoctorID.setText("" + lPatient.getDoctorID());
						etChietKhau.setText("" + lPatient.getSumpertage());
						etCHanDoan.setText("" + lPatient.getCommend());
						etLyDo.setText("" + lPatient.getDiagnostic());
						etBOD.setText("" + lPatient.getAge());
						
						if (lPatient.getSex().equals("M"))
						{etSex.setSelection(1);}
						else
						{etSex.setSelection(2);}
						
						switch (lPatient.getGG()) 
						{
							case 100: etGG.setSelection(1); break;
							case 200: etGG.setSelection(2); break;
							case 500: etGG.setSelection(3); break;
							case 1000: etGG.setSelection(4); break;
							default:break;
						} 
						
						etCanbo.setText("" + lPatient.getRandom());
						ObjectID = lPatient.getObjectID();
						
					}
					else
					{
						int x = randInt(1000, 9999);
						etCanbo.setText("" + x);
					}
		        }
				
			}
		});
		
		etChietKhau.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if (!hasFocus)
		        {
					if (etChietKhau.getText().equals("0") == false)
					{
						etGG.setSelection(0);
					}
		        }
				
			}
		});
		
		
		etGG.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        
		    	if (etGG.getSelectedItemPosition() != 0)
		    	{
		    		etChietKhau.setText("0");
		    	}
		    	
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		if (etCanbo.getText().toString().trim().length() < 2)
		{
			etCanbo.setText(CanboID);
		}
		
		etCanbo.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
		    @Override
		    public void onFocusChange(View v, boolean hasFocus)
		    {
		        if (!hasFocus)
		        {
		        	CanboID = etCanbo.getText().toString();
		        }
		    }
		});
		
		
		etDoctor.setOnItemClickListener(autoItemSelectedListner);
		
		//////////////////////////////////////// Run the syncer
		
		if (isMyServiceRunning() == false)
		{
			Intent ServiceIntent = new Intent(this, TestSecService.class);
	        this.startService(ServiceIntent);
		}
		
		//////////////////////////////////////// Yes or No
	
		isInsert = 1;
	}
	
	/*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	            //Yes button clicked
	        	

	        	isInsert = 1;
	        	
	        	///
	        	
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            //No button clicked

	        	isInsert = 0;
	            break;
	        }
	    }
	};*/
	
	public void clearFragResult()
	{
		List<String> lSID = myDbHelper.getAllFraq();
    	for (int i = 0; i < lSID.size(); i++)
    	{
    		if (myDbHelper.checkValidSID(lSID.get(i)) == 0)
    		{
    			myDbHelper.deleteFrag(lSID.get(i));
    			Log.d("DELETE FRAG", "" + lSID.get(i));
    		}
    	}
	}
	
	private OnItemClickListener autoItemSelectedListner = new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	    {
	        String dtObject = String.valueOf(arg0.getItemAtPosition(arg2)).split("  ")[0];
	        etDoctorID.setText(dtObject);
	    }
	};

	@Override
	public void onResume()
	{
		super.onResume();
		
		// CLEAR
		
		
		
		// START SERVICE
		
		if (isMyServiceRunning() == false)
		{
			Intent ServiceIntent = new Intent(this, TestSecService.class);
	        this.startService(ServiceIntent);
	        Log.d("Đã khởi động lại service", "GO SERVICE! GO!");
		}
		
		
		//clearFragResult();
		
		if (MainActivity.packageFromCaller != null)
        {
			if(MainActivity.packageFromCaller.getString("sid") == null) 
			{
				Log.d("DUMB DA VAO", "DA DUMB VAO");
				
		        String name = MainActivity.packageFromCaller.getString("name");
		        String mobile = MainActivity.packageFromCaller.getString("mobile");
		        String phone = MainActivity.packageFromCaller.getString("phone");
		        maLH = MainActivity.packageFromCaller.getString("malh");
		        String namsinh = MainActivity.packageFromCaller.getString("namsinh");
		        String gioitinh = MainActivity.packageFromCaller.getString("gioitinh");
		        
		        //yeucauXN = MainActivity.packageFromCaller.getString("yeucauXN");
		        //Log.d("YEU CAU XN", "" + yeucauXN);
		        
		        
		        if(etName.getText().toString().trim().equals(""))
		        {
			        etName.setText(name.split(":")[1].split("\\[")[0]);
			        etAddress.setText(phone);
			        if (mobile.length() > 20)
			        {
			        	etPhone.setText(mobile.substring(0, 20));
			        }
			        else 
			        {
			        	etPhone.setText(mobile);
					}
			        
			        if (namsinh.equals("0") == false)
			        {
			        	etBOD.setText("" + namsinh);
			        }
			        
			        if (gioitinh.equals("1"))
					{etSex.setSelection(1);}
					else if (gioitinh.equals("0"))
					{etSex.setSelection(2);}
		        }
		        
			}
			else
			{
				String sid = MainActivity.packageFromCaller.getString("sid");
				
				Patient lPatient = myDbHelper.getAllPatient(sid, 0).get(0);
				
				etSID.setText("" + lPatient.getSid().split("-")[1]);
				etName.setText("" + lPatient.getPatientname());
				etPhone.setText("" + lPatient.getPhone());
				etAddress.setText("" + lPatient.getAddress());
				etDoctorID.setText("" + lPatient.getDoctorID());
				etChietKhau.setText("" + lPatient.getSumpertage());
				etCHanDoan.setText("" + lPatient.getCommend());
				etLyDo.setText("" + lPatient.getDiagnostic());
				etBOD.setText("" + lPatient.getAge());
				
				
				if (lPatient.getSex().equals("M"))
				{
					etSex.setSelection(1);
				}
				else
				{
					etSex.setSelection(2);
				}
				
				ObjectID = lPatient.getObjectID();
			}
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        
        case R.id.menu_clear:
            Toast.makeText(PatientInformation.this, "ĐÃ XÓA TOÀN BỘ THÔNG TIN KHÁCH HÀNG", Toast.LENGTH_SHORT).show();
            myDbHelper.deleteAllForSync();
            return true;
            
        /*case R.id.menu_clear_error:
            //Toast.makeText(PatientInformation.this, "Clear Error", Toast.LENGTH_SHORT).show();
            //myDbHelper.deleteError();
        	//showNoty();
            return true;*/
            
        case R.id.menu_check:
            // Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            Toast.makeText(PatientInformation.this, isMyServiceRunning() == true ? "Đang chạy" : "Ko chạy" , Toast.LENGTH_SHORT).show();
            return true;
            
        case R.id.menu_sync:
            // Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            Toast.makeText(PatientInformation.this, "Synced", Toast.LENGTH_SHORT).show();
            sync();
            return true;
            
            

            
        case R.id.scan:
        	scanBarCode();
        	return true;
        	
        case R.id.error:
        	showError();
        	return true;
           
            

 
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	Intent intentx;
	
	public void showError()
	{
		String[] x = myDbHelper.getError();
		
		if (x[0] != null && x[1] != null)
		{
			etAddress.setText(x[1]);
		}
	}
	
	public void showNoty()
	{
	        String ns = Context.NOTIFICATION_SERVICE;

	        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

	        int icon = R.drawable.btn_blue_matte;
	        CharSequence tickerText = "Ready for Play time."; // ticker-text
	        long when = System.currentTimeMillis();
	        Context context = getApplicationContext();
	        CharSequence contentTitle = "Play Time";
	        CharSequence contentText = "Your match is at ";
	        Intent notificationIntent = new Intent(this, TestSecService.class);
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	        Notification notification = new Notification(icon, tickerText, when);
	        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        // and this

	        mNotificationManager.notify(126, notification);

	        notification.defaults |= Notification.DEFAULT_SOUND;
	        notification.vibrate = new long[] { 0, 100, 200, 300 };
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	}
	
	private class AsyncCallWS extends AsyncTask<String, String, String> {
        @SuppressWarnings("deprecation")
		@Override
        protected String doInBackground(String... params) {
          //  Log.i(TAG, "doInBackground");
        	//txtMain.setText("haha2");
    		String response = "";
    		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);       
         
    		request.addProperty("PID", String.valueOf(etPID.getText().toString()));
    		
    		
            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           
            
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
           
            try {
                  HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 13000);
                 
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
        		String[] pidInfo = result.split("_");
        		
        		etName.setText(pidInfo[0]);
        		etBOD.setText(String.valueOf(pidInfo[1]));
        		if(pidInfo[2].equals("M"))
        		{etSex.setSelection(1);}
        		else
        		{etSex.setSelection(2);}
        		etAddress.setText(pidInfo[4]);
        		etPhone.setText(pidInfo[3]);
        	}
        	else
        	{
        		Toast.makeText(PatientInformation.this, "Không có thông tin của thẻ PID này", Toast.LENGTH_SHORT).show();
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
	
	public void scanBarCode()
	{
		 intentx = new Intent(this, CaptureActivity.class);
	    startActivityForResult(intentx, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    //super.onActivityResult(requestCode, resultCode, data);
	    //Retrieve data in the intent
	    //Log.d("ĐÃ VE", "");
	    String editTextValue = data.getStringExtra("valueId");
	    etSID.setText(editTextValue);
	}
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	


	public void sync()
	{
		///
		Intent ServiceIntent = new Intent(this, TestSecService.class);
        this.startService(ServiceIntent);
        ////
        
        
        /*Intent UpdateSev = new Intent(this, UpdateService.class);
        UpdateSev.putExtra("receiver", new UpdateResult(new Handler()));
        this.startService(UpdateSev);*/
        
        //
	}
	
	public void clear()
	{
		etNgay.setText("");
		etSID.setText("");
		etName.setText("");
		etBOD.setText("");
		etAddress.setText("");
		etDoctor.setText("");
		etChietKhau.setText("");
		etCHanDoan.setText("");
		etLyDo.setText("");
		etTime.setText("");
		etPhone.setText("");
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
	

	
	@SuppressWarnings("deprecation")
	protected void onDestroy() 
    {
        super.onDestroy();
        if (myDbHelper != null) 
        {
        	myDbHelper.close();
        }
    }

	///////////////////////////////////////
	


}
