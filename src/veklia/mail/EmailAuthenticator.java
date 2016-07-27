package veklia.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/**
 * 
 * @ClassName: EmailAuthenticator 
 * @Description: 权限验证类 
 * @author veklia 
 * @date 2016年7月27日 
 *
 */
public class EmailAuthenticator extends Authenticator{
	private String userName;
	private String password;
	
	public EmailAuthenticator(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName,password);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
