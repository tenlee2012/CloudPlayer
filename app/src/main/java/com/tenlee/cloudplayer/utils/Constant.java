package com.tenlee.cloudplayer.utils;

import java.util.HashMap;

/**
 * Created by tenlee on 16-3-11.
 * 内容,放常量
 */
public class Constant {

    public static String DB_NAME = "cloud_music_love_music";
    public static String SP_NAME = "cloudMusic";

    public static String PLAY_MODE = "playMode";

    public static String CURRENTPOSITIONINMP3LIST = "currentPositionInMp3list";

    public static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 "
            + "(KHTML, like Gecko) Chrome/49.0.2612.0 Safari/537.36 OPR/36.0.2120.0 "
            + "(Edition developer)";

    public static HashMap<String, String> SEARCH_TYPE_MAP = new HashMap<String, String>() {{
        put("百度音乐", "bd");
        put("QQ音乐", "qq");
        put("咪咕音乐", "mg");
        put("天天动听", "tt");
        put("虾米音乐", "xm");
        put("多米音乐", "dm");
        put("萌否电台", "mf");
        put("5Sing原创", "fs");
        put("电信iMusic", "dx");

    }};
}
