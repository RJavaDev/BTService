package uz.BTService.btservice.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.controller.dto.base.BaseOrderRequestDto;

@Getter
@Setter
public class OrderForProductCreateDto extends BaseOrderRequestDto {

    @NotBlank(message = "productId should not be blank")
    private Integer productId;

}
