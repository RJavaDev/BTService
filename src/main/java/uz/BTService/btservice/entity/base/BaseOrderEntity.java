package uz.BTService.btservice.entity.base;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseOrderEntity extends BaseServerModifierEntity {

    private String phoneNumber;

    private String customerFullName;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;
}
