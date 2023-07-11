package uz.BTService.btservice.controller.dto;

import lombok.*;
import uz.BTService.btservice.constants.CategoryType;
import uz.BTService.btservice.controller.dto.base.BaseServerModifierDto;
import uz.BTService.btservice.controller.dto.response.AttachUrlResponse;
import uz.BTService.btservice.entity.CategoryEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto extends BaseServerModifierDto {


    private String name;

    private Integer parentId;

    private CategoryType type;

    private List<CategoryDto> children;

    private AttachUrlResponse attach;

    public CategoryEntity toEntity(String... ignoreProperties) {
        return super.toEntity(this, new CategoryEntity(), ignoreProperties);
    }
}
