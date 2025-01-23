package com.chen.bitten.common.utils;

import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRowHandler;
import cn.hutool.core.text.csv.CsvUtil;
import com.chen.bitten.common.csv.CountCsvRowHandler;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CsvUtils {

    private static final String LOG_PREFIX = "[CsvUtils]";

    public static final String RECEIVER_HEADER = "userId";

    private CsvUtils() {

    }

    /**
     * 读取一行数据
     */
    public static void getCsvRow(String path, CsvRowHandler csvRowHandler) {
        try (CsvReader csvReader = CsvUtil.getReader(
                new InputStreamReader(Files.newInputStream(Paths.get(path)), StandardCharsets.UTF_8),
                new CsvReadConfig().setContainsHeader(true)
        )) {
            csvReader.read(csvRowHandler);
        } catch (IOException e) {
            log.error("{}getCsvRow fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
    }

    public static Long countCsvRow(String path, CountCsvRowHandler csvRowHandler) {
        try (CsvReader csvReader = CsvUtil.getReader(
                new InputStreamReader(Files.newInputStream(Paths.get(path)), StandardCharsets.UTF_8),
                new CsvReadConfig().setContainsHeader(true)
        )) {
            csvReader.read(csvRowHandler);
        } catch (IOException e) {
            log.error("{}countCsvRow fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
        return csvRowHandler.getRowSize();
    }

    public static Map<String, String> getParamFromLine(Map<String, String> fieldMap) {
        HashMap<String, String> params = new HashMap<>();
        for (Map.Entry<String, String> entry: fieldMap.entrySet()) {
            if (!RECEIVER_HEADER.equals(entry.getKey())) {
                params.put(entry.getKey(), entry.getValue());
            }
        }
        return params;
    }
}
