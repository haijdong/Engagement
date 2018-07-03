package com.segi.view.advert;

import java.io.Serializable;

/**
 * 广告实体
 * @author liangzx
 *
 */
public class AdvertInfo implements Serializable{
	/**
	 * 广告id
	 */
	public int id;
	/**
	 * 标题
	 */
	public String tittle;
	/**
	 * 图片地址
	 */
	public String imageUrl;
	/**
	 * 图片跳转的类型
	 */
	public int linktype;
	/**
	 * 
	 */
	public String link;
	/**
	 * 广告位置
	 */
	public int position;
	/**
	 * 是否已读
	 */
	public int isRead;
	/**
	 * 广告显示时间
	 */
	public int runingSecond;
	
}
