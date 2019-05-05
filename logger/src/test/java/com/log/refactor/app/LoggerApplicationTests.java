package com.log.refactor.app;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.log.refactor.app.controllers.LogController;
import com.log.refactor.app.decorator.LogWriter;
import com.log.refactor.app.decorator.Loggeable;
import com.log.refactor.app.decorator.logger.LogConsole;
import com.log.refactor.app.decorator.logger.LogDataBase;
import com.log.refactor.app.decorator.logger.LogFile;
import com.log.refactor.app.models.dao.ILogDao;
import com.log.refactor.app.util.MessageType;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebMvcTest(LogController.class)
public class LoggerApplicationTests {
	private final String fileName = "C:\\temp\\pr\\logFileRefactor.txt";
    private static Connection connection;
    private static Statement stmt;
    
    @MockBean
	private ILogDao logDao;
	
    @Autowired
	private Loggeable loggeable;
    
    
    @TestConfiguration
    static class LoggeableImplTestContextConfiguration {
  
        @Bean
        public Loggeable employeeService() {
            return new LogWriter();
        }
    }
    
    @Test
    public void testOnlyOneLogWriter() throws Exception {
        boolean logErrorOn = true;
        boolean logWarningOn = true;
        boolean logMessageOn = true;
        //Loggeable loggeable = new LogWriter();
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        Files.deleteIfExists(Paths.get(fileName));
        stmt.executeUpdate("delete FROM APP.Log");

        //Message
        MessageType messageType = MessageType.MESSAGE;
        boolean enableLogToTest = logMessageOn;
        writeFileLog(loggeable, messageType, enableLogToTest);

        //Warning
        messageType = MessageType.WARNING;
        enableLogToTest = logWarningOn;
        writeFileLog(loggeable, messageType, enableLogToTest);

        //Error
        messageType = MessageType.ERROR;
        enableLogToTest = logMessageOn;
        writeFileLog(loggeable, messageType, enableLogToTest);
        
    }

    @Test
    public void testLogMessageAllLevelsOn() throws Exception {
        boolean logErrorOn = true;
        boolean logWarningOn = true;
        boolean logMessageOn = true;
        //Loggeable loggeable = new LogWriter();
        //loggeable = new LogDataBase(loggeable,logDao);
        loggeable = new LogConsole(loggeable);
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        Files.deleteIfExists(Paths.get(fileName));
        stmt.executeUpdate("delete FROM APP.Log");
        MessageType messageType = MessageType.MESSAGE;
        boolean enableLogToTest = logMessageOn;
        writeLog(loggeable, messageType, enableLogToTest);
    }

    @Test
    public void testLogMessageAllLevelsOff() throws Exception {
        boolean logErrorOn = false;
        boolean logWarningOn = false;
        boolean logMessageOn = false;
        //Loggeable loggeable = new LogWriter();
        //loggeable = new LogDataBase(loggeable);
        loggeable = new LogConsole(loggeable);
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        stmt.executeUpdate("delete FROM APP.Log");
        Files.deleteIfExists(Paths.get(fileName));
        MessageType messageType = MessageType.MESSAGE;
        boolean enableLogToTest = logMessageOn;
        writeLog(loggeable, messageType, enableLogToTest);
    }

    @Test
    public void testLogMessageOn() throws Exception {
        boolean logErrorOn = false;
        boolean logWarningOn = true;
        boolean logMessageOn = false;
        //Loggeable loggeable = new LogWriter();
        //loggeable = new LogDataBase(loggeable);
        loggeable = new LogConsole(loggeable);
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        stmt.executeUpdate("delete FROM APP.Log");
        Files.deleteIfExists(Paths.get(fileName));

        //Message
        MessageType messageType = MessageType.MESSAGE;
        boolean enableLogToTest = logMessageOn;
        writeLog(loggeable, messageType, enableLogToTest);

        //Warning
        messageType = MessageType.WARNING;
        enableLogToTest = logWarningOn;
        writeLog(loggeable, messageType, enableLogToTest);

        //Error
        messageType = MessageType.ERROR;
        enableLogToTest = logMessageOn;
        writeLog(loggeable, messageType, enableLogToTest);
    }

