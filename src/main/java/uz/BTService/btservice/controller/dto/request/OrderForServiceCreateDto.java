package uz.BTService.btservice.controller.dto.request;

import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.controller.dto.base.BaseOrderRequestDto;

@Getter
@Setter
public class OrderForServiceCreateDto extends BaseOrderRequestDto {

    private Integer technicalServiceId;

}
