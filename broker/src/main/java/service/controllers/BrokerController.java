package service.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import service.core.Application;
import service.core.ClientInfo;
import service.core.Quotation;

@RestController
public class BrokerController {
	private int id = 1000;
	private Map<Integer, Application> applicationMap = new TreeMap<>();
	//private LocalBrokerService service = new LocalBrokerService();
	//private final String[] urls = {"http://localhost:8080/quotations", "http://localhost:8081/quotations", "http://localhost:8082/quotations"};
	private final String[] urls = {"http://auldfellas:8080/quotations", "http://dodgygeezers:8082/quotations", "http://girlsallowed:8081/quotations"};
	 
	private List<String> registeredServices = new ArrayList<>();
	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${server.port}")
	private int port;

	@GetMapping(value = "/applications", produces = "application/json")
    public ResponseEntity<ArrayList<String>> getQuotationURLs() {
        ArrayList<String> list = new ArrayList<>();
        for (Application application : applicationMap.values()) {
            for (Quotation quotation : application.quotations) {
                list.add("http:" + getHost() + "/quotations/" + quotation.reference);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
	
	@GetMapping(value = "/applications/{id}", produces = "application/json")
    public ResponseEntity<Application> getApplication(@PathVariable Integer id) {
        Application application = applicationMap.get(id);
        if (application == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        System.out.println("Inside application get method");
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
	
	@PostMapping(value = "/applications", consumes = "application/json")
    public ResponseEntity<Application> createApplication(@RequestBody ClientInfo info) {
        Application application = new Application(info);
        
        for (String url : registeredServices) {
            ResponseEntity<Quotation> quotationResponse = restTemplate.postForEntity(url, info, Quotation.class);
            if (quotationResponse.getStatusCode().equals(HttpStatus.CREATED)) {
                Quotation quotation = quotationResponse.getBody();
                application.quotations.add(quotation);
            }
        }
        applicationMap.put(application.getId(), application);

        // Return the created application
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }
	
	@PostMapping("/services")
    public ResponseEntity<String> registerService(@RequestBody String serviceUrl) {
        registeredServices.add(serviceUrl);
        String message = "Service registered: " + serviceUrl;
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/services")
    public ResponseEntity<List<String>> getRegisteredServices() {
        return ResponseEntity.status(HttpStatus.OK).body(registeredServices);
    }

    
	private String getHost() {
		try {
			return InetAddress.getLocalHost().getHostAddress() + ":" + port;
		} catch (UnknownHostException e) {
			return "localhost:" + port;
		}
	}
}
