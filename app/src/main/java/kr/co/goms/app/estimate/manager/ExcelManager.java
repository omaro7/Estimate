package kr.co.goms.app.estimate.manager;

import static org.apache.poi.ss.usermodel.Font.U_DOUBLE;
import static org.apache.poi.ss.usermodel.Font.U_NONE;

import android.app.Activity;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.common.util.IOUtils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.app.estimate.model.EstimateBeanTB;
import kr.co.goms.app.estimate.model.ItemBeanTB;
import kr.co.goms.module.common.util.DateUtil;
import kr.co.goms.module.common.util.FileUtil;
import kr.co.goms.module.common.util.FormatUtil;
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.ImageUtil;
import kr.co.goms.module.common.util.StringUtil;

/**
 * 사용법

 */
public class ExcelManager {

    private static final String LOG_TAG = ExcelManager.class.getSimpleName();
    static ExcelManager mExcelManager;    //instance
    static Activity mActivity;
    HSSFWorkbook mWorkbook;

    HSSFSheet mSheet;           //맨홀조사야장
    HSSFCellStyle mHSSFCellStyleMainTitle;           //셀스타일
    HSSFCellStyle mHSSFCellStyleBorderRight;//셀스타일-border right
    HSSFCellStyle mHSSFCellStyleBorderLeftRight;//셀스타일-border left, right
    HSSFCellStyle mHSSFCellStyleBorderTopRight;//셀스타일-border top, right
    HSSFCellStyle mHSSFCellStyleBorderAll;      //셀스타일-border all
    HSSFFont mSumFont, mFont, mFontDataList, mFontReport;
    Row mRow;
    public enum SHEET_TYPE {
        SUMMARY,
        SUMMARY_DETAIL,
        SUMMARY_EXTENSION,
        DETAIL,
    }

    public enum CELL_TYPE_BG{
        Y,
        N
    }

    public enum HEIGHT_SIZE{
        MAIN((short)720),   //메인 타이틀 높이
        TITLE((short)360), //일반 높이
        VALUE((short)360),
        DIV((short)140),    //간격 높이
        EXTENTION((short) 400),//연장집계표 행 높이
        REPORT_ROW((short) 400),//맨홀조사야장 행 높이
        BLOCK_ROW((short) 300),//맨홀조사야장 > 블록 행 높이
        ;

        short size;
        HEIGHT_SIZE(short i) {
            this.size = i;
        }
    }

    // 폰트크기 설정
    // 원하는 폰트크기의 X2 후 0을 붙여줍니다.
    // ex 14 -> 280
    public enum TITLE_SIZE{
        MAIN((short)400),           //20
        PRICE_TITLE((short)240),    //12
        PRICE_KOR((short)240),      //12
        PRICE_UNIT((short)240),
        PRICE_NUMBER((short)240),   //12
        TITLE((short)160),          //8
        VALUE((short)160),          //8
        ;

        short size;
        TITLE_SIZE(short i) {
            this.size = i;
        }
    }

