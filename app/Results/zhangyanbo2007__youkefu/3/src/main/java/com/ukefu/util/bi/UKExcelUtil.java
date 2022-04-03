package com.ukefu.util.bi;
 import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import com.ukefu.util.bi.model.FirstTitle;
import com.ukefu.util.bi.model.Level;
import com.ukefu.util.bi.model.ValueData;
public class UKExcelUtil {

 private  ReportData reportData;

 private  SXSSFWorkbook wb;

 private  Sheet sheet;

 private  int ROW_LIMIT;

 private  int rowNum;

 private  int rowTitleNum;

 private  int cellNumber;

 private  String headTitle;

 private  String startTime;

 private  String endTime;

 private  CellStyle firstStyle;

 private  CellStyle style;

 private  CellStyle datastyle;

 private int page;

 private  OutputStream out;

public UKExcelUtil() {
    super();
    wb = new SXSSFWorkbook(ROW_LIMIT);
    firstStyle = createFirstCellStyle();
    style = createContentCellStyle();
    datastyle = createContentCellStyle();
}public UKExcelUtil(ReportData reportData, OutputStream out, String title) {
    this.reportData = reportData;
    this.out = out;
    this.headTitle = title;
    init();
}
public void setHeadTitle(String title){
    this.headTitle = title;
}


public void createContent(){
    StringBuffer sbRol = new StringBuffer();
    StringBuffer sbCol = new StringBuffer();
    StringBuffer sbDataCol = new StringBuffer();
    StringBuffer sbDataRol = new StringBuffer();
    if (reportData.getRow() != null) {
        List<List<Level>> rowlst = reportData.getRow().getTitle();
        List<List<ValueData>> dataList = reportData.getData();
        for (int r = 0; dataList != null && r < dataList.size(); r++) {
            if (rowNum > 1048575) {
                // 超过excel上限新建sheet
                sheet = wb.createSheet();
                rowNum = 0;
                this.createCellNumber();
                this.createHead();
                this.createSubHead();
                this.createTitle();
            }
            int cellNum = 0;
            Row row2 = sheet.createRow(rowNum);
            row2.setHeight((short) 420);
            Cell cell2 = row2.createCell(0);
            cell2.setCellStyle(firstStyle);
            // weidu
            for (int c = 0; c < rowlst.size(); c++) {
                int rowspan = 0;
                Cell cell3 = row2.createCell(cellNum);
                List<Level> leveList = rowlst.get(c);
                Level currentLevel = null;
                for (Level level : leveList) {
                    if (rowspan == r) {
                        currentLevel = level;
                        break;
                    }
                    rowspan = rowspan + (level.getRowspan() > 0 ? level.getRowspan() : 1);
                }
                if (currentLevel != null) {
                    String value = String.valueOf(currentLevel.getName());
                    if (!"".equals(value)) {
                        if (value.indexOf("%") > -1) {
                            value = value.replace("%", "");
                            if (checkIsNumber(String.valueOf(value))) {
                                cell3.setCellValue(Double.parseDouble(value) / 100);
                                datastyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
                                cell3.setCellStyle(datastyle);
                            } else {
                                // style.setDataFormat(wb.createDataFormat().getFormat(""));
                                cell3.setCellValue("");
                                cell3.setCellStyle(style);
                            }
                        } else {
                            if (checkIsNumber(String.valueOf(value))) {
                                cell3.setCellValue(Double.parseDouble(String.valueOf(value)));
                                cell3.setCellStyle(style);
                            } else {
                                cell3.setCellValue(String.valueOf(value));
                                cell3.setCellStyle(style);
                            }
                        }
                    } else {
                        cell3.setCellValue("");
                        cell3.setCellStyle(style);
                    }
                    if (currentLevel != null && currentLevel.getRowspan() > 1) {
                        // 开始行；开始列；合并行数
                        sbRol.append(rowNum).append(",").append(cellNum).append(",").append(currentLevel.getRowspan()).append(";");
                    } else if (currentLevel != null && currentLevel.getColspan() > 1) {
                        // 开始行；开始列；合并列数
                        sbCol.append(rowNum).append(",").append(cellNum).append(",").append(currentLevel.getColspan()).append(";");
                    }
                } else {
                    cell3.setCellValue("");
                    cell3.setCellStyle(style);
                }
                cellNum++;
            }
            // zhibiao
            for (int j = 0; j < dataList.get(r).size(); j++) {
                Cell cell3 = row2.createCell(cellNum);
                String value = String.valueOf(dataList.get(r).get(j));
                if (dataList.get(r).get(j) != null && dataList.get(r).get(j).getRowspan() > 1) {
                    // 开始行；开始列；合并行数
                    sbDataRol.append(rowNum).append(",").append(cellNum).append(",").append(dataList.get(r).get(j).getRowspan()).append(";");
                } else if (dataList.get(r).get(j) != null && dataList.get(r).get(j).getColspan() > 1) {
                    // 开始行；开始列；合并列数
                    sbDataCol.append(rowNum).append(",").append(cellNum).append(",").append(dataList.get(r).get(j).getColspan()).append(";");
                }
                if (!"".equals(value)) {
                    if (value.indexOf("%") > -1) {
                        value = value.replace("%", "");
                        if (checkIsNumber(String.valueOf(value))) {
                            cell3.setCellValue(Double.parseDouble(value) / 100);
                            datastyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
                            cell3.setCellStyle(datastyle);
                        } else {
                            cell3.setCellValue("0.00%");
                            cell3.setCellStyle(style);
                        }
                    } else {
                        // if(checkIsNumber(String.valueOf(value)) ){
                        // cell3.setCellValue(Double.parseDouble(String.valueOf(value)));
                        // cell3.setCellStyle(style);
                        // }else{
                        cell3.setCellValue(String.valueOf(value));
                        cell3.setCellStyle(style);
                    // }
                    }
                } else {
                    cell3.setCellValue("0");
                    cell3.setCellStyle(style);
                }
                cellNum++;
            }
            rowNum++;
        }
        // 单元格合并
        // 四个参数分别是：起始行，结束行，起始列，结束列
        for (String clospan : sbRol.toString().split(";")) {
            if (clospan.length() > 0) {
                String[] valueStr = clospan.split(",");
                sheet.addMergedRegion(new CellRangeAddress(Integer.parseInt(valueStr[0]), Integer.parseInt(valueStr[0]) + Integer.parseInt(valueStr[2]) - 1, (short) Integer.parseInt(valueStr[1]), (short) Integer.parseInt(valueStr[1])));
            }
        }
        for (String clospan : sbCol.toString().split(";")) {
            if (clospan.length() > 0) {
                String[] valueStr = clospan.split(",");
                sheet.addMergedRegion(new CellRangeAddress(Integer.parseInt(valueStr[0]), Integer.parseInt(valueStr[0]), (short) Integer.parseInt(valueStr[1]), (short) (Integer.parseInt(valueStr[1]) + Integer.parseInt(valueStr[2]) - 1)));
            }
        }
        for (String clospan : sbDataRol.toString().split(";")) {
            if (clospan.length() > 0) {
                String[] valueStr = clospan.split(",");
                sheet.addMergedRegion(new CellRangeAddress(Integer.parseInt(valueStr[0]), Integer.parseInt(valueStr[0]) + Integer.parseInt(valueStr[2]) - 1, (short) Integer.parseInt(valueStr[1]), (short) Integer.parseInt(valueStr[1])));
            }
        }
        for (String clospan : sbDataCol.toString().split(";")) {
            if (clospan.length() > 0) {
                String[] valueStr = clospan.split(",");
                sheet.addMergedRegion(new CellRangeAddress(Integer.parseInt(valueStr[0]), Integer.parseInt(valueStr[0]), (short) Integer.parseInt(valueStr[1]), (short) (Integer.parseInt(valueStr[1]) + Integer.parseInt(valueStr[2]) - 1)));
            }
        }
    }
}


public int getPage(){
    // TODO Auto-generated method stub
    return this.page;
}


public void createTitle(){
    List<List<Level>> title = reportData.getCol().getTitle();
    if (title != null) {
        // HSSFRow row = sheet.createRow(rowNum);
        // row.setHeight((short)480);
        // 
        // rowNum ++;
        CellStyle titleStyle = createTitleCellStyle();
        {
            int rowinx = 0;
            int rowTitleSize = reportData.getRow() != null ? reportData.getRow().getTitle().size() : 0;
            for (List<Level> levelList : title) {
                Row titleRow = sheet.createRow(rowNum);
                // 第一个空表
                int i = 0;
                if (title.size() > 1 && rowTitleSize > 0 && rowinx == 0) {
                    Cell cell = titleRow.createCell(0);
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + reportData.getCol().getTitle().size() - 2, 0, (rowTitleSize - 1)));
                    cell.setCellValue(new HSSFRichTextString(""));
                    cell.setCellStyle(titleStyle);
                }
                if (rowinx + 1 == reportData.getCol().getTitle().size()) {
                    int firstTitleNo = 0;
                    if (reportData.getRow() != null && reportData.getRow().getFirstTitle() != null) {
                        for (FirstTitle firstTitle : reportData.getRow().getFirstTitle()) {
                            Cell blankcell = titleRow.createCell(firstTitleNo++);
                            blankcell.setCellStyle(titleStyle);
                            blankcell.setCellValue(new XSSFRichTextString(firstTitle.getName()));
                        }
                    }
                }
                for (Level level : levelList) {
                    Cell cell = titleRow.createCell(rowTitleSize + i);
                    if ("newcol".equals(level.getLeveltype())) {
                        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + reportData.getCol().getTitle().size() - 1, (rowTitleSize + i), (rowTitleSize + i)));
                    }
                    if (level.getColspan() > 1) {
                        for (int rowinxno = 0; rowinxno < level.getColspan(); rowinxno++) {
                            Cell blankcell = titleRow.createCell(rowTitleSize + i + rowinxno);
                            blankcell.setCellStyle(titleStyle);
                            blankcell.setCellValue(new HSSFRichTextString(level.getName()));
                        }
                        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, (rowTitleSize + i), (rowTitleSize + i + level.getColspan() - 1)));
                    }
                    cell.setCellValue(new XSSFRichTextString(level.getName()));
                    cell.setCellStyle(titleStyle);
                    i = i + level.getColspan();
                }
                rowinx++;
                rowNum++;
            }
        }
    }
}


