package com.dgsd.sydtrip.updater;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.dgsd.sydtrip.updater.model.Manifest;
import com.dgsd.sydtrip.updater.model.ManifestFile;
import com.dgsd.sydtrip.updater.service.ServiceInterface;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import retrofit.RestAdapter;
import retrofit.client.Response;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class UpdateManager {

    private static final String KEY_LAST_SYNC_TIME = "_update_checker_last_sync_time";

    private static final SimpleDateFormat IFMODIFIEDSINCE_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    private final ServiceInterface service;

    private final Context context;

    private final SharedPreferences prefs;

    public UpdateManager(Context context, String serverUrl) {
        this.context = context.getApplicationContext();
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        service = new RestAdapter.Builder()
                .setEndpoint(serverUrl)
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .build()
                .create(ServiceInterface.class);
    }

    public Observable<String> update() {
        final long lastUpdateTime = getLastUpdatedTime();

        final String ifModifiedSince = lastUpdateTime < 0 ?
                null : IFMODIFIEDSINCE_FORMAT.format(new Date(lastUpdateTime));

        return service.getManifest(ifModifiedSince)
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        setLastUpdatedTime(System.currentTimeMillis());
                    }
                }).flatMap(new Func1<Manifest, Observable<ManifestFile>>() {
                    @Override
                    public Observable<ManifestFile> call(Manifest manifest) {
                        return Observable.from(manifest.getFiles());
                    }
                }).filter(new Func1<ManifestFile, Boolean>() {
                    @Override
                    public Boolean call(ManifestFile manifestFile) {
                        return manifestFile.isEnabled();
                    }
                }).flatMap(new Func1<ManifestFile, Observable<Response>>() {
                    @Override
                    public Observable<Response> call(ManifestFile manifestFile) {
                        return service.getFile(manifestFile.getUrlPath());
                    }
                }).map(new Func1<Response, File>() {
                    @Override
                    public File call(Response response) {
                        try {
                            return writeToFile(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).map(new Func1<File, File>() {
                    @Override
                    public File call(File file) {
                        return unpackZip(file);
                    }
                }).map(new Func1<File, String>() {
                    @Override
                    public String call(File file) {
                        return file.getAbsolutePath();
                    }
                });
    }

    private long getLastUpdatedTime() {
        return prefs.getLong(KEY_LAST_SYNC_TIME, -1);
    }

    private void setLastUpdatedTime(long time) {
        prefs.edit().putLong(KEY_LAST_SYNC_TIME, time).apply();
    }

    private File writeToFile(Response response) throws IOException {
        final Uri uri = Uri.parse(response.getUrl());
        final File file = new File(context.getFilesDir(), uri.getLastPathSegment());
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));

        try {
            final InputStream is = response.getBody().in();

            byte[] buffer = new byte[1024];

            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return file;
    }

    private File unpackZip(File zipFile) {
        File rootDir = null;
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            final String path = zipFile.getParent();

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path, filename);
                    fmd.mkdirs();

                    if (rootDir == null) {
                        rootDir = fmd;
                    }
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(new File(path, filename));

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return rootDir;
    }
}
