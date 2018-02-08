package de.codecut.googledrive.quickanddirty;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.google.api.services.gmail.Gmail;

/***
 * Main method of the application.
 * 
 * @author k.seyhan
 *
 */
public class App {

	public static void main(String[] args) throws URISyntaxException {
		
		// parse commandline arguments
		Cli cli = new Cli(args);
		cli.parse();

		try {
			// upload file to gdrive and exit program if there was an error.
			String previewLink = GDrive.uploadFile(cli.getUploadFile());

			if (previewLink == null) {
				System.err.println("File Error.. Exiting");
				System.exit(-1);
			}

			// delete file if option is set
			if (AppProperties.isDeleteAfterUpload()) {
				File file = new File(cli.getUploadFile());
				if (!file.delete()) {
					System.err.println("Error on deleting file!");
				}
			}

			// send email if option is set
			if (AppProperties.isSendEmail()) {
				Gmail service = GMail.getGmailService();
				MimeMessage message = GMail.createEmail(AppProperties.getMailTo(), AppProperties.getMailFrom(),
						AppProperties.getMailSubject(), AppProperties.getMailMessage() + "\n\n" + previewLink);
				GMail.sendMessage(service, "me", message);
			}

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