public boolean checkIsNumber(String str){
    Pattern p = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
    Matcher m = p.matcher(str);
    return m.matches();
}


public void writeRow(ReportData reportData){
    if (reportData != null) {
        this.reportData = reportData;
        this.createContent();
    }
}


public void createFile(){
    this.createCellNumber();
    this.createHead();
    this.createSubHead();
    this.createTitle();
    this.createContent();
    this.outputExcel();
}


public void setOut(OutputStream out){
    // TODO Auto-generated method stub
    this.out = out;
}


public String getEndTime(){
    // TODO Auto-generated method stub
    return this.endTime;
}


public void outputExcel(){
    if (this.out != null) {
        wb.write(this.out);
    }
}


public void setStartTime(String startTime){
    this.startTime = startTime;
}


public String getStartTime(){
    // TODO Auto-generated method stub
    return this.startTime;
}


public void close(){
    try {
        wb.write(out);
        wb.dispose();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


public void setPage(int page){
    // TODO Auto-generated method stub
    this.page = page;
}


public void init(){
    wb = new SXSSFWorkbook(ROW_LIMIT);
    sheet = wb.createSheet();
    firstStyle = createFirstCellStyle();
    style = createContentCellStyle();
    datastyle = createContentCellStyle();
}


public String getHeadTitle(){
    // TODO Auto-generated method stub
    return this.headTitle;
}


public ReportData getReportData(){
    // TODO Auto-generated method stub
    return this.reportData;
}


@SuppressWarnings("deprecation")
public CellStyle createTitleCellStyle(){
    CellStyle cellStyle = baseCellStyle();
    Font font = wb.createFont();
    font.setFontHeight((short) 180);
    cellStyle.setFont(font);
    cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
    cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    return cellStyle;
}


public void createSheet(String sheetName){
    // TODO Auto-generated method stub
    this.sheet = wb.createSheet(sheetName);
}


@SuppressWarnings("deprecation")
public void createSubHead(){
    CellStyle cellStyle = wb.createCellStyle();
    // 指定单元格居中对齐
    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    // 指定单元格垂直居中对齐
    cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    // 指定单元格自动换行
    cellStyle.setWrapText(true);
    // 设置单元格字体
    Font font = wb.createFont();
    // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setFontName("宋体");
    font.setFontHeight((short) 180);
    cellStyle.setFont(font);
    cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    CellStyle leftStyle = wb.createCellStyle();
    // 指定单元格居中对齐
    leftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    // 指定单元格垂直居中对齐
    leftStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    // 指定单元格自动换行
    leftStyle.setWrapText(true);
    leftStyle.setFont(font);
    leftStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    leftStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    leftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    leftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    Row row1 = sheet.createRow(rowNum);
    row1.setHeight((short) 440);
    StringBuffer strb = new StringBuffer();
    // if(false){	//增加 过滤器
    // 
    // }else{
    // 
    // }
    strb.append("报表生成日期:").append(getNowDate());
    Cell cell2 = row1.createCell(0);
    cell2.setCellType(HSSFCell.ENCODING_UTF_16);
    cell2.setCellValue(new XSSFRichTextString(strb.toString()));
    cell2.setCellStyle(leftStyle);
    // 指定合并区域
    if (rowNum > 1) {
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, (cellNumber - 1)));
    }
    for (int i = 1; i < this.cellNumber; i++) {
        Cell cell3 = row1.createCell(i);
        cell3.setCellStyle(cellStyle);
    }
    rowNum++;
}


