package examples;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import examples.pojos.Activity;
import examples.pojos.Person;


public class PersonProfileReader {
	
	static Document doc;
    static XPath xpath;

	//load people.xml file
    public void loadXML() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        doc = builder.parse("people.xml");

        //creating xpath object
        getXPathObj();
    }

	//create an xpath object from the xml
    public static XPath getXPathObj() {

        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
        return xpath;
    }
    
	//return a string with filtered person name and the description of his activity
    public static String getActivityDescription(String personId) throws XPathExpressionException {
		//XPath expression to filter person by id
        XPathExpression expr = xpath.compile("/people/person[@id='" + personId + "']");
        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
        String s = "";
        Person p;
		try {
			p = NodeToPerson(node);
			//compose a human readable string of data
			s = "PERSON: "+p.getFirstname()+" "+p.getLastname()+"\r\nActivity description: "+p.getActivity().getDescription();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        return s;
    }
    
	//return a string with filtered person name and the place of his activity
    public static String getActivityPlace(String personId) throws XPathExpressionException {
		//XPath expression to filter person by id
        XPathExpression expr = xpath.compile("/people/person[@id='" + personId + "']");
        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
        String s = "";
        Person p;
		try {
			p = NodeToPerson(node);
			//compose a human readable string of data
			s = "Activity place: "+p.getActivity().getPlace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        return s;
    }
	
	//return a nodelist of all the people
    private static NodeList getPeople() throws XPathExpressionException {
    	XPathExpression expr = xpath.compile("/people/person");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nodes;
    }
    
	//Convert a Node to Person
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
    
	//return a string with all the people
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
    
	//return a string with filtered person name and his activity
    public static String getPersonActivity(String personId) throws XPathExpressionException {
    	//XPath expression to filter person by id
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
    
	//return a string with person and his activity filtered by date and a condition
    public static String getPeopleByActivityDate(String date, String condition) throws XPathExpressionException {
    	
    	String r="";
    	String s_date = date.split("-")[0]+date.split("-")[2]+date.split("-")[1];
    	//XPath expression to get all people filtering by date and condition on his activity
		XPathExpression expr = xpath.compile("//person[(number(substring( translate(activitypreference/startdate,'-T:.',''),1,8)))"+condition+s_date+"]");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
        	//Person p = (Person)nodes.item(i);
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
    
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    	
    	PersonProfileReader ppr = new PersonProfileReader();
		//load xml
        ppr.loadXML();
		//switch to call different methods
        switch(args[0]) {
	    	case "printPeople": System.out.println(printPeople()); break;
	    	case "fullActivity":  
	    		if(args[1]!=null) {
	    			System.out.println(getPersonActivity(args[1]));
	    		}
				break;
	    	case "date": 
	    		if(args[1]!=null && args[2]!=null) {
					System.out.println(getPeopleByActivityDate(args[1],args[2]));
	    		}
	    		break;
        	default: break;
        }
    }
}