package com.fpedFIND.UserController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fpedFIND.Data.StatusStatistics;
import com.fpedFIND.Entity.Status;
import com.fpedFIND.Service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class StatusController {

	@Autowired
	private UserService service;
	
	// THIS IS FOR CRUD OPERATION // CREATE, READ, UPDATE, AND DELETE
	
	
    @GetMapping("/statistics/status")
    public StatusStatistics getStatusStatistics() {
        StatusStatistics statistics = new StatusStatistics();

        // Fetch and calculate status counts
        statistics.setNewCount(service.getCountByStatusName("New"));
        statistics.setPendingCount(service.getCountByStatusName("Pending"));
        statistics.setCompletedCount(service.getCountByStatusName("Completed"));
        statistics.setArchivedCount(service.getCountByStatusName("Archived"));
        statistics.setOngoingCount(service.getCountByStatusName("Ongoing" ));

        return statistics;
    }
	

	
	@GetMapping("/status/Allstatus")
	public List<Status> getAllStatus(){
		return service.getAllStatus();		
	}
	
	@PostMapping("/status/addStatus")	
	public void addStatus(@RequestBody Status status) {
		service.addStatus(status);
	
	}
	
	@PutMapping("/status/{id}/edit")
	public void updateStatus(@PathVariable("id") Integer id, @RequestBody Status status) {
		service.updateStatus(status);

	}
	
	@DeleteMapping("/status/{id}/delete")
	public void deleteStatus(@PathVariable("id") Integer id) {
		service.deleteStatus(id);
	}
	
	//// END OF CRUD OPERATION ////
	
	
	
	
}
