package uz.BTService.btservice.controller.dto.request;

import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.controller.dto.base.BaseParentAndChildDto;

@Getter
@Setter
public class RegionUpdateRequestDto extends BaseParentAndChildDto {

    private String name;
}
