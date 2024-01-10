package org.rainbowx.javaserver.Utiles;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class LoginMap {
    protected static Map<Integer, String> Uid2Uuid;
    protected static Map<String, Integer> Uuid2Uid;

    static {
        Uid2Uuid = new TreeMap<>();
        Uuid2Uid = new TreeMap<>();
    }

    public static synchronized boolean contains(int uid) {
        return Uid2Uuid.containsKey(uid);
    }

    public static synchronized boolean contains(String uuid) {
        return Uuid2Uid.containsKey(uuid);
    }

    @SuppressWarnings("unused")
    public static synchronized String getUuid(int uid) {
        if(!Uid2Uuid.containsKey(uid)) throw new RuntimeException("这个uid没有登录过: " + uid);
        return Uid2Uuid.get(uid);
    }

    public static synchronized int getUid(String uuid) {
        if(!Uuid2Uid.containsKey(uuid)) throw new RuntimeException("这个uuid没有登录过: " + uuid);
        return Uuid2Uid.get(uuid);
    }

    public static synchronized void remove(int uid) {
        if(contains(uid)){
            String uuid = Uid2Uuid.get(uid);
            Uuid2Uid.remove(uuid);
            Uid2Uuid.remove(uid);
        }
    }

    public static synchronized void remove(String uuid) {
        if(contains(uuid)){
            int uid = Uuid2Uid.get(uuid);
            Uid2Uuid.remove(uid);
            Uuid2Uid.remove(uuid);
        }
    }

    public static synchronized String addUser(int uid) {
        if(contains(uid)) remove(uid);
        String uuid = UUID.randomUUID().toString();
        Uid2Uuid.put(uid, uuid);
        Uuid2Uid.put(uuid, uid);
        return uuid;
    }
}
