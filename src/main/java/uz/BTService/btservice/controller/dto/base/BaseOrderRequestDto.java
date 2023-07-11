package uz.BTService.btservice.controller.dto.base;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseOrderRequestDto {


    private String phoneNumber;

    private String customerFullName;

    @NotBlank(message = "Latitude should not be blank")
    private double latitude;

    @NotBlank(message = "Longitude should not be blank")
    private double longitude;

    @NotBlank(message = "Address should not be blank")
    private String address;
}
