package medlatec.listview.testcode;

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
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class TestCode_MienDich extends Activity {

	DatabaseHandler myDbHelperMain;
	DatabaseHandlerTestCode myDbHelper;
	TextView tvAll;
	String[] testCodeSader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mien_dich);
		
		myDbHelper = new DatabaseHandlerTestCode(this);
		myDbHelper.initialise();
		
		myDbHelperMain = new DatabaseHandler(this);
		myDbHelperMain.initialise();
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
	    private ProgressDialog Dialog = new ProgressDialog(TestCode_MienDich.this);

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
	    			if (PatientInformation.ObjectID.equals("") == false)
        			{
        				item.setPrice("" + (Double.parseDouble(item.getPrice()) * 0.95));
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
        	chooseTestCode(tvAll, new String[] {"207HBsAgElec"});
        	break;
        	
        case R.id.tv12: 
        	tvAll = (TextView) findViewById(R.id.tv12);
        	chooseTestCode(tvAll, new String[] { "211HBsAbL" });
        	break;
        	
        case R.id.tv13: 
        	tvAll = (TextView) findViewById(R.id.tv13);
        	chooseTestCode(tvAll, new String[] { "208HbsAg " });
        	break;
        
        	
        	
        	/// P 2
        	
        case R.id.tv21:
        	tvAll = (TextView) findViewById(R.id.tv21);
        	chooseTestCode(tvAll, new String[] { "212HBeAg" });
        	break;
        	
        case R.id.tv25:
        	tvAll = (TextView) findViewById(R.id.tv25);
        	chooseTestCode(tvAll, new String[] { "212HBeAg Ele" });
        	break;
        	
        case R.id.tv22:
        	tvAll = (TextView) findViewById(R.id.tv22);
        	chooseTestCode(tvAll, new String[] { "213HBeAb" });
        	break;
        	
        case R.id.tv26:
        	tvAll = (TextView) findViewById(R.id.tv26);
        	chooseTestCode(tvAll, new String[] { "214HBeAbEle" });
        	break;
        	
        case R.id.tv23:
        	tvAll = (TextView) findViewById(R.id.tv23);
        	chooseTestCode(tvAll, new String[] { "219HBcAbIgM Ele" });
        	break;
        	
        case R.id.tv24:
        	tvAll = (TextView) findViewById(R.id.tv24);
        	chooseTestCode(tvAll, new String[] { "218HBcAbEle" });
        	break;
        	
        
/// P 3
        	
        case R.id.tv31:
        	tvAll = (TextView) findViewById(R.id.tv31);
        	chooseTestCode(tvAll, new String[] { "216HBV DL" });
        	break;
        	
        case R.id.tv32:
        	tvAll = (TextView) findViewById(R.id.tv32);
        	chooseTestCode(tvAll, new String[] { "219HBVR", "219HBVR1", "219HBVR2" });
        	break;
        	
        case R.id.tv33:
        	tvAll = (TextView) findViewById(R.id.tv33);
        	chooseTestCode(tvAll, new String[] { "336Hbv" });
        	break;
        	
        	
        	/// P 4
        	
        case R.id.tv41:
        	tvAll = (TextView) findViewById(R.id.tv41);
        	chooseTestCode(tvAll, new String[] { "405HEV" });
        	break;
        	
        case R.id.tv42:
        	tvAll = (TextView) findViewById(R.id.tv42);
        	chooseTestCode(tvAll, new String[] { "303HAVIgM" });
        	break;
        	
        case R.id.tv43:
        	tvAll = (TextView) findViewById(R.id.tv43);
        	chooseTestCode(tvAll, new String[] { "304HAV Total" });
        	break;
        	
        case R.id.tv44:
        	tvAll = (TextView) findViewById(R.id.tv44);
        	chooseTestCode(tvAll, new String[] { "332HCV Ab Elec" });
        	break;
        	
/// P 5
        	
        case R.id.tv51:
        	tvAll = (TextView) findViewById(R.id.tv51);
        	chooseTestCode(tvAll, new String[] { "1336HCVCAR","1336HCVRN2","1336HCVRNA" }); // đổi HCV
        	break;
        	
        case R.id.tv55:
        	tvAll = (TextView) findViewById(R.id.tv55);
        	chooseTestCode(tvAll, new String[] { "220HCVRT","220HCVRT1","220HCVRT2" });
        	break;
        	
        case R.id.tv52:
        	tvAll = (TextView) findViewById(R.id.tv52);
        	chooseTestCode(tvAll, new String[] { "331HCV" });
        	break;
        	
        case R.id.tv53:
        	tvAll = (TextView) findViewById(R.id.tv53);
        	chooseTestCode(tvAll, new String[] { "210HIV nhanh" });
        	break;
        	
        case R.id.tv54:
        	tvAll = (TextView) findViewById(R.id.tv54);
        	chooseTestCode(tvAll, new String[] { "210HIV Elec" });
        	break;
        
/// P 6
        	
        case R.id.tv61:
        	tvAll = (TextView) findViewById(R.id.tv61);
        	chooseTestCode(tvAll, new String[] { "325Dengue", "325DengueIgG", "325DengueIgM" });
        	break;
        	
        case R.id.tv62:
        	tvAll = (TextView) findViewById(R.id.tv62);
        	chooseTestCode(tvAll, new String[] { "324DengueM ", "325 Dengue NS1", "325DengueIgG", "325DengueIgM" });
        	break;
        	
        case R.id.tv63:
        	tvAll = (TextView) findViewById(R.id.tv63);
        	chooseTestCode(tvAll, new String[] { "794Dengue", "795Dengue1", "796Dengue2" });
        	break;
        	
/// P 7
        	
        case R.id.tv71:
        	tvAll = (TextView) findViewById(R.id.tv71);
        	chooseTestCode(tvAll, new String[] { "222BRube" });
        	break;
        	
        case R.id.tv72:
        	tvAll = (TextView) findViewById(R.id.tv72);
        	chooseTestCode(tvAll, new String[] { "222ARube" });
        	break;
        	
        case R.id.tv73:
        	tvAll = (TextView) findViewById(R.id.tv73);
        	chooseTestCode(tvAll, new String[] { "222Toxo IgM" });
        	break;
   
        case R.id.tv74:
        	tvAll = (TextView) findViewById(R.id.tv74);
        	chooseTestCode(tvAll, new String[] { "223Toxo IgG" });
        	break;
        	
/// P8
        	
        case R.id.tv81:
        	tvAll = (TextView) findViewById(R.id.tv81);
        	chooseTestCode(tvAll, new String[] { "218CMV IgM" });
        	break;
        	
        case R.id.tv82:
        	tvAll = (TextView) findViewById(R.id.tv82);
        	chooseTestCode(tvAll, new String[] { "219CMVIgG" });
        	break;
        	
        case R.id.tv83:
        	tvAll = (TextView) findViewById(R.id.tv83);
        	chooseTestCode(tvAll, new String[] { "325CMVDL" });
        	break;
      
/// P 9
        	
        case R.id.tv91:
        	tvAll = (TextView) findViewById(R.id.tv91);
        	chooseTestCode(tvAll, new String[] { "302Heper" });
        	break;
        	
        case R.id.tv92:
        	tvAll = (TextView) findViewById(R.id.tv92);
        	chooseTestCode(tvAll, new String[] { "220Heper" });
        	break;
        	
        case R.id.tv93:
        	tvAll = (TextView) findViewById(R.id.tv93);
        	chooseTestCode(tvAll, new String[] { "329EBV" });
        	break;
        	
        case R.id.tv94:
        	tvAll = (TextView) findViewById(R.id.tv94);
        	chooseTestCode(tvAll, new String[] { "330EBV" });
        	break;
        	
        case R.id.tv95:
        	tvAll = (TextView) findViewById(R.id.tv95);
        	chooseTestCode(tvAll, new String[] { "797EBV" });
        	break;
        	
/// P 10
        	
        case R.id.tv101:
        	tvAll = (TextView) findViewById(R.id.tv101);
        	chooseTestCode(tvAll, new String[] { "333HPV" });
        	break;
        	
        case R.id.tv102:
        	tvAll = (TextView) findViewById(R.id.tv102);
        	chooseTestCode(tvAll, new String[] { "334HPV" });
        	break;
        	
/// P 11
        	
        case R.id.tv111:
        	tvAll = (TextView) findViewById(R.id.tv111);
        	chooseTestCode(tvAll, new String[] { "786Cum", "786CumA","786CumB","786CumH" });
        	break;
        	
        case R.id.tv112:
        	tvAll = (TextView) findViewById(R.id.tv112);
        	chooseTestCode(tvAll, new String[] { "378EV" });
        	break;
        	
/// P 12
        	
        case R.id.tv121:
        	tvAll = (TextView) findViewById(R.id.tv121);
        	chooseTestCode(tvAll, new String[] { "788CAR" });
        	break;
        	
        case R.id.tv122:
        	tvAll = (TextView) findViewById(R.id.tv122);
        	chooseTestCode(tvAll, new String[] { "787CAR" });
        	break;
        	
        case R.id.tv123:
        	tvAll = (TextView) findViewById(R.id.tv123);
        	chooseTestCode(tvAll, new String[] { "174 Anti Beta 2", "174 Anti beta2", "174Anti beta2" });
        	break;
        	
/// P 13
        	
        case R.id.tv131:
        	tvAll = (TextView) findViewById(R.id.tv131);
        	chooseTestCode(tvAll, new String[] { "791ANTIP" });
        	break;
        	
        case R.id.tv132:
        	tvAll = (TextView) findViewById(R.id.tv132);
        	chooseTestCode(tvAll, new String[] { "791ANTIPG" });
        	break;
        	
/// P 14
        	
        case R.id.tv141:
        	tvAll = (TextView) findViewById(R.id.tv141);
        	chooseTestCode(tvAll, new String[] { "131CRPDL" });
        	break;
        	
        case R.id.tv142:
        	tvAll = (TextView) findViewById(R.id.tv142);
        	chooseTestCode(tvAll, new String[] { "328CRP-hs" });
        	break;
        	
        case R.id.tv143:
        	tvAll = (TextView) findViewById(R.id.tv143);
        	chooseTestCode(tvAll, new String[] { "304IgA" });
        	break;
        	
        case R.id.tv144:
        	tvAll = (TextView) findViewById(R.id.tv144);
        	chooseTestCode(tvAll, new String[] { "305IgG" });
        	break;
        	
        case R.id.tv145:
        	tvAll = (TextView) findViewById(R.id.tv145);
        	chooseTestCode(tvAll, new String[] { "306IgM" });
        	break;
        	
/// P 15
        	
        case R.id.tv151:
        	tvAll = (TextView) findViewById(R.id.tv151);
        	chooseTestCode(tvAll, new String[] { "144ASLODL" });
        	break;
        	
        case R.id.tv152:
        	tvAll = (TextView) findViewById(R.id.tv152);
        	chooseTestCode(tvAll, new String[] { "132RFDL" });
        	break;
        	
        case R.id.tv153:
        	tvAll = (TextView) findViewById(R.id.tv153);
        	chooseTestCode(tvAll, new String[] { "799CCP" });
        	break;
        	
        case R.id.tv154:
        	tvAll = (TextView) findViewById(R.id.tv154);
        	chooseTestCode(tvAll, new String[] { "780C3" });
        	break;
        	
        case R.id.tv155:
        	tvAll = (TextView) findViewById(R.id.tv155);
        	chooseTestCode(tvAll, new String[] { "781C4" });
        	break;
        	
        default:
        	break;
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_code__mien_dich, menu);
		return true;
	}

}
