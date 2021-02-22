package com.lx.musicdump.music.kugoo;

import com.lx.musicdump.music.Extension;
import com.lx.musicdump.music.MusicFile;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

@Extension({"vpr", "kgm", "kgma"})
public class KugooMusicFile extends MusicFile {

    private static final byte[] HEADER_VPR = {
            (byte) 0x05, (byte) 0x28, (byte) 0xBC, (byte) 0x96, (byte) 0xE9, (byte) 0xE4, (byte) 0x5A, (byte) 0x43,
            (byte) 0x91, (byte) 0xAA, (byte) 0xBD, (byte) 0xD0, (byte) 0x7A, (byte) 0xF5, (byte) 0x36, (byte) 0x31};

    private static final byte[] HEADER_KGM = {
            (byte) 0x7C, (byte) 0xD5, (byte) 0x32, (byte) 0xEB, (byte) 0x86, (byte) 0x02, (byte) 0x7F, (byte) 0x4B,
            (byte) 0xA8, (byte) 0xAF, (byte) 0xA6, (byte) 0x8E, (byte) 0x0F, (byte) 0xFF, (byte) 0x99, (byte) 0x14};

    private static final byte[] VPR_MASK_DIFF = {
            (byte) 0x25, (byte) 0xDF, (byte) 0xE8, (byte) 0xA6, (byte) 0x75, (byte) 0x1E, (byte) 0x75, (byte) 0x0E,
            (byte) 0x2F, (byte) 0x80, (byte) 0xF3, (byte) 0x2D, (byte) 0xB8, (byte) 0xB6, (byte) 0xE3, (byte) 0x11,
            (byte) 0x00};

