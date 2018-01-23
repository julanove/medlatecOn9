package medlatec.listview.object;

public class Idk_Patient 
{
	private String SID;
	private String TestCode;
	private String Category;

	public Idk_Patient() {
		
	}
	
	public Idk_Patient(String sid, String patientName, String age) {
		this.SID = sid;
		this.TestCode = patientName;
		this.Category = age;

	}
	
	public String getSID() {
		return SID;
	}

	public void setSID(String sID) {
		SID = sID;
	}

	public String getTestCode() {
		return TestCode;
	}

	public void setTestCode(String patientName) {
		TestCode = patientName;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String age) {
		Category = age;
	}

	
}