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
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class TestCode_Khac extends Activity {

	DatabaseHandler myDbHelperMain;
	DatabaseHandlerTestCode myDbHelper;
	TextView tvAll;
	String[] testCodeSader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_khac);
		
		myDbHelper = new DatabaseHandlerTestCode(this);
		myDbHelper.initialise();
		
		myDbHelperMain = new DatabaseHandler(this);
		myDbHelperMain.initialise();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity__khac, menu);
		return true;
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
	    private ProgressDialog Dialog = new ProgressDialog(TestCode_Khac.this);

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
        	chooseTestCode(tvAll, new String[] { "803GRP" });
        	break;
        	
        case R.id.tv12:
        	tvAll = (TextView) findViewById(R.id.tv12);
        	chooseTestCode(tvAll, new String[] { "802PEPSNG" });
        	break;
        	
        case R.id.tv13:
        	tvAll = (TextView) findViewById(R.id.tv13);
        	chooseTestCode(tvAll, new String[] { "803PEPSNG", "804Pep I", "805Pepsinogen","806Pep I/II" });
        	break;
        	
        case R.id.tv14:
        	tvAll = (TextView) findViewById(R.id.tv14);
        	chooseTestCode(tvAll, new String[] { "113KCT"});
        	break;
        	
        	/// P 2
        	
        	
        	
        case R.id.tv21:
        	tvAll = (TextView) findViewById(R.id.tv21);
        	chooseTestCode(tvAll, new String[] { "208HbsAg " });
        	break;
        	
        case R.id.tv22:
        	tvAll = (TextView) findViewById(R.id.tv22);
        	chooseTestCode(tvAll, new String[] { "280F" });
        	break;
        	
        
/// P 3
        	
        case R.id.tv31:
        	tvAll = (TextView) findViewById(R.id.tv31);
        	chooseTestCode(tvAll, new String[] { "999VZT" });
        	break;
        	
        case R.id.tv32:
        	tvAll = (TextView) findViewById(R.id.tv32);
        	chooseTestCode(tvAll, new String[] { "707VZ" });
        	break;
        	
        case R.id.tv33:
        	tvAll = (TextView) findViewById(R.id.tv33);
        	chooseTestCode(tvAll, new String[] { "707VZM" });
        	break;
        	
        	/// P 4
        	
        case R.id.tv41:
        	tvAll = (TextView) findViewById(R.id.tv41);
        	chooseTestCode(tvAll, new String[] { "782Mea"});
        	break;
        	
        case R.id.tv42:
        	tvAll = (TextView) findViewById(R.id.tv42);
        	chooseTestCode(tvAll, new String[] { "783Mea-M"});
        	break;
        	
        case R.id.tv43:
        	tvAll = (TextView) findViewById(R.id.tv43);
        	chooseTestCode(tvAll, new String[] { "1189Ma"});
        	break;
        	
        case R.id.tv44:
        	tvAll = (TextView) findViewById(R.id.tv44);
        	chooseTestCode(tvAll, new String[] { "798Pre"});
        	break;
      
        	
/// P 5
        	
        case R.id.tv51:
        	tvAll = (TextView) findViewById(R.id.tv51);
        	chooseTestCode(tvAll, new String[] { "785Mum" });
        	break;
        	
        case R.id.tv52:
        	tvAll = (TextView) findViewById(R.id.tv52);
        	chooseTestCode(tvAll, new String[] { "785MumM" });
        	break;
        	
        case R.id.tv53:
        	tvAll = (TextView) findViewById(R.id.tv53);
        	chooseTestCode(tvAll, new String[] { "330EBV" });
        	break;
        	
        case R.id.tv54:
        	tvAll = (TextView) findViewById(R.id.tv54);
        	chooseTestCode(tvAll, new String[] { "329EBV" });
        	break;
        	
        case R.id.tv55:
        	tvAll = (TextView) findViewById(R.id.tv55);
        	chooseTestCode(tvAll, new String[] { "797EBV" });
        	break;
        	
        
