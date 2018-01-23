package medlatec.listview.testcode;

import java.util.ArrayList;
import java.util.List;

import medlatec.listview.Front_Activity;
import medlatec.listview.MainActivity;
import medlatec.listview.PatientInformation;
import medlatec.listview.R;
import medlatec.listview.R.id;
import medlatec.listview.R.layout;
import medlatec.listview.R.menu;
import medlatec.listview.adapter.MyCustomBaseAdapter;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.connect.DatabaseHandlerTestCode;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import medlatec.listview.object.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {

	private Button btnTestCode = null;
	private Button btnProfile = null;
	private EditText txtKey = null;
	
	
	DatabaseHandlerTestCode myDbHelper = null;
	DatabaseHandler myDbHelperMain = null;
	
	
	
	private ListView listView1 = null;
	MyCustomBaseAdapter cAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		myDbHelper = new DatabaseHandlerTestCode(this);
	 	myDbHelper.initialise();
	 	
	 	myDbHelperMain = new DatabaseHandler(this);
	 	myDbHelperMain.initialise();
		
		init();
		
		////////////////////////
		
	
	}
	
	public void init()
	{
		btnTestCode = (Button) findViewById(R.id.btnSave);
		txtKey = (EditText) findViewById(R.id.txtName);
		listView1 = (ListView) findViewById(R.id.List);
		
		btnTestCode.setOnClickListener(btnTestCodeOnClickListener);
		
		
		////////////////////////////
		
		listView1.setOnItemClickListener(new OnItemClickListener() {
        	@Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

            	CheckBox cBox = (CheckBox) view.findViewById(R.id.item2);
                cBox.toggle();
                
                String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
                
                TestCode item = (TestCode) listView1.getAdapter().getItem(position);
                
                if (cBox.isChecked() == true)
                {
                	TestCode itemx = new TestCode();
        			item = myDbHelper.selectOneTestCode(item.getTestCode());
        			//
        			if (PatientInformation.ObjectID.equals("") == false)
        			{
        				item.setPrice("" + (Double.parseDouble(item.getPrice()) * 0.95));
        			}
        			//
        			item.setSid(sid);
            		//
        			myDbHelperMain.addResult(item);
        			
                }
                else
                {
                	myDbHelperMain.deleteOneTestCode(sid, item.getTestCode());
                }
                
                String tits = "[CÁN BỘ: " + Front_Activity.edRealName.toUpperCase() + "] - TỔNG TIỀN DỊCH VỤ: " + myDbHelperMain.getSumResult(sid);
                tits += "\nTIN NHẮN CHƯA ĐỌC: " + myDbHelperMain.getNonReadDumb();
                MainActivity.txtSum.setText(tits);
            }

        });
	}
	
	OnClickListener btnTestCodeOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			String key = txtKey.getText().toString();
			
			ArrayList<TestCode> list1Strings = new ArrayList<TestCode>();
			List<TestCode> contacts = myDbHelper.searchTestCode(key);
	        
	        TestCode ptObject;
	        TestCode tcObject;
	        
	        for (int i = 0; i < contacts.size(); i++)
	        {
	        	ptObject = (TestCode)contacts.get(i);
	        	tcObject = new TestCode();
	        	
	        		tcObject.setTestName(ptObject.getTestName());
	        		tcObject.setTestCode(ptObject.getTestCode());
	        		tcObject.setPrice(ptObject.getPrice());
	        		tcObject.setChecked(false);
	        		list1Strings.add(tcObject);
	        	
	        }
	        
	        /////////////
	        cAdapter = new MyCustomBaseAdapter(v.getContext(), list1Strings);
	        listView1.setAdapter(cAdapter);
		}
	};
	
	OnClickListener btnProfileOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			
			
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	
	
}

