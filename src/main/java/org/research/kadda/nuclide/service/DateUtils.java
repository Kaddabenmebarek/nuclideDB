package org.research.kadda.nuclide.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.list.TreeList;
import org.research.kadda.nuclide.entity.NuclideBottle;
import org.springframework.stereotype.Service;

/**
 * @author benmeka1
 * date tool
 *
 */
@Service(value = "dateUtils")
public class DateUtils {
	
	private final static String DATE_PATTERN = "yyyy-MMM-dd";
	private final static String DATE_PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * checkUsageAndParentTracerDates
	 * @param parentTracer Date
	 * @param usageDate Date
	 * @return error message String
	 */
	public String checkUsageAndParentTracerDates(Date parentTracerDate, Date usageDate) {
		if(!compareDates(parentTracerDate, usageDate)) {
			StringBuilder errorMsg = new StringBuilder("The usage date (");
			errorMsg.append(new SimpleDateFormat(DATE_PATTERN, Locale.US).format(usageDate));
			errorMsg.append(") is before the initial activity date of the parent tracer tube (");
			errorMsg.append(new SimpleDateFormat(DATE_PATTERN, Locale.US).format(parentTracerDate));
			errorMsg.append(").");
			errorMsg.append(" Please change the usage date.");
			return errorMsg.toString();
		}
		return null;
	}
	
	/**
	 * getFormatedDate
	 * @param date
	 * @return
	 */
	public String getFormatedDate(Date date) {
		return new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(date);
	}
	
	/**
	 * checkWasteClosureDate
	 * @param wasteClosureDate Date
	 * @param disposedTracers List<NuclideBottle>
	 * @return error message String
	 */
	public String checkWasteClosureDate(Date wasteClosureDate, List<NuclideBottle> disposedTracers) {
		List<Long> disposalTracersDates = new TreeList<Long>();
		for(NuclideBottle tracer : disposedTracers) {
			disposalTracersDates.add(tracer.getDisposalDate().getTime());
		}
		Date mostRecentDisposalTracerDate = new Date (disposalTracersDates.get(disposalTracersDates.size()-1));
		if(!compareDates(mostRecentDisposalTracerDate, wasteClosureDate)) {
			StringBuilder errorMsg = new StringBuilder("The waste closure date (");
			errorMsg.append(new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(wasteClosureDate));
			errorMsg.append(") is before the most recent disposal date of tracers inside (");
			errorMsg.append(new SimpleDateFormat("yyyy-MMM-dd", Locale.US).format(mostRecentDisposalTracerDate));
			errorMsg.append(").");
			errorMsg.append(" Please change the waste closure date.");
			return errorMsg.toString();
		}
		return null;
	}
	
	public Date getMostRecentDisposedTracerDate(List<NuclideBottle> disposedTracers) {
		if (CollectionUtils.isNotEmpty(disposedTracers)) {
			List<Long> disposalTracersDates = new TreeList<Long>();
			for (NuclideBottle tracer : disposedTracers) {
				if (tracer.getDisposalDate() != null)
					disposalTracersDates.add(tracer.getDisposalDate().getTime());
			}
			if(!disposalTracersDates.isEmpty()) {				
				Date mosteRecentDisposedTracerDate = new Date(disposalTracersDates.get(disposalTracersDates.size() - 1));
				return mosteRecentDisposedTracerDate;
			}
		}
		return null;
	}
	
	/**
	 * format String into Date
	 * @param date
	 * @return Date
	 */
	public Date formatDate(String date) {
		DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.US);
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * check if date2 is after date1
	 * @param sDate1 String
	 * @param sDate2 String
	 * @return boolean
	 * @throws ParseException
	 */
	public boolean compareDates(String sDate1, String sDate2) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat (DATE_PATTERN, Locale.US);
        Date date1 = dateFormat.parse(sDate1);
        Date date2 = dateFormat.parse(sDate2);
        if(date2.after(date1)) {
            return true;
        } else {
            return false;
        }
    }
	
	/**
	 * check if date2 is after date1
	 * @param date1 Date
	 * @param date2 Date
	 * @return boolean
	 */
	public boolean compareDates(Date date1, Date date2) {
        return date2.after(date1);
    }
	
	/**
	 * @param day1 String
	 * @param day2 String 
	 * @return days between day2 and day1
	 */
	public String daysBetween(String day1, String day2) {
	    String daysBetween = "";
	    SimpleDateFormat myFormat = new SimpleDateFormat(DATE_PATTERN_FULL, Locale.US);
	    try {
	        Date date1 = myFormat.parse(day1);
	        Date date2 = myFormat.parse(day2);
	        long diff = date2.getTime() - date1.getTime();
	        daysBetween = ""+(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return daysBetween;
	}
	
}
