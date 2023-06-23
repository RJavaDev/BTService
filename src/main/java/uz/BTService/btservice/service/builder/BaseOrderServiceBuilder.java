package uz.BTService.btservice.service.builder;

import uz.BTService.btservice.constants.OrderStatus;

public abstract class BaseOrderServiceBuilder<R> implements BaseServiceBuilder<R> {

    public abstract boolean updateOrderStatus(OrderStatus status, Integer orderId);
}
