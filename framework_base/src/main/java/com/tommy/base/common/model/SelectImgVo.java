package com.tommy.base.common.model;

import java.io.Serializable;

/**
 * 图片实体
 */
public class SelectImgVo implements Serializable {
	/**
	 * 图片路径
	 */
	public String path;
	/**
	 * 缩略图路径
	 */
	public String thumbPath;
	/**
	 * 是否已选择
	 */
	public boolean isSelect;
}
