import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;

public class SlowXPath {
    public static void main(String[] args) throws Exception {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        String xmlString = prepareXml(5000);

//    System.out.println(xmlString);
        final Document xmlDoc = documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));

        final XPathFactory xPathFactory = XPathFactory.newInstance();

        final XPathExpression nodeXPath = xPathFactory.newXPath().compile("//node");
        final XPathExpression iXPath = xPathFactory.newXPath().compile("./i/text()");

        final NodeList nodeList = (NodeList) nodeXPath.evaluate(xmlDoc, XPathConstants.NODESET);

        System.out.println("Nodes number=" + nodeList.getLength());

        timeIt("Simple iterate", new Runnable() {
            @Override
            public void run() {
                int sum = 0;

                for (int i = 0; i < nodeList.getLength(); i++) {
                    final Node node = nodeList.item(i);
                    try {
                        final String iStr = (String) iXPath.evaluate(node, XPathConstants.STRING);
                        sum += Integer.parseInt(iStr.trim());
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Sum=" + sum);
            }
        });
        timeIt("Iterate with cloning", new Runnable() {
            @Override
            public void run() {
                int sum = 0;

                for (int i = 0; i < nodeList.getLength(); i++) {
                    final Node node = nodeList.item(i).cloneNode(true); // <-- Note cloning here
                    try {
                        final String iStr = (String) iXPath.evaluate(node, XPathConstants.STRING);
                        sum += Integer.parseInt(iStr.trim());
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Sum=" + sum);
            }
        });
        timeIt("Iterate with detaching node", new Runnable() {
            @Override
            public void run() {
                int sum = 0;

                for (int i = 0; i < nodeList.getLength(); i++) {
                    final Node node = nodeList.item(i);

                    node.getParentNode().removeChild(node); // <-- Note detaching node

                    try {
                        final String iStr = (String) iXPath.evaluate(node, XPathConstants.STRING);
                        sum += Integer.parseInt(iStr.trim());
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Sum=" + sum);
            }
        });
    }

    private static String prepareXml(int n) {
        StringBuilder sb = new StringBuilder();

        sb.append("<root>");

        for (int i = 0; i < n; i++) {
            sb.append("<node><i>\n")
                .append(i)
                .append("</i></node>\n");
        }

        sb.append("</root>");

        return sb.toString();
    }

    private static void timeIt(String name, Runnable runnable) {
        long t0 = System.currentTimeMillis();
        runnable.run();
        long t1 = System.currentTimeMillis();

        System.out.println(name + " executed " + ((t1 - t0) / 1000f) + "sec.");
    }
}