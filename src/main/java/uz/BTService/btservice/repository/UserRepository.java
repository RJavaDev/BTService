package uz.BTService.btservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.BTService.btservice.entity.UserEntity;
import uz.BTService.btservice.interfaces.UserInterface;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    @Query(value = "SELECT * FROM bts_user du WHERE du.username = :username AND status <> 'DELETED'", nativeQuery = true)
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Query(value = "SELECT btsu.*, get_region_address(btsu.region_id) AS address, btsa.path, btsa.type " +
            "FROM bts_user btsu " +
            "LEFT JOIN bts_attach btsa ON btsu.attach_id = btsa.id " +
            "WHERE btsu.username = :username AND status <> 'DELETED'", nativeQuery = true)
    Optional<UserInterface> getUserByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM bts_user du WHERE du.id = :userId AND status <> 'DELETED'", nativeQuery = true)
    Optional<UserEntity> getUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT * FROM bts_user du WHERE du.username = :username OR du.phone_number = :phoneNumber", nativeQuery = true)
    List<UserEntity> findByUsernameOriginalDB(@Param("username") String username,@Param("phoneNumber") String phoneNumber);


    @Query(value = "SELECT btsu.*, get_region_address(btsu.region_id) AS address, btsa.path, btsa.type " +
            "FROM bts_user btsu " +
            "LEFT JOIN bts_attach btsa ON btsu.attach_id = btsa.id " +
            "WHERE btsu.status <> 'DELETED' " +
            "AND 'USER' = ANY(btsu.role_enum_list)", nativeQuery = true)
    List<UserInterface> getAllUserInterface();

    @Query(value = "SELECT btsu.*, get_region_address(btsu.region_id) AS address, btsa.path, btsa.type " +
            "FROM bts_user btsu " +
            "LEFT JOIN bts_attach btsa ON btsu.attach_id = btsa.id " +
            "WHERE btsu.id = :userId AND status <> 'DELETED' " +
            "AND NOT 'SUPER_ADMIN' = ANY(role_enum_list)", nativeQuery = true)
    Optional<UserInterface> getUserInformation(@Param("userId") Integer userId);

    @Modifying
    @Query(value = "UPDATE bts_user SET status = 'DELETED' WHERE id = :userId AND status <> 'DELETED' AND NOT 'SUPER_ADMIN' = ANY(role_enum_list)", nativeQuery = true)
    Integer userDelete(@Param("userId") Integer userId);


    @Query(value = "SELECT btsu.*, get_region_address(btsu.region_id) AS address, btsa.path, btsa.type\n" +
            "            FROM bts_user btsu\n" +
            "            LEFT JOIN bts_attach btsa ON btsu.attach_id = btsa.id\n" +
            "            WHERE NOT 'SUPER_ADMIN' = ANY(role_enum_list)\n" +
            "            AND 'USER' <> ANY(role_enum_list)\n" +
            "            AND status <> 'DELETED'", nativeQuery = true)
    List<UserInterface> getAllAdmin();

    @Query(value = "SELECT btsu.*, get_region_address(btsu.region_id) AS address, btsa.path, btsa.type\n" +
            "            FROM bts_user btsu\n" +
            "            LEFT JOIN bts_attach btsa ON btsu.id = :adminId AND btsu.attach_id = btsa.id\n" +
            "            WHERE NOT 'SUPER_ADMIN' = ANY(role_enum_list)\n" +
            "            AND 'USER' <> ANY(role_enum_list)\n" +
            "            AND status <> 'DELETED'", nativeQuery = true)
    Optional<UserInterface> getAdminById(@Param("adminId") Integer adminId);
}