    @Test
    public void testLogMessageOff() throws Exception {
        boolean logErrorOn = true;
        boolean logWarningOn = true;
        boolean logMessageOn = false;
        //Loggeable loggeable = new LogWriter();
        //loggeable = new LogDataBase(loggeable);
        loggeable = new LogConsole(loggeable);
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        stmt.executeUpdate("delete FROM APP.Log");
        Files.deleteIfExists(Paths.get(fileName));

        //Message        
        MessageType messageType = MessageType.MESSAGE;
        boolean enableLogToTest = logMessageOn;
        writeLog(loggeable, messageType, enableLogToTest);

        //Warning
        stmt.executeUpdate("delete FROM APP.Log");
        messageType = MessageType.WARNING;
        enableLogToTest = logWarningOn;
        writeLog(loggeable, messageType, enableLogToTest);

        //Error
        stmt.executeUpdate("delete FROM APP.Log");
        messageType = MessageType.ERROR;
        enableLogToTest = logErrorOn;
        writeLog(loggeable, messageType, enableLogToTest);
    }
    
    
    public void writeFileLog(Loggeable loggeable, MessageType messageType, boolean enableLogToTest) {

        try {
            String message = "this is my message" + " - " + messageType.getMsgText();
            Integer idMensage = messageType.getdBTypeId();
            String assertMessage = "Invalid Notification Log";
            loggeable.logMessage(message, messageType);
            assertTrue(assertMessage, validateFileLog(message, enableLogToTest));
            assertTrue(assertMessage, validateDatabaseLog(message, idMensage, false));            
            
        } catch (Exception ex) {
            Logger.getLogger(LoggerApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void writeLog(Loggeable loggeable, MessageType messageType, boolean enableLogToTest) {

        try {
            String message = "this is my message" + " - " + messageType.getMsgText();
            Integer idMensage = messageType.getdBTypeId();
            String assertMessage = "Invalid Notification Log";
            loggeable.logMessage(message, messageType);
            assertTrue(assertMessage, validateFileLog(message, enableLogToTest));
            assertTrue(assertMessage, validateDatabaseLog(message, idMensage, enableLogToTest));
        } catch (Exception ex) {
            Logger.getLogger(LoggerApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean validateFileLog(String msgToFind, boolean logEnable) {
        String writtenMsg = readFile(fileName);
        boolean contains = writtenMsg != null ? writtenMsg.contains(msgToFind) : false;
        return logEnable ? contains : !contains;
    }

    public boolean validateConsoleLog(String msgToFind, boolean logEnable) {
        String writtenMsg = readFile(fileName);
        boolean contains = writtenMsg != null ? writtenMsg.contains(msgToFind) : false;
        return logEnable ? contains : !logEnable;
    }

    public boolean validateDatabaseLog(String msgToFind, Integer idToComp, boolean logEnable) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM  APP.Log");
        boolean contains = false;
        while (rs.next()) {
            Integer id = rs.getInt("VALORMENSAJE");
            String message = rs.getString("MENSAJE");

            boolean compareId = id.equals(idToComp);
            boolean compareMessage = message.equals(msgToFind);
            if (compareId && compareMessage) {
                contains = true;
                break;
            }
        }
        return logEnable ? contains : !contains;
    }

    public String readFile(String file) {
        try (FileReader reader = new FileReader(file);
                BufferedReader br = new BufferedReader(reader)) {
            StringBuilder lineBf = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                lineBf.append(line);
            }
            return lineBf.toString();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            return null;
        }
    }

    public static void dBConect() {
        Map<String, String> dbParams = new HashMap<>();
        dbParams.put("userName", "app");
        dbParams.put("password", "root");
        dbParams.put("dbms", "derby");
        dbParams.put("serverName", "localhost");
        dbParams.put("portNumber", "1527/testDB");
        dbParams.put("databaseName", "APP");

        Properties connectionProps = new Properties();
        connectionProps.put("user", dbParams.get("userName"));
        connectionProps.put("password", dbParams.get("password"));

        try {
            connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
                    + ":" + dbParams.get("portNumber") + "/", connectionProps);
            stmt = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(LoggerApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
