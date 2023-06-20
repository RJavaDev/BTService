package uz.BTService.btservice.service.builder;

import java.util.List;

public interface BaseProduct<R> extends BaseServiceBuilder<R>{

    boolean add(R crateNewObject, Integer categoryId);

    List<R> getObjectByCategoryId(Integer categoryId);

}
