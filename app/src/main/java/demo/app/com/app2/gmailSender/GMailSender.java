package demo.app.com.app2.gmailSender;

import android.util.Log;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import demo.app.com.app2.AppContext;
import demo.app.com.app2.database.dataSource.ClientInfoDataSource;
import demo.app.com.app2.helper.ApplicationHelper;
import demo.app.com.app2.helper.HelperInterface;
import demo.app.com.app2.models.ClientInfo;

import static demo.app.com.app2.constants.AppConstants.APP_CRASH_SENDER_EMAIL;
import static demo.app.com.app2.constants.AppConstants.APP_CRASH_SENDER_PASSWORD;


/**
 * Created by root on 6/9/17.
 */

public class GMailSender extends javax.mail.Authenticator implements HelperInterface {

    private static final String TAG = GMailSender.class.getSimpleName();
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    private Multipart _multipart = new MimeMultipart();

    private ClientInfoDataSource clientInfoDataSource;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender(final String user, final String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");

        props.setProperty("mail.smtp.quitwait", "false");
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        session = Session.getDefaultInstance(props, this);

        clientInfoDataSource = new ClientInfoDataSource(AppContext.getInstance());

    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body,
                                      String sender, String recipients, String filename, List<ClientInfo> clientInfoList) throws Exception {

        //without attachment code

        try {


        MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler((javax.activation.DataSource) new ByteArrayDataSource(
                body.getBytes(), "text/plain"));
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);

        message.setDataHandler(handler);

        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(
                    recipients));
        Transport.send(message);


        clientInfoDataSource.open();

        for(ClientInfo info : clientInfoList){
            info.setClientInfoStaus(CLIENT_INFO_S);

            long updateId = clientInfoDataSource.updateClientInfo(info);
        }

        clientInfoDataSource.close();

        Log.e(TAG, "MAIL SENT");


        // Without Attchment End


/*        // with attachment code


        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.stmp.user", APP_CRASH_SENDER_EMAIL);

        //To use TLS
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.password", APP_CRASH_SENDER_PASSWORD);
        //To use SSL
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(APP_CRASH_SENDER_EMAIL, APP_CRASH_SENDER_PASSWORD);
                    }
                });

        session = Session.getInstance(props, this);

      //  Session session = Session.getDefaultInstance(props, null);

        String to = recipients;
        String from = sender;

        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(from));
            if (recipients.indexOf(',') > 0) {

                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));
            }else {
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
                        to));

            }
            msg.setSubject(subject);
            msg.setText("Working fine..!");
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", 465, APP_CRASH_SENDER_EMAIL, APP_CRASH_SENDER_PASSWORD);


            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);

            multipart.addBodyPart(messageBodyPart);

            msg.setContent(multipart);

            transport.send(msg);

            clientInfoDataSource.open();

            for(ClientInfo info : clientInfoList){
                info.setClientInfoStaus(CLIENT_INFO_S);

                long updateId = clientInfoDataSource.updateClientInfo(info);
            }

            clientInfoDataSource.close();

            Log.e(TAG, "MAIL SENT");

            // with attachment end*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }


    public class ByteArrayDataSource implements DataSource {
            private byte[] data;
            private String type;

            public ByteArrayDataSource(byte[] data, String type) {
                super();
                this.data = data;
                this.type = type;
            }

            public ByteArrayDataSource(byte[] data) {
                super();
                this.data = data;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContentType() {
                if (type == null)
                    return "application/octet-stream";
                else
                    return type;
            }

            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(data);
            }

            public String getName() {
                return "ByteArrayDataSource";
            }

            public OutputStream getOutputStream() throws IOException {
                throw new IOException("Not Supported");
            }
        }
}
