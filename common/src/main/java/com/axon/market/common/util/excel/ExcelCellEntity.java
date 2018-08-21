package com.axon.market.common.util.excel;

/**
 * Created by zengcr on 2016/12/5.
 */
public class ExcelCellEntity
{
    private int colSpan;

    private int rowSpan;

    private String text;

    public ExcelCellEntity()
    {
    }

    public ExcelCellEntity(int rowSpan, int colSpan, String text)
    {
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.text = text;
    }

    public int getColSpan()
    {
        return colSpan;
    }

    public void setColSpan(int colSpan)
    {
        this.colSpan = colSpan;
    }

    public int getRowSpan()
    {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan)
    {
        this.rowSpan = rowSpan;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
