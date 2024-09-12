package api.ourwallet.controllers;


import api.ourwallet.domains.Category;
import api.ourwallet.dtos.CreateCategoryRequest;
import api.ourwallet.services.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/categories")
@Transactional
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping("/create")
    public ResponseEntity createCategory(@RequestBody CreateCategoryRequest body) {
        try {
            Category category = this.categoryService.createNewCategory(body.email(), new Category(body));
            return ResponseEntity.ok().body("Category created successfully");
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id, @RequestBody CreateCategoryRequest body) {
        try {
            this.categoryService.deleteCategoryById(id, body.email());
            return ResponseEntity.ok().body("Category deleted successfully");
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
