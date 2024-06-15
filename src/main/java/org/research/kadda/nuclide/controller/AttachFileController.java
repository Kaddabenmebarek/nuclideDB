package org.research.kadda.nuclide.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.research.kadda.nuclide.entity.NuclideAttached;
import org.research.kadda.nuclide.entity.NuclideBottle;
import org.research.kadda.nuclide.entity.NuclideUser;
import org.research.kadda.nuclide.models.TracerOverview;
import org.research.kadda.nuclide.service.NuclideAttachedService;
import org.research.kadda.nuclide.service.NuclideBottleService;
import org.research.kadda.nuclide.service.NuclideUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AttachFileController {

	@Autowired
	ServletContext context;

	@Autowired
	private NuclideAttachedService nuclideAttachedService;

	@Autowired
	private NuclideUserService nuclideUserService;

	@Autowired
	private NuclideBottleService nuclideBottleService;
	
	private final Logger logger = LoggerFactory.getLogger(AttachFileController.class);
	private final static String PATH_PREFIX = "resources/uploads";

	@RequestMapping(value = "/insertTracer", method = RequestMethod.GET)
	public ModelAndView getPage() {
		ModelAndView view = new ModelAndView("insertTracer");
		return view;
	}

	@RequestMapping(value = "/insertTracer", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> fileUpload(MultipartHttpServletRequest request,
			HttpServletResponse response) {
		Properties prop = fetchProperties(request);
		String path = prop.getProperty("files.path");
		Map<String, Object> mapToForward = new HashMap<String, Object>();
		Iterator<String> itr = request.getFileNames();
		String tracerId = request.getParameter("tracerId");
		String currentUserId = request.getParameter("currentUserId");
		MultipartFile multipartFile = null;
		NuclideUser user = nuclideUserService.findByUserId(currentUserId);
		NuclideBottle tracerSaved = nuclideBottleService.findById(Integer.valueOf(tracerId));
		if (itr.hasNext()) {
			new File(path + File.separator + tracerId).mkdir();
		}
		while (itr.hasNext()) {
			multipartFile = request.getFile(itr.next());
			String fileName = multipartFile.getOriginalFilename().replace(" ", "-");
			String file = "";
			try {
				// Files.createDirectories(Paths.get(PATH + File.separator + tracerId));
				file = path + File.separator + tracerId + File.separator
						+ fileName;
				// location = "C:"+ File.separator + "uploads"+ File.separator + fileName;
				FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
				// filesUploaded.add(fileName);

				int idx = fileName.lastIndexOf(".");
				NuclideAttached nuclideAttached = new NuclideAttached();
				nuclideAttached.setFileName(fileName.substring(0, idx));
				String fileType = fileName.substring(idx + 1, fileName.length());
				nuclideAttached.setFileType(fileType);
				nuclideAttached.setFilePath(PATH_PREFIX + File.separator + tracerId + File.separator + fileName);
				nuclideAttached.setFileFullPath(File.separator + path + File.separator + tracerId + File.separator + fileName);
				nuclideAttached.setNuclideUser(user);
				nuclideAttached.setNuclideBottle(tracerSaved);
				nuclideAttached.setFileDate(new Date());
				if (!nuclideAttachedService.saveNuclideAttached(nuclideAttached)) {
					String redirect = "warning";
					mapToForward.put("redirect", redirect);
					return mapToForward;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		String redirect = "listAttachedFiles" + tracerId;
		mapToForward.put("redirect", redirect);
		return mapToForward;
	}

	@RequestMapping(value = "/attachFile{tracerId}", method = RequestMethod.GET)
	public ModelAndView displayAttachFile(@PathVariable Long tracerId) {
		ModelAndView mv = new ModelAndView("/attachFile");
		mv.addObject("tracerId", tracerId);
		return mv;
	}

	@RequestMapping(value = "/attachFile{tracerId}", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> fileUploadTo(@ModelAttribute TracerOverview tracerOverview,
			MultipartHttpServletRequest request, HttpServletResponse response) {
		Properties prop = fetchProperties(request);
		String path = prop.getProperty("files.path");
		Map<String, Object> mapToForward = new HashMap<String, Object>();
		Iterator<String> itr = request.getFileNames();
		String tracerId = request.getParameter("tracerId");
		String currentUserId = request.getParameter("currentUserId");
		MultipartFile multipartFile = null;
		NuclideUser user = nuclideUserService.findByUserId(currentUserId);
		NuclideBottle tracerSaved = nuclideBottleService.findById(Integer.valueOf(tracerId));
		if (itr.hasNext()) {
			new File(path + File.separator + tracerId).mkdir();
		}
		while (itr.hasNext()) {
			multipartFile = request.getFile(itr.next());
			String fileName = multipartFile.getOriginalFilename().replace(" ", "-");
			String file = "";
			try {
				File tracerFolder = new File(path + File.separator + tracerId);
				boolean tracerFolderCreated = tracerFolder.mkdirs();
				if (tracerFolderCreated || tracerFolder.exists()) {					
					file = path + File.separator + tracerId + File.separator
							+ fileName;
					FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
					
					int idx = fileName.lastIndexOf(".");
					NuclideAttached nuclideAttached = new NuclideAttached();
					nuclideAttached.setFileName(fileName.substring(0, idx));
					String fileType = fileName.substring(idx + 1, fileName.length());
					nuclideAttached.setFileType(fileType);
					nuclideAttached.setFilePath(PATH_PREFIX +File.separator + tracerId + File.separator + fileName);
					nuclideAttached.setFileFullPath(File.separator + path + File.separator + tracerId + File.separator + fileName);
					nuclideAttached.setNuclideUser(user);
					nuclideAttached.setNuclideBottle(tracerSaved);
					nuclideAttached.setFileDate(new Date());
					
					nuclideAttachedService.saveNuclideAttached(nuclideAttached);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		String redirect = "listAttachedFiles" + tracerId;
		mapToForward.put("redirect", redirect);
		return mapToForward;
	}
	
	@RequestMapping(value = "/deleteFile{deleteParam}", method = RequestMethod.GET)
	public ModelAndView deleteAttachFile(@PathVariable String deleteParam, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String currrentUserId = (String) session.getAttribute("username");
		
		int attachedFileId = Integer.valueOf(StringUtils.substringBetween(deleteParam,"(",")"));
		String tracerId = StringUtils.substringBefore(deleteParam, "(");
		String userId = StringUtils.substringAfter(deleteParam, ")");
		String redirect = "redirect:/listAttachedFiles" + tracerId;
		ModelAndView mv = new ModelAndView(redirect);
		NuclideAttached nuclideAttached = nuclideAttachedService.findById(attachedFileId);
		String fullPath = nuclideAttached.getFileFullPath();
		
		if(!StringUtils.equalsIgnoreCase(currrentUserId.trim(), userId.trim())) {
			ModelAndView modv = new ModelAndView("redirect:/errorCannotRemove");
			return modv;
		}

		nuclideAttached.setNuclideBottle(null);
		nuclideAttached.setNuclideUser(null);
		nuclideAttachedService.deleteNuclideAttached(nuclideAttached);
		try {
			Files.deleteIfExists(Paths.get(fullPath));
		} catch (IOException e) {
			e.printStackTrace();
			ModelAndView modv = new ModelAndView("redirect:/errorCannotRemove");
			return modv;
		} 
		mv.addObject("tracerId", tracerId);
		return mv;
	}
	
	public Properties fetchProperties(HttpServletRequest request){
        Properties properties = new Properties();
        try {
        	File file;
        	if(request.getRequestURL() != null && (request.getRequestURL().toString().contains("ares"))) {
        		file = ResourceUtils.getFile("classpath:datasource-prod.properties");
        	}else {
        		file = ResourceUtils.getFile("classpath:datasource-test.properties");
        	}
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
        	logger.error(e.getMessage());
        }
        return properties;
    }

}
