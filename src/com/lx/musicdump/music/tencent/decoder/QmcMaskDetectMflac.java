package com.lx.musicdump.music.tencent.decoder;

import com.lx.musicdump.music.tencent.QmcMask;

public class QmcMaskDetectMflac extends QmcMaskDecoder {

    private QmcMask qmcMask;

    public QmcMaskDetectMflac(byte[] data) {
        super(data);

        qmcMask = QmcMask.QmcMaskDetectMflac(data);
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        return qmcMask.decrypt(bytes);
    }
}
