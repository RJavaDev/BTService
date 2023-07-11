package uz.BTService.btservice.entity;

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

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "product_id", insertable=false, updatable=false)
    private ProductEntity technicalServiceEntity;

    @Column(name = "order_status", length = 32, columnDefinition = "varchar(32) default 'NEW'")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable=false, updatable=false)
    private UserEntity user;


    public OrderForProductResponseDto toDto(String... ignoreProperties){
        return toDto(this, new OrderForProductResponseDto(), ignoreProperties);
    }
}
