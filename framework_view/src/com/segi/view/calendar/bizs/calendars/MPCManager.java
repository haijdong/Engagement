package com.segi.view.calendar.bizs.calendars;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.segi.view.calendar.bizs.languages.DPLManager;
import com.segi.view.calendar.entities.DPInfo;

/**
 * 月份管理器
 * The manager of date picker.
 *
 * @author AigeStudio 2015-06-12
 */
public final class MPCManager {
    @SuppressLint("UseSparseArrays")
	private static final HashMap<Integer, DPInfo[][]> DATE_CACHE = new HashMap<Integer, DPInfo[][]>();

    private static final HashMap<String, Set<String>> DECOR_CACHE_BG = new HashMap<String, Set<String>>();
//    private static final HashMap<String, Set<String>> DECOR_CACHE_TL = new HashMap<String, Set<String>>();
//    private static final HashMap<String, Set<String>> DECOR_CACHE_T = new HashMap<String, Set<String>>();
//    private static final HashMap<String, Set<String>> DECOR_CACHE_TR = new HashMap<String, Set<String>>();
//    private static final HashMap<String, Set<String>> DECOR_CACHE_L = new HashMap<String, Set<String>>();
//    private static final HashMap<String, Set<String>> DECOR_CACHE_R = new HashMap<String, Set<String>>();
//    private static final HashMap<String, Set<String>> DECOR_CACHE_ON_TIME = new HashMap<String, Set<String>>();
//    private static final HashMap<String, Set<String>> DECOR_CACHE_NOT_ON_TIME = new HashMap<String, Set<String>>();

    private static MPCManager sManager;
    private DPLManager sDPLManager;
    

    private DPCalendar c;

    @SuppressLint("DefaultLocale")
	private MPCManager() {
        // 默认显示为中文日历
        String locale = Locale.getDefault().getCountry().toLowerCase();
        if (locale.equals("cn")) {
            initCalendar(new DPCNCalendar());
        } else {
            initCalendar(new DPUSCalendar());
        }
        sDPLManager = DPLManager.getInstance();
    }

    /**
     * 获取月历管理器
     * Get calendar manager
     *
     * @return 月历管理器
     */
    public static MPCManager getInstance() {
        if (null == sManager) {
            sManager = new MPCManager();
        }
        return sManager;
    }

    /**
     * 初始化日历对象
     * <p/>
     * Initialization Calendar
     *
     * @param c ...
     */
    public void initCalendar(DPCalendar c) {
        this.c = c;
    }

    
    /**
     * 设置有背景标识物的日期
     * <p/>
     * Set date which has decor of background
     *
     * @param date 日期列表 List of date
     */
    public void setDecorBG(List<String> date) {
        setDecor(date, DECOR_CACHE_BG);
    }


    /**
     * 获取指定年月的日历对象数组
     *
     * @param year  公历年
     * @param month 公历月
     * @return 日历对象数组 该数组长度恒为6x7 如果某个下标对应无数据则填充为null
     */
    public DPInfo[][] obtainDPInfo(int year) {
        DPInfo[][] dataOfYear = DATE_CACHE.get(year);
        if (null != dataOfYear && dataOfYear.length > 0) {
            return dataOfYear;
        }
        if (null == dataOfYear) dataOfYear = buildDPInfo(year);
        DATE_CACHE.put(year, dataOfYear);
        return dataOfYear;
    }

    private void setDecor(List<String> date, HashMap<String, Set<String>> cache) {
        for (String str : date) {
            int index = str.lastIndexOf("-");
            String key = str.substring(0, index - 1);
            Set<String> days = cache.get(key);
            if (null == days) {
                days = new HashSet<String>();
            }
            days.add(str.substring(index + 1, str.length()));
            cache.put(key, days);
        }
    }

    private DPInfo[][] buildDPInfo(int year) {
        DPInfo[][] info = new DPInfo[2][6];
        Set<String> decorBG = DECOR_CACHE_BG.get(year);
        int index = 0;
        for (int i = 0; i < info.length; i++) {
            for (int j = 0; j < info[i].length; j++) {
                DPInfo tmp = new DPInfo();
                tmp.strG = Integer.toString(index+1);
                tmp.strF = sDPLManager.getMonthName(index);
                index++;
                if (!TextUtils.isEmpty(tmp.strG)) tmp.isToday = c.isThisMonth(year, index);
                if (null != decorBG && decorBG.contains(tmp.strG)) tmp.isDecorBG = true;
                info[i][j] = tmp;
            }
        }
        return info;
    }
}
