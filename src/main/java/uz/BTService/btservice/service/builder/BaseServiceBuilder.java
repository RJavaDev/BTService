package uz.BTService.btservice.service.builder;

import java.util.List;

public interface BaseServiceBuilder<R> {

    boolean addObject(R createObject);

    R getObjectById(Integer id);

    List<R> getAllObject();

    default void delete(Integer id) {}
}
