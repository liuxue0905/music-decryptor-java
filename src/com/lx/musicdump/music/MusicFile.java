package com.lx.musicdump.music;

import com.lx.musicdump.Controller;
import com.lx.musicdump.music.kugoo.KugooMusicFile;
import com.lx.musicdump.music.kuwo.KuwoMusicFile;
import com.lx.musicdump.music.neteast.NeteastMusicFile;
import com.lx.musicdump.music.tencent.QQMusicFile;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MusicFile {

    private static Class[] classes = {KugooMusicFile.class, KuwoMusicFile.class, NeteastMusicFile.class, QQMusicFile.class};

    public static final byte[] FLAC_HEADER = {0x66, 0x4C, 0x61, 0x43};
    public static final byte[] MP3_HEADER = {0x49, 0x44, 0x33};
    public static final byte[] OGG_HEADER = {0x4F, 0x67, 0x67, 0x53};
    public static final byte[] M4A_HEADER = {0x66, 0x74, 0x79, 0x70};
    public static final byte[] WMA_HEADER = {
            (byte) 0x30, (byte) 0x26, (byte) 0xB2, (byte) 0x75, (byte) 0x8E, (byte) 0x66, (byte) 0xCF, (byte) 0x11,
            (byte) 0xA6, (byte) 0xD9, (byte) 0x00, (byte) 0xAA, (byte) 0x00, (byte) 0x62, (byte) 0xCE, (byte) 0x6C,
    };
    public static final byte[] WAV_HEADER = {0x52, 0x49, 0x46, 0x46};

    protected String pathname;
    private String srcParent;
    private String dstParent;

    private Mode mode = Mode.IGNORE;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        IGNORE,
        BOTH,
        REPLACE,
    }

    public void setSrcParent(String srcParent) {
        if (srcParent != null && !Paths.get(pathname).startsWith(srcParent)) {
            throw new IllegalArgumentException();
        }
        this.srcParent = srcParent;
    }

    public void setDstParent(String dstParent) {
        this.dstParent = dstParent;
    }

    public static String[] getExtensions() {
        List<String> extensionList = new ArrayList<>();

//        Class[] classes = {KugooMusicFile.class, KuwoMusicFile.class, NeteastMusicFile.class, QQMusicFile.class};
        for (Class c : classes) {
            Extension annotation = (Extension) c.getAnnotation(Extension.class);
            String[] extionsionArray = annotation.value();
            extensionList.addAll(Arrays.asList(extionsionArray));
        }
        return extensionList.toArray(new String[0]);
    }

    public MusicFile() {
    }

    public MusicFile(String pathname) {
        this.pathname = pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public static MusicFile read(String pathname) {
        String extension = FilenameUtils.getExtension(pathname);
        Class _class = getClass(extension);
        System.out.println(_class);

        Constructor constructor = _class.getDeclaredConstructors()[0];
        System.out.println(constructor.getGenericParameterTypes().length);
        constructor.setAccessible(true);
        try {
            MusicFile musicFile = (MusicFile) constructor.newInstance();
            musicFile.setPathname(pathname);
            return musicFile;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Class<? extends MusicFile> getClass(String extension) {
//        Class[] classes = {KugooMusicFile.class, KuwoMusicFile.class, NeteastMusicFile.class, QQMusicFile.class};
        for (Class _class : classes) {
            Extension annotation = (Extension) _class.getAnnotation(Extension.class);
            if (ArrayUtils.contains(annotation.value(), extension)) {
                return _class;
            }
        }
        return null;
    }

    protected void writeTag(String path, String title, String album, String artist, byte[] bytesImage, String comment) {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(path));
            Tag tag = audioFile.getTag();

//            System.out.println("tag = " + tag);
//            System.out.println("tag = " + tag.getClass());

            Artwork artwork = new StandardArtwork();
            artwork.setBinaryData(bytesImage);
            artwork.setMimeType("image/jpeg");
            tag.setField(artwork);

            tag.setField(FieldKey.TITLE, title);
            tag.setField(FieldKey.ALBUM, album);
            tag.setField(FieldKey.COMMENT, comment);

            if (artist != null) {
                tag.setField(FieldKey.ARTIST, artist);
            }

            if (tag instanceof ID3v23Tag) {
                ID3v23Tag id3v23Tag = (ID3v23Tag) tag;
            } else if (tag instanceof FlacTag) {
                FlacTag flacTag = (FlacTag) tag;
            }

            try {
                AudioFileIO.write(audioFile);
            } catch (CannotWriteException e) {
                e.printStackTrace();
            }
        } catch (CannotReadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }
    }

    public static String getExtension(byte[] bytes) {
        System.out.println("getExtension() bytes[0, 8] = " + Hex.encodeHexString(Arrays.copyOfRange(bytes, 0, 8)));
        if (Arrays.equals(Arrays.copyOfRange(bytes, 0, MP3_HEADER.length), MP3_HEADER)) {
            return "mp3";
        }
        if (Arrays.equals(Arrays.copyOfRange(bytes, 0, FLAC_HEADER.length), FLAC_HEADER)) {
            return "flac";
        }
        if (Arrays.equals(Arrays.copyOfRange(bytes, 0, OGG_HEADER.length), OGG_HEADER)) {
            return "ogg";
        }
        if (Arrays.equals(Arrays.copyOfRange(bytes, 0, 4 + M4A_HEADER.length), M4A_HEADER)) {
            return "m4a";
        }
        if (Arrays.equals(Arrays.copyOfRange(bytes, 0, WMA_HEADER.length), WMA_HEADER)) {
            return "wma";
        }
        if (Arrays.equals(Arrays.copyOfRange(bytes, 0, WAV_HEADER.length), WAV_HEADER)) {
            return "wav";
        }
        return null;
    }

    public abstract boolean isValid();

    public void dump() {
        if (!isValid()) {
            return;
        }

        String extension = getExtension();
        String outPathname = getOutPathname(extension);

        System.out.println("dump outPathname 0 = " + outPathname);

        if (Files.exists(Paths.get(outPathname))) {
            if (getMode() == Mode.IGNORE) {
                return;
            } else if (getMode() == Mode.REPLACE) {
                try {
                    Files.deleteIfExists(Paths.get(outPathname));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (getMode() == Mode.BOTH) {
                outPathname = getNotExistsPathname(outPathname);
            }
        }

        System.out.println("dump outPathname 1 = " + outPathname);
        _dump(outPathname);
    }

    private static String getNotExistsPathname(String pathname) {
        String extension = FilenameUtils.getExtension(pathname);
        int i = 2;
        String tempPathname = pathname;
        while (Files.exists(Paths.get(tempPathname))) {
            tempPathname = FilenameUtils.removeExtension(pathname) + "(" + i + ")" + FilenameUtils.EXTENSION_SEPARATOR + extension;
            i++;
        }
        return tempPathname;
    }

    protected abstract String getExtension();

    protected abstract void _dump(String pathname);

    public String getOutPathname(String extension) {
        System.out.println("getOutPathname extension = " + extension);

        Path srcParentPath = srcParent == null ? Paths.get(pathname).getParent() : Paths.get(srcParent);
        Path dstParentPath = dstParent == null ? Paths.get(pathname).getParent() : Paths.get(dstParent);

        System.out.println("getOutPathname srcParentPath = " + srcParentPath);
        System.out.println("getOutPathname dstParentPath = " + dstParentPath);

        Path relativize = srcParentPath.relativize(Paths.get(pathname));
        System.out.println("getOutPathname relativize = " + relativize);
        relativize = relativize.getParent();
        System.out.println("getOutPathname relativize = " + relativize);

        Path outParentPath = dstParentPath;
        if (relativize != null) {
            outParentPath = outParentPath.resolve(relativize);
        }

        System.out.println("getOutPathname outParentPath = " + outParentPath);

        Path outPath = outParentPath.resolve(FilenameUtils.getBaseName(pathname) + FilenameUtils.EXTENSION_SEPARATOR + extension);

        System.out.println("getOutPathname outPath = " + outPath);

        try {
            Files.createDirectories(outPath.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outPath.toString();
    }

    public static String getUserDir() {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(FileSystems.getDefault().getPath("").toAbsolutePath());
        System.out.println(Paths.get(".").normalize().toAbsolutePath());

        return System.getProperty("user.dir");
    }
}
