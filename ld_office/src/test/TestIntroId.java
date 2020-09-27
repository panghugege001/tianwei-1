package test;

public class TestIntroId {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "admin";
		StringBuffer sbf = new StringBuffer();
		sbf.append("R_");
		for(char c : str.toCharArray()){
			sbf.append(Integer.toHexString(c));
		}
		System.out.println(sbf.toString());
		
	}

}
