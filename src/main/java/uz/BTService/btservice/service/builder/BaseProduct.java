package uz.BTService.btservice.service.builder;

import java.util.List;

public abstract class BaseProduct<R> implements BaseServiceBuilder<R> {

    public abstract boolean add(R crateNewObject, Integer categoryId);

    public abstract List<R> getObjectByCategoryId(Integer categoryId);

}
