package medlatec.listview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import medlatec.listview.R;

public class MainMenuAdapter extends BaseAdapter{
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;
      public MainMenuAdapter(Context c,String[] web,int[] Imageid ) {
          mContext = c;
          this.Imageid = Imageid;
          this.web = web;
      }
    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return web.length;
    }
    @Override
    public Object getItem(int position) {
      // TODO Auto-generated method stub
      return null;
    }
    @Override
    public long getItemId(int position) {
      // TODO Auto-generated method stub
      return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      View grid;
      
          if (convertView == null) 
          {
        	  LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	          grid = new View(mContext);
	          //grid = inflater.inflate(R.layout.itemtweet, null);
	          grid = inflater.inflate(R.layout.itemtweet, parent, false);
          } 
          else 
          {
        	  grid = (View) convertView;
          }
          
          TextView textView = (TextView) grid.findViewById(R.id.grid_text);
          ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
          textView.setText(web[position]);
          imageView.setImageResource(Imageid[position]);
          
      return grid;
    }
    
  /*  public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) 
        {  // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(     Context.LAYOUT_INFLATER_SERVICE );
            v = inflater.inflate(R.layout.gridview_item_layout, parent, false);
        } else {
            v = (View) convertView;
        }
        TextView text = (TextView)v.findViewById(R.id.grid_item_text);
        text.setText(mTextIds[position]);
        ImageView image = (ImageView)v.findViewById(R.id.grid_item_image);
        image.setImageDrawable(mThumbIds[position]);
        return v;
    }*/
    
    
}