/// P 6
        	
        case R.id.tv61:
        	tvAll = (TextView) findViewById(R.id.tv61);
        	chooseTestCode(tvAll, new String[] { "807Sirolimus" });
        	break;
        	
        case R.id.tv62:
        	tvAll = (TextView) findViewById(R.id.tv62);
        	chooseTestCode(tvAll, new String[] { "337CE" });
        	break;
        	
        case R.id.tv63:
        	tvAll = (TextView) findViewById(R.id.tv63);
        	chooseTestCode(tvAll, new String[] { "337LA" });
        	break;
        	
        	
/// P 7
        	
        case R.id.tv71:
        	tvAll = (TextView) findViewById(R.id.tv71);
        	chooseTestCode(tvAll, new String[] { "999CHE" });
        	break;
        	
        case R.id.tv72:
        	tvAll = (TextView) findViewById(R.id.tv72);
        	chooseTestCode(tvAll, new String[] { "166G6" });
        	break;
        	
        case R.id.tv73:
        	tvAll = (TextView) findViewById(R.id.tv73);
        	chooseTestCode(tvAll, new String[] { "628M" });
        	break;

        	
/// P8
        	
        case R.id.tv81:
        	tvAll = (TextView) findViewById(R.id.tv81);
        	chooseTestCode(tvAll, new String[] { "138CU" });
        	break;
        	
        case R.id.tv82:
        	tvAll = (TextView) findViewById(R.id.tv82);
        	chooseTestCode(tvAll, new String[] { "770NST" });
        	break;
        	
        case R.id.tv83:
        	tvAll = (TextView) findViewById(R.id.tv83);
        	chooseTestCode(tvAll, new String[] { "150Phot" });
        	break;
        	
        case R.id.tv84:
        	tvAll = (TextView) findViewById(R.id.tv84);
        	chooseTestCode(tvAll, new String[] { "136MgS" });
        	break;
        	
        case R.id.tv85:
        	tvAll = (TextView) findViewById(R.id.tv85);
        	chooseTestCode(tvAll, new String[] { "139MgU" });
        	break;
 
/// P9
        case R.id.tv91:
        	tvAll = (TextView) findViewById(R.id.tv91);
        	chooseTestCode(tvAll, new String[] { "1299CGNNT",
			"1300THCM",
			"1301MOPH",
			"1302METM",
			"1303MDMA" });
        	break;
        	
        case R.id.tv92:
        	tvAll = (TextView) findViewById(R.id.tv92);
        	chooseTestCode(tvAll, new String[] { "137Nghien NT" });
        	break;
        	
        case R.id.tv93:
        	tvAll = (TextView) findViewById(R.id.tv93);
        	chooseTestCode(tvAll, new String[] { "137Nghien M" });
        	break;
        	
/// P10
        	
        case R.id.tv101:
        	tvAll = (TextView) findViewById(R.id.tv101);
        	chooseTestCode(tvAll, new String[] { "940jevm" });
        	break;
        	
        case R.id.tv102:
        	tvAll = (TextView) findViewById(R.id.tv102);
        	chooseTestCode(tvAll, new String[] { "941jevG" });
        	break;
        	
        case R.id.tv103:
        	tvAll = (TextView) findViewById(R.id.tv103);
        	chooseTestCode(tvAll, new String[] { "1337HCVART",
        			"1337HCVRN2",
        			"1337HCVRNA" });
        	break;
        	
        case R.id.tv104:
        	tvAll = (TextView) findViewById(R.id.tv104);
        	chooseTestCode(tvAll, new String[] { "1336HCVCAR",
        			"1336HCVRN2",
        			"1336HCVRNA" });
        	break;
        	
        case R.id.tv105:
        	tvAll = (TextView) findViewById(R.id.tv105);
        	chooseTestCode(tvAll, new String[] { "628Widal",
				"628Widal1",
				"628Widal2",
				"628Widal3",
				"628Widal4",
				"628Widal5",
				"628Widal6" });
        	break;
        	
        default:
        	break;
		}
		

	}

	
	    
}
