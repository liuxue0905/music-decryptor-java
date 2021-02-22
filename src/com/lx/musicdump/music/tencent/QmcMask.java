package com.lx.musicdump.music.tencent;

import com.lx.musicdump.music.MusicFile;
import com.lx.musicdump.music.tencent.decoder.QmcMaskDetectMflac;
import com.lx.musicdump.music.tencent.decoder.QmcMaskDetectMgg;
import com.lx.musicdump.music.tencent.decoder.QmcMaskGetDefault;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class QmcMask {

    public static final byte[] QMOggPublicHeader1 = {
            (byte) 0x4f, (byte) 0x67, (byte) 0x67, (byte) 0x53, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff,
            (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x1e, (byte) 0x01, (byte) 0x76, (byte) 0x6f, (byte) 0x72,
            (byte) 0x62, (byte) 0x69, (byte) 0x73, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x44, (byte) 0xac, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0xee, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb8, (byte) 0x01, (byte) 0x4f, (byte) 0x67, (byte) 0x67, (byte) 0x53, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
    public static final byte[] QMOggPublicHeader2 = {
            (byte) 0x03, (byte) 0x76, (byte) 0x6f, (byte) 0x72, (byte) 0x62, (byte) 0x69, (byte) 0x73, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x58, (byte) 0x69, (byte) 0x70, (byte) 0x68, (byte) 0x2e,
            (byte) 0x4f, (byte) 0x72, (byte) 0x67, (byte) 0x20, (byte) 0x6c, (byte) 0x69, (byte) 0x62, (byte) 0x56, (byte) 0x6f, (byte) 0x72, (byte) 0x62, (byte) 0x69, (byte) 0x73, (byte) 0x20, (byte) 0x49, (byte) 0x20,
            (byte) 0x32, (byte) 0x30, (byte) 0x31, (byte) 0x35, (byte) 0x30, (byte) 0x31, (byte) 0x30, (byte) 0x35, (byte) 0x20, (byte) 0x28, (byte) 0xe2, (byte) 0x9b, (byte) 0x84, (byte) 0xe2, (byte) 0x9b, (byte) 0x84,
            (byte) 0xe2, (byte) 0x9b, (byte) 0x84, (byte) 0xe2, (byte) 0x9b, (byte) 0x84, (byte) 0x29, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x54,
            (byte) 0x49, (byte) 0x54, (byte) 0x4c, (byte) 0x45, (byte) 0x3d};
    public static final byte[] QMOggPublicConf1 = {
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0,
            0, 0, 9, 9, 9, 9, 0, 0, 0, 0, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 6, 3, 3, 3, 3, 6, 6, 6, 6,
            3, 3, 3, 3, 6, 6, 6, 6, 6, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0, 9, 9, 9, 9,
            0, 0, 0, 0};
    public static final byte[] QMOggPublicConf2 = {
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 0, 1, 3, 3, 0, 1, 3, 3, 3,
            3, 3, 3, 3, 3};
    public static final byte[] QMCDefaultMaskMatrix = {
            (byte) 0xde, (byte) 0x51, (byte) 0xfa, (byte) 0xc3, (byte) 0x4a, (byte) 0xd6, (byte) 0xca, (byte) 0x90,
            (byte) 0x7e, (byte) 0x67, (byte) 0x5e, (byte) 0xf7, (byte) 0xd5, (byte) 0x52, (byte) 0x84, (byte) 0xd8,
            (byte) 0x47, (byte) 0x95, (byte) 0xbb, (byte) 0xa1, (byte) 0xaa, (byte) 0xc6, (byte) 0x66, (byte) 0x23,
            (byte) 0x92, (byte) 0x62, (byte) 0xf3, (byte) 0x74, (byte) 0xa1, (byte) 0x9f, (byte) 0xf4, (byte) 0xa0,
            (byte) 0x1d, (byte) 0x3f, (byte) 0x5b, (byte) 0xf0, (byte) 0x13, (byte) 0x0e, (byte) 0x09, (byte) 0x3d,
            (byte) 0xf9, (byte) 0xbc, (byte) 0x00, (byte) 0x11};

    static byte[][] allMapping = new byte[256][];
    static byte[] mask128to44 = new byte[128];

    static {
        for (int i = 0; i < 128; i++) {
            int realIdx = (i * i + 27) % 256;
            if (allMapping[realIdx] != null) {
                allMapping[realIdx] = ArrayUtils.add(allMapping[realIdx], (byte) i);
            } else {
                allMapping[realIdx] = new byte[]{(byte) i};
            }
        }

//        System.out.println("allMapping " + arraysToString(allMapping));

        int idx44 = 0;
        for (byte[] all128 : allMapping) {
            if (all128 == null) {
                continue;
            }

//            System.out.println("idx44 all128 " + idx44 + " " + Arrays.toString(all128));
            for (byte _i128 : all128) {
                mask128to44[_i128] = (byte) idx44;
            }
            idx44++;
        }

//        System.out.println("mask128to44 " + Arrays.toString(mask128to44));
    }

    static byte[][] GetConvertMapping() {
        return allMapping;
    }

    static byte GetMask44Index(int idx128) {
        return mask128to44[idx128 % 128];
    }

    static byte[] QmcGenerateOggHeader(int page2) {
        byte[] spec = {(byte) page2, (byte) 0xFF};
        for (int i = 2; i < page2; i++) ArrayUtils.add(spec, (byte) 0xFF);
        ArrayUtils.add(spec, (byte) 0xFF);

        byte[] array = ArrayUtils.addAll(QMOggPublicHeader1, spec);
        array = ArrayUtils.addAll(array, QMOggPublicHeader2);
        return array;
    }

    static byte[] QmcGenerateOggConf(int page2) {
        byte[] specConf = {6, 0};
        for (int i = 2; i < page2; i++) specConf = ArrayUtils.add(specConf, (byte) 4);
        specConf = ArrayUtils.add(specConf, (byte) 0);
        byte[] array = ArrayUtils.addAll(QMOggPublicConf1, specConf);
        array = ArrayUtils.addAll(array, QMOggPublicConf2);
        return array;
    }

    public static QmcMask QmcMaskGetDefault() {
        return new QmcMask(QMCDefaultMaskMatrix);
    }

    public static QmcMask QmcMaskDetectMflac(byte[] data) {
        int search_len = Math.min(0x8000, data.length);
        QmcMask mask = null;
        for (int block_idx = 0; block_idx < search_len; block_idx += 128) {
            try {
                mask = new QmcMask(Arrays.copyOfRange(data, block_idx, block_idx + 128));
                if (Arrays.equals(MusicFile.FLAC_HEADER, mask.decrypt(Arrays.copyOfRange(data, 0, MusicFile.FLAC_HEADER.length))))
                    break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mask;
    }

    public static QmcMask QmcMaskDetectMgg(byte[] data) {
        if (data.length < 0x100) return null;
        byte[][] matrixConfidence = new byte[44][];
        for (int i = 0; i < 44; i++) matrixConfidence[i] = new byte[]{};

        int page2 = data[0x54] ^ data[0xC] ^ QMOggPublicHeader1[0xC];
        byte[] spHeader = QmcGenerateOggHeader(page2);
        byte[] spConf = QmcGenerateOggConf(page2);

        for (int idx128 = 0; idx128 < spHeader.length; idx128++) {

        }
        byte[] matrix = new byte[44];
        for (int i = 0; i < 44; i++)
            matrix[i] = (byte) getMaskConfidenceResult(matrixConfidence[i]);
        QmcMask mask = new QmcMask(matrix);
        byte[] dx = mask.decrypt(Arrays.copyOfRange(data, 0, MusicFile.OGG_HEADER.length));
        if (!Arrays.equals(MusicFile.OGG_HEADER, dx)) {
            return null;
        }

        return mask;
    }

    public static QmcMask QmcMaskCreate128(byte[] mask128) {
        return new QmcMask(mask128);
    }

    public static QmcMask QmcMaskCreate58(byte[] matrix, Byte superA, Byte superB) {
        return new QmcMask(matrix, superA, superB);
    }

    public static QmcMask QmcMaskCreate44(byte[] mask44) {
        return new QmcMask(mask44);
    }

    static int getMaskConfidenceResult(byte[] confidence) {
        if (confidence.length == 0) throw new IllegalArgumentException("can not match at least one key");
        if (confidence.length > 1) System.out.println("There are 2 potential value for the mask!");
        int result = 0;
        int conf = 0;
        for (int idx : confidence) {
            if (confidence[idx] > conf) {
                result = idx;
                conf = confidence[idx];
            }
        }
        return result;
    }

    private byte[] Matrix44;
    private byte[] Matrix128;
    private byte[] Matrix58;
    private byte Super58A;
    private byte Super58B;

    public QmcMask(byte[] matrix) {
        this(matrix, null, null);
    }

    public QmcMask(byte[] matrix, Byte superA, Byte superB) {
        if (superA == null || superB == null) {
            System.out.println("QmcMask matrix.length = " + matrix.length);
            if (matrix.length == 44) {
                this.Matrix44 = matrix;
                this.generateMask128from44();
            } else {
                this.Matrix128 = matrix;
                this.generateMask44from128();
            }
            this.generateMask58from128();
        } else {
            this.Matrix58 = matrix;
            this.Super58A = superA;
            this.Super58B = superB;
            this.generateMask128from58();
            this.generateMask44from128();
        }
    }

    private void generateMask128from58() {
        if (this.Matrix58.length != 56) throw new IllegalArgumentException("incorrect mask58 matrix length");

        byte[] matrix128 = new byte[0];
        for (int rowIdx = 0; rowIdx < 8; rowIdx++) {
            matrix128 = ArrayUtils.addAll(matrix128, this.Super58A);
            matrix128 = ArrayUtils.addAll(matrix128, Arrays.copyOfRange(this.Matrix58, 7 * rowIdx, 7 * rowIdx + 7));
            matrix128 = ArrayUtils.addAll(matrix128, this.Super58B);

            byte[] array = Arrays.copyOfRange(this.Matrix58, 56 - 7 - 7 * rowIdx, 56 - 7 * rowIdx);
            ArrayUtils.reverse(array);
            matrix128 = ArrayUtils.addAll(matrix128, array);
        }
        this.Matrix128 = matrix128;
    }

    // TODO check
    private void generateMask58from128() {
        if (this.Matrix128.length != 128) throw new IllegalArgumentException("incorrect mask128 length");

        byte superA = this.Matrix128[0];
        byte superB = this.Matrix128[8];
        byte[] matrix58 = new byte[0];

        for (int rowIdx = 0; rowIdx < 8; rowIdx++) {
            int lenStart = 16 * rowIdx;
            int lenRightStart = 120 - lenStart;
            if (this.Matrix128[lenStart] != superA || this.Matrix128[lenStart + 8] != superB) {
                throw new IllegalArgumentException("decode mask-128 to mask-58 failed");
            }
            byte[] rowLeft = Arrays.copyOfRange(this.Matrix128, lenStart + 1, lenStart + 8);
            byte[] array = Arrays.copyOfRange(this.Matrix128, lenRightStart + 1, lenRightStart + 8);
            ArrayUtils.reverse(array);
            byte[] rowRight = array;
            if (Arrays.equals(rowLeft, rowRight)) {
                matrix58 = ArrayUtils.addAll(matrix58, rowLeft);
            } else {
                throw new IllegalArgumentException("decode mask-128 to mask-58 failed");
            }
        }

        this.Matrix58 = matrix58;
        this.Super58A = superA;
        this.Super58B = superB;
    }

    // TODO check
    private void generateMask44from128() {
        if (this.Matrix128.length != 128) throw new IllegalArgumentException("incorrect mask128 matrix length");
        byte[][] mapping = GetConvertMapping();
        this.Matrix44 = new byte[0];
        int idxI44 = 0;
        for (byte[] it256 : mapping) {
            if (it256 == null) {
                continue;
            }

            int it256Len = it256.length;
            for (int i = 1; i < it256Len; i++) {
                if (this.Matrix128[it256[0]] != this.Matrix128[it256[i]]) {
                    throw new IllegalArgumentException("decode mask-128 to mask-44 failed");
                }
            }
            this.Matrix44[idxI44] = this.Matrix128[it256[0]];
            idxI44++;
        }
    }

    private void generateMask128from44() {
        if (this.Matrix44.length != 44) {
            throw new IllegalArgumentException("incorrect mask length");
        }

        this.Matrix128 = new byte[128];

        int idx44 = 0;
        for (byte[] it256 : GetConvertMapping()) {
            if (it256 == null) {
                continue;
            }

            for (byte m : it256) {
                this.Matrix128[m] = this.Matrix44[idx44];
            }

            idx44++;
        }

        System.out.println("generateMask128from44 = " + Arrays.toString(this.Matrix128));
    }

    public byte[] decrypt(byte[] data) {
        byte[] dst = Arrays.copyOfRange(data, 0, data.length);
        int index = -1;
        int maskIdx = -1;
        for (int cur = 0; cur < data.length; cur++) {
            index++;
            maskIdx++;
            if (index == 0x8000 || (index > 0x8000 && (index + 1) % 0x8000 == 0)) {
                index++;
                maskIdx++;
            }
            if (maskIdx >= 128) maskIdx -= 128;
            dst[cur] ^= this.Matrix128[maskIdx];
        }
        return dst;
    }


//    static String arraysToString(byte[][] bytes) {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        stringBuilder.append("[\n");
//
//        int empty = 0;
//
//        for (int i = 0; i < bytes.length; i++) {
//            byte[] array = bytes[i];
//
//            if (array != null) {
//                stringBuilder.append(Arrays.toString(array));
//
//                stringBuilder.append(",\n");
//            } else {
//                empty++;
//                if (i + 1 == bytes.length || bytes[i + 1] != null) {
//                    stringBuilder.append(String.format("<%d empty items>", empty));
//                    empty = 0;
//
//                    stringBuilder.append(",\n");
//                }
//            }
//        }
//
//        stringBuilder.append("]");
//
//        return stringBuilder.toString();
//    }
}
