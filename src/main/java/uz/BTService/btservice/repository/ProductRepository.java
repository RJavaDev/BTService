package uz.BTService.btservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.BTService.btservice.entity.ProductEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    @Query(value = "SELECT btsp.* FROM bts_product btsp " +
            "INNER JOIN bts_category btsc ON btsp.id = :productId " +
            "AND btsc.id=btsp.category_id " +
            "AND btsp.status <> 'DELETED' " +
            "AND btsc.status <> 'DELETED'", nativeQuery = true)
    Optional<ProductEntity> findByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT btsp.* FROM bts_product btsp WHERE btsp.id=:productId", nativeQuery = true)
    Optional<ProductEntity> findByProductIdOrgDB(@Param("productId") Integer productId);


    @Query(value = "SELECT p.* " +
            "FROM bts_product p " +
            "WHERE p.status <> 'DELETED'",
            nativeQuery = true)
List<ProductEntity> getAllProduct();



    @Query(value = "SELECT btsp.* FROM bts_product btsp " +
            "INNER JOIN bts_category btsc ON btsp.category_id = :categoryId " +
            "AND btsc.id=btsp.category_id " +
            "AND btsp.status <> 'DELETED' " +
            "AND btsc.status <> 'DELETED'", nativeQuery = true)
    List<ProductEntity> getCategoryId(@Param("categoryId") Integer categoryId);


    @Query(value = "SELECT btsp.* FROM bts_product btsp\n" +
            "            INNER JOIN bts_category btsc ON btsp.category_id = btsc.id\n" +
            "                WHERE btsp.name ILIKE ANY (ARRAY [:productName])\n" +
            "                   AND btsp.status<>'DELETED'\n" +
            "                   AND btsc.status<>'DELETED'\n" +
            "                 ORDER BY CASE WHEN\n" +
            "                  btsp.name = ANY (ARRAY [:productName])\n" +
            "                   THEN 0 ELSE 1 END, btsp.name", nativeQuery = true)
    List<ProductEntity> getProductNameListSearch(@Param("productName") String[] categoryNameList);

    @Modifying
    @Query(value = "UPDATE bts_product SET status = 'DELETED' AND updated_date = now() AND modified_by = :userId WHERE id = :productId", nativeQuery = true)
    void productDeleted(@Param("productId") Integer productId, @Param("userId")Integer userId);


    @Query(value = "SELECT btsp.* FROM bts_product btsp " +
            "WHERE btsp.status = 'DELETED'" +
            " AND btsp.updated_date BETWEEN " +
            "COALESCE(:startDate, CAST('1970-01-01 00:00:00' AS TIMESTAMP WITHOUT TIME ZONE)) " +
            "AND COALESCE(:endDate, NOW())",nativeQuery = true)
    List<ProductEntity> getDeletedProductByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT btsp.* FROM bts_product btsp " +
            "INNER JOIN bts_category btsc " +
            "ON btsp.name = :productName " +
            "AND btsp.category_id = btsc.id " +
            "WHERE btsp.status<>'DELETED' " +
            "AND btsc.status<>'DELETED'", nativeQuery = true)
    List<ProductEntity> getByProductName(@Param("productName") String productName);
}
