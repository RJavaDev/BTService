package uz.BTService.btservice.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.controller.dto.base.BaseOrderRequestDto;

@Getter
@Setter
public class OrderForServiceCreateDto extends BaseOrderRequestDto {

    @NotBlank(message = "technicalServiceId should not be blank")
    private Integer technicalServiceId;

}
