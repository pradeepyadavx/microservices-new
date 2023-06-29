package com.pradeep.productservice.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Component
public class FooService {

    @Autowired
    @Qualifier("barFormatter")
    private Formatter formatter;

    @PostConstruct
    public void print(){
        System.out.println( formatter.format());
        System.out.println(Instant.now());
    }
}
