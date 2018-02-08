package de.codecut.googledrive.quickanddirty;

import static de.codecut.googledrive.quickanddirty.AppConstants.APP_PROPERTIES;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Properties;

/***
 * Property File Parser for the various options that need to be configurable.
 * 
 * @author k.seyhan
 *
 */
public class AppProperties {

	private static Properties instance;

	private static String clientSecretFile;

	private AppProperties() {
	}

	/***
	 * Loads the configuration file or returns an instance of the loaded object.
	 * 
	 * @return returns an Property instance of the config file.
	 */
	public static Properties getInstance() {
		if (instance == null) {
			try {
				instance = new Properties();
				final InputStream in = new FileInputStream(new File(getJarDirectory() + APP_PROPERTIES));

				instance.load(in);
				in.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return instance;
	}

	/***
	 * invalidate the loaded properties incase the configfile option is used at the
	 * commandline.
	 */
	public static void invalidate() {
		instance = null;
	}

	/***
	 * getter for the configurable mailto address.
	 * 
	 * @return mailto address.
	 */
	public final static String getMailTo() {
		return AppProperties.getInstance().getProperty("mail.To");
	}

	/***
	 * getter for the configurable mailfrom address.
	 * 
	 * @return mailfrom address.
	 */
	public final static String getMailFrom() {
		return AppProperties.getInstance().getProperty("mail.From");
	}

	/***
	 * getter for the configurable mail subject string.
	 * 
	 * @return mail subject string.
	 */
	public final static String getMailSubject() {
		return AppProperties.getInstance().getProperty("mail.Subject");
	}

	/***
	 * getter for the configurable datastore path where the auth token will be saved
	 * at.
	 * 
	 * @return datastore path string.
	 */
	public final static String getDataStore() {
		return AppProperties.getInstance().getProperty("datastore");
	}

	/***
	 * getter for the configurable mail message string that will be infront of the
	 * webview link in the body of the mail.
	 * 
	 * @return mail message string.
	 */
	public final static String getMailMessage() {
		return AppProperties.getInstance().getProperty("mail.Message");
	}

	/***
	 * getter for the configurable google drive folder name the files get uploaded
	 * to.
	 * 
	 * @return google drive folder name string.
	 */
	public final static String getGDriveFolder() {
		return AppProperties.getInstance().getProperty("gdrive.folder");
	}

	/***
	 * getter for the configurable option to delete the source file after upload.
	 * 
	 * @return is source file to delete boolean.
	 */
	public final static boolean isDeleteAfterUpload() {
		return Boolean.parseBoolean(AppProperties.getInstance().getProperty("delete-after-upload"));
	}

	/***
	 * getter for the configurable option to send an email after a file got uploaded
	 * 
	 * @return is email to be send boolean.
	 */
	public final static boolean isSendEmail() {
		return Boolean.parseBoolean(AppProperties.getInstance().getProperty("send-email"));
	}

	/***
	 * getter for the path of client_secrets.json file that has to be prepared and
	 * downloaded from google to get an authorization.
	 * 
	 * @return path of client_secrets.json
	 */
	public static String getClientSecretFile() {
		return clientSecretFile;
	}

	/***
	 * setter for the path of client_secrets.json file that has to be prepared and
	 * downloaded from google to get an authorization.
	 * 
	 * @param clientSecretFile
	 */
	public static void setClientSecretFile(String clientSecretFile) {
		AppProperties.clientSecretFile = clientSecretFile;
	}

	/***
	 * return the path of the executable jar file.
	 * 
	 * @return return the path of the executable jar file.
	 */
	public static String getJarDirectory() {
		try {
			CodeSource codeSource = App.class.getProtectionDomain().getCodeSource();
			File jarFile = new File(codeSource.getLocation().toURI().getPath());

			return jarFile.getParentFile().getPath() + "/";
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
}
