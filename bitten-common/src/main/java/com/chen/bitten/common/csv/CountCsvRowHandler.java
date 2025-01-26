package com.chen.bitten.common.csv;

import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvRowHandler;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import lombok.Getter;

@Getter
public class CountCsvRowHandler implements CsvRowHandler {

    private long rowSize;

    @Override
    public void handle(CsvRow csvRow) {
        rowSize++;
    }
}
