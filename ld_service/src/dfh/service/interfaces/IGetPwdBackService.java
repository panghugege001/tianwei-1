package dfh.service.interfaces;


public interface IGetPwdBackService {
	/**
	 * 根据邮箱找回密码
	 * @param name
	 * @param yxdz
	 * @return
	 */
	public String isUserByYx(String name,String yxdz);
	/**
	 * 根据电话找回密码
	 * @param name
	 * @param dh
	 * @return
	 */
	public String isUserByDh(String name,String dh,String pwd);
}