    enum CELL_STYLE {
        MAIN_TITLE(CELL_TYPE_BG.N, "견적서", 0, 0, 0, 0, 0, 21, HEIGHT_SIZE.MAIN.size, TITLE_SIZE.MAIN.size, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_DOUBLE),
        CLIENT_NAME_VALUE(CELL_TYPE_BG.N,"", 1, 0, 1, 1, 0, 6, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        CLIENT_NAME_TO(CELL_TYPE_BG.N,"귀중", 1, 7, 1, 1, 7, 8, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_TITLE_01(CELL_TYPE_BG.Y,"공", 1, 9, 1, 2, 9, 9, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_TITLE_02(CELL_TYPE_BG.Y,"급", 3, 9, 3, 4, 9, 9, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_TITLE_03(CELL_TYPE_BG.Y,"자", 5, 9, 5, 6, 9, 9, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_BIZ_NUM_TITLE(CELL_TYPE_BG.Y,"등록번호", 1, 10, 1, 1, 10, 13, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_BIZ_NUM_VALUE(CELL_TYPE_BG.Y,"", 1, 14, 1, 1, 14, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_NAME_TITLE(CELL_TYPE_BG.Y,"상호명", 2, 10, 2, 2, 10, 13, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_NAME_VALUE(CELL_TYPE_BG.Y,"", 2, 14, 2, 2, 14, 19, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_STEMP_VALUE(CELL_TYPE_BG.Y,"", 2, 20, 2, 2, 20, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ESTIMATE_DATE_TITLE(CELL_TYPE_BG.Y,"견 적 일", 3, 0, 3, 3, 0, 1, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        SPECIFICATION_DATE_TITLE(CELL_TYPE_BG.Y,"거 래 일", 3, 0, 3, 3, 0, 1, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        ESTIMATE_DATE_VALUE(CELL_TYPE_BG.Y,"", 3, 2, 3, 3, 2, 8, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_CEO_NAME_TITLE(CELL_TYPE_BG.Y,"대표명", 3, 10, 3, 3, 10, 13, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_CEO_NAME_VALUE(CELL_TYPE_BG.Y,"", 3, 14, 3, 3, 14, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        MANAGER_NAME_TITLE(CELL_TYPE_BG.Y,"담 당 자", 4, 0, 4, 4, 0, 1, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        SPEC_MANAGER_NAME_TITLE(CELL_TYPE_BG.Y,"인 수 자", 4, 0, 4, 4, 0, 1, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        MANAGER_NAME_VALUE(CELL_TYPE_BG.Y,"", 4, 2, 4, 4, 2, 8, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        COMPANY_ADDRESS_TITLE(CELL_TYPE_BG.Y,"사업장주소", 4, 10, 4, 4, 10, 13, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_ADDRESS_VALUE(CELL_TYPE_BG.Y,"", 4, 14, 4, 4, 14, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),


        COMPANY_UPTAE_TITLE(CELL_TYPE_BG.Y,"업태", 5, 10, 5, 5, 10, 13, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_UPTAE_VALUE(CELL_TYPE_BG.Y,"", 5, 14, 5, 5, 14, 16, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        COMPANY_UPJONG_TITLE(CELL_TYPE_BG.Y,"업종", 5, 17, 5, 5, 17, 18, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_UPJONG_VALUE(CELL_TYPE_BG.Y,"", 5, 19, 5, 5, 19, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        COMPANY_TEL_TITLE(CELL_TYPE_BG.Y,"전화번호", 6, 10, 6, 6, 10, 13, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        COMPANY_TEL_VALUE(CELL_TYPE_BG.Y,"", 6, 14, 6, 6, 14, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        DIV_7(CELL_TYPE_BG.Y,"", 7, 0, 7, 7, 0, 21, HEIGHT_SIZE.DIV.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        EFFECTIVE_DATE_TITLE(CELL_TYPE_BG.Y,"", 8, 0, 8, 8, 0, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        DIV_9(CELL_TYPE_BG.Y,"", 9, 0, 9, 9, 0, 21, HEIGHT_SIZE.DIV.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        MONEY_TITLE(CELL_TYPE_BG.Y,"금액", 10, 0, 10, 10, 0, 2, HEIGHT_SIZE.REPORT_ROW.size, TITLE_SIZE.PRICE_TITLE.size, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        MONEY_KOR_VALUE(CELL_TYPE_BG.Y,"", 10, 3, 10, 10, 3, 13, HEIGHT_SIZE.REPORT_ROW.size, TITLE_SIZE.PRICE_KOR.size, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        MONEY_UNIT_VALUE(CELL_TYPE_BG.Y,"₩", 10, 14, 10, 10, 14, 16, HEIGHT_SIZE.REPORT_ROW.size, TITLE_SIZE.PRICE_UNIT.size, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        MONEY_NUM_VALUE(CELL_TYPE_BG.Y,"", 10, 17, 10, 10, 17, 21, HEIGHT_SIZE.REPORT_ROW.size, TITLE_SIZE.PRICE_NUMBER.size, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        DIV_11(CELL_TYPE_BG.Y,"", 11, 0, 11, 11, 0, 21, HEIGHT_SIZE.DIV.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        ITEM_NO_TITLE(CELL_TYPE_BG.Y,"No", 12, 0, 12, 12, 0, 0, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_ITEM_TITLE(CELL_TYPE_BG.Y,"상품", 12, 1, 12, 12, 1, 7, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_QUANTITY_TITLE(CELL_TYPE_BG.Y,"수량", 12, 8, 12, 12, 8, 9, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_UNIT_TITLE(CELL_TYPE_BG.Y,"단위", 12, 10, 12, 12, 10, 10, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_UNIT_MONEY_TITLE(CELL_TYPE_BG.Y,"단가", 12, 11, 12, 12, 11, 12, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_MONEY_TITLE(CELL_TYPE_BG.Y,"금액", 12, 13, 12, 12, 13, 15, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_TAX_MONEY_TITLE(CELL_TYPE_BG.Y,"세액", 12, 16, 12, 12, 16, 18, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_TOTAL_MONEY_TITLE(CELL_TYPE_BG.Y,"총금액", 12, 19, 12, 12, 19, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        ITEM_NO_VALUE(CELL_TYPE_BG.N,"", 13, 0, 13, 13, 0, 0, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_ITEM_VALUE(CELL_TYPE_BG.Y,"", 13, 1, 13, 13, 1, 7, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_QUANTITY_VALUE(CELL_TYPE_BG.Y,"", 13, 8, 13, 13, 8, 9, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_UNIT_VALUE(CELL_TYPE_BG.Y,"", 13, 10, 13, 13, 10, 10, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_UNIT_MONEY_VALUE(CELL_TYPE_BG.Y,"", 13, 11, 13, 13, 11, 12, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_MONEY_VALUE(CELL_TYPE_BG.Y,"", 13, 13, 13, 13, 13, 15, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_TAX_MONEY_VALUE(CELL_TYPE_BG.Y,"", 13, 16, 13, 13, 16, 18, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        ITEM_TOTAL_MONEY_VALUE(CELL_TYPE_BG.Y,"", 13, 19, 13, 13, 19, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),


        DIV_31(CELL_TYPE_BG.N,"", 31, 0, 31, 31, 0, 21, HEIGHT_SIZE.DIV.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        REMARK_TITLE(CELL_TYPE_BG.Y,"세부사항", 32, 0, 32, 32, 0, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        REMARK_VALUE(CELL_TYPE_BG.Y,"", 33, 0, 33, 36, 0, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        BOTTOM(CELL_TYPE_BG.Y,"", 37, 0, 37, 37, 0, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),
        TEST(CELL_TYPE_BG.Y,"", 38, 0, 38, 38, 0, 21, HEIGHT_SIZE.REPORT_ROW.size, (short) 110, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, true, U_NONE),

        ;

        CELL_TYPE_BG cellTypeBg;
        String title;
        int row, col, firstRow, lastRow, firstCol, lastCol;
        short rowHeight, fontHeight, hAlignCenter, vAlignCenter;
        boolean bold;
        byte underline;
        CELL_STYLE(CELL_TYPE_BG cellTypeBg, String title, int row, int col, int firstRow, int lastRow, int firstCol, int lastCol, short rowHeight, short fontHeight, short hAlignCenter, short vAlignCenter, boolean bold, byte underline) {
            this.cellTypeBg = cellTypeBg;
            this.title = title;
            this.row=row;
            this.col = col;
            this.firstRow = firstRow;
            this.lastRow = lastRow;
            this.firstCol = firstCol;
            this.lastCol = lastCol;
            this.rowHeight = rowHeight;
            this.fontHeight = fontHeight;
            this.hAlignCenter = hAlignCenter;
            this.vAlignCenter = vAlignCenter;
            this.bold = bold;
            this.underline = underline;
        }
    }

    Font mFontNoramValue;

    public ExcelManager() {
    }

    public static ExcelManager I(Activity activity){
        if(mExcelManager == null){
            mExcelManager = new ExcelManager();
        }
        mActivity = activity;
        return mExcelManager;
    }

    public HSSFWorkbook createWorkbook() {

        if(mWorkbook != null){
            try {
                mWorkbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        mWorkbook = workbook;
        return workbook;
    }

    public HSSFFont getFont(){
        if(mFont == null) {
            mFont = mWorkbook.createFont();
        }
        return mFont;
    }

    public HSSFFont getFontDataType(){
        if(mFontDataList == null) {
            mFontDataList = mWorkbook.createFont();
        }
        return mFontDataList;
    }

    short hAlignCenter = CellStyle.ALIGN_CENTER;
    short hAlignLeft = CellStyle.ALIGN_LEFT;
    short vAlignCenter = CellStyle.VERTICAL_CENTER;
    short vAlignTop = CellStyle.VERTICAL_TOP;

    public HSSFCellStyle createCellStyle(){
        return mWorkbook.createCellStyle();
    }

    /**
     * 페이지 중앙정렬 처리
     * @param sheet
     */
    public void setPaperAligment(Sheet sheet){
        sheet.setDisplayGridlines(false);                               //가이드라인 안보이게

        sheet.getPrintSetup().setLandscape(false);                      //세로모드 A4사이즈
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);    //출력시 A4사이즈
        sheet.getPrintSetup().setFitWidth((short)1);  //가로는 1페이지에
        sheet.getPrintSetup().setFitHeight((short)0);  //세로는 자동으로

        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true); //출력시 가로정렬 Center로
        sheet.setVerticallyCenter(true);

    }

    public void setColumeWidth(Sheet sheet, SHEET_TYPE sheetType){

        sheet.setColumnWidth(0, 1000);  //No.
        sheet.setColumnWidth(1, 850);  //상품
        sheet.setColumnWidth(2, 850);
        sheet.setColumnWidth(3, 850);
        sheet.setColumnWidth(4, 850);
        sheet.setColumnWidth(5, 850);
        sheet.setColumnWidth(6, 850);
        sheet.setColumnWidth(7, 850);
        sheet.setColumnWidth(8, 1000);   //수량
        sheet.setColumnWidth(9, 1000);
        sheet.setColumnWidth(10, 1000); //단위
        sheet.setColumnWidth(11, 1000); //단가
        sheet.setColumnWidth(12, 1000);
        sheet.setColumnWidth(13, 1000); //금액
        sheet.setColumnWidth(14, 1000);
        sheet.setColumnWidth(15, 1000);
        sheet.setColumnWidth(16, 1000); //세액
        sheet.setColumnWidth(17, 1000);
        sheet.setColumnWidth(18, 1000);
        sheet.setColumnWidth(19, 1000); //총금액
        sheet.setColumnWidth(20, 1000);
        sheet.setColumnWidth(21, 1000);
    }

    /**
     *제목 스타일
     */
    private void setMainTitleStyle(HSSFSheet sheet, Row row , CELL_STYLE cellStyle, @Nullable String text, HSSFCellStyle cellStyleMainTitle){

        Cell cell = row.createCell(cellStyle.col);

        //해당 셀의 Row 높이 지정하기
        row.setHeight(cellStyle.rowHeight);

        //해당 셀에 타이틀 지정하기
        String cellValue = cellStyle.title;
        if(!StringUtil.isEmpty(text)){
            cellValue = text;
        }
        cell.setCellValue(cellValue);

        //row, col 병합 처리하기
        int iRegion = sheet.addMergedRegion(new CellRangeAddress(cellStyle.firstRow, cellStyle.lastRow, cellStyle.firstCol, cellStyle.lastCol));

        CellRangeAddress mergedRegion = sheet.getMergedRegion(iRegion);
        for (int iRow = mergedRegion.getFirstRow(); iRow <= mergedRegion.getLastRow(); iRow++) {

            Row currentRow = sheet.getRow(iRow);
            if (currentRow == null) {
                currentRow = sheet.createRow(iRow);
            }

            for (int iCol = mergedRegion.getFirstColumn(); iCol <= mergedRegion.getLastColumn(); iCol++) {
                Cell borderCell;
                try {
                    borderCell = currentRow.getCell(iCol);
                    if (borderCell == null) {
                        borderCell = currentRow.createCell(iCol);
                    }
                }catch(NullPointerException e){
                    borderCell = currentRow.getCell(iCol);
                }

                borderCell.setCellStyle(cellStyleMainTitle);
            }

        }
    }

    /**
     *금액 스타일
     */
    private void setMoneyStyle(HSSFSheet sheet, Row row , CELL_STYLE cellStyle, @Nullable String text, HSSFCellStyle cellStyleMainTitle){

        Cell cell = row.createCell(cellStyle.col);

        //해당 셀의 Row 높이 지정하기
        row.setHeight(cellStyle.rowHeight);

        //해당 셀에 타이틀 지정하기
        String cellValue = cellStyle.title;
        if(!StringUtil.isEmpty(text)){
            cellValue = text;
        }
        cell.setCellValue(cellValue);

        //row, col 병합 처리하기
        int iRegion = sheet.addMergedRegion(new CellRangeAddress(cellStyle.firstRow, cellStyle.lastRow, cellStyle.firstCol, cellStyle.lastCol));

        CellRangeAddress mergedRegion = sheet.getMergedRegion(iRegion);
        for (int iRow = mergedRegion.getFirstRow(); iRow <= mergedRegion.getLastRow(); iRow++) {

            Row currentRow = sheet.getRow(iRow);
            if (currentRow == null) {
                currentRow = sheet.createRow(iRow);
            }

            for (int iCol = mergedRegion.getFirstColumn(); iCol <= mergedRegion.getLastColumn(); iCol++) {
                Cell borderCell;
                try {
                    borderCell = currentRow.getCell(iCol);
                    if (borderCell == null) {
                        borderCell = currentRow.createCell(iCol);
                    }
                }catch(NullPointerException e){
                    borderCell = currentRow.getCell(iCol);
                }

                borderCell.setCellStyle(cellStyleMainTitle);
            }

        }
    }


    /**
     * 배경 컬럼 그레이
     * MainTitle row0, CELL_STYLE_MAIN
     * @param row
     * @param cellStyle
     */
    public void setStyleBg(HSSFSheet sheet, Row row, CELL_STYLE cellStyle,@Nullable String text, HSSFCellStyle hssfCellStyle, CreationHelper creationHelper){

        Cell cell = row.createCell(cellStyle.col);

        //해당 셀의 Row 높이 지정하기
        row.setHeight(cellStyle.rowHeight);

        //해당 셀에 타이틀 지정하기
        String cellValue = cellStyle.title;
        if(!StringUtil.isEmpty(text)){
            cellValue = text;
        }

        cell.setCellValue(cellValue);

        //row, col 병합 처리하기
        int iRegion = sheet.addMergedRegion(new CellRangeAddress(cellStyle.firstRow, cellStyle.lastRow, cellStyle.firstCol, cellStyle.lastCol));

        CellRangeAddress mergedRegion = sheet.getMergedRegion(iRegion);
        for (int iRow = mergedRegion.getFirstRow(); iRow <= mergedRegion.getLastRow(); iRow++) {

            Row currentRow = sheet.getRow(iRow);
            if (currentRow == null) {
                currentRow = sheet.createRow(iRow);
            }

            for (int iCol = mergedRegion.getFirstColumn(); iCol <= mergedRegion.getLastColumn(); iCol++) {
                Cell borderCell;
                try {
                    borderCell = currentRow.getCell(iCol);
                    if (borderCell == null) {
                        borderCell = currentRow.createCell(iCol);
                    }
                }catch(NullPointerException e){
                    borderCell = currentRow.getCell(iCol);
                }
                borderCell.setCellStyle(hssfCellStyle);
            }
        }
    }

    public void setStyleCreateCell(HSSFSheet sheet, Row row, Cell cell, CELL_STYLE cellStyle, String text, HSSFCellStyle hssfCellStyle){
        //해당 셀의 Row 높이 지정하기
        row.setHeight(cellStyle.rowHeight);

        //해당 셀에 타이틀 지정하기
        String cellValue = cellStyle.title;
        if(!StringUtil.isEmpty(text)){
            cellValue = text;
        }

        cell.setCellValue(cellValue);

        //row, col 병합 처리하기
        int iRegion = sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cellStyle.firstCol, cellStyle.lastCol));

        CellRangeAddress mergedRegion = sheet.getMergedRegion(iRegion);
        for (int iRow = mergedRegion.getFirstRow(); iRow <= mergedRegion.getLastRow(); iRow++) {

            Row currentRow = sheet.getRow(iRow);
            if (currentRow == null) {
                currentRow = sheet.createRow(iRow);
            }

            for (int iCol = mergedRegion.getFirstColumn(); iCol <= mergedRegion.getLastColumn(); iCol++) {
                Cell borderCell;
                try {
                    borderCell = currentRow.getCell(iCol);
                    if (borderCell == null) {
                        borderCell = currentRow.createCell(iCol);
                    }
                }catch(NullPointerException e){
                    borderCell = currentRow.getCell(iCol);
                }

                borderCell.setCellStyle(hssfCellStyle);

            }

        }
    }

    /**
     * 일반 컬럼
     * @param sheet
     * @param row
     * @param cellStyle
     * @param text
     * @param hssfCellStyle
     */
    public void setStyle(HSSFSheet sheet, Row row, CELL_STYLE cellStyle,@Nullable String text, HSSFCellStyle hssfCellStyle){

        Cell cell = row.createCell(cellStyle.col);

        //해당 셀의 Row 높이 지정하기
        row.setHeight(cellStyle.rowHeight);

        //해당 셀에 타이틀 지정하기
        String cellValue = cellStyle.title;
        if(!StringUtil.isEmpty(text)){
            cellValue = text;
        }

        cell.setCellValue(cellValue);

        //row, col 병합 처리하기
        int iRegion = sheet.addMergedRegion(new CellRangeAddress(cellStyle.firstRow, cellStyle.lastRow, cellStyle.firstCol, cellStyle.lastCol));

        CellRangeAddress mergedRegion = sheet.getMergedRegion(iRegion);
        for (int iRow = mergedRegion.getFirstRow(); iRow <= mergedRegion.getLastRow(); iRow++) {

            Row currentRow = sheet.getRow(iRow);
            if (currentRow == null) {
                currentRow = sheet.createRow(iRow);
            }

            for (int iCol = mergedRegion.getFirstColumn(); iCol <= mergedRegion.getLastColumn(); iCol++) {
                Cell borderCell;
                try {
                    borderCell = currentRow.getCell(iCol);
                    if (borderCell == null) {
                        borderCell = currentRow.createCell(iCol);
                    }
                }catch(NullPointerException e){
                    borderCell = currentRow.getCell(iCol);
                }
                borderCell.setCellStyle(hssfCellStyle);
            }
        }
    }

    public void setStyleRemark(HSSFSheet sheet, Row row, CELL_STYLE cellStyle,@Nullable String text, HSSFCellStyle hssfCellStyle){

        Cell cell = row.createCell(cellStyle.col);

        //해당 셀의 Row 높이 지정하기
        row.setHeight(cellStyle.rowHeight);

        //해당 셀에 타이틀 지정하기
        String cellValue = cellStyle.title;
        if(!StringUtil.isEmpty(text)){
            cellValue = text;
        }

        cell.setCellValue(cellValue);

        //row, col 병합 처리하기
        int iRegion = sheet.addMergedRegion(new CellRangeAddress(cellStyle.firstRow, cellStyle.lastRow, cellStyle.firstCol, cellStyle.lastCol));

        CellRangeAddress mergedRegion = sheet.getMergedRegion(iRegion);
        for (int iRow = mergedRegion.getFirstRow(); iRow <= mergedRegion.getLastRow(); iRow++) {

            Row currentRow = sheet.getRow(iRow);
            if (currentRow == null) {
                currentRow = sheet.createRow(iRow);
            }

            for (int iCol = mergedRegion.getFirstColumn(); iCol <= mergedRegion.getLastColumn(); iCol++) {
                Cell borderCell;
                try {
                    borderCell = currentRow.getCell(iCol);
                    if (borderCell == null) {
                        borderCell = currentRow.createCell(iCol);
                    }
                }catch(NullPointerException e){
                    borderCell = currentRow.getCell(iCol);
                }

                borderCell.setCellStyle(hssfCellStyle);

            }

        }
    }

    /**
     * 엑셀 생성 시작 > 맨홀 야장 조사
     * for문으로 detailList array 기반으로 "맨홀 야장 조사" 탭 생성 및 내용을 기입하자
     * @param estimateBeanTB       //기본 데이타
     */
    public void createExcel(EstimateBeanTB estimateBeanTB, AppConstant.SAVE_EXCEL_TYPE saveExcelType) {
        GomsLog.d(LOG_TAG, "createExcel()");

        Font fontReportBg = mWorkbook.createFont();
        fontReportBg.setBold(false);
        fontReportBg.setFontHeight(TITLE_SIZE.TITLE.size);  //원하는 폰트크기의 X2 후 0을 붙여줍니다.  ex 14 -> 280
        fontReportBg.setUnderline(U_NONE);
        fontReportBg.setFontName("맑은 고딕");

        mFontNoramValue = mWorkbook.createFont();
        mFontNoramValue.setBold(false);
        mFontNoramValue.setFontHeight(TITLE_SIZE.TITLE.size);
        mFontNoramValue.setUnderline(U_NONE);
        mFontNoramValue.setFontName("맑은 고딕");

        Font fontReportMainTitle = mWorkbook.createFont();
        fontReportMainTitle.setBold(true);
        fontReportMainTitle.setFontHeight(TITLE_SIZE.MAIN.size);
        fontReportMainTitle.setUnderline(U_DOUBLE);
        fontReportMainTitle.setFontName("맑은 고딕");

        //CellStyle 객체생성 - 배경
        HSSFCellStyle hssfCellStyleBg = createCellStyle();
        hssfCellStyleBg.setAlignment(hAlignCenter);
        hssfCellStyleBg.setVerticalAlignment(vAlignCenter);
        hssfCellStyleBg.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleBg.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleBg.setFillPattern((short) 1);
        hssfCellStyleBg.setBorderTop((short)1);
        hssfCellStyleBg.setBorderBottom((short)1);
        hssfCellStyleBg.setBorderLeft((short)1);
        hssfCellStyleBg.setBorderRight((short)1);
        hssfCellStyleBg.setFont(fontReportBg);

        //CellStyle 객체생성 - 값
        HSSFCellStyle hssfCellStyleValue = createCellStyle();
        hssfCellStyleValue.setAlignment(hAlignCenter);
        hssfCellStyleValue.setVerticalAlignment(vAlignCenter);
        hssfCellStyleValue.setBorderTop((short)1);
        hssfCellStyleValue.setBorderBottom((short)1);
        hssfCellStyleValue.setBorderLeft((short)1);
        hssfCellStyleValue.setBorderRight((short)1);
        hssfCellStyleValue.setFont(mFontNoramValue);

        //CellStyle 객체생성 - 값 > 라인없음
        HSSFCellStyle hssfCellStyleNoLine = createCellStyle();
        hssfCellStyleNoLine.setAlignment(hAlignCenter);
        hssfCellStyleNoLine.setVerticalAlignment(vAlignCenter);
        hssfCellStyleNoLine.setFont(mFontNoramValue);

        //CellStyle 객체생성 - 특이사항
        HSSFCellStyle hssfCellStyleRemark = createCellStyle();
        hssfCellStyleRemark.setAlignment(hAlignLeft);
        hssfCellStyleRemark.setVerticalAlignment(vAlignTop);
        hssfCellStyleRemark.setBorderTop((short)1);
        hssfCellStyleRemark.setBorderBottom((short)1);
        hssfCellStyleRemark.setBorderLeft((short)1);
        hssfCellStyleRemark.setBorderRight((short)1);
        hssfCellStyleRemark.setFont(mFontNoramValue);

        //CellStyle 객체생성 - 메인타이틀
        HSSFCellStyle hssfCellStyleMainTitle = createCellStyle();
        hssfCellStyleMainTitle.setAlignment(hAlignCenter);
        hssfCellStyleMainTitle.setVerticalAlignment(vAlignCenter);
        hssfCellStyleMainTitle.setFont(fontReportMainTitle);

        CreationHelper creationHelper = mWorkbook.getCreationHelper();

        CompanyBeanTB companyBeanTB = MyApplication.getInstance().getDBHelper().getCompany(estimateBeanTB.getCom_idx());

        //상세리스트별 맨홀야장조사 탭 생성 및 데이타 주입

        if(AppConstant.SAVE_EXCEL_TYPE.ESTIMATE == saveExcelType) {
            setEstimateExcel(estimateBeanTB, companyBeanTB, hssfCellStyleBg, hssfCellStyleValue, hssfCellStyleRemark, hssfCellStyleMainTitle, hssfCellStyleNoLine, creationHelper);
            saveExcel(estimateBeanTB, AppConstant.SAVE_EXCEL_TYPE.ESTIMATE.name().toLowerCase());
        }else{
            setSpecificationExcel(estimateBeanTB, companyBeanTB, hssfCellStyleBg, hssfCellStyleValue, hssfCellStyleRemark, hssfCellStyleMainTitle, hssfCellStyleNoLine, creationHelper);
            saveExcel(estimateBeanTB, AppConstant.SAVE_EXCEL_TYPE.SPECIFICATION.name().toLowerCase());
        }

    }

    /**
     * 견적서 엑셀 데이타 주입 처리
     * createManholeExcel에서 호출
     * @param estimateBeanTB
     */
    private void setEstimateExcel(EstimateBeanTB estimateBeanTB, CompanyBeanTB companyBeanTB, HSSFCellStyle cellStyleBg, HSSFCellStyle cellStyleValue, HSSFCellStyle cellStyleRemark, HSSFCellStyle cellStyleMainTitle, HSSFCellStyle cellStyleNoLine, CreationHelper creationHelper) {

        HSSFSheet sheet = mWorkbook.createSheet("견적서");

        //첫번째 행 >> 맨홀 조사 야장
        Row row0 = sheet.createRow(0);
        setMainTitleStyle(sheet,  row0, CELL_STYLE.MAIN_TITLE, "견적서", cellStyleMainTitle);

        Row row1 = sheet.createRow(1);

        setStyle(sheet, row1,  CELL_STYLE.CLIENT_NAME_VALUE, estimateBeanTB.getEst_cli_name(), cellStyleNoLine);
        setStyle(sheet, row1,  CELL_STYLE.CLIENT_NAME_TO, CELL_STYLE.CLIENT_NAME_TO.title, cellStyleNoLine);
        setStyleBg(sheet, row1,  CELL_STYLE.COMPANY_TITLE_01, CELL_STYLE.COMPANY_TITLE_01.title, cellStyleBg, creationHelper);

        setStyleBg(sheet, row1,  CELL_STYLE.COMPANY_BIZ_NUM_TITLE, CELL_STYLE.COMPANY_BIZ_NUM_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row1,  CELL_STYLE.COMPANY_BIZ_NUM_VALUE, FormatUtil.addHyphenToBusinessNumber(estimateBeanTB.getEst_com_biz_num()), cellStyleValue);

        Row row2 = sheet.createRow(2);
        setStyleBg(sheet, row2,  CELL_STYLE.COMPANY_NAME_TITLE, CELL_STYLE.COMPANY_NAME_TITLE.title, cellStyleBg, creationHelper);

        HSSFCellStyle companyNameStyle = createCellStyle();

        companyNameStyle.setAlignment(hAlignCenter);
        companyNameStyle.setVerticalAlignment(vAlignCenter);
        companyNameStyle.setBorderBottom((short) 1);
        companyNameStyle.setBorderTop((short) 1);
        companyNameStyle.setBorderLeft((short) 1);
        companyNameStyle.setBorderRight((short) 0);
        companyNameStyle.setFont(mFontNoramValue);

        setStyle(sheet, row2,  CELL_STYLE.COMPANY_NAME_VALUE, estimateBeanTB.getEst_com_name(), companyNameStyle);

        HSSFCellStyle stempStyle = createCellStyle();
        stempStyle.setAlignment(hAlignCenter);
        stempStyle.setVerticalAlignment(vAlignCenter);
        stempStyle.setBorderBottom((short) 1);
        stempStyle.setBorderTop((short) 1);
        stempStyle.setBorderLeft((short) 0);
        stempStyle.setBorderRight((short) 1);
        setStyle(sheet, row2,  CELL_STYLE.COMPANY_STEMP_VALUE, "", stempStyle);


        Row row3 = sheet.createRow(3);
        setStyle(sheet, row3,  CELL_STYLE.ESTIMATE_DATE_TITLE, CELL_STYLE.ESTIMATE_DATE_TITLE.title, cellStyleNoLine);
        setStyle(sheet, row3,  CELL_STYLE.ESTIMATE_DATE_VALUE, DateUtil.displayDateFormat(estimateBeanTB.getEst_date(), "yyyyMMdd", "yyyy.MM.dd"), cellStyleNoLine);
        setStyleBg(sheet, row3,  CELL_STYLE.COMPANY_TITLE_02, CELL_STYLE.COMPANY_TITLE_02.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row3,  CELL_STYLE.COMPANY_CEO_NAME_TITLE, CELL_STYLE.COMPANY_CEO_NAME_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row3,  CELL_STYLE.COMPANY_CEO_NAME_VALUE, estimateBeanTB.getEst_com_ceo_name(), cellStyleValue);

        Row row4 = sheet.createRow(4);
        setStyle(sheet, row4,  CELL_STYLE.MANAGER_NAME_TITLE, CELL_STYLE.MANAGER_NAME_TITLE.title, cellStyleNoLine);
        setStyle(sheet, row4,  CELL_STYLE.MANAGER_NAME_VALUE, estimateBeanTB.getEst_com_manager_name(), cellStyleNoLine);

        setStyleBg(sheet, row4,  CELL_STYLE.COMPANY_ADDRESS_TITLE, CELL_STYLE.COMPANY_ADDRESS_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row4,  CELL_STYLE.COMPANY_ADDRESS_VALUE, estimateBeanTB.getEst_com_address_01() + " " + estimateBeanTB.getEst_com_address_02(), cellStyleValue);

        Row row5 = sheet.createRow(5);
        setStyleBg(sheet, row5,  CELL_STYLE.COMPANY_TITLE_03, CELL_STYLE.COMPANY_TITLE_03.title, cellStyleBg, creationHelper);

        setStyleBg(sheet, row5,  CELL_STYLE.COMPANY_UPTAE_TITLE, CELL_STYLE.COMPANY_UPTAE_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row5,  CELL_STYLE.COMPANY_UPTAE_VALUE, companyBeanTB.getCom_uptae(), cellStyleValue);
        setStyleBg(sheet, row5,  CELL_STYLE.COMPANY_UPJONG_TITLE, CELL_STYLE.COMPANY_UPJONG_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row5,  CELL_STYLE.COMPANY_UPJONG_VALUE, companyBeanTB.getCom_upjong(), cellStyleValue);

        Row row6 = sheet.createRow(6);
        setStyleBg(sheet, row6,  CELL_STYLE.COMPANY_TEL_TITLE, CELL_STYLE.COMPANY_TEL_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row6,  CELL_STYLE.COMPANY_TEL_VALUE, FormatUtil.addHyphenToPhoneNumber(companyBeanTB.getCom_tel_num()), cellStyleValue);

        Row row7 = sheet.createRow(7);
        setStyle(sheet, row7,  CELL_STYLE.DIV_7, CELL_STYLE.DIV_7.title, cellStyleNoLine);

        Row row8 = sheet.createRow(8);
        String effectiveDate = StringUtil.isEmpty(estimateBeanTB.getEst_effective_date())?" ": " " + estimateBeanTB.getEst_effective_date() + "까지 유효합니다.";
        setStyle(sheet, row8,  CELL_STYLE.EFFECTIVE_DATE_TITLE, "하기와 같이 견적합니다." + effectiveDate, cellStyleNoLine);

        Row row9 = sheet.createRow(9);
        setStyle(sheet, row9,  CELL_STYLE.DIV_9, CELL_STYLE.DIV_9.title, cellStyleNoLine);

        //중간 금액 부가세포함 등 Row 처리

        Row row10 = sheet.createRow(10);
        String money = "";
        try {
            money = FormatUtil.money(estimateBeanTB.getEst_total_price());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        StringBuffer moneyTitle = new StringBuffer();
        moneyTitle.append(CELL_STYLE.MONEY_TITLE.title);
        moneyTitle.append("\n");
        moneyTitle.append("Y".equalsIgnoreCase(estimateBeanTB.getEst_tax_type())?"(부가세포함)":"(부가세별도)");

        setStyleBg(sheet, row10,  CELL_STYLE.MONEY_TITLE, moneyTitle.toString(), cellStyleBg, creationHelper);
        setStyle(sheet, row10,  CELL_STYLE.MONEY_KOR_VALUE, FormatUtil.convertNumberToKorean(StringUtil.stringToLong(estimateBeanTB.getEst_total_price())), cellStyleValue);

        Font fontReportMainTitle = mWorkbook.createFont();
        fontReportMainTitle.setBold(true);
        fontReportMainTitle.setFontHeight(TITLE_SIZE.PRICE_KOR.size);
        fontReportMainTitle.setFontName("맑은 고딕");

        HSSFCellStyle hssfCellStyleMoneyTitle = createCellStyle();
        hssfCellStyleMoneyTitle.setAlignment(hAlignCenter);
        hssfCellStyleMoneyTitle.setVerticalAlignment(vAlignCenter);
        hssfCellStyleMoneyTitle.setBorderTop((short)1);
        hssfCellStyleMoneyTitle.setBorderBottom((short)1);
        hssfCellStyleMoneyTitle.setBorderLeft((short)1);
        hssfCellStyleMoneyTitle.setBorderRight((short)1);
        hssfCellStyleMoneyTitle.setFont(fontReportMainTitle);

        setMoneyStyle(sheet, row10,  CELL_STYLE.MONEY_KOR_VALUE, "금액 " + FormatUtil.convertNumberToKorean(StringUtil.stringToLong(estimateBeanTB.getEst_total_price())) + " 원", hssfCellStyleMoneyTitle);

        setStyle(sheet, row10,  CELL_STYLE.MONEY_UNIT_VALUE, CELL_STYLE.MONEY_UNIT_VALUE.title, cellStyleValue);

        setMoneyStyle(sheet, row10,  CELL_STYLE.MONEY_NUM_VALUE, money, hssfCellStyleMoneyTitle);

        Row row11 = sheet.createRow(11);
        setStyle(sheet, row11,  CELL_STYLE.DIV_11, CELL_STYLE.DIV_11.title, cellStyleNoLine);

        Row row12 = sheet.createRow(12);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_NO_TITLE, CELL_STYLE.ITEM_NO_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_ITEM_TITLE, CELL_STYLE.ITEM_ITEM_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_QUANTITY_TITLE, CELL_STYLE.ITEM_QUANTITY_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_UNIT_TITLE, CELL_STYLE.ITEM_UNIT_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_UNIT_MONEY_TITLE, CELL_STYLE.ITEM_UNIT_MONEY_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_MONEY_TITLE, CELL_STYLE.ITEM_MONEY_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_TAX_MONEY_TITLE, CELL_STYLE.ITEM_TAX_MONEY_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_TOTAL_MONEY_TITLE, CELL_STYLE.ITEM_TOTAL_MONEY_TITLE.title, cellStyleBg, creationHelper);

        //for itemList
        ArrayList<ItemBeanTB> itemList = MyApplication.getInstance().getDBHelper().getEstimateItemListData(estimateBeanTB.getEst_idx());

        int no = 1;
        int i = 13;
        for(ItemBeanTB itemBeanTB : itemList){
            Row row = sheet.createRow(i);

            Cell cellItemNo = row.createCell(CELL_STYLE.ITEM_NO_TITLE.col);
            Cell cellItemName = row.createCell(CELL_STYLE.ITEM_ITEM_TITLE.col);
            Cell cellItemQuantity = row.createCell(CELL_STYLE.ITEM_QUANTITY_TITLE.col);
            Cell cellItemUnit = row.createCell(CELL_STYLE.ITEM_UNIT_TITLE.col);
            Cell cellItemUnitPrice = row.createCell(CELL_STYLE.ITEM_UNIT_MONEY_TITLE.col);
            Cell cellItemPrice = row.createCell(CELL_STYLE.ITEM_MONEY_TITLE.col);
            Cell cellItemTaxPrice = row.createCell(CELL_STYLE.ITEM_TAX_MONEY_TITLE.col);
            Cell cellItemTotalPrice = row.createCell(CELL_STYLE.ITEM_TOTAL_MONEY_TITLE.col);

            String moneyUnitPrice = "";
            String moneyPrice = "";
            String moneyTaxPrice = "";
            String moneyTotalPrice = "";
            try {
                moneyUnitPrice = FormatUtil.money(StringUtil.stringToLong(itemBeanTB.getItem_unit_price()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                moneyPrice = FormatUtil.money(StringUtil.stringToLong(itemBeanTB.getItem_price()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                moneyTaxPrice = FormatUtil.money(StringUtil.stringToLong(itemBeanTB.getItem_tax_price()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                moneyTotalPrice = FormatUtil.money(StringUtil.stringToLong(itemBeanTB.getItem_total_price()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            setStyleCreateCell(sheet, row, cellItemNo, CELL_STYLE.ITEM_NO_VALUE, StringUtil.intToString(no), cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemName, CELL_STYLE.ITEM_ITEM_VALUE, itemBeanTB.getItem_name(), cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemQuantity, CELL_STYLE.ITEM_QUANTITY_VALUE, itemBeanTB.getItem_quantity(), cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemUnit, CELL_STYLE.ITEM_UNIT_VALUE, itemBeanTB.getItem_unit(), cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemUnitPrice, CELL_STYLE.ITEM_UNIT_MONEY_VALUE, moneyUnitPrice, cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemPrice, CELL_STYLE.ITEM_MONEY_VALUE, moneyPrice, cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemTaxPrice, CELL_STYLE.ITEM_TAX_MONEY_VALUE, moneyTaxPrice, cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemTotalPrice, CELL_STYLE.ITEM_TOTAL_MONEY_VALUE, moneyTotalPrice, cellStyleValue);
            i++;
            no++;
        }

        //상품 빈 라인 채우기
        for(int k = i; k < 31; k++){
            Row row = sheet.createRow(k);
            Cell cellItemNo = row.createCell(CELL_STYLE.ITEM_NO_TITLE.col);
            Cell cellItemName = row.createCell(CELL_STYLE.ITEM_ITEM_TITLE.col);
            Cell cellItemQuantity = row.createCell(CELL_STYLE.ITEM_QUANTITY_TITLE.col);
            Cell cellItemUnit = row.createCell(CELL_STYLE.ITEM_UNIT_TITLE.col);
            Cell cellItemUnitPrice = row.createCell(CELL_STYLE.ITEM_UNIT_MONEY_TITLE.col);
            Cell cellItemPrice = row.createCell(CELL_STYLE.ITEM_MONEY_TITLE.col);
            Cell cellItemTaxPrice = row.createCell(CELL_STYLE.ITEM_TAX_MONEY_TITLE.col);
            Cell cellItemTotalPrice = row.createCell(CELL_STYLE.ITEM_TOTAL_MONEY_TITLE.col);

            setStyleCreateCell(sheet, row, cellItemNo, CELL_STYLE.ITEM_NO_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemName, CELL_STYLE.ITEM_ITEM_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemQuantity, CELL_STYLE.ITEM_QUANTITY_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemUnit, CELL_STYLE.ITEM_UNIT_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemUnitPrice, CELL_STYLE.ITEM_UNIT_MONEY_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemPrice, CELL_STYLE.ITEM_MONEY_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemTaxPrice, CELL_STYLE.ITEM_TAX_MONEY_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemTotalPrice, CELL_STYLE.ITEM_TOTAL_MONEY_VALUE, "", cellStyleValue);
        }

        Row row31 = sheet.createRow(31);
        setStyle(sheet, row31,  CELL_STYLE.DIV_31, CELL_STYLE.DIV_31.title, cellStyleNoLine);

        Row row32 = sheet.createRow(32);
        setStyleBg(sheet, row32,  CELL_STYLE.REMARK_TITLE, CELL_STYLE.REMARK_TITLE.title, cellStyleBg, creationHelper);

        Row row33 = sheet.createRow(33);
        setStyle(sheet, row33,  CELL_STYLE.REMARK_VALUE, estimateBeanTB.getEst_remark(), cellStyleRemark);

        //회사정보
        Row row37 = sheet.createRow(37);
        StringBuffer sb = new StringBuffer();
        sb.append(companyBeanTB.getCom_name());
        if(!StringUtil.isEmpty(companyBeanTB.getCom_tel_num())) {
            sb.append(" ");
            sb.append("Tel.");
            sb.append(FormatUtil.addHyphenToPhoneNumber(companyBeanTB.getCom_tel_num()));
        }

        if(!StringUtil.isEmpty(companyBeanTB.getCom_fax_num())) {
            sb.append(" ");
            sb.append("Fax.");
            sb.append(FormatUtil.addHyphenToPhoneNumber(companyBeanTB.getCom_fax_num()));
        }
        String companyInfo = sb.toString();
        setStyle(sheet, row37,  CELL_STYLE.BOTTOM, companyInfo, cellStyleValue);

        setPaperAligment(sheet);
        setColumeWidth(sheet, SHEET_TYPE.DETAIL);

        HSSFCellStyle hssfCellStyleLeftTop = mWorkbook.createCellStyle();
        hssfCellStyleLeftTop.setBorderLeft((short)1);
        hssfCellStyleLeftTop.setBorderTop((short)1);
        hssfCellStyleLeftTop.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeftTop.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeftTop.setFillPattern((short) 1);
        hssfCellStyleLeftTop.setAlignment(hAlignCenter);
        hssfCellStyleLeftTop.setVerticalAlignment(vAlignCenter);
        hssfCellStyleLeftTop.setFont(mFontNoramValue);

        HSSFCellStyle hssfCellStyleLeft = mWorkbook.createCellStyle();
        hssfCellStyleLeft.setBorderLeft((short)1);
        hssfCellStyleLeft.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeft.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeft.setFillPattern((short) 1);
        hssfCellStyleLeft.setAlignment(hAlignCenter);
        hssfCellStyleLeft.setVerticalAlignment(vAlignCenter);
        hssfCellStyleLeft.setFont(mFontNoramValue);

        HSSFCellStyle hssfCellStyleLeftBottom = mWorkbook.createCellStyle();
        hssfCellStyleLeftBottom.setBorderLeft((short)1);
        hssfCellStyleLeftBottom.setBorderBottom((short)1);
        hssfCellStyleLeftBottom.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeftBottom.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeftBottom.setFillPattern((short) 1);
        hssfCellStyleLeftBottom.setAlignment(hAlignCenter);
        hssfCellStyleLeftBottom.setVerticalAlignment(vAlignCenter);
        hssfCellStyleLeftBottom.setFont(mFontNoramValue);

        setBorderLine_top(sheet, hssfCellStyleLeftTop);
        setBorderLine(sheet, hssfCellStyleLeft);
        setBorderLine_bottom(sheet, hssfCellStyleLeftBottom);

        try {
            setPhoto(sheet, companyBeanTB);
        } catch (ExecutionException | InterruptedException | IOException e) {
            Log.d("EXCEL", "stampUri.toString() : " + e.toString());
        }

    }

    /**
     * 거래명세서 엑셀 데이타 주입 처리
     * createManholeExcel에서 호출
     * @param estimateBeanTB
     */
    private void setSpecificationExcel(EstimateBeanTB estimateBeanTB, CompanyBeanTB companyBeanTB, HSSFCellStyle cellStyleBg, HSSFCellStyle cellStyleValue, HSSFCellStyle cellStyleRemark, HSSFCellStyle cellStyleMainTitle, HSSFCellStyle cellStyleNoLine, CreationHelper creationHelper) {

        HSSFSheet sheet = mWorkbook.createSheet("거래명세서");

        //첫번째 행 >> 맨홀 조사 야장
        Row row0 = sheet.createRow(0);
        setMainTitleStyle(sheet,  row0, CELL_STYLE.MAIN_TITLE, "거래명세서", cellStyleMainTitle);

        Row row1 = sheet.createRow(1);

        setStyle(sheet, row1,  CELL_STYLE.CLIENT_NAME_VALUE, estimateBeanTB.getEst_cli_name(), cellStyleNoLine);
        setStyle(sheet, row1,  CELL_STYLE.CLIENT_NAME_TO, CELL_STYLE.CLIENT_NAME_TO.title, cellStyleNoLine);
        setStyleBg(sheet, row1,  CELL_STYLE.COMPANY_TITLE_01, CELL_STYLE.COMPANY_TITLE_01.title, cellStyleBg, creationHelper);

        setStyleBg(sheet, row1,  CELL_STYLE.COMPANY_BIZ_NUM_TITLE, CELL_STYLE.COMPANY_BIZ_NUM_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row1,  CELL_STYLE.COMPANY_BIZ_NUM_VALUE, FormatUtil.addHyphenToBusinessNumber(estimateBeanTB.getEst_com_biz_num()), cellStyleValue);

        Row row2 = sheet.createRow(2);
        setStyleBg(sheet, row2,  CELL_STYLE.COMPANY_NAME_TITLE, CELL_STYLE.COMPANY_NAME_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row2,  CELL_STYLE.COMPANY_NAME_VALUE, estimateBeanTB.getEst_com_name(), cellStyleValue);
        setStyle(sheet, row2,  CELL_STYLE.COMPANY_STEMP_VALUE, "", cellStyleValue);

        HSSFCellStyle companyNameStyle = createCellStyle();

        companyNameStyle.setAlignment(hAlignCenter);
        companyNameStyle.setVerticalAlignment(vAlignCenter);
        companyNameStyle.setBorderBottom((short) 1);
        companyNameStyle.setBorderTop((short) 1);
        companyNameStyle.setBorderLeft((short) 1);
        companyNameStyle.setBorderRight((short) 0);
        companyNameStyle.setFont(mFontNoramValue);

        HSSFCellStyle stempStyle = createCellStyle();
        stempStyle.setAlignment(hAlignCenter);
        stempStyle.setVerticalAlignment(vAlignCenter);
        stempStyle.setBorderBottom((short) 1);
        stempStyle.setBorderTop((short) 1);
        stempStyle.setBorderLeft((short) 0);
        stempStyle.setBorderRight((short) 1);

        setStyle(sheet, row2,  CELL_STYLE.COMPANY_NAME_VALUE, estimateBeanTB.getEst_com_name(), companyNameStyle);
        setStyle(sheet, row2,  CELL_STYLE.COMPANY_STEMP_VALUE, "", stempStyle);

        Row row3 = sheet.createRow(3);

        setStyle(sheet, row3,  CELL_STYLE.SPECIFICATION_DATE_TITLE, CELL_STYLE.SPECIFICATION_DATE_TITLE.title, cellStyleNoLine);
        setStyle(sheet, row3,  CELL_STYLE.ESTIMATE_DATE_VALUE, DateUtil.displayDateFormat(estimateBeanTB.getEst_date(), "yyyyMMdd", "yyyy.MM.dd"), cellStyleNoLine);

        setStyleBg(sheet, row3,  CELL_STYLE.COMPANY_TITLE_02, CELL_STYLE.COMPANY_TITLE_02.title, cellStyleBg, creationHelper);

        setStyleBg(sheet, row3,  CELL_STYLE.COMPANY_CEO_NAME_TITLE, CELL_STYLE.COMPANY_CEO_NAME_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row3,  CELL_STYLE.COMPANY_CEO_NAME_VALUE, estimateBeanTB.getEst_com_ceo_name(), cellStyleValue);

        //인수자, 회사 주소
        Row row4 = sheet.createRow(4);
        setStyle(sheet, row4,  CELL_STYLE.SPEC_MANAGER_NAME_TITLE, CELL_STYLE.SPEC_MANAGER_NAME_TITLE.title, cellStyleNoLine);
        setStyle(sheet, row4,  CELL_STYLE.MANAGER_NAME_VALUE, estimateBeanTB.getEst_com_manager_name(), cellStyleNoLine);

        setStyleBg(sheet, row4,  CELL_STYLE.COMPANY_ADDRESS_TITLE, CELL_STYLE.COMPANY_ADDRESS_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row4,  CELL_STYLE.COMPANY_ADDRESS_VALUE, estimateBeanTB.getEst_com_address_01() + " " + estimateBeanTB.getEst_com_address_02(), cellStyleValue);

        Row row5 = sheet.createRow(5);
        setStyleBg(sheet, row5,  CELL_STYLE.COMPANY_TITLE_03, CELL_STYLE.COMPANY_TITLE_03.title, cellStyleBg, creationHelper);

        setStyleBg(sheet, row5,  CELL_STYLE.COMPANY_UPTAE_TITLE, CELL_STYLE.COMPANY_UPTAE_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row5,  CELL_STYLE.COMPANY_UPTAE_VALUE, companyBeanTB.getCom_uptae(), cellStyleValue);
        setStyleBg(sheet, row5,  CELL_STYLE.COMPANY_UPJONG_TITLE, CELL_STYLE.COMPANY_UPJONG_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row5,  CELL_STYLE.COMPANY_UPJONG_VALUE, companyBeanTB.getCom_upjong(), cellStyleValue);

        Row row6 = sheet.createRow(6);
        setStyleBg(sheet, row6,  CELL_STYLE.COMPANY_TEL_TITLE, CELL_STYLE.COMPANY_TEL_TITLE.title, cellStyleBg, creationHelper);
        setStyle(sheet, row6,  CELL_STYLE.COMPANY_TEL_VALUE, FormatUtil.addHyphenToPhoneNumber(companyBeanTB.getCom_tel_num()), cellStyleValue);

        Row row7 = sheet.createRow(7);
        setStyle(sheet, row7,  CELL_STYLE.DIV_7, CELL_STYLE.DIV_7.title, cellStyleNoLine);

        Row row8 = sheet.createRow(8);
        String effectiveDate = StringUtil.isEmpty(estimateBeanTB.getEst_effective_date())?" ": " " + estimateBeanTB.getEst_effective_date() + "까지 유효합니다.";
        setStyle(sheet, row8,  CELL_STYLE.EFFECTIVE_DATE_TITLE, "하기와 같이 계산합니다." + effectiveDate, cellStyleNoLine);

        Row row9 = sheet.createRow(9);
        setStyle(sheet, row9,  CELL_STYLE.DIV_9, CELL_STYLE.DIV_9.title, cellStyleNoLine);

        //중간 금액 부가세포함 등 Row 처리

        Row row10 = sheet.createRow(10);
        String money = "";
        try {
            money = FormatUtil.money(estimateBeanTB.getEst_total_price());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        StringBuffer moneyTitle = new StringBuffer();
        moneyTitle.append(CELL_STYLE.MONEY_TITLE.title);
        moneyTitle.append("\n");
        moneyTitle.append("Y".equalsIgnoreCase(estimateBeanTB.getEst_tax_type())?"부가세포함":"부가세별도");

        setStyleBg(sheet, row10,  CELL_STYLE.MONEY_TITLE, moneyTitle.toString(), cellStyleBg, creationHelper);
        setStyle(sheet, row10,  CELL_STYLE.MONEY_KOR_VALUE, FormatUtil.convertNumberToKorean(StringUtil.stringToLong(estimateBeanTB.getEst_total_price())), cellStyleValue);

        Font fontReportMainTitle = mWorkbook.createFont();
        fontReportMainTitle.setBold(true);
        fontReportMainTitle.setFontHeight(TITLE_SIZE.PRICE_KOR.size);
        fontReportMainTitle.setFontName("맑은 고딕");

        HSSFCellStyle hssfCellStyleMoneyTitle = createCellStyle();
        hssfCellStyleMoneyTitle.setAlignment(hAlignCenter);
        hssfCellStyleMoneyTitle.setVerticalAlignment(vAlignCenter);
        hssfCellStyleMoneyTitle.setBorderTop((short)1);
        hssfCellStyleMoneyTitle.setBorderBottom((short)1);
        hssfCellStyleMoneyTitle.setBorderLeft((short)1);
        hssfCellStyleMoneyTitle.setBorderRight((short)1);
        hssfCellStyleMoneyTitle.setFont(fontReportMainTitle);

        setMoneyStyle(sheet, row10,  CELL_STYLE.MONEY_KOR_VALUE, "금액 " + FormatUtil.convertNumberToKorean(StringUtil.stringToLong(estimateBeanTB.getEst_total_price())) + " 원", hssfCellStyleMoneyTitle);

        setStyle(sheet, row10,  CELL_STYLE.MONEY_UNIT_VALUE, CELL_STYLE.MONEY_UNIT_VALUE.title, cellStyleValue);

        setMoneyStyle(sheet, row10,  CELL_STYLE.MONEY_NUM_VALUE, money, hssfCellStyleMoneyTitle);

        Row row11 = sheet.createRow(11);
        setStyle(sheet, row11,  CELL_STYLE.DIV_11, CELL_STYLE.DIV_11.title, cellStyleNoLine);

        Row row12 = sheet.createRow(12);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_NO_TITLE, CELL_STYLE.ITEM_NO_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_ITEM_TITLE, CELL_STYLE.ITEM_ITEM_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_QUANTITY_TITLE, CELL_STYLE.ITEM_QUANTITY_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_UNIT_TITLE, CELL_STYLE.ITEM_UNIT_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_UNIT_MONEY_TITLE, CELL_STYLE.ITEM_UNIT_MONEY_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_MONEY_TITLE, CELL_STYLE.ITEM_MONEY_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_TAX_MONEY_TITLE, CELL_STYLE.ITEM_TAX_MONEY_TITLE.title, cellStyleBg, creationHelper);
        setStyleBg(sheet, row12,  CELL_STYLE.ITEM_TOTAL_MONEY_TITLE, CELL_STYLE.ITEM_TOTAL_MONEY_TITLE.title, cellStyleBg, creationHelper);

        //for itemList
        ArrayList<ItemBeanTB> itemList = MyApplication.getInstance().getDBHelper().getEstimateItemListData(estimateBeanTB.getEst_idx());

        int no = 1;
        int i = 13;
        for(ItemBeanTB itemBeanTB : itemList){
            Row row = sheet.createRow(i);

            Cell cellItemNo = row.createCell(CELL_STYLE.ITEM_NO_TITLE.col);
            Cell cellItemName = row.createCell(CELL_STYLE.ITEM_ITEM_TITLE.col);
            Cell cellItemQuantity = row.createCell(CELL_STYLE.ITEM_QUANTITY_TITLE.col);
            Cell cellItemUnit = row.createCell(CELL_STYLE.ITEM_UNIT_TITLE.col);
            Cell cellItemUnitPrice = row.createCell(CELL_STYLE.ITEM_UNIT_MONEY_TITLE.col);
            Cell cellItemPrice = row.createCell(CELL_STYLE.ITEM_MONEY_TITLE.col);
            Cell cellItemTaxPrice = row.createCell(CELL_STYLE.ITEM_TAX_MONEY_TITLE.col);
            Cell cellItemTotalPrice = row.createCell(CELL_STYLE.ITEM_TOTAL_MONEY_TITLE.col);

            String moneyUnitPrice = "";
            String moneyPrice = "";
            String moneyTaxPrice = "";
            String moneyTotalPrice = "";
            try {
                moneyUnitPrice = FormatUtil.money(StringUtil.stringToLong(itemBeanTB.getItem_unit_price()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                moneyPrice = FormatUtil.money(StringUtil.stringToLong(itemBeanTB.getItem_price()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                moneyTaxPrice = FormatUtil.money(StringUtil.stringToLong(itemBeanTB.getItem_tax_price()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                moneyTotalPrice = FormatUtil.money(StringUtil.stringToLong(itemBeanTB.getItem_total_price()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            setStyleCreateCell(sheet, row, cellItemNo, CELL_STYLE.ITEM_NO_VALUE, StringUtil.intToString(no), cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemName, CELL_STYLE.ITEM_ITEM_VALUE, itemBeanTB.getItem_name(), cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemQuantity, CELL_STYLE.ITEM_QUANTITY_VALUE, itemBeanTB.getItem_quantity(), cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemUnit, CELL_STYLE.ITEM_UNIT_VALUE, itemBeanTB.getItem_unit(), cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemUnitPrice, CELL_STYLE.ITEM_UNIT_MONEY_VALUE, moneyUnitPrice, cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemPrice, CELL_STYLE.ITEM_MONEY_VALUE, moneyPrice, cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemTaxPrice, CELL_STYLE.ITEM_TAX_MONEY_VALUE, moneyTaxPrice, cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemTotalPrice, CELL_STYLE.ITEM_TOTAL_MONEY_VALUE, moneyTotalPrice, cellStyleValue);
            i++;
            no++;
        }

        //상품 빈 라인 채우기
        for(int k = i; k < 31; k++){
            Row row = sheet.createRow(k);
            Cell cellItemNo = row.createCell(CELL_STYLE.ITEM_NO_TITLE.col);
            Cell cellItemName = row.createCell(CELL_STYLE.ITEM_ITEM_TITLE.col);
            Cell cellItemQuantity = row.createCell(CELL_STYLE.ITEM_QUANTITY_TITLE.col);
            Cell cellItemUnit = row.createCell(CELL_STYLE.ITEM_UNIT_TITLE.col);
            Cell cellItemUnitPrice = row.createCell(CELL_STYLE.ITEM_UNIT_MONEY_TITLE.col);
            Cell cellItemPrice = row.createCell(CELL_STYLE.ITEM_MONEY_TITLE.col);
            Cell cellItemTaxPrice = row.createCell(CELL_STYLE.ITEM_TAX_MONEY_TITLE.col);
            Cell cellItemTotalPrice = row.createCell(CELL_STYLE.ITEM_TOTAL_MONEY_TITLE.col);

            setStyleCreateCell(sheet, row, cellItemNo, CELL_STYLE.ITEM_NO_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemName, CELL_STYLE.ITEM_ITEM_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemQuantity, CELL_STYLE.ITEM_QUANTITY_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemUnit, CELL_STYLE.ITEM_UNIT_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemUnitPrice, CELL_STYLE.ITEM_UNIT_MONEY_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemPrice, CELL_STYLE.ITEM_MONEY_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemTaxPrice, CELL_STYLE.ITEM_TAX_MONEY_VALUE, "", cellStyleValue);
            setStyleCreateCell(sheet, row, cellItemTotalPrice, CELL_STYLE.ITEM_TOTAL_MONEY_VALUE, "", cellStyleValue);
        }

        Row row31 = sheet.createRow(31);
        setStyle(sheet, row31,  CELL_STYLE.DIV_31, CELL_STYLE.DIV_31.title, cellStyleNoLine);

        Row row32 = sheet.createRow(32);
        setStyleBg(sheet, row32,  CELL_STYLE.REMARK_TITLE, CELL_STYLE.REMARK_TITLE.title, cellStyleBg, creationHelper);

        Row row33 = sheet.createRow(33);
        setStyle(sheet, row33,  CELL_STYLE.REMARK_VALUE, estimateBeanTB.getEst_remark(), cellStyleRemark);

        //회사정보
        Row row37 = sheet.createRow(37);
        StringBuffer sb = new StringBuffer();
        sb.append(companyBeanTB.getCom_name());
        if(!StringUtil.isEmpty(companyBeanTB.getCom_tel_num())) {
            sb.append(" ");
            sb.append("Tel.");
            sb.append(FormatUtil.addHyphenToPhoneNumber(companyBeanTB.getCom_tel_num()));
        }

        if(!StringUtil.isEmpty(companyBeanTB.getCom_fax_num())) {
            sb.append(" ");
            sb.append("Fax.");
            sb.append(FormatUtil.addHyphenToPhoneNumber(companyBeanTB.getCom_fax_num()));
        }
        String companyInfo = sb.toString();
        setStyle(sheet, row37,  CELL_STYLE.BOTTOM, companyInfo, cellStyleValue);

        setPaperAligment(sheet);
        setColumeWidth(sheet, SHEET_TYPE.DETAIL);

        HSSFCellStyle hssfCellStyleLeftTop = mWorkbook.createCellStyle();
        hssfCellStyleLeftTop.setBorderLeft((short)1);
        hssfCellStyleLeftTop.setBorderTop((short)1);
        hssfCellStyleLeftTop.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeftTop.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeftTop.setFillPattern((short) 1);
        hssfCellStyleLeftTop.setAlignment(hAlignCenter);
        hssfCellStyleLeftTop.setVerticalAlignment(vAlignCenter);
        hssfCellStyleLeftTop.setFont(mFontNoramValue);

        HSSFCellStyle hssfCellStyleLeft = mWorkbook.createCellStyle();
        hssfCellStyleLeft.setBorderLeft((short)1);
        hssfCellStyleLeft.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeft.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeft.setFillPattern((short) 1);
        hssfCellStyleLeft.setAlignment(hAlignCenter);
        hssfCellStyleLeft.setVerticalAlignment(vAlignCenter);
        hssfCellStyleLeft.setFont(mFontNoramValue);

        HSSFCellStyle hssfCellStyleLeftBottom = mWorkbook.createCellStyle();
        hssfCellStyleLeftBottom.setBorderLeft((short)1);
        hssfCellStyleLeftBottom.setBorderBottom((short)1);
        hssfCellStyleLeftBottom.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeftBottom.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hssfCellStyleLeftBottom.setFillPattern((short) 1);
        hssfCellStyleLeftBottom.setAlignment(hAlignCenter);
        hssfCellStyleLeftBottom.setVerticalAlignment(vAlignCenter);
        hssfCellStyleLeftBottom.setFont(mFontNoramValue);

        setBorderLine_top(sheet, hssfCellStyleLeftTop);
        setBorderLine(sheet, hssfCellStyleLeft);
        setBorderLine_bottom(sheet, hssfCellStyleLeftBottom);

        try {
            setPhoto(sheet, companyBeanTB);
        } catch (ExecutionException | InterruptedException | IOException e) {
            Log.d("EXCEL", "stampUri.toString() : " + e.toString());
        }
    }


    private void setBorderLine_top(Sheet sheet, HSSFCellStyle hssfCellStyleLeft){

        for(int k = 1; k <= 2; k++) {
            Row iRow = sheet.getRow(k);
            if (iRow == null) {
                iRow = sheet.createRow(k);
            }

            Cell iCell01;
            try {
                iCell01 = iRow.getCell(CELL_STYLE.COMPANY_TITLE_01.col);
                if (iCell01 == null) {
                    iCell01 = iRow.createCell(CELL_STYLE.COMPANY_TITLE_01.col);
                }
            } catch (NullPointerException e) {
                iCell01 = iRow.createCell(CELL_STYLE.COMPANY_TITLE_01.col);
            }
            iCell01.setCellStyle(hssfCellStyleLeft);
        }
    }
    private void setBorderLine(Sheet sheet, HSSFCellStyle hssfCellStyleLeft){

        for(int k = 3; k <= 4; k++) {
            Row iRow = sheet.getRow(k);
            if (iRow == null) {
                iRow = sheet.createRow(k);
            }

            Cell iCell01;
            try {
                iCell01 = iRow.getCell(CELL_STYLE.COMPANY_TITLE_02.col);
                if (iCell01 == null) {
                    iCell01 = iRow.createCell(CELL_STYLE.COMPANY_TITLE_02.col);
                }
            } catch (NullPointerException e) {
                iCell01 = iRow.createCell(CELL_STYLE.COMPANY_TITLE_02.col);
            }
            iCell01.setCellStyle(hssfCellStyleLeft);
        }
    }
    private void setBorderLine_bottom(Sheet sheet, HSSFCellStyle hssfCellStyleLeft){

        for(int k = 5; k <= 6; k++) {
            Row iRow = sheet.getRow(k);
            if (iRow == null) {
                iRow = sheet.createRow(k);
            }

            Cell iCell01;
            try {
                iCell01 = iRow.getCell(CELL_STYLE.COMPANY_TITLE_03.col);
                if (iCell01 == null) {
                    iCell01 = iRow.createCell(CELL_STYLE.COMPANY_TITLE_03.col);
                }
            } catch (NullPointerException e) {
                iCell01 = iRow.createCell(CELL_STYLE.COMPANY_TITLE_03.col);
            }
            iCell01.setCellStyle(hssfCellStyleLeft);
        }
    }

    private void setPhoto(HSSFSheet sheet,  CompanyBeanTB companyBeanTB) throws ExecutionException, InterruptedException, IOException {

        CreationHelper creationHelper = mWorkbook.getCreationHelper();
        HSSFPatriarch hssfPatriarch = sheet.createDrawingPatriarch();

        ClientAnchor clientAnchorStamp = creationHelper.createClientAnchor();
        ClientAnchor clientAnchorLogo = creationHelper.createClientAnchor();

        if(!StringUtil.isEmpty(companyBeanTB.getCom_stamp_path())) {
            Uri stampUri = Uri.parse("content://media" + companyBeanTB.getCom_stamp_path());
            Log.d("EXCEL", "stampPath : " + stampUri);
            Log.d("EXCEL", "stampUri.toString() : " + stampUri.toString());
            String filePath = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                filePath = ImageUtil.getRealPathFromURI(mActivity, stampUri);
                Log.d("EXCEL", "real filePath : " + filePath);
                try (FileInputStream imageStream = new FileInputStream(filePath)) {
                    byte[] imageBytes = IOUtils.toByteArray(imageStream);
                    int stempIndex = mWorkbook.addPicture(imageBytes, HSSFWorkbook.PICTURE_TYPE_PNG);
                    HSSFClientAnchor anchor = new HSSFClientAnchor(250, 0, 750, 0, (short) CELL_STYLE.COMPANY_STEMP_VALUE.firstCol, (short) CELL_STYLE.COMPANY_STEMP_VALUE.firstRow, (short) (CELL_STYLE.COMPANY_STEMP_VALUE.lastCol), (short) (CELL_STYLE.COMPANY_STEMP_VALUE.lastRow + 1));
                    hssfPatriarch.createPicture(anchor, stempIndex);
                }
            }

            /*
            Glide.with(mActivity).asBitmap()
                    .load(stampUri)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap glideBitmap,
                                                    @Nullable Transition<? super Bitmap> transition) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            glideBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                            byte[] byteData = baos.toByteArray();
                            int iPictureStamp = mWorkbook.addPicture(byteData, HSSFWorkbook.PICTURE_TYPE_JPEG);
                            Log.d("EXCEL", "stampPath >> iPictureStamp : " + iPictureStamp);
                            hssfPatriarch.createPicture(clientAnchorStamp, iPictureStamp);
                            try {
                                baos.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
             */
        }


        /*
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!StringUtil.isEmpty(companyBeanTB.getCom_logo_path())) {
                    String logoPath = "content://media" + companyBeanTB.getCom_logo_path();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            byte[] bytesOuter = new byte[0];
                            try {
                                bytesOuter = Glide.with(mActivity)
                                        .as(byte[].class)
                                        .load(logoPath)
                                        .submit()
                                        .get();
                            } catch (ExecutionException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            pictureLogo[0] = mWorkbook.addPicture(bytesOuter, HSSFWorkbook.PICTURE_TYPE_JPEG);
                        }
                    });
                }
            };
        });
        */

        /*
        // 하단에 회사 로고 이미지 입력.
        if(!StringUtil.isEmpty(companyBeanTB.getCom_logo_path())) {
            Uri logoUri = Uri.parse("content://media" + companyBeanTB.getCom_logo_path());
            Log.d("EXCEL", "logoUri : " + logoUri);
            Log.d("EXCEL", "logoUri.toString() : " + logoUri.toString());
            String filePath = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                filePath = ImageUtil.getRealPathFromURI(mActivity, logoUri);
                Log.d("EXCEL", "real filePath : " + filePath);
                try (FileInputStream imageStream = new FileInputStream(filePath)) {
                    byte[] imageBytes = IOUtils.toByteArray(imageStream);
                    int logoIndex = mWorkbook.addPicture(imageBytes, HSSFWorkbook.PICTURE_TYPE_PNG);
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) CELL_STYLE.BOTTOM.firstCol, (short) CELL_STYLE.BOTTOM.firstRow, (short) (CELL_STYLE.BOTTOM.lastCol + 1), (short) (CELL_STYLE.BOTTOM.lastRow + 1));
                    hssfPatriarch.createPicture(anchor, logoIndex);
                }
            }
        }
         */

        //clientAnchorStamp.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
        //clientAnchorLogo.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);

    }


    /**
     *
     * @param estimateBeanTB
     * @param saveExcelType 견젹서(estimate), 거래명세서(specification)
     */
    private void saveExcel(EstimateBeanTB estimateBeanTB, String saveExcelType){
        FileOutputStream fileOut = null;

        //File file = FileUtil.createFile(mActivity, Environment.DIRECTORY_DOCUMENTS, "excel", ".xls");
        String prefix = "";
        prefix = estimateBeanTB.getEst_cli_name();
        prefix = prefix.replace(" ", "").trim();

        File file = FileUtil.createExcelFile(mActivity, saveExcelType, prefix, Environment.DIRECTORY_DOCUMENTS, "excel", ".xls");

        try {
            fileOut = new FileOutputStream(file);
            mWorkbook.write(fileOut);
        }catch (IOException e){
            GomsLog.d(LOG_TAG, e.toString());
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                    mWorkbook.close();

                    MediaScannerConnection.scanFile(mActivity,
                            new String[]{file.getAbsolutePath()},
                            null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    // Handle scan completion, if needed
                                    GomsLog.d(LOG_TAG, "path : " + path);
                                    mExcelInterface.onComplete(path, uri);
                                }
                            });

                } catch (IOException e) {
                    GomsLog.d(LOG_TAG, e.toString());
                }
            }
        }
    }



    ExcelInterface mExcelInterface;

    public ExcelInterface getExcelInterface() {
        return mExcelInterface;
    }

    public void setExcelInterface(ExcelInterface mExcelInterface) {
        this.mExcelInterface = mExcelInterface;
    }

    public interface ExcelInterface{
        void onComplete(String path, Uri uri);
    }
}
