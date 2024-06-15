package org.research.kadda.nuclide.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.service.NuclideUserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NuclideUserServiceTest {

	private static ClassPathXmlApplicationContext context;
	private static NuclideUserService nuclideUserService;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:test-context.xml");
		nuclideUserService = (NuclideUserService) context.getBean("nuclideUserService");	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		context.close();
	}

	@Test
	public void testFindAll() {
		List<NuclideUser> allUsers = nuclideUserService.findAll();
		assertNotNull(allUsers);
		assertTrue(allUsers.size() > 0);
		System.out.println(allUsers);
	}

	/*@Test
	public void testGetUserOverviewList() {
		List<UserOverview> userOverviewList = nuclideUserService.getUserOverviewList("3H", "CHOPINS", "last",'Y');
		assertNotNull(userOverviewList);
		assertTrue(userOverviewList.size() > 0);
		List<UserOverview> userOverviewLastUsageList = nuclideUserService.getUserOverviewList("3H", "CHOPINS", "all",'Y');
		assertNotNull(userOverviewLastUsageList);
		assertTrue(userOverviewLastUsageList.size() == 1);
	}*/


}
