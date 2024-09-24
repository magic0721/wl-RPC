package com.wl.loadbalance;

import com.wl.common.URL;

import java.util.List;
import java.util.Random;

public class Loadbalance {

    public static URL random(List<URL> urls){
        Random random = new Random();
        int i = random.nextInt(urls.size());
        return urls.get(i);
    }
}
