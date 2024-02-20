package com.example.demo.testB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ClassC {
    protected Logger log = LoggerFactory.getLogger(ClassC.class);
    public void common() {
        log.info("😁😁😁Smile");
    }

    public abstract void read();
}
