package com.dgsd.sydtrip.updater.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class Manifest {

    @SerializedName("version")
    private int version;

    @SerializedName("files")
    private List<ManifestFile> files;

    public int getVersion() {
        return version;
    }

    public List<ManifestFile> getFiles() {
        return files == null ? Collections.<ManifestFile>emptyList() : files;
    }
}
