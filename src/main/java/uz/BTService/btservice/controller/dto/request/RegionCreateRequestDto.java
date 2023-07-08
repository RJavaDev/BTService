package uz.BTService.btservice.controller.dto.request;

import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.controller.dto.base.BaseParentAndChildDto;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class RegionCreateRequestDto extends BaseParentAndChildDto {

    @NotEmpty(message = "Region name should not be empty")
    private String name;
}
