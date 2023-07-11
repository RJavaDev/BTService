package uz.BTService.btservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import uz.BTService.btservice.constants.TableNames;
import uz.BTService.btservice.controller.dto.RegionDto;
import uz.BTService.btservice.entity.base.BaseForParentAndChild;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = TableNames.REGION)
public class RegionEntity extends BaseForParentAndChild {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    List<RegionEntity> children = new ArrayList<>();

    @JsonIgnore
    public RegionDto toDto(String... ignoreProperties){
        return toDto(this, new RegionDto(), ignoreProperties);
    }
}
