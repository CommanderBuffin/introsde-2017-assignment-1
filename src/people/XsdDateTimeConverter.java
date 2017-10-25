package people;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

//class that define how to convert XMLGregorianCalendar to java.util.Date used by binding.xml and xjc
public class XsdDateTimeConverter {

	//from String date without hours to Date
	public static Date XSDDateToDate(String date) {
		return DatatypeConverter.parseDate(date).getTime();
	}
	
	//from String date with hours to Date
	public static Date XSDDatetimeToDateTime(String date) {
		return DatatypeConverter.parseDateTime(date).getTime();
	}
	
	//from Date to String date without hours
    public static String printDate(Date dateTime) {
        String s = "";
        s = dateTime.getDate()+"-"+(dateTime.getMonth()+1)+"-"+(dateTime.getYear()+1900);
        return s;
    }
	
	//from Date to String date with hours
    public static String printDateTime(Date dateTime) {
    	String s = "";
        s = dateTime.getDate()+"-"+(dateTime.getMonth()+1)+"-"+(dateTime.getYear()+1900)+ " "+ dateTime.getHours()+":"+(dateTime.getMinutes()/10)+(dateTime.getMinutes()%10);
        return s;
    }

}