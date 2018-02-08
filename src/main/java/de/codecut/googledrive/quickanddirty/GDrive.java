package de.codecut.googledrive.quickanddirty;

import static de.codecut.googledrive.quickanddirty.AppConstants.APPLICATION_NAME;
import static de.codecut.googledrive.quickanddirty.GoogleAuth.HTTP_TRANSPORT;
import static de.codecut.googledrive.quickanddirty.GoogleAuth.JSON_FACTORY;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/***
 * Upload Files to Google Drive.
 * 
 * @author k.seyhan
 *
 */
public class GDrive {

	/**
	 * Build and return an authorized Drive client service.
	 * 
	 * @return an authorized Drive client service
	 * @throws IOException
	 */
	private final static Drive getDriveService() throws IOException {
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleAuth.getCredential())
				.setApplicationName(APPLICATION_NAME).build();
	}

	/***
	 * Upload a file to Google Drive.
	 * 
	 * @param path
	 * @return a link to the uploaded file
	 */
	public final static String uploadFile(String path) {
		final String folderId = getFolderId();
		File fileMetadata = new File();
		fileMetadata.setName(getFilename(path));
		fileMetadata.setParents(Collections.singletonList(folderId));
		final java.io.File filePath = new java.io.File(path);
		if (!filePath.exists()) {
			System.out.println("File not found!");
			System.out.println(path);
			System.exit(-1);
		}
		final FileContent mediaContent = new FileContent("video/avi", filePath);
		final File file;
		try {
			file = getDriveService().files().create(fileMetadata, mediaContent).setFields("id, parents, webViewLink")
					.execute();
			System.out.println("Link: " + file.getWebViewLink());
			return file.getWebViewLink();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * Iterates thru the saved files at Google Drive to return a specific folderId.
	 * 
	 * @return The Folder ID of the configured folder from the configuration file.
	 */
	private final static String getFolderId() {
		// Build a new authorized API client service.
		final Drive service;
		try {
			service = getDriveService();
			final FileList result = service.files().list().setFields("nextPageToken, files(id, name)").execute();
			final List<File> files = result.getFiles();
			if (files == null || files.size() == 0) {
				System.out.println("Folder not found.");
			} else {
				for (File file : files) {
					// System.out.printf("%s (%s)\n", file.getName(), file.getId());
					if (file.getName().equals(AppProperties.getGDriveFolder())) {
						return file.getId();
					}
				}
				System.out.println("Folder not found.");
				System.exit(-1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * Extract filename from a full path
	 * 
	 * @param path
	 * @return Filename without the path
	 */
	private final static String getFilename(String path) {
		final int idx = path.lastIndexOf('/');
		return path.substring(idx + 1, path.length());
	}
}
