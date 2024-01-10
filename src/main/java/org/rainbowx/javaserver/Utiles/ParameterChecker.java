package org.rainbowx.javaserver.Utiles;

import com.alibaba.fastjson2.JSONObject;

public class ParameterChecker {
    public static boolean check(JSONObject obj, String... argNames){
        for(String arg: argNames) {
            if(!obj.containsKey(arg)) return false;
        }
        return true;
    }
}
