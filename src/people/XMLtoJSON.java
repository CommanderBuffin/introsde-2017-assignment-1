package people;

import java.io.File;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import people.generated.PeopleType;

public class XMLtoJSON {
	//get data from jaxbmarshall which read people.xml file
	private static PeopleType initilizeDB() {
		JAXBMarshaller jaxbMarshaller = new JAXBMarshaller();
		return jaxbMarshaller.generatePeople();
	}
	
	public static void main(String[] args) throws Exception {
		
		PeopleType people = initilizeDB();
		
		if(people!=null) {
			// Jackson Object Mapper 
			ObjectMapper mapper = new ObjectMapper();
			
			// Adding the Jackson Module to process JAXB annotations
	        JaxbAnnotationModule module = new JaxbAnnotationModule();
	        
			// configure as necessary
			mapper.registerModule(module);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
	
	        String result = mapper.writeValueAsString(people);
	        System.out.println(result);
			//write the new json file
	        mapper.writeValue(new File("people.json"), people);
		}
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
