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

public class TestCode_HuyetHoc extends Activity {

	DatabaseHandler myDbHelperMain;
	DatabaseHandlerTestCode myDbHelper;
	TextView tvAll;
	String[] testCodeSader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huyet_hoc);
		
		myDbHelper = new DatabaseHandlerTestCode(this);
		myDbHelper.initialise();
		
		myDbHelperMain = new DatabaseHandler(this);
		myDbHelperMain.initialise();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_code__huyet_hoc, menu);
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
	    private ProgressDialog Dialog = new ProgressDialog(TestCode_HuyetHoc.this);

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
        case R.id.tv13:
        	
        	tvAll = (TextView) findViewById(R.id.tv13);
        	chooseTestCode(tvAll, new String[] {"001TPT32",
        	"002WBC",         
			"003RBC",         
			"004Hb",          
			"005Hct",         
			"006MCV",         
			"007MCH",         
			"008MCHC",        
			"009RDW",         
			"010PLT",         
			"011MPV",         
			"012PCT",         
			"013PDW",         
			"014NEU%",        
			"015LYM%",        
			"016MONO%",       
			"017Eos%",        
			"018Baso%",       
			"020ALY%",        
			"020LIC%",        
			"020NEU#",        
			"021LYM#",        
			"021MONO#",       
			"022EOS#",        
			"023BASO#",      
			"024ALY#",        
			"025LIC#",        
			"027IMG%",        
			"028IML%",        
			"029IMM%",        
			"030IMG#",        
			"031IML#",        
			"032IMM#"});
        	break;
        	
        case R.id.tv11:
        	tvAll = (TextView) findViewById(R.id.tv11);
        	chooseTestCode(tvAll, new String[] { "021MONO#",
			"016MONO%",
			"021LYM#",
			"020NEU#",
			"015LYM%",
			"014NEU%",
			"013PDW",
			"012PCT",
			"011MPV",
			"010PLT",
			"009RDW",
			"008MCHC",
			"007MCH",
			"006MCV",
			"005Hct",
			"004Hb",
			"003RBC",
			"002WBC",
			"001TPTM18" });
        	break;
        	
        case R.id.tv12:
        	tvAll = (TextView) findViewById(R.id.tv12);
        	chooseTestCode(tvAll, new String[] { "001TPT22",
			"002WBC",
			"003RBC",
			"004Hb",
			"005Hct",
			"006MCV",
			"007MCH",
			"008MCHC",
			"009RDW",
			"010PLT",
			"011MPV",
			"012PCT",
			"013PDW",
			"014NEU%",
			"015LYM%",
			"016MONO%",
			"017Eos%",
			"018Baso%",
			"020NEU#",
			"021LYM#",
			"021MONO#",
			"022EOS#",
			"023BASO#" });
        	break;
        	
        	/// P 2
        	
        	
        	
        case R.id.tv21:
        	tvAll = (TextView) findViewById(R.id.tv21);
        	chooseTestCode(tvAll, new String[] { "001aNhommau" });
        	break;
        	
        case R.id.tv22:
        	tvAll = (TextView) findViewById(R.id.tv22);
        	chooseTestCode(tvAll, new String[] { "001bRh" });
        	break;
        	
        case R.id.tv23:
        	tvAll = (TextView) findViewById(R.id.tv23);
        	chooseTestCode(tvAll, new String[] { "332GELC" });
        	break;
        	
        
/// P 3
        	
        case R.id.tv31:
        	tvAll = (TextView) findViewById(R.id.tv31);
        	chooseTestCode(tvAll, new String[] { "033VSS",
			"033VSSh1",
			"033VSSh2" });
        	break;
        	
        	/// P 4
        	
        case R.id.tv41:
        	tvAll = (TextView) findViewById(R.id.tv41);
        	chooseTestCode(tvAll, new String[] { "034ACMD",
			"034BTS",
			"034CTC" });
        	break;
        	
        case R.id.tv42:
        	tvAll = (TextView) findViewById(R.id.tv42);
        	chooseTestCode(tvAll, new String[] { "170DDProtein",
			"170Epro",
			"170FAlb",
			"171AF1",
			"171AF11",
			"172AF2",
			"172AF21",
			"173Beta1",
			"173Beta11",
			"173Beta2",
			"173Beta21",
			"174GM",
			"174GM1",
			"175AGfree" });
        	break;
      
        	
