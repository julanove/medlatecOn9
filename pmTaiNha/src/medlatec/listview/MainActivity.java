package medlatec.listview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import medlatec.listview.adapter.MainMenuAdapter;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.connect.DatabaseHandlerDoctor;
import medlatec.listview.connect.DatabaseHandlerTestCode;
import medlatec.listview.object.TestCode;
import medlatec.listview.testcode.SearchActivity;
import medlatec.listview.testcode.TestCode_Cay;
import medlatec.listview.testcode.TestCode_HoaSinh;
import medlatec.listview.testcode.TestCode_HuyetHoc;
import medlatec.listview.testcode.TestCode_Khac;
import medlatec.listview.testcode.TestCode_MienDich;
import medlatec.listview.testcode.TestCode_NoiTiet;
import medlatec.listview.testcode.TestCode_Selected;
import medlatec.listview.testcode.TestCode_TeBao;
import medlatec.listview.testcode.TestCode_ViKhuan;
import android.R.integer;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends TabActivity implements OnTabChangeListener {

	public static TextView txtSum;
	
	/////////////

    private static final String LIST1_TAB_TAG = "Thông tin KH";
    
    //private static final String LIST2_TAB_TAG = "Hóa sinh";
    private static final String LIST3_TAB_TAG = "Huyết học";
    private static final String LIST4_TAB_TAG = "Miễn dịch";
    private static final String LIST5_HOA_SINH = "Hóa sinh";
    private static final String LIST6_HUYET_HOC = "Huyết học";
    private static final String LIST7_MIEN_DICH = "Miễn dịch";
    private static final String LIST8_TE_BAO = "Tế bào học, khối u";
    private static final String LIST9_VIKHUAN = "Vi khuẩn, Ký sinh trùng";
    private static final String LIST10_NOITIET = "Nội tiết - Hormon";
    private static final String LIST11_SINHTHIET = "Tìm kiếm";
    private static final String LIST13_CAY = "Cấy - ST";
    private static final String LIST12_KHAC = "Khác";
    private static final String LIST_SELECTED_TAB_TAG = "DV đã chọn";
    private static final String LIST_PRINT_TAB_TAG = "In hóa đơn";
    private static final String LIST_NI_GK = "Gói khám";
    
    
    ContentResolver mResolver;
    DatabaseHandler myDbHelper;
    DatabaseHandlerTestCode myDbHelperTest;
    public static TabHost tabHost;
	private Intent profileIntent;
    
    ////////
	
	public static Bundle packageFromCaller;
	
	

    @SuppressWarnings("deprecation")
	@Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_3);
        
        setTitle("" + this.getResources().getString(R.string.app_name));
        
        txtSum = (TextView) findViewById(R.id.txtSumx);
        
        ///////////////////////
        
        
        
	    //////////////////////
	        
        myDbHelper = new DatabaseHandler(this);
     	myDbHelper.initialise();
        
     	myDbHelperTest = new DatabaseHandlerTestCode(this);
     	myDbHelperTest.initialise();
	 	//////////////////////

        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);
        
        //////////////////////
        
        String tits = "[CÁN BỘ: " + Front_Activity.edRealName.toUpperCase() + "] - TỔNG TIỀN DỊCH VỤ: 0";
        tits += "\nTIN NHẮN CHƯA ĐỌC: " + myDbHelper.getNonReadDumb();
        txtSum.setText(tits);
    	///////////////////////////////////////////////////
        
        
        
        
        ///////////////////////////////////////////////////


        // add views to tab host

        /*tabHost.addTab(tabHost.newTabSpec(LIST1_TAB_TAG).setIndicator(LIST1_TAB_TAG).setContent(new TabContentFactory() {

        public View createTabContent(String arg0) {

            return getLayoutInflater().inflate(R.layout.patientx_activity, null);

        }

        }));*/
        
        TabSpec profileSpec = tabHost.newTabSpec(LIST1_TAB_TAG);
        profileSpec.setIndicator(LIST1_TAB_TAG);
        profileIntent = new Intent(this, PatientInformation.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);

        ///////////////////////////////////////////////////////////////
        
        profileSpec = tabHost.newTabSpec(LIST_NI_GK);
        profileSpec.setIndicator(LIST_NI_GK);
        profileIntent = new Intent(this, GoiKham.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        ///////////////////////////////////////////////////////////////
        
        profileSpec = tabHost.newTabSpec(LIST5_HOA_SINH);
        profileSpec.setIndicator(LIST5_HOA_SINH);
        profileIntent = new Intent(this, TestCode_HoaSinh.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        profileSpec = tabHost.newTabSpec(LIST6_HUYET_HOC);
        profileSpec.setIndicator(LIST6_HUYET_HOC);
        profileIntent = new Intent(this, TestCode_HuyetHoc.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        profileSpec = tabHost.newTabSpec(LIST7_MIEN_DICH);
        profileSpec.setIndicator(LIST7_MIEN_DICH);
        profileIntent = new Intent(this, TestCode_MienDich.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        
    	/////////////////////////////////////////////////////////////
        
        profileSpec = tabHost.newTabSpec(LIST8_TE_BAO);
        profileSpec.setIndicator(LIST8_TE_BAO);
        profileIntent = new Intent(this, TestCode_TeBao.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        profileSpec = tabHost.newTabSpec(LIST9_VIKHUAN);
        profileSpec.setIndicator(LIST9_VIKHUAN);
        profileIntent = new Intent(this, TestCode_ViKhuan.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        profileSpec = tabHost.newTabSpec(LIST10_NOITIET);
        profileSpec.setIndicator(LIST10_NOITIET);
        profileIntent = new Intent(this, TestCode_NoiTiet.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        profileSpec = tabHost.newTabSpec(LIST13_CAY);
        profileSpec.setIndicator(LIST13_CAY);
        profileIntent = new Intent(this, TestCode_Cay.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        profileSpec = tabHost.newTabSpec(LIST12_KHAC);
        profileSpec.setIndicator(LIST12_KHAC);
        profileIntent = new Intent(this, TestCode_Khac.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        profileSpec = tabHost.newTabSpec(LIST11_SINHTHIET);
        profileSpec.setIndicator(LIST11_SINHTHIET);
        profileIntent = new Intent(this, SearchActivity.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        /////////////////////////////////////////////////////////////
        
        
        profileSpec = tabHost.newTabSpec(LIST_SELECTED_TAB_TAG);
        profileSpec.setIndicator(LIST_SELECTED_TAB_TAG);
        profileIntent = new Intent(this, TestCode_Selected.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
        profileSpec = tabHost.newTabSpec(LIST_PRINT_TAB_TAG);
        profileSpec.setIndicator(LIST_PRINT_TAB_TAG);
        profileIntent = new Intent(this, PatientPrinter.class);
        profileSpec.setContent(profileIntent);
        tabHost.addTab(profileSpec);
        
   
        
        
        /*tabHost.addTab(tabHost.newTabSpec(LIST5_TAB_TAG).setIndicator(LIST5_TAB_TAG).setContent(new TabContentFactory() {

            public View createTabContent(String arg0) {

                return getLayoutInflater().inflate(R.layout.printer_layout, null);

            }

            }));*/
        
        //myDbHelper.deleteAllForSync();
        
       /* Intent ServiceIntent = new Intent(this, TestSecService.class);
        this.startService(ServiceIntent);*/
        
       
        
        Intent callerIntent = getIntent();
        packageFromCaller = callerIntent.getBundleExtra("MyPackage");
        
        if (packageFromCaller != null)
        {
	        String name = packageFromCaller.getString("name");
	        String mobile = packageFromCaller.getString("mobile");
	        String phone = packageFromCaller.getString("phone");
        }

    }

    
    
 
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }*/
    
    
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        
        case R.id.menu_clear:
            Toast.makeText(MainActivity.this, "Clear", Toast.LENGTH_SHORT).show();
            return true;
        
        case R.id.menu_save:
            // Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            saveInfo();
            return true;
 
        case R.id.menu_back:
            Toast.makeText(MainActivity.this, "Back", Toast.LENGTH_SHORT).show();
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    } */   

    public void saveInfo()
    {
    	
    }
    
    /**

     * Implement logic here when a tab is selected

     */

    
    
    public void onTabChanged(String tabName) {

    	
        /*if (tabName.equals(LIST_PRINT_TAB_TAG))
        {
        	if (myDbHelper.checkPatientForPrint(sid) == 0)
        	{
        		Log.d("COUNT","COUNT PATIENT");
        		Toast.makeText(MainActivity.this,"Chưa thể in hóa đơn khi chưa lưu thông tin khách hàng", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        	}
        }*/
        
        if(tabName.equals(LIST1_TAB_TAG) == false) 
        {
        	String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
            String namsinh = String.valueOf(PatientInformation.etBOD.getText());
            String sex = String.valueOf(PatientInformation.etSex.getSelectedItem().toString());
            String chietKhau = String.valueOf(PatientInformation.etChietKhau.toString());
           
            		
           
        	if (sid.length() != 14)
        	{
        		Toast.makeText(MainActivity.this,"Chưa nhập SID hoặc SID ko đúng định dạng", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        		return;
        	}
        	
        	if (namsinh.length() < 1)
        	{
        		Toast.makeText(MainActivity.this,"Phải nhập năm sinh", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        		return;
        	}
        	
        	if (namsinh.trim().equals("19"))
        	{
        		Toast.makeText(MainActivity.this,"Phải nhập năm sinh", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        		return;
        	}
        	
        	if (sex.equals("- Chọn giới tính -"))
        	{
        		Toast.makeText(MainActivity.this,"Chưa chọn giới tính", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        		return;
        	}
        	
        	if(PatientInformation.etSID.getText().toString().substring(2, 4).equals(Front_Activity.edID.trim()) == false)
        	{
        		Toast.makeText(MainActivity.this, "Không thể cập nhật - User cán bộ nhập liệu ko phù hợp với SID", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        		return;
        	}
        	
        	if(PatientInformation.etLyDo.getText().toString().trim().equals(""))
        	{
        		Toast.makeText(MainActivity.this, "Chưa nhập lý do khám bệnh ", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        		return;
        	}
        	
        	if(PatientInformation.txtH.getText().toString().trim().equals(""))
        	{
        		Toast.makeText(MainActivity.this, "Chưa nhập quận", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        		return;
        	}
        	
        	/*if (PatientInformation.yeucauXN.equals("") == false)
        	{
        		String[] yeucau = PatientInformation.yeucauXN.split("~");
        		
        		for (int i = 0; i < yeucau.length; i++)
        		{
	        		TestCode item = myDbHelperTest.selectOneTestCode(yeucau[i]);
	    			item.setSid(sid);
					myDbHelper.addResult(item);
        		}
        		
        		PatientInformation.yeucauXN = "";
        	}*/
        	
        	if (chietKhau.equals(""))
        	{
        		Toast.makeText(MainActivity.this, "Chiết khấu không phù hợp", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
        		return;
        	}
        	else
        	{
        		/*if (Integer.parseInt(chietKhau) > 100)
        		{
        			Toast.makeText(MainActivity.this, "Chiết khấu không phù hợp", Toast.LENGTH_SHORT).show();
            		tabHost.setCurrentTab(0);
            		return;
        		}*/
        	}
        		
        	
        	
    		/*if (myDbHelper.checkExistsUser(canbo) == 0)
    		{
        		Toast.makeText(MainActivity.this,"User cán bộ tại nhà không đúng. VD: xuanthanh, ngocque ...", Toast.LENGTH_SHORT).show();
        		tabHost.setCurrentTab(0);
    		}*/
        	
        }
    }
    
    /*@SuppressWarnings("deprecation")
	protected void onDestroy() 
    {
        super.onDestroy();
        if (myDbHelper != null) 
        {
        	myDbHelper.close();
        }
    }
    */
    
}

