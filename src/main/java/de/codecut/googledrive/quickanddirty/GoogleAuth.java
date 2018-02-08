package de.codecut.googledrive.quickanddirty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.java6.auth.oauth2.GooglePromptReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.gmail.GmailScopes;

/***
 * Class file to gain oauth authorization for a user at google services.
 * 
 * @author k.seyhan
 *
 */
public class GoogleAuth {

	static Credential credential;

	/** Directory to store user credentials for this application. */
	public static String DATA_STORE_DIR;

	/** Global instance of the {@link FileDataStoreFactory}. */
	static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	public static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * the configured "datastore" folder.
	 */
	static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_SEND, DriveScopes.DRIVE);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(-1);
		}
	}

	/***
	 * private constructor since we want to deal with a singleton
	 */
	private GoogleAuth() {
	}

	/***
	 * Authorize the user to the google services and return an instance of the authorized credentials.
	 * 
	 * @return returns an instance of the authorized credentials.
	 */
	public final static Credential getCredential() {
		if (credential == null) {
			authorize();
		}
		return credential;

	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @throws IOException
	 */
	private final static void authorize() {
		try {
			final InputStream in = new FileInputStream(new File(AppProperties.getJarDirectory() + AppProperties.getClientSecretFile()));
			final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
					JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY)
							.setAccessType("offline").build();

			// authorize
			credential = new AuthorizationCodeInstalledApp(flow, new GooglePromptReceiver()).authorize("user");
			System.out.println("Credentials saved to " + DATA_STORE_DIR);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/***
	 * Setter for the configurable "datastore" path.
	 * 
	 * @param path
	 */
	public final static void setDataStore(String path) {
		DATA_STORE_DIR = path;
		try {
			DATA_STORE_FACTORY = new FileDataStoreFactory(new java.io.File(DATA_STORE_DIR));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
