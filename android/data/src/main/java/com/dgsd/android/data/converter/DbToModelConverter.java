package com.dgsd.android.data.converter;

public interface DbToModelConverter<DbObject, ModelObject> {

    public ModelObject convert(DbObject dbObject);

}
