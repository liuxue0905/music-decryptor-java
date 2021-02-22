package com.lx.musicdump.music.xiami;

import com.lx.musicdump.music.Extension;
import com.lx.musicdump.music.MusicFile;

@Extension({"xm"})
public class XiamiMusicFile extends MusicFile {
    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    protected String getExtension() {
        return null;
    }

    @Override
    protected void _dump(String pathname) {

    }
}
