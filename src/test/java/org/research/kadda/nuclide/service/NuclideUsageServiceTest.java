package org.research.kadda.nuclide.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.research.kadda.nuclide.models.UsageOverview;
import org.research.kadda.nuclide.service.NuclideUsageService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NuclideUsageServiceTest {

	private static ClassPathXmlApplicationContext context;
	private static NuclideUsageService nuclideUsageService;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:test-context.xml");
		nuclideUsageService = (NuclideUsageService) context.getBean("nuclideUsageService");	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		context.close();
	}

	/*@Test
	public void getCurrentActivityKBqLEMap() {
		Map<String, String> currentActivityKBqLEMap = nuclideUsageService.getCurrentActivityKBqLEMap('Y', 1);
		assertTrue(MapUtils.isNotEmpty(currentActivityKBqLEMap));
	}

		@Test
	public void getLastSeq() {
		int i = nuclideUsageService.getLastSeq();
		assertNotNull(i);
		assertTrue(i > 0);
	}*/
	
	@Test
	public void getCurrentActivityKBq() {
		List<UsageOverview> usageOverviewList = nuclideUsageService.getTracerUsageList(52, false);
		assertNotNull(usageOverviewList);
		assertTrue(usageOverviewList.size() > 0);
	}
}
