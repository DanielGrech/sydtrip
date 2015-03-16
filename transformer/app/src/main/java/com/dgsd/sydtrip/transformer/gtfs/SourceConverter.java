package com.dgsd.sydtrip.transformer.gtfs;

import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;
import com.dgsd.sydtrip.transformer.gtfs.parser.ParserFactory;

import java.io.File;
import java.util.List;

public class SourceConverter {

    public static List<? extends BaseGtfsModel> processSourceFile(GtfsFile gtfsFile, File file) {
        return convert(file, gtfsFile.getModelClass());
    }

    private static <T extends BaseGtfsModel> List<T> convert(File file, Class<T> cls) {
        return ParserFactory.getParser(cls).parseRows(file);
    }

}
