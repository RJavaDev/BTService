package uz.BTService.btservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.constants.EntityStatus;
import uz.BTService.btservice.entity.CategoryEntity;
import uz.BTService.btservice.exceptions.CategoryNotFoundException;
import uz.BTService.btservice.repository.CategoryRepository;
import uz.BTService.btservice.service.builder.BaseParentAndChild;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryService extends BaseParentAndChild<CategoryEntity> {
    private final CategoryRepository categoryRepository;
    @Override
    public boolean addObject(CategoryEntity category) {
        Integer userId = SecurityUtils.getUserId();

        Optional<CategoryEntity> byCreatedByName = categoryRepository.findByCreatedByName(category.getName());

        if (byCreatedByName.isPresent()) {
            categoryStatusCheckAndSave(byCreatedByName, category, userId);
            return true;
        }

        category.forCreate(userId);

         categoryRepository.save(category);
        return true;
    }
    @Override
    public CategoryEntity getObjectById(Integer id) {
        if (id == null) return null;

        return categoryRepository.findById(id).orElseThrow(
                () -> {throw new CategoryNotFoundException(id + "-id not found!!!");}
        );
    }
    @Override
    public CategoryEntity getObjectByIdTree(Integer id) {
        if (id == null) return null;
        return categoryRepository.findById(id).orElseThrow(
                () -> {
                    throw new CategoryNotFoundException(id + "-id not found");
                }
        );
    }

    @Override
    public List<CategoryEntity> getAllObject() {
        return categoryRepository.findAllCategory();
    }
    @Override
    public List<CategoryEntity> getAllObjectTree() {
        return categoryRepository.getCategoryTree();
    }

    @Override
    public CategoryEntity firstLevelChildrenOfObject() {
        return null;
    }

    @Override
    public CategoryEntity updateObject(CategoryEntity newUpdateObject) {
        CategoryEntity entity = childIdAndParentIdVerify(newUpdateObject);

        entity.setParentId(newUpdateObject.getParentId());
        entity.setName(newUpdateObject.getName());
        entity.forUpdate(SecurityUtils.getUserId());

        return categoryRepository.save(entity);
    }
    @Transactional
    public void delete(Integer id) {
        if (id != null) {
            categoryRepository.findByCategoryId(id).orElseThrow(
                    () -> new CategoryNotFoundException(id + " id not found!!!"));
        }
        categoryRepository.categoryDelete(id);
    }

    private void categoryStatusCheckAndSave(Optional<CategoryEntity> byCreatedByName, CategoryEntity categoryentity, Integer userId) {

        CategoryEntity categoryEntity = byCreatedByName.get();
        if (categoryEntity.getStatus() == EntityStatus.DELETED) {

            categoryEntity.setName(categoryentity.getName());

            if (categoryentity.getParentId() != null) {
                categoryRepository.findByCategoryId(categoryEntity.getParentId()).orElseThrow(() -> {
                    throw new CategoryNotFoundException(categoryentity.getParentId() + " parent id not found!");
                });
                categoryEntity.setParentId(categoryentity.getParentId());
            }

            categoryEntity.setStatus(EntityStatus.CREATED);
            categoryEntity.forCreate(userId);
            categoryRepository.save(categoryEntity);
        } else {
            throw new CategoryNotFoundException(categoryentity.getName() + " such a category exists!");
        }
    }

    private CategoryEntity childIdAndParentIdVerify(CategoryEntity category) {

        CategoryEntity entity = null;
        if (category.getParentId() != null) {

            entity = parentIdVerify(category);

        } else {
            entity = categoryRepository.findByCategoryId(category.getId()).orElseThrow(
                    () -> new CategoryNotFoundException(category.getId() + " id not found!!!"));
        }
        return entity;
    }

    private CategoryEntity parentIdVerify(CategoryEntity category) {

        CategoryEntity entity = null;
        List<CategoryEntity> parentAndChild = categoryRepository.getCategoryIdParentAndChild(category.getId(), category.getParentId());

        if(parentAndChild.size()==2){
            for (CategoryEntity categoryDB : parentAndChild) {

                if (Objects.equals(categoryDB.getId(), category.getId())) {
                    entity = categoryDB;

                }
            }

        } else {
            throw new CategoryNotFoundException("id not found!!!");
        }
        return entity;
    }}
