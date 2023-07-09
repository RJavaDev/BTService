package uz.BTService.btservice.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.config.token.JwtService;
import uz.BTService.btservice.controller.convert.TokenResponseConvert;
import uz.BTService.btservice.controller.convert.UserConvert;
import uz.BTService.btservice.controller.dto.request.LoginRequestDto;
import uz.BTService.btservice.controller.dto.response.TokenResponseDto;
import uz.BTService.btservice.entity.AttachEntity;
import uz.BTService.btservice.entity.RegionEntity;
import uz.BTService.btservice.entity.UserEntity;
import uz.BTService.btservice.entity.role.RoleEnum;
import uz.BTService.btservice.exceptions.FileNotFoundException;
import uz.BTService.btservice.exceptions.UserDataException;
import uz.BTService.btservice.exceptions.AuthenticationException;
import uz.BTService.btservice.exceptions.UserUnauthorizedAction;
import uz.BTService.btservice.interfaces.UserInterface;
import uz.BTService.btservice.repository.AttachRepository;
import uz.BTService.btservice.repository.UserRepository;
import uz.BTService.btservice.validation.CommonSchemaValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final CommonSchemaValidator commonSchemaValidator;


    public TokenResponseDto register(UserEntity request) {

        UserEntity userEntity = saveUser(request);
        String jwtToken = jwtService.generateToken(userEntity);

        return TokenResponseConvert.from(jwtToken, userEntity);

    }

    public TokenResponseDto authenticate(LoginRequestDto request) {

        UserInterface user = verifyUser(request.getUsername(),request.getPassword());
        UserEntity userEntity = UserConvert.convertToEntity(user);
        String jwt = jwtService.generateToken(userEntity);

        return TokenResponseConvert.from(jwt, user);
    }

    public UserEntity saveUser(UserEntity user) throws UserDataException {

        commonSchemaValidator.userPasswordAndPhoneNumberCheck(user.getUsername(), user.getPhoneNumber());

        user.setPassword(passwordEncoder.encode(user.getPassword()));


        user.setAttach(commonSchemaValidator.validateAttachId(user.getAttachId()));
        user.setRegion(commonSchemaValidator.validateRegion(user.getRegionId()));

        userForCreate(user);

        return repository.save(user);

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

    private UserInterface verifyUser(String username, String password) {
//        UserEntity user = repository.findByUsername(request.getUsername()).orElseThrow(() -> {
//            throw new AuthenticationException(request.getUsername() + " username not found!");
//        });
        UserInterface userInterface = repository.getUserByUsername(username).orElseThrow(
                () -> {
                    throw new AuthenticationException(username + " username not found!");
                }
        );

        verifyPassword(password, userInterface.getPassword());

        return userInterface;
    }

    private void verifyPassword(String passwordDto, String userPassword) {
        if (!passwordEncoder.matches(passwordDto, userPassword)) {
            throw new AuthenticationException("Incorrect password");
        }
    }

}
