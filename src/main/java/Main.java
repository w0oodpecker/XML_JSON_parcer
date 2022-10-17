import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String fileName = "data.xml";
        String jsonFileName = "data.json";

        List<Employee> employeeList = parceXML(fileName);
        String json = listToJason(employeeList);
        writeString(json, jsonFileName);
    }


    //Парсинг CSV в список объектов
    public static List<Employee> parceXML(String fileName) {

        List<Employee> employeeList;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();
            employeeList = read(root);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }


    private static List<Employee> read(Node node) {

        NodeList nodeList = node.getChildNodes();
        List<Employee> employeeList = new ArrayList<>();
        long attrID;
        String attrFirstName;
        String attrLastName;
        String attrCountry;
        int attrAge;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);

            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element element = (Element) node_;
                attrID = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                attrFirstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                attrLastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                attrCountry = element.getElementsByTagName("country").item(0).getTextContent();
                attrAge = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

                Employee emp = new Employee(attrID, attrFirstName, attrLastName, attrCountry, attrAge);
                employeeList.add(emp);
            }
        }
        return employeeList;
    }


    //Преобразование списка объектов в строку Jason
    public static String listToJason(List employeeList) {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(employeeList, listType);
    }


    //Запись строки Json в файл
    public static void writeString(String string, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(string);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
