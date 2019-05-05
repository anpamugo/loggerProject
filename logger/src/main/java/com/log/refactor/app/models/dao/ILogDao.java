package com.log.refactor.app.models.dao;

import java.util.List;

import com.log.refactor.app.models.entity.Log;

public interface ILogDao {
	
	public List<Log> findAll();
	
	public void save(Log log);
}
