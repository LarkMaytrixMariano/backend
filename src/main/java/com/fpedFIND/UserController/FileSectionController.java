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

import com.fpedFIND.Entity.FileSection;
import com.fpedFIND.Service.UserService;

@RestController
@CrossOrigin(origins = "*")

public class FileSectionController {

	
	@Autowired
	private UserService service;
	
	
	// THIS IS FOR CRUD OPERATION // CREATE, READ, UPDATE, AND DELETE
	
	@GetMapping("/section/Allsection")
	public List<FileSection> getAllSection(){
		return service.getAllSection();		
	}
	
	@PostMapping("/section/addnew")	
	public void addSection(@RequestBody FileSection section) {
		service.addSection(section);
	
	}
	
	@PutMapping("/section/{id}/edit")
	public void updateSection(@PathVariable("id") Integer id, @RequestBody FileSection section) {
		service.updateSection(section);

	}
	
	@DeleteMapping("/section/{id}/delete")
	public void deleteSection(@PathVariable("id") Integer id) {
		service.deleteSection(id);
	}
	
	//// END OF CRUD OPERATION ////
}
