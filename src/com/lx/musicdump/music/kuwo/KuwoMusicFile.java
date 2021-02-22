package com.lx.musicdump.music.kuwo;

import com.lx.musicdump.music.Extension;
import com.lx.musicdump.music.MusicFile;

@Extension({"kwm"})
public class KuwoMusicFile extends MusicFile {
    public KuwoMusicFile(String path) {
        super(path);
    }

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
