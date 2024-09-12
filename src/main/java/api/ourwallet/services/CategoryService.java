package api.ourwallet.services;

import api.ourwallet.domains.Category;
import api.ourwallet.domains.User;
import api.ourwallet.repositories.CategoryRepository;
import api.ourwallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;


    public Category createNewCategory(String email, Category category) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Category> alreadyExists = this.categoryRepository.findCategoryByName(category.getName());

        if(alreadyExists.isPresent()) {
            Category newCategory = alreadyExists.get();
            user.addCategory(newCategory);
            this.userRepository.save(user);
            return newCategory;
        }

        Category newCategory = this.categoryRepository.save(category);
        user.addCategory(newCategory);
        this.userRepository.save(user);
        return newCategory;
    }

    public void deleteCategoryById(Long id, String userEmail) {
        User user = this.userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        Optional<List<User>> usersWithCategory = this.userRepository.findUsersByCategories(List.of(category));
        for (int i = 0; i < user.getCategories().size(); i++) {
            if(user.getCategories().get(i).getId().equals(id)) {
                Category removed = user.getCategories().remove(i);
            }
        }
        if(usersWithCategory.get().size() <= 1 && !this.isStandardCategory(id)){
            this.categoryRepository.deleteById(id);
        }
        this.userRepository.save(user);
    }


    public List<Category> standardCategories() {
        Category c1 = this.categoryRepository.findById(1L).get();
        Category c2 = this.categoryRepository.findById(2L).get();
        Category c3 = this.categoryRepository.findById(3L).get();
        return List.of(c1,c2,c3);
    }

    public boolean isStandardCategory(Long id) {
        List<Category> standardCategories = this.standardCategories();

        for(Category category : standardCategories) {
            if(category.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


}
