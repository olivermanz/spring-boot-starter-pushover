# Spring Boot Starter for Pushover

**NOTE:** I am currently working on deploying this to maven central. Until then, I would kindly ask you to build the project 
from sources for now. 

## Pushover
Pushover is a service for sending push messages to end devices like smartphone. For details refer to their website 
at https://www.pushover.net. This project basically adds a service for sending message over the pushover REST API to your spring boot application. For this 
to work you need to register your application with Pushover and obtain an application token. 

This projects API should be pretty stable, but changed might occur. It is currently based on spring boot 2.7.2.

## Adding and using the starter. 

### Add the dependency
In maven, for example, add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>de.omanz</groupId>
    <artifactId>spring-boot-starter-pushover</artifactId>
    <version>0.0.1</version>
</dependency> 
```

### Properties

| Property                    | Meaning                                            | Default Value  |
|-----------------------------|----------------------------------------------------|----------------|
| spring.pushover.schema      | Schema to use for HTTP connection to Pushover API | https
| spring.pushover.host        | Host to use for HTTP connection to Pushover API   | api.pushover.net
| spring.pushover.port        | Port to use for HTTP connection to Pushover API   | 443
| spring.pushover.messagepost | Endpoint for POSTing messages                     | 1/messages.json
| spring.pushover.token       | Application token                                 | -

**spring.pushover.token needs to be set with your application token.**

### Usage

Let Spring autowire the PushoverService to your bean.

```java
import de.omanz.pushover.spring.service.PushoverService;
import org.springframework.stereotype.Component;

@Component
public class MyComponent {

    private final PushoverService pushoverService;
    
    public MyComponent(PushoverService pushoverService) {
        this.pushoverService=pushoverService;
    } 

}
```

Pushover basically defines three methods for sending messages.
 * single user
 * multiple users
 * a group

Each of these methods is associated with a request class. The actual message - the payload - is 
the same for all of them:  

```
     PushoverImage pushoverImage = PushoverImage.builder()
                .imageFileName("myImage.jpeg")
                .rawContent(byteArray)
                .mediaType("image/jpeg")
                .build();
     
     PushoverMessage pushoverMessage = PushoverMessage.builder()
                .messageTitle(messageTitle)
                .message(message)
                .type(PushoverMessageType.TEXT)
                .priority(PushoverPriority.NORMAL)
                .attachedImage(pushoverImage)
                .sound(PushoverSound.INCOMING)
                .displayedTime(Instant.now())
                .build();

```
Please see Pushover API documentation for the field description and limitations (https://pushover.net/api)
#### Sending a message to a single user
Besides the user a list of devices can be set to identify to which devices the 
request should be sent. If no devices are defined the message will be sent to all registered devices of the user.

```
    SingleUserPushoverRequest.builder()
        .userKey(userKey)
        .devices(listOfDevices)
        .message(pushoverMessage)
        .build();
```
#### Sending a message to multiple users
Will always be sent to all devices. A maximum of 50 users can be added to the list. 
```
    MultiUserPushoverRequest.builder()
        .userKeys(listOfUsers)
        .message(pushoverMessage)
        .build();
```
#### Sending a group
Group has to be defined in Pushover and a group key must be obtained. Please refer to pushover documentation for details. 
```
    GroupPushoverRequest.builder()
         .groupKey(groupKey)
         .message(pushoverMessage)
         .build();
```

### Response

The service is blocking and will return a response containing the following information:

| Field             | Meaning |
|-------------------|---|
| status            | Whether or not the message could be sent. |  
 | errors            | A list of errors returned from the Pushover API |
 | appLimitTotal     | The total number of request allowed within the specified period of time. Normally a calendar month.  | 
 | appLimitRemaining | Messages remaining for the current period. |
 | appLimitReset | The point in time when the current consumption will be reset. | 

### Error Handling

4xx errors are handled within the client and will lead to a response with 
the status set to ERROR. All other errors are propagated by a RecoverablePushoverClientException. 

### Rest Template
The starter defines a rest template for usage within the PushoverClient (@Qualifier("pushover")). If you override please
make sure the rules for error handling are followed, meaning 2xx and 4xx must not throw
an exception.

## Limitations
 * Currently only the sending of message if supported.
 * The Priority EMERGENCY defines a slightly more complex workflow that includes a receipt for the message. This can not be done right now but is next on the list.  

## License
This project is licensed under MIT License. 