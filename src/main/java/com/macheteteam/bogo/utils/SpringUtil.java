package com.macheteteam.bogo.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by xudi on 2017/8/13.
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(String name) {
        if(context != null) {
            try {
                return (T) context.getBean(name);
            } catch (Throwable var2) {
                var2.printStackTrace();
            }
        }

        return null;
    }
}
