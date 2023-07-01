package uz.BTService.btservice.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@MappedSuperclass
public class BaseForParentAndChild extends BaseServerModifierEntity{

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "parentId")
    private Integer parentId;


}
