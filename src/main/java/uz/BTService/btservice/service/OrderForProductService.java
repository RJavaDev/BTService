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
import uz.BTService.btservice.validation.CommonSchemaValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderForProductService extends BaseOrderServiceBuilder<OrderForProductEntity> {

    private final OrderForProductRepository repository;

    private final CommonSchemaValidator commonSchemaValidator;

    private final MassageRepository massageRepository;


    @Override
    public boolean addObject(OrderForProductEntity createObject, Integer productId) {

        createObject.setProduct(commonSchemaValidator.validateProductId(productId));

        OrderForProductEntity saveOrderProduct = repository.save(createObject);
        sendMessage(saveOrderProduct);

        return true;
    }

    @Override
    public OrderForProductEntity getObjectById(Integer id) {
        return commonSchemaValidator.validateOrderForProduct(id);
    }

    @Override
    public List<OrderForProductEntity> getAllObject() {
        return repository.getAllOrderProduct();
    }

    @Transactional
    public boolean updateOrderStatus(OrderStatus orderStatus, Integer id) {

        OrderForProductEntity orderForProductEntity = commonSchemaValidator.validateOrderForProduct(id);

        orderForProductEntity.setOrderStatus(orderStatus);
        orderForProductEntity.forUpdate(SecurityUtils.getUserId());

        return true;
    }

    @Override
    public List<OrderForProductEntity> getMyOrder() {
        Integer userId = SecurityUtils.getUserId();
        return repository.getMyOrder(userId);
    }

    private void sendMessage(OrderForProductEntity saveOrderProduct) {
        MessageEntity message = new MessageEntity();
        String productName = saveOrderProduct.getProduct().getName();
        message.setOrderForProductId(saveOrderProduct.getId());
        message.setText_en(productName + MassageText.NEW_ORDER_PRODUCT_EN);
        message.setText_ru(productName + MassageText.NEW_ORDER_PRODUCT_RU);
        message.setText_uz(productName + MassageText.NEW_ORDER_PRODUCT_UZ);
        message.setText_ki(productName + MassageText.NEW_ORDER_PRODUCT_KI);
        massageRepository.save(message);
    }
}
