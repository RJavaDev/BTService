package uz.BTService.btservice.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.controller.dto.base.BaseOrderRequestDto;

@Getter
@Setter
public class OrderForServiceCreateDto extends BaseOrderRequestDto {

    @NotNull(message = "technicalServiceId should not be blank")
    private Integer technicalServiceId;

}
