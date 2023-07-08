package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.BTService.btservice.controller.convert.OrderForProductConvert;
import uz.BTService.btservice.controller.dto.request.OrderForProductCreateDto;
import uz.BTService.btservice.controller.dto.response.OrderForProductResponseDto;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.controller.dto.request.OrderStatusUpdateDto;
import uz.BTService.btservice.entity.OrderForProductEntity;
import uz.BTService.btservice.service.OrderForProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-for-product")
@RequiredArgsConstructor
@Tag(name = "Product Orders", description = "This controller manages orders for products.")
public class OrderForProductController {

    private final OrderForProductService service;

    @PreAuthorize("permitAll()")
    @Operation(summary = "Add Order for Product", description = "This method adds a new order for a product.")
    @PostMapping("/add")
    public HttpResponse<Object> addOrderForProduct(@RequestBody OrderForProductCreateDto orderForProductCreateDto) {

        HttpResponse<Object> response = HttpResponse.build(true);
        OrderForProductEntity orderForProduct = OrderForProductConvert.convertToEntity(orderForProductCreateDto);
        boolean addOrder = service.addObject(orderForProduct);

        return response
                .code(HttpResponse.Status.OK)
                .body(addOrder)
                .message(HttpResponse.Status.OK.name());
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CALL_CENTER_FOR_PRODUCT','SUPER_ADMIN')")
    @Operation(summary = "Get Order for Product by ID", description = "This method retrieves an order for product based on the provided ID.")
    @GetMapping("/get/{id}")
    public HttpResponse<Object> getOrderForProduct(@PathVariable Integer id) {

        HttpResponse<Object> response = HttpResponse.build(true);

        OrderForProductEntity orderForProduct = service.getObjectById(id);
        OrderForProductResponseDto orderForProductResponseDto = OrderForProductConvert.from(orderForProduct);

        return response
                .code(HttpResponse.Status.OK)
                .body(orderForProductResponseDto)
                .message(HttpResponse.Status.OK.name());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CALL_CENTER_FOR_PRODUCT','SUPER_ADMIN')")
    @Operation(summary = "Get All Orders for Product", description = "This method retrieves all orders for product.")
    @GetMapping("/get/all")
    public HttpResponse<Object> getOrderForProductAll() {

        HttpResponse<Object> response = HttpResponse.build(true);
        List<OrderForProductEntity> orderForProductEntityList = service.getAllObject();
        List<OrderForProductResponseDto> orderForProductResponseDtoList = OrderForProductConvert.from(orderForProductEntityList);

        return response
                .code(HttpResponse.Status.OK)
                .body(orderForProductResponseDtoList)
                .message(HttpResponse.Status.OK.name());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CALL_CENTER_FOR_PRODUCT','SUPER_ADMIN')")
    @Operation(summary = "Update Order Status", description = "This method updates the status of an order by the provided ID.")
    @PatchMapping("/update/status/{id}")
    public HttpResponse<Object> updateOrderStatus(@RequestBody OrderStatusUpdateDto orderStatusUpdateDto, @PathVariable Integer id) {

        HttpResponse<Object> response = HttpResponse.build(true);
        boolean updateOrderStatus = service.updateOrderStatus(orderStatusUpdateDto.getOrderStatus(), id);

        return response
                .code(HttpResponse.Status.OK)
                .body(updateOrderStatus)
                .message(HttpResponse.Status.OK.name());
    }
}