public void setRowNum(int rowNum){
    this.rowNum = rowNum;
}


@SuppressWarnings("deprecation")
public void createHead(){
    Row row = sheet.createRow(rowNum);
    // 设置第一行
    Cell cell = row.createCell(0);
    row.setHeight((short) 1100);
    // 定义单元格为字符串类型
    cell.setCellType(HSSFCell.ENCODING_UTF_16);
    cell.setCellValue(new XSSFRichTextString(this.headTitle));
    // 指定合并区域
    if (rowNum > 0) {
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, (cellNumber - 1)));
    }
    CellStyle cellStyle = wb.createCellStyle();
    // 指定单元格居中对齐
    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    // 指定单元格垂直居中对齐
    cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    // 指定单元格自动换行
    cellStyle.setWrapText(true);
    // 设置单元格字体
    Font font = wb.createFont();
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setFontName("宋体");
    font.setFontHeight((short) 400);
    cellStyle.setFont(font);
    cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    cell.setCellStyle(cellStyle);
    for (int i = 1; i < this.cellNumber; i++) {
        Cell cell3 = row.createCell(i);
        cell3.setCellStyle(cellStyle);
    }
    rowNum++;
}


public int getRowNum(){
    // TODO Auto-generated method stub
    return rowNum;
}


public void createCellNumber(){
    if (reportData.getRow() != null) {
        if (reportData.getRow().getFirstTitle() != null) {
            rowTitleNum = reportData.getRow().getTitle().size();
        }
    }
    // 总列数
    cellNumber = reportData.getCol().getColspan() + rowTitleNum;
    getnewCol();
    sheet.setColumnWidth(0, 8000);
    for (int i = 1; i < cellNumber; i++) {
        sheet.setColumnWidth(i, 5000);
    }
}


