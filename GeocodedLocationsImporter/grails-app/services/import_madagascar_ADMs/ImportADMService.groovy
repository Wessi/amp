package import_madagascar_ADMs

import au.com.bytecode.opencsv.CSVReader
import org.apache.commons.fileupload.disk.DiskFileItem
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.springframework.web.multipart.commons.CommonsMultipartFile

class ImportADMService {

    public static final String COUNTRY_MADAGASCAR = "MADAGASCAR"

    def importADM3s(LinkedList<CommonsMultipartFile> files) {
        HSSFWorkbook rm = null
        DiskFileItem regionManagerExport = null
        DiskFileItem csvFromShapefile = null
        List<String> errors = new ArrayList<String>()
        files?.each {
            log.info(it.fileItem)
            if (it.fileItem.name.contains(".xls")) {
                regionManagerExport = it.fileItem
            } else if (it.fileItem.name.contains(".csv")) {
                csvFromShapefile = it.fileItem
            }
        }
        if (regionManagerExport != null && csvFromShapefile != null) {
            CSVReader csv = new CSVReader(new BufferedReader(new InputStreamReader(csvFromShapefile.getInputStream())), ";".toCharacter())
            rm = new HSSFWorkbook(regionManagerExport.getInputStream())
            HSSFSheet sheet = rm.getSheetAt(0)
            int maxId = findMaxId(sheet) + 1
            String[] currentRow;
            int i = 0;
            while ((currentRow = csv.readNext()) != null) {
                if (i > 0) {
                    // Get data from CSV.
                    log.info(currentRow.toString())
                    String ADM1 = currentRow[9].trim()
                    String ADM2 = currentRow[8].trim()
                    String ADM3 = currentRow[7].trim()
                    Double lat = new Double(currentRow[10].trim())
                    Double lon = new Double(currentRow[11].trim())
                    Integer geoID = new Integer(currentRow[12].trim())

                    // Find ADM in Excel (sanity check).
                    Row auxRow = findADMInExcel(sheet, ADM1, ADM2, ADM3, errors, maxId)
                    if (auxRow != null) {
                        Row insertRow = sheet.createRow(sheet.getLastRowNum() + 1)
                        insertRow.createCell(0).setCellValue(geoID)
                        insertRow.createCell(1).setCellValue(COUNTRY_MADAGASCAR)
                        insertRow.createCell(2).setCellValue(auxRow.getCell(2).getStringCellValue())
                        insertRow.createCell(3).setCellValue(auxRow.getCell(3).getStringCellValue())
                        insertRow.createCell(4).setCellValue(ADM3)
                        insertRow.createCell(5).setCellValue(lat)
                        insertRow.createCell(6).setCellValue(lon)
                        insertRow.createCell(7).setCellValue(geoID)
                    } else {
                        errors << "ERROR: Cant find ${currentRow.toString()} in Excel."
                    }
                }
                i++
            }
            rm.close()
            FileOutputStream outExcel = new FileOutputStream(regionManagerExport.storeLocation)
            rm.write(outExcel)
            outExcel.close()
        } else {
            errors << "ERROR: Missing Region Manager Export or missing CSV from Shapefile."
        }
        errors?.each {
            log.warn(it.toString())
        }
        return [errors: errors, excel: regionManagerExport]
    }

    int findMaxId(HSSFSheet sheet) {
        int ret = 0
        Iterator<Row> rowIterator = sheet.iterator()
        while (rowIterator.hasNext()) {
            Row auxRow = rowIterator.next()
            if (auxRow.getCell(0).cellType == Cell.CELL_TYPE_NUMERIC) {
                if (auxRow.getCell(0).getNumericCellValue() > ret) {
                    ret = (int) auxRow.getCell(0).getNumericCellValue()
                }
                if (auxRow.getCell(7).getNumericCellValue() > ret) {
                    ret = (int) auxRow.getCell(7).getNumericCellValue()
                }
            }
        }
        return ret
    }

    Row findADMInExcel(HSSFSheet sheet, String ADM1, String ADM2, String ADM3, errors, int id) {
        Row ret = null
        Iterator<Row> rowIterator = sheet.iterator()
        while (rowIterator.hasNext()) {
            Row auxRow = rowIterator.next()
            if (auxRow.getCell(3) != null) {
                if (auxRow.getCell(3).getStringCellValue().trim().equalsIgnoreCase(ADM2)) {
                    ret = auxRow
                }
            }
        }
        return ret
    }
}