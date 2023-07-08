package uz.BTService.btservice.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.BTService.btservice.entity.AttachEntity;
import uz.BTService.btservice.entity.CategoryEntity;
import uz.BTService.btservice.entity.ProductEntity;
import uz.BTService.btservice.exceptions.CategoryNotFoundException;
import uz.BTService.btservice.exceptions.FileNotFoundException;
import uz.BTService.btservice.exceptions.ProductNotFoundException;
import uz.BTService.btservice.repository.AttachRepository;
import uz.BTService.btservice.repository.CategoryRepository;
import uz.BTService.btservice.repository.ProductRepository;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommonSchemaValidator {

    private final AttachRepository attachRepository;

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;


    public CategoryEntity validateCategory(Integer categoryId){
        return categoryRepository.findByCategoryId(categoryId).orElseThrow(
                () -> {
                    throw new CategoryNotFoundException(categoryId + "-id not found!!!");
                }
        );
    }

    public AttachEntity validateAttachId(String attachId){
        return attachRepository.findById(attachId).orElseThrow(()->{
            throw new FileNotFoundException(attachId+ "-id not found!");
        });
    }

    public List<AttachEntity> validateAttachId(List<String> attachIdList){
        if(Objects.nonNull(attachIdList)){
            List<AttachEntity> attachEntitiList = attachRepository.getAttachListById(attachIdList);
            for(String id: attachIdList){
                for (int i = 0; i < attachEntitiList.size(); i++) {
                    if(id.equals(attachEntitiList.get(i).getId())){
                        break;
                    } else if (i == attachEntitiList.size()-1) {
                        throw new FileNotFoundException(id+ "-attach id not found!");
                    }
                }
            }
            return attachEntitiList;
        }
       return null;
    }

    public ProductEntity validateProductId(Integer productId){
        return productRepository.findByProductId(productId).orElseThrow(() -> {
                    throw new ProductNotFoundException(productId + " product id not found");
                }
        );
    }

    public ProductEntity validateProduct(Integer objectId, Integer categoryId, List<String> attachIdList, String name, String color, Double price, String description){

        ProductEntity productDB = validateProductId(objectId);

        if(categoryId!=null){
            CategoryEntity categoryIdDb = validateCategory(categoryId);
            productDB.setCategory(categoryIdDb);
        }
        if(attachIdList!=null){
            List<AttachEntity> attachList = validateAttachId(attachIdList);
            productDB.setAttach(attachList);
        }
        if(name.isEmpty()){
            productDB.setName(name);
        }

        if(price>0){
            productDB.setPrice(price);
        }
        if(!color.isEmpty()){
            productDB.setColor(color);
        }
        if(!description.isEmpty()){
            productDB.setDescription(description);
        }

        return productDB;
    }
}
