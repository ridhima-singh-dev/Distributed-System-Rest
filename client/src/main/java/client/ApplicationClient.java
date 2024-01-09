package client;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.core.ClientInfo;
import service.core.Quotation;

public class ApplicationClient {

    public static void main(String[] args)  throws Exception{
        HttpClient httpClient = HttpClients.createDefault();
        String serverUrl = "http://localhost:8083/applications";
        HttpPost httpPost = new HttpPost(serverUrl);

        ObjectMapper objectMapper = new ObjectMapper();
        String json;

        // Create the broker and run the test data
        for (ClientInfo info : clients) {
            displayProfile(info);
            json = objectMapper.writeValueAsString(info);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity httpentity = response.getEntity();
            if (httpentity != null) {
                String result = EntityUtils.toString(httpentity);
                ObjectMapper resobjectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});
                List<Map<String, Object>> quotationsMapList = (List<Map<String, Object>>) jsonMap.get("quotations");
                List<Quotation> quotationsList = new ArrayList<>();
                for (Map<String, Object> quotationMap : quotationsMapList) {
                    Quotation quotation = resobjectMapper.convertValue(quotationMap, Quotation.class);
                    quotationsList.add(quotation);
                }
                for(Quotation quot : quotationsList) {
                    displayQuotation(quot);
                }
            }
        }
    }

    /**
     * Display the client info nicely.
     *
     * @param info
     */
    public static void displayProfile(ClientInfo info) {
        System.out.println("|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.name) +
                        " | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
                        " | Age: " + String.format("%1$-30s", info.age)+" |");
        System.out.println(
                "| Weight/Height: " + String.format("%1$-20s", info.weight+"kg/"+info.height+"m") +
                        " | Smoker: " + String.format("%1$-27s", info.smoker?"YES":"NO") +
                        " | Medical Problems: " + String.format("%1$-17s", info.medicalIssues?"YES":"NO")+" |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Display a quotation nicely - note that the assumption is that the quotation will follow
     * immediately after the profile (so the top of the quotation box is missing).
     *
     * @param quotation
     */
    public static void displayQuotation(Quotation quotation) {
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.company) +
                        " | Reference: " + String.format("%1$-24s", quotation.reference) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Test Data
     */
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 1.6, 100, true, true),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 21, 1.78, 65, false, false),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 49, 1.8, 120, false, true),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 1.9, 75, true, false),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 0.45, 1.6, false, false)
    };
}