package medlatec.listview.object;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


public class TestSerial implements KvmSerializable 
{
	
	private String name;
	private double money;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	
	
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
}
