package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scrappers.Scraper;
import scrappers.Search;

public class ExcelWrite {
	
	private Workbook workbook;
	private String path;
	private boolean SheetCreated=false;
	private Sheet sheet=null;
	private Row headerRow;
	public ExcelWrite(String filepath)
	{
		
		this.path=filepath;
		File file = new File(this.path);
				
		try {
			this.workbook = this.getWorkbook(filepath);
			file.createNewFile();			
			} catch (IOException e) {
			System.out.println("file path is not an excel file");
		}
	}

	/**
	 * Factory for xls and xlsx 
	 * @param type
	 * @return
	 * @throws IOException
	 */
	private Workbook getWorkbook(String excelFilePath)
	        throws IOException {
	    Workbook workbook = null;
	    if (excelFilePath.endsWith("xlsx")) {
	        workbook = new XSSFWorkbook();
	    } else if (excelFilePath.endsWith("xls")) {
	        workbook = new HSSFWorkbook();
	    } else {
	        throw new IllegalArgumentException("The specified file is not Excel file");
	    }
	 
	    return workbook;
	}
	
	public ExcelWrite createSheet(String name)
	{		
		this.sheet =this.workbook.createSheet(name);
		try (FileOutputStream outputStream = new FileOutputStream(this.path)) {
			workbook.write(outputStream); 
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return this ;
	}
	
	public ExcelWrite saveScrapingResult(Scraper result)
	{
		if(this.sheet == null)
			this.sheet = this.workbook.createSheet();
		//Creating header Row
		if(sheet.getRow(0) ==null)
			headerRow =sheet.createRow(0);
		else
			headerRow = sheet.getRow(0);
		//Header Row Style 
		
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
	    Font font = sheet.getWorkbook().createFont();
	    font.setBoldweight((short) 300);
	    font.setFontHeightInPoints((short) 14);
	    cellStyle.setFont(font);
		headerRow.setRowStyle(cellStyle);
		
		int lastcol=0;
		if(headerRow.getLastCellNum() != -1)
			lastcol=headerRow.getLastCellNum();
		System.out.println("index of the scraping result start"+lastcol);
		System.out.println("index of the scraping result shoud be"+this.sheet.getRow(0).getLastCellNum());
		// saving emails
		headerRow.createCell(lastcol+0).setCellValue("Emails");
		this.createCol(result.getEmails(),lastcol+0);
		
		
		// saving phones
		headerRow.createCell(lastcol+1).setCellValue("Phones");
		this.createCol(result.getPhones(),lastcol+1);
		
		// saving images
		headerRow.createCell(lastcol+2).setCellValue("Images");
		this.createCol(new HashSet<String>(result.getMedias().get("image")),lastcol+2);
		
		
		// saving video
		headerRow.createCell(lastcol+3).setCellValue("Videos");
		this.createCol(new HashSet<String>(result.getMedias().get("video")),lastcol+3);
		
		// saving audio
		headerRow.createCell(lastcol+4).setCellValue("Audios");
		this.createCol(new HashSet<String>(result.getMedias().get("audio")),4);
		
		// saving document
		headerRow.createCell(5).setCellValue("Document");
		this.createCol(new HashSet<String>(result.getMedias().get("document")),lastcol+5);
		
		// saving links
		headerRow.createCell(lastcol+6).setCellValue("Links");
		this.createCol(result.getInternalLinks(),lastcol+6);
		
		this.saveWorkbook();
			
			return this;
			
	}
	
	private void saveWorkbook() {

		try (FileOutputStream outputStream = new FileOutputStream(this.path)) {
	        workbook.write(outputStream);
	    } catch (FileNotFoundException e) {
	    	System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public ExcelWrite saveSearchResult(String url,String key)
	{
		
		if(this.sheet == null)		{
			this.sheet = this.workbook.createSheet();
		}
		//Header Row Style 
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
	    Font font = sheet.getWorkbook().createFont();
	    font.setBoldweight((short) 500);
	    font.setFontHeightInPoints((short) 14);
	    cellStyle.setFont(font);
		int startColIndex=0;
		
		if(this.sheet.getRow(0) != null)
		{
			headerRow = this.sheet.getRow(0);
			startColIndex = headerRow.getLastCellNum();
		}
		else
		{			
		 headerRow=this.sheet.createRow(startColIndex);
		}
		
		headerRow.createCell(startColIndex).setCellValue("mot clé : "+key);
		Set<String> searchResult =Search.source(url).find(key);
		
		System.out.println("inside save search result keyword ="+key+" "+Search.source(url).find(key));
		
		this.createCol(searchResult, startColIndex);
		this.saveWorkbook();
		headerRow.setRowStyle(cellStyle);
		return this;
	}
	
	private void createCol(Set<String> elements,int colIndex)
	{	
		int rowCount = 1;
		for(String value : elements)
		{
			Cell cell;
			if(this.sheet.getRow(rowCount)==null)
				cell =this.sheet.createRow(rowCount).createCell(colIndex);
			else
				cell = this.sheet.getRow(rowCount).createCell(colIndex);
			cell.setCellValue(value);
			rowCount++;
		}
		
	}
	
	
}
