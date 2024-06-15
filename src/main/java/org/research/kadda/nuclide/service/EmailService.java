package org.research.kadda.nuclide.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.entity.NuclideWaste;

public interface EmailService {

	void sendSimpleMessage(String message);
	
	void sendJavaMail(String message, String recipe, List<String> ccs, String subject, boolean isTest) throws AddressException, MessagingException, IOException;

	void processTenPercentReached(NuclideBottle nb, NuclideBottle parentTracer, StringBuilder sb, boolean testMode) throws AddressException, MessagingException, IOException;

	void processRadiactivityLimitEmail(NuclideBottle newNuclideBottle, Double tracerSum, Double allowance, boolean testMode) throws AddressException, MessagingException, IOException;

	void process32PUsageEmail(String user, String healAndSafetyExpertEmail, String subject, boolean testMode) throws AddressException, MessagingException, IOException;

	void processWasteRadiactivityLimitEmail(NuclideWaste waste, Double wasteActivity, Double allowance, String healAndSafetyExpertEmail, String subject,
			String userId, boolean testMode) throws AddressException, MessagingException, IOException;
	
}
