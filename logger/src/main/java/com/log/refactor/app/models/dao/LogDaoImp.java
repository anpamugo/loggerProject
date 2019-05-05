package com.log.refactor.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.log.refactor.app.models.entity.Log;

@Repository
public class LogDaoImp implements ILogDao{
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<Log> findAll() {
		// TODO Auto-generated method stub
		return em.createQuery("from Log").getResultList();
	}

	@Override
	@Transactional
	public void save(Log log) {
		em.persist(log);		
	}
	
}
