package com.log.refactor.app.util;

public enum MessageType {
	
    MESSAGE("message",1), WARNING("warning",3), ERROR("error",2);
    
    private final String msgText;
    private final Integer dBTypeId;

    private MessageType(String msgText, Integer dBTypeId) {
        this.msgText = msgText;
        this.dBTypeId = dBTypeId;
    }

    public String getMsgText() {
        return msgText;
    }

    public Integer getdBTypeId() {
        return dBTypeId;
    }
}
