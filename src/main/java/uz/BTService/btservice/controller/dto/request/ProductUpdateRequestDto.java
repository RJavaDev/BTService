package uz.BTService.btservice.controller.dto.request;

import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.controller.dto.base.BaseProductDto;
import uz.BTService.btservice.entity.ProductEntity;

import java.util.List;

@Getter
@Setter
public class ProductUpdateRequestDto extends BaseProductDto {


    private String name;

    private Double price;

    private String color;

    private Integer categoryId;

    private List<String> attachId;

    public ProductEntity toEntity(String... ignoreProperties) {
        return super.toEntity(this, new ProductEntity(), ignoreProperties);
    }
}
