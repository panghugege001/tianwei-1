package dfh.model.bean.png;

/**
 * 注册用户
 * */
public class RegisterUser {
	private String ExternalUserId;
	private String Username;
	private String Nickname;
	private String Currency;
	private String Country;
	private String Birthdate;
	private String Registration;
	private String BrandId;
	private String Language;
	private String IP;
	private String Locked;
	private String Gender;
	
	public String getExternalUserId() {
		return ExternalUserId;
	}
	
	public void setExternalUserId(String externalUserId) {
		ExternalUserId = externalUserId;
	}
	
	public String getNickname() {
		return Nickname;
	}
	
	public void setNickname(String nickname) {
		Nickname = nickname;
	}
	
	public String getCurrency() {
		return Currency;
	}
	
	public void setCurrency(String currency) {
		Currency = currency;
	}
	
	public String getCountry() {
		return Country;
	}
	
	public void setCountry(String country) {
		Country = country;
	}
	
	public String getBirthdate() {
		return Birthdate;
	}
	
	public void setBirthdate(String birthdate) {
		Birthdate = birthdate;
	}
	
	public String getRegistration() {
		return Registration;
	}
	
	public void setRegistration(String registration) {
		Registration = registration;
	}
	
	public String getBrandId() {
		return BrandId;
	}

	public void setBrandId(String brandId) {
		BrandId = brandId;
	}

	public String getLanguage() {
		return Language;
	}
	
	public void setLanguage(String language) {
		Language = language;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getLocked() {
		return Locked;
	}

	public void setLocked(String locked) {
		Locked = locked;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}
}
