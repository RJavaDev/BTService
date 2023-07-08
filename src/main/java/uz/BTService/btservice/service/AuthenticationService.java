package uz.BTService.btservice.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.config.token.JwtService;
import uz.BTService.btservice.controller.convert.TokenResponseConvert;
import uz.BTService.btservice.controller.dto.request.LoginRequestDto;
import uz.BTService.btservice.controller.dto.response.TokenResponseDto;
import uz.BTService.btservice.entity.AttachEntity;
import uz.BTService.btservice.entity.UserEntity;
import uz.BTService.btservice.entity.role.RoleEnum;
import uz.BTService.btservice.exceptions.FileNotFoundException;
import uz.BTService.btservice.exceptions.UserDataException;
import uz.BTService.btservice.exceptions.AuthenticationException;
import uz.BTService.btservice.exceptions.UserUnauthorizedAction;
import uz.BTService.btservice.repository.AttachRepository;
import uz.BTService.btservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final AttachRepository attachRepository;


    public TokenResponseDto register(UserEntity request) {

        UserEntity userEntity = saveUser(request);
        String jwtToken = jwtService.generateToken(userEntity);

        return TokenResponseConvert.from(jwtToken, userEntity);

    }

    public TokenResponseDto authenticate(LoginRequestDto request) {

        UserEntity user = verifyUser(request);
        String jwt = jwtService.generateToken(user);

        return TokenResponseConvert.from(jwt, user);
    }

    public UserEntity saveUser(UserEntity user) throws UserDataException {
        List<UserEntity> userDB = userRepository.findByUsernameOriginalDB(user.getUsername(), user.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!userDB.isEmpty()) {
            userPasswordAndPhoneNumberCheck(userDB, user);
        }
        if(user.getAttachId()!=null){
            AttachEntity attach = getAttach(user.getAttachId());
            user.setAttach(attach);
        }

        userForCreate(user);

        return userRepository.save(user);
    }

    private AttachEntity getAttach(String attachId) {
        return attachRepository.findById(attachId).orElseThrow(() ->
                new FileNotFoundException(attachId + "-id not found!!!")
        );
    }

    private void userPasswordAndPhoneNumberCheck(List<UserEntity> user, UserEntity userDto) {

        String username = userDto.getUsername();
        String phoneNumber = userDto.getPhoneNumber();

        user.forEach((u)->{
            if(u.getUsername().equals(username)){
                throw new RuntimeException(username + " there is a user with this username");
            }
            if (u.getPhoneNumber().equals(phoneNumber)){
                throw new RuntimeException(phoneNumber + " there is a user with this phone number");
            }
        });

    }

    private void userForCreate(UserEntity user) {
        var userEntity = SecurityUtils.getUser();
        if (userEntity != null) {

            if (userRoleAdminVerify(userEntity.getRoleEnumList())) {
                user.forCreate(userEntity.getId());
            } else {
                throw new UserUnauthorizedAction(userEntity.getId() + " User Unauthorized action!!!");
            }

        } else
            user.forCreate();
    }

    private boolean userRoleAdminVerify(List<RoleEnum> roleEnumList) {
        for (RoleEnum e : roleEnumList) {
            if (e == RoleEnum.SUPER_ADMIN)
                return true;
        }
        return false;
    }

    private UserEntity verifyUser(LoginRequestDto request) {
        UserEntity user = repository.findByUsername(request.getUsername()).orElseThrow(() -> {
            throw new AuthenticationException(request.getUsername() + " username not found!");
        });


        verifyPassword(request, user.getPassword());

        return user;
    }

    private void verifyPassword(LoginRequestDto request, String userPassword) {
        if (!passwordEncoder.matches(request.getPassword(), userPassword)) {
            throw new AuthenticationException("Incorrect password");
        }
    }

}
