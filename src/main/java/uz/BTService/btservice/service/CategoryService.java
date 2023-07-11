package uz.BTService.btservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.constants.EntityStatus;
import uz.BTService.btservice.entity.AttachEntity;
import uz.BTService.btservice.entity.CategoryEntity;
import uz.BTService.btservice.exceptions.CategoryNotFoundException;
import uz.BTService.btservice.repository.CategoryRepository;
import uz.BTService.btservice.service.builder.BaseParentAndChild;
import uz.BTService.btservice.validation.CommonSchemaValidator;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class CategoryService extends BaseParentAndChild<CategoryEntity> {

    private final CategoryRepository repository;

    private final CommonSchemaValidator commonSchemaValidator;

    @Override
    public boolean addObject(CategoryEntity category, String attachId) {
        Integer userId = SecurityUtils.getUserId();

        CategoryEntity getByCategoryNameOriginDB = repository.findByCategoryName(category.getName());

        commonSchemaValidator.categoryStatusCheck(getByCategoryNameOriginDB, category);
        AttachEntity attach = commonSchemaValidator.validateAttachId(attachId);

        category.setAttach(attach);
        category.forCreate(userId);

        repository.save(category);
        return true;
    }

    @Override
    public CategoryEntity getObjectById(Integer id) {
        if (id == null) return null;

        return repository.findByCategoryId(id).orElseThrow(
                () -> {
                    throw new CategoryNotFoundException(id + "-id not found!!!");
                }
        );
    }

    @Override
    public CategoryEntity getObjectByIdTree(Integer id) {
        if (id == null) return null;
        return repository.findById(id).orElseThrow(
                () -> {
                    throw new CategoryNotFoundException(id + "-id not found");
                }
        );
    }

    @Override
    public List<CategoryEntity> getAllObject() {
        return repository.findAllCategory();
    }

    @Override
    public List<CategoryEntity> getAllObjectTree() {
        return repository.getCategoryTree();
    }

    @Override
    public CategoryEntity firstLevelChildrenOfObject() {
        return null;
    }

    @Override
    public boolean updateObject(CategoryEntity newUpdateObject, Integer categoryId, String attachId) {
        CategoryEntity entity = childIdAndParentIdVerify(newUpdateObject, categoryId);
        entity.setParentId(newUpdateObject.getParentId());

        String updateObjectName = newUpdateObject.getName();
        if (!updateObjectName.isEmpty()) {
            deletedObjectOriginDataBase(updateObjectName);
            entity.setName(updateObjectName);
        }

        AttachEntity attach = commonSchemaValidator.validateAttachId(attachId);
        entity.setAttach(attach);

        entity.forUpdate(SecurityUtils.getUserId());
        repository.save(entity);
        return true;
    }

    @Transactional
    public void delete(Integer id) {
        if (id != null) {
            repository.findByCategoryId(id).orElseThrow(
                    () -> new CategoryNotFoundException(id + " id not found!!!"));
        }
        repository.categoryDelete(id);
    }



    private CategoryEntity childIdAndParentIdVerify(CategoryEntity category, Integer categoryId) {

        CategoryEntity entity = null;
        if (category.getParentId() != null) {

            entity = parentIdVerify(category, categoryId);

        } else {
            entity = repository.findByCategoryId(categoryId).orElseThrow(
                    () -> new CategoryNotFoundException(categoryId + " id not found!!!"));
        }
        return entity;
    }

    private CategoryEntity parentIdVerify(CategoryEntity category, Integer categoryId) {

        CategoryEntity entity = null;
        List<CategoryEntity> parentAndChild = repository.getCategoryIdParentAndChild(categoryId, category.getParentId());

        if (parentAndChild.size() == 2) {
            for (CategoryEntity categoryDB : parentAndChild) {

                if (Objects.equals(categoryDB.getId(), categoryId)) {
                    entity = categoryDB;
                }

            }

        } else {
            throw new CategoryNotFoundException("id not found!!!");
        }
        return entity;
    }

    private void deletedObjectOriginDataBase(String updateObjectName) {
        CategoryEntity originDBObject = repository.findByCategoryName(updateObjectName);
        if (originDBObject.getStatus() == EntityStatus.DELETED) {
            repository.delete(originDBObject);
        } else {
            throw new CategoryNotFoundException(updateObjectName + " the category with this name already exists");
        }
    }
}
