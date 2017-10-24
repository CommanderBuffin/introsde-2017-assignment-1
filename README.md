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