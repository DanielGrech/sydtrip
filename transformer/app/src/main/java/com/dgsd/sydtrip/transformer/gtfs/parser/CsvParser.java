package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import au.com.bytecode.opencsv.CSVReader;

public abstract class CsvParser<T extends BaseGtfsModel> implements IParser<T> {

    private final static Logger LOG = Logger.getLogger(CsvParser.class.getName());

    protected abstract T create(List<String> fields, String...values);

    @Override
    public List<T> parseRows(File file) {
        final List<T> retval = new LinkedList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            final List<String> fields = new LinkedList<>();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (fields.isEmpty()) {
                    fields.addAll(Arrays.asList(nextLine));

                    // Remove non-ascii characters
                    fields.replaceAll(value -> value.replaceAll("[^\\x00-\\x7F]", ""));
                } else {
                    if (fields.size() != nextLine.length) {
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
