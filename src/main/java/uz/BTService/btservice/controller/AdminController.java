package uz.BTService.btservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.BTService.btservice.controller.convert.UserConvert;
import uz.BTService.btservice.controller.dto.request.AdminCreateRequestDto;
import uz.BTService.btservice.controller.dto.response.TokenResponseDto;
import uz.BTService.btservice.controller.dto.UserDto;
import uz.BTService.btservice.controller.dto.dtoUtil.HttpResponse;
import uz.BTService.btservice.entity.UserEntity;
import uz.BTService.btservice.entity.role.RoleEnum;
import uz.BTService.btservice.interfaces.UserInterface;
import uz.BTService.btservice.service.AuthenticationService;
import uz.BTService.btservice.service.UserService;

import java.rmi.server.UID;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "This Controller manages the administrators for Super Admin")
public class AdminController {

    private final UserService userService;
    private final AuthenticationService service;


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Add Admin", description = "This method adds an administrator")
    @PostMapping("/add")
    public HttpResponse<Object> add(@RequestBody AdminCreateRequestDto requestDto) {
        HttpResponse<Object> response = HttpResponse.build(false);
        try {
            UserEntity user = UserConvert.convertToEntity(requestDto);
            TokenResponseDto register = service.register(user);

            response
                    .code(HttpResponse.Status.OK)
                    .success(true)
                    .body(register)
                    .message(HttpResponse.Status.OK.name());
        } catch (Exception e) {
            e.printStackTrace();
            response.code(HttpResponse.Status.INTERNAL_SERVER_ERROR).success(false).message(e.getMessage());
        }
        return response;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get Admin List", description = "This method retrieves the list of administrators")
    @GetMapping("/list")
    public HttpResponse<Object> getAdminList() {
        HttpResponse<Object> response = HttpResponse.build(true);

        List<UserInterface> adminList = userService.getAdminAll();
        List<UserDto> userDtoList = UserConvert.from(adminList);

        response
                .code(HttpResponse.Status.OK)
                .body(userDtoList)
                .message(HttpResponse.Status.OK.name());

        return response;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get Admin Information", description = "This method retrieves the information of an admin by ID")
    @GetMapping("/info/{id}")
    public HttpResponse<Object> getAdminInformation(@PathVariable Integer id) {
        HttpResponse<Object> response = HttpResponse.build(true);

        UserInterface userInformation = userService.getUserInformation(id);
        UserDto user = UserConvert.from(userInformation);

        response
                .code(HttpResponse.Status.OK)
                .body(user)
                .message(HttpResponse.Status.OK.name());

        return response;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get User Roles", description = "This method retrieves the list of available roles for admins")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/get/roles")
    public HttpResponse<Object> getUserRoles() {

        HttpResponse<Object> response = new HttpResponse<>(true);
        RoleEnum[] roleEnums = RoleEnum.values();

        return response
                .code(HttpResponse.Status.OK)
                .body(roleEnums)
                .message(HttpResponse.Status.OK.name());

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete User by ID", description = "This method allows a Super Admin to delete an admin by their ID")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public HttpResponse<Object> userDelete(@PathVariable Integer id) {

        HttpResponse<Object> response = new HttpResponse<>(true);
        Boolean userDelete = userService.userDelete(id);

        return response
                .code(HttpResponse.Status.OK)
                .body(userDelete)
                .message(id + "-id admin deleted successfully");

    }

}
