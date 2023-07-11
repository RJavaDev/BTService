package uz.BTService.btservice.service.builder;

import java.util.List;

public interface BaseServiceBuilder<R> {

    default boolean addObject(R createObject) {
        return false;
    }
    default boolean addObject(R createdObject, Integer categoryId){
        return false;
    }

    default boolean addObject(R createdObject, String attachId){
        return false;
    }

    default boolean addObject(R createdObject, Integer categoryId, List<String> attachId){
        return false;
    }

    default boolean addObject(R createdObject, Integer categoryId, String attachId){
        return false;
    }

    R getObjectById(Integer id);

    List<R> getAllObject();

    default void delete(Integer id) {}
}