    private static final byte[] MaskV2PreDef = {
            (byte) 0xB8, (byte) 0xD5, (byte) 0x3D, (byte) 0xB2, (byte) 0xE9, (byte) 0xAF, (byte) 0x78, (byte) 0x8C, (byte) 0x83, (byte) 0x33, (byte) 0x71, (byte) 0x51, (byte) 0x76, (byte) 0xA0, (byte) 0xCD, (byte) 0x37,
            (byte) 0x2F, (byte) 0x3E, (byte) 0x35, (byte) 0x8D, (byte) 0xA9, (byte) 0xBE, (byte) 0x98, (byte) 0xB7, (byte) 0xE7, (byte) 0x8C, (byte) 0x22, (byte) 0xCE, (byte) 0x5A, (byte) 0x61, (byte) 0xDF, (byte) 0x68,
            (byte) 0x69, (byte) 0x89, (byte) 0xFE, (byte) 0xA5, (byte) 0xB6, (byte) 0xDE, (byte) 0xA9, (byte) 0x77, (byte) 0xFC, (byte) 0xC8, (byte) 0xBD, (byte) 0xBD, (byte) 0xE5, (byte) 0x6D, (byte) 0x3E, (byte) 0x5A,
            (byte) 0x36, (byte) 0xEF, (byte) 0x69, (byte) 0x4E, (byte) 0xBE, (byte) 0xE1, (byte) 0xE9, (byte) 0x66, (byte) 0x1C, (byte) 0xF3, (byte) 0xD9, (byte) 0x02, (byte) 0xB6, (byte) 0xF2, (byte) 0x12, (byte) 0x9B,
            (byte) 0x44, (byte) 0xD0, (byte) 0x6F, (byte) 0xB9, (byte) 0x35, (byte) 0x89, (byte) 0xB6, (byte) 0x46, (byte) 0x6D, (byte) 0x73, (byte) 0x82, (byte) 0x06, (byte) 0x69, (byte) 0xC1, (byte) 0xED, (byte) 0xD7,
            (byte) 0x85, (byte) 0xC2, (byte) 0x30, (byte) 0xDF, (byte) 0xA2, (byte) 0x62, (byte) 0xBE, (byte) 0x79, (byte) 0x2D, (byte) 0x62, (byte) 0x62, (byte) 0x3D, (byte) 0x0D, (byte) 0x7E, (byte) 0xBE, (byte) 0x48,
            (byte) 0x89, (byte) 0x23, (byte) 0x02, (byte) 0xA0, (byte) 0xE4, (byte) 0xD5, (byte) 0x75, (byte) 0x51, (byte) 0x32, (byte) 0x02, (byte) 0x53, (byte) 0xFD, (byte) 0x16, (byte) 0x3A, (byte) 0x21, (byte) 0x3B,
            (byte) 0x16, (byte) 0x0F, (byte) 0xC3, (byte) 0xB2, (byte) 0xBB, (byte) 0xB3, (byte) 0xE2, (byte) 0xBA, (byte) 0x3A, (byte) 0x3D, (byte) 0x13, (byte) 0xEC, (byte) 0xF6, (byte) 0x01, (byte) 0x45, (byte) 0x84,
            (byte) 0xA5, (byte) 0x70, (byte) 0x0F, (byte) 0x93, (byte) 0x49, (byte) 0x0C, (byte) 0x64, (byte) 0xCD, (byte) 0x31, (byte) 0xD5, (byte) 0xCC, (byte) 0x4C, (byte) 0x07, (byte) 0x01, (byte) 0x9E, (byte) 0x00,
            (byte) 0x1A, (byte) 0x23, (byte) 0x90, (byte) 0xBF, (byte) 0x88, (byte) 0x1E, (byte) 0x3B, (byte) 0xAB, (byte) 0xA6, (byte) 0x3E, (byte) 0xC4, (byte) 0x73, (byte) 0x47, (byte) 0x10, (byte) 0x7E, (byte) 0x3B,
            (byte) 0x5E, (byte) 0xBC, (byte) 0xE3, (byte) 0x00, (byte) 0x84, (byte) 0xFF, (byte) 0x09, (byte) 0xD4, (byte) 0xE0, (byte) 0x89, (byte) 0x0F, (byte) 0x5B, (byte) 0x58, (byte) 0x70, (byte) 0x4F, (byte) 0xFB,
            (byte) 0x65, (byte) 0xD8, (byte) 0x5C, (byte) 0x53, (byte) 0x1B, (byte) 0xD3, (byte) 0xC8, (byte) 0xC6, (byte) 0xBF, (byte) 0xEF, (byte) 0x98, (byte) 0xB0, (byte) 0x50, (byte) 0x4F, (byte) 0x0F, (byte) 0xEA,
            (byte) 0xE5, (byte) 0x83, (byte) 0x58, (byte) 0x8C, (byte) 0x28, (byte) 0x2C, (byte) 0x84, (byte) 0x67, (byte) 0xCD, (byte) 0xD0, (byte) 0x9E, (byte) 0x47, (byte) 0xDB, (byte) 0x27, (byte) 0x50, (byte) 0xCA,
            (byte) 0xF4, (byte) 0x63, (byte) 0x63, (byte) 0xE8, (byte) 0x97, (byte) 0x7F, (byte) 0x1B, (byte) 0x4B, (byte) 0x0C, (byte) 0xC2, (byte) 0xC1, (byte) 0x21, (byte) 0x4C, (byte) 0xCC, (byte) 0x58, (byte) 0xF5,
            (byte) 0x94, (byte) 0x52, (byte) 0xA3, (byte) 0xF3, (byte) 0xD3, (byte) 0xE0, (byte) 0x68, (byte) 0xF4, (byte) 0x00, (byte) 0x23, (byte) 0xF3, (byte) 0x5E, (byte) 0x0A, (byte) 0x7B, (byte) 0x93, (byte) 0xDD,
            (byte) 0xAB, (byte) 0x12, (byte) 0xB2, (byte) 0x13, (byte) 0xE8, (byte) 0x84, (byte) 0xD7, (byte) 0xA7, (byte) 0x9F, (byte) 0x0F, (byte) 0x32, (byte) 0x4C, (byte) 0x55, (byte) 0x1D, (byte) 0x04, (byte) 0x36,
            (byte) 0x52, (byte) 0xDC, (byte) 0x03, (byte) 0xF3, (byte) 0xF9, (byte) 0x4E, (byte) 0x42, (byte) 0xE9, (byte) 0x3D, (byte) 0x61, (byte) 0xEF, (byte) 0x7C, (byte) 0xB6, (byte) 0xB3, (byte) 0x93, (byte) 0x50,
    };

    private byte[] MaskV2;

    private byte[] audioData;

