package medlatec.listview.adapter;

import java.util.ArrayList;

import medlatec.listview.R;
import medlatec.listview.R.id;
import medlatec.listview.R.layout;
import medlatec.listview.connect.DatabaseHandler;
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


public class AdapterDumb extends ArrayAdapter<PersonalInfo> 
{
	private Context appContext = null;
	private ArrayList<PersonalInfo> items = null;
	DatabaseHandler myDbHelper = null;

public AdapterDumb(Context context, int textViewResourceId, ArrayList<PersonalInfo> items)
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
PersonalInfo o = items.get(position);
if (o != null) {
	
TextView name = (TextView) v.findViewById(R.id.lst_item_Name);
TextView mobile = (TextView) v.findViewById(R.id.lst_item_Mobile);
TextView phone = (TextView) v.findViewById(R.id.lst_item_phone);
TextView id = (TextView) v.findViewById(R.id.lst_item_id);
TextView nsgt = (TextView) v.findViewById(R.id.lst_NS_GT);

Button btnDelete = (Button)v.findViewById(R.id.lst_item_Delete);
Button btnConfirm = (Button)v.findViewById(R.id.lst_item_Read);
btnConfirm.setTag(position);
btnConfirm.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		String pos = v.getTag().toString();
		int _position = Integer.parseInt(pos);
		////
		myDbHelper.updateDumb(items.get(_position).getMaLH(), "1");
		////
		String newName = items.get(_position).GetName() + " [XÁC NHẬN]";
		items.get(_position).SetName(newName);
		
		notifyDataSetChanged();
		
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

	if (name != null) 
	{
		if(o.getTinhtrang().equals("1"))
		{
			name.setText(o.GetName() + " [XÁC NHẬN]");
		}
		else
		{
			name.setText(o.GetName());
		}
	}
	if(mobile != null)
	{
		mobile.setText(o.GetMobile());
	}
	if(phone != null)
	{
		phone.setText(o.getPhone());
	}
	if (id != null)
	{
		id.setText(o.getMaLH() + " [" + o.getGiohen1() + "-" + o.getGiohen2() + "]");
	}
	if (nsgt != null)
	{
		String gioitinh = "Không XĐ";
		String namsinh = "0";
		
		if (o.getGioitinh().equals("0"))
				gioitinh = "Nữ";
		else if (o.getGioitinh().equals("1"))
				gioitinh = "Nam";
		
		if (o.getNamsinh().equals("") == false)
			namsinh = o.getNamsinh();
		
		nsgt.setText(gioitinh + " - " + namsinh);
	}
	
	
	
}
return v;
}
}