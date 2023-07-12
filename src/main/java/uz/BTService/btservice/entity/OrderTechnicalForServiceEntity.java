package uz.BTService.btservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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


    @Column(name = "order_status", nullable = false, length = 32, columnDefinition = "varchar(32) default 'NEW'")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TechnicalServiceEntity technicalServiceEntity;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserEntity user;


    @JsonIgnore
    public OrderForServiceResponseDto toDto(String... ignoreProperties){
        return toDto(this, new OrderForServiceResponseDto(), ignoreProperties);
    }
}
