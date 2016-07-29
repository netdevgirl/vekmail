package test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import veklia.mail.Email;
import veklia.mail.SendEmail;

public class EmailTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEmail() throws Exception {
		Email email = new Email("mail-test", "包含有一个压缩包的两个附件<br/><table style='color: red;border:1px solid blue;'><tr><td>1</td><td>2</td></tr></table>", new String[] { "E:/test.txt", "E:/test.zip"});
//		Email email = new Email("E mail 5", "zip附件<br/><table style='color: red;'><tr><td>1</td><td>2</td></tr></table>");
//		SendEmail.sendEmail(email);
		SendEmail.sendHtmlEmail(email);
	}

}
