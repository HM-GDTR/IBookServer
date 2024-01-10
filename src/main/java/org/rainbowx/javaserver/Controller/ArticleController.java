package org.rainbowx.javaserver.Controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.rainbowx.javaserver.Bean.Article;
import org.rainbowx.javaserver.Service.ArticleService;
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
public class ArticleController {
    ArticleService articleService;
    Logger logger = Logger.getLogger("ArticleController");

    public ArticleController(ArticleService service){
        this.articleService = service;
    }

    @RequestMapping(value = "/api/article/release")
    public JSONObject releaseHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(!ParameterChecker.check(requestBody, "uuid", "title", "content")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        String uuid, title, content;
        uuid = requestBody.getString("uuid");
        title = requestBody.getString("title");
        content = requestBody.getString("content");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        int uid = LoginMap.getUid(uuid);
        int aid = articleService.release(uid, title, content);

        if (aid == -1) {
            ret.put("code", -1);
            ret.put("reason", "发送文章失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/article/show")
    public JSONObject showHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(!ParameterChecker.check(requestBody, "uuid")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        String uuid, title, content;
        uuid = requestBody.getString("uuid");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        List<Article> articles = articleService.query();

        if (articles == null) {
            ret.put("code", -1);
            ret.put("reason", "查询文章失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");

            JSONArray res = new JSONArray();
            for(Article article: articles) {
                JSONObject obj = new JSONObject();
                obj.put("aid", article.getAid());
                obj.put("author", article.getAuthor().getUid());
                obj.put("title", article.getTitle());
                obj.put("time", article.getTime());
                obj.put("content", article.getContent());
                res.add(obj);
            }
            ret.put("res", res);
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }
    @RequestMapping(value = "/api/article/modify")
    public JSONObject modifyHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(!ParameterChecker.check(requestBody, "uuid", "aid", "content")) {
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

        int res = articleService.modify(aid, content);

        if (res == -1) {
            ret.put("code", -1);
            ret.put("reason", "修改文章失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/article/delete")
    public JSONObject deleteHandling(@RequestBody(required = false) String requestStr) {
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

        int res = articleService.delete(aid);

        if (res == -1) {
            ret.put("code", -1);
            ret.put("reason", "删除文章失败");
        } else {
            ret.put("code", 0);
            ret.put("reason", "ok");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }
}