public void setEndTime(String endTime){
    this.endTime = endTime;
}


public void writeHead(ReportData reportData){
    if (this.reportData == null) {
        this.reportData = reportData;
    }
    this.createCellNumber();
    this.createHead();
    this.createSubHead();
    this.createTitle();
}


public void getnewCol(){
    List<List<Level>> title = reportData.getCol().getTitle();
    if (title != null) {
        for (List<Level> levelList : title) {
            for (Level level : levelList) {
                if ("newcol".equals(level.getLeveltype())) {
                    cellNumber++;
                }
            }
        }
    }
}


@SuppressWarnings("deprecation")
public CellStyle createFirstCellStyle(){
    CellStyle cellStyle = baseCellStyle();
    Font font = wb.createFont();
    font.setFontHeight((short) 180);
    cellStyle.setFont(font);
    cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
    cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    return cellStyle;
}


@SuppressWarnings("deprecation")
public CellStyle createContentCellStyle(){
    CellStyle cellStyle = baseCellStyle();
    Font font = wb.createFont();
    font.setFontHeight((short) 200);
    font.setBoldweight((short) 0);
    cellStyle.setFont(font);
    return cellStyle;
}


@SuppressWarnings("deprecation")
public CellStyle baseCellStyle(){
    CellStyle cellStyle = wb.createCellStyle();
    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    cellStyle.setWrapText(true);
    cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    Font font = wb.createFont();
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setFontName("宋体");
    font.setFontHeight((short) 200);
    cellStyle.setFont(font);
    return cellStyle;
}


public void setReportData(ReportData reportData){
    // TODO Auto-generated method stub
    this.reportData = reportData;
}


public String getNowDate(){
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    return sf.format(new Date());
}


}