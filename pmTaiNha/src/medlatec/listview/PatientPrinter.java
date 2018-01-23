package medlatec.listview;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.auth.DocSo;
import medlatec.listview.auth.DocSoBeTri;
import medlatec.listview.auth.MarshalDouble;
import medlatec.listview.auth.RemoveAccent;
import medlatec.listview.auth.Utils;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.connect.DatabaseHandlerUser;
import medlatec.listview.object.Patient;
import medlatec.listview.object.TestCode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Time;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.google.zxing.common.reedsolomon.GenericGF;


public class PatientPrinter extends Activity {

	///////////// Printer variable
		
	public static BluetoothAdapter myBluetoothAdapter;
	private Button mBtnConnetBluetoothDevice = null;
	private Button mBtnPrint = null;
	private Button mBtnPrintKH;
	private EditText etDate;
	private EditText etTime;
	private Button mBtnConnectWebservice = null;
	
	//////////////////
	
	private EditText etMaThe = null;
	private Button btnLayThongTin = null;
	private EditText etSoTienConLai = null;
	private EditText etSATN = null;
	private EditText etKNTN = null;
	private EditText etDD = null;
	private Button btnThanhToan = null;
	
	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION = "http://syn.medlatec.vn/FindTheTT";
	private static final String METHOD_NAME = "FindTheTT";
	
	private static final String SOAP_ACTION2 = "http://syn.medlatec.vn/ThanhToanThe";
	private static final String METHOD_NAME2 = "ThanhToanThe";
	
	private static final String SOAP_ACTION3 = "http://syn.medlatec.vn/TraKetQua";
	private static final String METHOD_NAME3 = "TraKetQua";
	
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	public static final String DEVICE_NAME = "device_name";
	
	////
	public String SelectedBDAddress;
	private EditText txtMain = null;
	
	////
	DatabaseHandler myDbHelper;
	DatabaseHandlerUser myDbHelperUser;
	
