package com.lx.musicdump.music.tencent;

import com.lx.musicdump.music.tencent.decoder.QmcMaskDecoder;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {TYPE})
public @interface ExtentionConfiguration {
    //    Item[] value() default {};
    Item[] value();

    public @interface Item {
        Class<? extends QmcMaskDecoder> handler();

        String extensionIn();

        String extensionOut();

        boolean detect();
//        boolean detect() default false;
    }
}