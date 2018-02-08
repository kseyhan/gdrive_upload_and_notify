package de.codecut.googledrive.quickanddirty;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/***
 * Commandline handling.
 * 
 * @author k.seyhan
 *
 */
public class Cli {

	private String[] args = null;
	private Options options = new Options();

	private String uploadFile;

	/***
	 * Add the various options that we want to be configurable at the commandline.
	 * 
	 * @param args
	 */
	public Cli(String[] args) {

		this.args = args;

		options.addOption(Option.builder("f").longOpt("file").hasArg(true)
				.desc("File to upload.\nexample: -f /home/pi/uploadme.avi").required(true).argName("FILE").build());

		options.addOption(Option.builder("h").longOpt("help").desc("show this HELP.").required(false).build());

		options.addOption(Option.builder("c").longOpt("config").hasArg(true)
				.desc("config File.\nexample: -c /home/pi/application.properties").required(false).argName("FILE")
				.build());

		options.addOption(Option.builder("d").longOpt("datastore").hasArg(true)
				.desc("datastore Path.\nexample: -d /home/pi/.credentials/motion-uploader").required(false)
				.argName("PATH").build());

		options.addOption(Option.builder("s").longOpt("secret").hasArg(true)
				.desc("google secrets File.\nexample: -s /home/pi/client_secret.json").required(false).argName("FILE")
				.build());
	}

	/***
	 * Parse the commandline and set the various options in the code.
	 */
	public void parse() {
		CommandLineParser parser = new DefaultParser();

		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("h")) {
				help();
			}

			if (cmd.hasOption("f")) {
				uploadFile = cmd.getOptionValue("f");
			}

			if (cmd.hasOption("c")) {
				AppProperties.invalidate();
				AppConstants.APP_PROPERTIES = cmd.getOptionValue("c");
			}

			if (cmd.hasOption("d")) {
				GoogleAuth.setDataStore(cmd.getOptionValue("d"));
			} else {
				GoogleAuth.setDataStore(AppProperties.getDataStore());
			}

			if (cmd.hasOption("s")) {
				AppProperties.setClientSecretFile(cmd.getOptionValue("s"));
			} else {
				AppProperties.setClientSecretFile("client_secret.json");
			}

		} catch (ParseException e) {
			System.err.println(e.getMessage());
			help();
		}
	}

	/***
	 * Shows the command line help
	 */
	private void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.setOptionComparator(null);

		formater.printHelp("gdrive_upload_and_notify", options, true);
		System.exit(-1);
	}

	/***
	 * returns the path of the file to be uploaded to gdrive.
	 * 
	 * @return returns the path of the file to be uploaded to gdrive.
	 */
	public String getUploadFile() {
		return uploadFile;
	}
}