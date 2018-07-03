package com.segi.view.calendar.bizs.languages;

/**
 * 英文的默认实现类
 * 如果你想实现更多的语言请参考Language{@link DPLManager}
 *
 * The implementation class of english.
 * You can refer to Language{@link DPLManager} if you want to define more language.
 *
 * @author AigeStudio 2015-03-28
 */
public class EN extends DPLManager {
    @Override
    public String[] titleMonth() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    @Override
    public String titleEnsure() {
        return "Ok";
    }

    @Override
    public String titleBC() {
        return "B.C.";
    }

    @Override
    public String[] titleWeek() {
        return new String[]{"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    }

	@Override
	public String titleToday() {
		return "Today";
	}

	@Override
	public String thisMonth() {
		// TODO Auto-generated method stub
		return "Today";
	}

	@Override
	public String getMonthName(int index) {
		switch (index) {
		case 0:
			return "Jan";
		case 1:
			return "Feb";
		case 2:
			return "Mar";
		case 3:
			return "Apr";
		case 4:
			return "May";
		case 5:
			return "Jun";
		case 6:
			return "Jul";
		case 7:
			return "Aug";
		case 8:
			return "Sep";
		case 9:
			return "Oct";
		case 10:
			return "Nov";
		case 11:
			return "Dec";
		default:
			return "";
		}
	}
}
