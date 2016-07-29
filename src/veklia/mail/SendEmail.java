/**   
* @Title: SendEmail.java 
* @Package veklia.mail 
* @Description: TODO
* @author veklia   
* @date 2016年7月28日
* @version V1.0   
*/
package veklia.mail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

/**
 * @ClassName: SendEmail
 * @Description: 发送邮件接口
 * @author veklia
 * @date 2016年7月25日
 * 
 */
public class SendEmail {

	private static Logger logger = Logger.getLogger(SendEmail.class);

	/**
	 * 
	 * @Title: sendEmail 
	 * @Description: 发送普通(非html)内容邮件
	 * @param @param email  邮件实体 
	 * @return void  返回类型 
	 * @throws
	 */
	public static boolean sendEmail(Email email) {
		Session session = initSession();// 创建会话
		try {
			Message message = initMessage(session,email);//初始化message的配置/收发件抄送人/主题等.
			//发送普通邮件or带附件的邮件。
			if( email.getFiles() == null || email.getFiles().length < 1 )
				message.setText(email.getEmailContent());//设置消息的内容
			else{
				//构造附件邮件内容对象
				Multipart multipart = new MimeMultipart();
				//添加邮件内容部分
				BodyPart bodyPart = new MimeBodyPart();
				bodyPart.setText(email.getEmailContent());
				multipart.addBodyPart(bodyPart);
				//添加附件
				if( email.getFiles().length > 0 ){
					for( String file : email.getFiles() ){
						System.out.println(file);
						BodyPart attachmentPart = new MimeBodyPart();
						attachmentPart.setDataHandler(new DataHandler(new FileDataSource(file)));
						attachmentPart.setFileName(file);
						multipart.addBodyPart(attachmentPart);
					}
					logger.info("vekmail:添加了"+email.getFiles().length + "个附件。");
				}
				//为消息设置内容
				message.setContent(multipart);
			}
			message.saveChanges();
			transportSentMessage(session, message);//发送消息
			return true;
		} catch (MessagingException e) {
			logger.error("vekmail:邮件发送失败！" + e.getMessage() + "-" + new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSSS").format(Calendar.getInstance().getTime()));
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 
	 * @Title: sendHtmlEmail 
	 * @Description: 发送HTML内容的邮件。 
	 * @param @param email 邮件主类
	 * @param @param charSet HTML内容编码类型 
	 * @return void  返回类型 
	 * @throws
	 */
	public static boolean sendHtmlEmail(Email email){
		Session session = initSession();// 创建会话
		try {
			Message message = initMessage(session,email);//初始化message的配置/收发件抄送人/主题等.
			//发送普通邮件or带附件的邮件。
			if( email.getFiles() == null || email.getFiles().length < 1 )
				message.setContent(email.getEmailContent(), "text/html;charset=" +  Email.CHARSET);
			else{
				//构造邮件内容对象
				Multipart multipart = new MimeMultipart();
				//添加邮件内容部分
				BodyPart bodyPart = new MimeBodyPart();
				bodyPart.setContent(email.getEmailContent(), "text/html;charset=" + Email.CHARSET);
				multipart.addBodyPart(bodyPart);
				//添加附件
				if( email.getFiles().length > 0 ){
					for( String file : email.getFiles() ){
						System.out.println(file);
						BodyPart attachmentPart = new MimeBodyPart();
						attachmentPart.setDataHandler(new DataHandler(new FileDataSource(file)));
						attachmentPart.setFileName(file);
						multipart.addBodyPart(attachmentPart);
					}
					logger.info("vekmail:添加了"+email.getFiles().length + "个附件。");
				}
				//为消息设置内容
				message.setContent(multipart);
			}
			message.saveChanges();//保存邮件信息
			transportSentMessage(session,message);//发送
			return true;
		} catch (MessagingException e) {
			logger.error("vekmail:邮件发送失败！" + e.getMessage() + "-" + new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSSS").format(Calendar.getInstance().getTime()));
			e.printStackTrace();
			return false;
		}
	}

	/** 
	 * @Title: transportSentMessage 
	 * @Description: 发送邮件. 
	 * @param @param session
	 * @param @param message
	 * @param @throws NoSuchProviderException
	 * @param @throws MessagingException  设定文件 
	 * @return void  返回类型 
	 * @throws 
	 */
	private static void transportSentMessage(Session session, Message message) throws NoSuchProviderException, MessagingException {
		//发送邮件.
		logger.info("vekmail:开始发送邮件。-" + new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSSS").format(Calendar.getInstance().getTime()));
		Transport transport = session.getTransport(Email.EMAIL_PROTOCOL);
		transport.connect(Email.EMAIL_SERVER_HOST, Email.USER_NAME, Email.PASSWORD);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
		logger.info("vekmail:发送邮件完成。-" + new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSSS").format(Calendar.getInstance().getTime()));
	}

	/**
	 * @throws MessagingException 
	 * @throws AddressException  
	 * @Title: initMessage 
	 * @Description: 初始化消息的收发信息和主题 
	 * @param @param email  设定文件 
	 * @return void  返回类型 
	 * @throws 
	 */
	private static Message initMessage(Session session,Email email) throws AddressException, MessagingException {
		logger.info("vekmail:装载邮件内容。-" + new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSSS").format(Calendar.getInstance().getTime()));
		Message message = new MimeMessage(session);// 通过会话创建消息
		message.setFrom(new InternetAddress(Email.FROM));// 设置消息的发送人
		// 设置消息的接收人
		if (email.getTos() == null || email.getTos().length < 1) {
			logger.error("没有设置收件人！");
			new Exception("没有设置收件人！");
			return null;
		}
		message.addRecipients(RecipientType.TO, arrayToAddressArray(email.getTos()));
		//设置抄送人
		if( email.getCcs() != null && email.getCcs().length > 0 )
			message.addRecipients(RecipientType.CC, arrayToAddressArray(email.getCcs()));
		//设置密送人
		if( email.getBccs() != null && email.getBccs().length > 0 )
			message.addRecipients(RecipientType.BCC, arrayToAddressArray(email.getBccs()));
		message.setSubject(email.getEmailSubject());//设置消息主题
		message.setSentDate(new Date());//设置消息发送时间
		return message;
	}

	/** 
	 * @Title: initSession 
	 * @Description: 初始化会话 
	 * @param @return  设定文件 
	 * @return Session  返回类型 
	 * @throws 
	 */
	private static Session initSession() {
		Session session;
		if (Email.VALIDATE)
			session = Session.getDefaultInstance(getProperties(), new EmailAuthenticator(Email.USER_NAME, Email.PASSWORD));
		else
			session = Session.getDefaultInstance(getProperties());
		return session;
	}

	// 将地址的String 数组转为address数组.
	private static Address[] arrayToAddressArray(String[] list) throws AddressException {
		Address[] addresses = new Address[list.length];
		for (int i = 0; i < list.length; i++) {
			addresses[i] = new InternetAddress(list[i]);
		}
		return addresses;
	}

	// 创建properties
	private static Properties getProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", Email.EMAIL_SERVER_HOST);
		properties.put("mail.smtp.port", Email.EMAIL_SERVER_PORT);
		properties.put("mail.smtp.auth", Email.VALIDATE);
		return properties;
	}
}
