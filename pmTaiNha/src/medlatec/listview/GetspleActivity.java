package medlatec.listview;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.adapter.AdapterDumb;
import medlatec.listview.adapter.AdapterNhanMau;
import medlatec.listview.auth.MarshalDouble;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.NhanMau;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class GetspleActivity extends ListActivity {

	public static Spinner etSL;
	public static Spinner etLoai;
	private Button btnSave;
	private ListView lvAll;
    EditText txtGhiChu;
	
	ArrayList<NhanMau> newList = null;
	private AdapterNhanMau newAdpt = null;
	DatabaseHandler myDbHelper = null;
	private String sid;
	
	private static final String NAMESPACE = "http://syn.medlatec.vn/";
	private static final String URL = "http://syn.medlatec.vn/InsertUpdate_Patient.asmx?WSDL";	
	private static final String SOAP_ACTION = "http://syn.medlatec.vn/BanGiaoMau";
	private static final String METHOD_NAME = "BanGiaoMau";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getsple);
		
		
		
		Intent intent = getIntent();
		sid = intent.getStringExtra("sid");
		String name = intent.getStringExtra("name");
		
		myDbHelper = new DatabaseHandler(this);
	 	myDbHelper.initialise();
		
		this.setTitle("MẪU: " + sid + " - " + name);
		
		etSL = (Spinner) findViewById(R.id.etSL);
		etLoai = (Spinner) findViewById(R.id.etLoai);
		lvAll = (ListView) findViewById(R.id.List);
		txtGhiChu = (EditText) findViewById(R.id.etGhiChu);
		
		////////////////////////////////////////////////////////////////////////////////
		
		
		List<String> listDL = new ArrayList<String>();
        listDL.add("1");
        listDL.add("2");
        listDL.add("3");
        listDL.add("4");
        listDL.add("5");
        listDL.add("6");
       
        ArrayAdapter<String> dataAdapterDL = new ArrayAdapter<String>
        (this, android.R.layout.simple_spinner_item,listDL);            
        dataAdapterDL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etSL.setAdapter(dataAdapterDL);
        etSL.setSelection(0);   
       
        List<String> listDL2 = new ArrayList<String>();
        listDL2.add("Heparin"); //0
        listDL2.add("Edta"); //1
        listDL2.add("Citrat"); //2
        listDL2.add("Serum"); //3
        listDL2.add("Chimigly"); //4
        listDL2.add("nước tiểu"); //5
        listDL2.add("Phân"); //6
        listDL2.add("Đờm"); //7
        listDL2.add("Dịch"); //8
        listDL2.add("Lam kính"); //9
        listDL2.add("Máu lắng"); //10
        listDL2.add("Cup.HT"); //11
        listDL2.add("Tinh trùng"); //12
        listDL2.add("GPB"); //13
        listDL2.add("Cấy máu"); //14
        listDL2.add("Ống trắng"); //15
        
        /*0 heparin
        1 edta
        2 citrat
        3 serum
        4 chimigly
        5 nước tiểu
        6 phân
        7 đờm
        8 dịch
        9 lam kính
        10 máu lắng
        11 Cup.HT
        12 Tinh trùng
        13 GPB
        14 Cấy máu
        15 Ống trắng*/
       
        ArrayAdapter<String> dataAdapterDL2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listDL2);            
        dataAdapterDL2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etLoai.setAdapter(dataAdapterDL2);
        etLoai.setSelection(0);   
	       
    	////////////////////////////////////////////////////////////////////////////////
        
        newList = new ArrayList<NhanMau>();
    	newList = (ArrayList<NhanMau>) myDbHelper.getAllNhanMau(sid);
    	Log.d("ALL NHANMAU", "" + newList.size());
    	newAdpt = new AdapterNhanMau(this, R.layout.itemnhanmau, newList);
    	setListAdapter(this.newAdpt);
        
        ////////////////////////////////////////////////////////////////////////////////
        
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new OnClickListener() 
   	    {
    	@Override
   		public void onClick(View arg0) 
   		{
    		NhanMau nmObject = new NhanMau();
    		nmObject.setSid(sid);
    		nmObject.setLoai(etLoai.getSelectedItemPosition());
    		nmObject.setSoluong(etSL.getSelectedItemPosition() + 1);
    		nmObject.setGhichu(0);
    		
    		if (myDbHelper.updateNhanMau(nmObject) == 0)
    		{
    			myDbHelper.addNhanMau(nmObject);
    		}
    		
    		//////////////////////
    		
    		newAdpt.clear();
    		newList = (ArrayList<NhanMau>) myDbHelper.getAllNhanMau(sid);
    		for (int i = 0; i < newList.size(); i++)
    		{
    			newAdpt.add(newList.get(i));
    		}
    		newAdpt.notifyDataSetChanged();
    		
        	
   		}
   		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.getsple, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
        {
        	case R.id.menu_bangiao:
            
        		
        		
        		String user = Front_Activity.edUserID;
        		String info = ""; 
        		// 250315-1103001_1_3_ghichux&250315-1103001_2_1_ghichu2x
        		for (int i = 0; i < newList.size(); i++)
        		{
        			if (i == 0)
        			{
        				info = newList.get(i).getSid() + "_" + newList.get(i).getLoai() + "_" + newList.get(i).getSoluong();
        			}
    				else
    				{
    					info = info + "&" + newList.get(i).getSid() + "_" + newList.get(i).getLoai() + "_" + newList.get(i).getSoluong();
    				}
        		}
        		
        		//Log.d("USER", " " + user);
        		//Log.d("INFO", " " + info);
        		
        		SomeTask x = new SomeTask();
    			x.execute(new String[] {user, info});
        		
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
	}
	
	private class SomeTask extends AsyncTask<String, String, String> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(GetspleActivity.this);
	
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
			request.addProperty("user", params[0]);
			request.addProperty("info", params[1]);
			
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
	    	if (result.startsWith("1"))
	    	  {
	    		 myDbHelper.doneNhanMau(sid);
	    		 Toast.makeText(GetspleActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
	    		 
	    		 //////////////////////
	    		 
	    		 newAdpt.clear();
	     		 newList = (ArrayList<NhanMau>) myDbHelper.getAllNhanMau(sid);
	     		 for (int i = 0; i < newList.size(); i++)
	     		 {
	     			newAdpt.add(newList.get(i));
	     		 }
	     		 newAdpt.notifyDataSetChanged();
	     		 
	     		 
	    	  }
	    	  else if (result.startsWith("9"))
	    	  {
	    		  Toast.makeText(GetspleActivity.this, "Có lỗi xảy ra, xin cập nhật lại", Toast.LENGTH_SHORT).show(); 
	    	  }
	        // after completed finished the progressbar
	        Dialog.dismiss();
	    }
	}
}
