package com.fpedFIND.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpedFIND.Entity.Folder;
import com.fpedFIND.Service.UserService;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/folders")
public class FolderController {
	
	 @Autowired
	    private UserService folderService;
	 	
	 	//@Cacheable(cacheNames = "allFiles")
	    @GetMapping
	    public List<Folder> getAllFolders() {
	        return folderService.getAllFolders();
	    }
	    
	    //ROOTFOLDER
	    @GetMapping("/rootfolders")
	    public ResponseEntity<List<Folder>> getRootFolders() {
	        List<Folder> folders = folderService.getRootFolders();
	        return new ResponseEntity<>(folders, HttpStatus.OK);
	    }

	    @GetMapping("/{id}")
	    public Folder getFolderById(@PathVariable Long id) {
	        return folderService.getFolderById(id);
	    }

	    @PostMapping("/{parentFolderId}")
	    public Folder createFolder(@RequestBody Folder folder, @PathVariable Long parentFolderId) {
	        return folderService.createFolder(folder, parentFolderId);
	    }
	    
	    @PostMapping ("/create")
	    public Folder createRootFolder(@RequestBody Folder folder) {
	        return folderService.createRootFolder(folder);
	    }


	    @PutMapping
	    public Folder updateFolder(@RequestBody Folder folder) {
	        return folderService.updateFolder(folder);
	    }

	    @DeleteMapping("/{id}")
	    public void deleteFolder(@PathVariable Long id) {
	        folderService.deleteFolder(id);
	    }
	    
	    @GetMapping("/name/{foldername}")
	    public List<Folder> getFoldersByName(@PathVariable String foldername) {
	        return folderService.getFoldersByName(foldername);
	    }
	    
	    @PutMapping("/{id}/{newFolderName}")
	    public Folder updateFolderName(@PathVariable Long id, @PathVariable String newFolderName) {
	        return folderService.updateFolderName(id, newFolderName);
	    }

	    // FOLDER CONTENTS
	    @GetMapping("/subfolders/{id}")
	    public List<Folder> getSubFolders(@PathVariable Long id) {
	        Folder parentFolder = folderService.getFolderById(id);
	        return folderService.getSubFolders(parentFolder);
	    }
	    
	    //SEARCH
	    @GetMapping("/search/folders")
	    public List<Folder> searchFolders(@RequestParam String term) {
	        return folderService.searchFolders(term);
	    }
}