package uz.BTService.btservice.service.builder;

import java.util.List;

public abstract class BaseProduct<R> implements BaseServiceBuilder<R> {

    public abstract List<R> getObjectByCategoryId(Integer categoryId);

}
