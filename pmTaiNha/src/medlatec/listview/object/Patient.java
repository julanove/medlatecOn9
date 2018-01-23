package medlatec.listview.object;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import medlatec.listview.PatientInformation;

import org.joda.time.DateTime;

import android.util.Log;

public class Patient  {
	
	 private String pid = "";
	 private String sid = "";
	 private String patientname = "";
	 private int age;
	 private String sex;
	 private String address = "";
	 private String doctorID = "";
	 private double sumpertage;
	 private int tiendilai;
	 private String diagnostic = "";
	 
	 

	 private String seq = "";
	 private String phone = "";
	 private String quan = "";
	 private String commend = "";
	 private int trangthai;
	 private String userI;
	 private String intime;
	 private String random;
	 private String email;
	 private int GG;
	 private double pos;
	 private double summoney;
	 private int tuvan;
	 
	 private String objectID = "";
	 //03-04
	 

	 
	 public String getObjectID() {
		return objectID;
	}
	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	// 20-02
	 private int reason;
	 private String sReason;
	 
	
	 
	 
	 public String getsReason() {
			return sReason;
		}
		public void setsReason(String sReason) {
			this.sReason = sReason;
		}
	 
	 public int getReason() {
			return reason;
		}
		public void setReason(int reason) {
			this.reason = reason;
		}
	 
	 public int getTuvan() {
		return tuvan;
	}
	public void setTuvan(int tuvan) {
		this.tuvan = tuvan;
	}
	public double getSummoney() {
		return summoney;
	}
	public void setSummoney(double summoney) {
		this.summoney = summoney;
	}
	public int getGG() {
		return GG;
	 }
	 public void setGG(int gG) {
	GG = gG;
	 }
	public double getPos() {
			return pos;
		}
	public void setPos(double pos) {
			this.pos = pos;
		}
	 
    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public String getIntime() {
		return intime;
	}
	public void setIntime(String intime) {
		this.intime = intime;
	}
	
	public String getUserI() {
		return userI;
	}
	public void setUserI(String userI) {
		this.userI = userI;
	}
	
	public int getTrangthai() {
		return trangthai;
	}
	public void setTrangthai(int trangthai) {
		this.trangthai = trangthai;
	}
	
	
	public String getCommend() {
		return commend;
	}
	public void setCommend(String commend) {
		this.commend = commend;
	}
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getQuan() {
		return quan;
	}
	public void setQuan(String quan) {
		this.quan = quan;
	}
	public int getTiendilai() 
    {
		return tiendilai;
	}
	public void setTiendilai(int tiendilai) {
		this.tiendilai = tiendilai;
	}
	 
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getPatientname() {
		return patientname;
	}
	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDoctorID() {
		return doctorID;
	}
	public void setDoctorID(String doctorID) {
		this.doctorID = doctorID;
	}
	public double getSumpertage() {
		return sumpertage;
	}
	public void setSumpertage(double sumpertage) {
		this.sumpertage = sumpertage;
	}
	public String getDiagnostic() {
		return diagnostic;
	}
	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}

	 public double finalMoney()
	 {
		 double result = 0;
		 
		 if (sumpertage == 0 == false)
		 {
			 double tienChietkhau = sumpertage;
			 
			 double xChietkhau = ((tienChietkhau/100) * summoney);
			 
			 result = (summoney - xChietkhau) + tiendilai;
		 }
		 else
		 {
			 result = (summoney + tiendilai) >= GG ? (summoney + tiendilai) - GG : 0;
		 }
		 
		 return result;
	 }
	
	
	}