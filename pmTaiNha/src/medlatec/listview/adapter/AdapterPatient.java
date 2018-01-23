package medlatec.listview.adapter;

import java.util.ArrayList;

import medlatec.listview.R;
import medlatec.listview.R.id;
import medlatec.listview.R.layout;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class AdapterPatient extends ArrayAdapter<Patient> 
{
	private Context appContext = null;
	private ArrayList<Patient> items = null;
	DatabaseHandler myDbHelper = null;

public AdapterPatient(Context context, int textViewResourceId, ArrayList<Patient> items)
{
	super(context,textViewResourceId,items);
	this.appContext = context;
	this.items=items;
	
	///////////////////////////////////////////////
	
	myDbHelper = new DatabaseHandler(context);
 	myDbHelper.initialise();
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
View v = convertView;
if (v == null) {
LayoutInflater vi = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
v = vi.inflate(R.layout.itemdumb, null);
}
Patient o = items.get(position);
if (o != null) {
	
TextView name = (TextView) v.findViewById(R.id.lst_item_Name);
TextView mobile = (TextView) v.findViewById(R.id.lst_item_Mobile);
TextView phone = (TextView) v.findViewById(R.id.lst_item_phone);
Button btnDelete = (Button)v.findViewById(R.id.lst_item_Delete);


btnDelete.setTag(position);
btnDelete.setOnClickListener(new OnClickListener() {

@Override
public void onClick(View view) {
	
	//Section 3. remove rows in list
	String pos = view.getTag().toString();
	int _position = Integer.parseInt(pos);
	
	////
	
	myDbHelper.deleteOneDumb(items.get(_position).getPhone());
	
	////
	
	items.remove(_position);
	notifyDataSetChanged();
	//just replace the 3
	//add a command to remove record from sqlite and
	//call notifyDataSetChanged(); thats all
	
	//Log.d("REMOVE", items.get(_position).GetName());
	
	

}
});

	if (name != null) 
	{
		name.setText(o.getPatientname());
	}
	if(mobile != null)
	{
		mobile.setText(o.getPhone());
	}
	if(phone != null)
	{
		phone.setText(o.getAddress());
	}
}
return v;
}
}