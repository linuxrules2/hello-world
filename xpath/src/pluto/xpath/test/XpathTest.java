package pluto.xpath.test;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;

public class XpathTest {

    public static void main(String args[]) {

        try
        {
            FileInputStream fileIS = new FileInputStream("/Users/pluto/IdeaProjects/xpath/src/pluto/xpath/test/tutorials.xml");

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(fileIS);

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "/Tutorials/Tutorial";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

            String exp2 = "@tutId";
            String id = (String)xPath.compile(exp2).evaluate(nodeList.item(0));

            //String expression = "/Tutorials/Tutorial[@tutId=" + "'" + "02" + "'" + "]";
            //Node node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            System.out.println("The End");
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}
