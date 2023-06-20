package uz.BTService.btservice.service.builder;

import java.util.List;

public interface BaseParentAndChild<R> extends BaseServiceBuilder<R>{



    R getObjectByIdTree(Integer id);

    List<R> getAllObjectTree();

    R firstLevelChildrenOfObject();

    R updateObject(R newUpdateObject);

}
