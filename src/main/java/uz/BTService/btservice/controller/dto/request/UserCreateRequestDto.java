package uz.BTService.btservice.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import uz.BTService.btservice.controller.dto.base.BaseUserDto;
import uz.BTService.btservice.entity.UserEntity;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto extends BaseUserDto {

    @NotBlank(message = "firstname must not be null!!!")
    private String firstname;

    @NotBlank(message = "birtDate must not be null!!!")
    @Schema(name = "birtDate", example = "25-04-2000")
    private String birtDate;

    @NotBlank(message = "phoneNumber must not be null!!!")
    @Schema(name = "phoneNumber", example = "+998901389918")
    private String phoneNumber;

    @NotBlank(message = "username must not be null!!!")
    private String username;

    @NotBlank(message = "password must not be null!!!")
    private String password;

//    @NotBlank(message = "region id must not be null!!!")
    private Integer regionId;

    private String attachId;

    public UserEntity toEntity(String... ignoreProperties) {
        return super.toEntity(this, new UserEntity(), ignoreProperties);
    }

}
