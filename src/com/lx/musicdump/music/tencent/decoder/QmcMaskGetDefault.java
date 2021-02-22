package com.lx.musicdump.music.tencent.decoder;

import com.lx.musicdump.music.tencent.QmcMask;

public class QmcMaskGetDefault extends QmcMaskDecoder {

    private QmcMask qmcMask;

    public QmcMaskGetDefault(byte[] data) {
        super(data);

        qmcMask = QmcMask.QmcMaskGetDefault();
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        return qmcMask.decrypt(bytes);
    }
}
