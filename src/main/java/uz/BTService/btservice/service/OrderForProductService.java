package uz.BTService.btservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.constants.MassageText;
import uz.BTService.btservice.constants.OrderStatus;
import uz.BTService.btservice.entity.MessageEntity;
import uz.BTService.btservice.entity.OrderForProductEntity;
import uz.BTService.btservice.repository.MassageRepository;
import uz.BTService.btservice.repository.OrderForProductRepository;
import uz.BTService.btservice.service.builder.BaseOrderServiceBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderForProductService implements BaseOrderServiceBuilder<OrderForProductEntity> {

    private final OrderForProductRepository repository;

    private final MassageRepository massageRepository;


    @Override
    public boolean addObject(OrderForProductEntity createObject) {
        MessageEntity message = new MessageEntity();

        message.setOrderForProductId(createObject.getProductId());
        message.setText(MassageText.ORDER_SERVICE_CREATE);

        createObject.setOrderStatus(OrderStatus.NEW);
        repository.save(createObject);
        massageRepository.save(message);

        return true;
    }

    @Override
    public OrderForProductEntity getObjectById(Integer id) {
        return repository.getOrderForProductById(id);
    }

    @Override
    public List<OrderForProductEntity> getAllObject() {
        return repository.getAllOrderProduct();
    }

    @Transactional
    public boolean updateOrderStatus(OrderStatus orderStatus, Integer id) {

        OrderForProductEntity orderForProductEntity = repository.getOrderForProductById(id);

        orderForProductEntity.setOrderStatus(orderStatus);
        orderForProductEntity.forUpdate(SecurityUtils.getUserId());

        return true;
    }
}
