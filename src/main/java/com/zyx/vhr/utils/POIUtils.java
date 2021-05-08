package com.zyx.vhr.utils;

import com.zyx.vhr.model.*;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2021/5/8 16:08
 */
public class POIUtils {
    public static ResponseEntity<byte[]> employee2Excel(List<Employee> list) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();

        workbook.createInformationProperties();
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        docInfo.setCategory("员工信息");
        docInfo.setManager("octopus");
        SummaryInformation summaryInfo = workbook.getSummaryInformation();
        summaryInfo.setTitle("员工信息表");
        summaryInfo.setAuthor("octopus");
        summaryInfo.setComments("本文档由程序自动生成😀");
        // 创建标题行样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

        HSSFSheet sheet = workbook.createSheet("员工信息表");
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell[] cols = new HSSFCell[26];
        final String[] colNames = {"编号", "姓名", "工号", "性别", "出生日期", "身份证号码", "婚姻状况", "民族", "籍贯", "政治面貌", "电话号码", "联系地址", "所属部门", "职称", "职位", "聘用形式", "最高学历", "专业", "毕业院校", "入职日期", "在职状态", "邮箱", "合同期限(年)", "合同起始日期", "合同终止日期", "转正日期"};
        final int[] colWidths = {5, 12, 10, 5, 12, 20, 10, 10, 16, 12, 15, 20, 16, 14, 14, 12, 8, 20, 20, 15, 8, 25, 14, 15, 15, 15};
        for (int i = 0; i < cols.length; i++) {
            cols[i] = r0.createCell(i);
            cols[i].setCellStyle(headerStyle);
            cols[i].setCellValue(colNames[i]);
            sheet.setColumnWidth(i, colWidths[i] * 256);
        }
        for (int i = 0; i < list.size(); i++) {
            Employee emp = list.get(i);
            HSSFRow row = sheet.createRow(1 + i);
            row.createCell(0).setCellValue(emp.getId());
            row.createCell(1).setCellValue(emp.getName());
            row.createCell(2).setCellValue(emp.getWorkId());
            row.createCell(3).setCellValue(emp.getGender());
            HSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(dateCellStyle);
            cell4.setCellValue(emp.getBirthday());
            row.createCell(5).setCellValue(emp.getIdCard());
            row.createCell(6).setCellValue(emp.getWedlock());
            row.createCell(7).setCellValue(emp.getNation().getName());
            row.createCell(8).setCellValue(emp.getNativePlace());
            row.createCell(9).setCellValue(emp.getPoliticsstatus().getName());
            row.createCell(10).setCellValue(emp.getPhone());
            row.createCell(11).setCellValue(emp.getAddress());
            row.createCell(12).setCellValue(emp.getDepartment().getName());
            row.createCell(13).setCellValue(emp.getJobLevel().getName());
            row.createCell(14).setCellValue(emp.getPosition().getName());
            row.createCell(15).setCellValue(emp.getEngageForm());
            row.createCell(16).setCellValue(emp.getTiptopDegree());
            row.createCell(17).setCellValue(emp.getSpecialty());
            row.createCell(18).setCellValue(emp.getSchool());
            HSSFCell cell19 = row.createCell(19);
            cell19.setCellStyle(dateCellStyle);
            cell19.setCellValue(emp.getBeginDate());
            row.createCell(20).setCellValue(emp.getWorkState());
            row.createCell(21).setCellValue(emp.getEmail());
            row.createCell(22).setCellValue(emp.getContractTerm());
            HSSFCell cell23 = row.createCell(23);
            cell23.setCellStyle(dateCellStyle);
            cell23.setCellValue(emp.getBeginContract());
            HSSFCell cell24 = row.createCell(24);
            cell24.setCellStyle(dateCellStyle);
            cell24.setCellValue(emp.getEndContract());
            HSSFCell cell25 = row.createCell(25);
            cell25.setCellStyle(dateCellStyle);
            cell25.setCellValue(emp.getConversionTime());
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDispositionFormData("attachment", new String("员工表.xls".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        workbook.write(stream);
        return new ResponseEntity<>(stream.toByteArray(), httpHeaders, HttpStatus.CREATED);
    }

    /**
     * transform Excel to list of Employee
     *
     * @param file
     * @param allNations
     * @param allPoliticsstatus
     * @param allDepartments
     * @param allPositions
     * @param allJobLevels
     * @return
     */

    public static List<Employee> excel2Employee(MultipartFile file, List<Nation> allNations, List<Politicsstatus> allPoliticsstatus, List<Department> allDepartments, List<Position> allPositions, List<JobLevel> allJobLevels) throws IOException {
        List<Employee> list = new ArrayList<>();
        Employee employee = null;
        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            for (int j = 1; j < physicalNumberOfRows; j++) {
                HSSFRow row = sheet.getRow(j);
                if (row == null) continue;
                int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                employee = new Employee();
                boolean needAdd = true;
                for (int k = 0; k < physicalNumberOfCells; k++) {
                    HSSFCell cell = row.getCell(k);
                    if (cell == null) {
                        needAdd = false;
                        break;
                    }
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getStringCellValue();
                        switch (k) {
                            case 1:
                                employee.setName(cellValue);
                                break;
                            case 2:
                                employee.setWorkId(cellValue);
                                break;
                            case 3:
                                employee.setGender(cellValue);
                                break;
                            case 5:
                                employee.setIdCard(cellValue);
                                break;
                            case 6:
                                employee.setWedlock(cellValue);
                                break;
                            case 7:
                                int nationIndex = allNations.indexOf(new Nation(cellValue));
                                employee.setNationId(allNations.get(nationIndex).getId());
                                break;
                            case 8:
                                employee.setNativePlace(cellValue);
                                break;
                            case 9:
                                int politicstatusIndex = allPoliticsstatus.indexOf(new Politicsstatus(cellValue));
                                employee.setPoliticId(allPoliticsstatus.get(politicstatusIndex).getId());
                                break;
                            case 10:
                                employee.setPhone(cellValue);
                                break;
                            case 11:
                                employee.setAddress(cellValue);
                                break;
                            case 12:
                                int departmentIndex = allDepartments.indexOf(new Department(cellValue));
                                employee.setDepartmentId(allDepartments.get(departmentIndex).getId());
                                break;
                            case 13:
                                int jobLevelIndex = allJobLevels.indexOf(new JobLevel(cellValue));
                                employee.setJobLevelId(allJobLevels.get(jobLevelIndex).getId());
                                break;
                            case 14:
                                int positionIndex = allPositions.indexOf(new Position(cellValue));
                                employee.setPosId(allPositions.get(positionIndex).getId());
                                break;
                            case 15:
                                employee.setEngageForm(cellValue);
                                break;
                            case 16:
                                employee.setTiptopDegree(cellValue);
                                break;
                            case 17:
                                employee.setSpecialty(cellValue);
                                break;
                            case 18:
                                employee.setSchool(cellValue);
                                break;
                            case 20:
                                employee.setWorkState(cellValue);
                                break;
                            case 21:
                                employee.setEmail(cellValue);
                                break;
                        }
                    } else {
                        switch (k) {
                            case 4:
                                employee.setBirthday(cell.getDateCellValue());
                                break;
                            case 19:
                                employee.setBeginDate(cell.getDateCellValue());
                                break;
                            case 23:
                                employee.setBeginContract(cell.getDateCellValue());
                                break;
                            case 24:
                                employee.setEndContract(cell.getDateCellValue());
                                break;
                            case 22:
                                employee.setContractTerm(cell.getNumericCellValue());
                                break;
                            case 25:
                                employee.setConversionTime(cell.getDateCellValue());
                                break;
                        }
                    }
                }
                if(needAdd) list.add(employee);
            }
        }
        return list;
    }
}
