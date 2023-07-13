package uz.BTService.btservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import uz.BTService.btservice.constants.TableNames;
import uz.BTService.btservice.controller.dto.UserDto;
import uz.BTService.btservice.controller.dto.response.TechnicalServiceResponseDto;
import uz.BTService.btservice.entity.base.BaseServerModifierEntity;


@Getter
@Setter
@Entity
@Table(name = TableNames.TECHNICAL_SERVICE)
public class TechnicalServiceEntity extends BaseServerModifierEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    private AttachEntity attach;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CategoryEntity category;

    public TechnicalServiceResponseDto toDto(String... ignoreProperties) {
        return toDto(this, new TechnicalServiceResponseDto(), ignoreProperties);
    }
}
