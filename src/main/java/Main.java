import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> list = new ArrayList<>();
        try {
            list.addAll(parseXML("data.xml"));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        String json = listToJson(list);
        try {
            writeString("data2.json", json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        if (fileName == null || fileName.isEmpty())
            throw new IllegalArgumentException();

        List<Employee> employeesList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(fileName);
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Node employee = nodeList.item(i);
                if (employee.getNodeName().equals("employee")) {
                    NodeList dataList = employee.getChildNodes();
                    long id = 0;
                    String firstName = "";
                    String lastName = "";
                    String country = "";
                    int age = 0;
                    for (int j = 0; j < dataList.getLength(); j++) {
                        if (dataList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            if (dataList.item(j).getNodeName().equals("id")) {
                                id = Long.parseLong(dataList.item(j).getTextContent());
                            }
                            if (dataList.item(j).getNodeName().equals("firstName")) {
                                firstName = dataList.item(j).getTextContent();
                            }
                            if (dataList.item(j).getNodeName().equals("lastName")) {
                                lastName = dataList.item(j).getTextContent();
                            }
                            if (dataList.item(j).getNodeName().equals("country")) {
                                country = dataList.item(j).getTextContent();
                            }
                            if (dataList.item(j).getNodeName().equals("age")) {
                                age = Integer.parseInt(dataList.item(j).getTextContent());
                            }
                        }
                    }
                    if (id != 0 && !firstName.isEmpty() && !lastName.isEmpty()) {
                        employeesList.add(new Employee(id, firstName, lastName, country, age));
                    }
                }
            }
        }

        return employeesList;
    }

    private static void writeString(String fileName, String json) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
        }
    }


    private static String listToJson(List<Employee> list) {
        if (list == null)
            throw new IllegalArgumentException();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Type listType = new TypeToken<List<Employee>>() {}.getType();

        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }
}
