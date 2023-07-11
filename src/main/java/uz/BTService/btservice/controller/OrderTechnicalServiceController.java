package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.BTService.btservice.controller.convert.OrderForServiceConvert;
import uz.BTService.btservice.controller.dto.response.OrderForServiceResponseDto;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.controller.dto.request.OrderForServiceCreateDto;
import uz.BTService.btservice.controller.dto.request.OrderStatusUpdateDto;
import uz.BTService.btservice.entity.OrderTechnicalForServiceEntity;
import uz.BTService.btservice.service.OrderTechnicalService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-for-service")
@RequiredArgsConstructor
@Tag(name = "Technical Services", description = "This controller manages technical service orders.")
public class OrderTechnicalServiceController {

    private final OrderTechnicalService service;

    @Operation(summary = "Add Order for Technical Service", description = "This method adds a new order for technical service.")
    @PostMapping("/add")
    public HttpResponse<Object> addOrderForService(@RequestBody @Validated OrderForServiceCreateDto orderForServiceCreateDto) {

        HttpResponse<Object> response = HttpResponse.build(true);
        OrderTechnicalForServiceEntity orderTechnicalService = OrderForServiceConvert.convertToEntity(orderForServiceCreateDto);
        boolean addOrder = service.addObject(orderTechnicalService);

        return response
                .code(HttpResponse.Status.OK)
                .body(addOrder)
                .message(HttpResponse.Status.OK.name());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get My Order", description = "This method retrieves the order belonging to the authenticated user.")
    @GetMapping("/my")
    public HttpResponse<Object> getMyOrder() {

        HttpResponse<Object> response = HttpResponse.build(true);

        List<OrderTechnicalForServiceEntity> serviceEntity = service.getMyOrder();
        List<OrderForServiceResponseDto> getMyOrderList = OrderForServiceConvert.from(serviceEntity);

        return response
                .code(HttpResponse.Status.OK)
                .body(getMyOrderList)
                .message(HttpResponse.Status.OK.name());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CALL_CENTER_FOR_SERVICE','SUPER_ADMIN')")
    @Operation(summary = "Get Order for Technical Service by ID", description = "This method retrieves an order for technical service based on the provided ID.")
    @GetMapping("/get/{id}")
    public HttpResponse<Object> getOrderForService(@PathVariable Integer id) {

        HttpResponse<Object> response = HttpResponse.build(true);

        OrderTechnicalForServiceEntity serviceEntity = service.getObjectById(id);
        OrderForServiceResponseDto orderForServiceResponseDto = OrderForServiceConvert.from(serviceEntity);

        return response
                .code(HttpResponse.Status.OK)
                .body(orderForServiceResponseDto)
                .message(HttpResponse.Status.OK.name());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CALL_CENTER_FOR_SERVICE','SUPER_ADMIN')")
    @Operation(summary = "Get All Orders for Technical Service", description = "This method retrieves all orders for technical service.")
    @GetMapping("/get/all")
    public HttpResponse<Object> getOrderForServiceAll() {

        HttpResponse<Object> response = HttpResponse.build(true);
        List<OrderTechnicalForServiceEntity> orderTechnicalForServiceEntityList = service.getAllObject();
        List<OrderForServiceResponseDto> orderTechnicalServiceEntities = OrderForServiceConvert.from(orderTechnicalForServiceEntityList);

        return response
                .code(HttpResponse.Status.OK)
                .body(orderTechnicalServiceEntities)
                .message(HttpResponse.Status.OK.name());
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CALL_CENTER_FOR_SERVICE','SUPER_ADMIN')")
    @Operation(summary = "Update Order Status for Technical Service", description = "This method updates the status of an order for technical service by the provided ID.")
    @PutMapping("/update/status/{id}")
    public HttpResponse<Object> updateOrderStatus(@RequestBody OrderStatusUpdateDto orderStatusUpdateDto, @PathVariable Integer id) {

        HttpResponse<Object> response = HttpResponse.build(true);
        boolean updateOrderStatus = service.updateOrderStatus(orderStatusUpdateDto.getOrderStatus(), id);

        return response
                .code(HttpResponse.Status.OK)
                .body(updateOrderStatus)
                .message(HttpResponse.Status.OK.name());
    }



}
