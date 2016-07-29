# vekmail
java mail tools

1 创建邮件对象
Email email = new Email(主题,内容)/(主题,内容,附件)/(收件人,抄送人,主题内容,附件)/...
2 调用发送邮件
    SendEmail.sendEmail(email);//发送普通内容邮件(如果email对象有带文件,则发送附件.)
		SendEmail.sendHtmlEmail(email);//发送HTML格式的邮件(如果email对象有带文件,则发送附件.)
