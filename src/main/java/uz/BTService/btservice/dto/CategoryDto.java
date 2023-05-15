package uz.BTService.btservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import uz.BTService.btservice.dto.base.BaseServerModifierDto;
import uz.BTService.btservice.entity.CategoryEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto extends BaseServerModifierDto {

    @NotBlank(message = "name must not be empty")
    private String name;

    private Long parentId;

    private List<CategoryDto> children;

    public CategoryEntity toEntity(String... ignoreProperties) {
        return super.toEntity(this, new CategoryEntity(), ignoreProperties);
    }
}
