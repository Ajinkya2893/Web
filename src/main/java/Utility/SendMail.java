package Utility;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.testng.annotations.Test;

public class SendMail{

	@Test
	public void sendMail() {

		final String username = "ajinkya.gangawane@pay1.in";
		final String password = "Ajiswap@2817";

		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.EnableSSL.enable","true");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ajinkya.gangawane@pay1.in"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("anand.bhatt@pay1.in"));
			message.addRecipients(Message.RecipientType.CC, 
					InternetAddress.parse("vijay.kadus@pay1.in"));
			message.setSubject("Pay1 Automation Web Reports");
			message.setText("PFA");

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setText(
					"Hello Team,\n\nBelow is Attached the latest Report run.\n\nPlease check for the same.\n\nThanks,\nAjinkya Gangawane");

			Multipart multipart = new MimeMultipart();

			multipart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();
			String file = Constants.DesPath;
			String fileName = "Pay1Web.zip";
			DataSource source = new FileDataSource(file);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			System.out.println("Sending");
			Transport.send(message);

			System.out.println("Done");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}