package people;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

public class XsdDateTimeConverter {

	public static Date XSDDateToDate(String date) {
		return DatatypeConverter.parseDate(date).getTime();
	}
	
	public static Date XSDDatetimeToDateTime(String date) {
		return DatatypeConverter.parseDateTime(date).getTime();
	}
	
    public static String printDate(Date dateTime) {
        String s = "";
        s = dateTime.getDate()+"-"+(dateTime.getMonth()+1)+"-"+(dateTime.getYear()+1900);
        return s;
    }
    public static String printDateTime(Date dateTime) {
    	String s = "";
        s = dateTime.getDate()+"-"+(dateTime.getMonth()+1)+"-"+(dateTime.getYear()+1900)+ " "+ dateTime.getHours()+":"+(dateTime.getMinutes()/10)+(dateTime.getMinutes()%10);
        return s;
    }

}