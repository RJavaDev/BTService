package uz.BTService.btservice.controller.convert;

import lombok.experimental.UtilityClass;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.controller.dto.response.OrderForServiceResponseDto;
import uz.BTService.btservice.controller.dto.request.OrderForServiceCreateDto;
import uz.BTService.btservice.entity.OrderTechnicalForServiceEntity;
import uz.BTService.btservice.exceptions.UsernameNotFoundException;

import java.util.List;

@UtilityClass
public class OrderForServiceConvert {

    public OrderTechnicalForServiceEntity convertToEntity(OrderForServiceCreateDto orderForServiceCreateDto) {
        OrderTechnicalForServiceEntity orderTechnicalService = new OrderTechnicalForServiceEntity();

        validateUser(orderTechnicalService,orderForServiceCreateDto);

        orderTechnicalService.setLatitude(orderForServiceCreateDto.getLatitude());
        orderTechnicalService.setLongitude(orderForServiceCreateDto.getLongitude());
        orderTechnicalService.setAddress(orderForServiceCreateDto.getAddress());

        return orderTechnicalService;
    }

    public OrderForServiceResponseDto from(OrderTechnicalForServiceEntity orderTechnicalService) {
        OrderForServiceResponseDto orderForServiceResponseDto = orderTechnicalService.toDto("technicalServiceEntity","technicalServiceResponseDto", "user");
        orderForServiceResponseDto.setUser(UserConvert.from(orderTechnicalService.getUser()));
        orderForServiceResponseDto.setTechnicalServiceResponseDto(TechnicalServiceConvert.from(orderTechnicalService.getTechnicalServiceEntity()));
        return orderForServiceResponseDto;
    }

    public List<OrderForServiceResponseDto> from(List<OrderTechnicalForServiceEntity> orderTechnicalServiceEntities) {
        return orderTechnicalServiceEntities.stream().map(OrderForServiceConvert::from).toList();
    }

    private void validateUser(OrderTechnicalForServiceEntity orderTechnicalService, OrderForServiceCreateDto orderForServiceCreateDto){

        String phoneNumber = orderForServiceCreateDto.getPhoneNumber();
        if(phoneNumber!=null){
            orderTechnicalService.setPhoneNumber(phoneNumber);
            orderTechnicalService.setCustomerFullName(orderTechnicalService.getCustomerFullName());
            orderTechnicalService.forCreate();
        }else{

            Integer userId = SecurityUtils.getUserId();
            if(userId!=null){
                orderTechnicalService.setUserId(userId);
                orderTechnicalService.forCreate(userId);
            }else{
                throw new UsernameNotFoundException("The user needs to register or enter a phone number, but has not entered anything!!!");
            }

        }
    }
}
