package com.lx.musicdump.music.neteast;

import com.google.gson.Gson;
import com.lx.musicdump.music.Extension;
import com.lx.musicdump.music.MusicFile;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Extension({"ncm"})
public class NeteastMusicFile extends MusicFile {

    public static final long HEADER_0 = 0x4e455443;
    public static final long HEADER_1 = 0x4d414446;

    private static final byte[] AES_CORE_KEY = {0x68, 0x7A, 0x48, 0x52, 0x41, 0x6D, 0x73, 0x6F, 0x35, 0x6B, 0x49, 0x6E, 0x62, 0x61, 0x78, 0x57};
    private static final byte[] AES_MODIFY_KEY = {0x23, 0x31, 0x34, 0x6C, 0x6A, 0x6B, 0x5F, 0x21, 0x5C, 0x5D, 0x26, 0x30, 0x55, 0x3C, 0x27, 0x28};


    private long offsetHeader0;
    private long offsetHeader1;
    private long offsetGap2;
    private long offsetKey163Length;
    private long offsetKey163Data;
    private long offsetMetaLength;
    private long offsetMetaData;
    private long offsetGap5;
    private long offsetImageCoverCRC;
    private long offsetCoverLength;
    private long offsetCoverData;
    private long offsetMusicContent;

    private long header0;
    private long header1;
    private long key163Length;
    private long metaLength;
    private String musicJson;
    private long imageCoverCRC;
    private long coverDataLength;

    private Music music;

