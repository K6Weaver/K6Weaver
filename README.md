# 📌 K6 Weaver - K6 부하 테스트 스크립트 자동 생성기
[![Latest Release](https://img.shields.io/github/v/release/kobenlys/K6Weaver)](https://github.com/kobenlys/K6Weaver)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kobenlys/K6Weaver)](https://central.sonatype.com/artifact/io.github.kobenlys/K6Weaver)
[![License](https://img.shields.io/github/license/kobenlys/K6Weaver)](LICENSE)

#### 🎯 K6Weaver는 K6 부하 테스트 스크립트를 자동 생성하는 Spring Boot 오픈소스 라이브러리입니다!
- 자동화된 K6 스크립트 생성으로 REST API 성능 테스트를 손쉽게 수행하세요.
- 컨트롤러를 분석하여 K6 부하 테스트 코드를 자동으로 생성합니다.

✅ [Maven Central Repository - 상세 정보](https://central.sonatype.com/artifact/io.github.kobenlys/K6Weaver) <br>
✅ [Maven Repository - 상세 정보](https://mvnrepository.com/artifact/io.github.kobenlys/K6Weaver)

---

## 🚀 **특징**
✅ **Spring Boot 컨트롤러 스캔** 후 자동으로 K6 테스트 스크립트 생성<br>
✅ **REST API** 기반 부하 테스트를 위한 손쉬운 설정


---

## 📦 **설정 방법**
### 🔗 의존성 설정
- gradle, maven 라이브러리 의존성 추가하기. <br>

#### **Gradle**
```properties
dependencies {
    implementation 'io.github.kobenlys:K6Weaver:1.0.1-BETA'
}
```
#### **Maven**
```xml
<dependency>
    <groupId>io.github.kobenlys</groupId>
    <artifactId>K6Weaver</artifactId>
    <version>1.0.1-BETA</version>
</dependency>
```

### 🔗 application.properties, application.yml 파일 설정
- 아래와 같이 application.properties 또는 application.yml 파일을 설정하세요.

#### application.properties
```properties
k6.weaver.base-url=http://localhost:8080 // 테스트 할 base-url
k6.weaver.base-package=com.ssafy.org // 프로젝트 패키지 경로
```
#### application.yml
```yml
k6:
  weaver:
    base-url: "http://localhost:8080"
    base-package: "com.ssafy.org"
```


---

## 🛠️ 사용 방법

### 1️⃣ 특정 클래스 또는 메서드를 제외하려면 @K6Ignore 애노테이션을 사용하세요.
```java
@K6Ignore // 해당 Endpoint를 Script에서 제외합니다.
@GetMapping("{storeId}/select")
public ApiResponse<List<ProductListResponse>> selectAllProduct(@PathVariable("storeId") Integer storeId) {
  List<ProductListResponse> productList = productService.getListAllProduct(storeId);
  return ApiResponse.success(productList);
}
```

### 2️⃣ API 요청으로 K6 테스트 스크립트 자동 생성
- http://localhost:8080/k6/gen-script 엔드포인트로 GET 요청을 보내면, 자동으로 K6 테스트 코드가 생성됩니다.
- 생성된 스크립트에서 **PathVariable**과 **Parameter, Payload** 를 테스트 시나리오에 맞게 수정하여 사용하세요. ✨

✅ **요청 예시** <br>
![image](https://github.com/user-attachments/assets/1c9f425a-1bfe-41e9-b42b-d0acd4f4185d)


✅ **응답 예시** <br>

```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';

// Here write your base URL
const baseUrl = 'http://localhost:8080';

export let options = {
  stages: [
    // Custom Test Scenario with ramp-up, peak load, and ramp-down!
    { duration: "1m", target: 50 },
    { duration: "2m", target: 100 },
    { duration: "1m", target: 0 },
  ],
};

export default function () {
   // write body here! 
   let mockModifyuserUserIdPayload = /* write body here! */ null;
   let mockJoinuserPayload = /* write body here! */ null;
   let params = {
           headers: {
               'Content-Type': 'application/json',
           },
       };
    let res;

    /* ========== com.mockproject.org.controller ========== */
    // Put method
    res = http.put(`${baseUrl}/api/mock/modify-user/{userId}`,mockModifyuserUserIdPayload, params);
    check(res, { 'status was 2xx': (r) => r.status >= 200 && r.status < 300 });

    // Post method
    res = http.post(`${baseUrl}/api/mock/join-user`,mockJoinuserPayload, params);
    check(res, { 'status was 2xx': (r) => r.status >= 200 && r.status < 300 });

    // Get method
    res = http.get(`${baseUrl}/api/mock/search-user`);
    check(res, { 'status was 2xx': (r) => r.status >= 200 && r.status < 300 });

    // Delete method
    res = http.del(`${baseUrl}/api/mock/delete-user`);
    check(res, { 'status was 2xx': (r) => r.status >= 200 && r.status < 300 });
    sleep(1);
}

```

--- 
## ✨ 기여자들
<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="16.66%"><a href="https://github.com/kobenlys"><img src="https://avatars.githubusercontent.com/u/149328708?v=4?s=100" width="100px;" alt="영석"/><br /><sub><b>영석</b></sub></a><br /><a href="https://github.com/kobenlys/K6Weaver/commits?author=kobenlys" title="Code">💻</a></td>
      <td align="center" valign="top" width="16.66%"><a href="https://github.com/haazz"><img src="https://avatars.githubusercontent.com/u/127824457?v=4?s=100" width="100px;" alt="haazz"/><br /><sub><b>haazz</b></sub></a><br /><a href="https://github.com/kobenlys/K6Weaver/commits?author=haazz" title="Code">💻</a></td>
      <td align="center" valign="top" width="16.66%"><a href="https://github.com/cod0216"><img src="https://avatars.githubusercontent.com/u/83526046?v=4?s=100" width="100px;" alt="cod0216"/><br /><sub><b>cod0216</b></sub></a><br /><a href="https://github.com/kobenlys/K6Weaver/commits?author=cod0216" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

---
## 🏆 기여 방법

1️⃣ 이 프로젝트가 마음에 든다면 ⭐ Star를 눌러주세요! <br>
2️⃣ 버그나 개선 사항을 발견하셨다면 이슈(issue) 를 등록해 주세요.<br>
3️⃣ 직접 기여하고 싶다면 Fork 후 PR을 보내 주세요!<br>

---
## 📄 라이선스
이 프로젝트는 Apache 2.0 라이선스를 따릅니다. 📝
- [25-03-02]




