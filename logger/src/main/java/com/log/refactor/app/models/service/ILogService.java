package com.log.refactor.app.models.service;

import java.util.List;


import com.log.refactor.app.models.entity.Log;

public interface ILogService {
	
	public List<Log> findAll();
	
	public Log findOne(Long id);	
	
	public void save(Log cliente);	
	
	public void delete(Long id);
	
	public void deleteAll();
}
