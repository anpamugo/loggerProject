package com.log.refactor.app.decorator.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.log.refactor.app.decorator.LogDecorator;
import com.log.refactor.app.decorator.Loggeable;
import com.log.refactor.app.models.dao.ILogDao;
import com.log.refactor.app.models.entity.Log;
import com.log.refactor.app.util.MessageType;


public class LogDataBase extends LogDecorator {
	@Autowired
	@Qualifier("delegate")
	private Loggeable loggeable;
	
	@Autowired
	private ILogDao logDao;
	
	@Autowired
    public LogDataBase(Loggeable loggeable,ILogDao logDao) {
    	this.loggeable = loggeable;
    	this.logDao = logDao;
    }
	
	@Override
	public void logMessage(String messageText, MessageType messageType) throws Exception {

		loggeable.logMessage(messageText, messageType);

		Log log = new Log();
		log.setMessage(message);
		log.setIdLevel(messageType.getdBTypeId());

		if (message != null) {
			logDao.save(log);
		}
	}
}
