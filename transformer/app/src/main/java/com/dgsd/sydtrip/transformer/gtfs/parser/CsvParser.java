package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class CsvParser<T extends BaseGtfsModel> implements IParser<T> {

    private final static Logger LOG = Logger.getLogger(CsvParser.class.getName());

    protected abstract T create(List<String> fields, List<String> values);

    @Override
    public List<T> parseRows(File file) {
        final List<T> retval = new LinkedList<>();
        try (CsvListReader reader = new CsvListReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE)) {
            final List<String> fields = new LinkedList<>();
            List<String> nextLine;
            while ((nextLine = reader.read()) != null) {
                if (fields.isEmpty()) {
                    fields.addAll(nextLine);

                    // Remove non-ascii characters
                    fields.replaceAll(value -> value.replaceAll("[^\\x00-\\x7F]", ""));
                } else {
                    if (fields.size() != nextLine.size()) {
                        throw new RuntimeException("Different number of fields to values!");
                    }

                    retval.add(create(fields, nextLine));
                }
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error reading file: " + file, e);
        }

        return retval;
    }
}
