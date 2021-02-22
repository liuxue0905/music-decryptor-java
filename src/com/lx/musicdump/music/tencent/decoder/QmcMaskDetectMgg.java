package com.lx.musicdump.music.tencent.decoder;

import com.lx.musicdump.music.tencent.QmcMask;

public class QmcMaskDetectMgg extends QmcMaskDecoder {

    private QmcMask qmcMask;

    public QmcMaskDetectMgg(byte[] data) {
        super(data);

        qmcMask = QmcMask.QmcMaskDetectMgg(data);
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        return qmcMask.decrypt(bytes);
    }
}
