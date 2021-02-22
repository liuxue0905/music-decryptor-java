package com.lx.musicdump.music.tencent;

import com.lx.musicdump.music.Extension;
import com.lx.musicdump.music.MusicFile;
import com.lx.musicdump.music.tencent.decoder.QmcMaskDecoder;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Extension({
        "qmc3",// QQ Music Android Mp3
        "qmc2", // QQ Music Android Ogg
        "qmc0", // QQ Music Android Mp3
        "qmcflac", // QQ Music Android Flac
        "qmcogg", // QQ Music Android Ogg
        "tkm", // QQ Music Accompaniment M4a
        "bkcmp3", // Moo Music Mp3
        "bkcflac", // Moo Music Flac
        "mflac", // QQ Music Desktop Flac
        "mgg", // QQ Music Desktop Ogg
        "666c6163", // QQ Music Weiyun Flac
        "6d7033", // QQ Music Weiyun Mp3
        "6f6767", // QQ Music Weiyun Ogg
        "6d3461", // QQ Music Weiyun M4a
        "776176", // QQ Music Weiyun Wav
})
public class QQMusicFile extends MusicFile {

    private String extensionIn;
    private String extensionOut;
    private Class<? extends QmcMaskDecoder> decoder;
    private Boolean detect;

    private void init() {
        String extension = FilenameUtils.getExtension(pathname);

        ExtentionConfiguration annotation = QmcMaskDecoder.class.getAnnotation(ExtentionConfiguration.class);
        System.out.println("annotation = " + annotation);

        for (ExtentionConfiguration.Item item : annotation.value()) {
            if (Objects.equals(item.extensionIn(), extension)) {

                extensionIn = item.extensionIn();
                extensionOut = item.extensionOut();
                decoder = item.handler();
                detect = item.detect();

                break;
            }
        }
    }

    @Override
    public boolean isValid() {

        init();

        System.out.println("extensionIn = " + extensionIn);
        System.out.println("extensionOut = " + extensionOut);
        System.out.println("decoder = " + decoder);
        System.out.println("detect = " + detect);

        return extensionIn != null && extensionOut != null && decoder != null && detect != null;
    }

    @Override
    protected String getExtension() {
        return extensionOut;
    }

    @Override
    public void _dump(String pathname) {

        System.out.println("_dump() pathname = " + pathname);

        RandomAccessFile randomAccessFile = null;

        try {
            randomAccessFile = new RandomAccessFile(this.pathname, "r");

            System.out.println("file lenght = " + randomAccessFile.length());
            System.out.println("integer max value = " + Integer.MAX_VALUE);

            byte[] bytes = new byte[(int) randomAccessFile.length()];
            randomAccessFile.read(bytes);

            byte[] fileData = bytes;

            byte[] audioData = null;
            QmcMaskDecoder seed = null;
            byte[] keyData = null;

            if (detect) {

            } else {
                audioData = fileData;
                seed = QmcMaskDecoder.create(decoder, audioData);
            }

            System.out.println("seed = " + seed);

            byte[] musicDecoded = seed.decrypt(audioData);

            System.out.println("musicDecoded.length = " + musicDecoded.length);

            String extension = MusicFile.getExtension(musicDecoded);
            System.out.println("extension = " + extension);
            System.out.println("extensionOut = " + extensionOut);
            extension = extension == null ? extensionOut : extension;

            Files.write(Paths.get(pathname), musicDecoded);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
