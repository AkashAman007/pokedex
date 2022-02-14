# Pokedex

Pokedex is a SpringBoot JAVA Application running on jetty servers.\
The application leverages Spring Framework to utilize Dependency Injection to create modular applications consisting of loosely coupled components.

## Libraries Used In The Application

1. Spring Boot Starter Web Pack
2. Lombok Annotation Processors
3. Reflections
3. Spring Boot Starter Test Pack
4. Mockito

## Building and Starting the Application

1. Clone Repository
``` bash
git clone https://github.com/AkashAman007/pokedex.git
```
2. For running the application
   
   **a. Using docker**
   - Make sure the docker daemon is running. You can use the `systemctl` command to check the docker status
   - Build Docker Image
   - Run Application
    ```bash
    cd pokedex

    # build docker image
    docker build -t pokedex/pokedex .

    # start the application
    docker run -p 8080:8080 pokedex/pokedex
    ```
   **b. Without docker**
   -  Install JAVA 1.8
   - Set it as your default JDK
   - Run Gradle Build Command
   - Start Application
    ```bash
    cd pokedex

    # Build using gradle with Java 1.8 as default SDK
    ./gradlew build -x test

    # Alternatively you can build by Passing Java 1.8 Path to build command
    ./gradlew build -x test -Dorg.gradle.java.home={JAVA_HOME_PATH}
    
    # start the application using Java 1.8
    java -jar build/libs/pokedex-1.0-SNAPSHOT.jar
    ```
3. The application will start at port `8080` in `foreground`
4. You can tail logs in the logs folder if needed
5. The application is ready to receive request for it's registered Endpoints
6. The application can be killed by simply stopping the process running in foreground

Note - \
The Gradle Version used in the application is 4.10.2 . You don't need to install gradle externally\
The process is started in foreground for dev purpose.

## Testing
The application uses `SpringBootTest` to run a Web Server on a Random Port.\
It registers a `TestRestTemplate` bean to test the application as a fully running server with given endpoints

The Test Application again depends on `Java 1.8`.
Some annotations are not compatible with higher version of Java and would fail if it's not correctly provided.\
Better to pass the Path of Java 1.8 in the environment Variable and then run the test using gradle.
```bash
export JAVA_HOME={Path to Java 1.8 JDK}
# E.g., In Mac OS using Open Jdk 1.8
export JAVA_HOME=/Library/Java/JavaVirtualMachines/openlogic-openjdk-8.jdk/Contents/Home
# Run test using gradle
./gradlew clean test -Dorg.gradle.java.home=$JAVA_HOME
```
This will run all the test that are there in `test` directory
You can further check the test result in `build` folder

For testing, I am using `TestResttemplate` as mentioned above to simulate an API request.\
For fetching data from Upstream Service such as `PokeAPI` or `FunTranslation`, I have stored the actual json data in `test/resources` folder and mocked the external API calls response using `Mockito`


## Project Structure
All the classes are inside **src/** folder
- **controllers**\
  Has Request Mapping for all the endpoints\
  Functions are decorated with relative route path\
  Does Basic Validation on Request\
  `PokemonController` - Has Route mapping for both Get Pokemon and Get Pokemon With Fun Translation

- **services**\
  service class performs business logic on request and returns appropriate response\
  In case of an exception, it throws appropriate `Exception`.\
  `PokeApiService` - Responsible for interacting with External PokeAPI Service using Restful Service\
  `TranslationService` - Responsible for interacting with External Fun Translation Service using Restful Service\
  `PokemonService` Handles Business Logic for fetching pokemon details and translation

- **dto**\
  dto class handles transfer of data using Java Classes.\
  `response` - Has Response Classes for API Request.\
  `model` - Stores upstream data that needs to be mapped to response.\
  `request` - Request Classes for API calls.\
  `mapper` - Mapper Classes that transfers data from model to response

- **exception**\
  Has all the CustomException classes and default Exception handler in case an Uncaught Exception is sent to Controller Class\
  `BusinessException`- Thrown when a Business Logic is not satisfied\
  `ResourceNotFoundException`- Thrown when the Resource that is being requested does not exist\
  `RestExceptionHandler` Is a ControllerAdvice that applies to all Controllers in the application. Used to intercept the Exception and create proper Response based on Exception thrown

- **config**\
  Has common config files\
  `RestTemplateConfig`- Common RestTemplate to make External API calls from the system\
  `RestClientHeaderInterceptor`- Intercepts external API calls made by RestTemplate and modify Request Header

- **util**\
  Has util Classes that cane be commonly used across the project

- **test**\
  Has all the test files

## API Endpoints
1. Get Pokemon Basic Information
   `GET` `/pokedex/pokemon/{name}`
```bash
#cURL Request
curl --location --request GET 'http://localhost:8080/pokedex/pokemon/mewtwo'
```
2. Get Pokemon Information With Fun Translation
   `GET` `/pokedex/pokemon/translated/{name}`
```bash
#cURL Resquest
curl --location --request GET 'http://localhost:8080/pokedex/pokemon/translated/bulbasaur'
```

## Caching
The Service uses Spring's annotation-driven cache management capability, and the cached functions are annotated with
`Cacheable`\
All successful Responses from External Services are cahced as they are quite static
Currenlty a `Cache Eviction Policy` has not been implemented, but can be done using this
This prevents additional API calls for static data

## Logging
Logging is enabled and all the properties of logs `appeneder` and `rolling policy` has been defined in `logback.xml`\
The logs are produced in `logs` directory and any class can have access to logging using `@Sl4j` annotation
The logs can be set at appropriate level
## Production Best Practices
- **Observability** Produce stats from application which is usually done using `statsd` to monitor api latencies, request counts, errors and other metrics. These metrics can be stored in a `Time series Db` and plotted using grafana
- **Caching** Instead of a local cache and in-memory database cache can be used with better cache eviction policy
- **Terraform** To manage infrastructure as code
- **Test Coverage** Rules around minimum Test Coverage
- **API Calls Authentication** Create a custom RestTemplate whose header Interceptor can handle authentication for external API calls and add appropriate Secret Production Key
