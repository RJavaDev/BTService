package uz.BTService.btservice.controller.dto.request;

import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.constants.CategoryType;
import uz.BTService.btservice.controller.dto.base.BaseParentAndChildDto;



@Getter
@Setter
public class CategoryUpdateRequestDto extends BaseParentAndChildDto {

    private String name;

    private CategoryType type;

    private String attachId;

}
