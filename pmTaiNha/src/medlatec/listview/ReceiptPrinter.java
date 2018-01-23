package medlatec.listview;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;

import medlatec.listview.auth.DocSoBeTri;
import medlatec.listview.auth.RemoveAccent;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.connect.DatabaseHandlerDoctor;
import medlatec.listview.connect.DatabaseHandlerUser;
import medlatec.listview.object.Doctor;
import medlatec.listview.object.Patient;
import medlatec.listview.object.TestCode;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReceiptPrinter extends Activity {

	public static BluetoothAdapter myBluetoothAdapter;
	private Button mBtnConnetBluetoothDevice = null;
	private Button mBtnPrint = null;
	private Button mBtnPrintKH;
	private EditText etDate;
	private EditText etTime;
	private Button mBtnConnectWebservice = null;
	
	// INSTANT VARIABLE
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	public static final String DEVICE_NAME = "device_name";
	public String SelectedBDAddress;
	private EditText txtMain = null;
	DatabaseHandler myDbHelper;
	DatabaseHandlerDoctor myDbHelperDoc;
	DatabaseHandlerUser myDbHelperUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);
		
		myDbHelper = new DatabaseHandler(this);
		myDbHelper.initialise();
		
		myDbHelperDoc = new DatabaseHandlerDoctor(this);
		myDbHelperDoc.initialise();
		
		myDbHelperUser = new DatabaseHandlerUser(this);
		myDbHelperUser.initialise();
		
		
		//
		
		txtMain = (EditText)findViewById(R.id.txtMain);
		mBtnPrintKH = (Button) findViewById(R.id.etSide);
		mBtnPrint = (Button)findViewById(R.id.btnPrint);
		etDate = (EditText) findViewById(R.id.spDate);
		etTime = (EditText) findViewById(R.id.spTime);
    	mBtnPrint.setOnClickListener(mBtnPrintOnClickListener);
    	mBtnPrintKH.setOnClickListener(mBtnPrintKHOnClickListener);
    	mBtnConnetBluetoothDevice = (Button)findViewById(R.id.btnConnect);
    	mBtnConnetBluetoothDevice.setOnClickListener(ConnectBluetoothClickListener);
		
    	//////////////////////////////////////////////////////////////////////////////
    	
    	etDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(ReceiptPrinter.this, new OnDateSetListener() {
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
	            mTimePicker = new TimePickerDialog(ReceiptPrinter.this, new TimePickerDialog.OnTimeSetListener() {
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
            serverIntent = new Intent(ReceiptPrinter.this, DeviceListActivity.class);
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
	
	
	public void printHeader()
	 {
			
			BluetoothPrintDriver.Begin();
			BluetoothPrintDriver.AddInverse((byte)0x01);
			BluetoothPrintDriver.ImportData("\t\t\t      BENH VIEN MEDLATEC      \n\n");
			BluetoothPrintDriver.excute();
			BluetoothPrintDriver.ClearData();
	 }
	
	public void fillSelected(int printStatus, int type)
	{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dfx = new SimpleDateFormat("dd/MM/yyyy");
			String formattedDate = dfx.format(cal.getTime());
			String docID = ReceiptTK.txt2Name.getText().toString();
			Doctor docObject = myDbHelperDoc.selectDocInfo(docID);
			double xTotal = 0;
			///////////////////////////////////
			
			String fau = "";
			
			fau += "Bien lai thu  tien tai nha: \n";
			fau += "============================= \n";
			fau += "Ngay: " + formattedDate + "\n";
			fau += "Ma phong kham: " + docObject.getID() + "\n";
			fau += "Ten phong kham:" + docObject.getName() + "\n";
			fau += "Dia chi: " + docObject.getAddress() + "\n";
			fau += "Dien thoai: " + docObject.getPhone() + "\n";
			
			fau += "============================= \n";
			
		    fau += "Danh sach khach hang: \n";
		    
			List<Patient> listPatient = myDbHelper.getAllPatient(docID, 1);
			double finalMoney = 0;
			for (int i = 0; i < listPatient.size(); i++)
			{
				listPatient.get(i).setSummoney(myDbHelper.getSumMoney(listPatient.get(i).getSid()));
				fau += (i+1) + ". Ma SID: " + listPatient.get(i).getSid() + "\n";
				fau += "   Ho ten: " + listPatient.get(i).getPatientname() + "\n";
				fau += "   Gioi tinh: " + listPatient.get(i).getSex() + " - Nam sinh: " + listPatient.get(i).getAge() +  "\n";
				finalMoney = listPatient.get(i).finalMoney();
				xTotal += finalMoney;
				fau += "   Tong tien: " + finalMoney + "\n";
			}
			
			fau += "============================= \n";

	        
	      /// Tongcuoi Tiendilai
	        fau = fau + "Tổng cuối:                ";
	        
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
	        
	        String strSoTien = new DocSoBeTri().DocSo(xTotal * 1000);
	        
	        fau += "\n Bang chu: " + strSoTien + "dong.";
	        //fau += "\n\n " + new DocSo().DocSoTien(xTotal * 1000);
	        
	        //au += "\n(DVT: nghin dong)";
	        fau += "(Ngoai cac khoan tren, khach hang khong phai tra them bat ky chi  phi nao.) \n";
	        
	        fau += "============================= \n";
	        
	        if (type == 0)
	        {
	        	fau += "Ky ten can bo tai nha: \n.\n.\n.\n.\n.\n...................";
	        	String startBy = (listPatient.get(0).getSid().split("-")[1].trim()).substring(2, 4);
	        	fau += "\n" + myDbHelperUser.getUserforBL(startBy) + "";
	        	fau += "\nGiờ hẹn trả kq." + etTime.getText().toString() + " " + etDate.getText().toString() + "\n\n.";
	        }
	        else
	        {
	        	//fau += "Ten phong kham:" + docObject.getName() + "\n";
	        	fau += "Ky ten khach hang: \n.\n.\n.\n.\n.\n...................";
	        	//fau += "\n" + PatientInformation.etName.getText() + "";
	        	
	        	fau += "\nGiờ hẹn trả kq." + etTime.getText().toString() + " " + etDate.getText().toString() + "\n\n.";
	        }
	        
	        txtMain.setText(new RemoveAccent().removeAccent(fau));
		//}
        /////////////
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receipt, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		fillSelected(-1, 0);
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
