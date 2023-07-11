package uz.BTService.btservice.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.BTService.btservice.entity.AttachEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AttachRepository extends CrudRepository<AttachEntity, String>, PagingAndSortingRepository<AttachEntity, String> {

    @Query(value = "SELECT btsa.* FROM bts_attach btsa WHERE btsa.id = ANY(ARRAY [:attachIdList])",nativeQuery = true)
    List<AttachEntity> getAttachListById(@Param("attachIdList") List<String> attachIdList);

    @Query(value = "SELECT btsa.id FROM bts_attach btsa WHERE btsa.id IN (:attachIdList)",nativeQuery = true)
    List<String> getAttachListByIds(@Param("attachIdList") List<String> attachIdList);
}
