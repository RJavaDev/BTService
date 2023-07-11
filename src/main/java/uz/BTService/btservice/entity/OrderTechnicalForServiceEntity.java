package uz.BTService.btservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.constants.OrderStatus;
import uz.BTService.btservice.constants.TableNames;
import uz.BTService.btservice.controller.dto.response.OrderForServiceResponseDto;
import uz.BTService.btservice.entity.base.BaseOrderEntity;

@Getter
@Setter
@Entity
@Table(name = TableNames.ORDER_TECHNICAL_SERVICE)
public class OrderTechnicalForServiceEntity extends BaseOrderEntity {

    @Column(name = "technical_service_id", nullable = false)
    private Integer technicalServiceId;

    @ManyToOne
    @JoinColumn(name = "technical_service_id", insertable = false, updatable = false)
    private TechnicalServiceEntity technicalServiceEntity;

    @Column(name = "order_status", length = 32, columnDefinition = "varchar(32) default 'NEW'")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    public OrderForServiceResponseDto toDto(String... ignoreProperties){
        return toDto(this, new OrderForServiceResponseDto(), ignoreProperties);
    }
}
