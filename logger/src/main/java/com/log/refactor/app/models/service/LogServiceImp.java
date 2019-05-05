package com.log.refactor.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.log.refactor.app.models.dao.ILogDao;
import com.log.refactor.app.models.entity.Log;

@Service
@Repository("LogServiceJPA")
public class LogServiceImp implements ILogService {
	@Autowired
	private ILogDao logDao;

	@Override
	@Transactional(readOnly = true)
	public List<Log> findAll() {
		return (List<Log>) logDao.findAll();
	}

	@Override
	@Transactional
	public void save(Log log) {
		logDao.save(log);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		logDao.deleteById(id);
	}

	@Override
	public Log findOne(Long id) {
		// TODO Auto-generated method stub
		return logDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public void deleteAll() {
		logDao.deleteAll();
	}

}
