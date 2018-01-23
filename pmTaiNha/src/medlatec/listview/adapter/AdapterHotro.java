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


public class AdapterHotro extends ArrayAdapter<Patient> 
{
	private Context appContext = null;
	private ArrayList<Patient> items = null;
	DatabaseHandler myDbHelper = null;

public AdapterHotro(Context context, int textViewResourceId, ArrayList<Patient> items)
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
v = vi.inflate(R.layout.itemhotro, null);
}
Patient o = items.get(position);
if (o != null) {
	
TextView name = (TextView) v.findViewById(R.id.lst_item_Name);
TextView mobile = (TextView) v.findViewById(R.id.lst_item_Mobile);
TextView phone = (TextView) v.findViewById(R.id.lst_item_phone);
TextView id = (TextView) v.findViewById(R.id.lst_item_id);
TextView nsgt = (TextView) v.findViewById(R.id.lst_NS_GT);



	if (name != null) 
	{
		if (o.getReason() != -1)
		{
			name.setText("[ĐÃ TRẢ] SID: " + o.getSid());
		}
		else
		{
			name.setText("[ĐÃ TRẢ] SID: " + o.getSid());
		}
	}
	if(mobile != null)
	{
		mobile.setText("HỌ TÊN: " + o.getPatientname());
	}
	
	if(phone != null)
	{
		phone.setText("SỐ ĐIỆN THOẠI: " + o.getPhone());
	}
	
	if (id != null)
	{
		id.setText("ĐỊA CHỈ: " + o.getAddress());
	}
	
	if (nsgt != null)
	{
		 /*  <item>Chưa trả kết quả</item>
	        <item>Đã trả kết quả</item>
	        <item>Gửi người thân</item>
	        <item>Nhét cửa</item>
	        <item>Lý do khác</item>*/
		
		switch (o.getReason()) {
		case 0:
			nsgt.setText("CHƯA TRẢ KẾT QUẢ. " + o.getsReason().toUpperCase());
			break;
		case 1:
			nsgt.setText("ĐÃ TRẢ KẾT QUẢ. " + o.getsReason().toUpperCase());
			break;
		case 2:
			nsgt.setText("GỬI NGƯỜI THÂN. " + o.getsReason().toUpperCase());
			break;
		case 3:
			nsgt.setText("NHÉT CỬA. " + o.getsReason().toUpperCase());
			break;

		default:
			nsgt.setText("LÝ DO KHÁC. " + o.getsReason().toUpperCase());
			break;
		}
		
		
	}
	
}
return v;
}
}