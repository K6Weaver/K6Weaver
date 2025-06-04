# 📌 K6 Weaver - Automated K6 Load Test Script Generator
[![Latest Release](https://img.shields.io/github/v/release/kobenlys/K6Weaver)](https://github.com/kobenlys/K6Weaver)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kobenlys/K6Weaver)](https://central.sonatype.com/artifact/io.github.kobenlys/K6Weaver)
[![License](https://img.shields.io/github/license/kobenlys/K6Weaver)](LICENSE)

#### 🎯 K6Weaver is an open-source Spring Boot library that automatically generates K6 load test scripts!
- Easily perform REST API performance testing with auto-generated K6 scripts.
- Analyze your controller classes and generate corresponding K6 test scripts automatically.

✅ [Maven Central Repository - View Details](https://central.sonatype.com/artifact/io.github.kobenlys/K6Weaver)  
✅ [Maven Repository - View Details](https://mvnrepository.com/artifact/io.github.kobenlys/K6Weaver)  

---

## 🚀 **Features**
✅ Automatically generate K6 test scripts by scanning Spring Boot controllers  
✅ Simple configuration for REST API-based performance testing  

### **🧐 Learn More**
📃 [K6Weaver - Documentation Website](https://k6weaver-docs.vercel.app/)

---

## 📦 **Installation**

### 🔗 Add Dependency
Add the following dependency to your `gradle` or `maven` configuration.

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

### 🔗 Configuration (application.properties or application.yml)

#### application.properties
```properties
k6.weaver.base-url=http://localhost:8080
k6.weaver.base-package=com.ssafy.org
```

#### application.yml
```yml
k6:
  weaver:
    base-url: "http://localhost:8080"
    base-package: "com.ssafy.org"
```

---

## 🛠️ How to Use

### 1️⃣ Exclude specific classes or methods using `@K6Ignore`
```java
@K6Ignore // This endpoint will be excluded from script generation.
@GetMapping("{storeId}/select")
public ApiResponse<List<ProductListResponse>> selectAllProduct(@PathVariable("storeId") Integer storeId) {
  List<ProductListResponse> productList = productService.getListAllProduct(storeId);
  return ApiResponse.success(productList);
}
```

### 2️⃣ Generate K6 Test Script via API Request
Send a GET request to `http://localhost:8080/k6/gen-script` to automatically generate a K6 script.

Modify the generated script to suit your test scenarios by adjusting PathVariables, parameters, and request bodies as needed.

✅ **Request Example**  
![image](https://github.com/user-attachments/assets/1c9f425a-1bfe-41e9-b42b-d0acd4f4185d)

✅ **Response Example**
```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';

const baseUrl = 'http://localhost:8080';

export let options = {
  stages: [
    { duration: "1m", target: 50 },
    { duration: "2m", target: 100 },
    { duration: "1m", target: 0 },
  ],
};

export default function () {
   let mockModifyuserUserIdPayload = null;
   let mockJoinuserPayload = null;
   let params = {
           headers: {
               'Content-Type': 'application/json',
           },
       };
    let res;

    /* ========== com.mockproject.org.controller ========== */
    res = http.put(`${baseUrl}/api/mock/modify-user/{userId}`,mockModifyuserUserIdPayload, params);
    check(res, { 'status was 2xx': (r) => r.status >= 200 && r.status < 300 });

    res = http.post(`${baseUrl}/api/mock/join-user`,mockJoinuserPayload, params);
    check(res, { 'status was 2xx': (r) => r.status >= 200 && r.status < 300 });

    res = http.get(`${baseUrl}/api/mock/search-user`);
    check(res, { 'status was 2xx': (r) => r.status >= 200 && r.status < 300 });

    res = http.del(`${baseUrl}/api/mock/delete-user`);
    check(res, { 'status was 2xx': (r) => r.status >= 200 && r.status < 300 });

    sleep(1);
}
```

---

## ✨ Contributors
<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="16.66%"><a href="https://github.com/kobenlys"><img src="https://avatars.githubusercontent.com/u/149328708?v=4?s=100" width="100px;" alt="Youngseok"/><br /><sub><b>Youngseok</b></sub></a><br /><a href="https://github.com/kobenlys/K6Weaver/commits?author=kobenlys" title="Code">💻</a></td>
      <td align="center" valign="top" width="16.66%"><a href="https://github.com/haazz"><img src="https://avatars.githubusercontent.com/u/127824457?v=4?s=100" width="100px;" alt="haazz"/><br /><sub><b>haazz</b></sub></a><br /><a href="https://github.com/kobenlys/K6Weaver/commits?author=haazz" title="Code">💻</a></td>
      <td align="center" valign="top" width="16.66%"><a href="https://github.com/cod0216"><img src="https://avatars.githubusercontent.com/u/83526046?v=4?s=100" width="100px;" alt="cod0216"/><br /><sub><b>cod0216</b></sub></a><br /><a href="https://github.com/kobenlys/K6Weaver/commits?author=cod0216" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>
<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

---

## 🏆 How to Contribute

1️⃣ If you like this project, please give it a ⭐ star!  
2️⃣ Found a bug or have suggestions? Feel free to open an issue.  
3️⃣ Want to contribute? Fork the repo and submit a pull request.  

---

## 🌐 Available Languages

- [한국어 (Korean)](/i18n/README_ko.md)

---

## 📄 License
This project is licensed under the Apache 2.0 License. 📝
