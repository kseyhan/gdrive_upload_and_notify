package de.codecut.googledrive.quickanddirty;

import static de.codecut.googledrive.quickanddirty.AppConstants.APPLICATION_NAME;
import static de.codecut.googledrive.quickanddirty.GoogleAuth.HTTP_TRANSPORT;
import static de.codecut.googledrive.quickanddirty.GoogleAuth.JSON_FACTORY;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

/***
 * Send email messages thru gmail.
 * 
 * @author k.seyhan
 *
 */
public final class GMail {

	/**
	 * Send an email from the user's mailbox to its recipient.
	 *
	 * @param service
	 *            Authorized Gmail API instance.
	 * @param userId
	 *            User's email address. The special value "me" can be used to
	 *            indicate the authenticated user.
	 * @param emailContent
	 *            Email to be sent.
	 * @return The sent message
	 * @throws MessagingException
	 * @throws IOException
	 */
	public final static Message sendMessage(Gmail service, String userId, MimeMessage emailContent)
			throws MessagingException, IOException {
		Message message = createMessageWithEmail(emailContent);
		message = service.users().messages().send(userId, message).execute();

		System.out.println("Message id: " + message.getId());
		System.out.println(message.toPrettyString());
		return message;
	}

	/**
	 * Create a message from an email.
	 *
	 * @param emailContent
	 *            Email to be set to raw of message
	 * @return a message containing a base64url encoded email
	 * @throws IOException
	 * @throws MessagingException
	 */
	public static Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);
		final byte[] bytes = buffer.toByteArray();
		final String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}

	/**
	 * Create a MimeMessage using the parameters provided.
	 *
	 * @param to
	 *            email address of the receiver
	 * @param from
	 *            email address of the sender, the mailbox account
	 * @param subject
	 *            subject of the email
	 * @param bodyText
	 *            body text of the email
	 * @return the MimeMessage to be used to send email
	 * @throws MessagingException
	 */
	public final static MimeMessage createEmail(String to, String from, String subject, String bodyText)
			throws MessagingException {
		final Properties props = new Properties();
		final Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	/**
	 * Build and return an authorized Gmail client service.
	 * 
	 * @return an authorized Gmail client service
	 * @throws IOException
	 */
	public final static Gmail getGmailService() throws IOException {
		return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleAuth.getCredential())
				.setApplicationName(APPLICATION_NAME).build();
	}

}