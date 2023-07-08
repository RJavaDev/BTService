package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.BTService.btservice.controller.convert.ProductConvert;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.controller.dto.response.ProductResponseForUserDto;
import uz.BTService.btservice.entity.ProductEntity;
import uz.BTService.btservice.service.ProductService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "This controller provides services for retrieving product open information.")
public class ProductController{

    private final ProductService service;


    @Operation(summary = "Get Product by ID", description = "This method retrieves product information based on the provided ID.")
    @GetMapping("/get/{id}")
    public HttpResponse<Object> getProductId(@PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(false);

        ProductEntity responseProduct = service.getObjectById(id);
        ProductResponseForUserDto responseForUserDto = ProductConvert.from(responseProduct);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(responseForUserDto)
                .message(HttpResponse.Status.OK.name());

    }


    @Operation(summary = "Get All Products", description = "This method retrieves all available products.")
    @GetMapping("/get/all")
    public HttpResponse<Object> getProductAll() {

        HttpResponse<Object> response = HttpResponse.build(false);

        List<ProductEntity> productAllList = service.getAllObject();
        List<ProductResponseForUserDto> responseForUserDtoList = ProductConvert.from(productAllList);

        response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(responseForUserDtoList)
                .message(HttpResponse.Status.OK.name());

        return response;
    }

    @Operation(summary = "Get Products by Category ID", description = "This method retrieves products based on the provided category ID.")
    @GetMapping("/get/category/{id}")
    public HttpResponse<Object> getProductByCategoryId(@PathVariable Integer id) {

        HttpResponse<Object> response = HttpResponse.build(false);

        List<ProductEntity> responseEntityList = service.getObjectByCategoryId(id);
        List<ProductResponseForUserDto> responseForUserDtoList = ProductConvert.from(responseEntityList);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(responseForUserDtoList)
                .message(HttpResponse.Status.OK.name());
    }


    @Operation(summary = "Search Products by Name", description = "This method searches for products based on the provided product name.")
    @GetMapping("/get/search-product/name/{productName}")
    public HttpResponse<Object> getProductNameSearch(@PathVariable String productName) {
        HttpResponse<Object> response = HttpResponse.build(false);

        List<ProductEntity> responseEntityList = service.getProductNameSearch(productName);
        List<ProductResponseForUserDto> responseProductList = ProductConvert.from(responseEntityList);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(responseProductList)
                .message(HttpResponse.Status.OK.name());

    }


    @Operation(summary = "Get Product by Name", description = "This method retrieves a product based on the provided product name.")
    @GetMapping("/get/name/{productName}")
    public HttpResponse<Object> getProductName(@PathVariable String productName) {
        HttpResponse<Object> response = HttpResponse.build(false);

        List<ProductEntity> responseEntityList = service.getProductName(productName);
        List<ProductResponseForUserDto> responseProductList = ProductConvert.from(responseEntityList);

        return response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(responseProductList)
                .message(HttpResponse.Status.OK.name());

    }
}
