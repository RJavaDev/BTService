package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.BTService.btservice.controller.convert.UserConvert;
import uz.BTService.btservice.controller.dto.request.LoginRequestDto;
import uz.BTService.btservice.controller.dto.response.TokenResponseDto;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.controller.dto.request.UserCreateRequestDto;
import uz.BTService.btservice.entity.UserEntity;
import uz.BTService.btservice.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "This Controller for login and register")
public class AuthenticationController {

    private final AuthenticationService service;


    @Operation(summary = "User Registration", description = "This method is used for user registration")
    @PostMapping("/register")
    public HttpResponse<Object> register(@RequestBody UserCreateRequestDto userDto) {

        HttpResponse<Object> response = HttpResponse.build(false);

        UserEntity userEntity = UserConvert.convertToEntity(userDto);
        TokenResponseDto register = service.register(userEntity);

        response
                .code(HttpResponse.Status.OK)
                .success(true)
                .body(register)
                .message("successfully!!!");

        return response;
    }

    @Operation(summary = "User Login", description = "This method is used for user authentication and login")
    @PostMapping("/login")
    public HttpResponse<Object> authenticate(@RequestBody LoginRequestDto request) {
        HttpResponse<Object> response = HttpResponse.build(true);
        TokenResponseDto authenticate = service.authenticate(request);
        return response
                .success(true)
                .code(HttpResponse.Status.OK)
                .body(authenticate)
                .message("successfully!!!");

    }


}
