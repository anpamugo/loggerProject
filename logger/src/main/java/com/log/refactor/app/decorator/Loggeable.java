package com.log.refactor.app.decorator;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.log.refactor.app.util.MessageType;

public abstract class Loggeable {
    protected static Logger logger = Logger.getLogger("log.refactor");
    protected static Level level;
    protected static String message;
    protected static Integer idLevelMsg;

    protected static boolean errorLevelOn;
    protected static boolean warningLevelOn;
    protected static boolean messageLevelOn;
    
    public abstract void logMessage(String messageText,MessageType messageType) throws Exception;
    
    public static void configureMessageLevel(boolean isErrorLevelOn, boolean isWarningLevelOn, boolean isMessageLevelOn) {
        errorLevelOn = isErrorLevelOn;
        warningLevelOn = isWarningLevelOn;
        messageLevelOn = isMessageLevelOn;        
    }
    
}