/// P 5
        	
        case R.id.tv51:
        	tvAll = (TextView) findViewById(R.id.tv51);
        	chooseTestCode(tvAll, new String[] { "001HD",
			"002WBC",
			"003RBC",
			"004Hb",
			"005Hct",
			"006MCV",
			"007MCH",
			"008MCHC",
			"009RDW",
			"010PLT",
			"011MPV",
			"012PCT",
			"013PDW",
			"014NEU%",
			"015LYM%",
			"016MONO%",
			"017Eos%",
			"018Baso%",
			"018HCL",
			"020ALY%",
			"020LIC%",
			"020NEU#",
			"021LYM#",
			"021MONO#",
			"022EOS#",
			"023BASO#",
			"024ALY#",
			"025LIC#",
			"027IMG%",
			"028IML%",
			"029IMM%",
			"030IMG#",
			"031IML#",
			"032IMM#" });
        	break;
        	
        case R.id.tv52:
        	tvAll = (TextView) findViewById(R.id.tv52);
        	chooseTestCode(tvAll, new String[] { "040DDHST",
			"041DDHSTA1",
			"042DDHSTA2",
			"043DDHSTE",
			"044DDHSTF",
			"045DDHST",
			"045HBSZone",
			"046DDHS",
			"046DDHST" });
        	break;
        	
        
/// P 6
        	
        case R.id.tv61:
        	tvAll = (TextView) findViewById(R.id.tv61);
        	chooseTestCode(tvAll, new String[] { "063ANA" });
        	break;
        	
        case R.id.tv62:
        	tvAll = (TextView) findViewById(R.id.tv62);
        	chooseTestCode(tvAll, new String[] { "064Ds" });
        	break;
        	
        	
/// P 7
        	
        case R.id.tv71:
        	tvAll = (TextView) findViewById(R.id.tv71);
        	chooseTestCode(tvAll, new String[] { "062TB Har" });
        	break;
        	
        	// temp
        	case R.id.tv103:
        	tvAll = (TextView) findViewById(R.id.tv102);
        	chooseTestCode(tvAll,  new String[] { "062TB Har" });
        	
        	break;
        	//
        	
        case R.id.tv72:
        	tvAll = (TextView) findViewById(R.id.tv72);
        	chooseTestCode(tvAll, new String[] { "42MD" });
        	break;
        	
        case R.id.tv73:
        	tvAll = (TextView) findViewById(R.id.tv73);
        	chooseTestCode(tvAll, new String[] { "061Daythat" });
        	break;

        	
/// P8
        	
        case R.id.tv81:
        	tvAll = (TextView) findViewById(R.id.tv81);
        	chooseTestCode(tvAll, new String[] { "034DM3",
			"034DPTs",
			"034HPT%",
			"035INR" });
        	break;
      
/// P 9
        	
        case R.id.tv91:
        	tvAll = (TextView) findViewById(R.id.tv91);
        	chooseTestCode(tvAll, new String[] { "036TT" });
        	break;
        	
        case R.id.tv92:
        	tvAll = (TextView) findViewById(R.id.tv92);
        	chooseTestCode(tvAll, new String[] { "036APTT" });
        	break;
        	
        	
/// P 10
        	
        case R.id.tv101:
        	tvAll = (TextView) findViewById(R.id.tv101);
        	chooseTestCode(tvAll, new String[] { "039FIB" });
        	break;
        	
        case R.id.tv102:
        	tvAll = (TextView) findViewById(R.id.tv102);
        	chooseTestCode(tvAll, new String[] { "060Dimer" });
        	break;
        	
/// P 11
        	
        case R.id.tv111:
        	tvAll = (TextView) findViewById(R.id.tv111);
        	chooseTestCode(tvAll, new String[] { "050Coombs",
			"051Coombs TT",
			"052Coombs GT" });
        	break;
        	
        default:
        	break;
		}
		

	}

	
	    

}
