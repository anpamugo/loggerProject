package com.log.refactor.app.decorator.logger;

import com.log.refactor.app.decorator.LogDecorator;
import com.log.refactor.app.decorator.Loggeable;
import com.log.refactor.app.util.MessageType;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class LogConsole extends LogDecorator {

    private StreamHandler handler;
    @Autowired
    @Qualifier("delegate")
    private Loggeable loggeable;

    @Autowired
    public LogConsole(Loggeable loggeable) {
    	this.loggeable = loggeable;
        logger.setUseParentHandlers(false);
    }

    private StreamHandler getHandler() {
        //if (this.handler == null) {
            try {
                this.handler = new ConsoleHandler();                
            } catch (SecurityException ex) {
                Logger.getLogger(LogConsole.class.getName()).log(Level.SEVERE, null, ex);
            }
        //}
        return handler;
    }

    @Override
    public void logMessage(String messageText, MessageType messageType) throws Exception {
        loggeable.logMessage(messageText, messageType);
        getHandler();
        logger.addHandler(handler);
        if (message != null && logger != null) {
            logger.log(level, message);
            handler.flush();
        }
        logger.removeHandler(handler);
        handler.close();        
    }

}
