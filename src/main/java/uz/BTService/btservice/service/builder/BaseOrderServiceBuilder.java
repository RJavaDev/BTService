package uz.BTService.btservice.service.builder;

import uz.BTService.btservice.constants.OrderStatus;

import java.util.List;

public abstract class BaseOrderServiceBuilder<R> implements BaseServiceBuilder<R> {

    public abstract boolean updateOrderStatus(OrderStatus status, Integer orderId);

    public abstract List<R> getMyOrder();
}
