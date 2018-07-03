package com.segi.view.calendar.numberpicker;

/**
 * 日期格式
 * @author Administrator
 *
 */
public enum PickerEnum {

	/**
	 * 年月日时分
	 */
	YYYYMMDDHHMM(1, "年月日时分"),
	/**
	 * 年月日时
	 */
	YYYYMMDDHH(2, "年月日时"),
	/**
	 * 年月日
	 */
	YYYYMMDD(3, "年月日"),
	/**
	 * 年月
	 */
	YYYYMM(4, "年月"),
	/**
	 * 月日时分
	 */
	MMDDHHMM(5, "月日时分"),
	/**
	 * 时分
	 */
	HHMM(6, "时分"),
	/**
	 * 月日
	 */
	MMDD(7, "月日"),
	/**
	 * 月日时
	 */
	MMDDHH(8, "月日时"),
	/**
	 * 年
	 */
	YYYY(9, "年"),
	/**
	 * 月
	 */
	MM(10, "月"),
	/**
	 * 天
	 */
	DD(11, "日"),
	/**
	 * 天
	 */
	DDHH(12, "天时");


	final private int value;
	final private String tagName;

	private PickerEnum(final int value, final String tagName)
	{
		this.value = value;
		this.tagName = tagName;
	}

	public int value()
	{
		return value;
	}

	public String tagName()
	{
		return tagName;
	}

	/**
	 * 依据标签值获取枚举实例
	 * 
	 * @param tagValue 标签值
	 * @return 标签枚举对象
	 */
	public static PickerEnum toEnum(final int tagValue)
	{
		for (final PickerEnum tag : PickerEnum.values())
		{
			if (tag.value() == tagValue)
			{
				return tag;
			}
		}
		return null;
	}

	/**
	 * 依据标签值转换成标签名称
	 * 
	 * @param tagValue 标签名称
	 * @return 标签名称
	 */
	public static String toTagName(final int tagValue)
	{
		final PickerEnum tag = toEnum(tagValue);
		if (tag == null)
		{
			return "";
		}
		return tag.tagName();
	}
	
}
