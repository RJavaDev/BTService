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

import java.util.List;


@Service
@RequiredArgsConstructor
public class TechnicalService extends BaseProduct<TechnicalServiceEntity> {

    private final TechnicalServiceRepository repository;

    private final CategoryRepository categoryRepository;

    @Override
    public boolean add(TechnicalServiceEntity entity, Integer categoryId, List<String> list){
        categoryRepository.findByCategoryId(categoryId).orElseThrow(()->{
            throw new CategoryNotFoundException(categoryId+"-id category not found");
        });
        entity.forCreate(SecurityUtils.getUserId());
        repository.save(entity);
        return true;
    }

    @Override
    public boolean addObject(TechnicalServiceEntity createObject) {
        return false;
    }

    @Override
    public TechnicalServiceEntity getObjectById(Integer id) {
        return repository.getByTechnicalId(id);
    }

    @Override
    public List<TechnicalServiceEntity> getAllObject() {
        return repository.findAll();
    }

    @Override
    public List<TechnicalServiceEntity> getObjectByCategoryId(Integer categoryId) {
        return  repository.getTechnicalServiceCategoryType(categoryId);
    }

//    @Override
//    public boolean update(TechnicalServiceEntity updateNewUpdateObject, Integer objectId, Integer categoryId) {
//        return false;
//    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.technicalServiceDelete(id);
    }
}
