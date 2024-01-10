package org.rainbowx.javaserver.Controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.rainbowx.javaserver.Bean.Chat;
import org.rainbowx.javaserver.Service.ChatService;
import org.rainbowx.javaserver.Utiles.JSONObjectParser;
import org.rainbowx.javaserver.Utiles.LoginMap;
import org.rainbowx.javaserver.Utiles.ParameterChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ChatController {
    final ChatService chatService;
    final Logger logger = Logger.getLogger("ChatController");

    @Autowired
    public ChatController(ChatService service) {
        this.chatService = service;
    }

    @RequestMapping(value = "/api/chat/send")
    public JSONObject sendHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(ParameterChecker.check(requestBody, "uuid", "target", "content")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        int target;
        String uuid, content;
        uuid = requestBody.getString("uuid");
        target = requestBody.getIntValue("target");
        content = requestBody.getString("content");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        int origin = LoginMap.getUid(uuid);
        int cid = chatService.addChat(origin, target, content);

        if (cid == -1) {
            ret.put("code", -1);
            ret.put("reason", "发送消息失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");

            ret.put("cid", cid);
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/chat/history")
    public JSONObject historyHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(ParameterChecker.check(requestBody, "uuid")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        String uuid;
        Integer target;
        uuid = requestBody.getString("uuid");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        int uid = LoginMap.getUid(uuid);
        if(!requestBody.containsKey("target")) target = null;
        else target = requestBody.getIntValue("target");

        List<Chat> chats;
        if(target == null || target == -1)
            chats = chatService.queryChats(uid);
        else chats = chatService.queryChats(uid, target);

        if (chats == null) {
            ret.put("code", -1);
            ret.put("reason", "获取历史记录失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");

            JSONArray res = getRes(chats, uid);

            ret.put("res", res);
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    private static JSONArray getRes(List<Chat> chats, int uid) {
        JSONArray res = new JSONArray();
        for (Chat chat: chats) {
            int other,order;
            JSONObject obj = new JSONObject();

            obj.put("cid", chat.getCid());

            if(chat.getSource().getUid() == uid) {
                order = 1;
                other = chat.getDest().getUid();
            }
            else {
                order = -1;
                other = chat.getSource().getUid();
            }

            obj.put("other", other);
            obj.put("order", order);
            obj.put("content", chat.getContent());

            res.add(obj);
        }
        return res;
    }
}
