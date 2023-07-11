package uz.BTService.btservice.controller.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.constants.CategoryType;
import uz.BTService.btservice.controller.dto.base.BaseParentAndChildDto;



@Getter
@Setter
public class CategoryCreateRequestDto extends BaseParentAndChildDto {

    @NotBlank(message = "category name should not be empty")
    private String name;

    @NotNull(message = "category type should not be null")
    private CategoryType type;

    @NotBlank(message = "category attachId should not be empty")
    private String attachId;
}
