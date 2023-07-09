package uz.BTService.btservice.controller.convert;

import lombok.experimental.UtilityClass;
import uz.BTService.btservice.controller.dto.response.TokenResponseDto;
import uz.BTService.btservice.entity.UserEntity;
import uz.BTService.btservice.interfaces.UserInterface;

@UtilityClass
public class TokenResponseConvert {

    public TokenResponseDto from(String token, UserEntity user){
        return TokenResponseDto.builder()
                .token(token)
                .user(UserConvert.from(user))
                .build();
    }

    public TokenResponseDto from(String token, UserInterface user){
        return TokenResponseDto.builder()
                .token(token)
                .user(UserConvert.from(user))
                .build();
    }

}
