package medlatec.listview.testcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import medlatec.listview.Front_Activity;
import medlatec.listview.MainActivity;
import medlatec.listview.PatientInformation;
import medlatec.listview.R;
import medlatec.listview.R.id;
import medlatec.listview.R.layout;
import medlatec.listview.adapter.MyCustomBaseAdapter;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.TestCode;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TestCode_Selected extends Activity {

	private ListView listView1 = null;
	
	DatabaseHandler myDbHelper = null;
	
	MyCustomBaseAdapter cAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_code__selected);
		
		//Log.d("ON CREATE", "CREATED");
		
		
		myDbHelper = new DatabaseHandler(this);
		 
        /*try
    	{
        	myDbHelper.createDataBase();
	 	} catch (IOException ioe) 
	 	{throw new Error("Unable to create database");}*/
		myDbHelper.initialise();
 	
 	///////////////////////////////////////////////////

        listView1 = (ListView) findViewById(R.id.SelectedList);

        fillSelected();
        
	}
	
	
	public void fillSelected()
	{
		ArrayList<TestCode> list1Strings = new ArrayList<TestCode>();
        
		String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
		
        List<TestCode> contacts = myDbHelper.getAllContacts(sid, 2);
        
        TestCode ptObject;
        TestCode tcObject;
        
        Log.d("SIZE", "" + contacts.size());
        
        for (int i = 0; i < contacts.size(); i++)
        {
        	ptObject = (TestCode)contacts.get(i);
        	tcObject = new TestCode();
        	
        		tcObject.setTestName(ptObject.getTestName());
        		tcObject.setTestCode(ptObject.getTestCode());
        		tcObject.setPrice(ptObject.getPrice());
        		tcObject.setChecked(true);
        		//Log.d("Price", tcObject.getPrice());
        		list1Strings.add(tcObject);
        	
        }
        
        /////////////
        cAdapter = new MyCustomBaseAdapter(this, list1Strings);
        listView1.setAdapter(cAdapter);
        
        
        
        listView1.setOnItemClickListener(new OnItemClickListener() {
        	@Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

            	CheckBox cBox = (CheckBox) view.findViewById(R.id.item2);
                cBox.toggle();
                
                String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
                
                TestCode item = (TestCode) listView1.getAdapter().getItem(position);
                
                if (cBox.isChecked() == false)
                {
                	Log.d("a","b");
                	((MyCustomBaseAdapter)listView1.getAdapter()).remove(position);
                	((MyCustomBaseAdapter)listView1.getAdapter()).notifyDataSetChanged();
                	myDbHelper.deleteOneTestCode(sid, item.getTestCode());
                	
                	String tits = "[CÁN BỘ: " + Front_Activity.edRealName.toUpperCase() + "] - TỔNG TIỀN DỊCH VỤ: " + myDbHelper.getSumResult(sid);
                    tits += "\nTIN NHẮN CHƯA ĐỌC: " + myDbHelper.getNonReadDumb();
                    MainActivity.txtSum.setText(tits);
                }
            }

        });
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	//	listView1.getAdapter()
	//	this.onCreate(getIntent().getExtras());
		
		fillSelected();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.layout.menu_selected, menu);
				return true;
	}
	
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        
        case R.id.menu_remove:
            Toast.makeText(TestCode_Selected.this, "Remove", Toast.LENGTH_SHORT).show();
            remove();
            return true;
 
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	public void remove()
	{
		
	}
	
	/*@SuppressWarnings("deprecation")
	protected void onDestroy() 
    {
        super.onDestroy();
        if (myDbHelper != null) 
        {
        	myDbHelper.close();
        }
    }*/
	
	/*@Override
	public void onResume()
	{
		super.onResume();
		//listView1.invalidateViews();
		
		try
    	{
			cAdapter.notifyDataSetChanged();
	 	} 
		catch (Exception ex)
		{
			Log.d("EXCE","" + ex.getMessage());
		}
	}*/
	

}
