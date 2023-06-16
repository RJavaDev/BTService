package uz.BTService.btservice.interfaces;

import uz.BTService.btservice.constants.EntityStatus;
import uz.BTService.btservice.entity.role.RoleEnum;

import java.util.Date;
import java.util.List;

public interface UserInterface {
    Integer getId();

    EntityStatus getStatus();

    Integer getCreated_by();

    Date getCreated_date();

    Integer getModified_by();

    Date getUpdated_date();

    Date getBirth_date();

    String getFirstname();

    String getLastname();

    String getMiddle_name();

    String getPassword();

    String getPhone_number();

    String getRole_enum_list();

    String getUsername();

    Integer getRegion_id();

    String getAddress();

}
