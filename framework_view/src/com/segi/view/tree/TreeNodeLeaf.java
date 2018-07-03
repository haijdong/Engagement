package com.segi.view.tree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否是子节点的注解类
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-12-14 17:05
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeNodeLeaf {
}
