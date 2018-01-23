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
import medlatec.listview.connect.DatabaseHandler;
import medlatec.listview.connect.DatabaseHandlerTestCode;
import medlatec.listview.object.TestCode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.SQLException;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TestCode_HoaSinh extends Activity {

	DatabaseHandler myDbHelperMain;
	DatabaseHandlerTestCode myDbHelper;
	TextView tvAll;
	String[] testCodeSader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hoa_sinh);
		
		
		myDbHelper = new DatabaseHandlerTestCode(this);
		myDbHelper.initialise();
		
		myDbHelperMain = new DatabaseHandler(this);
		myDbHelperMain.initialise();
		
		String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
        String tits = "[CÁN BỘ: " + Front_Activity.edRealName.toUpperCase() + "] - TỔNG TIỀN DỊCH VỤ: " + myDbHelperMain.getSumResult(sid);
        tits += "\nTIN NHẮN CHƯA ĐỌC: " + myDbHelperMain.getNonReadDumb();
        MainActivity.txtSum.setText(tits);
	}
	
	public void chooseTestCode(TextView tvChoose, String[] testCodes)
	{
		String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
		testCodeSader = testCodes;
		
		if (tvAll.getCurrentTextColor() == Color.RED)
    	{
			new SomeTask().execute(new String[] {sid, "red"});
    	}
		else
		{
			new SomeTask().execute(new String[] {sid, "blue"});
		}
	}
	
	private class SomeTask extends AsyncTask<String, Void, Integer> 
	{
	    private ProgressDialog Dialog = new ProgressDialog(TestCode_HoaSinh.this);

	    @Override
	    protected void onPreExecute()
	    {
	        Dialog.setMessage("Cập nhật dịch vụ, xin chờ ...! ");
	        Dialog.show();
	        Dialog.setCancelable(false);
	        Dialog.setCanceledOnTouchOutside(false);
	    }

	    @Override
	    protected Integer doInBackground(String... params) 
	    {
	        TestCode item = new TestCode();
	        int resultType = 0;
	        
			if (params[1].equals("red"))
			{
				resultType = 1;
				for(int i = 0; i < testCodeSader.length; i++)
				{
					myDbHelperMain.deleteResult(testCodeSader[i], params[0]);
				}
	    	}
			else
	    	{
				resultType = 2;
	    		for(int i = 0; i < testCodeSader.length; i++)
				{
	    			item = myDbHelper.selectOneTestCode(testCodeSader[i]);
	    			//
	    			if (PatientInformation.ObjectID.equals("") == false)
        			{
        				item.setPrice("" + (Double.parseDouble(item.getPrice()) * 0.95));
        				Log.d("OBJECTID", "" + PatientInformation.ObjectID);
        			}
	    			item.setSid(params[0]);
					myDbHelperMain.addResult(item);
				}
	    	}
	        return resultType;
	    }

	    @Override
	    protected void onPostExecute(Integer result)
	    {
	        if(result==1)
	        {
	        	tvAll.setTextColor(Color.BLACK);
	        }
	        else
	        {
	        	tvAll.setTextColor(Color.RED);
	        }
	        //
	        String sid = String.valueOf(PatientInformation.etTime.getText()) + "-" + String.valueOf(PatientInformation.etSID.getText());
	        String tits = "[CÁN BỘ: " + Front_Activity.edRealName.toUpperCase() + "] - TỔNG TIỀN DỊCH VỤ: " + myDbHelperMain.getSumResult(sid);
	        tits += "\nTIN NHẮN CHƯA ĐỌC: " + myDbHelperMain.getNonReadDumb();
	        MainActivity.txtSum.setText(tits);
	        // after completed finished the progressbar
	        Dialog.dismiss();
	    }
	}
	
	public void onClick (View view)
	{
		switch(view.getId()) {
        case R.id.tv11:
        	
        	tvAll = (TextView) findViewById(R.id.tv11);
        	chooseTestCode(tvAll, new String[] {"107AST"});
        	break;
        	
        case R.id.tv12: 
        	tvAll = (TextView) findViewById(R.id.tv12);
        	chooseTestCode(tvAll, new String[] { "108ALT" });
        	break;
        
        case R.id.tv13:
        	tvAll = (TextView) findViewById(R.id.tv13);
        	chooseTestCode(tvAll, new String[] { "110LDH" });
        	break;
        	
        	
        	/// P 2
        	
        case R.id.tv21:
        	tvAll = (TextView) findViewById(R.id.tv21);
        	chooseTestCode(tvAll, new String[] { "109Bili1", "109Bili2", "109Bili3" });
        	break;
        	
        case R.id.tv22:
        	tvAll = (TextView) findViewById(R.id.tv22);
        	chooseTestCode(tvAll, new String[] { "114ALBMau" });
        	break;
        	
        case R.id.tv23:
        	tvAll = (TextView) findViewById(R.id.tv23);
        	chooseTestCode(tvAll, new String[] { "115ALBnt" });
        	break;
        	
        
/// P 3
        	
        case R.id.tv31:
        	tvAll = (TextView) findViewById(R.id.tv31);
        	chooseTestCode(tvAll, new String[] { "109GGT" });
        	break;
        	
        case R.id.tv32:
        	tvAll = (TextView) findViewById(R.id.tv32);
        	chooseTestCode(tvAll, new String[] { "116GLO" });
        	break;
        	
        case R.id.tv33:
        	tvAll = (TextView) findViewById(R.id.tv33);
        	chooseTestCode(tvAll, new String[] { "117AG" });
        	break;
        	
        	
        	/// P 4
        	
        case R.id.tv41:
        	tvAll = (TextView) findViewById(R.id.tv41);
        	chooseTestCode(tvAll, new String[] { "112Pro" });
        	break;
        	
        case R.id.tv42:
        	tvAll = (TextView) findViewById(R.id.tv42);
        	chooseTestCode(tvAll, new String[] { "143ProNT" });
        	break;
        	
        case R.id.tv43:
        	tvAll = (TextView) findViewById(R.id.tv43);
        	chooseTestCode(tvAll, new String[] { "126AMY mau" });
        	break;
        	
        case R.id.tv44:
        	tvAll = (TextView) findViewById(R.id.tv44);
        	chooseTestCode(tvAll, new String[] { "127Amyla NT" });
        	break;
        	
        case R.id.tv45:
        	tvAll = (TextView) findViewById(R.id.tv45);
        	chooseTestCode(tvAll, new String[] { "113PNT" });
        	break;
        	
/// P 5
        	
        case R.id.tv51:
        	tvAll = (TextView) findViewById(R.id.tv51);
        	chooseTestCode(tvAll, new String[] { "902Lipase" });
        	break;
        	
        case R.id.tv52:
        	tvAll = (TextView) findViewById(R.id.tv52);
        	chooseTestCode(tvAll, new String[] { "149C-Peptid" });
        	break;
        	
        case R.id.tv53:
        	tvAll = (TextView) findViewById(R.id.tv53);
        	chooseTestCode(tvAll, new String[] { "123Uric m" });
        	break;
        	
        case R.id.tv54:
        	tvAll = (TextView) findViewById(R.id.tv54);
        	chooseTestCode(tvAll, new String[] { "125Uric NT" });
        	break;
        
/// P 6
        	
        case R.id.tv61:
        	tvAll = (TextView) findViewById(R.id.tv61);
        	chooseTestCode(tvAll, new String[] { "117Glu" });
        	break;
        	
        case R.id.tv62:
        	tvAll = (TextView) findViewById(R.id.tv62);
        	chooseTestCode(tvAll, new String[] { "119HbA1CD" });
        	break;
        	
        case R.id.tv63:
        	tvAll = (TextView) findViewById(R.id.tv63);
        	chooseTestCode(tvAll, new String[] { "120Insu" });
        	break;
        	
/// P 7
        	
        case R.id.tv71:
        	tvAll = (TextView) findViewById(R.id.tv71);
        	chooseTestCode(tvAll, new String[] { "121Ure" });
        	break;
        	
        case R.id.tv72:
        	tvAll = (TextView) findViewById(R.id.tv72);
        	chooseTestCode(tvAll, new String[] { "121Ure NT" });
        	break;
        	
        case R.id.tv73:
        	tvAll = (TextView) findViewById(R.id.tv73);
        	chooseTestCode(tvAll, new String[] { "122CRE" });
        	break;
   
        case R.id.tv74:
        	tvAll = (TextView) findViewById(R.id.tv74);
        	chooseTestCode(tvAll, new String[] { "122CRE NT" });
        	break;
        	
/// P8
        	
        case R.id.tv81:
        	tvAll = (TextView) findViewById(R.id.tv81);
        	chooseTestCode(tvAll, new String[] { "131Trig" });
        	break;
        	
        case R.id.tv82:
        	tvAll = (TextView) findViewById(R.id.tv82);
        	chooseTestCode(tvAll, new String[] { "133Cho" });
        	break;
      
/// P 9
        	
        case R.id.tv91:
        	tvAll = (TextView) findViewById(R.id.tv91);
        	chooseTestCode(tvAll, new String[] { "137CK" });
        	break;
        	
        case R.id.tv92:
        	tvAll = (TextView) findViewById(R.id.tv92);
        	chooseTestCode(tvAll, new String[] { "138CKMB" });
        	break;
        	
        case R.id.tv93:
        	tvAll = (TextView) findViewById(R.id.tv93);
        	chooseTestCode(tvAll, new String[] { "352BNP" });
        	break;
        	
/// P 10
        	
        case R.id.tv101:
        	tvAll = (TextView) findViewById(R.id.tv101);
        	chooseTestCode(tvAll, new String[] { "134HDL" });
        	break;
        	
        case R.id.tv102:
        	tvAll = (TextView) findViewById(R.id.tv102);
        	chooseTestCode(tvAll, new String[] { "135LDL_T" });
        	break;
        	
/// P 11
        	
        case R.id.tv111:
        	tvAll = (TextView) findViewById(R.id.tv111);
        	chooseTestCode(tvAll, new String[] { "145Tro" });
        	break;
        	
        case R.id.tv112:
        	tvAll = (TextView) findViewById(R.id.tv112);
        	chooseTestCode(tvAll, new String[] { "360Homo" });
        	break;
        	
/// P 12
        	
        case R.id.tv121:
        	tvAll = (TextView) findViewById(R.id.tv121);
        	chooseTestCode(tvAll, new String[] { "142Fe" });
        	break;
        	
        case R.id.tv122:
        	tvAll = (TextView) findViewById(R.id.tv122);
        	chooseTestCode(tvAll, new String[] { "144Ferrtin" });
        	break;
        	
        case R.id.tv123:
        	tvAll = (TextView) findViewById(R.id.tv123);
        	chooseTestCode(tvAll, new String[] { "146Tran" });
        	break;
        	
/// P 13
        	
        case R.id.tv131:
        	tvAll = (TextView) findViewById(R.id.tv131);
        	chooseTestCode(tvAll, new String[] { "226Fola" });
        	break;
        	
        case R.id.tv132:
        	tvAll = (TextView) findViewById(R.id.tv132);
        	chooseTestCode(tvAll, new String[] { "408D3" });
        	break;
        	
        case R.id.tv133:
        	tvAll = (TextView) findViewById(R.id.tv133);
        	chooseTestCode(tvAll, new String[] { "154Oste" });
        	break;
        	
        case R.id.tv134:
        	tvAll = (TextView) findViewById(R.id.tv134);
        	chooseTestCode(tvAll, new String[] { "327B12" });
        	break;
        	
/// P 14
        	
        case R.id.tv141:
        	tvAll = (TextView) findViewById(R.id.tv141);
        	chooseTestCode(tvAll, new String[] { "149BDG2", "149CNaS", "149DKaS" , "149ECls" });
        	break;
        	
        case R.id.tv142:
        	tvAll = (TextView) findViewById(R.id.tv142);
        	chooseTestCode(tvAll, new String[] { "150ADiengiai NT", "150Clo", "150Kali","150Na" });
        	break;
        	
        case R.id.tv143:
        	tvAll = (TextView) findViewById(R.id.tv143);
        	chooseTestCode(tvAll, new String[] { "149FCa" });
        	break;
        	
        case R.id.tv144:
        	tvAll = (TextView) findViewById(R.id.tv144);
        	chooseTestCode(tvAll, new String[] { "149FCanxiion" });
        	break;
        	
/// P 15
        	
        case R.id.tv151:
        	tvAll = (TextView) findViewById(R.id.tv151);
        	chooseTestCode(tvAll, new String[] { "149ASC",
			"148AERY",
			"147UBil",
			"146UUBG",
			"145UKet",
			"144Uglu",
			"143ProNT",
			"142Nit",
			"141ULeu",
			"140UpH",
			"139USG",
			"138PTNT" });
        	break;
        	
        case R.id.tv152:
        	tvAll = (TextView) findViewById(R.id.tv152);
        	chooseTestCode(tvAll, new String[] { "149UCan", "149Vcan" });
        	break;
        	
        case R.id.tv153:
        	tvAll = (TextView) findViewById(R.id.tv153);
        	chooseTestCode(tvAll, new String[] { "0988Micro" });
        	break;
        	
        case R.id.tv154:
        	tvAll = (TextView) findViewById(R.id.tv154);
        	chooseTestCode(tvAll, new String[] { "140Microalb" });
        	break;
        	
        case R.id.tv155:
        	tvAll = (TextView) findViewById(R.id.tv155);
        	chooseTestCode(tvAll, new String[] { "156ALP" });
        	break;
        	
        default:
        	break;
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hoa_sinh, menu);
		return true;
	}

	
}
