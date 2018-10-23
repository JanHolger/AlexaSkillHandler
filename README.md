# AlexaSkillHandler
A simple handler library for fast and simple skill development in java.

## How to use
### Creating your application
Create a new instance of SkillPipeline, add your SkillHandler's and pipe the verified json requests from Amazon via the methode `pipe(String)` into your SkillPipeline object.
```java
SkillPipeline myPipeline = new SkillPipeline();
myPipeline.addHandler(new SkillHandler(){
  public SkillResponse handle(SkillRequest request){
    if(request.getRequest().getType()==SkillRequest.RequestType.IntentRequest){
      if(request.getRequest().getIntent().getName().equals("YourIntent")){
        return SkillResponse.builder().speech("Hello World").build();
      }
    }
    return null;
  }
});
String jsonResponse = myPipeline.pipe(jsonRequest);
```
### Building your application
To build your application you need to add this library and google's gson library to the build path and include both into your built application. The minimum required language level is 8. You can let maven automatically manage the required dependencies for you.
```xml
<repositories>
  <repository>
    <id>bebendorf</id>
    <url>http://repo.bebendorf.eu/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>eu.bebendorf</groupId>
    <artifactId>AlexaSkillHandler</artifactId>
    <version>1.0</version>
    <scope>compile</scope>
  </dependency>
  <dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.2</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```
## Example using SparkJava
```java
import static spark.Spark.*;

public class Example {
    public static void main(String [] args){
        SkillPipeline exampleSkill = new SkillPipeline(); //Initialize the pipeline
        exampleSkill.addHandler(new SkillHandler() { //Add the handler
            public SkillResponse handle(SkillRequest request) {
                if(request.getRequest().getType()!=SkillRequest.RequestType.IntentRequest) //Only handle the IntentRequest request type
                    return null;
                SkillRequest.Intent intent = request.getRequest().getIntent();
                if(!intent.getName().equals("AddIntent")) //Only handle the AddIntent
                    return null;
                if(!(intent.verifySlot("addend1")&&intent.verifySlot("addend2"))) //Check if both slots are given and have a value
                    return SkillResponse.builder().speech("You have to give me 2 numbers!").build();
                int a1 = intent.getSlot("addend1").asInt();
                int a2 = intent.getSlot("addend2").asInt();
                int sum = a1 + a2;
                return SkillResponse.builder().speech(a2+" added to "+a1+" equals "+sum).build(); //Build and return the response
            }
        });
        post("/",(request, response) -> { //Map the post request
            // <-- HERE YOU SHOULD VERIFY THE REQUEST IS VALID AND AUTHORIZED
            return exampleSkill.pipe(request.body()); //Pipe the request into the pipeline
        });
        init(); //Initialize the webserver
    }
}
```
## Used libraries
* Google Gson (https://github.com/google/gson)
