package uz.BTService.btservice.controller.convert;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.time.DateUtils;
import uz.BTService.btservice.common.util.DateUtil;
import uz.BTService.btservice.controller.dto.UserDto;
import uz.BTService.btservice.controller.dto.request.AdminCreateRequestDto;
import uz.BTService.btservice.controller.dto.request.UserCreateRequestDto;
import uz.BTService.btservice.controller.dto.response.AttachUrlResponse;
import uz.BTService.btservice.entity.AttachEntity;
import uz.BTService.btservice.entity.UserEntity;
import uz.BTService.btservice.entity.role.RoleEnum;
import uz.BTService.btservice.exceptions.UserDataException;
import uz.BTService.btservice.interfaces.UserInterface;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class UserConvert {

    public UserEntity convertToEntity(UserCreateRequestDto userCreateRequestDto) {
        return userIgnorePropertiesAdd(
                userCreateRequestDto.toEntity("role", "birtDate"),
                userCreateRequestDto.getAttachId(),
                userCreateRequestDto.getBirtDate(),
                null);
    }

    public UserEntity convertToEntity(AdminCreateRequestDto userCreateRequestDto) {
        return userIgnorePropertiesAdd(
                userCreateRequestDto.toEntity("role", "birtDate"),
                userCreateRequestDto.getAttachId(),
                userCreateRequestDto.getBirtDate(),
                userCreateRequestDto.getRoles());
    }

    public UserDto from(UserEntity user) {
        UserDto userDto = user.toDto("password", "birtDate");
        userDto.setBirtDate(String.valueOf(DateUtil.format(user.getBirtDate(), DateUtil.PATTERN3)));
        AttachEntity attach = user.getAttach();
        if(Objects.nonNull(attach)){
            userDto.setAttach(AttachConvert.convertToAttachUrlDto(attach));
        }
        return userDto;
    }

    public List<UserDto> fromEntity(List<UserEntity> userEntityList) {
        return userEntityList.stream().map(UserConvert::from).toList();
    }

    public UserDto from(UserInterface userInterface) {
        UserDto dto = new UserDto();
        dto.setId(userInterface.getId());
        dto.setFirstname(userInterface.getFirstname());
        dto.setLastname(userInterface.getLastname());
        dto.setMiddleName(userInterface.getMiddle_name());
        dto.setCreatedBy(userInterface.getCreated_by());
        dto.setUpdatedDate(userInterface.getUpdated_date());
        dto.setCreatedDate(userInterface.getCreated_date());
        dto.setModifiedBy(userInterface.getModified_by());
        dto.setBirtDate(DateUtil.format(userInterface.getBirt_date(), DateUtil.PATTERN3));
        dto.setPhoneNumber(userInterface.getPhone_number());
        dto.setUsername(userInterface.getUsername());

        dto.setRoleEnumList(stringToRoleList(userInterface.getRole_enum_list()));
        attachIdVerifyAndSet(userInterface.getAttach_id(), userInterface.getPath(), userInterface.getType(), dto);

        dto.setStatus(userInterface.getStatus());
        dto.setAddress(userInterface.getAddress());
        return dto;
    }

    public List<UserDto> from(List<UserInterface> userInterfaceList) {
        return userInterfaceList.stream().map(UserConvert::from).toList();
    }

    public UserEntity convertToEntity(UserInterface userInterface){
        UserEntity user = new UserEntity();
        user.setUsername(userInterface.getUsername());
        return user;
    }

    private UserEntity userIgnorePropertiesAdd(UserEntity user, String attachId, String birtDate, List<RoleEnum> role) {

        try {
            user.setBirtDate(DateUtils.parseDate(birtDate, DateUtil.PATTERN3));
            user.setAttachId(attachId);
            user.setRoleEnumList(setRoleEnum(role));

        } catch (ParseException e) {
            e.printStackTrace();
            throw new UserDataException(e.getMessage());
        }
        return user;
    }

    private List<RoleEnum> setRoleEnum(List<RoleEnum> roleEnums) {
        return roleEnums == null ? List.of(RoleEnum.USER) : roleEnums;
    }

    private List<RoleEnum> stringToRoleList(String roles) {
        String[] split = roles.split(",");
        List<RoleEnum> roleEnums = new ArrayList<>();
        for (String s : split) {
            roleEnums.add(RoleEnum.valueOf(s));
        }
        return roleEnums;
    }

    private void attachIdVerifyAndSet(String attachId, String path, String type, UserDto dto) {
        if (attachId != null) {
            AttachUrlResponse attachUrlResponse = AttachConvert.convertToAttachUrlDto(
                    attachId,
                    path,
                    type
            );
            dto.setAttach(attachUrlResponse);
        }
    }
}
