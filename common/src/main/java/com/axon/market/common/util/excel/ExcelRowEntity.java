package com.axon.market.common.util.excel;

import java.util.List;

/**
 * Created by zengcr on 2016/12/5.
 */
public class ExcelRowEntity
{
    /**
     * 行类型是大标题行
     */
    public static final int TITLE_ROW = 0;

    /**
     * 行类型是小标题行
     */
    public static final int SMALL_TITLE_ROW = 1;

    /**
     * 行类型是报表头行
     */
    public static final int HEAD_ROW = 2;

    /**
     * 行类型是分割行
     */
    public static final int SPLIT_ROW = 3;

    private short rowHeight = 16;

    /**
     * 判断是否是标题行
     */
    private int rowType;

    /**
     * 此行中包含的所单元格列表
     */
    private List<ExcelCellEntity> cellEntityList;

    public int getRowType()
    {
        return rowType;
    }

    public void setRowType(int rowType)
    {
        this.rowType = rowType;
    }

    public List<ExcelCellEntity> getCellEntityList()
    {
        return cellEntityList;
    }

    public void setCellEntityList(List<ExcelCellEntity> cellEntityList)
    {
        this.cellEntityList = cellEntityList;
    }

    public short getRowHeight()
    {
        return rowHeight;
    }

    public void setRowHeight(short rowHeight)
    {
        this.rowHeight = rowHeight;
    }
}
