package people;

import people.generated.*;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JAXBMarshaller {
	public void generateXMLDocument(File xmlDocument) throws DatatypeConfigurationException {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance("people.generated");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
			people.generated.ObjectFactory factory = new people.generated.ObjectFactory();
			//people.addPerson(person);
			PeopleType people = generatePeople();
			
			JAXBElement<PeopleType> peopleElement = factory.createPeople(people);
			marshaller.marshal(peopleElement,
					new FileOutputStream(xmlDocument));
			marshaller.marshal(peopleElement, System.out);

		} catch (IOException e) {
			System.out.println(e.toString());

		} catch (JAXBException e) {
			System.out.println(e.toString());

		}

	}
	
	public PeopleType generatePeople() {
		people.generated.ObjectFactory factory = new people.generated.ObjectFactory();

		PeopleType people = factory.createPeopleType();
		
		for(int i = 0; i < 3; i++) {
			PersonType person = factory.createPersonType();
			person.setFirstname("Firstname"+i);
			person.setLastname("Lastname"+i);
			/*GregorianCalendar gcalendar = new GregorianCalendar();
			gcalendar.setTime(new Date());
			XMLGregorianCalendar xmlDate = null;
			try {
				xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcalendar);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			Date xmlDate = new Date();
			person.setBirthdate(xmlDate);
			person.setId(BigInteger.valueOf(1+i));
			
			ActivityType activity = factory.createActivityType();
			activity.setName("Name"+i);
			activity.setDescription("Description"+i);
			activity.setPlace("Place"+i);
			activity.setId(BigInteger.valueOf(100+i));
			activity.setStartdate(xmlDate);
			//activity.setStartdate(Calendar.getInstance());
			
			person.setActivitypreference(activity);
			people.getPerson().add(person);
		}
		return people;
	}

	public static void main(String[] argv) throws DatatypeConfigurationException {
		String xmlDocument = "JAXBPerson.xml";
		JAXBMarshaller jaxbMarshaller = new JAXBMarshaller();
		jaxbMarshaller.generateXMLDocument(new File(xmlDocument));
	}
}
