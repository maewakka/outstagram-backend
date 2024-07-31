## Outstagram Backend
아웃스타그램 백엔드

### 개발 환경
- **Java** : JDK 17
- **Spring Boot** : 3.1.7
- **DataBase** : Oracle 21c
- **ETC** : JPA, Minio, WebSocket, Spring Security, Jwt

### 프로젝트 구조
```
outstagram-backend/
└── src/
    └── main/
        ├── java/
        │   └── com/woo/outstagram/
        │       ├── config/
        │       ├── controller/
        │       ├── dto/
        │       │── entity/
        │       │── repository/
        │       │── service/
        │       └── util/
        └── resources/
            └── application.yml
```

#### `com.woo.outstagram.config`
이 패키지는 어플리케이션의 설정들과 관련된 클래스들을 포함합니다.

#### `com.woo.outstagram.controller`
이 패키지는 어플리케이션의 Rest 컨트롤러와 관련된 클래스들을 포함합니다.

#### `com.woo.outstagram.dto`
이 패키지는 어플리케이션의 DTO 클래스들을 포함합니다.

#### `com.woo.outstagram.entity`
이 패키지는 어플리케이션의 JPA 엔티티 클래스들을 포함합니다.

#### `com.woo.outstagram.repository`
이 패키지는 어플리케이션의 JPA Repository 클래스들을 포함합니다.

#### `com.woo.outstagram.service`
이 패키지는 어플리케이션의 비즈니스 로직을 담당하는 서비스 클래스들을 포함합니다.

#### `com.woo.outstagram.util`
이 패키지는 어플리케이션의 유틸 클래스들을 포함합니다.


### 배포
1. Gradle로 해당 프로젝트를 빌드한다.
```
./gradlew clean build
```
2. 빌드로 나온 App.jar를 실행시키는 도커 컨테이너 이미지를 생성한다.
```
FROM openjdk:17.0

ENV TZ=Asia/Seoul

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

COPY src/main/resources/application.yml /config/application.yml

CMD ["java", "-jar", "app.jar", "--spring.config.location=file:/config/application.yml"]
```
3. 해당 이미지를 쿠버네티스 환경에 배포한다.
```
kubectl apply -f deployment.yaml -f service.yaml -f config.yaml
```