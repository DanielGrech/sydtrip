package com.dgsd.sydtrip.transformer.util;

import com.dgsd.sydtrip.transformer.exception.DatabaseOperationException;

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CompressionUtils {

    public static void compress(String inputFile, String outputFile)  {
        try {
            final FileInputStream inFile = new FileInputStream(inputFile);
            final FileOutputStream outfile = new FileOutputStream(outputFile);

            LZMA2Options options = new LZMA2Options();

            options.setPreset(9);

            try (final XZOutputStream out = new XZOutputStream(outfile, options)) {
                byte[] buf = new byte[8192];
                int size;
                while ((size = inFile.read(buf)) != -1) {
                    out.write(buf, 0, size);
                }

                out.finish();
            }
        } catch (IOException e) {
            throw new DatabaseOperationException(e);
        }
    }
}