    @Override
    public boolean isValid() {
        String extension = FilenameUtils.getExtension(FilenameUtils.getExtension(pathname));

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(pathname, "r");

            System.out.println("length = " + randomAccessFile.length());

            if (Objects.equals(extension, "vpr")) {
                byte[] bytes = new byte[0x10];
                randomAccessFile.read(bytes);

                System.out.println(Hex.encodeHexString(HEADER_VPR));
                System.out.println(Hex.encodeHexString(bytes));

                return Arrays.equals(bytes, HEADER_VPR);
            } else {
                byte[] bytes = new byte[0x10];
                randomAccessFile.read(bytes);

                System.out.println(Hex.encodeHexString(HEADER_KGM));
                System.out.println(Hex.encodeHexString(bytes));

                return Arrays.equals(bytes, HEADER_KGM);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

        return false;
    }

    @Override
    protected String getExtension() {

        String extension = FilenameUtils.getExtension(FilenameUtils.getExtension(pathname));

        RandomAccessFile randomAccessFile = null;
        try {

            randomAccessFile = new RandomAccessFile(pathname, "r");

            if (Objects.equals(extension, "vpr")) {
                byte[] bytes = new byte[0x10];
                randomAccessFile.read(bytes);

                System.out.println(Hex.encodeHexString(HEADER_VPR));
                System.out.println(Hex.encodeHexString(bytes));

//                return Arrays.equals(bytes, HEADER_VPR);
            } else {
                byte[] bytes = new byte[0x10];
                randomAccessFile.read(bytes);

                System.out.println(Hex.encodeHexString(HEADER_KGM));
                System.out.println(Hex.encodeHexString(bytes));

//                return Arrays.equals(bytes, HEADER_KGM);
            }

            byte[] bHeaderLenBytes = new byte[0x14 - 0x10];
            randomAccessFile.read(bHeaderLenBytes);
            // Uint32 littleEndian true
            long headerLen = org.apache.commons.lang3.Conversion.byteArrayToLong(bHeaderLenBytes, 0, 0, 0, bHeaderLenBytes.length);

            System.out.println(Hex.encodeHexString(bHeaderLenBytes));
            System.out.println("headerLen = " + headerLen);

            randomAccessFile.seek(0);
            randomAccessFile.skipBytes((int) headerLen);

            long byteLength = randomAccessFile.length() - randomAccessFile.getFilePointer();
            System.out.println("byteLength = " + byteLength);

            byte[] audioData = new byte[(int) byteLength];
            randomAccessFile.read(audioData);

            System.out.println("max = " + (1 << 26));
            if (byteLength > 1 << 26) {
                System.out.println("byteLength too long");
                return null;
            }

            if (MaskV2 == null) {
                loadMaskV2();
            }

            byte[] key1 = new byte[17];
            randomAccessFile.seek(0);
            randomAccessFile.skipBytes(0x1c);
            randomAccessFile.read(key1, 0, 0x2c - 0x1c);

            System.out.println(Hex.encodeHexString(key1));

            for (int i = 0; i < byteLength; i++) {

                byte med8 = (byte) (key1[i % 17] ^ audioData[i]);
                med8 ^= (med8 & 0xf) << 4;

                byte msk8 = getMask(i);
                msk8 ^= (msk8 & 0xf) << 4;
                audioData[i] = (byte) (med8 ^ msk8);
            }
            if (Objects.equals(extension, "vpr")) {
                for (int i = 0; i < byteLength; i++) audioData[i] ^= VPR_MASK_DIFF[i % 17];
            }

            this.audioData = audioData;

            return getExtension(audioData);
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
        return null;
    }

    @Override
    protected void _dump(String pathname) {
        try {
            Files.write(Paths.get(pathname), audioData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMaskV2() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/kgm.mask");
            System.out.println("inputStream = " + inputStream);
            if (inputStream != null) {
                System.out.println("inputStream.available() = " + inputStream.available());

                MaskV2 = new byte[inputStream.available()];
                inputStream.read(MaskV2);
            }

//            File file = new File(getUserDir(), "/resources/kgm.mask");
//            System.out.println("file = " + file);
//            System.out.println("file = " + file.exists());
//            System.out.println("file = " + file.length());
//            MaskV2 = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte getMask(int pos) {
        return (byte) (MaskV2PreDef[pos % 272] ^ MaskV2[pos >> 4]);
    }
}
