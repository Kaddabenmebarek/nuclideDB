package org.research.kadda.nuclide.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.entity.NuclideWaste;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import org.research.kadda.osiris.OsirisService;
import org.research.kadda.osiris.data.EmployeeDto;


@Component
public class EmailServiceImpl implements EmailService {

	private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	private final static String EMAIL_TEMPLATE = "emailTemplate.html"; 
	
	@Autowired
	private HttpServletRequest request;
	
    @Override
    public void sendJavaMail(String message, String recipe, List<String> ccs,String subject, boolean isTest)
			throws AddressException, MessagingException, IOException {

    	Properties emailProp = fetchProperties();
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", emailProp.getProperty("mail.host"));
		props.setProperty("mail.smtp.starttls.enable", emailProp.getProperty("mail.smtp.starttls.enable"));

		Session mailConnection = Session.getInstance(props, null);
		mailConnection.setDebug(false);
		Message msg = new MimeMessage(mailConnection);
		msg.setSentDate(new Date());

		Address recip;
		if(isTest) {
			recip = new InternetAddress(emailProp.getProperty("mail.to"));
			Address recipcc = new InternetAddress(emailProp.getProperty("mail.cc"));
			msg.setRecipient(Message.RecipientType.CC, recipcc);
		}else {
			recip = recipe != null ? new InternetAddress(recipe) : new InternetAddress(emailProp.getProperty("mail.to"));	
		}
		msg.setRecipient(Message.RecipientType.TO, recip);
		
		Address from = new InternetAddress(emailProp.getProperty("mail.from"));
		msg.setFrom(from);
		String purpose = subject == null ? emailProp.getProperty("mail.subject") : subject;
		msg.setSubject(purpose);

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		
//		MimeBodyPart attachPart = new MimeBodyPart();
//		attachPart.attachFile("claspath:/nuclideDB/resources/img/banner.png");
//		multipart.addBodyPart(attachPart);

		msg.setContent(multipart);
		Transport.send(msg);

	}
	
