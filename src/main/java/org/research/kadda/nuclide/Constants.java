package org.research.kadda.nuclide;

public class Constants {

    private static final boolean PROD = !"true".equals(System.getProperty("test")); //false; 
	public static final String DB_URL; 
	public static final String user ="nuclide_reader";
	public static final String passwd ="powdergun";
	
	static {

		if(PROD) {
    		DB_URL = "jdbc:oracle:thin:@hypnos:1521:act0";
    	} else {
    		DB_URL = "jdbc:oracle:thin:@hypnos-test:1521:testact0";
    	}    	
    	//System.out.println("NuclideDB started in " + (PROD?"PROD":"TEST"));
    	/*try {
    		DriverManager.registerDriver(new OracleDriver());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}*/
    }
    
    public static boolean isTestEnvironment() {
    	return !PROD;
    }

}
