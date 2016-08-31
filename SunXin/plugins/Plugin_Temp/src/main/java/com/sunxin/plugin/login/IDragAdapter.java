/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.sunxin.plugin.login;

/**
 * Created by 钟光燕 on 2016/3/23.
 * ===================================================
 * <p>
 * code is m h l
 * <p>
 * ===================================================
 */
public interface IDragAdapter {

    /**
     * 交换两个数据源
     * @param oldPosition
     * @param newPosition
     */
    void swapItem(int oldPosition, int newPosition) ;

    /**
     * 移除这个item,从数据源中
     * @param position
     */
    void removeItem(int position) ;

    /**
     * 隐藏item，不移除
     * @param position
     */
    void hideItem(int position) ;

    /**
     * 不可移动的item
     * @return position 大于这个返回的值标识不可移动
     *
     * 默认返回getCount()，表示所有都可以移动，
     *
     * 产品有个变态需求，要求补全不够的item，所以
     * 没办法咯
     */
    int unableMove() ;

}
