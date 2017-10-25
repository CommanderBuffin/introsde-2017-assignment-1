package examples.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
  * Class with the prefer activity of a Person.
  * Attributes: id (int), name (string), description (string), place (string), startDate (date).
  */
@XmlRootElement(name = "activitypreference")
@XmlType(propOrder = { "name", "description", "place", "startDate" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Activity {
	
	//setting id as attribute instead of element in the xml
	@XmlAttribute(name="id")
	private int id;
	
	//private variable that will become element inside Person xml
	private String name;
	private String description;
	private String place;
	
	//renaming activity as activitypreference in the xml
	@XmlElement(name="startdate")
	private Date startDate;

	// this constructor creates an Activity object with a particular id, name, description, place and startDate
	public Activity(int id, String name, String description, String place, Date startDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.place = place;
		this.startDate = startDate;
	}

	//getter and setter methods
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPlace() {
		return this.place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	//private method to convert a Date into a human-friendly format.
	private String DateToString(Date date) {
		return date.getDate()+"-"+(date.getMonth()+1)+"-"+(date.getYear()+1900);
	}
	
	//toString method to define which data will be printed for Activity.
	public String toString() {
		return "ACTIVITY\r\nName: "+name+"\r\nDescription: "+description+"\r\nPlace: "+place+"\r\nStartDate: "+DateToString(startDate);
	}

}
