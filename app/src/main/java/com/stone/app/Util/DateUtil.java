package com.stone.app.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Brandon Zhang on 2017/9/7.
 */

public class DateUtil {



    public static long getDate() {
        SimpleDateFormat formatter   =  new SimpleDateFormat   ("yyyyMMddHHmmss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        return Long.parseLong(str);

    }
}
