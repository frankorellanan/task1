package test;

import static org.mockito.Mockito.doNothing;

import java.io.IOException;
import java.sql.Connection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import task1.DatabaseConnection;
import task1.JobLogger;

@RunWith(MockitoJUnitRunner.class)
public class JobLoggerTest {
	
	@Mock
	private DatabaseConnection mockConnection;
	
	@Mock
	private Connection connection;
	
	@InjectMocks
	private JobLogger logger;
	
	@Test
	public void LogMessageTestConsole() throws Exception {
		logger = new JobLogger(false, true, false, 
				true, true, true, null);
		
		logger.LogMessage("Console Error", true, true, true);	
		
	}
	
	@Test
	public void LogMessageTestFile() throws Exception {
		logger = new JobLogger(true, false, false, 
				true, true, true, "F:");
		
		logger.LogMessage("Console Error", true, true, true);
		
	}
	
	@Test(expected = IOException.class)
	public void LogMessageTestFileException() throws Exception {
		logger = new JobLogger(true, false, false, 
				true, true, true, "XX:");
		
		logger.LogMessage("Console Error", true, true, true);
		
	}
	
	@Test
	public void LogMessageTestDB() throws Exception {
		
		logger = Mockito.spy(new JobLogger(false, false, true, 
				true, true, true, null));
		doNothing().when(logger).writeDB(Mockito.anyString(), Mockito.anyBoolean(),
				Mockito.anyBoolean(), Mockito.anyBoolean());
		
		logger.LogMessage("Console Error", true, true, true);
		
	}
	
	@Test(expected = Exception.class)
	public void LogMessageTestDBException() throws Exception {
		logger = new JobLogger(false, false, true, 
				true, true, true, null);
				
		logger.LogMessage("Console Error", true, true, true);
		
	}
}
