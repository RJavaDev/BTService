package uz.BTService.btservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.BTService.btservice.common.util.DateUtil;
import uz.BTService.btservice.common.util.SecurityUtils;
import uz.BTService.btservice.controller.dto.request.ProductUpdateRequestDto;
import uz.BTService.btservice.controller.dto.response.AttachResponseDto;
import uz.BTService.btservice.controller.dto.response.ProductResponseForAdminDto;
import uz.BTService.btservice.controller.dto.dtoUtil.DataGrid;
import uz.BTService.btservice.controller.dto.dtoUtil.FilterForm;
import uz.BTService.btservice.entity.AttachEntity;
import uz.BTService.btservice.entity.CategoryEntity;
import uz.BTService.btservice.entity.ProductEntity;
import uz.BTService.btservice.exceptions.CategoryNotFoundException;
import uz.BTService.btservice.exceptions.ProductNotFoundException;
import uz.BTService.btservice.repository.AttachRepository;
import uz.BTService.btservice.repository.CategoryRepository;
import uz.BTService.btservice.repository.ProductRepository;
import uz.BTService.btservice.service.builder.BaseProduct;
import uz.BTService.btservice.validation.CommonSchemaValidator;

import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService extends BaseProduct<ProductEntity> {


    private final ProductRepository repository;

    private final CategoryRepository categoryRepository;

    private final AttachRepository attachRepository;
    private final CommonSchemaValidator commonSchemaValidator;

    public DataGrid<ProductEntity> productPage(HttpServletRequest request, FilterForm filterForm) throws Exception {
        DataGrid<ProductEntity> dataGrid = new DataGrid<>();
        dataGrid.setRows(getAllObject());
        return dataGrid;
    }

    @Override
    public boolean add(ProductEntity crateNewObject, Integer categoryId, List<String> attachIdList) {

        CategoryEntity categoryIdDb = commonSchemaValidator.validateCategory(categoryId);

        List<AttachEntity> attachList = commonSchemaValidator.validateAttachId(attachIdList);

        crateNewObject.setAttach(attachList);
        crateNewObject.setCategory(categoryIdDb);
        crateNewObject.forCreate(SecurityUtils.getUserId());

        repository.save(crateNewObject);

        return true;
    }

    @Override
    public boolean addObject(ProductEntity createObject) {
        return false;
    }

    @Override
    public ProductEntity getObjectById(Integer id) {
        return commonSchemaValidator.validateProductId(id);
    }

    @Override
    public List<ProductEntity> getAllObject() {
        return repository.getAllProduct();
    }

    @Override
    public List<ProductEntity> getObjectByCategoryId(Integer categoryId) {
        if (categoryId != null) {
            return repository.getCategoryId(categoryId);
        } else {
            throw new CategoryNotFoundException("id is null");
        }
    }

    public List<ProductEntity> getProductNameSearch(String productName) {
        return repository.getProductNameListSearch(searchProductNameToArray(productName));
    }

    public List<ProductEntity> getDeletedProductsByDate(FilterForm filterForm) {

        Map<String, Object> filterMap = filterForm.getFilter();

        Date startDate = null;
        Date endDate = null;
        if (filterMap != null) {
            if (filterMap.containsKey("startDate")) {
                try {
                    startDate = DateUtils.parseDate((MapUtils.getString(filterMap, "startDate")), DateUtil.PATTERN3);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            if (filterMap.containsKey("endDate")) {
                try {
                    endDate = DateUtils.parseDate((MapUtils.getString(filterMap, "endDate")), DateUtil.PATTERN3);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        return repository.getDeletedProductByDate(startDate, endDate);

    }


    public List<ProductEntity> getProductName(String productName) {
        return repository.getByProductName(productName);
    }


    @Transactional
    public boolean update(ProductUpdateRequestDto updateNewObject, Integer objectId) {

        ProductEntity productDB = commonSchemaValidator.validateProduct(
                objectId,
                updateNewObject.getCategoryId(),
                updateNewObject.getAttachId(),
                updateNewObject.getName(),
                updateNewObject.getColor(),
                updateNewObject.getPrice(),
                updateNewObject.getDescription()
        );

        productDB.forUpdate(SecurityUtils.getUserId());

        repository.save(productDB);

        return true;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.productDeleted(id, SecurityUtils.getUserId());
    }

    private String[] searchProductNameToArray(String productName) {
        String[] categoryNameList = productName.split(" ");

        for (byte i = 0; i < categoryNameList.length; i++) {
            categoryNameList[i] = "%" + categoryNameList[i] + "%";
        }
        return categoryNameList;
    }

}
