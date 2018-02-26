package spirit.fitness.scanner.util;



import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;




public class EmailHelper {

	public static void sendMail(String items) 
	{

	   final String username = "geminih@spiritfitness.com";
       final String password = "$pirit3Ma1l";

       Properties props = new Properties();
       props.put("mail.smtp.auth", "true");
       props.put("mail.smtp.starttls.enable", "true");
       props.put("mail.smtp.host", "smtp.gmail.com");
       props.put("mail.smtp.port", "587");

       Session session = Session.getInstance(props,
               new javax.mail.Authenticator() {
                   protected PasswordAuthentication getPasswordAuthentication() {
                       return new PasswordAuthentication(username, password);
                   }
               });

       try {

    	   String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			
           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress("geminih@spiritfitness.com"));
           message.setRecipients(Message.RecipientType.TO,
                   InternetAddress.parse("gemini612gemini@gmail.com"));
           message.setSubject(timeStamp +" Receiving Container");
           message.setText(items);

           Transport.send(message);

           System.out.println("Mail sent succesfully!");

       } catch (MessagingException e) {
           throw new RuntimeException(e);
       }
    }
	
}
