package uz.BTService.btservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.BTService.btservice.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query(value = "SELECT btsc.* FROM bts_category btsc WHERE btsc.id=:categoryId AND btsc.status <> 'DELETED'", nativeQuery = true)
    Optional<CategoryEntity> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT btsc.* FROM bts_category btsc WHERE btsc.status <> 'DELETED'", nativeQuery = true)
    List<CategoryEntity> findAll();

    @Query(value = "SELECT btsc.* FROM bts_category btsc WHERE btsc.status <> 'DELETED' fetch first 2 row only",nativeQuery = true)
    List<CategoryEntity> findAllById(@Param("id")Long id);

    @Modifying
    @Query(value = "UPDATE bts_category SET status = 'DELETED' WHERE id = :categoryId", nativeQuery = true)
    Integer categoryDelete(@Param("categoryId") Long categoryId);
}
