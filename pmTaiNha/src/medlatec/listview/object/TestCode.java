package medlatec.listview.object;

import java.io.IOException;
import java.io.Serializable;

import org.apache.http.entity.SerializableEntity;

public class TestCode {
	
	 
	private String testCode = "";
	 private String testName = "";
	 private String price;
	 private String sid = "";
	 private int autoSID;
	 private String category = "";
	 private boolean isChecked = false;
	 
	 
	 private String range = "";
	 private String rangeF = "";
	 private boolean testHead;
	 private double lower;
	 private double higher;
	 private boolean bold;
	 private boolean profile;
	 private String type;
	 private String unit;
	 private int printOrder;
	 
	 //
	 
	 private String bllLower;
	 private String bllHigher;
	 private String bllLowerE;
	 private String bllHigherE;
	 private String testNameE;
	 private String criteria;
	 private String rowguid;
	 private int trangthai;
	 
	 
	 
	 public int getTrangthai() {
		return trangthai;
	}

	public void setTrangthai(int trangthai) {
		this.trangthai = trangthai;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public int getPrintOrder() {
		return printOrder;
	}

	public void setPrintOrder(int printOrder) {
		this.printOrder = printOrder;
	}

	public String getBllLower() {
		return bllLower;
	}

	public void setBllLower(String bllLower) {
		this.bllLower = bllLower;
	}

	public String getBllHigher() {
		return bllHigher;
	}

	public void setBllHigher(String bllHigher) {
		this.bllHigher = bllHigher;
	}

	public String getBllLowerE() {
		return bllLowerE;
	}

	public void setBllLowerE(String bllLowerE) {
		this.bllLowerE = bllLowerE;
	}

	public String getBllHigherE() {
		return bllHigherE;
	}

	public void setBllHigherE(String bllHigherE) {
		this.bllHigherE = bllHigherE;
	}

	public String getTestNameE() {
		return testNameE;
	}

	public void setTestNameE(String testNameE) {
		this.testNameE = testNameE;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	
	
	 

	 public String getRange() {
			return range;
		}

		public void setRange(String range) {
			this.range = range;
		}

		public String getRangeF() {
			return rangeF;
		}

		public void setRangeF(String rangeF) {
			this.rangeF = rangeF;
		}

		public boolean isTestHead() {
			return testHead;
		}

		public void setTestHead(boolean testHead) {
			this.testHead = testHead;
		}

		public double getLower() {
			return lower;
		}

		public void setLower(double lower) {
			this.lower = lower;
		}

		public double getHigher() {
			return higher;
		}

		public void setHigher(double higher) {
			this.higher = higher;
		}

		public boolean isBold() {
			return bold;
		}

		public void setBold(boolean bold) {
			this.bold = bold;
		}

		public boolean isProfile() {
			return profile;
		}

		public void setProfile(boolean profile) {
			this.profile = profile;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getAutoSID() {
		return autoSID;
	}

	public void setAutoSID(int autoSID) {
		this.autoSID = autoSID;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	
	}