package uz.BTService.btservice.service.builder;

import uz.BTService.btservice.constants.OrderStatus;

public interface BaseOrderServiceBuilder<R> extends BaseServiceBuilder<R>{

    boolean updateOrderStatus (OrderStatus status, Integer orderId);
}
