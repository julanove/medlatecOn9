package medlatec.listview;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.adapter.MainMenuAdapter;
import medlatec.listview.auth.MarshalDouble;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.connect.DatabaseHandlerDoctor;
import medlatec.listview.connect.DatabaseHandlerPID;
import medlatec.listview.connect.DatabaseHandlerTestCode;
import medlatec.listview.connect.DatabaseHandlerUser;
import medlatec.listview.object.Doctor;
import medlatec.listview.object.PID;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import medlatec.listview.object.User;
import medlatec.listview.sync.TestSecService;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Front_Activity extends Activity {

	//private Button btnPrepare;
	//private Button btnInput;
	//private Button btnSynced;
	//private Button btnUpdate;
	
	GridView grid;
	  String[] web = {
	      "TIẾP NHẬN THÔNG TIN",
	      "QUẢN LÝ ĐỒNG BỘ",
	      "DANH SÁCH TỔNG ĐÀI",
	      "CẬP NHẬT PHIÊN BẢN",
	      "IN HÓA ĐƠN THEO MÃ",
	      "THỐNG KÊ TRƯỞNG NHÓM",
	      "HỖ TRỢ TRẢ KẾT QUẢ"
	  } ;
	  int[] imageId = {
	      R.drawable.front_contact,
	      R.drawable.front_text,
	      R.drawable.fron_run,
	      R.drawable.front_update,
	      R.drawable.front_doctor,
	      R.drawable.checklist,
	      R.drawable.function7
	  };
	  

	Context thisContext;
	DatabaseHandler myDbHelper = null;
    DatabaseHandlerDoctor myDbHelperDoc = null;
	DatabaseHandlerTestCode myDbHelperTest = null;
	DatabaseHandlerUser myDbHelperUser = null;
	DatabaseHandlerPID myDbHelperPID = null;
	
	// Download
	
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private Button startBtn;
    private ProgressDialog mProgressDialog;
    
    // Login
    
    private Button btnLogin;
    private EditText edUser;
	private EditText edPass;
	public static String edRealName = "";
	public static String edUserID= "";
	public static String edID;
	public TextView checkConn;
	
	
	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION_V = "http://syn.medlatec.vn/SelectVersion";
	private static final String METHOD_NAME_V = "SelectVersion";

	
	int checkVersion = 0;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front);
		
		// Check GPS
        /* if(!LocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER ))
        {
            Intent myIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS );
            startActivity(myIntent);
        }*/
		///
		
		myDbHelper = new DatabaseHandler(this);
		myDbHelperDoc = new DatabaseHandlerDoctor(this);
		myDbHelperTest = new DatabaseHandlerTestCode(this);
		myDbHelperUser = new DatabaseHandlerUser(this);
		myDbHelperPID = new DatabaseHandlerPID(this);
		myDbHelper.initialise();
		myDbHelperPID.initialise();
		myDbHelperUser.initialise();
		

		
		btnLogin = (Button) findViewById(R.id.btnLogin);
		edPass = (EditText) findViewById(R.id.loginPassword);
		edUser = (EditText) findViewById(R.id.loginUser);
		checkConn = (TextView) findViewById(R.id.login_error);
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				//////////////////////////////////////////////
				
				if (isOnline() == false)
				{
					Toast.makeText(Front_Activity.this, "Không tìm thấy kết nối mạng. Xin thử lại" , Toast.LENGTH_SHORT).show();
					return;
				}
				
				new CheckTask().execute();
				
				//////////////////////////////////////////////
				
				User login = myDbHelperUser.login(String.valueOf(edUser.getText()),String.valueOf(edPass.getText().toString()));
				
				if (edUser.getText().toString().trim().equals("") == true || login.getId().trim().equals(String.valueOf(edUser.getText())) == false)
				{
					Toast.makeText(Front_Activity.this, "Sai thông tin đăng nhập" , Toast.LENGTH_SHORT).show();
					
					/*btnPrepare.setEnabled(false);
					btnInput.setEnabled(false);
					btnSynced.setEnabled(false);
					btnUpdate.setEnabled(false);*/
					edPass.setText("");
					
				}
				else
				{
					
					edPass.setText("");
					edRealName = login.getName();
					edUserID = edUser.getText().toString().trim();
					edID = login.getRole();
					Toast.makeText(Front_Activity.this, "Đăng nhập thành công" , Toast.LENGTH_SHORT).show();
					
					MainMenuAdapter adapter = new MainMenuAdapter(Front_Activity.this, web, imageId);
				    grid=(GridView)findViewById(R.id.grid);
				        grid.setAdapter(adapter);
				        
				        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				                @Override
				                public void onItemClick(AdapterView<?> parent, View view,
				                                        int position, long id) {
				                	
				                	if(edRealName.equals(""))
				                	{
				                		Toast.makeText(Front_Activity.this, "Chưa đăng nhập vào hệ thống!", Toast.LENGTH_SHORT).show();
				                		return;
				                	}
				                	
				                	switch (position) {
									case 0:
										clearFragResult();
										Intent intent = new Intent(thisContext, MainActivity.class);
									    startActivity(intent);
										break;
									case 1:
										Intent intent2 = new Intent(thisContext, SyncActivity.class);
									    startActivity(intent2);
										break;
									case 2:
										
										if (checkVersion == 0)
										{
											Toast.makeText(Front_Activity.this, "Phiên bản đã lỗi thời, xin cập nhật phiên bản mới!", Toast.LENGTH_SHORT).show();
					                		return;
										}
										
										Intent intent4 = new Intent(thisContext, DumbInfo.class);
									    startActivity(intent4);						
										break;
									case 3:
										
										DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
										    @Override
										    public void onClick(DialogInterface dialog, int which) {
										        switch (which){
										        case DialogInterface.BUTTON_POSITIVE:
										            
										        	myDbHelper.deleteALLDB();
										        	startDownload();
										        	
										            break;

										        case DialogInterface.BUTTON_NEGATIVE:
										            //No button clicked
										            break;
										        }
										    }
										};

										AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
										builder.setMessage("Cập nhật phiên bản mới sẽ xóa toàn bộ dữ liệu hiện thời của bạn. Đồng ý ?").setPositiveButton("Yes", dialogClickListener)
										    .setNegativeButton("No", dialogClickListener).show();
										
										
										break;
									case 4:
										Intent intent5 = new Intent(thisContext, ReceiptMain.class);
									    startActivity(intent5);	
										break;	
										
									case 5:
										
										Intent intent6 = new Intent(thisContext, GetspleActivity.class);
									    startActivity(intent6);	
										
										//new SomeTask().execute();
										
										break;	
										
									case 6:
										Intent intent7 = new Intent(thisContext, Hotro.class);
									    startActivity(intent7);	
										break;

									default:
										break;
									}
				                	
				                   // Toast.makeText(Front_Activity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
				                }
				            });
				}
				
			}
		});
		
		
		////
		
		thisContext = this;
		
		
	}
	
	private class CheckTask extends AsyncTask<Patient, String, String> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(Front_Activity.this);

	    @Override
	    protected void onPreExecute()
	    {
	        Dialog.setMessage("Kiểm tra phiên bản, xin chờ ...! ");
	        Dialog.show();
	        Dialog.setCancelable(false);
	        Dialog.setCanceledOnTouchOutside(false);
	    }

	    @Override
	    protected String doInBackground(Patient... params) 
	    {
	    	String response = "";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_V);   
			
			
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
	              androidHttpTransport.call(SOAP_ACTION_V, envelope);
	             
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
	    	if (result.equals(Front_Activity.this.getResources().getString(R.string.version)))
	    	{
	    		checkVersion = 1;
	    	}
	    	else
	    	{
	    		checkVersion = 0;
	    	}
	    	
	        // after completed finished the progressbar
	        Dialog.dismiss();
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.front_, menu);
		return true;
	}
	
	public void getGPS()
	{
		
	     // Get the location manager
	     LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	     Criteria criteria = new Criteria();
	     String bestProvider = locationManager.getBestProvider(criteria, false);
	     Location location = locationManager.getLastKnownLocation(bestProvider);
	     Double lat,lon;
	     try {
	       lat = location.getLatitude ();
	       lon = location.getLongitude ();
	       Toast.makeText(Front_Activity.this, "LAT: " + lat + " LON: " + lon, Toast.LENGTH_SHORT).show();
	     }
	     catch (NullPointerException e){
	         e.printStackTrace();
	         Toast.makeText(Front_Activity.this, "" + e.getStackTrace(), Toast.LENGTH_SHORT).show();
	     }
	    
	}
	
	private void showGPSDisabledAlertToUser(){
		
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS đang bị tắt. Bật ?")
        .setCancelable(false)
        .setPositiveButton("Bật GPS",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        
        alertDialogBuilder.setNegativeButton("Hủy",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
                
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
	
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		checkConn.setText("[KẾT NỐI DỮ LIỆU: " + (isMyServiceRunning() == true ? "MỞ]" : "TẮT]"));
		
		if (isMyServiceRunning() == false)
		{
			Intent ServiceIntent = new Intent(this, TestSecService.class);
	        this.startService(ServiceIntent);
	        Log.d("Đã khởi động lại service", "GO SERVICE GO");
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        
        case R.id.action_delete:
            DatabaseHandler x = new DatabaseHandler(this);
            x.deleteALLDB();
            return true;
            
        case R.id.action_test:
           
        	
        	
        	/*lertDialog dialog = new AlertDialog(this,
                    android.R.style.Theme_Translucent_NoTitleBar);

  
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);

            window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            dialog.setTitle(null);
            dialog.setContentView(R.layout.selectpic_dialog);
            dialog.setCancelable(true);

            dialog.show();*/
            
        	
            return true;
       
/*        case R.id.action_update:
        	
        	//String maCanBo = myDbHelperPID.getMaxTime();
			
			AsyncCallWS xx = new AsyncCallWS();
			xx.execute("");
        	
        	return true;
        	
        case R.id.action_update_testcode:
        	synTest();
        	return true;
        	
    	case R.id.action_update_doctor:
        	syncDoc();
        	return true;
        	
    	case R.id.action_update_canbo:
    		syncCanbo();
        	return true;*/
            
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	public boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("medlatec.listview.sync.TestSecService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
	
	private void startDownload() {
		
		 boolean delete = new File(Environment.getExternalStorageDirectory()  + "/xx.apk").delete();
		 Log.d("DELETE DELETE", "" + delete);
		
		//// Update
        String url = "http://noibo.medlatec.vn/Upload/Document/Medlatec1649462014.apk";
        new DownloadFileAsync().execute(url);
    }

	 @Override
	protected Dialog onCreateDialog(int id) {
	        switch (id) {
			case DIALOG_DOWNLOAD_PROGRESS:
				mProgressDialog = new ProgressDialog(this);
				mProgressDialog.setMessage("Loading..");
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
				
				return mProgressDialog;
			default:
				return null;
	        }
	    }
	 
	class DownloadFileAsync extends AsyncTask<String, String, String> {
		   
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showDialog(DIALOG_DOWNLOAD_PROGRESS);
			}

			@Override
			protected String doInBackground(String... aurl) {
				int count;

			try {

			URL url = new URL(aurl[0]);
			URLConnection conexion = url.openConnection();
			conexion.connect();

			int lenghtOfFile = conexion.getContentLength();
			Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream("/sdcard/xx.apk", true);
			
			byte data[] = new byte[1024];

			long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress(""+(int)((total*100)/lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
				
				//
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
			    intent.setDataAndType(Uri.fromFile(new File
			            (Environment.getExternalStorageDirectory()  + "/xx.apk")), "application/vnd.android.package-archive");
			    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(intent);
			    
			    
				
				/*Intent intent = new Intent(Intent.ACTION_VIEW);
	            intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Medlatec1649462014.apk")), "application/vnd.android.package-archive");
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
	            context.startActivity(intent);*/
				
			} catch (Exception e) {}
			return null;

			}
			protected void onProgressUpdate(String... progress) {
				 Log.d("ANDRO_ASYNC",progress[0]);
				 mProgressDialog.setProgress(Integer.parseInt(progress[0]));
			}
			
			

			@Override
			protected void onPostExecute(String unused) 
			{
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			}
		}

}
