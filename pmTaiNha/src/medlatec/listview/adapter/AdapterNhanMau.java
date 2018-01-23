package medlatec.listview.adapter;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import medlatec.listview.DumbInfo;
import medlatec.listview.Front_Activity;
import medlatec.listview.GetspleActivity;
import medlatec.listview.MainActivity;
import medlatec.listview.PatientInformation;
import medlatec.listview.R;
import medlatec.listview.SyncActivity;
import medlatec.listview.R.id;
import medlatec.listview.R.layout;
import medlatec.listview.auth.MarshalDouble;
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.object.NhanMau;
import medlatec.listview.object.Patient;
import medlatec.listview.object.PersonalInfo;
import medlatec.listview.object.TestCode;
import medlatec.listview.testcode.TestCode_HoaSinh;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class AdapterNhanMau extends ArrayAdapter<NhanMau> 
{
	private Context appContext = null;
	private ArrayList<NhanMau> items = null;
	DatabaseHandler myDbHelper = null;
	

public AdapterNhanMau(Context context, int textViewResourceId, ArrayList<NhanMau> items)
{
	super(context,textViewResourceId,items);
	this.appContext = context;
	this.items=items;
	
	///////////////////////////////////////////////
	
	myDbHelper = new DatabaseHandler(context);
 	myDbHelper.initialise();
}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;
		if (v == null) 
		{
			LayoutInflater vi = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.itemnhanmau, null);
		}
		
		NhanMau o = items.get(position);
		
		if (o != null) 
		{
			TextView name = (TextView) v.findViewById(R.id.tvLoai);
			TextView address = (TextView) v.findViewById(R.id.tvSoLuong);
			TextView phone = (TextView) v.findViewById(R.id.tvGhiChu);
	
			Button btnSync = (Button)v.findViewById(R.id.btnDelete);
			btnSync.setTag(position);
			btnSync.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				//Section 3. remove rows in list
				String pos = view.getTag().toString();
				int _position = Integer.parseInt(pos);
				
				myDbHelper.deleteNhanMau(items.get(_position).getSid(), "" + items.get(_position).getLoai());
				
				////
				
				items.remove(_position);
				notifyDataSetChanged();
				
			}
			});
			
			
			if (name != null) 
			{
				name.setText(String.valueOf(o.getLoai()));
				
				switch (o.getLoai()) 
				{
					case 0:        name.setText("Heparin");                 break;
					case 1:        name.setText("Edta");              break;
					case 2:        name.setText("Citrat");              break;
					case 3:        name.setText("Serum");               break;
					case 4:        name.setText("Chimigly");                 break;
					case 5:        name.setText("Nước tiểu");                 break;
					case 6:        name.setText("Phân");                 break;
					case 7:        name.setText("Đờm");                 break;
					case 8:        name.setText("Dịch");                 break;
					case 9:        name.setText("Lam kính");                 break;
					case 10:        name.setText("Máu lắng");                 break;
					case 11:        name.setText("Cup.HT");                 break;
					case 12:        name.setText("Tinh trùng");                 break;
					case 13:        name.setText("GPB");                 break;
					case 14:        name.setText("Cấy máu");                 break;
					case 15:        name.setText("Ống trắng");                 break;
	
	
					
					default:
						break;
				}

			}
		
			if(address != null)
			{
				address.setText("SL: " + String.valueOf(o.getSoluong()));
			}
			
			if(phone != null)
			{
				phone.setText(o.getGhichu() == 0 ? "Chưa đồng bộ" : "Đã đồng bộ");
			}
			
		
			
		}
		
		return v;
	}
}