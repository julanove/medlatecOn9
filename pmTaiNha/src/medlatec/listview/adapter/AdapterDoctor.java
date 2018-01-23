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


public class AdapterDoctor extends ArrayAdapter<Patient> 
{
	private Context appContext = null;
	private ArrayList<Patient> items = null;
	DatabaseHandler myDbHelper = null;

public AdapterDoctor(Context context, int textViewResourceId, ArrayList<Patient> items)
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
v = vi.inflate(R.layout.itempatient, null);
}
Patient o = items.get(position);
if (o != null) {
	
TextView name = (TextView) v.findViewById(R.id.lst_NAME);
TextView ckDL = (TextView) v.findViewById(R.id.lst_CK_DL);
TextView sid = (TextView) v.findViewById(R.id.lst_SID);
TextView total = (TextView) v.findViewById(R.id.lst_TOTAL);
TextView sexage = (TextView) v.findViewById(R.id.lst_NS_GT);

Button btnDelete = (Button)v.findViewById(R.id.lst_item_Delete);
Button btnConfirm = (Button)v.findViewById(R.id.lst_item_Read);
btnConfirm.setTag(position);
btnConfirm.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		
	}
});


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
	
	

}
});

	

	if(sid != null)
	{
		sid.setText("SID: " + o.getSid());
		
		o.setSummoney(myDbHelper.getSumMoney(o.getSid()));
	}
	if (name != null) 
	{
		name.setText("TÊN: " + o.getPatientname().toUpperCase());
	}
	if(sexage != null)
	{
		sexage.setText("GIỚI TÍNH: " + o.getSex() + " - TUỔI: " + o.getAge());
	}
	
	if (ckDL != null)
	{
		ckDL.setText("CHIẾT KHẤU: " + o.getSumpertage()  + " - ĐI LẠI: " + o.getTiendilai() + " - THẺ: " + o.getGG());
	}
	if (total != null)
	{
		total.setText("TỔNG DV: " + myDbHelper.getSumMoney(o.getSid()) + " - " + "TỔNG TIỀN: " + o.finalMoney());
	}
	
	
	
}
return v;
}
}