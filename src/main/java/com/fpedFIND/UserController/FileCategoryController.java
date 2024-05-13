package com.fpedFIND.UserController;

import java.util.Comparator;
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

import com.fpedFIND.Entity.FileCategory;
import com.fpedFIND.Service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class FileCategoryController {


	@Autowired
	private UserService service;
	
	
	// THIS IS FOR CRUD OPERATION // CREATE, READ, UPDATE, AND DELETE
	@GetMapping("/category/main")
	public List<FileCategory> getMainCategories() {
	    return service.getMainCategories();
	}
	
	   @GetMapping("/category/Allcategory")
	    public List<FileCategory> getAllCategory(){
	        List<FileCategory> categories = service.getAllCategory();
	        
	        // Sort the categories alphabetically by categoryName
	        categories.sort(Comparator.comparing(FileCategory::getCategoryName));
	        
	        return categories;
	    }
	
	@PostMapping("/category/addnew")	
	public void addCategory(@RequestBody FileCategory category) {
		service.addCategory(category);
	
	}
	
	@PutMapping("/category/{id}/edit")
	public void updateCategory(@PathVariable("id") Integer id, @RequestBody FileCategory category) {
		service.updateCategory(category);

	}
	
	@DeleteMapping("/category/{id}/delete")
	public void deleteCategory(@PathVariable("id") Integer id) {
		service.deleteCategory(id);
	}
	
    // ADDING SUBCATEGORY TO A PARENT CATEGORY
    @PostMapping("/category/{parentId}/subcategory/add")
    public void addSubcategoryToCategory(@PathVariable("parentId") Integer parentId,
                                         @RequestBody FileCategory subcategory) {
        service.addSubcategoryToCategory(parentId, subcategory);
    }

    // GETTING ALL SUBCATEGORIES OF A PARENT CATEGORY
    @GetMapping("/category/{parentId}/subcategories")
    public List<FileCategory> getAllSubcategoriesOfCategory(@PathVariable("parentId") Integer parentId) {
        return service.getAllSubcategoriesOfCategory(parentId);
    }
    
    
    // Update a subcategory of a parent category
    @PutMapping("/category/{parentId}/subcategory/{subId}/edit")
    public void updateSubcategoryOfCategory(@PathVariable("parentId") Integer parentId,
                                            @PathVariable("subId") Integer subId,
                                            @RequestBody FileCategory subcategory) {
        service.updateSubCategoryOfMainCategory(parentId, subId, subcategory);
    }

    // Delete a subcategory of a parent category
    @DeleteMapping("/category/{parentId}/subcategory/{subId}/delete")
    public void deleteSubcategoryOfCategory(@PathVariable("parentId") Integer parentId,
                                            @PathVariable("subId") Integer subId) {
        service.deleteSubCategoryOfMainCategory(parentId, subId);
    }
    

	@GetMapping("/category/section/{section}")
	public List<FileCategory> getCategoriesBySection(@PathVariable("section") String section) {
	    return service.getCategoriesBySection(section);
	}
    
    
	//// END OF CRUD OPERATION ////
    
    
    
    
    
}