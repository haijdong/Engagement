package com.segi.view.calendar.bizs.languages;

/**
 * 中文的默认实现类
 * 如果你想实现更多的语言请参考Language{@link DPLManager}
 *
 * The implementation class of chinese.
 * You can refer to Language{@link DPLManager} if you want to define more language.
 *
 * @author AigeStudio 2015-03-28
 */
public class CN extends DPLManager {
    @Override
    public String[] titleMonth() {
        return new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
    }

    @Override
    public String titleEnsure() {
        return "确定";
    }

    @Override
    public String titleBC() {
        return "公元前";
    }

    @Override
    public String[] titleWeek() {
        return new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    }

	@Override
	public String titleToday() {
		return "今天";
	}

	@Override
	public String thisMonth() {
		// TODO Auto-generated method stub
		return "今月";
	}

	@Override
	public String getMonthName(int index) {
		switch (index) {
		case 0:
			return "1月";
		case 1:
			return "2月";
		case 2:
			return "3月";
		case 3:
			return "4月";
		case 4:
			return "5月";
		case 5:
			return "6月";
		case 6:
			return "7月";
		case 7:
			return "8月";
		case 8:
			return "9月";
		case 9:
			return "10月";
		case 10:
			return "11月";
		case 11:
			return "12月";
		default:
			return "";
		}
	}
}
