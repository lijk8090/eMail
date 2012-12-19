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

	private static void addAMessage(Multipart multiPart, String message) throws Exception {

		BodyPart bodyPart = new MimeBodyPart();
		bodyPart.setText(message);
		multiPart.addBodyPart(bodyPart);
	}

	private static void addAttachment(Multipart multiPart, String filename) throws Exception {

		DataSource source = new FileDataSource(filename);
		BodyPart bodyPart = new MimeBodyPart();

		bodyPart.setDataHandler(new DataHandler(source));
		bodyPart.setFileName(filename);

		multiPart.addBodyPart(bodyPart);
	}

	public static void main(String[] args) {

		String username = "lijk@infosec.com.cn";
		String password = "Ljk19881016";

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
			@Override
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

			addAMessage(multiPart, message);
			addAMessage(multiPart, message);

			addAttachment(multiPart, filename);
			addAttachment(multiPart, filename);

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
