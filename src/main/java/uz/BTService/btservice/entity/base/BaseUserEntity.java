package uz.BTService.btservice.entity.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseUserEntity extends BaseServerModifierEntity{

    private String attachId;

    private Integer regionId;
}
