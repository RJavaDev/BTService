package uz.BTService.btservice.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.BTService.btservice.constants.EntityStatus;
import uz.BTService.btservice.entity.*;
import uz.BTService.btservice.exceptions.*;
import uz.BTService.btservice.interfaces.UserInterface;
import uz.BTService.btservice.repository.*;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommonSchemaValidator {

    private final AttachRepository attachRepository;

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final RegionRepository regionRepository;

    private final UserRepository userRepository;

    private final TechnicalServiceRepository technicalServiceRepository;

    private final OrderForProductRepository orderForProductRepository;


    private void throwIdIsEmpty(String attachId) {
        if (attachId.isEmpty()) {
            throw new FileNotFoundException(attachId + " not null!");
        }
    }

    public CategoryEntity validateCategory(Integer categoryId) {
        return categoryRepository.findByCategoryId(categoryId).orElseThrow(
                () -> {
                    throw new CategoryNotFoundException(categoryId + "-id not found!!!");
                }
        );
    }

    public AttachEntity validateAttachId(String attachId) {
        if (!attachId.isEmpty()) {
            return attachRepository.findById(attachId).orElseThrow(() -> {
                throw new FileNotFoundException(attachId + "-id not found!");
            });
        }
        return null;
    }

    public List<AttachEntity> validateAttachId(List<String> attachIdList) {
        if (Objects.nonNull(attachIdList)) {
            List<AttachEntity> attachEntitiList = attachRepository.getAttachListById(attachIdList);
            for (String id : attachIdList) {
                for (int i = 0; i < attachEntitiList.size(); i++) {
                    if (id.equals(attachEntitiList.get(i).getId())) {
                        break;
                    } else if (i == attachEntitiList.size() - 1) {
                        throw new FileNotFoundException(id + "-attach id not found!");
                    }
                }
            }
            return attachEntitiList;
        }
        return null;
    }

    public ProductEntity validateProductId(Integer productId) {
        return productRepository.findByProductId(productId).orElseThrow(() -> {
                    throw new ProductNotFoundException(productId + " product id not found");
                }
        );

    }

    public ProductEntity validateProduct(Integer objectId, Integer categoryId, List<String> attachIdList, String name, String color, Double price, String description) {

        ProductEntity productDB = validateProductId(objectId);

        if (Objects.nonNull(categoryId)) {
            CategoryEntity categoryIdDb = validateCategory(categoryId);
            productDB.setCategory(categoryIdDb);
        }
        if (Objects.nonNull(attachIdList)) {
            List<AttachEntity> attachList = validateAttachId(attachIdList);
            productDB.setAttach(attachList);
        }
        if (name.isEmpty()) {
            productDB.setName(name);
        }

        if (price > 0) {
            productDB.setPrice(price);
        }
        if (!color.isEmpty()) {
            productDB.setColor(color);
        }
        if (!description.isEmpty()) {
            productDB.setDescription(description);
        }

        return productDB;
    }

    public RegionEntity validateRegion(Integer regionId) {
        if (Objects.nonNull(regionId)) {
            return regionRepository.findByRegionId(regionId).orElseThrow(
                    () -> new RecordNotFoundException(regionId + "-region id not found!"));
        }
        return null;
    }

    public void userPasswordAndPhoneNumberCheck(String username, String phoneNumber) {
        List<UserEntity> user = userRepository.findByUsernameOriginalDB(username, phoneNumber);
        if (!user.isEmpty()) {

            user.forEach((u) -> {
                if (u.getUsername().equals(username)) {
                    throw new RuntimeException(username + " there is a user with this username");
                }
                if (u.getPhoneNumber().equals(phoneNumber)) {
                    throw new RuntimeException(phoneNumber + " there is a user with this phone number");
                }
            });
        }
    }

    public UserInterface validateUser(Integer userId) {
        return userRepository.getUserInformation(userId).orElseThrow(() -> {
                    throw new UsernameNotFoundException(userId + " user id not found!");
                }
        );
    }

    public void categoryStatusCheck(CategoryEntity getByCategoryNameOriginDB, CategoryEntity categoryentity) {

        if (getByCategoryNameOriginDB != null) {

            if (getByCategoryNameOriginDB.getStatus() == EntityStatus.DELETED) {
                Integer parentIdDTO = categoryentity.getParentId();

                if (parentIdDTO != null) {
                    categoryRepository.findByCategoryId(parentIdDTO).orElseThrow(() -> {
                        throw new CategoryNotFoundException(parentIdDTO + " parent id not found!");
                    });
                    getByCategoryNameOriginDB.setParentId(categoryentity.getParentId());
                }
                categoryentity.setId(getByCategoryNameOriginDB.getId());
            } else {
                throw new CategoryNotFoundException(categoryentity.getName() + " such a category exists!");
            }
        }
    }

    public TechnicalServiceEntity validateService(Integer technicalServiceId) {
        return technicalServiceRepository.getByTechnicalId(technicalServiceId).orElseThrow(
                () -> {
                    throw new ProductNotFoundException(technicalServiceId + "-id not found!");
                }
        );
    }

    public void validateServiceId(Integer technicalServiceId) {
        technicalServiceRepository.getByTechnicalId(technicalServiceId).orElseThrow(
                () -> {
                    throw new ProductNotFoundException(technicalServiceId + "-id not found!");
                }
        );
    }

    public OrderForProductEntity validateOrderForProduct(Integer orderId){
        return orderForProductRepository.getOrderForProductById(orderId).orElseThrow(()->{
            throw new OrderNotFoundException(orderId+ "-id not found!");
        });
    }
}
