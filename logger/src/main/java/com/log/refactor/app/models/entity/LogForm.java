package com.log.refactor.app.models.entity;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.log.refactor.app.util.MessageType;

public class LogForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String message;

	private MessageType messageType;
	private Boolean logConsole;
	private Boolean logFile;
	private Boolean logDatabase;

	private Boolean logError;
	private Boolean logWarning;
	private Boolean logMessage;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Boolean getLogConsole() {
		return logConsole;
	}

	public void setLogConsole(Boolean logConsole) {
		this.logConsole = logConsole;
	}

	public Boolean getLogFile() {
		return logFile;
	}

	public void setLogFile(Boolean logFile) {
		this.logFile = logFile;
	}

	public Boolean getLogDatabase() {
		return logDatabase;
	}

	public void setLogDatabase(Boolean logDatabase) {
		this.logDatabase = logDatabase;
	}

	public Boolean getLogError() {
		return logError;
	}

	public void setLogError(Boolean logError) {
		this.logError = logError;
	}

	public Boolean getLogWarning() {
		return logWarning;
	}

	public void setLogWarning(Boolean logWarning) {
		this.logWarning = logWarning;
	}

	public Boolean getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(Boolean logMessage) {
		this.logMessage = logMessage;
	}
}
