package uz.BTService.btservice.controller.dto.base;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseOrderRequestDto {


    private String phoneNumber;

    private String customerFullName;

    private double latitude;

    private double longitude;
}
