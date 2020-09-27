package app.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public interface DataDictionary {

	enum Deposit {
		
		DEPOSIT101("PT", "PT存送优惠", 1),
		DEPOSIT102("MG", "MG存送优惠", 2),
		DEPOSIT103("DT", "DT存送优惠", 3),
		DEPOSIT104("QT", "QT存送优惠", 4),
		DEPOSIT105("TTG", "TTG存送优惠", 5),
		DEPOSIT106("NT", "NT存送优惠", 6);
		
		public static List<Deposit> getSortValue() {
		
			List<Deposit> list = new ArrayList<Deposit>();
			
			Deposit[] d = Deposit.values();
			int len = d.length;
			
			for (int i = 0; i < len; i++) {
				
				list.add(d[i]);
			}
			
			Collections.sort(list, new Comparator<Deposit>() {

				public int compare(Deposit o1, Deposit o2) {
					
					return o1.order.compareTo(o2.order);
				}
				
			});
			
			return list;
		}
		
		private String code;
		private String value;
		private Integer order;
		
		private Deposit(String code, String value, Integer order) {
			
			this.code = code;
			this.value = value;
			this.order = order;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}
	}
	
	enum Xima {

		XIMA101("pttiger", "PT老虎机", 1),
		XIMA102("ttg", "TTG老虎机", 2),
		XIMA103("nt", "NT老虎机", 3),
		XIMA104("qt", "QT老虎机", 4),
		XIMA105("dt", "DT老虎机", 5),
		XIMA106("mg", "MG老虎机", 6);
		
		public static List<Xima> getSortValue() {
			
			List<Xima> list = new ArrayList<Xima>();
			
			Xima[] d = Xima.values();
			int len = d.length;
			
			for (int i = 0; i < len; i++) {
				
				list.add(d[i]);
			}
			
			Collections.sort(list, new Comparator<Xima>() {

				public int compare(Xima o1, Xima o2) {
					
					return o1.order.compareTo(o2.order);
				}
				
			});
			
			return list;
		}
		
		private String code;
		private String value;
		private Integer order;
		
		private Xima(String code, String value, Integer order) {
			
			this.code = code;
			this.value = value;
			this.order = order;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}
	}
	
	enum Bank {
	
		BANK101("工商银行", "工商银行", 1),
		BANK102("招商银行", "招商银行", 2),
		BANK103("上海农村商业银行", "上海农村商业银行", 3),
		BANK104("农业银行", "农业银行", 4),
		BANK105("建设银行", "建设银行", 5),
		BANK106("交通银行", "交通银行", 6),
		BANK107("民生银行", "民生银行", 7),
		BANK108("光大银行", "光大银行", 8),
		BANK109("兴业银行", "兴业银行", 9),
		BANK110("上海浦东银行", "上海浦东银行", 10),
		BANK111("广东发展银行", "广东发展银行", 11),
		BANK112("深圳发展银行", "深圳发展银行", 12),
		BANK113("中国银行", "中国银行", 13),
		BANK114("中信银行", "中信银行", 14),
		BANK115("邮政银行", "邮政银行", 15);
		
		public static List<Bank> getSortValue() {
			
			List<Bank> list = new ArrayList<Bank>();
			
			Bank[] d = Bank.values();
			int len = d.length;
			
			for (int i = 0; i < len; i++) {
				
				list.add(d[i]);
			}
			
			Collections.sort(list, new Comparator<Bank>() {

				public int compare(Bank o1, Bank o2) {
					
					return o1.order.compareTo(o2.order);
				}
				
			});
			
			return list;
		}

		private String code;
		private String value;
		private Integer order;
		
		private Bank(String code, String value, Integer order) {
			
			this.code = code;
			this.value = value;
			this.order = order;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}
	}
	
	enum Question {
		
		QUESTION101("1", "您最喜欢的明星名字？", 1),
		QUESTION102("2", "您最喜欢的职业？", 2),
		QUESTION103("3", "您最喜欢的城市名称？", 3),
		QUESTION104("4", "对您影响最大的人名字是？", 4),
		QUESTION105("5", "您就读的小学名称？", 5),
		QUESTION106("6", "您最熟悉的童年好友名字是？", 6);
		
		public static List<Question> getSortValue() {
			
			List<Question> list = new ArrayList<Question>();
			
			Question[] d = Question.values();
			int len = d.length;
			
			for (int i = 0; i < len; i++) {
				
				list.add(d[i]);
			}
			
			Collections.sort(list, new Comparator<Question>() {

				public int compare(Question o1, Question o2) {
					
					return o1.order.compareTo(o2.order);
				}
				
			});
			
			return list;
		}

		public static Boolean existKey(String code) {
		
			Question[] d = Question.values();
			int len = d.length;
			
			for (int i = 0; i < len; i++) {
			
				if (d[i].getCode().equalsIgnoreCase(code)) {
					
					return true;
				}
			}
			
			return false;
		}
		
		private String code;
		private String value;
		private Integer order;
		
		private Question(String code, String value, Integer order) {
			
			this.code = code;
			this.value = value;
			this.order = order;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}
	}

}