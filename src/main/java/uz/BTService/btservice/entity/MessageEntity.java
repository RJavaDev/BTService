package uz.BTService.btservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.BTService.btservice.constants.TableNames;

@Getter
@Setter
@Entity
@Table(name = TableNames.MESSAGE)
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text_en, text_ru, text_uz, text_ki;

    @Column(name = "order_service_id")
    private Integer orderServiceId;

    @Column(name = "order_product_id")
    private Integer orderForProductId;

}
