package murach.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtilLocal {

    public static void sendMail(
            String to,
            String from,
            String subject,
            String body,
            boolean bodyIsHTML)
            throws MessagingException {

        // 1 - get a mail session

        Properties props = new Properties();

        props.put(
                "mail.transport.protocol",
                "smtp"
        );

        props.put(
                "mail.smtp.host",
                "smtp.gmail.com"
        );

        props.put(
                "mail.smtp.port",
                "587"
        );

        props.put(
                "mail.smtp.auth",
                "true"
        );

        props.put(
                "mail.smtp.starttls.enable",
                "true"
        );

        // Gmail của bạn
        final String username =
                "kiendang151@gmail.com";

        // App Password của Gmail
        final String password =
                "gqgo sfdk pwql jzwa";

        Session session =
                Session.getInstance(
                        props,
                        new javax.mail.Authenticator() {

                            @Override
                            protected PasswordAuthentication
                                    getPasswordAuthentication() {

                                return new PasswordAuthentication(
                                        username,
                                        password
                                );
                            }
                        }
                );

        session.setDebug(true);

        // 2 - create a message

        Message message =
                new MimeMessage(session);

        message.setSubject(subject);

        if (bodyIsHTML) {

            message.setContent(
                    body,
                    "text/html"
            );

        } else {

            message.setText(body);
        }

        // 3 - address the message

        Address fromAddress =
                new InternetAddress(from);

        Address toAddress =
                new InternetAddress(to);

        message.setFrom(fromAddress);

        message.setRecipient(
                Message.RecipientType.TO,
                toAddress
        );

        // 4 - send the message

        Transport.send(message);
    }
}