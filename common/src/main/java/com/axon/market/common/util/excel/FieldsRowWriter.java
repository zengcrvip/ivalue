package com.axon.market.common.util.excel;

import com.axon.market.common.util.excel.ExcelTemplate;

import java.util.Map;

/**
 * 按字段顺序写入
 * Created by zengcr on 2016/12/5.
 */
public class FieldsRowWriter implements ExcelTemplate.RowWriter
{
    private String[] fields;

    private int startColumn;

    public FieldsRowWriter(String[] fields, int startColumn)
    {
        super();
        this.fields = fields;
        this.startColumn = startColumn;
    }

    @Override
    public void write(ExcelTemplate template, int rowIndex, Map<String, ?> data)
    {
        for (int j = 0; j < fields.length; j++)
        {
            template.writeCell(rowIndex, startColumn + j, data.get(fields[j]));
        }
    }
}
