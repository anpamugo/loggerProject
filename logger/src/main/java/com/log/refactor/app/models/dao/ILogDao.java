package com.log.refactor.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.log.refactor.app.models.entity.Log;


public interface ILogDao extends CrudRepository<Log, Long>{
	
}
