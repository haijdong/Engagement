package com.segi.view.calendar.bizs.themes;

/**
 * 考勤时
 * @author liangzx
 *
 */
public class CheckWorkTheme extends DPTheme {

	 @Override
	    public int colorBG() {
	        return 0xFFFFFFFF;
	    }

	    @Override
	    public int colorBGCircle() {
	        return 0xD1FAA419;
	    }

	    @Override
	    public int colorTitleBG() {
	        return 0xFFFFFFFF;
	    }

	    @Override
	    public int colorTitle() {
	        return 0xFF333333;
	    }

	    @Override
	    public int colorToday() {
	        return 0xFF333333;
	    }

	    @Override
	    public int colorG() {
	        return 0xFF333333;
	    }

	    /**
	     * 不准时字体颜色
	     */
	    @Override
	    public int colorF() {
	        return 0xFFFF6867;
	    }

	    @Override
	    public int colorWeekend() {
	        return 0xFF333333;
	    }

	    /**
	     * 准时字体颜色
	     */
	    @Override
	    public int colorHoliday() {
	        return 0xFF56BE85;
	    }

}
