package veklia.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * @ClassName: Email 
 * @Description: 邮件实体类。包含邮件相关的属性配置。 
 * @author veklia 
 * @date 2016年7月25日 
 *
 */
public class Email implements Serializable {
	static Logger logger = Logger.getLogger(Email.class);
	
	private static final long serialVersionUID = 1L;
//	SendEmail.class.getClassLoader().getResource("mailTemplet.tpl").getFile();
	public final static String EMAIL_FILE = Email.class.getClassLoader().getResource("email.properties").getFile();//默认properties位置

	public static String EMAIL_SERVER_HOST;// host服务器
	public static String EMAIL_SERVER_PORT;// 端口
	public static String FROM;// 发件人
	public static List<String> TOS;// 收件人
	public static List<String> CCS;// 抄送
	public static List<String> MCS;//密送
	public static String EMAIL_SUBJECT;// 邮件主题
	public static String EMAIL_CONTENT;// 邮件内容

	public static String USER_NAME;
	public static String PASSWORD;
	public static boolean VALIDATE;// 是否需要验证

	/**
	 * 初始化配置
	 */
	static {
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(EMAIL_FILE));
			Properties properties = new Properties();
			properties.load(fileInputStream);
			EMAIL_SERVER_HOST = properties.getProperty("email_server_host");
			if( EMAIL_SERVER_HOST == null || EMAIL_SERVER_HOST.equals("") )
				logger.error("vekmail : email_server_host is null");
			EMAIL_SERVER_PORT = properties.getProperty("email_server_post");
			if( EMAIL_SERVER_PORT == null || EMAIL_SERVER_HOST.equals("") )
				logger.error("vekmail : email_server_post is null");
			USER_NAME = properties.getProperty("user_name");
			if( USER_NAME == null || USER_NAME.equals("") )
				logger.error("vekmail : user_name is null");
			PASSWORD = properties.getProperty("password");
			if( PASSWORD == null || PASSWORD.equals("") )
				logger.error("vekmail : password is null");
			FROM = properties.getProperty("from");
			if( FROM == null || FROM.equals("") )
				logger.error("vekmail : from is null");
			TOS = Arrays.asList(properties.getProperty("to").split(","));
			if( FROM == null || FROM.equals("") )
				logger.error("vekmail : from is null");
			CCS = properties.getProperty("cc")==null?null:Arrays.asList(properties.getProperty("cc").split(","));
			MCS = properties.getProperty("mc")==null?null:Arrays.asList(properties.getProperty("mc").split(","));

			//默认验证
			VALIDATE = properties.getProperty("validate")==null?true:Boolean.parseBoolean(properties.getProperty("validate"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: getProperties 
	 * @Description: 初始化邮件属性 
	 * @param @return  设定文件 
	 * @return Properties  返回类型 
	 * @throws
	 */
	public static Properties getProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", EMAIL_SERVER_HOST);
		properties.put("mail.smtp.port", EMAIL_SERVER_PORT);
		properties.put("mail.smtp.auth", VALIDATE);
		return properties;
	}

}
