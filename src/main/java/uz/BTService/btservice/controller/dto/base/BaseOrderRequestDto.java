package uz.BTService.btservice.controller.dto.base;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseOrderRequestDto {


    private String phoneNumber;

    private String customerFullName;

    @NotNull(message = "Latitude should not be blank")
    private double latitude;

    @NotNull(message = "Longitude should not be blank")
    private double longitude;

    @NotBlank(message = "Address should not be blank")
    private String address;
}
