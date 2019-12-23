package task1;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author HP
 *
 *	As best practice the lines length must be 120 as maximum. 
 *	Remove variable initialized, because It's not used.
 *	We must to split the code in methods by task, in order to make It more readable.
 *		Instead of have all code in one block.
 *	It is advisable reference the Map variable with the type objects.
 *	The connection parameters (dbParams) could be null, so is necessary add validation
 *	I think the table Log_Values must be save the messageText instead of a boolean.
 *	The DB connection must be a Singleton, so We will avoid create multiple connections.
 *	After move DB connection to Its own class, the Map parameter could be replace by a String one.
 */
public class JobLogger {
	private static boolean logToFile;
	private static boolean logToConsole;
	private static boolean logMessage;
	private static boolean logWarning;
	private static boolean logError;
	private static boolean logToDatabase;
	private static String logFileFolder;
	private static Logger logger;

	public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, String logFileFolderParam) {
		logger = Logger.getLogger("MyLog");
		logError = logErrorParam;
		logMessage = logMessageParam;
		logWarning = logWarningParam;
		logToDatabase = logToDatabaseParam;
		logToFile = logToFileParam;
		logToConsole = logToConsoleParam;
		logFileFolder = logFileFolderParam;
	}
	
	public JobLogger() {
	}

	public void LogMessage(String messageText, boolean message, boolean warning, boolean error)
			throws Exception {
		messageText.trim();
		if (messageText == null || messageText.length() == 0) {
			return;
		}
		if (!logToConsole && !logToFile && !logToDatabase) {
			throw new Exception("Invalid configuration");
		}
		if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
			throw new Exception("Error or Warning or Message must be specified");
		}

		String l = null;
		
		if (error && logError) {
			l = l + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}

		if (warning && logWarning) {
			l = l + "warning " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}

		if (message && logMessage) {
			l = l + "message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}

		if (logToFile) {
			writeFile(messageText);
		}

		if (logToConsole) {
			writeConsole(messageText);
		}
		
		if (logToDatabase) {
			writeDB(messageText, message, error, warning);
		}
	}
	
	private void writeConsole(String messageText) {
		ConsoleHandler ch = new ConsoleHandler();
		logger.addHandler(ch);
		logger.log(Level.INFO, messageText);
	}
	
	private void writeFile(String messageText) throws IOException {
		if(logFileFolder!=null) {
			
			File logFile = new File(logFileFolder + "/logFile.txt");
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			FileHandler fh = new FileHandler(logFileFolder + "/logFile.txt");
			
			logger.addHandler(fh);
			logger.log(Level.INFO, messageText);
		}
	}
	
	public void writeDB(String text, boolean message, boolean error, boolean warning) throws SQLException {
		
		int t = 0;
		if (message && logMessage) {
			t = 1;
		}

		if (error && logError) {
			t = 2;
		}

		if (warning && logWarning) {
			t = 3;
		}
		
		DatabaseConnection instance = DatabaseConnection.getInstance();
		Connection connection = instance.getConnection();
		
		Statement stmt = connection.createStatement();
		
		stmt.executeUpdate("insert into Log_Values('" + message + "', " + String.valueOf(t) + ")");
		
	}
	
}
