package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;

import java.io.File;
import java.util.List;

public interface IParser<T extends BaseGtfsModel> {

    /**
     * @return A map of csv column name
     */
    public List<T> parseRows(File file);
}
