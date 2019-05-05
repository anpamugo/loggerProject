package com.log.refactor.app.controllers;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.log.refactor.app.decorator.Loggeable;
import com.log.refactor.app.decorator.logger.LogConsole;
import com.log.refactor.app.decorator.logger.LogDataBase;
import com.log.refactor.app.decorator.logger.LogFile;
import com.log.refactor.app.models.dao.ILogDao;
import com.log.refactor.app.models.entity.Log;
import com.log.refactor.app.util.MessageType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogControllerTests {
	@Autowired
	private ILogDao logDao;

	@Autowired
	private Loggeable loggeable;
	
	
	private final String fileName = "C:\\temp\\pr\\logFileRefactor.txt";
	private OutputStream logConsoleOut;
	private StreamHandler testLogHandler;
	
    @Test
    public void testOnlyOneLogWriter() throws Exception {
        boolean logErrorOn = true;
        boolean logWarningOn = true;
        boolean logMessageOn = true;
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        Files.deleteIfExists(Paths.get(fileName));
        logDao.deleteAll();

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
        loggeable = new LogDataBase(loggeable,logDao);
        loggeable = new LogConsole(loggeable);
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        Files.deleteIfExists(Paths.get(fileName));
        logDao.deleteAll();
        MessageType messageType = MessageType.MESSAGE;
        boolean enableLogToTest = logMessageOn;
        writeLog(loggeable, messageType, enableLogToTest);
    }

    @Test
    public void testLogMessageAllLevelsOff() throws Exception {
        boolean logErrorOn = false;
        boolean logWarningOn = false;
        boolean logMessageOn = false;
        loggeable = new LogDataBase(loggeable,logDao);
        loggeable = new LogConsole(loggeable);
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        logDao.deleteAll();
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
        loggeable = new LogDataBase(loggeable,logDao);
        loggeable = new LogConsole(loggeable);
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        logDao.deleteAll();
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
        loggeable = new LogDataBase(loggeable,logDao);
        loggeable = new LogConsole(loggeable);
        loggeable = new LogFile(loggeable);

        Loggeable.configureMessageLevel(logErrorOn, logWarningOn, logMessageOn);
        logDao.deleteAll();
        Files.deleteIfExists(Paths.get(fileName));

        //Message        
        MessageType messageType = MessageType.MESSAGE;
        boolean enableLogToTest = logMessageOn;
        writeLog(loggeable, messageType, enableLogToTest);

        //Warning
        logDao.deleteAll();
        messageType = MessageType.WARNING;
        enableLogToTest = logWarningOn;
        writeLog(loggeable, messageType, enableLogToTest);

        //Error
        logDao.deleteAll();
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
            Logger.getLogger(LogControllerTests.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LogControllerTests.class.getName()).log(Level.SEVERE, null, ex);
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
    	List<Log> logList = (List<Log>) logDao.findAll();
    	boolean contains = false;
    	
    	for(Log logDb: logList) {
    		Integer id = logDb.getIdLevel();
            String message = logDb.getMessage();

            boolean compareId = id.equals(idToComp);
            boolean compareMessage = message.equals(msgToFind);
            if (compareId && compareMessage) {
                contains = true;
                break;
            }
    	}
        return logEnable ? contains : !contains;
    }
    
    public void readConsole() {
    	//logConsoleOut = new Byte
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

}
