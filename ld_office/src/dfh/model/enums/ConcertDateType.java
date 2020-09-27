package dfh.model.enums;

import java.text.ParseException;
import java.util.Date;

import dfh.utils.DateUtil;

public enum ConcertDateType
{
	  ROUND1("ROUND1", 1,"2018-04-02 00:00:00","2018-04-08 23:59:59","2018-04-03 00:00:00","2018-04-09 23:59:59"),
	  ROUND2("ROUND2", 2,"2018-04-09 00:00:00","2018-04-15 23:59:59","2018-04-10 00:00:00","2018-04-16 23:59:59"),  
	  ROUND3("ROUND3", 3,"2018-04-16 00:00:00","2018-04-22 23:59:59","2018-04-17 00:00:00","2018-04-23 23:59:59"),
	  ROUND4("ROUND4", 4,"2018-04-23 00:00:00","2018-04-29 23:59:59","2018-04-24 00:00:00","2018-04-30 23:59:59");  
	
	public static ConcertDateType getCode(Integer round) {
		ConcertDateType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			ConcertDateType type = p[i];
			if (type.getText().intValue()==round.intValue())
				return type;
		}
		return null;
	}
	
	public static Integer getCount(Date date) {
		try {
		ConcertDateType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			ConcertDateType type = p[i];
			Date start = DateUtil.fmtStandard(type.getStart());
			Date end=DateUtil.fmtStandard(type.getEnd());
			if (start.getTime()<=date.getTime()&&end.getTime()>=date.getTime()) 
				return type.getText();
		 }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public static Integer getXimaCount(Date date) {
		try {
		ConcertDateType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			ConcertDateType type = p[i];
			Date start = DateUtil.fmtStandard(type.getXimaStart());
			Date end=DateUtil.fmtStandard(type.getXimaEnd());
			if (start.getTime()<=date.getTime()&&end.getTime()>=date.getTime()) 
				return type.getText();
		 }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	private String code;

	private Integer text;
	
	private String start;
	
	private String end;
	
    private String ximaStart;
	
	private String ximaEnd;

	private ConcertDateType(String code, Integer text,String start,String end,String ximaStart,String ximaEnd) {
		this.code = code;
		this.text = text;
		this.start = start;
		this.end = end;
		this.ximaStart = ximaStart;
		this.ximaEnd = ximaEnd;
		
	}
	
	  public String getCode()
	  {
	    return this.code;
	  }
	
	  public Integer getText() {
	    return this.text;
	  }

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public String getXimaStart() {
		return ximaStart;
	}

	public String getXimaEnd() {
		return ximaEnd;
	}

	  
}
