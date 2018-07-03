package cn.segi.framework.util;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author kuangyong
 */
public class StringUtils {
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static String PHONE = "^1[3,4,5,7,8]\\d{9}$";

    private final static String ID_CARD = "([0-9]{17}([0-9]|X))|([0-9]{15})";


    private final static String ZIP_CODE = "[0-9]\\d{5}(?!\\d)";
    
    /**
     * 系统路径分割符
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> HourFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        if (StringUtils.isBlank(sdate))
            return null;
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input) || "-1".equals(input) || "NULL".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Object arg0) {
        if (arg0 == null)
            return true;
        return false;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是否为手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {

        return phoneNumber.matches(PHONE);
    }

    /**
     * 判断是否是邮政编码
     *
     * @param zipCode
     * @return
     */
    public static boolean isZipCode(String zipCode) {

        return zipCode.matches(ZIP_CODE);
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    public static String getURLEncoder(String eStr) {
        String str = "";
        try {
            str = URLEncoder.encode(eStr, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p/>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return
     * true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }


    /**
     * 像素值转换成dp
     *
     * @param mContext
     * @param px
     * @return
     */
    public static int PxToDp(Context mContext, int px) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    private static long lastClickTime;

    /**
     * 防止按钮连续点击,导致界面或方法执行多次
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    // 将绝对路径转为
    public static String replaceImgSrc(String content, String replaceHttp) {
        // content = content.replace("\n", "");
        // content = content.replace("\r", "");
        // content = content.replace("\t", "");
        // String patternStr = "^.*<img\\s*.*\\s*src=\\\"(.*?)\\\"\\s*.*>.*$";
        // Pattern pattern = Pattern.compile(patternStr);
        if (content != null) {
            Pattern pattern = Pattern.compile("<img[^<>]*?\\ssrc=['\"]?(.*?)['\"]?\\s.*?>", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);
            // 如果匹配到了img
            while (matcher.find()) {
                if (matcher.group(1).indexOf("http") < 0)
                    content = content.replaceAll(matcher.group(1), (replaceHttp + matcher.group(1)));
            }
        }
        return content;
    }

    /**
     * 判断是否是一个http url
     *
     * @param url
     * @return
     */
    public static boolean isHttpUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith("http") || url.startsWith("www.");
    }

    /**
     * 解析一个url地址，将其参数以map方式返回
     *
     * @param url
     * @return
     */
    public static Map<String, String> parseHttpParams(String url) {
        Map<String, String> params = new HashMap<String, String>();

        if (url.contains("?") && url.contains("=")) {
            String paramsArray = url.substring((url.indexOf("?") + 1));
            String[] array = paramsArray.split("&");
            String[] keyValue = null;
            for (int i = 0; i < array.length; i++) {
                keyValue = array[i].split("=");
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }

    /*
     * 将手机号码的中间四位替换为“*”
     */
    public static String ensurePhoneNum(String phoneNum) {
        if (null != phoneNum) {
            try {
                if (phoneNum.contains("*")) {

                    // 字符串本身已经是带*号的，不做处理，直接返回
                    return phoneNum;
                } else {
                    return phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, 11);
                }
            } catch (Exception e) {
                //防止字符串截取，越界异常
                return phoneNum;
            }
        }
        return phoneNum;
    }

    /*
     * 将邮箱号码的中间四位替换为“*”
     */
    public static String ensureEmailAddress(String emailAddr) {
        try {
            if (emailAddr.contains("***")) {
                return emailAddr;
            }
            return emailAddr.substring(0, 2) + "****" + emailAddr.substring(6, emailAddr.length());
        } catch (Exception e) {
            return emailAddr;
        }
    }

    /*
     * 判断密码强度 1、纯数字或者字母或特殊字符的时候为弱 2、数字+字母，数字+特殊字符，字母+特殊字符为中 3、数字+字母+特殊字符为强
     */
    public static int checkPwLevel(String pw) {
        int numCount = 0;
        int letterCount = 0;
        int specialChar = 0;
        if (pw.length() <= 6) {
            return 1;
        } else {
            for (int i = 0; i < pw.length(); i++) {
                if (Character.isDigit(pw.charAt(i))) {
                    numCount = 1;
                } else if (Character.isLetter(pw.charAt(i))) {
                    letterCount = 1;
                } else {
                    specialChar = 1;
                }
            }
            return numCount + letterCount + specialChar;
        }

    }

    /**
     * unicode 转换成 中文
     *
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {

        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {

            aChar = theString.charAt(x++);

            if (aChar == '\\') {
                aChar = theString.charAt(x++);

                if (aChar == 'u') {

                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {

                        aChar = theString.charAt(x++);

                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /**
     * 给imgUrl添加图片尺寸
     *
     * @param imgUrls
     * @param picSize
     * @return
     */
    public static ArrayList<String> getImageUrls(ArrayList<String> imgUrls, String picSize) {
        ArrayList<String> newUrls = new ArrayList<String>();
        for (int i = 0; i < imgUrls.size(); i++) {

            String newUrl = addPicSizeInImgUrl(imgUrls.get(i), picSize);
            newUrls.add(i, newUrl);
        }
        return newUrls;
    }

    /**
     * 给imgUrl添加图片尺寸
     *
     * @param originUrl
     * @param picSize
     * @return
     */
    public static String addPicSizeInImgUrl(String originUrl, String picSize) {

        if (TextUtils.isEmpty(picSize)) {
            return originUrl;
        }

        int index = originUrl.lastIndexOf("/");
        int point = originUrl.lastIndexOf(".");

        String pre = originUrl.substring(0, index + 1);
        String picName = originUrl.substring(index + 1, point);
        String end = originUrl.substring(point);

        //则说明图片本身就是带了nnnXnnn尺寸的图片url，需要重新处理
        if (picName.contains("_") && picName.contains("X")) {
            picName = picName.split("_")[0];
        }

        originUrl = pre + picName + "_" + picSize + end;
        return originUrl;
    }

    /**
     * 将手机号码中间几位替换成*
     *
     * @param phoneNum
     * @return
     */
    public static String changePhoneForrmat(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return null;
        } else if (phoneNum.length() < 11) {
            return null;
        }
        String forrmatNum = "";
        int leghth = phoneNum.length();
        String start = phoneNum.substring(0, 3);
        String end = phoneNum.substring(leghth - 4, leghth);
        forrmatNum = start + "******" + end;
        return forrmatNum;
    }

    /**
     * 将邮箱名2位以后的替换成*
     *
     * @param email
     * @return
     */
    public static String changeEmailForrmat(String email) {
        if (TextUtils.isEmpty(email)) {
            return null;
        }
        if (!email.contains("@")) {
            return null;
        }
        String forrmatNum = "";
        int index = email.indexOf("@");
        String end = email.substring(index, email.length());
        String start = "";
        StringBuffer sb = new StringBuffer();
        if (index < 3) {
            for (int i = 0; i < 6 - index; i++) {
                sb.append("*");
            }
            start = email.substring(0, index) + sb;
        } else {
            sb.append("****");
            start = email.substring(0, 2) + sb;
        }
        forrmatNum = start + end;
        return forrmatNum;
    }


    /**
     * 判断是否为身份证号码
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {

        return idCard.matches(ID_CARD);
    }

    /**
     * 获得非null的返回值
     */
    public static String notNull(String content) {
        if (content == null) {
            return "";
        }
        return content;
    }

    /**
     * 判断字符串是否保存特殊字符
     *
     * @param text
     * @return
     */
    public static boolean hasSpecialString(String text) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * 将Stringlist转换成以“，”分割的字符串
     *
     * @param stringList
     * @return
     */
    public static String listToStringWithComma(List<String> stringList) {
        if (null == stringList || stringList.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String str : stringList) {
            if (sb.length() == 0) {
                sb.append(str);
            } else {
                sb.append("," + str);
            }
        }
        return sb.toString();
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为null或空值
     * 
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     * 
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     * 
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }
    
    /**
     * 去除字符串空格
     * 
     * @param str 字符串
     * @return 字符串为空则返回空
     */
    public static String trim(String str) {
        return str != null ? str.trim() : str;
    }
    
    /**
     * 将字符串list转化为字符串，默认以","分隔
     * 
     * @param strList 字符串list
     * @param split 分隔符，默认为","
     * @return 组装后的字符串对象
     */
    public static String listToString(Collection<String> strList, String split) {
        String[] values = null;
        if (strList != null) {
            values = new String[strList.size()];
            strList.toArray(values);
        }
        
        return arrayToString(values, split);
    }
    
    /**
     * 用于获取字符串中字符的个数<BR>
     * 一个汉字按两个字节的方法计算字数
     * 
     * @param content 文本内容
     * @return 返回字符的个数
     */
    public static int getStringLeng(String content) {
        return (int) Math.ceil(count2BytesChar(content));
    }
    
    /**
     * 按照一个汉字两个字节的方法计算字数
     * 
     * @param string String
     * @return 返回字符串's count
     */
    public static int count2BytesChar(String string) {
        int count = 0;
        if (string != null) {
            for (char c : string.toCharArray()) {
                count++;
                if (isChinese(c)) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 判断参数c是否为中文
     * 
     * @param c char
     * @return 是中文字符返回true，反之false
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;        
    }
    
    /**
     * 将字符串数组转化为字符串，默认以","分隔
     * 
     * @param values 字符串数组
     * @param split 分隔符，默认为","
     * @return 有字符串数组转化后的字符串
     */
    public static String arrayToString(String[] values, String split) {
        StringBuffer buffer = new StringBuffer();
        if (values != null) {
            if (split == null) {
                split = ",";
            }
            int len = values.length;
            for (int i = 0; i < len; i++) {
                buffer.append(values[i]);
                if (i != len - 1) {
                    buffer.append(split);
                }
            }
        }
        
        return buffer.toString();
    }
    
    /**
     * InputStream转换成String
     * @param is InputStream
     * @param enc 编码格式
     * @return 转换后的String内容
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null!=reader){
                    reader.close();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 将assets的InputStream转换成String
     * @param is InputStream
     * @param enc 编码格式
     * @return 转换后的String内容
     * @throws IOException 
     */
    public static String assetsStreamToString(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        byte[] barr = new byte[1024];

        try {
            while(true) {
                int r = is.read(barr);
                if (r <= 0) {
                    break;
                }
                sb.append(new String(barr, 0, r));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != is){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 获取系统的设备唯一标识号<BR>
     * 若IMIE号为空则取AndroidID值，若还为空随机取UUID值<BR>
     * 
     * @param context 上下文
     * @return 设备唯一标识号
     */
    public static String getDeviceId(Context context){
        String deviceId = ((TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE)).getDeviceId();
        if(isNullOrEmpty(deviceId) ){
            deviceId = Settings.System.getString(context.getContentResolver(), "android_id");
            if (isNullOrEmpty(deviceId)) {
                deviceId = UUID.randomUUID().toString();
            }
        }
        
        return deviceId;
    }
	/**
	 * 过滤HTML标签，取出文本内容
	 * 
	 * @param htmlText
	 * @return
	 */
	public static String filterHtmlTag(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{�?script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{�?style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符�?
	}
	
	/**
	 * 提取h5页面传递过来的url里的参数
	 * @param url
	 * @return
	 */
	public static Map<String,String> parserUrlToParams(String url){
		Map<String, String> params = new HashMap<String, String>();
		if (!isNullOrEmpty(url)) {
			url = URLDecoder.decode(url);
			if (url.contains("?")) {
				int index = url.indexOf("?")+1;
				String paramsStr = url.substring(index);
				if (paramsStr.contains("&")) {
					String [] arr1 = paramsStr.split("&");
					for (int i = 0; i < arr1.length; i++) {
						if (arr1[i].contains("=")) {
							String[] arr2 = arr1[i].split("=");
							if (arr2.length == 2) {
								params.put(arr2[0], arr2[1]);
							}
						}
					}
				} else {
					if (paramsStr.contains("=")) {
						int index2 = paramsStr.indexOf("=")+1;
						String paramsStr2 = paramsStr.substring(index2);
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(paramsStr2);
							if (null != jsonObject) {
								Iterator it = jsonObject.keys();
								if (null != it && it.hasNext()) {
									// 遍历jsonObject数据，添加到Map对象
									while (it.hasNext()) {
										String key = String.valueOf(it.next());
										Object obj = jsonObject.opt(key);
										if (null != obj) {
											params.put(key, jsonObject.opt(key).toString());
										}
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							return null;
						}
					}
				}
			}
		}
		return params;
	}
	
	public static Map<String,String> parserJsonUrlToParams(String url){
		Map<String,String> params=new HashMap<String, String>();
		if(!isNullOrEmpty(url)){
			url= URLDecoder.decode(url);
			if(url.contains("params=")){
				String param = url.split("params=")[1];
				try {
					JSONObject jsonObject = new JSONObject(param);
					if(null!=jsonObject){
						Iterator it = jsonObject.keys();
						if(null!=it&&it.hasNext()){
							// 遍历jsonObject数据，添加到Map对象
							while (it.hasNext())
							{
								String key = String.valueOf(it.next());
								Object obj=jsonObject.opt(key);
								if(null!=obj){
									params.put(key,jsonObject.opt(key).toString());
								}
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return params;
	}

	public static String parsJsonWithOutBracket(JSONObject json){
		StringBuffer sb=new StringBuffer();
		Iterator<String> it=json.keys();
		while (it.hasNext()){
			String key=it.next();
			if(sb.length()==0){
				sb.append("\""+key+"\":"+"\""+json.optString(key)+"\"");
			}else{
				sb.append(",\""+key+"\":"+"\""+json.optString(key)+"\"");
			}
		}
		return sb.toString();
	}

	/**
	 * 分割字符串成list
	 * @param str
	 * @param reg 分隔符
	 * @return
	 */
	public static List<String> parsStringToList(String str,String reg){
		List<String> list=new ArrayList<String>();
		if(TextUtils.isEmpty(str)){
			return null;
		}
		if(str.contains(reg)){
			String []arr=str.split(reg);
			for (String str1:arr){
				list.add(str1);
			}
			return list;
		}else{
			return null;
		}
	}
    
}
