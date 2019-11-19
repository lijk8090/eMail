package cn.com.infosec;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;

public class SendEmail {

	public static void main(String[] args) {

		String username = "lijk@infosec.com.cn";
		String password = "********";

		String from = "lijk@infosec.com.cn";
		String to = "zhaoxy@infosec.com.cn";
		String cc = "zhangyinhua@infosec.com.cn"; // 抄送
		String bcc = "lihong@infosec.com.cn"; // 密抄

		String filename = "lib/javax.mail.jar";
		String subject = "Send Email";
		String message = "12345678";
		Date now = new Date();

		String protocol = "smtp";
		String host = "smtp.qiye.163.com";
		int port = 25;
		boolean ssl = true;
		boolean auth = true;

		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.starttls.enable", ssl);
		properties.put("mail.smtp.auth", auth);

		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		MimeMessage mime = new MimeMessage(session);

		try {

			mime.setFrom(new InternetAddress(from));
			mime.setReplyTo(InternetAddress.parse(from, false));

			mime.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			mime.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
			mime.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));

			mime.setSubject(subject);
			mime.setSentDate(now);

			Multipart multiPart = new MimeMultipart();

			BodyPart bodyPart = new MimeBodyPart();
			bodyPart.setText(message);
			multiPart.addBodyPart(bodyPart);

			BodyPart attachPart = new MimeBodyPart();
			DataSource source = new FileDataSource(filename);
			attachPart.setDataHandler(new DataHandler(source));
			attachPart.setFileName(filename);
			multiPart.addBodyPart(attachPart);

			mime.setContent(multiPart);

			SMTPTransport transport = (SMTPTransport) session.getTransport(protocol);
			transport.connect();
			transport.sendMessage(mime, mime.getAllRecipients());

			System.out.println("Response: " + transport.getLastServerResponse());
			transport.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

}
