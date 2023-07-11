package uz.BTService.btservice.controller.convert;

import lombok.experimental.UtilityClass;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.controller.dto.request.OrderForProductCreateDto;
import uz.BTService.btservice.controller.dto.response.OrderForProductResponseDto;
import uz.BTService.btservice.entity.OrderForProductEntity;
import uz.BTService.btservice.entity.UserEntity;
import uz.BTService.btservice.exceptions.UsernameNotFoundException;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class OrderForProductConvert {

    public OrderForProductEntity convertToEntity(OrderForProductCreateDto orderForProductCreateDto){
        OrderForProductEntity orderForProduct = new OrderForProductEntity();

        String phoneNumber = orderForProductCreateDto.getPhoneNumber();
        if(phoneNumber!=null){
            orderForProduct.setPhoneNumber(phoneNumber);
            orderForProduct.setCustomerFullName(orderForProductCreateDto.getCustomerFullName());
        }else{
            Integer userId = SecurityUtils.getUserId();
            if(userId!=null){
                orderForProduct.setUserId(userId);
                orderForProduct.forCreate(userId);
            }else{
                throw new UsernameNotFoundException("The user needs to register or enter a phone number, but has not entered anything!!!");
            }

        }

        orderForProduct.setLatitude(orderForProductCreateDto.getLatitude());
        orderForProduct.setLongitude(orderForProductCreateDto.getLongitude());
        orderForProduct.setAddress(orderForProductCreateDto.getAddress());

        return orderForProduct;
    }

    public OrderForProductResponseDto from(OrderForProductEntity orderForProduct){
        OrderForProductResponseDto orderForProductResponseDto = orderForProduct.toDto("user", "product");
        if(Objects.nonNull(orderForProduct.getUser())){
            orderForProductResponseDto.setUser(UserConvert.from(orderForProduct.getUser()));
        }
        orderForProductResponseDto.setProduct(ProductConvert.from(orderForProduct.getProduct()));
        return orderForProductResponseDto;
    }

    public List<OrderForProductResponseDto> from(List<OrderForProductEntity> orderForProductEntityList){
        return orderForProductEntityList.stream().map(OrderForProductConvert::from).toList();
    }
}
