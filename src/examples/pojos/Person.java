// classes are grouped together in 'packages'
// Classes in the same pakage already see each other. 
// If a class is in another package, in other to see it, you need to import it
package examples.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Class representing a Person.
 * Attributes: id (int), firstname (string), lastname (string), birthdate (Date), activity (Activity)
 */
@XmlRootElement(name = "person")
@XmlType(propOrder = { "firstname", "lastname", "birthdate", "activity" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
	//setting id as attribute instead of element in the xml
	@XmlAttribute(name="id")
	private int id;
	
	//private variable that will become element inside Person xml
	private String firstname;
	private String lastname;	
	private Date birthdate;
	
	//renaming activity as activitypreference in the xml
	@XmlElement(name="activitypreference")
	private Activity activity;
	
	// this constructor creates a Person object with a particular id, firstname, lastname, birthdate and activity
	public Person(int id, String fname, String lname, Date birthdate, Activity activity) {
		this.id = id;
		this.setFirstname(fname);
		this.setLastname(lname);
		this.birthdate = birthdate;
		this.activity=activity;
	}

	//getter and setter for the class variables
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Activity getActivity() {
		return activity;
	}
	public void sethProfile(Activity activity) {
		this.activity = activity;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	//toString method to define which data will be printed for Person.
	public String toString() {
		String r = "";
		String d = birthdate.getDate()+"-"+(birthdate.getMonth()+1)+"-"+(birthdate.getYear()+1900);
		String ad = activity.getStartDate().getDate()+"-"+(activity.getStartDate().getMonth()+1)+"-"+(activity.getStartDate().getYear()+1900)+" "+activity.getStartDate().getHours()+":"+activity.getStartDate().getMinutes();
		r = String.format("PERSON%1$30sACTIVITY\r\nName: %2$-30sName: %3$-30s\r\nLastname: %4$-26sDescription: %5$-30s\r\nBirthdate: %6$-25sPlace: %7$-30s\r\n%8$-36sStartdate: %9$-30s\r\n",
				"",firstname,activity.getName(),lastname,activity.getDescription(),d,activity.getPlace(),"",ad);
		return r;
	}
	
}
