package org.research.kadda.nuclide.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.research.kadda.nuclide.entity.Nuclide;
import org.research.kadda.nuclide.service.NuclideService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NuclideServiceTest {

	private static ClassPathXmlApplicationContext context;
	private static NuclideService nuclideService;
	private static final String RADIOACTIF_125I = "125I";
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:test-context.xml");
		nuclideService = (NuclideService) context.getBean("nuclideService");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		context.close();
	}

	@Test
	public void testFindAll() {
		List<Nuclide> allNuclide = nuclideService.findAll();
		assertNotNull(allNuclide);
		assertTrue(allNuclide.size() > 0);
		System.out.println(allNuclide);
	}

	@Test
	public void testFindByName() {
		Nuclide nuclide = nuclideService.findByName(RADIOACTIF_125I);
		assertNotNull(nuclide);
		assertEquals(nuclide.getNuclideName(), RADIOACTIF_125I);
		System.out.println(nuclide);
	}

	/*@Test
	public void testSave() {
		Nuclide nuclideToSave = new Nuclide("Test", 60.50, 50);
		nuclideService.save(nuclideToSave);
		assertEquals(nuclideService.findByName("Test").getNuclideName(), nuclideToSave.getNuclideName());
		nuclideService.delete(nuclideToSave);
	}*/

}
