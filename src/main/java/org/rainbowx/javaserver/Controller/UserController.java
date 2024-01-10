package org.rainbowx.javaserver.Controller;

import com.alibaba.fastjson2.JSONObject;
import org.rainbowx.javaserver.Bean.User;
import org.rainbowx.javaserver.Service.UserService;
import org.rainbowx.javaserver.Utiles.HashUtils;
import org.rainbowx.javaserver.Utiles.JSONObjectParser;
import org.rainbowx.javaserver.Utiles.LoginMap;
import org.rainbowx.javaserver.Utiles.ParameterChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class UserController {
    final UserService userService;
    final Logger logger = Logger.getLogger("UserController");

    @Autowired
    UserController(UserService service) {
        this.userService = service;
    }

    @RequestMapping(value = "/api/user/login")
    public JSONObject loginHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(ParameterChecker.check(requestBody, "uname", "passwd")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        String userName, passWd;
        userName = requestBody.getString("uname");
        passWd = requestBody.getString("passwd");

        User user = userService.findUserByUserName(userName);

        String inputSha256, correctSha256, inputPassWd;

        if (user != null) {
            correctSha256 = user.getPassHashSha256();
            inputPassWd = passWd + user.getPassSalt();
            inputSha256 = HashUtils.calculateSHA256(inputPassWd);
        } else {
            correctSha256 = "";
            inputSha256 = "no";
        }

        if (inputSha256.equals(correctSha256)) {
            int uid = user.getUid();
            String uuid = LoginMap.addUser(uid);

            ret.put("code", 0);
            ret.put("reason", "ok");

            ret.put("uid", uid);
            ret.put("uuid", uuid);
        } else {
            ret.put("code", -1);
            ret.put("reason", "账号或密码错误");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/user/signup")
    public JSONObject signupHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(ParameterChecker.check(requestBody, "uname", "passwd")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        String userName, passWd;
        userName = requestBody.getString("uname");
        passWd = requestBody.getString("passwd");

        int uid;
        String passSha256, salt = UUID.randomUUID().toString(), passWithSalt = passWd + salt;
        passSha256 = HashUtils.calculateSHA256(passWithSalt);
        uid = userService.signup(userName, passSha256, salt);
        if(uid == -1){
            ret.put("code", -1);
            ret.put("reason", "Fail to signup.");
        }
        else {
            ret.put("code", 0);
            ret.put("reason", "ok");

            ret.put("uid", uid);
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/user/modifyPassWd")
    public JSONObject modifyPassWdHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(ParameterChecker.check(requestBody, "uuid", "passwd")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        String uuid = requestBody.getString("uuid");
        String passWd = requestBody.getString("passwd");

        if(uuid == null || passWd == null) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        int uid = LoginMap.getUid(uuid);
        String passSha256, salt = UUID.randomUUID().toString(), passWithSalt = passWd + salt;
        passSha256 = HashUtils.calculateSHA256(passWithSalt);
        if(userService.modifyPassWd(uid, passSha256, salt) != 0){
            ret.put("code", -1);
            ret.put("reason", "Fail to modify password");
        }
        else {
            ret.put("code", 0);
            ret.put("reason", "ok");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/user/modify")
    public JSONObject modifyHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(ParameterChecker.check(requestBody, "uuid")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        String uuid = requestBody.getString("uuid");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        int uid = LoginMap.getUid(uuid);

        LocalDateTime birthday;
        String avatar, nickName, sign, gender;

        sign = requestBody.getString("sign");
        gender = requestBody.getString("gender");
        avatar = requestBody.getString("avatar");
        nickName = requestBody.getString("nickname");
        birthday = requestBody.getDate("birthday").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if(userService.modify(uid, avatar, nickName, sign, birthday, gender) == -1){
            ret.put("code", -1);
            ret.put("reason", "Fail to modify");
        }
        else {
            ret.put("code", 0);
            ret.put("reason", "ok");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/user/find")
    public JSONObject findHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(ParameterChecker.check(requestBody, "uid")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        int uid = requestBody.getIntValue("uid");

        User user = userService.findUserByUid(uid);

        if(user == null){
            ret.put("code", -1);
            ret.put("reason", "Fail to find");
        }
        else {
            ret.put("code", 0);
            ret.put("reason", "ok");

            ret.put("uid", user.getUid());
            ret.put("avatar", user.getAvatar());
            ret.put("nickname", user.getNickName());
            ret.put("sign", user.getSign());
            ret.put("birthday", user.getBirthday());
            ret.put("gender", user.getGender());
            ret.put("reg_date", user.getRegistrationDate());
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/user/active")
    public JSONObject aliveHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(ParameterChecker.check(requestBody, "uuid")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        String uuid = requestBody.getString("uuid");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "The specified user is not active.");
        }
        else {
            ret.put("code", 0);
            ret.put("reason", "ok");
        }

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }

    @RequestMapping(value = "/api/user/logout")
    public JSONObject logoutHandling(@RequestBody(required = false) String requestStr) {
        JSONObject requestBody = JSONObjectParser.parseToJSONObject(requestStr);
        JSONObject ret = JSONObjectParser.getPreparedResult(requestBody);
        if (requestBody == null) return ret;

        if(ParameterChecker.check(requestBody, "uuid")) {
            ret.put("code", -1);
            ret.put("reason", "参数错误");
            return ret;
        }

        String uuid = requestBody.getString("uuid");

        if(!LoginMap.contains(uuid)){
            ret.put("code", -1);
            ret.put("reason", "登录令牌已过期");
            return ret;
        }

        LoginMap.remove(uuid);
        ret.put("code", 0);
        ret.put("reason", "ok");

        logger.log(Level.INFO, requestBody.toString());

        return ret;
    }
}