    public byte[] get163Key() {
        RandomAccessFile randomAccessFile = null;

        try {
            randomAccessFile = new RandomAccessFile(pathname, "r");
            randomAccessFile.seek(offsetKey163Data);

            byte[] bytesKey = new byte[(int) key163Length];
            randomAccessFile.read(bytesKey);

            return get163Key(bytesKey);
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

    public static byte[] get163Key(byte[] bytes) {
        byte[] bytesKeyXor = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytesKeyXor[i] = (byte) (bytes[i] ^ 0x64);
        }

//            System.out.println("xor length = " + bytesKeyXor.length);
//            System.out.println("xor data = " + Hex.encodeHexString(bytesKeyXor));

        byte[] bytesKey163 = decodeAes128Ecb(AES_CORE_KEY, bytesKeyXor);
        System.out.println("163 key length = " + bytesKey163.length);
        System.out.println("163 key data = " + Hex.encodeHexString(bytesKey163));

        return bytesKey163;
//        return new byte[0];
    }

    @Override
    public boolean isValid() {
        RandomAccessFile inputStream = null;

        try {
            inputStream = new RandomAccessFile(pathname, "r");

            offsetHeader0 = 0;
            byte[] h40 = new byte[4];
            inputStream.read(h40);

            offsetHeader1 = offsetHeader0 + 4;
            byte[] h41 = new byte[4];
            inputStream.read(h41);

            header0 = org.apache.commons.lang3.Conversion.byteArrayToLong(h40, 0, 0, 0, h40.length);
            header1 = org.apache.commons.lang3.Conversion.byteArrayToLong(h41, 0, 0, 0, h41.length);

            offsetGap2 = offsetHeader1 + 4;
            offsetKey163Length = offsetGap2 + 2;

            return Objects.equals(header0, NeteastMusicFile.HEADER_0) && Objects.equals(header1, NeteastMusicFile.HEADER_1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    @Override
    protected String getExtension() {
        RandomAccessFile inputStream = null;

        try {
            inputStream = new RandomAccessFile(pathname, "r");

            inputStream.seek(offsetKey163Length);

            byte[] bytes = new byte[4];
            inputStream.read(bytes);
            key163Length = org.apache.commons.lang3.Conversion.byteArrayToLong(bytes, 0, 0, 0, bytes.length);
//            key163Length = inputStream.readInt() & 0xFFFFFFFFL;

            offsetKey163Data = offsetKey163Length + 4;
            offsetMetaLength = offsetKey163Data + key163Length;
            offsetMetaData = offsetMetaLength + 4;

            inputStream.seek(offsetMetaLength);
            byte[] bytesMetaLength = new byte[4];
            inputStream.read(bytesMetaLength);
            metaLength = org.apache.commons.lang3.Conversion.byteArrayToLong(bytesMetaLength, 0, 0, 0, bytesMetaLength.length);
//            metaLength = inputStream.readInt() & 0xFFFFFFFFL;

            System.out.println("metaLength = " + metaLength);

            musicJson = getMusicJson();

            String musicJson = this.musicJson.substring("music:".length());
            music = new Gson().fromJson(musicJson, Music.class);
            System.out.println(music);

            return music.getFormat();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void _dump(String pathname) {

        RandomAccessFile inputStream = null;

        try {
            inputStream = new RandomAccessFile(this.pathname, "r");

            offsetGap5 = offsetMetaData + metaLength;
            offsetImageCoverCRC = offsetGap5 + 5;
            offsetCoverLength = offsetImageCoverCRC + 4;

            inputStream.seek(offsetImageCoverCRC);

            byte[] bytesImageSpace = new byte[4];
            inputStream.read(bytesImageSpace);
            imageCoverCRC = org.apache.commons.lang3.Conversion.byteArrayToLong(bytesImageSpace, 0, 0, 0, bytesImageSpace.length);
            System.out.println("imageCoverCRC = " + imageCoverCRC);

            inputStream.seek(offsetCoverLength);

            byte[] bytesImageSize = new byte[4];
            inputStream.read(bytesImageSize);
            coverDataLength = org.apache.commons.lang3.Conversion.byteArrayToLong(bytesImageSize, 0, 0, 0, bytesImageSize.length);
            System.out.println("coverDataLength = " + coverDataLength);

            offsetCoverData = offsetCoverLength + 4;
            offsetMusicContent = offsetCoverLength + 4 + coverDataLength;

            writeMusicContent(get163Key(), pathname);

            String musicJson = this.musicJson.substring("music:".length());
            writeTag(pathname, music, musicJson);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getMusicJson(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] ^= 0x63;
        }

        System.out.println(new String(bytes));
        bytes = Arrays.copyOfRange(bytes, "163 key(Don't modify):".length(), bytes.length);
        System.out.println(new String(bytes));

        byte[] bytesMusic = Base64.decodeBase64(bytes);

        System.out.println("bytesMusic = " + bytesMusic.length);
        System.out.println("bytesMusic = " + Hex.encodeHexString(bytesMusic));

        byte[] bytesMusicJson = decodeAes128Ecb(AES_MODIFY_KEY, bytesMusic);

        // music:{"musicId":436495751,"musicName":"Feelings","artist":[["Morris Albert",39673]],"albumId":34932161,"album":"Feelings","albumPicDocId":"17737321579648129","albumPic":"http://p4.music.126.net/xc4-dqlKOW0Qf3cu6qTu_Q==/17737321579648129.jpg","bitrate":320000,"mp3DocId":"430b7c83e92ada2f0066fc16ffb78807","duration":224417,"mvId":0,"alias":[],"transNames":[],"format":"mp3"}
        String musicJson = new String(bytesMusicJson);

        System.out.println(musicJson);

        return musicJson;
    }

    private static byte[] decodeAes128Ecb(byte[] bytesKey, byte[] bytes) {

        System.out.println("decodeAes128Ecb bytes = " + bytes.length);
        System.out.println("decodeAes128Ecb bytes = " + Hex.encodeHexString(bytes));

//        Security.addProvider(new com.sun.crypto.provider.SunJCE());
//        if (Security.getProvider("BC") == null) {
//            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        }

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bytesKey, "AES");
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
//        catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    public static byte[] buildKeyBox(byte[] key) {
        System.out.println("buildKeyBox key = " + Hex.encodeHexString(key));

        byte[] mKeyBox = new byte[256];

        for (int i = 0; i < 256; i++) {
            mKeyBox[i] = (byte) i;
        }

        byte swap = 0;
        byte last_byte = 0;
        byte key_offset = 0;

        byte c = 0;

        for (int i = 0; i < 256; i++) {
            swap = mKeyBox[i];
            c = (byte) ((swap + last_byte + key[key_offset++]) & 0xff);
            if (key_offset >= key.length) key_offset = 0;

//            System.out.println("==========");
//            System.out.println(i);
//            System.out.println(c);
//            System.out.println(Byte.toUnsignedInt(c));
//            System.out.println("==========");

//            mKeyBox[i] = mKeyBox[c];
//            mKeyBox[c] = swap;
            mKeyBox[i] = mKeyBox[Byte.toUnsignedInt(c)];
            mKeyBox[Byte.toUnsignedInt(c)] = swap;

            last_byte = c;
        }

        System.out.println(Hex.encodeHexString(mKeyBox));

        return mKeyBox;
    }

    public void writeTag(String file, Music music, String json) {
        byte[] bytesImage = null;

        if (coverDataLength > 0) {

            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(pathname, "r");

                randomAccessFile.skipBytes((int) offsetCoverData);

                bytesImage = new byte[(int) coverDataLength];
                randomAccessFile.read(bytesImage);
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
        }

        writeTag(file, music.getMusicName(), music.getAlbum(), getArtist(music.getArtist()), bytesImage, json);
    }

    private static String getArtist(List<List<String>> artist) {
        if (artist != null) {
            List<String> artistNameList = new ArrayList<>();

            for (List<String> list : artist) {
                String artistName = list.get(0);
                String artistId = list.get(1);

                artistNameList.add(artistName);
            }

            return StringUtils.join(artistNameList, '/');
        }
        return null;
    }

    private String getMusicJson() {
        if (metaLength == 0) {
            return null;
        }

        RandomAccessFile randomAccessFile = null;

        try {
            randomAccessFile = new RandomAccessFile(pathname, "r");

            randomAccessFile.seek(offsetMetaData);

            // 1024 ** 2 * 16 flac/mp3
            byte[] bytesMetaData = new byte[(int) metaLength];
            randomAccessFile.read(bytesMetaData);

            return NeteastMusicFile.getMusicJson(bytesMetaData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomAccessFile == null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private void writeMusicContent(byte[] bytesKey163, String file) {
        bytesKey163 = Arrays.copyOfRange(bytesKey163, 17, bytesKey163.length);

        byte[] keyBox = NeteastMusicFile.buildKeyBox(bytesKey163);

//        int index = 0;

        RandomAccessFile inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new RandomAccessFile(pathname, "r");
            inputStream.skipBytes((int) offsetMusicContent);

            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {

//                if (index == 0) {
//                    System.out.println(read);
//                    System.out.println(Hex.encodeHexString(buffer));
//                }

                for (int i = 0; i < read; i++) {
                    int j = (i + 1) & 0xff;
                    buffer[i] ^= keyBox[(keyBox[j] + keyBox[(keyBox[j] + j) & 0xff]) & 0xff];
                }
                outputStream.write(buffer, 0, read);

//                if (index == 0) {
//                    System.out.println(read);
//                    System.out.println(Hex.encodeHexString(buffer));
//                }
//
//                index++;
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
