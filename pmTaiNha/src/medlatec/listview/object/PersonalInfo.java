package medlatec.listview.object;

public class PersonalInfo 
{
	private String name = "";
	private String mobile = "";
	private String phone = "";
	private String maLH = "";
	private String giohen1 = "";
	private String giohen2 = "";
	private String tinhtrang = "";
	private String gioitinh = "";
	private String namsinh = "";
	private String isSent = "";
	private String SID = "";
	
	private String yeucauXN = "";
	
	public String getYeucauXN() {
		return yeucauXN;
	}
	public void setYeucauXN(String yeucauXN) {
		this.yeucauXN = yeucauXN;
	}
	public String getGioitinh() {
		return gioitinh;
	}
	public String getSID() {
		return SID;
	}
	public void setSID(String sID) {
		SID = sID;
	}
	public void setGioitinh(String gioitinh) {
		this.gioitinh = gioitinh;
	}
	public String getNamsinh() {
		return namsinh;
	}
	public void setNamsinh(String namsinh) {
		this.namsinh = namsinh;
	}
	public String getIsSent() {
		return isSent;
	}
	public void setIsSent(String isSent) {
		this.isSent = isSent;
	}
	
	public String getTinhtrang() {
		return tinhtrang;
	}
	public void setTinhtrang(String tinhtrang) {
		this.tinhtrang = tinhtrang;
	}
	

	public String getGiohen1() {
		return giohen1;
	}
	public void setGiohen1(String giohen1) {
		this.giohen1 = giohen1;
	}
	public String getGiohen2() {
		return giohen2;
	}
	public void setGiohen2(String giohen2) {
		this.giohen2 = giohen2;
	}
	
	public String getMaLH() {
		return maLH;
	}
	public void setMaLH(String maLH) {
		this.maLH = maLH;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void SetName(String name){
	this.name = name;
	}
	public String GetName(){
	return this.name;
	}
	
	public void SetMobile(String mobile){
	this.mobile = mobile;
	}
	public String GetMobile(){
	return this.mobile;
	}
}