package com.dgsd.sydtrip.transformer;

import com.dgsd.sydtrip.transformer.exception.BadConfigurationException;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.logging.Logger;

public class Main {

    private final static Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            fatalError("Usage: transformer GTFS_FOLDER OUTPUT_DB_FILE");
        }

        String gtfsFolder = args[0];
        String databaseFilePath = args[1];

        final File folder = new File(gtfsFolder);
        if (!folder.exists()) {
            fatalError(String.format("%s does not exist", gtfsFolder));
        } else if (!folder.isDirectory()) {
            fatalError(String.format("%s needs to be the unzipped, root GTFS folder", gtfsFolder));
        } else if (folder.listFiles().length == 0) {
            fatalError(String.format("%s is empty!", gtfsFolder));
        } else {
            if (StringUtils.isEmpty(databaseFilePath)) {
                databaseFilePath = "";
            } else if (!databaseFilePath.endsWith("/")) {
                databaseFilePath += "/";
            }

            new Application(folder, databaseFilePath).run();
        }
    }

    private static void fatalError(String message) {
        throw new BadConfigurationException(message);
    }
}
