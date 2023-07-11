package uz.BTService.btservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.BTService.btservice.entity.OrderForProductEntity;

import java.util.List;
import java.util.Optional;

public interface OrderForProductRepository extends JpaRepository<OrderForProductEntity, Integer> {


    @Query(value = "SELECT btsofp.* FROM bts_order_for_product btsofp WHERE btsofp.status<>'DELETED'", nativeQuery = true)
    List<OrderForProductEntity> getAllOrderProduct();


    @Query(value = "SELECT btsofp.* FROM bts_order_for_product btsofp WHERE btsofp.id = :id AND btsofp.status<>'DELETED'", nativeQuery = true)
    Optional<OrderForProductEntity> getOrderForProductById(@Param("id") Integer id);


    @Query(value = "SELECT btsofp.*\n" +
            "       FROM bts_order_for_product btsofp\n" +
            "       WHERE btsofp.user_id = :userId\n" +
            "       AND btsofp.status <> 'DELETED'", nativeQuery = true)
    List<OrderForProductEntity> getMyOrder(@Param("userId")Integer userId);

    List<OrderForProductEntity> findByUserId(Integer user_id);
}
