package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.BTService.btservice.constants.CategoryType;
import uz.BTService.btservice.controller.convert.CategoryConvert;
import uz.BTService.btservice.controller.dto.CategoryDto;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.controller.dto.request.CategoryCreateRequestDto;
import uz.BTService.btservice.controller.dto.request.CategoryUpdateRequestDto;
import uz.BTService.btservice.entity.CategoryEntity;
import uz.BTService.btservice.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "This controller manages the categories.")
public class CategoryController {

    private final CategoryService service;


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @Operation(summary = "Add Category", description = "This method adds a new category. If no parentId is provided, the added category will be considered as a parent category.")
    @PostMapping("/add")
    public HttpResponse<Object> addCategory(@RequestBody CategoryCreateRequestDto categoryDto) {

        HttpResponse<Object> response = HttpResponse.build(false);

            CategoryEntity category = CategoryConvert.convertToEntity(categoryDto);
            boolean categorySave = service.addObject(category);
            return response
                    .code(HttpResponse.Status.OK)
                    .success(true)
                    .body(categorySave)
                    .message(HttpResponse.Status.OK.name());

    }


    @PreAuthorize("permitAll()")
    @Operation(summary = "Get Category Tree", description = "This method retrieves the category along with its descendants in a tree structure based on the provided ID.")
    @GetMapping("/get/tree/{id}")
    public HttpResponse<Object> getCategoryIdTree(@PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(false);

        CategoryEntity category = service.getObjectByIdTree(id);
        if (category != null) {response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(category)
                .message(HttpResponse.Status.OK.name());
        } else {
            response
                    .code(HttpResponse.Status.NOT_FOUND)
                    .success(true)
                    .message(id + " is category not found!!!");
        }

        return response;
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Get Category by ID", description = "This method retrieves a category based on the provided ID without any tree structure.")
    @GetMapping("/get/{id}")
    public HttpResponse<Object> getCategoryId(@PathVariable Integer id) {

        HttpResponse<Object> response = HttpResponse.build(false);

        CategoryEntity category = service.getObjectById(id);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(CategoryConvert.fromNoTree(category))
                .message(HttpResponse.Status.OK.name());

    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Get All Categories", description = "This method retrieves all categories without any tree structure.")
    @GetMapping("/get/all")
    public HttpResponse<Object> getAllCategory() {
        HttpResponse<Object> response = HttpResponse.build(false);
        List<CategoryEntity> allCategory = service.getAllObject();
        List<CategoryDto> categoryNoTreeList = CategoryConvert.fromNoTree(allCategory);
        response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(categoryNoTreeList)
                .message(HttpResponse.Status.OK.name());


        return response;
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Get All Categories with Tree Structure", description = "This method retrieves all categories in a tree structure, starting from the root category and including all its descendants.")
    @GetMapping("/get/all-tree")
    public HttpResponse<Object> getAllTreeCategory() {
        HttpResponse<Object> response = HttpResponse.build(false);

        List<CategoryEntity> allCategoryTree = service.getAllObjectTree();
        List<CategoryDto> categoryTreeList = CategoryConvert.fromTree(allCategoryTree);
        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(categoryTreeList)
                .message(HttpResponse.Status.OK.name());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @Operation(summary = "Update Category by ID", description = "This method updates a category based on the provided ID.")
    @PatchMapping("/update/{id}")
    public HttpResponse<Object> update(@RequestBody CategoryUpdateRequestDto categoryDto, @PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(false);

        CategoryEntity category = CategoryConvert.convertToEntity(categoryDto);
        boolean isUpdate = service.updateObject(category,id);

        response.code(HttpResponse.Status.OK)
                .success(true)
                .body(isUpdate)
                .message(HttpResponse.Status.OK.name());

        return response;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @Operation(summary = "Get Categories by Type", description = "This method retrieves categories based on the category type.")
    @GetMapping("/category-type")
    public HttpResponse<Object> getByCategoryType() {
        HttpResponse<Object> response = HttpResponse.build(false);

        return response.code(HttpResponse.Status.OK)
                .success(true)
                .body(CategoryType.values())
                .message(HttpResponse.Status.OK.name());

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @Operation(summary = "Delete Category by ID", description = "This method deletes a category by its ID. If the category is a parent, its children are automatically deleted.")
    @DeleteMapping("/delete/{id}")
    public HttpResponse<Object> deleteCategory(@PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(false);

        service.delete(id);

        return response.code(HttpResponse.Status.OK)
                .success(true)
                .body(true)
                .message(HttpResponse.Status.OK.name());

    }
}
