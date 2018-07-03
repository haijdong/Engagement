package com.segi.view.tree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 节点名称注解类
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-12-14 16:09
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeNodeName {
}
