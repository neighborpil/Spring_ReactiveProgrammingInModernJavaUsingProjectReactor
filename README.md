# Spring_ReactiveProgrammingInModernJavaUsingProjectReactor

### Official site and documents
code for training

https://projectreactor.io/

https://projectreactor.io/docs/core/release/reference/

### Course github that I enrolled

https://github.com/dilipsundarraj1/reactive-programming-using-reactor

### Flux document

https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html


### Mono document

https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html


### Cold Stream
 - all the subscribers get the same data
 
### Hot stream
 - any new subscribers get the current data
 - examples
    + stock tickers
    + uber driver ticketing

### swagger link
 - http://localhost:8080/movies/webjars/swagger-ui/index.html?configUrl=/movies/v3/api-docs/swagger-config
 
### Reactor debuuging tool dependency
 - guide line : https://projectreactor.io/docs/core/release/reference/#reactor-tools-debug
 
 - maven
```
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-tools</artifactId>
    
</dependency>

```
 - gradle
 
 ```
 dependencies {
   compile 'io.projectreactor:reactor-tools'
}
 ```
 
 ### ReactorDebugAgent
  - main thread에 영향을 주지 않으면서 디버깅 하는 방법이다
  - spring boot application의 main 메소드에 선언이 필요하다
 
 ```
 public static void main (String[] main) {
 
  ReactorDebugAgent.init();
  
  SpringApplication.run(Application.class, args);
 }
 ```
  
