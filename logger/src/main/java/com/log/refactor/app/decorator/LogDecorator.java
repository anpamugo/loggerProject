package com.log.refactor.app.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class LogDecorator extends Loggeable{
	
	
}
