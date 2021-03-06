package people;

import people.generated.*;

import javax.xml.bind.*;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import java.io.*;

public class JAXBUnMarshaller {
	//convert xml data to object defined from xsd (PeopleType)
	public PeopleType unMarshall(File xmlDocument) {
		try {
			//init to config unMarshaller
			JAXBContext jaxbContext = JAXBContext.newInstance("people.generated");

			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File(
					"people.xsd"));
			unMarshaller.setSchema(schema);
			CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
			unMarshaller.setEventHandler(validationEventHandler);

			@SuppressWarnings("unchecked")
			JAXBElement<PeopleType> peopleElement = (JAXBElement<PeopleType>) unMarshaller
					.unmarshal(xmlDocument);

			PeopleType people = peopleElement.getValue();
			
			//print all the person
			for(PersonType person : people.getPerson()) {
				System.out.println(formatPerson(person));
			}
			return people;
		} catch (JAXBException e) {
			System.out.println(e.toString());
			return null;
		} catch (SAXException e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	//convert a Person to a String in human readable format
	private static String formatPerson(PersonType p) {
		String r = "";
		String d = p.getBirthdate().getDay()+"-"+(p.getBirthdate().getMonth()+1)+"-"+(p.getBirthdate().getYear()+1900);
		ActivityType a = p.getActivitypreference();
		String ad = a.getStartdate().getDay()+"-"+(a.getStartdate().getMonth()+1)+"-"+(a.getStartdate().getYear()+1900)+" "+a.getStartdate().getHours()+":"+(a.getStartdate().getMinutes()/10)+(a.getStartdate().getMinutes()%10);
		r = String.format("PERSON%1$30sACTIVITY\r\nName: %2$-30sName: %3$-30s\r\nLastname: %4$-26sDescription: %5$-30s\r\nBirthdate: %6$-25sPlace: %7$-30s\r\n%8$-36sStartdate: %9$-30s\r\n",
				"",p.getFirstname(),a.getName(),p.getLastname(),a.getDescription(),d,a.getPlace(),"",ad);
		return r;
	}

	public static void main(String[] argv) {
		//open people.xml and call the unMarshaller
		File xmlDocument = new File("people.xml");
		JAXBUnMarshaller jaxbUnmarshaller = new JAXBUnMarshaller();

		jaxbUnmarshaller.unMarshall(xmlDocument);

	}

	class CustomValidationEventHandler implements ValidationEventHandler {
		public boolean handleEvent(ValidationEvent event) {
			if (event.getSeverity() == ValidationEvent.WARNING) {
				return true;
			}
			if ((event.getSeverity() == ValidationEvent.ERROR)
					|| (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {

				System.out.println("Validation Error:" + event.getMessage());

				ValidationEventLocator locator = event.getLocator();
				System.out.println("at line number:" + locator.getLineNumber());
				System.out.println("Unmarshalling Terminated");
				return false;
			}
			return true;
		}

	}
}
