//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.7 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.10.24 alle 12:38:29 PM CEST 
//


package people.generated;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (people.XsdDateTimeConverter.XSDDateToDate(value));
    }

    public String marshal(Date value) {
        return (people.XsdDateTimeConverter.printDateTime(value));
    }

}
