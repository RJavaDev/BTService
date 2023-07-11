package uz.BTService.btservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.constants.MassageText;
import uz.BTService.btservice.constants.OrderStatus;
import uz.BTService.btservice.entity.MessageEntity;
import uz.BTService.btservice.entity.OrderTechnicalForServiceEntity;
import uz.BTService.btservice.repository.MassageRepository;
import uz.BTService.btservice.repository.OrderTechnicalServiceRepository;
import uz.BTService.btservice.service.builder.BaseOrderServiceBuilder;
import uz.BTService.btservice.validation.CommonSchemaValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTechnicalService extends BaseOrderServiceBuilder<OrderTechnicalForServiceEntity> {

    private final OrderTechnicalServiceRepository repository;

    private final MassageRepository massageRepository;

    private final CommonSchemaValidator commonSchemaValidator;

    @Override
    public boolean addObject(OrderTechnicalForServiceEntity createObject) {

        commonSchemaValidator.validateServiceId(createObject.getTechnicalServiceId());
        MessageEntity message = new MessageEntity();

        message.setOrderServiceId(createObject.getTechnicalServiceId());
        message.setText(MassageText.ORDER_SERVICE_CREATE);

        createObject.setOrderStatus(OrderStatus.NEW);
        repository.save(createObject);
        massageRepository.save(message);

        return true;
    }

    @Override
    public OrderTechnicalForServiceEntity getObjectById(Integer id) {
        return repository.getOrderById(id);
    }

    @Override
    public List<OrderTechnicalForServiceEntity> getAllObject() {
        return repository.getAllOrderForServiceList();
    }

    @Transactional
    public boolean updateOrderStatus(OrderStatus orderStatus, Integer orderId) {

        OrderTechnicalForServiceEntity orderDb = repository.getOrderById(orderId);
        orderDb.setOrderStatus(orderStatus);
        orderDb.forUpdate(SecurityUtils.getUserId());

        repository.save(orderDb);

        return true;
    }
}
