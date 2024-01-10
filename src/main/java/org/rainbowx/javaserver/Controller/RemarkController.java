package org.rainbowx.javaserver.Controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.rainbowx.javaserver.Bean.Article;
import org.rainbowx.javaserver.Bean.Remark;
import org.rainbowx.javaserver.DAO.RemarkRepository;
import org.rainbowx.javaserver.Service.ArticleService;
import org.rainbowx.javaserver.Service.RemarkService;
import org.rainbowx.javaserver.Utiles.JSONObjectParser;
import org.rainbowx.javaserver.Utiles.LoginMap;
import org.rainbowx.javaserver.Utiles.ParameterChecker;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class RemarkController {
    RemarkService remarkService;
    Logger logger = Logger.getLogger("RemarkController");
    public RemarkController(RemarkService service){
        this.remarkService = service;
    }

    @RequestMapping(value = "/api/remark/release")
    public JSONObject releaseHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(!ParameterChecker.check(requestBody, "aid", "uuid", "content")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        int aid;
        String uuid, content;
        aid = requestBody.getInteger("aid");
        uuid = requestBody.getString("uuid");
        content = requestBody.getString("content");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        int uid = LoginMap.getUid(uuid);
        int rid = remarkService.release(aid, uid, content);

        if (rid == -1) {
            ret.put("code", -1);
            ret.put("reason", "发送评论失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/remark/show")
    public JSONObject showHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(!ParameterChecker.check(requestBody, "uuid", "aid")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        int aid;
        String uuid;
        aid = requestBody.getInteger("aid");
        uuid = requestBody.getString("uuid");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        List<Remark> remarks = remarkService.queryByAid(aid);

        if (remarks == null) {
            ret.put("code", -1);
            ret.put("reason", "查询评论失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");

            JSONArray res = new JSONArray();
            for(Remark remark: remarks) {
                JSONObject obj = new JSONObject();
                obj.put("rid", remark.getRid());
                obj.put("time", remark.getTime());
                obj.put("author", remark.getAuthor().getUid());
                obj.put("content", remark.getContent());
                res.add(obj);
            }
            ret.put("res", res);
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }
    @RequestMapping(value = "/api/remark/modify")
    public JSONObject modifyHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(!ParameterChecker.check(requestBody, "rid", "uuid", "content")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        int rid;
        String uuid, content;
        rid = requestBody.getInteger("rid");
        uuid = requestBody.getString("uuid");
        content = requestBody.getString("content");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        int res = remarkService.modify(rid, content);

        if (res == -1) {
            ret.put("code", -1);
            ret.put("reason", "修改评论失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/remark/delete")
    public JSONObject deleteHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(!ParameterChecker.check(requestBody, "uuid", "rid")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        int rid;
        String uuid;
        rid = requestBody.getInteger("rid");
        uuid = requestBody.getString("uuid");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        int res = remarkService.delete(rid);

        if (res == -1) {
            ret.put("code", -1);
            ret.put("reason", "删除评论失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }
}
