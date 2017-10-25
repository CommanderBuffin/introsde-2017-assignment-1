# SDE 2017 Assigment 1 


**Assigment 1 - Mattia Buffa mattia.buffa-1@studenti.unitn.it**

**Table of Contents**

- [SDE 2017 Assigment 1](#sde-2017-assigment-1)
	- [Project Description](#project-description)
		- [Code analysis](#code-analysis)
		- [Code tasks](#code-tasks)
		- [Execution](#execution)
		- [Additional Notes](#additional-notes)

## Project Description

### Code analysis

**Parser method to Person from a "person" XML node**

The following code is used to parse a person node to a Person object getting subnodes as values

```Java
private static Person NodeToPerson(Node n) throws ParseException {
	//Person(int id, String fname, String lname, Date birthdate, Activity activity)
	
	NodeList nl = n.getChildNodes();
	Node na = nl.item(7);
	
	//Activity(int id, String name, String description, String place, Date startDate)
	
	int activityId = Integer.parseInt(na.getAttributes().item(0).getNodeValue());
	String activityName = na.getChildNodes().item(1).getTextContent();
	String activityDesc = na.getChildNodes().item(3).getTextContent();
	String activityPlace = na.getChildNodes().item(5).getTextContent();

	String s_ad = na.getChildNodes().item(7).getTextContent();
	DateFormat a_df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	Date activityDate = a_df.parse(s_ad);
	Activity a = new Activity(activityId, activityName, activityDesc, activityPlace, activityDate);
	
	int personId = Integer.parseInt(n.getAttributes().item(0).getNodeValue());
	String name = nl.item(1).getTextContent();
	String lastname= nl.item(3).getTextContent();
	String s_d = nl.item(5).getTextContent();
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Date d = df.parse(s_d);
	Person p = new Person(personId, name, lastname, d, a);
	return p;
}
```

**Methods to diplay people informations implemented inside PersonProfileReader.java**

The following code displays the activity description value given a personId from the XML file after the person parsing

```Java
public static String getActivityDescription(String personId) throws XPathExpressionException {
	XPathExpression expr = xpath.compile("/people/person[@id='" + personId + "']");
	Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
	String s = "";
	Person p;
	try {
		p = NodeToPerson(node);
		s = "PERSON: "+p.getFirstname()+" "+p.getLastname()+"\r\nActivity description: "+p.getActivity().getDescription();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	return s;
}
```

The following code displays the activity preference of a person filtered by id

```Java
public static String getPersonActivity(String personId) throws XPathExpressionException {	
	XPathExpression expr = xpath.compile("/people/person[@id='" + personId + "']");
	Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
	String s="";
	Person p;
	try {
		p = NodeToPerson(node);
		s= p.getActivity().toString();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	return s;
}
```

The following code displays the activity place value given a personId from the XML file after the person parsing

```Java
public static String getActivityPlace(String personId) throws XPathExpressionException {
	XPathExpression expr = xpath.compile("/people/person[@id='" + personId + "']");
	Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
	String s = "";
	Person p;
	try {
		p = NodeToPerson(node);
		s = "Activity place: "+p.getActivity().getPlace();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	return s;
}
```

The following code reads all people and them from the XML file after the person parsing

```Java
private static NodeList getPeople() throws XPathExpressionException {
	XPathExpression expr = xpath.compile("/people/person");
	NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	return nodes;
}

public static String printPeople(){
	String r="";
	NodeList people;
	try {
		people = getPeople();
		for (int i = 0; i < people.getLength(); i++) {
			Person p;
			try {
				p = NodeToPerson(people.item(i));
				r+=p.toString()+"\r\n";
			} catch (ParseException e) {
				e.printStackTrace();
			}	
		}
	} catch (XPathExpressionException e) {
		e.printStackTrace();
	}
	return r;
}
```

The following code displays filtered people using a condition ( >, <, =) and a date as parameters

```Java
public static String getPeopleByActivityDate(String date, String condition) throws XPathExpressionException {
	String r="";
	String s_date = date.split("-")[0]+date.split("-")[2]+date.split("-")[1];
	XPathExpression expr = xpath.compile("//person[(number(substring( translate(activitypreference/startdate,'-T:.',''),1,8)))"+condition+s_date+"]");
	NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	for (int i = 0; i < nodes.getLength(); i++) {
		Person p;
		try {
			p = NodeToPerson(nodes.item(i));
			r+=p.toString()+"\r\n";
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	return r;
}
```

**Marshaller and Unmarshaller code**

The following code creates the JAXBPerson.xml file using a marshaller based on classes generated through people.xsd schema and prints the result

```Java
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
```

The following code uses the people.xsd schema to read people.xml file and unmarshal it to generate PeopleType, PersonType and ActivityType objects and prints them

```Java
public PeopleType unMarshall(File xmlDocument) {
	try {

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
```

**XML to Json convertion**

The following code uses Jackson library to convert XML data to Json generating people.json and prints the result

```Java
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
		mapper.writeValue(new File("people.json"), people);
	}
}
```

### Code tasks

**Retrieving data through XPath**

The first task of the code is to read people.xml and to show data based on the required filter.  
The build.xml file contains 6 targets which are called by execute.evaluation, the first three targets use XPath to filter data and to print the results in a human readable format.  
Target 1 prints all the people's information using printPeople() method.  
Target 2 filters a person using getPersonActivity(string personId) method and prints its activity preference, in the example the used id is "0005".  
Target 3 prints all the filtered people's information using getPeopleByActivityDate(String date, String condition) method, in the example date="2017-13-10" and condition=">".

**XSD and classes generation**

The next task consists of automatic generation of classes using xjc command on the schema defined by people.xsd.  
This procedure will use another file named binding.xml which it allows to define adapters to convert XMLGreogorianCalendar type, that is the standard date type, to any other type, in this case Date from java.util package.  
**_Alert:_** _the binding procedure for Java 8 requires the javax.xml.accessExternalSchema flag to be set on value="all", how to set it will be described inside Additional Notes paragraph._

**Marshaller and Unmarshaller**

Target 4 and Target 5 will use the previous generated classes to define JAXBMarshaller and JAXBUnmarshaller.  
Target 4 executes the marshalling to JAXBpeople.xml of three people defined inside the JAXBMarshaller.java file.  
Target 5 will unmarshall the people.xml file and prints the result.

**XML to Json**

Target 6 will read the people.xml file using the unmarshaller used by Target 5 and will generate people.json file which contains all the people read.

**Build.xml**

The file build.xml contains all the task that will:  
- install, download and resolve ivy dependencies  
- generate classes using people.xsd  
- compile classes  
- execute targets  
- run execute.evaluation target which will call all the required targets
	
### Execution

**_Alert:_** _before executing check to have set javax.xml.accessExternalSchema=all, for more information see Additional Notes paragraph._

In order to execute the assigment we need to have ant installed: open a terminal, navigate to the root project folder and run this instruction "ant execute.evaluation".

### Additional Notes

Since xjc command generates automatically classes based on people.xsd schema there is a problem with the default type defined for date values.  
In order to convert the default type XMLGreogorianCalendar to java.util.Date we need to bind people.xsd with binding.xml which defines how to convert date values.  
In Java 8 there is a flag that blocks the binding procedure, so we need to change it.  
To solve the problem we need to create a file called "jaxp.properties" and save it inside "path\of\jdk_version\jre\lib", in my case is "C:\Program Files\Java\jdk1.8.0_144\jre\lib".  
This file has to contain this text "javax.xml.accessExternalSchema = all" without quotes.  
Another way is to set this flag javax.xml.accessExternalSchema = all for the JVM inside your IDE and then execute the target execute.evaluation from it.  