	////
	double xTotal = 0;
	double xChietkhau = 0;
	int xGiamgia = 0;
	double total;
	String sothe;
	int allowKado = 0;
	int allowCheck = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printer_layout);
		
		////
		myDbHelper = new DatabaseHandler(this);
		myDbHelper.initialise();
		
		myDbHelperUser = new DatabaseHandlerUser(this);
		myDbHelperUser.initialise();
		
		///////////////////////////////////////////////////////////////////////////////
		
		txtMain = (EditText)findViewById(R.id.txtMain);
		mBtnPrintKH = (Button) findViewById(R.id.etSide);
		mBtnPrint = (Button)findViewById(R.id.btnPrint);
		etDate = (EditText) findViewById(R.id.spDate);
		etTime = (EditText) findViewById(R.id.spTime);
		
		///////////////////////////////////////////
        
	     etMaThe = (EditText) findViewById(R.id.etMaThe);
		 btnLayThongTin = (Button) findViewById(R.id.btnLayThongTin);
		 etSoTienConLai = (EditText) findViewById(R.id.etSoTienConLai);
		 etSATN = (EditText) findViewById(R.id.etSATN);
		 etKNTN = (EditText) findViewById(R.id.etKNTN);
		 etDD = (EditText) findViewById(R.id.etDuaDon);
		 btnThanhToan = (Button) findViewById(R.id.btnThanhToan);
		
		
		
    	mBtnPrint.setOnClickListener(mBtnPrintOnClickListener);
    	mBtnPrintKH.setOnClickListener(mBtnPrintKHOnClickListener);
    	mBtnConnetBluetoothDevice = (Button)findViewById(R.id.btnConnect);
    	mBtnConnetBluetoothDevice.setOnClickListener(ConnectBluetoothClickListener);
		
    	//////////////////////////////////////////////////////////////////////////////
    	
    	btnLayThongTin.setOnClickListener(new OnClickListener() 
    	{
    		@Override
    		public void onClick(View arg0) 
    		{
    			String mathe = etMaThe.getText().toString();
    			new SomeTask().execute(mathe);
    		}
    	});
    	
		btnThanhToan.setOnClickListener(btnThanhToanOnClickListener);
    	
		//////////////////////////////////////////////////////////////////////////////
		
    	etDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
        
                mDatePicker = new DatePickerDialog(PatientPrinter.this, new OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        etDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        fillSelected(-1, 0);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
			
			}
		});
		
    	etTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Calendar mcurrentTime = Calendar.getInstance();
	            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
	            int minute = mcurrentTime.get(Calendar.MINUTE);
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(PatientPrinter.this, new TimePickerDialog.OnTimeSetListener() {
	                @Override
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                    etTime.setText( selectedHour + ":" + selectedMinute);
	                    fillSelected(-1, 0);
	                }
	            }, hour, minute, true);//Yes 24 hour time
	            mTimePicker.setTitle("Select Time");
	            mTimePicker.show();
				
				
			}
		});
		//////////////////////////////////////////////////////////////////////////////
		
		SelectedBDAddress = "";
        if((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter())==null)
        {
     		Toast.makeText(this,"Did not find the Bluetooth adapter", Toast.LENGTH_LONG).show();
        }
        if(!myBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);    
            startActivityForResult(enableBtIntent, 2);
        }
        
	}

	public void fillSelected(int printStatus, int type)
	{
		String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
	
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dfx = new SimpleDateFormat("dd/MM/yyyy");
			String formattedDate = dfx.format(cal.getTime());
			xTotal = 0;
		
			///////////////////////////////////
			
			switch (PatientInformation.etGG.getSelectedItemPosition()) {
			case 1: xGiamgia = 100; break;
			case 2: xGiamgia = 200; break;
			case 3: xGiamgia = 500; break;
			case 4: xGiamgia = 1000; break;
			case 5: xGiamgia = 200; break;
			default: break;
	        }
			
			///////////////////////////////////
			
			String fau = "";
			
			fau += "Phieu chi dinh va bien lai thu  tien tai nha: \n";
			fau += "============================= \n";
			fau += "Ngay: " + formattedDate + "\n";
			fau += "Ma tra cuu: " + String.valueOf(PatientInformation.etSID.getText()) + "\n";
			fau += "Ma bao mat: " + PatientInformation.etCanbo.getText() + "\n";
			fau += "Ho ten: " + PatientInformation.etName.getText() + "\n";
			fau += "Nam sinh: " + PatientInformation.etBOD.getText() + "\n";
			fau += "Dia chi: " + PatientInformation.etAddress.getText() + "\n";
			fau += "So dien thoai: " + PatientInformation.etPhone.getText() + "\n";
			
			////////
			fau += "============================= \n";
			
		    fau += "Cac d/v dang ki:(DVT-nghin dong)\n";
		    
		    total = 0;
		
	        List<TestCode> contacts = myDbHelper.getAllContract4Print(sid, printStatus);
	        
	        TestCode ptObject;
	        
	        for (int i = 0; i < contacts.size(); i++)
	        {
	        	ptObject = (TestCode)contacts.get(i);
	        	
	        	// STT
	        	if (i < 9)
	        	{
	        		fau = fau + "" + (i+1) + "  ";
	        	}
	        	else
	        	{
	        		fau = fau + "" + (i+1) + " ";
	        	}
	        
	       		/// Name
	       		if (ptObject.getTestName().length() < 25)
	       		{
	       			fau = fau + ptObject.getTestName();
	       			for (int y = 0; y < 25 - ptObject.getTestName().length(); y++)
	       			{
	       				fau = fau + " ";
	       			}
	       		}
	       		else if (ptObject.getTestName().length() >= 25)
	       		{
	       			fau = fau + ptObject.getTestName().substring(0, 22) + ".. ";
	       		}
	       		
	       		/// Price
	       		if (ptObject.getPrice().length() < 4) 
	       		{
	       			for (int z = 0; z < 4 - ptObject.getPrice().length(); z++)
	       			{
	       				fau = fau + " ";
	       			}
	       			fau = fau + ptObject.getPrice() + "\n";
	       		}
	       		else
	       		{
	       			fau = fau + ptObject.getPrice() + "\n";
	       		}
	       		
	       		total += Double.parseDouble(ptObject.getPrice());
	        }
	        
        	/////////////////////////////////////////////////////////////// Tong dv
	        
	        fau += "============================= \n";
	        
	        fau = fau + "Tong dịch vụ:          ";
	        
	        if ((""+total).length() < 4) 
       		{
       			for (int z = 0; z < 4 - (""+total).length(); z++)
       			{
       				fau = fau + " ";
       			}
       			fau = fau + total + "";
       		}
       		else
       		{
       			fau = fau + total + "";
       		}
	        
        	/////////////////////////////////////////////////////////////// Chiết khấu
	        
	        if (allowKado == 0)
	        {
		        if (PatientInformation.etChietKhau.getText().equals("0"))
		        {
		        	xChietkhau = 0;
		        }
		        else
		        {
		        	double tienChietkhau = Double.parseDouble(String.valueOf(PatientInformation.etChietKhau.getText()));
		        	
		        	xChietkhau = ((tienChietkhau/100) * (double)total);
		        }
	        }
	        else
	        {
	        	if (PatientInformation.etChietKhau.getText().equals("0"))
		        {
		        	xChietkhau = 0;
		        }
		        else
		        {
		        	xChietkhau = (double)total;
		        }
	        }
	        
	        DecimalFormat df= new DecimalFormat("#.##");
	        fau = fau + "\nChiet khau:                 ";
	        
	        if (df.format(xChietkhau).length() < 4) 
       		{
       			for (int z = 0; z < 4 - df.format(xChietkhau).length(); z++)
       			{
       				fau = fau + " ";
       			}
       			fau = fau + df.format(xChietkhau) + "";
       		}
       		else
       		{
       			fau = fau + df.format(xChietkhau) + "";
       		}
	        
	        
	        ////////////////////////////////////////////////////////////// Tiendilai
	        
	        double tiendilai = 0;
	        if (PatientInformation.etDiLai.getSelectedItem().toString().equals("0") == false)
	        {
	        	tiendilai = Double.parseDouble(PatientInformation.etDiLai.getSelectedItem().toString()) / 1000;
	        }
	        
	        fau = fau + "\nTien di lai:                ";
	        if ((""+tiendilai).length() < 4) 
       		{
       			for (int z = 0; z < 4 - (""+tiendilai).length(); z++)
       			{
       				fau = fau + " ";
       			}
       			fau = fau + tiendilai+ "";
       		}
       		else
       		{
       			fau = fau + tiendilai + "";
       		}
	        
	        //////////////////////////////////////////////////////////////Tongtien
	        
	        
	        
/*	        fau = fau + "\nTong tien:                ";
	        xTotal =  (total - xChietkhau) + tiendilai;
	        
	        if ((""+xTotal).length() < 6) 
       		{
       			for (int z = 0; z < 6 - (""+xTotal).length(); z++)
       			{
       				fau = fau + " ";
       			}
       			fau = fau + xTotal+ "";
       		}
       		else
       		{
       			fau = fau + xTotal + "";
       		}*/
	        
	        /////////////////////////////////////////////////////////////// The giam gia
	        
	     	
	        if (PatientInformation.etGG.getSelectedItemPosition() != 0)
	        {
		        fau = fau + "\nThe giam gia:          ";
		        
		        ////////////////////////////////////////////////////////////////
		        
		        if ((""+xGiamgia).length() < 4) 
	       		{
	       			for (int z = 0; z < 4 - (""+xGiamgia).length(); z++)
	       			{
	       				fau = fau + " ";
	       			}
	       			fau = fau + xGiamgia + "";
	       		}
	       		else
	       		{
	       			fau = fau + xGiamgia + "";
	       		}
		        
	        }
		    
	        
			////////////////////////////////////////////////////////////// Tongcuoi
			
	     	
	     	Patient xxx = new Patient();
	     	xxx.setSummoney(total);
	     	xxx.setGG(xGiamgia);
	     	xxx.setSumpertage(Double.parseDouble(PatientInformation.etChietKhau.getText().toString()));
	     	xxx.setTiendilai((int)tiendilai);
	     	xTotal = xxx.finalMoney();
	     	
	     	
	     	
			fau = fau + "\nTong cuoi:                ";
			
			if ((""+xTotal).length() < 6) 
			{
			for (int z = 0; z < 6 - (""+xTotal).length(); z++)
			{
				fau = fau + " ";
			}
			fau = fau + xTotal+ "";
			}
			else
			{
				fau = fau + xTotal + "";
			}
	     	
			if (allowKado == 1)
	        {
				fau = fau + "\nChi tra 100% bang the MED";
	        }
	        
	        ////////////////////////////////////////////////////////////////
	        
	        String strSoTien = new DocSoBeTri().DocSo(xTotal * 1000);
	        
	        //fau += "\n(DVT: nghin dong)";
	        fau += "\n Bang chu: " + strSoTien + "dong.";
	        //fau += "\n\n " + new DocSo().DocSoTien(xTotal * 1000);
	        
	       
	        fau += "\n(Ngoai cac khoan tren, khach hang khong phai tra them bat ky chi  phi nao.) \n";
	        
	        fau += "============================= \n";
	        
	        if (type == 0)
	        {
	        	fau += "Ky ten can bo tai nha: \n.\n.\n.\n.\n.\n...................";
	        	String startBy = (sid.split("-")[1].trim()).substring(2, 4);
	        	fau += "\n" + myDbHelperUser.getUserforBL(startBy) + "";
	        	
	        	fau += "\nGio hen tra kq." + etTime.getText().toString() + " " + etDate.getText().toString() + "\n\n.";
	        }
	        else
	        {
	        	fau += "Toi dong y lam cac xn tren: \n.\n.\n.\n.\n.\n...................";
	        	fau += "\n" + PatientInformation.etName.getText() + "";
	        	
	        	fau += "\nGio hen tra kq." + etTime.getText().toString() + " " + etDate.getText().toString() + "\n\n.";
	        }
	        
	        txtMain.setText(new RemoveAccent().removeAccent(fau));
		//}
        /////////////
        
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		Log.d("Enter form", "Enter form");
		
		getMenuInflater().inflate(R.layout.menu2, menu);
		return true;
		
		
	}
	
	private boolean getAutoState() {
	    try {
	        return Settings.System.getInt(getContentResolver(), 
	            Settings.System.AUTO_TIME) > 0;            
	    } catch (SettingNotFoundException snfe) {
	        return true;
	    }
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        
        case R.id.menu_mpos:
        	
        	try {
        	
        		
        		PackageManager packageManager = getPackageManager();
        		Intent baseIntent = new Intent("vn.mpos.partner");
        		baseIntent.setFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);
        		List<ResolveInfo> list = packageManager.queryIntentActivities(baseIntent, PackageManager.GET_RESOLVED_FILTER);

        		if (list.size() == 0) 
        		{
        			
        		} else 
        		{
        			ResolveInfo info = list.get(0);
        			Intent intent = new Intent();
        			///
        			JSONObject jsonObj = new JSONObject();
			        jsonObj.put("sid", PatientInformation.etTime.getText().toString() + "-" + PatientInformation.etSID.getText().toString()); 
			        jsonObj.put("total", (double)xTotal  * 1000);
			        
			        intent.putExtra("SENDTOMPOS", jsonObj.toString());
        			///
        			intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
        			startActivity(intent);
        			finish();
        		}
        	} 
        	catch (Exception e) 
        	{
        		
        	}
        	
        	return true;
        
        case R.id.menu_save:
        	
        	/*if (getAutoState() == false)
        	{
        		Toast.makeText(PatientPrinter.this, "Không thể cập nhật - Thời gian hệ thống đã bị thay đổi", Toast.LENGTH_SHORT).show();
        		return true;
        	}*/
        	
        	
        	
        	String month = PatientInformation.etMonth.getText().toString().trim();
        	String day = PatientInformation.etDay.getText().toString().trim();
        	String year = PatientInformation.etBOD.getText().toString().trim();
        	
            Patient hml = new Patient();
    		
    		///////////////////////////////////////////
    		
    		String phone = PatientInformation.etPhone.getText().toString();
    		
    		if (phone.length() > 20)
	        {
    			hml.setPhone(phone.substring(0, 20));
	        }
	        else 
	        {
	        	hml.setPhone(phone);
			}
    		
    		///////////////////////////////////////////
    		
    		String patientName = PatientInformation.etName.getText().toString();
    		
    		if (patientName.length() > 39)
	        {
    			hml.setPatientname(patientName.substring(0, 39));
	        }
	        else 
	        {
	        	hml.setPatientname(patientName);
			}
    		
			///////////////////////////////////////////
    		
    		String address = PatientInformation.etAddress.getText().toString();
    		
    		if (address.length() > 99)
	        {
    			hml.setAddress(address.substring(0, 99));
	        }
	        else 
	        {
	        	hml.setAddress(address);
			}
    		
			///////////////////////////////////////////

    		if(month.equals("") == false && day.equals("") == false)
    		{
    			DateTime date1 = new DateTime(DateTime.parse("1900-01-01T00:00:00Z"));
            	DateTime date2 = new DateTime(DateTime.parse(""+year+"-"+month+"-"+day+"T00:00:00Z"));
            	int days = Days.daysBetween(date1, date2).getDays();
            	hml.setAge(days + 2);
    		}
    		else
    		{
    			hml.setAge(Integer.parseInt(year));
    		}
    		
    		hml.setDiagnostic(PatientInformation.etLyDo.getText().toString());
    		hml.setDoctorID(PatientInformation.etDoctorID.getText().toString());
    		
    		if (PatientInformation.etSex.getSelectedItem().toString().equals("Nam"))
    		{
    			hml.setSex("M");
    		}
    		else
    		{
    			hml.setSex("F");
    		}
    		
    		
    		hml.setSid(PatientInformation.etTime.getText().toString() + "-" + PatientInformation.etSID.getText().toString());
    		hml.setSeq(PatientInformation.etSID.getText().toString());
    		hml.setSumpertage(Double.parseDouble(PatientInformation.etChietKhau.getText().toString()));
    		hml.setQuan(PatientInformation.txtH.getText().toString());
    		hml.setObjectID(PatientInformation.ObjectID);
    		
    		if (PatientInformation.etDiLai.getSelectedItem().toString().equals("0"))
    		{
    			hml.setTiendilai(0);
    		}
    		else
    		{
    			hml.setTiendilai(Integer.parseInt(PatientInformation.etDiLai.getSelectedItem().toString()) / 1000);
    		}
    		
            hml.setPid(PatientInformation.etPID.getText().toString());
            hml.setUserI(PatientInformation.etCanbo.getText().toString());
            
            hml.setRandom(PatientInformation.etCanbo.getText().toString());
            hml.setEmail(PatientInformation.etEmail.getText().toString());
            //hml.setPos(myDbHelper.getPos(PatientInformation.etPID.getText().toString()));
            hml.setTuvan(PatientInformation.etTV.isChecked() == true ? 1 : 0);
            
            /*listGG.add("- Không có thẻ -");
            listGG.add("- 1Thẻ mệnh giá 100,000 VNĐ -");
            listGG.add("- 2Thẻ mệnh giá 200,000 VNĐ -");
            listGG.add("- 3Thẻ mệnh giá 500,000 VNĐ -");
            listGG.add("- 4Thẻ mệnh giá 1,000,000 VNĐ -");
            listGG.add("- 5Thẻ 200,000 VNĐ (trên 1 triệu) -");*/
            
            switch (PatientInformation.etGG.getSelectedItemPosition()) {
			case 0:
				hml.setGG(0);
				break;
			case 1:
				hml.setGG(100);
				break;
			case 2:
				hml.setGG(200);
				break;
			case 3:
				hml.setGG(500);
				break;
			case 4:
				hml.setGG(1000);
				break;
			case 5:
				hml.setGG(200);
				break;
			default:
				break;
			}
            
            ///// IMPORTANT /////////////////////////////////////////////////
            if (hml.getGG() != 0)
            {
	            if (hml.getGG() >= total)
	            {
	            	hml.setSumpertage(100);
	            }
	            else
	            {
	            	DecimalFormat df= new DecimalFormat("#.##");
	            	//hml.setSumpertage(df.format(((double)hml.getGG() / ((double)total/100))));
	            	hml.setSumpertage((double)hml.getGG() / ((double)total/100));
	            }
	            
	            hml.setCommend((PatientInformation.etCS.isChecked() == true ? "(CS)" : "(0CS)") +  "(" + this.getResources().getString(R.string.version) + ")" + "(GG: " + hml.getGG() + ") " + PatientInformation.etCHanDoan.getText().toString());
            }
            else
            {
            	hml.setCommend((PatientInformation.etCS.isChecked() == true ? "(CS)" : "(0CS)") +  "(" + this.getResources().getString(R.string.version) + ") " + PatientInformation.etCHanDoan.getText().toString());
            }
            
            
            
            ////////////////////////////////////////////////////////////////
            
        	if (PatientInformation.isInsert == 1)
        	{
        		if(myDbHelper.updatePatient(hml) == 0)
        		{
		            myDbHelper.addPatient(hml);
		    		myDbHelper.updateResultStatusForSync(hml.getSid(), 0);
		    		
		    		// new 31/01 for identity family
		    		if (PatientInformation.maLH != null)
		    		{
		    			myDbHelper.updateDumb(PatientInformation.maLH, "2");
		    			myDbHelper.addDumbClone(PatientInformation.maLH, hml.getSid());
		    		}
		    		
		    		Toast.makeText(PatientPrinter.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
        		}
        		else
        		{
        			int beha = myDbHelper.checkParentBehaviour(hml.getSid());
        			
        			if (beha == 1 || beha == 0)
        			{
        				myDbHelper.updatePatientForSync(hml.getSid(), 0);
        				myDbHelper.updateResultStatusForSync(hml.getSid(), 0);
        			}
	        		
		    		Toast.makeText(PatientPrinter.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
        		}
        	}
        	
            return true;
            
            
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
	        switch (requestCode) {
	        case REQUEST_CONNECT_DEVICE_SECURE:
	            // When DeviceListActivity returns with a device to connect
	            if (resultCode == Activity.RESULT_OK) 
	            {
	            	
	            	BluetoothPrintDriver.close();  
	            	//  MAC address
	            	SelectedBDAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
	            	// 
	            	if(!BluetoothPrintDriver.OpenPrinter(SelectedBDAddress)) 	
	            	{
	            		BluetoothPrintDriver.close();
	            		PatientPrinter.this.setTitle(R.string.bluetooth_connect_fail);
            			Toast.makeText(PatientPrinter.this, R.string.bluetooth_connect_fail, Toast.LENGTH_SHORT).show();
	                	return;
	            	}
	            	else
	            	{
//	            		mTitle.setText(SelectedBDAddress);
	   				 	String bluetoothConnectSucess = PatientPrinter.this.getResources().getString(R.string.bluetooth_connect_sucess);
	   				 	PatientPrinter.this.setTitle(bluetoothConnectSucess+SelectedBDAddress);
	   				 	Toast.makeText(PatientPrinter.this, bluetoothConnectSucess + " " + SelectedBDAddress, Toast.LENGTH_SHORT).show();
	            	}
	            }
	            break;
	        case REQUEST_CONNECT_DEVICE_INSECURE:
	            // When DeviceListActivity returns with a device to connect
	            if (resultCode == Activity.RESULT_OK) 
	            {
	            		;// connectDevice(data, false);
	            }
	            break;
	        }
    }
	 
	public void printHeader()
	 {
			
			BluetoothPrintDriver.Begin();
			
			BluetoothPrintDriver.AddInverse((byte)0x01);
			
			BluetoothPrintDriver.ImportData("\t\t\t      BENH VIEN MEDLATEC      \n\n");
			//BluetoothPrintDriver.ImportData("\r");
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
	 }
	 
	OnClickListener mBtnPrintOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(BluetoothPrintDriver.IsNoConnection())
				{return;}
				
				printHeader();
				
				/////
				
				BluetoothPrintDriver.Begin();
				
				fillSelected(0, 0); 
				
				myDbHelper.updateBLStatus(PatientInformation.etTime.getText().toString() + "-" + PatientInformation.etSID.getText().toString());
				
				/////
				
				String tmpContent = txtMain.getText().toString();
				BluetoothPrintDriver.ImportData(tmpContent);
				BluetoothPrintDriver.ImportData("\r");
				BluetoothPrintDriver.excute();
				BluetoothPrintDriver.ClearData();
				
				fillSelected(-1, 0);
			}
		};
	    
	OnClickListener ConnectBluetoothClickListener = new OnClickListener() {
			Intent serverIntent = null;
			public void onClick(View arg0)
			{
				//Launch the DeviceListActivity to see devices and do scan
	            serverIntent = new Intent(PatientPrinter.this, DeviceListActivity.class);
	            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			}
		};

	OnClickListener mBtnPrintKHOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(BluetoothPrintDriver.IsNoConnection())
				{return;}
				
				printHeader();
				
				BluetoothPrintDriver.Begin();
				
				fillSelected(0, 1); 
				
				String tmpContent = txtMain.getText().toString();
				BluetoothPrintDriver.ImportData(tmpContent);
				BluetoothPrintDriver.ImportData("\r");
				BluetoothPrintDriver.excute();
				BluetoothPrintDriver.ClearData();
				
				fillSelected(-1, 0);
			}
		};
		
	//////////////////	Thẻ giảm giá
		
	private class SomeTask extends AsyncTask<String, String, String> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(PatientPrinter.this);

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
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);     
	        //Use this to add parameters
			request.addProperty("sothe", params[0]);
			
			
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
	    	String[] badDay = result.split("_");
	    	
	    	sothe = badDay[0];
	    	etSoTienConLai.setText(badDay[2]);
	    	etSATN.setText(badDay[3]);
	    	etKNTN.setText(badDay[3]);
	    	etDD.setText(badDay[4]);
	    	
	        // after completed finished the progressbar
	        Dialog.dismiss();
	    }
	}
	
	private class InsertTask extends AsyncTask<Void, String, String> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(PatientPrinter.this);

	    @Override
	    protected void onPreExecute()
	    {
	        Dialog.setMessage("Cập nhật dịch vụ, xin chờ ...! ");
	        Dialog.show();
	        Dialog.setCancelable(false);
	        Dialog.setCanceledOnTouchOutside(false);
	    }

	    @Override
	    protected String doInBackground(Void... params) 
	    {
	    	String response = "";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);     
	        //Use this to add parameters
			
			String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
			double tong = total;
			
			if (PatientInformation.etDiLai.getSelectedItem().toString().equals("0") == false)
	        {
				tong = tong + Double.parseDouble(PatientInformation.etDiLai.getSelectedItem().toString()) / 1000;
	        }

			request.addProperty("sothe",sothe);
			request.addProperty("sid", sid);
			request.addProperty("summoney", tong);
			
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
	              androidHttpTransport.call(SOAP_ACTION2, envelope);
	             
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
	    	if(result.equals("insert"))
	    	{
	    		Toast.makeText(PatientPrinter.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
	    		allowKado = 1;
	    		fillSelected(-1, 0);
	    		
	    	}
	    	else if(result.equals("update"))
	    	{
	    		Toast.makeText(PatientPrinter.this, "Cập nhật lại thanh toán thành công", Toast.LENGTH_SHORT).show();
	    		allowKado = 1;
	    		fillSelected(-1, 0);
	    	}
	    	else
	    	{
	    		Toast.makeText(PatientPrinter.this, "Có lỗi xảy ra, không thể cập nhật. Xin vui lòng thử lại", Toast.LENGTH_SHORT).show();
	    		allowKado = 0;
	    	}
	    	
	        // after completed finished the progressbar
	        Dialog.dismiss();
	    }
	}
	
	

	OnClickListener btnThanhToanOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				new InsertTask().execute();
			}
		};
		
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		
		fillSelected(-1, 0);
		
		///// Get info from MPOS
		String jsonArray = getIntent().getStringExtra("medlatec.listview");
        try 
        {
        	if(jsonArray != null)
        	{
	            JSONObject array = new JSONObject(jsonArray);
	            
	            //String result = array.getString("receipt");
	            //result = result + "/n" + array.getString("status_code");
	            //result = result + "/n" + array.getString("msg");
	            //result = result + "/n" + array.getString("sid");
	            myDbHelper.addPos(array.getString("sid"), xTotal);
	            //txtMain.setText(result);
        	}
        } 
        catch (JSONException e) 
        {
            e.printStackTrace();
        }
        
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
		
}
