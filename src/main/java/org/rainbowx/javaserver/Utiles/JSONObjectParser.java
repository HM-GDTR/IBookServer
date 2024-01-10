package org.rainbowx.javaserver.Utiles;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONObjectParser {
    public static final Logger logger = Logger.getLogger("JSONObjectParser");
    public static JSONObject parseToJSONObject(String jsonStr){
        if(jsonStr == null) return null;
        JSONObject ret;
        try {
            ret = JSONObject.parse(jsonStr);
        } catch (JSONException e) {
            logger.log(Level.SEVERE,"Fail to parse request body to json. Detail:\n"+e);
            return null;
        }
        return ret;
    }

    public static JSONObject getPreparedResult(JSONObject obj) {
        JSONObject ret = new JSONObject();
        if(obj == null) {
            ret.put("code", -1);
            ret.put("reason", "Fail to parse request body to json.");
            logger.log(Level.SEVERE, "Fail to parse request body to json.");
        }
        return ret;
    }
}
