package com.dgsd.sydtrip.transformer.gtfs;

import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;
import com.dgsd.sydtrip.transformer.gtfs.parser.ParserFactory;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class SourceConverter {

    private final static Logger LOG = Logger.getLogger(SourceConverter.class.getName());

    public static List<? extends BaseGtfsModel> processSourceFile(GtfsFile gtfsFile, File file) {
        try {
            LOG.info(gtfsFile + " - source transform ");
            return convert(file, gtfsFile.getModelClass());
        } finally {
            LOG.info(gtfsFile + " - finished source transform ");
        }
    }

    private static <T extends BaseGtfsModel> List<T> convert(File file, Class<T> cls) {
        return ParserFactory.getParser(cls).parseRows(file);
    }

}
