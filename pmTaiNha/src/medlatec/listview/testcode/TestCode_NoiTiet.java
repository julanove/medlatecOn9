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
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class TestCode_NoiTiet extends Activity {

	DatabaseHandler myDbHelperMain;
	DatabaseHandlerTestCode myDbHelper;
	TextView tvAll;
	String[] testCodeSader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noi_tiet);
		
		myDbHelper = new DatabaseHandlerTestCode(this);
		myDbHelper.initialise();
		
		myDbHelperMain = new DatabaseHandler(this);
		myDbHelperMain.initialise();
	}
	
	
	public void onClick (View view)
	{
		switch(view.getId()) {
        case R.id.tv11:
        	
        	tvAll = (TextView) findViewById(R.id.tv11);
        	chooseTestCode(tvAll, new String[] {"314TSH"});
        	break;
        	
        case R.id.tv12: 
        	tvAll = (TextView) findViewById(R.id.tv12);
        	chooseTestCode(tvAll, new String[] { "310T3" });
        	break;
        
        case R.id.tv13:
        	tvAll = (TextView) findViewById(R.id.tv13);
        	chooseTestCode(tvAll, new String[] { "311FT3" });
        	break;
        	
        case R.id.tv14:
        	tvAll = (TextView) findViewById(R.id.tv14);
        	chooseTestCode(tvAll, new String[] { "312T4" });
        	break;
        	
        case R.id.tv15:
        	tvAll = (TextView) findViewById(R.id.tv15);
        	chooseTestCode(tvAll, new String[] { "313FT4" });
        	break;
        	
        	/// P 2
        	
        case R.id.tv21:
        	tvAll = (TextView) findViewById(R.id.tv21);
        	chooseTestCode(tvAll, new String[] { "315TPOAb" });
        	break;
        	
        case R.id.tv22:
        	tvAll = (TextView) findViewById(R.id.tv22);
        	chooseTestCode(tvAll, new String[] { "316TG" });
        	break;
        	
        case R.id.tv23:
        	tvAll = (TextView) findViewById(R.id.tv23);
        	chooseTestCode(tvAll, new String[] { "317TGAb" });
        	break;
        	
        case R.id.tv24:
        	tvAll = (TextView) findViewById(R.id.tv24);
        	chooseTestCode(tvAll, new String[] { "200TRAb" });
        	break;
        	
        case R.id.tv25:
        	tvAll = (TextView) findViewById(R.id.tv25);
        	chooseTestCode(tvAll, new String[] { "955pth" });
        	break;
        	
        
/// P 3
        	
        case R.id.tv31:
        	tvAll = (TextView) findViewById(R.id.tv31);
        	chooseTestCode(tvAll, new String[] { "318Cortisol M", "318DCOR", "318ECOR" });
        	break;
        	
        case R.id.tv32:
        	tvAll = (TextView) findViewById(R.id.tv32);
        	chooseTestCode(tvAll, new String[] { "318FCortisol NT" });
        	break;
        	
        case R.id.tv33:
        	tvAll = (TextView) findViewById(R.id.tv33);
        	chooseTestCode(tvAll, new String[] { "319Cate M", "319Fcate","319Nara","320Adre1" });
        	break;
        	
        case R.id.tv34:
        	tvAll = (TextView) findViewById(R.id.tv34);
        	chooseTestCode(tvAll, new String[] { "320Cate NT", "320dcate", "320E cate", "320Ecate" });
        	break;
        	
        case R.id.tv35:
        	tvAll = (TextView) findViewById(R.id.tv35);
        	chooseTestCode(tvAll, new String[] { "319CONT" });
        	break;
        	
        	
        	
        	/// P 4
        	
        case R.id.tv41:
        	tvAll = (TextView) findViewById(R.id.tv41);
        	chooseTestCode(tvAll, new String[] { "320Andro" });
        	break;
        	
        case R.id.tv42:
        	tvAll = (TextView) findViewById(R.id.tv42);
        	chooseTestCode(tvAll, new String[] { "352Testo" });
        	break;
        	
        case R.id.tv43:
        	tvAll = (TextView) findViewById(R.id.tv43);
        	chooseTestCode(tvAll, new String[] { "347PRG","348","349","350","351" });
        	break;
        	
/// P 5
        	
        case R.id.tv51:
        	tvAll = (TextView) findViewById(R.id.tv51);
        	chooseTestCode(tvAll, new String[] { "342Estr", "343", "344", "345", "346" });
        	break;
        	
        case R.id.tv52:
        	tvAll = (TextView) findViewById(R.id.tv52);
        	chooseTestCode(tvAll, new String[] { "327FSH", "328", "329", "330", "331" });
        	break;
        	
        case R.id.tv53:
        	tvAll = (TextView) findViewById(R.id.tv53);
        	chooseTestCode(tvAll, new String[] { "334LH", "335","336","337","338" });
        	break;
        	
        case R.id.tv54:
        	tvAll = (TextView) findViewById(R.id.tv54);
        	chooseTestCode(tvAll, new String[] { "339PRL","340","341","342" });
        	break;
        	
        case R.id.tv55:
        	tvAll = (TextView) findViewById(R.id.tv55);
        	chooseTestCode(tvAll, new String[] { "361AMH" });
        	break;
        
/// P 6
        	
        case R.id.tv61:
        	tvAll = (TextView) findViewById(R.id.tv61);
        	chooseTestCode(tvAll, new String[] { "358IGF" });
        	break;
        	
        case R.id.tv62:
        	tvAll = (TextView) findViewById(R.id.tv62);
        	chooseTestCode(tvAll, new String[] { "357HG" });
        	break;
        	
        case R.id.tv63:
        	tvAll = (TextView) findViewById(R.id.tv63);
        	chooseTestCode(tvAll, new String[] { "357Gh" });
        	break;
        	
        case R.id.tv64:
        	tvAll = (TextView) findViewById(R.id.tv64);
        	chooseTestCode(tvAll, new String[] { "326Beta hCG" });
        	break;
        	
/// P 7
        	
        case R.id.tv71:
        	tvAll = (TextView) findViewById(R.id.tv71);
        	chooseTestCode(tvAll, new String[] { "682DIU36",
			"683FA33",
			"694EQ1",
			"694F13",
			"694G12",
			"695EQ5",
			"710FEBM",
			"710FEBN",
			"710FECA",
			"710FECBM",
			"710FECC",
			"710FECDM",
			"710FECH",
			"710FECHI",
			"710FECM",
			"710FECMB",
			"710FECMP",
			"710FEDT",
			"710FEGBG",
			"710FEGX",
			"710FEHV",
			"710FEIGE",
			"710FELC",
			"710FELDT",
			"710FELTT",
			"710FELTX",
			"710FENC",
			"710FENCD",
			"710FENK",
			"710FENLA",
			"710FENM",
			"710FENPN",
			"710FESB",
			"710FESCL",
			"710FETB",
			"710FETG",
			"710FETM" });
        	break;
        	
        	
        case R.id.tv75:
        	tvAll = (TextView) findViewById(R.id.tv75);
        	chooseTestCode(tvAll, new String[] { "307IgE" });
        	break;
        	
        case R.id.tv76:
        	tvAll = (TextView) findViewById(R.id.tv76);
        	chooseTestCode(tvAll, new String[] { "358ACTH" });
        	break;
        	
/// P8
        	
        case R.id.tv81:
        	tvAll = (TextView) findViewById(R.id.tv81);
        	chooseTestCode(tvAll, new String[] { "137Nghien M" });
        	break;
        	
        case R.id.tv82:
        	tvAll = (TextView) findViewById(R.id.tv82);
        	chooseTestCode(tvAll, new String[] { "137Nghien NT" });
        	break;
        	
        case R.id.tv83:
        	tvAll = (TextView) findViewById(R.id.tv83);
        	chooseTestCode(tvAll, new String[] { "620TDD",
			"624TTD",
			"625KGH",
			"626TGTM",
			"627PH",
			"628BC",
			"628DLG",
			"628TTM",
			"635DNM",
			"636MDT",
			"639DDD",
			"640HDT",
			"641MDT",
			"642MDC",
			"643TDD",
			"644TTT",
			"645TTM",
			"646TSD",
			"648TSC" });
        	break;
      
/// P 9
        	
        case R.id.tv91:
        	tvAll = (TextView) findViewById(R.id.tv91);
        	chooseTestCode(tvAll, new String[] { "633DBT",
			"634BPA",
			"634BT",
			"634Do",
			"634Dou",
			"634Douw" });
        	break;
        	
        case R.id.tv92:
        	tvAll = (TextView) findViewById(R.id.tv92);
        	chooseTestCode(tvAll, new String[] { "308Trip",
			"333d",
			"335d",
			"336n",
			"631AFP1",
			"632Beta hCG",
			"633UE3" });
        	break;
        	
/// P 10
        	
        case R.id.tv101:
        	tvAll = (TextView) findViewById(R.id.tv101);
        	chooseTestCode(tvAll, new String[] { "360ReninA" });
        	break;
        
        	
        default:
        	break;
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_code__noi_tiet, menu);
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
	    private ProgressDialog Dialog = new ProgressDialog(TestCode_NoiTiet.this);

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
	        Dialog.dismiss();
	    }
	}
}
