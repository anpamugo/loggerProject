package com.log.refactor.app.decorator;

import com.log.refactor.app.util.MessageType;
import java.util.logging.Level;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("delegate")
@Primary
public class LogWriter extends Loggeable {

    public LogWriter() {
        super();
    }

    public void calculateLevel(String messageText, MessageType messageType) throws Exception {
        messageText = messageText.trim();
        if (messageText == null || messageText.length() == 0) {
            return;
        }

        if (messageType == null) {
            throw new Exception("Error or Warning or Message must be specified");
        }
        switch (messageType) {
            case MESSAGE:
                message = messageLevelOn ? messageText : null;
                level = Level.INFO;
                
                break;
            case ERROR:
                message = errorLevelOn ? messageText : null;
                level = Level.SEVERE;
                break;
            case WARNING:
                message = warningLevelOn ? messageText : null;
                level = Level.WARNING;
                break;
            default:
                message = null;
                level = null;
                break;
        }
        idLevelMsg = messageType.getdBTypeId();
        
    }

    @Override
    public void logMessage(String messageText, MessageType messageType) throws Exception {
        calculateLevel(messageText, messageType);        
    }
}

