package uz.BTService.btservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.entity.TechnicalServiceEntity;
import uz.BTService.btservice.exceptions.CategoryNotFoundException;
import uz.BTService.btservice.repository.CategoryRepository;
import uz.BTService.btservice.repository.TechnicalServiceRepository;
import uz.BTService.btservice.service.builder.BaseProduct;
import uz.BTService.btservice.validation.CommonSchemaValidator;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TechnicalService extends BaseProduct<TechnicalServiceEntity> {

    private final TechnicalServiceRepository repository;

    private final CommonSchemaValidator commonSchemaValidator;

    @Override
    public boolean addObject(TechnicalServiceEntity entity, Integer categoryId, String attachId){
        entity.setCategory(commonSchemaValidator.validateCategory(categoryId));
        entity.setAttach(commonSchemaValidator.validateAttachId(attachId));
        entity.forCreate(SecurityUtils.getUserId());
        repository.save(entity);
        return true;
    }


    @Override
    public TechnicalServiceEntity getObjectById(Integer id) {
        return commonSchemaValidator.validateService(id);
    }

    @Override
    public List<TechnicalServiceEntity> getAllObject() {
        return repository.findAll();
    }

    @Override
    public List<TechnicalServiceEntity> getObjectByCategoryId(Integer categoryId) {
        return  repository.getTechnicalServiceCategoryType(categoryId);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.technicalServiceDelete(id);
    }
}
