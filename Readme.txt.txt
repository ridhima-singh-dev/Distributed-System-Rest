REST Web Serice Implementation

Introduction
This practical focus is on adapting the LifeCo system to use REST web service for communication between components. The goal is to create a distributed system where each component can run in  a separate Docker container. This involved creating a microservices architecture for generating insurance quotations and managing applications.

The assignment focused on how microservices communicate with each other. The broker service played a crucial role in mediating communication between the application service and multiple quotation services. This highlighted the importance of service coordination and integration in a microservices architecture.

Components
core: Contains shared code, including distributed object interfaces and data classes.
auldfellas, dodgygeezers, girlsallowed: Quotation services providing quotes based on client information.
broker: The broker service get quotations from Quoatation services using rest web service.
client: Client services request quotations from the broker.

Module Integration: 
The core code serves as the foundation for the entire system. It defines the QuotationService interface, which specifies methods for obtaining quotations. Each quotation service module auldfellas, dodgygeezers, girlsallowed implements this interface. This step establishes a common contract for all services to adhere to when providing quotations.

Spring Boot: We used the Spring Boot framework to develop RESTful web services. Spring Boot simplifies the development of Java applications, made it easier to create web services and handle common tasks like dependency injection, configuration, and routing.

The use of configuration properties, such as server ports and service URLs, was a practical way to manage configurations. This approach allowed for easy configuration changes without modifying the source code.

RESTful API: We designed and implemented RESTful APIs for the services. RESTful principles, such as using HTTP methods (GET, POST) and adhering to resource-based URLs, were applied to create a clean and well-structured API.

HTTP Requests and Responses: I gained experience in making HTTP requests and handling responses. This included sending JSON data in request bodies and receiving JSON responses. We also learned how to handle different HTTP status codes, such as 201 (Created) and 404 (Not Found).

Containerization with Docker:  Used Docker to containerize microservices, enabling consistent and portable deployment across different environments. Docker containers encapsulate  applications, making it easy to manage dependencies and isolate services.

Docker Compose: Docker Compose was used to define and manage multi-container applications. Docker containers made it easy to package and deploy services with all their dependencies. This ensured consistency across different environments and simplified deployment.

The assignment provided me insight into building an end-to-end solution that includes multiple microservices working together to provide a complete service, which is a common scenario in real-world microservices architecture.
