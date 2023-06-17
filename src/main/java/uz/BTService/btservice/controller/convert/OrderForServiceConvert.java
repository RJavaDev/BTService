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

        String phoneNumber = orderForServiceCreateDto.getPhoneNumber();
        if(phoneNumber!=null){
            orderTechnicalService.setPhoneNumber(phoneNumber);
            orderTechnicalService.setCustomerFullName(orderTechnicalService.getCustomerFullName());
        }else{
            Integer userId = SecurityUtils.getUserId();
            if(userId!=null){
                orderTechnicalService.setUserId(userId);
                orderTechnicalService.forCreate(userId);
            }else{
                throw new UsernameNotFoundException("The user needs to register or enter a phone number, but has not entered anything!!!");
            }

        }

        Integer userId = SecurityUtils.getUserId();

        orderTechnicalService.setTechnicalServiceId(orderForServiceCreateDto.getTechnicalServiceId());
        orderTechnicalService.setLatitude(orderForServiceCreateDto.getLatitude());
        orderTechnicalService.setLongitude(orderForServiceCreateDto.getLongitude());
        orderTechnicalService.setUserId(userId);

        orderTechnicalService.forCreate(userId);

        return orderTechnicalService;
    }

    public OrderForServiceResponseDto from(OrderTechnicalForServiceEntity orderTechnicalService) {
        return orderTechnicalService.toDto();
    }

    public List<OrderForServiceResponseDto> from(List<OrderTechnicalForServiceEntity> orderTechnicalServiceEntities) {
        return orderTechnicalServiceEntities.stream().map(OrderForServiceConvert::from).toList();
    }
}
