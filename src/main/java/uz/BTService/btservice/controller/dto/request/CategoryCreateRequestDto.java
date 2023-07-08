package uz.BTService.btservice.controller.dto.request;

import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.constants.CategoryType;
import uz.BTService.btservice.controller.dto.base.BaseParentAndChildDto;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CategoryCreateRequestDto extends BaseParentAndChildDto {

    @NotEmpty(message = "category name should not be empty")
    private String name;

    @NotEmpty(message = "category type should not be empty")
    private CategoryType type;
}
