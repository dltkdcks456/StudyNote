# 🛠Maven To Gradle

- Spring Initializer에서 gradle 설정으로 다운
- `pom.xml`에 있던 의존성을 참고해서 `maven repository` 사이트에서 gradle 버전 찾기
- `build.gradle`에 해당 설정 입력

- 저자가 만들어둔 페이지와 자바 클래스를 복사해서 알맞은 경로에 복사해준다.
- WebServer 실행 후 `Hello-world` 출력 확인

```groovy
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.2.2'
	id 'io.spring.dependency-management' version '1.1.4'
}

group = 'com.web-server'
version = '0.0.1-SNAPSHOT'

java {
	sourceCompatibility = '17'
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'

	testImplementation 'ch.qos.logback:logback-classic:1.4.14'
	implementation 'com.google.guava:guava:32.1.3-jre'
	testImplementation 'junit:junit:4.13.2'
}

tasks.named('test') {
	useJUnitPlatform()
}

```

