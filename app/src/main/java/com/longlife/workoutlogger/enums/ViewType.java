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
