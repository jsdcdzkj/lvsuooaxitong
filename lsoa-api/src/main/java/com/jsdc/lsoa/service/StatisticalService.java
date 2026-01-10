package com.jsdc.lsoa.service;

import cn.hutool.core.util.StrUtil;
import com.jsdc.lsoa.mapper.CaseInfoMapper;
import com.jsdc.lsoa.model.SysDict;
import com.jsdc.lsoa.model.SysUser;
import com.jsdc.lsoa.vo.StatisticalVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class StatisticalService {

    @Autowired
    private CaseInfoMapper caseInfoMapper;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;

    public List<StatisticalVo> CaseTypeRanking(StatisticalVo vo) {
        return caseInfoMapper.CaseTypeRanking(vo,"count");
    }
    public List<StatisticalVo> CaseTypeMoneyRanking(StatisticalVo vo) {
        return caseInfoMapper.CaseTypeRanking(vo,"money");
    }

    /**
     * 统一入口：根据类型统计案件数量并补零
     * @param flag 统计类型：day（时间段）、month（某个月）、year（某年）
     * @param startTime 时间段统计的开始时间（格式：yyyy-MM-dd）
     * @param endTime 时间段统计的结束时间（格式：yyyy-MM-dd）
     * @param time 月或年的时间 某个月（格式：yyyy-MM） 某年（格式：yyyy）
     */
    public Map<String, List<String>> getStatisticalVoWithZero(StatisticalVo vo) {
        List<StatisticalVo> result;
        switch (vo.getFlag()) {
            case "day":
                result = handleDayStat(vo,"count");
                break;
            case "month":
                result = handleDayOfMonthStat(vo,"count");
                break;
            case "year":
                result = handleMonthOfYearStat(vo,"count");
                break;
            default :
                result = Collections.emptyList();
                break;
        }
        return convert(result,true);
    }
    public Map<String, List<String>> getStatisticalVoWithZeroMoney(StatisticalVo vo) {
        List<StatisticalVo> result;
        switch (vo.getFlag()) {
            case "day":
                result = handleDayStat(vo,"money");
                break;
            case "month":
                result = handleDayOfMonthStat(vo,"money");
                break;
            case "year":
                result = handleMonthOfYearStat(vo,"money");
                break;
            default:
                result = Collections.emptyList();
                break;
        }
        return convert(result,false);
    }
    private Map<String, List<String>> convert(List<StatisticalVo> result,boolean isMoney) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> caseCount = new ArrayList<>();
        if (isMoney) {
            caseCount = result.stream().map(StatisticalVo::getCaseCount).collect(Collectors.toList());
        }else {
            caseCount = result.stream().map(StatisticalVo::getTotalMoney).collect(Collectors.toList());
        }
        List<String> time = result.stream().map(StatisticalVo::getTime).collect(Collectors.toList());
        map.put("caseCount", caseCount);
        map.put("time", time);
        return map;
    }

    // 按时间段统计（每天）
    private List<StatisticalVo> handleDayStat(StatisticalVo vo,String type) {
        // 1. 生成时间段内的所有日期
//        List<String> dateList = generateDateSeries(
//                vo.getStartTime().substring(0, 10),
//                vo.getEndTime().substring(0, 10)
//        );
        List<String> dateList = getMonthsBetween(vo.getStartTime(), vo.getEndTime());

        // 2. 查询数据库统计结果
        List<StatisticalVo> dbStats = caseInfoMapper.statByDay(vo,type);

        // 3. 合并结果并补零
        return mergeStats(dateList,dbStats,type);
    }

    // 按月统计（每天）
    private List<StatisticalVo> handleDayOfMonthStat(StatisticalVo vo,String type) {
        // 1. 生成某个月的所有日期
//        LocalDate startDate = LocalDate.parse(vo.getTime() + "-01");
//        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
//        List<String> dateList = generateDateSeries(
//                startDate.format(DateTimeFormatter.ISO_DATE),
//                endDate.format(DateTimeFormatter.ISO_DATE)
//        );
        List<String> dateList = new ArrayList<>();
        dateList.add(vo.getTime());
        // 2. 查询数据库统计结果
        List<StatisticalVo> dbStats = caseInfoMapper.statByDayOfMonth(vo,type);

        // 3. 合并结果并补零
        return mergeStats(dateList, dbStats,type);
    }

    // 按年统计（每月）
    private List<StatisticalVo> handleMonthOfYearStat(StatisticalVo vo,String type) {
        // 1. 生成某年的所有月份
        List<String> monthList = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> String.format("%s-%02d", vo.getTime(), month))
                .collect(Collectors.toList());

        // 2. 查询数据库统计结果
        List<StatisticalVo> dbStats = caseInfoMapper.statByMonthOfYear(vo,type);

        // 3. 合并结果并补零
        return mergeStats(monthList, dbStats,type);
    }

    /**
     * 生成日期序列（按天）
     * @param startDate 格式：yyyy-MM-dd
     * @param endDate 格式：yyyy-MM-dd
     */
    private List<String> generateDateSeries(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<String> dates = new ArrayList<>();
        while (!start.isAfter(end)) {
            dates.add(start.format(DateTimeFormatter.ISO_DATE));
            start = start.plusDays(1);
        }
        return dates;
    }

    /**
     * 生成月份序列（按月）
     * @param startTime 格式：yyyy-MM
     * @param endTime 格式：yyyy-MM
     */
    private List<String> getMonthsBetween(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth start = YearMonth.parse(startTime, formatter);
        YearMonth end = YearMonth.parse(endTime, formatter);

        List<String> months = new ArrayList<>();
        YearMonth current = start;
        while (!current.isAfter(end)) {
            months.add(current.format(formatter));
            current = current.plusMonths(1);
        }
        return months;
    }

    /**
     * 合并统计结果并补零
     * @param timeSeries 完整时间序列
     * @param dbStats 数据库查询结果
     * @param type 类型：count（数量）、money（金额）
     */
    private List<StatisticalVo> mergeStats(List<String> timeSeries, List<StatisticalVo> dbStats, String type) {
        Map<String, String> statMap = new LinkedHashMap<>();

        // 初始化时间序列，默认值为 0
        timeSeries.forEach(time -> statMap.put(time, "0"));

        if (type.equals("count")) {
            // 填充数据库查询结果
            dbStats.forEach(stat -> {
                statMap.put(stat.getTime(), String.valueOf(stat.getCaseCount()));
            });
            // 转换为有序列表
            return statMap.entrySet().stream()
                    .map(entry -> new StatisticalVo().builder()
                            .time(entry.getKey())
                            .caseCount(String.valueOf(entry.getValue()))
                            .build()
                    )
                    .collect(Collectors.toList());
        }else {
            // 填充数据库查询结果
            dbStats.forEach(stat -> {
                statMap.put(stat.getTime(), String.valueOf(stat.getTotalMoney()));
            });
            // 转换为有序列表
            return statMap.entrySet().stream()
                    .map(entry -> new StatisticalVo().builder()
                            .time(entry.getKey())
                            .totalMoney(String.valueOf(entry.getValue()))
                            .build()
                    )
                    .collect(Collectors.toList());
        }


    }
    public Map<String, Object> lawFirmCaseStatisticsByType(StatisticalVo vo){
        return getCaseStatistics(vo,"count");
    }
    public Map<String, Object> lawFirmCaseStatisticsByTypeMoney(StatisticalVo vo){
        return getCaseStatistics(vo,"money");
    }

    public Map<String, Object> getCaseStatistics(StatisticalVo vo,String type) {
        // 获取所有案件类型
        List<SysDict> caseTypes = sysDictService.getListByType("caseType");
//        if (StrUtil.isNotBlank(vo.getCaseType())) {
//            caseTypes = caseTypes.stream().filter(x->x.getDictValue().equals(vo.getCaseType())).collect(Collectors.toList());
//        }
        List<String> timeList = new ArrayList<>();
        if (Objects.equals(vo.getFlag(), "day")) {
            // 1. 生成时间段内的所有日期
//            timeList = generateDateSeries(
//                    vo.getStartTime().substring(0, 10),
//                    vo.getEndTime().substring(0, 10)
//            );
            timeList = getMonthsBetween(vo.getStartTime(), vo.getEndTime());
        }else if (Objects.equals(vo.getFlag(), "month")) {
            // 1. 生成某个月的所有日期
//            LocalDate startDate = LocalDate.parse(vo.getTime() + "-01");
//            LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
//            timeList = generateDateSeries(
//                    startDate.format(DateTimeFormatter.ISO_DATE),
//                    endDate.format(DateTimeFormatter.ISO_DATE)
//            );
            timeList.add(vo.getTime());
        }else if (Objects.equals(vo.getFlag(), "year")) {
            // 1. 生成某年的所有月份
            timeList = IntStream.rangeClosed(1, 12)
                    .mapToObj(month -> String.format("%s-%02d", vo.getTime(), month))
                    .collect(Collectors.toList());
        }
        List<Map<String, String>> caseStatistics = caseInfoMapper.getCaseStatistics(vo, caseTypes,type);
        int total = 0 ;
        BigDecimal totalMoney = new BigDecimal(0); // 初始化 BigDecimal 为 0
        for (Map<String, String> map : caseStatistics) {
            String totalStr = "";
            try {
                if (Objects.equals(type, "count")) {
                    totalStr = String.valueOf(map.get("totalCases"));
                    total += Integer.parseInt(totalStr);
                }else if (Objects.equals(type, "money")) {
                    totalStr = String.valueOf(map.get("totalMoney"));
                    if (totalStr != null && !totalStr.isEmpty()) {
                        // 将字符串转换为 BigDecimal，并累加
                        BigDecimal money = new BigDecimal(totalStr);
                        totalMoney = totalMoney.add(money);  // 累加
                    }
                }
            } catch (NumberFormatException e) {
                // 如果totalCases不是有效的数字，可以选择忽略或处理
                System.out.println("Invalid number format: " + totalStr);
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (Objects.equals(type, "count")) {
            map.put("total",total);
        }else if (Objects.equals(type, "money")) {
            map.put("total",totalMoney.setScale(2, RoundingMode.HALF_UP).toString());
        }
        map.put("cases",nimbleMergeStats(timeList, caseStatistics, caseTypes,type));

        return map;
    }

    private List<Map<String, String>> nimbleMergeStats(List<String> timeSeries, List<Map<String, String>> dbStats,List<SysDict> caseTypes,String type) {
        Map<String, Map<String, String>> statMap = new LinkedHashMap<>();
        String total;
        if (Objects.equals(type, "count")) {
            total = "totalCases";
        }else {
            total = "totalMoney";
        }
        // 初始化时间序列，默认值为 0
        String finalTotal = total;
        timeSeries.forEach(time -> {
            Map<String, String> m = new HashMap<>();
            m.put("time", time);
            m.put(finalTotal,"0");
            for (SysDict caseType : caseTypes) {
                m.put(caseType.getDictValue(), "0");
            }
            statMap.put(time, m);
        });

        // 填充数据库查询结果
        dbStats.forEach(stat -> {
            statMap.put(stat.get("time"), stat);
        });

        // 转换为有序列表
        return statMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public void export(StatisticalVo vo,String type, HttpServletResponse response)throws IOException {

        Map<String, Object> count1 = getCaseStatistics(vo, type);
        List<Map<String, Object>> data = (List<Map<String, Object>>)count1.get("cases");
        // 1. 获取案件类型字典
        List<SysDict> caseTypeDict = sysDictService.getListByType("caseType");

        // 2. 创建 Excel 工作簿
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("案件统计");

        // 3. 创建表头
        Row headerRow = sheet.createRow(0);
        if (StrUtil.isNotBlank(vo.getUserId())){
            headerRow.createCell(0).setCellValue("姓名");
            headerRow.createCell(1).setCellValue("时间");
            headerRow.createCell(2).setCellValue("小计");
            // 动态添加案件类型表头
            int colIndex = 3;
            for (SysDict entry : caseTypeDict) {
                headerRow.createCell(colIndex).setCellValue(entry.getDictLabel());
                colIndex++;
            }
            String name = sysUserService.getById(Integer.parseInt(vo.getUserId())).getRealName();
            // 4. 填充数据
            int rowIndex = 1;
            String total;
            if (Objects.equals(type, "count")) {
                total = "totalCases";
            }else {
                total = "totalMoney";
            }
            for (Map<String, Object> rowData : data) {
                Row row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(name); // 姓名
                row.createCell(1).setCellValue(rowData.get("time").toString()); // 时间
                row.createCell(2).setCellValue(String.valueOf(rowData.get(total).toString())); // 小计

                // 动态填充案件类型数量
                colIndex = 3;
                for (SysDict entry : caseTypeDict) {
//                String count;
//                if (rowData.getOrDefault(entry.getDictValue(), 0) instanceof BigDecimal) {
                    String count = String.valueOf(rowData.getOrDefault(entry.getDictValue(), 0).toString());
//                }else {
//                    count = String.valueOf(rowData.getOrDefault(entry.getDictValue(), 0));
//                }
                    row.createCell(colIndex).setCellValue(count);
                    colIndex++;
                }
                rowIndex++;
            }
        }else {
            headerRow.createCell(0).setCellValue("时间");
            headerRow.createCell(1).setCellValue("小计");

            // 动态添加案件类型表头
            int colIndex = 2;
            for (SysDict entry : caseTypeDict) {
                headerRow.createCell(colIndex).setCellValue(entry.getDictLabel());
                colIndex++;
            }

            // 4. 填充数据
            int rowIndex = 1;
            String total;
            if (Objects.equals(type, "count")) {
                total = "totalCases";
            }else {
                total = "totalMoney";
            }
            for (Map<String, Object> rowData : data) {
                Row row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(rowData.get("time").toString()); // 时间
                row.createCell(1).setCellValue(String.valueOf(rowData.get(total).toString())); // 小计

                // 动态填充案件类型数量
                colIndex = 2;
                for (SysDict entry : caseTypeDict) {
//                String count;
//                if (rowData.getOrDefault(entry.getDictValue(), 0) instanceof BigDecimal) {
                    String count = String.valueOf(rowData.getOrDefault(entry.getDictValue(), 0).toString());
//                }else {
//                    count = String.valueOf(rowData.getOrDefault(entry.getDictValue(), 0));
//                }
                    row.createCell(colIndex).setCellValue(count);
                    colIndex++;
                }
                rowIndex++;
            }
        }




        // 5. 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=case_statistics.xlsx");

        // 6. 写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }


    public List<SysUser> getNormalUserList(){
        return sysRoleService.getNormalUserList();
    }
}
