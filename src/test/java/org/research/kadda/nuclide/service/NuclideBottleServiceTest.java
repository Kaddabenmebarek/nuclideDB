package org.research.kadda.nuclide.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.models.TracerHierarchy;
import org.research.kadda.nuclide.models.TracerOverview;
import org.research.kadda.nuclide.service.NuclideBottleService;
import org.research.kadda.nuclide.service.NuclideUsageService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.io.Files;

public class NuclideBottleServiceTest {

	private static ClassPathXmlApplicationContext context;
	private static NuclideBottleService nuclideBottleService;
	private static NuclideUsageService nuclideUsageService;
	private static final String CURRENT_ACTIVITY_TRACER_QUERY = "SELECT b.activity/EXP(LN(2.0)*(TRUNC(SYSDATE)-TRUNC(b.activity_date))/n.half_time) FROM osiris.nuclide n, osiris.nuclide_bottle b WHERE b.nuclide_name = n.nuclide_name AND b.nuclide_bottle_id = ";
	private final static String ANY = "any";
	private final static int TRACER = 4106;
	private static final String CHROME_DRIVER_PATH = "C:\\Program Files\\chromedriver_win32\\chromedriver.exe";
	private static final String NUCLIDEDB_URL = "https://apollo/nuclideDB";
	private static final String DOWNLOADED_EXCEL_FILE = "C:/Users/benmeka1/Downloads/Nuclide DatabaseList of Tracers.xlsx";
	private static final String INPUT_EXCEL_FILE_TRACERS = "C:/test-nuclide/ares_tracers-export.xlsx";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:test-context.xml");
		nuclideBottleService = (NuclideBottleService) context.getBean("nuclideBottleService");
		nuclideUsageService = (NuclideUsageService) context.getBean("nuclideUsageService");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		context.close();
	}

	@Test
	public void testFindAll() {
		List<NuclideBottle> allNuclideBottle = nuclideBottleService.findAll();
		assertNotNull(allNuclideBottle);
		assertTrue(allNuclideBottle.size() > 0);
	}

	@Test
	public void testFindById() {
		NuclideBottle nuclideBottle = nuclideBottleService.findById(1);
		assertNotNull(nuclideBottle);
		assertTrue(nuclideBottle.getNuclideBottleId() == 1);
	}

	@Test
	public void testGetCurrentTracerActivity() {
		//4106
		BigDecimal referenceCurrentTracerActivity = ((BigDecimal) nuclideBottleService.getTargetedObject(CURRENT_ACTIVITY_TRACER_QUERY + TRACER)).setScale(0, RoundingMode.HALF_UP);
		Integer testedTracerActivity = null;
		//used to display list tracers
		List<TracerOverview> tracers = nuclideBottleService.findNuclideBottleByParam(ANY, ANY, ANY, ANY, ANY, ANY, ANY,null);
		for(TracerOverview tracer : tracers) {
			if(tracer.getTracerId() == TRACER) {
				testedTracerActivity = Integer.valueOf(tracer.getCurrentActivity());
			}
		}
		assertTrue(referenceCurrentTracerActivity.intValue() == testedTracerActivity.intValue());
		//used to display tracer details
		NuclideBottle nuclideBottle = nuclideBottleService.findById(TRACER);
		Double solideWastePercentage = nuclideUsageService.getSolidWastePercentage(nuclideBottle.getNuclideBottleId());
		Double currentAmount =  nuclideBottle.getVolume() - (nuclideBottle.getVolume() * (solideWastePercentage/100));
		Double initialAmount = new BigDecimal(nuclideBottle.getVolume()).setScale(0, RoundingMode.HALF_UP).doubleValue();
		BigDecimal currentActivityToCheck =  new BigDecimal((nuclideBottle.getCurrentActivity() * currentAmount) / initialAmount);
		assertTrue(referenceCurrentTracerActivity.intValue() == currentActivityToCheck.intValue());
	}	
	
	
	@Test
	public void compareExportedDataForTracersActivity() throws IOException, InvalidFormatException {
		//excel export automation
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
		WebDriver wd = new ChromeDriver();
		wd.get(NUCLIDEDB_URL + "/login");
		wd.findElement(By.id("form_userId")).sendKeys("benmeka1");
		EncryptPwd encryptPwd = new EncryptPwd("mypwd");
		wd.findElement(By.id("form_userPassword")).sendKeys(encryptPwd.decrypt("95BD3609314BF8391226BD132CCB0F8B"));		
		wd.findElement(By.name("Submit")).click();
		wd.get(NUCLIDEDB_URL + "/showTracers");
		WebElement select = wd.findElement(By.id("discardedStatus"));
		Select dropDown = new Select(select);
		List<WebElement> options = dropDown.getOptions();
		for(WebElement option : options) {
			if(option.getText().equals("Show all tubes")) {
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
		//remove files
		File previousOpenFile = new File(INPUT_EXCEL_FILE_TRACERS);
		if(previousOpenFile.exists()) {
			previousOpenFile.delete();
		}
		File src = new File(DOWNLOADED_EXCEL_FILE);
		File dest = new File(INPUT_EXCEL_FILE_TRACERS);
		Files.move(src, dest);
		wd.quit();
		
		Map<String, Integer> aresOpenWastesActivityMap = getAresTracersActivityMap(INPUT_EXCEL_FILE_TRACERS);
		Map<String, Integer> implementationTracersActivityMap = getImplementationTracersActivityMap();
		for(Entry<String, Integer> entry : aresOpenWastesActivityMap.entrySet()) {
			if(implementationTracersActivityMap.get(entry.getKey()) != null){				
				int exportedActivityValue = entry.getValue();
				int implementedActivityValue = implementationTracersActivityMap.get(entry.getKey()); 
				if(exportedActivityValue != implementedActivityValue) {
					System.out.println(entry.getKey() + ": " + exportedActivityValue + " / " + implementedActivityValue);
				}
				assertTrue(exportedActivityValue == implementedActivityValue);
			}
		}
	}

	private Map<String, Integer> getAresTracersActivityMap(String path) throws IOException, InvalidFormatException {
		Map<String, Integer> aresTracersActivityMap = new HashMap<String, Integer>();
		Workbook wb = WorkbookFactory.create(new File(path)); 
		assertTrue(wb.getNumberOfSheets() > 0);
		Sheet sheet = wb.getSheetAt(0);
		DataFormatter df = new DataFormatter();
		for(Row row : sheet) {
			if(row.getRowNum()>0) {				
				String tracerId = df.formatCellValue(row.getCell(0));
				String tracerCurrentActivity = df.formatCellValue(row.getCell(5));
				aresTracersActivityMap.put(tracerId, Integer.valueOf(tracerCurrentActivity));
			}
		}
		return aresTracersActivityMap;
	}
	
	private Map<String, Integer> getImplementationTracersActivityMap() {
		Map<String, Integer> implementationTracersActivityMap = new HashMap<String, Integer>();
		List<TracerOverview> tracers = nuclideBottleService.findNuclideBottleByParam(ANY, ANY, ANY, ANY, ANY, ANY, ANY,null);
		//compare only 300 not need to compare more
		for(int i=0; i<300; i++) {
			int tracerId = tracers.get(i).getTracerId();
			String currentActivity = tracers.get(i).getCurrentActivity();
			if(tracerId != 0) {				
				implementationTracersActivityMap.put(String.valueOf(tracerId), Integer.valueOf(currentActivity));			
			}
		}
		return implementationTracersActivityMap;
	}
	
	@Test
	public void testGetTracerHierarchyList() {
		List<TracerOverview> originalTracers = nuclideBottleService.findNuclideBottleByParam(null, null, null, "N", null, "original", null, null);
		List<TracerHierarchy> thl = nuclideBottleService.getTracerHierarchyList("14C",originalTracers);
		assertNotNull(thl);
		assertTrue(thl.size() > 0);
		
		for(TracerHierarchy th:thl) {
			int level = 0;
			level = getLevel(Integer.valueOf(th.getNuclideId()), level);
			th.setLevel(level);
		}
		
		Collections.sort(thl, new Comparator<TracerHierarchy>() {
			@Override
			public int compare(TracerHierarchy th1, TracerHierarchy th2) {
				return Integer.compare(th1.getLevel(), th2.getLevel());
			}
		});
		assertNotNull(thl.get(0).getLevel());
	}
	
	private int getLevel(int id, int level) {
		Integer parentId = nuclideUsageService.getParent(id);
		if(parentId != null) {			
			++level;
			getLevel(parentId, level);
		}
		return level;
	}
	
	/*@Test
	public void testSaveNuclideBottle() {
		NuclideBottle nuclideBottleToSave = new NuclideBottle();
		nuclideBottleToSave.setNuclideBottleId(10000);
		nuclideBottleToSave.setNuclide(new Nuclide("3H"));
		nuclideBottleToSave.setSubstanceName("testSubstance");
		nuclideBottleToSave.setActivity(50.0);
		nuclideBottleToSave.setVolume(30.0);
		nuclideBottleToSave.setLocation("testLocation");
		nuclideBottleToSave.setNuclideUserByNuclideUserId(new NuclideUser("BENMEKA1", "Kadda", "Benmebarek", 'Y'));
		nuclideBottleToSave.setActivityDate(new Date());
		nuclideBottleToSave.setIsLiquid('Y');
		nuclideBottleService.saveNuclideBottle(nuclideBottleToSave);
		assertNotNull(nuclideBottleService.findById(10000));		
		nuclideBottleService.deleteNuclideBottle(nuclideBottleToSave);
	}*/

}
