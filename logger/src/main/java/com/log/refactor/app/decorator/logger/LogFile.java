package com.log.refactor.app.decorator.logger;

import com.log.refactor.app.decorator.LogDecorator;
import com.log.refactor.app.decorator.Loggeable;
import com.log.refactor.app.util.MessageType;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


public class LogFile extends LogDecorator {
	@Autowired
	@Qualifier("delegate")
	private Loggeable loggeable;
	
	private StreamHandler handler;
	private final String fileName;
	private final boolean appendFile;
	
	@Autowired
    public LogFile(Loggeable loggeable) {
    	this.loggeable = loggeable;        
        this.fileName = "C:\\temp\\pr\\logFileRefactor.txt";
		this.appendFile = true;
		logger.setUseParentHandlers(false);
    }
	
	private StreamHandler getHandler() {
		// if(this.handler == null){
		try {
			this.handler = new FileHandler(fileName, appendFile);
			this.handler.setFormatter(new SimpleFormatter());
		} catch (IOException | SecurityException ex) {
			Logger.getLogger(LogFile.class.getName()).log(Level.SEVERE, null, ex);
		}
		// }
		return handler;
	}

	@Override
	public void logMessage(String messageText, MessageType messageType) throws Exception {
		getHandler();
		loggeable.logMessage(messageText, messageType);
		logger.addHandler(handler);
		if (message != null && logger != null) {
			logger.log(level, message);
			handler.flush();
		}
		handler.close();
		logger.removeHandler(handler);
	}

}
