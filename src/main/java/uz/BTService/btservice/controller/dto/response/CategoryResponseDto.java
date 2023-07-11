package uz.BTService.btservice.controller.dto.response;

import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.constants.CategoryType;

import java.util.List;

@Getter
@Setter
public class CategoryResponseDto {

    private String name;

    private Integer parentId;

    private CategoryType type;

    private List<CategoryResponseDto> child;

    private AttachUrlResponse attach;

}
