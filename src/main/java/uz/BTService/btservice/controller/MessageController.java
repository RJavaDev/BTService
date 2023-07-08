package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.entity.MessageEntity;
import uz.BTService.btservice.service.MessageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
@Tag(name = "Category Management", description = "This controller manages the categories.")
public class MessageController {
    private final MessageService service;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CALL_CENTER_FOR_SERVICE','SUPER_ADMIN')")
    @Operation(summary = "Get Orders for Service", description = "This method retrieves the orders related to the service and sends a notification message. Once retrieved, the orders are automatically deleted.")
    @GetMapping("/service")
    public HttpResponse<Object> getOrderForService() {

        HttpResponse<Object> response = HttpResponse.build(true);
        List<MessageEntity> allMessageForService = service.getAllMessageForService();

        return response
                .code(HttpResponse.Status.OK)
                .body(allMessageForService)
                .message(HttpResponse.Status.OK.name());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('CALL_CENTER_FOR_PRODUCT','SUPER_ADMIN')")
    @Operation(summary = "Get Orders for Product", description = "This method retrieves the orders related to the product and sends a notification message. Once retrieved, the orders are automatically deleted.")
    @GetMapping("/product")
    public HttpResponse<Object> getOrderForProduct() {

        HttpResponse<Object> response = HttpResponse.build(true);
        List<MessageEntity> allMessageForService = service.getAllMessageForProduct();

        return response
                .code(HttpResponse.Status.OK)
                .body(allMessageForService)
                .message(HttpResponse.Status.OK.name());
    }
}
