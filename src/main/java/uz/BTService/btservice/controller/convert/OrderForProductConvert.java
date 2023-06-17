package uz.BTService.btservice.controller.convert;

import lombok.experimental.UtilityClass;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.controller.dto.request.OrderForProductCreateDto;
import uz.BTService.btservice.controller.dto.response.OrderForProductResponseDto;
import uz.BTService.btservice.entity.OrderForProductEntity;
import uz.BTService.btservice.exceptions.UsernameNotFoundException;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@UtilityClass
public class OrderForProductConvert {

    public OrderForProductEntity convertToEntity(OrderForProductCreateDto orderForProductCreateDto){
        OrderForProductEntity orderForProduct = new OrderForProductEntity();

        String phoneNumber = orderForProductCreateDto.getPhoneNumber();
        if(phoneNumber!=null){
            orderForProduct.setPhoneNumber(phoneNumber);
            orderForProduct.setCustomerFullName(orderForProduct.getCustomerFullName());
        }else{
            Integer userId = SecurityUtils.getUserId();
            if(userId!=null){
                orderForProduct.setUserId(userId);
                orderForProduct.forCreate(userId);
            }else{
                throw new UsernameNotFoundException("The user needs to register or enter a phone number, but has not entered anything!!!");
            }

        }

        orderForProduct.setProductId(orderForProductCreateDto.getProductId());
        orderForProduct.setLatitude(orderForProductCreateDto.getLatitude());
        orderForProduct.setLongitude(orderForProductCreateDto.getLongitude());

        return orderForProduct;
    }

    public OrderForProductResponseDto from(OrderForProductEntity product){
        return product.toDto();
    }

    public List<OrderForProductResponseDto> from(List<OrderForProductEntity> orderForProductEntityList){
        return orderForProductEntityList.stream().map(OrderForProductConvert::from).toList();
    }
}
