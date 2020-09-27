package dfh.model;
 
import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/** * Users entity. @author MyEclipse Persistence Tools */
@Entity
@Table(name = "tokeninfo", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TokenInfo implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String token = UUID.randomUUID().toString();
	private String platform;
	private String loginname;
	private Date timestamp;

	public TokenInfo() {
	}

	public TokenInfo(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public static final class Builder {
		private Long id;
		private String token = UUID.randomUUID().toString();
		private String loginname;
		private String platform;
		private Date timestamp;

		private Builder() {
		}

		public static Builder aTokenInfo() {
			return new Builder();
		}

		public Builder withId(long id) {
			this.id = id;
			return this;
		}

		public Builder withToken(String token) {
			this.token = token;
			return this;
		}

		public Builder withLoginname(String loginname) {
			this.loginname = loginname;
			return this;
		}

		public Builder withTimestamp(Date timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder withPlatform(String platform) {
			this.platform = platform;
			return this;
		}

		public TokenInfo build() {
			TokenInfo tokenInfo = new TokenInfo();
			tokenInfo.setId(id);
			tokenInfo.setToken(token);
			tokenInfo.setLoginname(loginname);
			tokenInfo.setTimestamp(timestamp);
			tokenInfo.setPlatform(platform);
			return tokenInfo;
		}
	}
}
