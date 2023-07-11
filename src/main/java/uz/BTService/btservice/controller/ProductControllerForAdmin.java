package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.BTService.btservice.controller.convert.ProductConvert;
import uz.BTService.btservice.controller.dto.dtoUtil.FilterForm;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.controller.dto.request.ProductCreateRequestDto;
import uz.BTService.btservice.controller.dto.request.ProductUpdateRequestDto;
import uz.BTService.btservice.controller.dto.response.ProductResponseForAdminDto;
import uz.BTService.btservice.entity.ProductEntity;
import uz.BTService.btservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "This Product Controller manages product-related operations for administrators.")
public class ProductControllerForAdmin {

    private final ProductService service;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CONTEND_MANAGER','SUPER_ADMIN')")
    @Operation(summary = "Add Product", description = "This method is used to add a new product.")
    @PostMapping("/add")
    public HttpResponse<Object> addProduct(@RequestBody ProductCreateRequestDto productDto) {

        HttpResponse<Object> response = HttpResponse.build(false);

        ProductEntity product = ProductConvert.convertToEntity(productDto);
        boolean isSave = service.addObject(product,productDto.getCategoryId(),productDto.getAttachId());

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(isSave)
                .message(HttpResponse.Status.OK.name());

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CONTEND_MANAGER','SUPER_ADMIN')")
    @Operation(summary = "Get Product by ID (Admin)", description = "This method retrieves the product based on the ID provided to the administrator. It also contains closed data.")
    @GetMapping("/for-admin/get/{id}")
    public HttpResponse<Object> getProductIdForAdmin(@PathVariable Integer id) {

        HttpResponse<Object> response = HttpResponse.build(false);

        ProductEntity responseProduct = service.getObjectById(id);
        ProductResponseForAdminDto responseForAdminDto = ProductConvert.fromForAdmin(responseProduct);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(responseForAdminDto)
                .message(HttpResponse.Status.OK.name());

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CONTEND_MANAGER','SUPER_ADMIN')")
    @Operation(summary = "Get All Products (Admin)", description = "This method retrieves all products for administrators. It includes closed information.")
    @GetMapping("/for-admin/get/all")
    public HttpResponse<Object> getProductAllForAdmin() {HttpResponse<Object> response = HttpResponse.build(false);

        List<ProductEntity> productAllList = service.getAllObject();
        List<ProductResponseForAdminDto> productResponseForAdminDtoList = ProductConvert.fromForAdmin(productAllList);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(productResponseForAdminDtoList)
                .message(HttpResponse.Status.OK.name());

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CONTEND_MANAGER','SUPER_ADMIN')")
    @Operation(summary = "Get Products by Category ID (Admin)", description = "This method retrieves products by the provided category ID for administrators. It includes sensitive information.")
    @GetMapping("/for-admin/category/get/{id}")
    public HttpResponse<Object> getProductByCategoryIdForAdmin(@PathVariable Integer id) {

        HttpResponse<Object> response = HttpResponse.build(false);

        List<ProductEntity> responseEntityList = service.getObjectByCategoryId(id);
        List<ProductResponseForAdminDto> productResponseForAdminDtoList = ProductConvert.fromForAdmin(responseEntityList);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(productResponseForAdminDtoList)
                .message(HttpResponse.Status.OK.name());

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CONTEND_MANAGER','SUPER_ADMIN')")
    @Operation(summary = "This method for get", description = "Products deleted during the period")
    @PostMapping("/for-admin/deleted-product")
    public HttpResponse<Object> getProductTimeIntervalDeletedForAdmin(@RequestBody FilterForm filter) {

        HttpResponse<Object> response = HttpResponse.build(false);

        List<ProductEntity> responseEntityList = service.getDeletedProductsByDate(filter);
        List<ProductResponseForAdminDto> productResponseForAdminDtoList = ProductConvert.fromForAdmin(responseEntityList);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(productResponseForAdminDtoList)
                .message(HttpResponse.Status.OK.name());

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CONTEND_MANAGER','SUPER_ADMIN')")
    @Operation(summary = "This method for Delete", description = "This method Product delete")
    @PatchMapping("/update/{id}")
    public HttpResponse<Object> updateProduct(@PathVariable Integer id, @RequestBody ProductUpdateRequestDto productUpdateRequestDto) {

        HttpResponse<Object> response = HttpResponse.build(false);

        service.update(productUpdateRequestDto,id);

        return response
                    .code(HttpResponse.Status.OK)
                    .success(false)
//                    .body(true)
                    .message(HttpResponse.Status.OK.name());

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CONTEND_MANAGER','SUPER_ADMIN')")
    @Operation(summary = "This method for Delete", description = "This method Product delete")
    @DeleteMapping("/delete/{id}")
    public HttpResponse<Object> deleteProductId(@PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(false);

        service.delete(id);

        try {
            response
                    .code(HttpResponse.Status.OK)
                    .success(true)
                    .body(true)
                    .message(HttpResponse.Status.OK.name());
        } catch (Exception e) {
            response
                    .code(HttpResponse.Status.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .message(e.getMessage());
        }
        return response;
    }

}
