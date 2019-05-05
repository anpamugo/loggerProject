package com.log.refactor.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.log.refactor.app.decorator.Loggeable;
import com.log.refactor.app.decorator.logger.LogConsole;
import com.log.refactor.app.decorator.logger.LogDataBase;
import com.log.refactor.app.decorator.logger.LogFile;
import com.log.refactor.app.models.dao.ILogDao;
import com.log.refactor.app.models.entity.LogForm;

@Controller
public class LogController {

	@Autowired
	private ILogDao logDao;
	
	@Autowired
	private Loggeable loggeable;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("title", "Message List");
		model.addAttribute("logList", logDao.findAll());
		return "listar";
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String crear(Model model) {
		LogForm logForm = new LogForm();
		logForm.setLogConsole(true);
		logForm.setLogFile(true);
		logForm.setLogDatabase(true);

		logForm.setLogError(true);
		logForm.setLogWarning(true);
		logForm.setLogMessage(true);

		model.addAttribute("title", "Log Register form");
		model.addAttribute("logForm", logForm);
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String save(@Valid LogForm logForm, BindingResult result) {
		if (result.hasErrors()) {			
			return "form";
		}
		Loggeable loggeable = createLogs(logForm);
		try {
			loggeable.logMessage(logForm.getMessage(), logForm.getMessageType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "form";
	}

	public Loggeable createLogs(LogForm logForm) {
		//Loggeable loggeable = new LogWriter();

		if (logForm.getLogConsole()) {
			loggeable = new LogConsole(loggeable);
		}
		if (logForm.getLogFile()) {
			loggeable = new LogFile(loggeable);
		}
		if (logForm.getLogDatabase()) {
			loggeable = new LogDataBase(loggeable, logDao);
		}

		Loggeable.configureMessageLevel(logForm.getLogError(), logForm.getLogWarning(), logForm.getLogMessage());
		return loggeable;
	}
}
