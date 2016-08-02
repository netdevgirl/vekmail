package veklia.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * @ClassName: Email
 * @Description: 邮件实体类。
 * @author veklia
 * @date 2016年7月25日
 *
 */
public class Email implements Serializable {
	static Logger logger = Logger.getLogger(Email.class);

	private static final long serialVersionUID = 1L;
	// SendEmail.class.getClassLoader().getResource("mailTemplet.tpl").getFile();
	public final static String EMAIL_FILE = Email.class.getClassLoader().getResource("email.properties").getFile();// 默认properties位置

	public static String EMAIL_PROTOCOL;//邮件协议
	public static String EMAIL_SERVER_HOST;// host服务器
	public static String EMAIL_SERVER_PORT;// 端口
	public static String FROM;// 发件人

	public static String USER_NAME;
	public static String PASSWORD;//一般情况下为邮箱密码。有部分需要使用授权码，如126,163邮箱，则这里的值是授权码。
	public static boolean VALIDATE;// 是否需要验证
	public static String CHARSET;//HTML内容邮件的字符集编码。

	public String[] tos = properties.getProperty("to") == null || properties.getProperty("to").equals("") ? null :properties.getProperty("to").split(",");;// 收件人
	public String[] ccs = properties.getProperty("cc") == null || properties.getProperty("cc").equals("") ? null : properties.getProperty("cc").split(",");;// 抄送
	public String[] bccs = properties.getProperty("bcc") == null || properties.getProperty("bcc").equals("") ? null : properties.getProperty("bcc").split(",");;// 密送

	public String emailSubject;// 邮件主题
	public String emailContent;// 邮件内容
	public String[] files;// 附件集

	public static Properties properties = new Properties();
	//初始化配置文件
	static {
		logger.info("vekmail:初始化配置文件信息。-" + new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSSS").format(Calendar.getInstance().getTime()));
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(EMAIL_FILE));
			properties.load(fileInputStream);
			boolean check = true;
			//邮件协议,默认为stmp
			EMAIL_PROTOCOL = properties.getProperty("email_protocol");
			if (EMAIL_PROTOCOL == null || EMAIL_PROTOCOL.equals("")) {
				EMAIL_PROTOCOL = "stmp";
			}
			EMAIL_SERVER_HOST = properties.getProperty("email_server_host");
			if (EMAIL_SERVER_HOST == null || EMAIL_SERVER_HOST.equals("")) {
				logger.error("vekmail : email_server_host is null");
				check = false;
			}
			EMAIL_SERVER_PORT = properties.getProperty("email_server_post");
			if (EMAIL_SERVER_PORT == null || EMAIL_SERVER_HOST.equals("")) {
				logger.error("vekmail : email_server_post is null");
				check = false;
			}
			// 是否验证:默认为true
			VALIDATE = properties.getProperty("validate") == null ? true : Boolean.parseBoolean(properties.getProperty("validate"));
			USER_NAME = properties.getProperty("user_name");
			if (USER_NAME == null || USER_NAME.equals("")) {
				logger.error("vekmail : user_name is null");
				check = false;
			}
			PASSWORD = properties.getProperty("password");
			if (PASSWORD == null || PASSWORD.equals("")) {
				logger.error("vekmail : password is null");
				check = false;
			}
			FROM = properties.getProperty("from");
			if (FROM == null || FROM.equals("")) {
				logger.error("vekmail : from is null");
				check = false;
			}
			
			CHARSET = properties.getProperty("charset");

			if (!check) {
				new Exception("邮件配置信息不全。");
				logger.error("邮件配置信息不全。");
			}
		} catch (IOException e) {
			logger.error("vekmail:初始化配置文件时出错！-"+new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSSS").format(Calendar.getInstance().getTime()));
			e.printStackTrace();
		}
	}

	// 打印信息。
	{
		logger.info("vekmai:邮件默认 收件人  " + Arrays.toString(tos) + "，抄送" + Arrays.toString(ccs) + "。");
		if( bccs != null ){
			logger.info("vekmai:邮件默认 密送" + Arrays.toString(bccs) + "。");
		}
	}
	
	public Email() {
		super();
	}

	public Email(String emailSubject, String emailContent) {
		super();
		this.emailSubject = emailSubject;
		this.emailContent = emailContent;
	}

	public Email(String emailSubject, String emailContent, String[] files) {
		super();
		this.emailSubject = emailSubject;
		this.emailContent = emailContent;
		this.files = files;
	}

	public Email(String[] tos, String emailSubject, String emailContent) {
		super();
		this.tos = tos;
		this.emailSubject = emailSubject;
		this.emailContent = emailContent;
	}

	public Email(String[] tos, String[] ccs, String emailSubject, String emailContent) {
		super();
		this.tos = tos;
		this.ccs = ccs;
		this.emailSubject = emailSubject;
		this.emailContent = emailContent;
	}

	public Email(String[] tos, String[] ccs, String emailSubject, String emailContent, String[] files) {
		super();
		this.tos = tos;
		this.ccs = ccs;
		this.emailSubject = emailSubject;
		this.emailContent = emailContent;
		this.files = files;
	}

	public Email(String[] tos, String[] ccs, String[] bccs, String emailSubject, String emailContent, String[] files) {
		super();
		this.tos = tos;
		this.ccs = ccs;
		this.bccs = bccs;
		this.emailSubject = emailSubject;
		this.emailContent = emailContent;
		this.files = files;
	}

	public String[] getTos() {
		return tos;
	}

	public void setTos(String[] tos) {
		if( this.tos == null )
			this.tos = tos;
		else{
			List<String> temp = new LinkedList<String>(Arrays.asList(this.tos));
			temp.addAll(Arrays.asList(tos));
			this.tos = (String[]) temp.toArray();
		}
	}

	public String[] getCcs() {
		return ccs;
	}

	public void setCcs(String[] ccs) {
		if( this.ccs == null )
			this.ccs = ccs;
		else{
			List<String> temp = new LinkedList<String>(Arrays.asList(this.ccs));
			temp.addAll(Arrays.asList(ccs));
			this.ccs = (String[])temp.toArray();
		}
	}

	public String[] getBccs() {
		return bccs;
	}

	public void setBccs(String[] bccs) {
		if( this.bccs == null )
			this.bccs = bccs;
		else{
			List<String>  temp = new LinkedList<String>(Arrays.asList(this.bccs));
			temp.addAll(Arrays.asList(bccs));
			this.bccs = (String[])temp.toArray();
		}
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public String[] getFiles() {
		return files;
	}

	public void setFiles(String[] files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "Email [from=" + Email.FROM + ", tos=" + Arrays.toString(tos) + ", ccs=" + Arrays.toString(ccs) + ", bccs=" + Arrays.toString(bccs) + ", emailSubject=" + emailSubject + ", emailContent="
				+ emailContent + ", files=" + Arrays.toString(files) + "]";
	}

}
