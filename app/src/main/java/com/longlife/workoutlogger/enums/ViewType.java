/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.enums;

public class ViewType {
    private int dataIndex;
    private int type;
    private int headerIndex;

    public ViewType(int dataIndex, int type, int headerIndex) {
        this.dataIndex = dataIndex;
        this.type = type;
        this.headerIndex = headerIndex;
    }


    public int getDataIndex() {
        return dataIndex;
    }

    public int getHeaderIndex() {
        return headerIndex;
    }

    public int getType() {
        return type;
    }
}