	@Override
	public void sendSimpleMessage(String msg) {
		Properties emailProp = fetchProperties();
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailProp.getProperty("mail.to"));
		message.setCc(emailProp.getProperty("mail.cc"));
		message.setFrom(emailProp.getProperty("mail.from"));
		message.setSubject(emailProp.getProperty("mail.subject"));
		message.setText(msg);
		getJavaMailSender().send(message);
	}
	
	public JavaMailSender getJavaMailSender() {
		
		Properties emailProp = fetchProperties();
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    
	    mailSender.setHost(emailProp.getProperty("mail.host"));
	    mailSender.setPort(Integer.valueOf(emailProp.getProperty("mail.port")));
	    mailSender.setUsername(emailProp.getProperty("mail.username"));
	    mailSender.setPassword(emailProp.getProperty("mail.password"));
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.smtp.auth", emailProp.getProperty("mail.smtp.auth"));
	    props.put("mail.smtp.starttls.enable", emailProp.getProperty("mail.smtp.starttls.enable"));
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	
	
    public Properties fetchProperties(){
        Properties properties = new Properties();
        try {
        	File file;
        	if(request.getRequestURL() != null && (request.getRequestURL().toString().contains("ares"))) {
        		file = ResourceUtils.getFile("classpath:email-prod.properties");
        	}else {
        		file = ResourceUtils.getFile("classpath:email-test.properties");
        	}
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
        	logger.error(e.getMessage());
        }
        return properties;
    }
    
    @Override
	public void processTenPercentReached(NuclideBottle nb, NuclideBottle parentTracer, StringBuilder sb, boolean testMode) throws AddressException, MessagingException, IOException {
		String parentTracerUnit = nb.getIsLiquid() == 'Y' ? " ul" : " mg";
		sb.append("<br /><font color=\"red\"><b> WARNING: The remaining amount of the tracer ")
		.append(parentTracer.getNuclideBottleId()).append(" is equal or less than 10% of the initial amount (")
		.append(parentTracer.getVolume()).append(parentTracerUnit).append(").")
		.append("</b></font>");
		// send email to owner of the parent tracer
		StringBuilder msg = new StringBuilder("The remaining amount of the tracer ")
		.append(parentTracer.getNuclideBottleId()).append(" is equal or less than 10% of the initial amount.")		
		.append("<br />");
		msg.append("Original amount: <b>").append(parentTracer.getVolume()).append(parentTracerUnit).append(".</b>").append("<br />");
		BigDecimal volumeUsed = parentTracer.getSumVolume() != null ? parentTracer.getSumVolume() : new BigDecimal(0);
		Double amountLeft = parentTracer.getVolume() - volumeUsed.doubleValue();
		int availableAmount = amountLeft < 0 ? 0 : amountLeft.intValue();
		msg.append("Current amount: <b>").append(availableAmount).append(parentTracerUnit).append(".</b>").append("<br />");
		NuclideUser parentTracerOwner = parentTracer.getNuclideUserByNuclideUserId();
		EmployeeDto employee = OsirisService.getEmployeeByUserId(parentTracerOwner.getUserId().toLowerCase());
		String dest = employee.getEmail();
		String mailContent = "";
		try {
			mailContent = generateContent(EMAIL_TEMPLATE, msg.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendJavaMail(mailContent, dest, null, "Tracer amount reached 90% of use",testMode);
	}
    
    @Override
	public void processRadiactivityLimitEmail(NuclideBottle newNuclideBottle, Double tracerSum, Double allowance, boolean testMode) throws AddressException, MessagingException, IOException {
		StringBuilder msg = new StringBuilder("This message was sent automatically by NuclideDB system.").append("<br />");
		msg.append("The radioactivity limit for the lab <a href='https://ares/nuclideDB/listLabs' target='_blank'>").append(newNuclideBottle.getLocation()).append("</a> has been exceeded.").append("<br />");
		msg.append("The combined ").append(newNuclideBottle.getNuclide().getNuclideName()).append(" activity of tracers (<b>").append(tracerSum.intValue()).append(" kBq</b>) and waste exceeds the allowed limit<b>").append("<br /><br />");
		String mailContent = "";
		try {
			mailContent = generateContent(EMAIL_TEMPLATE, msg.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendJavaMail(mailContent,null,null,null,testMode);
	}
    
    @Override
	public void process32PUsageEmail(String user, String healAndSafetyExpertEmail, String subject, boolean testMode) throws AddressException, MessagingException, IOException {
		StringBuilder msg = new StringBuilder("This message was sent automatically by NuclideDB system.").append("<br />");
		msg.append("The user ").append(user).append(" has just open a new tracer for P-32.").append("<br />");
		msg.append("Please order a new dosimeter and contact the user.").append("<br /><br />");
		String mailContent = "";
		try {
			mailContent = generateContent(EMAIL_TEMPLATE, msg.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendJavaMail(mailContent, healAndSafetyExpertEmail, null, subject, testMode);
	}
    
	@Override
	public void processWasteRadiactivityLimitEmail(NuclideWaste waste, Double wasteActivity, Double allowance, String healAndSafetyExpertEmail, String subject, String userId, boolean testMode) throws AddressException, MessagingException, IOException {
		StringBuilder msg = new StringBuilder("This message was sent automatically by NuclideDB system.").append("<br />");
		msg.append("The radioactivity for the waste <a href='https://ares/nuclideDB/wasteDetail_"+ waste.getNuclideWasteId() +"' target='_blank'>").append(waste.getNuclideWasteId()).append("</a> has reached 50% of the allowed limit.").append("<br />");
		msg.append("The combined activity of tracers inside the waste is (<b>").append(wasteActivity.intValue()).append(" kBq</b>).").append("<br /><br />");
		String mailContent = "";
		try {
			mailContent = generateContent(EMAIL_TEMPLATE, msg.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		EmployeeDto employee = OsirisService.getEmployeeByUserId(userId.toLowerCase());
		List<String> ccs = new ArrayList<String>();
		ccs.add(employee.getEmail());
		sendJavaMail(mailContent, healAndSafetyExpertEmail, ccs, subject, testMode);
	}
	
	public String generateContent(String inputfileName, String mailContent) throws FileNotFoundException, IOException {
		File sourceFile = ResourceUtils.getFile("classpath:" + inputfileName);
		StringBuilder sb = new StringBuilder();
		FileReader fr = new FileReader(sourceFile);
		BufferedReader br = new BufferedReader(fr);
		String s = br.readLine();
		try {
			while (s != null) {
				System.out.println(s);
				if(s.equals("{content}")) {
					s = mailContent;
				}
				sb.append(s);
				s = br.readLine();
			}
		} finally {
			br.close();
		}
		return sb.toString();
	}

}
