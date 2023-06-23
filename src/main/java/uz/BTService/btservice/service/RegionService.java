package uz.BTService.btservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.constants.EntityStatus;
import uz.BTService.btservice.entity.RegionEntity;
import uz.BTService.btservice.exceptions.RegionNotFoundException;
import uz.BTService.btservice.repository.RegionRepository;
import uz.BTService.btservice.service.builder.BaseParentAndChild;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionService extends BaseParentAndChild<RegionEntity> {

    private final RegionRepository regionRepository;

    @Override
    public boolean addObject(RegionEntity regionEntity) {
        Integer userId = SecurityUtils.getUserId();
        Optional<RegionEntity> byCreatedByName = regionRepository.findByCreatedByName(regionEntity.getName());
        if (byCreatedByName.isPresent()) {
             regionStatusCheckAndSave(byCreatedByName, regionEntity, userId);
             return true;
        }
        regionEntity.forCreate(userId);
        regionRepository.save(regionEntity);
        return true;
    }

    @Override
    public RegionEntity getObjectById(Integer id) {
        if (id == null) return null;

        return regionRepository.getRegionId(id).orElseThrow(
                () -> {
                    throw new RegionNotFoundException(id + "-id not found!!!");
                }
        );

    }

    @Override
    public RegionEntity getObjectByIdTree(Integer id) {
        if (id == null) return null;

        return regionRepository.findById(id).orElseThrow(
                () -> {
                    throw new RegionNotFoundException(id + "-id not found");
                }
        );
    }

    @Override
    public List<RegionEntity> getAllObject() {
       return regionRepository.findAllRegion();}

    @Override
    public List<RegionEntity> getAllObjectTree() {
        return regionRepository.getRegionAllTree();}

    @Override
    public RegionEntity firstLevelChildrenOfObject() {
        return null;
    }

    @Override
    public RegionEntity updateObject(RegionEntity newUpdateObject) {
        RegionEntity entity = childIdAndParentIdVerify(newUpdateObject);

        entity.setParentId(newUpdateObject.getParentId());
        entity.setName(newUpdateObject.getName());
        entity.forUpdate(SecurityUtils.getUserId());

        return regionRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id != null) {
            regionRepository.findByRegionId(id).orElseThrow(
                    () -> new RegionNotFoundException(id + " id not found!!!"));
        }
        regionRepository.regionDelete(id);
    }

    private void regionStatusCheckAndSave(Optional<RegionEntity> byCreatedByName, RegionEntity regionEntity, Integer userId) {

        RegionEntity regionEntity1 = byCreatedByName.get();
        if (regionEntity1.getStatus() == EntityStatus.DELETED) {

            regionEntity1.setName(regionEntity.getName());

            if (regionEntity.getParentId() != null) {
                regionRepository.findByRegionId(regionEntity1.getParentId()).orElseThrow(() -> {
                    throw new RegionNotFoundException(regionEntity.getParentId() + " parent id not found!");
                });
                regionEntity1.setParentId(regionEntity.getParentId());
            }

            regionEntity1.setStatus(EntityStatus.CREATED);
            regionEntity1.forCreate(userId);
            regionRepository.save(regionEntity1);
        } else {
            throw new RegionNotFoundException(regionEntity.getName() + " such a region exists!");
        }
    }

    private RegionEntity childIdAndParentIdVerify(RegionEntity region) {

        RegionEntity entity = null;
        if (region.getParentId() != null) {

            entity = parentIdVerify(region);

        } else {
            entity = regionRepository.findByRegionId(region.getId()).orElseThrow(
                    () -> new RegionNotFoundException(region.getId() + " id not found!!!"));
        }
        return entity;
    }

    private RegionEntity parentIdVerify(RegionEntity region) {

        RegionEntity entity = null;
        List<RegionEntity> parentAndChild = regionRepository.getRegionIdParentAndChild(region.getId(), region.getParentId());

        if (parentAndChild.size() == 2) {
            for (RegionEntity regionDB : parentAndChild) {

                if (Objects.equals(regionDB.getId(), region.getId())) {
                    entity = regionDB;

                }
            }

        } else {
            throw new RegionNotFoundException("id not found!!!");
        }
        return entity;
    }
}
