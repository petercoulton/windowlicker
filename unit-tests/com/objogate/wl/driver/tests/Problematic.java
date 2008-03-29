package com.objogate.wl.driver.tests;

import java.lang.annotation.*;
import com.objogate.wl.Platform;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Problematic {
    String why() default "Doesn't work reliably";
    Platform[] platform();
}
