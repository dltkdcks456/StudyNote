package com.example.demo.testA;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwithBImpl implements ClassAwithB{
    static Logger log = LoggerFactory.getLogger(AwithBImpl.class);

    @Override
    public void read() {
        log.info("책을 읽자");
    }

    @Override
    public void write() {
        log.info("글을 쓰자");
    }
}
