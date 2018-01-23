package medlatec.listview;

import java.util.ArrayList;
import java.util.List;

import medlatec.listview.adapter.AdapterDoctor;
import medlatec.listview.adapter.MyCustomBaseAdapter;
import medlatec.listview.adapter.AdapterDumb;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ReceiptTK extends ListActivity {

	ArrayList<Patient> newList = null;
	private Button btnSave = null;
	public static EditText txt2Name = null;
	private AdapterDoctor newAdpt = null;
	DatabaseHandler myDbHelper = null;
	AdapterDoctor cAdapter;
	private ListView listView1 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt_tk);
		
		myDbHelper = new DatabaseHandler(this);
	 	myDbHelper.initialise();
		
		newList = new ArrayList<Patient>();
		newAdpt = new AdapterDoctor(this, R.layout.itempatient, newList);
		setListAdapter(this.newAdpt);
		
		txt2Name = (EditText)findViewById(R.id.txtCV);
		btnSave = (Button)findViewById(R.id.btnSave);
		
		btnSave.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				String key = txt2Name.getText().toString();
				
				List<Patient> contacts = myDbHelper.getAllPatient(key, 1);
				
				newAdpt.clear();
				
				for (int i = 0; i < contacts.size(); i++)
				{
					newAdpt.add(contacts.get(i));
				}
				
				newAdpt.notifyDataSetChanged();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.receipt_tk, menu);
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
