package medlatec.listview.auth;

public class DocSo
{
	private String DocChuSo(String s)
	{
		
			if(s.equals("0"))
				return "";
				//break;
			else if(s.equals("1"))
				return "một";
				//break;
			else if(s.equals("2"))
				return "hai";
				//break;
			else if(s.equals("3"))
				return "ba";
				//break;
			else if(s.equals("4"))
				return "bốn";
				//break;
			else if(s.equals("5"))
				return "năm";
				//break;
			else if(s.equals("6"))
				return "sáu";
				//break;
			else if(s.equals("7"))
				return "bảy";
				//break;
			else if(s.equals("8"))
				return "tám";
				//break;
			else if(s.equals("9"))
				return "chín";
				//break;
			else
				return "lỗi";
				//break;
					
	}
	private String DocSoCoBan(String sotien)
	{
		
		
        if (sotien.length() == 0)
                return "";
            //break;
        else if (sotien.length() == 1)
                return DocChuSo(sotien);
            //break;
        else if (sotien.length() == 2)
        {
                if (sotien.substring(0, 1) == "1")
                {
                    if (sotien.substring(1, 2) == "5") return "mười lăm";
                    else
                        return "mười " + DocChuSo(sotien.substring(1, 2));
                }
                else
                {
                    if (sotien.substring(0, 1) == "0")
                        return DocChuSo(sotien.substring(1, 2));
                    else
                        if (sotien.substring(1, 2) == "1")
                                return DocChuSo(sotien.substring(0, 1)) + " mươi " + "mốt";
                            //break;
                        else if (sotien.substring(1, 2) == "4")
                                return DocChuSo(sotien.substring(0, 1)) + " mươi " + "tư";
                            //break;
                        else if (sotien.substring(1, 2) == "5")
                                return DocChuSo(sotien.substring(0, 1)) + " mươi " + "lăm";
                            //break;
                        else
                                return DocChuSo(sotien.substring(0, 1)) + " mươi " + DocChuSo(sotien.substring(1, 2));
                            //break;
                        
                }
        }
        else if (sotien.length() == 3)
        {
                if (sotien.substring(0, 1) == "0")
                    return DocSoCoBan(sotien.substring(1, 3));
                else
                    if ((sotien.substring(1, 2) == "0") && (sotien.substring(2, 3) != "0"))
                        return DocChuSo(sotien.substring(0, 1)) + " trăm linh " + DocChuSo(sotien.substring(2, 3));
                    else
                        return DocChuSo(sotien.substring(0, 1)) + " trăm " + DocSoCoBan(sotien.substring(1, 3));
        }
            //break;
        else //Mac dinh la be hon 6
        {
                byte dodai = (byte)sotien.length();
                String DocST1 = "", ST11 = sotien.substring(0, dodai - 3);
                String ST1 = CatChuoi(sotien.substring(0, dodai - 3));
                String ST2 = sotien.substring(dodai - 3, dodai);
                if (ST1 == "")
                {
                    if ((ST2.charAt(0) == '0') && (ST2.substring(1, 3) != "00"))
                    {
                        if (ST2.substring(1, 2) == "0")
                            return "không trăm linh " + DocSoCoBan(ST2.substring(1, 3));
                        else
                            return "không trăm " + DocSoCoBan(ST2.substring(1, 3));
                    }
                    else
                        return DocSoCoBan(ST2);
                }
                else
                {                        
                    if ((ST11.charAt(0) == '0') && (ST11.substring(1, 3) != "00"))
                    {
                        if (ST11.substring(1, 2) == "0")
                            DocST1 = "không trăm linh " + DocSoCoBan(ST1);
                        else
                            DocST1 = "không trăm " + DocSoCoBan(ST1);
                    }
                    else
                    {
                        DocST1 = DocSoCoBan(ST11);
                    }
                    if ((ST2.charAt(0) == '0') && (ST2.substring(1, 3) != "00"))
                    {
                        if (ST2.substring(1, 2) == "0")
                            return DocST1 + " nghìn không trăm linh " + DocSoCoBan(ST2.substring(1, 3));
                        else
                            return DocST1 + " nghìn không trăm " + DocSoCoBan(ST2.substring(1, 3));
                    }
                    else
                        return DocST1 + " nghìn " + DocSoCoBan(ST2);

                }
            //break; 
        }

	}        
	public String CatChuoi(String S)
	{

		String ST=S.trim();
		if(ST.length()>0) 
			while(ST.substring(0,1)=="0") 
			{
				StringBuilder sb = new StringBuilder(ST);
				ST = sb.delete(0, 1).toString();
				
				//ST=ST.Remove(0,1);
				if(ST=="") break;
			}
		byte dodai=(byte)ST.length();
		byte k=0;
		while(k<dodai)
		{
			if(ST.substring(k,k+1)!=",")
				k=(byte)(k+1);
			else
			{
				StringBuilder sb = new StringBuilder(ST);
				ST = sb.delete(k, k+1).toString();
				//ST = sb.toString();
				
				//ST=ST.Remove(k,1);
				dodai=(byte)(dodai-1);
			}
		}
		return ST;
	}
	public String DocSoTien(String sotien)
	{
		return DocSoTien(sotien,"");
	}
	public String DocSoTien(String sotien,String ngoaite)
	{
		String ST=CatChuoi(sotien);
		String doc;
		if(ST=="") doc="Không";
		else
		{
			byte dodai=(byte)ST.length();
			if(dodai<=6)
				doc=DocSoCoBan(ST);
			else
			{
				if(dodai<=9)			
					doc= DocSoCoBan(ST.substring(0,dodai-6))+" triệu "+ DocSoCoBan(ST.substring(dodai-6,dodai));
				else
				{
					String st1=DocSoCoBan(ST.substring(0,dodai-9))+" tỷ ";
					String st2=CatChuoi(ST.substring(dodai-9,(dodai-9) + 3));
					String st3=ST.substring(dodai-6,(dodai-6) + 6);
                    String Docst4="";
                    String st4=ST.substring(dodai- 9, (dodai- 9) + 3);
                    
                    if ((st4.charAt(0) =='0')&&(st4.substring(1, 3)!="00"))
                    {
                        if (st4.substring(1,2)=="0")
                            Docst4="không trăm linh "+DocSoCoBan(st2);
                        else
                            Docst4="không trăm "+DocSoCoBan(st2);
                    }
                    else
                        Docst4=DocSoCoBan(st4);
					if(st2!="") doc=st1+Docst4+" triệu "+DocSoCoBan(st3);
					else
						doc=st1+ DocSoCoBan(st3);
				}
			}
		}
		doc=doc.substring(0,1).toUpperCase()+doc.substring(1);
		
		
		if (ngoaite=="" || ngoaite=="VNĐ")
			return doc+" đồng chẵn";
		else
			return doc+" "+ngoaite;
	}
	public String DocSoTien(double tien)
	{
		//String sotien=(Convert.ToDouble(tien)).ToString();
		Double x = Double.parseDouble("" + tien);
		return DocSoTien(x.intValue() + "");		
		
		
	}
	public String DocSoTien(double tien, String ngte)
	{
		Double x = Double.parseDouble("" + tien);
		return DocSoTien(x.intValue() + "",ngte);			
		
		
	}
	public DocSo()
	{			
	}
}
