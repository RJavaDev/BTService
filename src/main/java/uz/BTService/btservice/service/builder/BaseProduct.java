package uz.BTService.btservice.service.builder;

import java.util.List;

public abstract class BaseProduct<R> implements BaseServiceBuilder<R> {

    public abstract boolean add(R crateNewObject, Integer categoryId,List<String> attachIdList);

    public abstract List<R> getObjectByCategoryId(Integer categoryId);

//    public abstract boolean update(R updateNewObject, Integer objectId,List<String> attachIdList, Integer categoryId);

}
