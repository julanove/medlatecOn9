package medlatec.listview.adapter;

import java.util.ArrayList;

import medlatec.listview.R;
import medlatec.listview.R.id;
import medlatec.listview.R.layout;
import medlatec.listview.object.TestCode;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyCustomBaseAdapter extends BaseAdapter {
	
	 private ArrayList<TestCode> searchArrayList;
	 
	 private LayoutInflater mInflater;

	 public MyCustomBaseAdapter(Context context, ArrayList<TestCode> results) {
	  searchArrayList = results;
	  mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
	  return searchArrayList.size();
	 }

	 public Object getItem(int position) {
	  return searchArrayList.get(position);
	 }

	 public long getItemId(int position) {
	  return position;
	 }

	 public View getView(int position, View convertView, ViewGroup parent) {
	  ViewHolder holder;
	  if (convertView == null) {
	   convertView = mInflater.inflate(R.layout.item2, null);
	   holder = new ViewHolder();
	   holder.txtName = (TextView) convertView.findViewById(R.id.txt123);
	   holder.txtCityState = (TextView) convertView.findViewById(R.id.cityState);
	   holder.txtPhone = (CheckBox) convertView.findViewById(R.id.item2);

	   convertView.setTag(holder);
	  } else {
	   holder = (ViewHolder) convertView.getTag();
	  }
	  
	  holder.txtName.setText(searchArrayList.get(position).getTestName());
	  holder.txtCityState.setText("Giá tiền: " + searchArrayList.get(position).getPrice());
	  holder.txtPhone.setChecked(searchArrayList.get(position).isChecked());

	  return convertView;
	 }
	 
	 public void add(TestCode album)
	 {
	     Log.w("StudentAdapter","add");
	     searchArrayList.add(album);
	 }
	 
	 public void remove(int i)
	 {
		 searchArrayList.remove(i);
	 }
	 
	 @Override
	 public void notifyDataSetChanged()
	 {
		 super.notifyDataSetChanged();
	 }

	 static class ViewHolder {
	  TextView txtName;
	  TextView txtCityState;
	  CheckBox txtPhone;
	 }
	}