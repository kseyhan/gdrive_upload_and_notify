# Google Drive Upload and Notify

Google Drive File Upload and Notify Commandline Java Application with the main purpose that its beeing used with linux motion daemon.

## Installation
Google setup:

Go to https://code.google.com/apis/console 
create a new application and enable the "Google Drive API" and "Gmail API" if you want to receive notification emails for your registered app.
download the client_secrets.json file from the Credentials Page of the app and place it besides the executable jar.

Login into your Google Drive Account.
Create a Folder where you want your files to be uploaded to.
Set the name of this folder in the "application.properties" File.

## Configuration:
Place the application.properties file from the repository besides the executable jar.
Adjust the settings in the file to your needs.
Create a Folder owned by the executing user of the jar where the authorisation tokens can be saved at.

Initial authentication:
Run the executable jar as follows:

java -jar gdrive_upload_and_notify-1.0.0-SNAPSHOT.jar -f /full/path/to/some/dummy/file.avi

after the first time this command is executed it will ask you to paste a link into a browser.
you'll receive a token from the google auth page that you have to enter into the terminal.
the client secrets will be setup into the configured "datastore" folder and the initial setup should be done.
after that the client will be able to refresh its token automagicaly by itself.


## Usage of the Commandline Application:
```
usage: gdrive_upload_and_notify -f <FILE> [-h] [-c <FILE>] [-d <PATH>] [-s <FILE>]
 -f,--file <FILE>        File to upload.
                         example: -f /home/pi/uploadme.avi
 -h,--help               show this HELP.
 -c,--config <FILE>      config File.
                         example: -c /home/pi/application.properties
 -d,--datastore <PATH>   datastore Path.
                         example: -d /home/pi/.credentials/motion-uploader
 -s,--secret <FILE>      google secrets File.
                         example: -s /home/pi/client_secret.json
```

## Configurable Options:
```
mail.To=<EMAIL> The Address you want to send your notifications to

mail.From=<EMAIL> The Adress youre sending from.. Most probably your gmail adress

mail.Subject=<STRING> The Subject of the Mail that youre going to receive

mail.Message=<STRING> The Body of the Email Message that will be prepended to the Web Preview Link of the file you uploaded.

datastore=<PATH> The path to the folder where the authorization secrets for google authentication are going to be saved at.

gdrive.folder=<STRING> The name of the folder at Google Drive youre going to upload your files into

delete-after-upload=<BOOLEAN> Should the Source Files be deleted after the upload 

send-email=<BOOLEAN> Do you want to receive email notifications.
```
