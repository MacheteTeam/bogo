package com.macheteteam.bogo.utils;

import java.util.Collection;

/**
 * Created by xudi on 2017/8/20.
 */
public class CollectionUtils {
    public static boolean isNotEmpty(Collection collection){
        if(collection!=null){
            if(collection.size()>0){
                return true;
            }
        }
        return false;
    }
}
