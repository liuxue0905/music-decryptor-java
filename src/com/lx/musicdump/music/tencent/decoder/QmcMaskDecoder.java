package com.lx.musicdump.music.tencent.decoder;

import com.lx.musicdump.music.tencent.ExtentionConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@ExtentionConfiguration({
        @ExtentionConfiguration.Item(extensionIn = "mgg", extensionOut = "ogg", handler = QmcMaskDetectMgg.class, detect = true),
        @ExtentionConfiguration.Item(extensionIn = "mflac", extensionOut = "flac", handler = QmcMaskDetectMflac.class, detect = true),
        @ExtentionConfiguration.Item(extensionIn = "qmc0", extensionOut = "mp3", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "qmc2", extensionOut = "ogg", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "qmc3", extensionOut = "mp3", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "qmcogg", extensionOut = "ogg", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "qmcflac", extensionOut = "flac", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "bkcmp3", extensionOut = "mp3", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "bkcflac", extensionOut = "flac", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "tkm", extensionOut = "m4a", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "666c6163", extensionOut = "flac", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "6d7033", extensionOut = "mp3", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "6f6767", extensionOut = "ogg", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "6d3461", extensionOut = "m4a", handler = QmcMaskGetDefault.class, detect = false),
        @ExtentionConfiguration.Item(extensionIn = "776176", extensionOut = "wav", handler = QmcMaskGetDefault.class, detect = false),
})
public abstract class QmcMaskDecoder {
    public static QmcMaskDecoder create(Class<? extends QmcMaskDecoder> decoder, byte[] audioData) {
        Constructor constructor = decoder.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        try {
            return (QmcMaskDecoder) constructor.newInstance(audioData);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public QmcMaskDecoder(byte[] data) {

    }

    public abstract byte[] decrypt(byte[] bytes);
}
