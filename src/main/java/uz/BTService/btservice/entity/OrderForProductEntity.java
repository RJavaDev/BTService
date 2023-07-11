package uz.BTService.btservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.constants.OrderStatus;
import uz.BTService.btservice.constants.TableNames;
import uz.BTService.btservice.controller.dto.response.OrderForProductResponseDto;
import uz.BTService.btservice.entity.base.BaseOrderEntity;

@Getter
@Setter
@Entity
@Table(name = TableNames.ORDER_FOR_PRODUCT)
public class OrderForProductEntity extends BaseOrderEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProductEntity product;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserEntity user;


    public OrderForProductResponseDto toDto(String... ignoreProperties){
        return toDto(this, new OrderForProductResponseDto(), ignoreProperties);
    }
}
