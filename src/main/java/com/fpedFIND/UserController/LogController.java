package com.fpedFIND.UserController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fpedFIND.Entity.Log;
import com.fpedFIND.Service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class LogController {

	@Autowired
	private UserService logService;

    public LogController(UserService logService) {
        this.logService = logService;
    }

    @PostMapping("/logs")
    public Log saveLog(@RequestBody Log log) {
        return logService.saveLog(log.getMessage());
    }
	
    @GetMapping("/logs")
    public List<Log> getAllLogs() {
        return logService.getAllLogs();
    }
}