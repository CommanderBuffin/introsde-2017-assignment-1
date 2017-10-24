# SDE 2017 Assigment 1 


**Assigment 1 - Mattia Buffa mattia.buffa-1@studenti.unitn.it**

## Project Description

### Code analysis

**Parser method to Person from a "person" XML node**

The following code is used to parse a person node to a Person object getting subnodes as values:

```
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

The following code is used to display the activity description value given a personId from the XML file after the person parsing:

```
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

The following code is used to display the activity place value given a personId from the XML file after the person parsing:

```
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

The following code is used to read all people and them from the XML file after the person parsing:

```
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

The following code is used to display filtered people using a condition ( >, <, =) and a date as parameters:

```
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