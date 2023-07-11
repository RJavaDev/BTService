package uz.BTService.btservice.service.builder;

import java.util.List;

public abstract class BaseParentAndChild<R> implements BaseServiceBuilder<R> {



    public abstract R getObjectByIdTree(Integer id);

    public abstract List<R> getAllObjectTree();

    public abstract R firstLevelChildrenOfObject();

    public abstract boolean updateObject(R newUpdateObject,Integer objectId,String attachId);

}
