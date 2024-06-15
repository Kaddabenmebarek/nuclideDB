package org.research.kadda.nuclide.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.research.kadda.nuclide.entity.NuclideWaste;
import org.research.kadda.nuclide.models.WasteOverview;
import org.research.kadda.nuclide.service.NuclideUsageService;
import org.research.kadda.nuclide.service.NuclideWasteService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.io.Files;

public class NuclideWasteServiceTest {

	private static ClassPathXmlApplicationContext context;
	private static NuclideWasteService nuclideWasteService;
	private static NuclideUsageService nuclideUsageService;
	private static final String ACTIVITY_WASTE_QUERY = "SELECT SUM(b.activity/b.volume*u.volume*u.liquid_waste_percentage/100/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time)) FROM osiris.nuclide n, osiris.nuclide_usage u, osiris.nuclide_bottle b WHERE u.nuclide_bottle_id=b.nuclide_bottle_id AND b.nuclide_name=n.nuclide_name AND u.liquid_waste_id=";
	private static final String CHROME_DRIVER_PATH = "C:\\Program Files\\chromedriver_win32\\chromedriver.exe";
	private static final String NUCLIDEDB_URL = "https://apollo/nuclideDB";
	private static final String DOWNLOADED_EXCEL_FILE = "C:/Users/benmeka1/Downloads/Nuclide DatabaseList of Wastes.xlsx";
	private static final String INPUT_EXCEL_FILE_OPEN_WASTES = "C:/test-nuclide/ares_open-wastes-export.xlsx";
	private static final String INPUT_EXCEL_FILE_DISPOSED_WASTES = "C:/test-nuclide/ares_discarded-wastes-export.xlsx";
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:test-context.xml");
		nuclideWasteService = (NuclideWasteService) context.getBean("nuclideWasteService");	
		nuclideUsageService = (NuclideUsageService) context.getBean("nuclideUsageService");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		context.close();
	}

	@Test
	public void testFindAll() {
		List<NuclideWaste> allWastes = nuclideWasteService.findAll();
		assertNotNull(allWastes);
		assertTrue(CollectionUtils.isNotEmpty(allWastes));
	}

	@Test
	public void testfindWasteByParams() {
		List<WasteOverview> wasteOverviewList = nuclideWasteService.findWasteByParams("125I", "2017", "Y");
		assertNotNull(wasteOverviewList);
		assertTrue(CollectionUtils.isNotEmpty(wasteOverviewList));		
	}
	
	@Test
	public void testGetMaxSumActivityVolume(){
		Map<Double,Double> maxSumMap = nuclideWasteService.getMaxSumActivityVolume(true, 1);
		assertNotNull(maxSumMap);
		assertTrue(MapUtils.isNotEmpty(maxSumMap));
	}
	
	@Test
	public void testGetCurrentActivity() {
		Double referenceWasteActivity = ((BigDecimal) nuclideWasteService.getTargetedObject(ACTIVITY_WASTE_QUERY + 2533)).doubleValue();
		Double testedWasteActivity = null;
		Map<String, String> currentActivityKBqLEMap = nuclideUsageService.getCurrentActivityKBqLEMap('Y', 2533);
		String twa = currentActivityKBqLEMap.keySet().iterator().next();
		if(twa != null) testedWasteActivity = Double.valueOf(twa);
		assertTrue(referenceWasteActivity.intValue() == testedWasteActivity.intValue());
	}
	
	//Regression test
	/*@Test
	public void compareExportedDataForActivityWaste() throws EncryptedDocumentException, InvalidFormatException, IOException {
		
		//excel export automation
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
		WebDriver wd = new ChromeDriver();
		wd.get(NUCLIDEDB_URL + "/login");
		wd.findElement(By.id("form_userId")).sendKeys("benmeka1");
		EncryptPwd encryptPwd = new EncryptPwd("mypwd");
		wd.findElement(By.id("form_userPassword")).sendKeys(encryptPwd.decrypt("95BD3609314BF8391226BD132CCB0F8B"));
		wd.findElement(By.name("Submit")).click();
		wd.get(NUCLIDEDB_URL + "/showWaste");
		wd.findElement(By.name("Submit")).click();
		wd.findElement(By.className("buttons-excel")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		//remove files
		File previousOpenFile = new File(INPUT_EXCEL_FILE_OPEN_WASTES);
		if(previousOpenFile.exists()) {
			previousOpenFile.delete();
		}
		File src = new File(DOWNLOADED_EXCEL_FILE);
		File dest = new File(INPUT_EXCEL_FILE_OPEN_WASTES);
		Files.move(src, dest);
				
		wd.get(NUCLIDEDB_URL + "/showWaste");
		WebElement select = wd.findElement(By.id("closedOn"));
		Select dropDown = new Select(select);
		List<WebElement> options = dropDown.getOptions();
		for(WebElement option : options) {
			if(option.getText().equals("All discarded")) {
				option.click();
			}
		}		
		wd.findElement(By.name("Submit")).click();
		wd.findElement(By.className("buttons-excel")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		File previousDicardedFile = new File(INPUT_EXCEL_FILE_DISPOSED_WASTES);
		if(previousDicardedFile.exists()) {
			previousDicardedFile.delete();
		}
		src = new File(DOWNLOADED_EXCEL_FILE);
		dest = new File(INPUT_EXCEL_FILE_DISPOSED_WASTES);
		Files.move(src, dest);
		wd.quit();
		
		//open wastes
		Map<String, Integer> aresOpenWastesActivityMap = getAresWastesActivityMap(INPUT_EXCEL_FILE_OPEN_WASTES);
		Map<String, Integer> implementationOpenWastesActivityMap = getImplementationActivityMap("open");
		for(Entry<String, Integer> entry : aresOpenWastesActivityMap.entrySet()) {
			if(implementationOpenWastesActivityMap.get(entry.getKey()) != null){				
				int exportedActivityValue = entry.getValue();
				int implementedActivityValue = implementationOpenWastesActivityMap.get(entry.getKey()); 
				if(exportedActivityValue != implementedActivityValue) {
					System.out.println(entry.getKey() + ": " + exportedActivityValue + " / " + implementedActivityValue);
				}
				assertTrue(exportedActivityValue == implementedActivityValue);
			}
		}
		//disposed wastes
		Map<String, Integer> aresDisposeddWastesActivityMap = getAresWastesActivityMap(INPUT_EXCEL_FILE_DISPOSED_WASTES);
		Map<String, Integer> implementationDisposedWastesActivityMap = getImplementationActivityMap("discarded");
		for(Entry<String, Integer> entry : aresDisposeddWastesActivityMap.entrySet()) {
			if(implementationDisposedWastesActivityMap.get(entry.getKey()) != null) {				
				int exportedActivityValue = entry.getValue();
				int implementedActivityValue = implementationDisposedWastesActivityMap.get(entry.getKey()); 
				if(exportedActivityValue != implementedActivityValue) {
					System.out.println(exportedActivityValue + " / " + implementedActivityValue);
				}
				assertTrue(exportedActivityValue == implementedActivityValue);
			}
		}
	}
	
	private Map<String, Integer> getAresWastesActivityMap(String path) throws IOException, InvalidFormatException {
		Map<String, Integer> aresActivityMap = new HashMap<String, Integer>();
		Workbook wb = WorkbookFactory.create(new File(path)); 
		assertTrue(wb.getNumberOfSheets() > 0);
		Sheet sheet = wb.getSheetAt(0);
		DataFormatter df = new DataFormatter();
		for(Row row : sheet) {
			if(row.getRowNum()>0) {				
				String wasteId = df.formatCellValue(row.getCell(0));
				String wasteActivity = df.formatCellValue(row.getCell(3));
				aresActivityMap.put(wasteId, Integer.valueOf(wasteActivity));
			}
		}
		return aresActivityMap;
	}
	
	private Map<String, Integer> getImplementationActivityMap(String wasteStatus) {
		Map<String, Integer> implementationActivityMap = new HashMap<String, Integer>();
		List<WasteOverview> wastes = nuclideWasteService.findWasteByParams("any", wasteStatus, "any");
		for(WasteOverview nuclideWaste : wastes) {			
			Integer totalTracersInWasteActivity = 0;
			Map<String,String> currentActivityKBqLEMap = nuclideUsageService.getCurrentActivityKBqLEMap(nuclideWaste.getSolidLiquidState().charAt(0), nuclideWaste.getNuclideWasteId());
			for(Entry<String,String> entry : currentActivityKBqLEMap.entrySet()) {
				if(entry.getKey()!=null) {
					totalTracersInWasteActivity = Double.valueOf(entry.getKey()).intValue();
				}
				break;
			}
			implementationActivityMap.put(String.valueOf(nuclideWaste.getNuclideWasteId()), totalTracersInWasteActivity);
		}
		return implementationActivityMap;
	}*/

	
	/*@Test
	public void testSave() {	
		NuclideWaste nuclideWasteToSave = new NuclideWaste();   
		nuclideWasteToSave.setNuclideWasteId(10000);
		nuclideWasteToSave.setNuclide(new Nuclide("125I"));
		nuclideWasteToSave.setNuclideUserByCreationUserId(new NuclideUser("SCHAEUM"));
		nuclideWasteToSave.setIsLiquid('Y');
		nuclideWasteToSave.setLocation("Waste room H91.U2.N03");
	
		nuclideWasteService.save(nuclideWasteToSave);
		assertNotNull(nuclideWasteService.findWasteById(10000));
		nuclideWasteService.delete(nuclideWasteToSave);
	}*/


}
