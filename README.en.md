<div align="center">
<a href="https://nexfly.xjd2020.com/">
<img src="doc/images/nexfly-logo.webp" width="520" alt="nexfly logo">
</a>
</div>

<p align="center">
  <a href="./README.md">简体中文</a> |
  <a href="./README.en.md">English</a> |
</p>

<p align="center">
    <a href="https://github.com/my-fastcms/nexfly/releases/latest">
        <img src="https://img.shields.io/github/v/release/my-fastcms/nexfly?color=blue&label=Latest%20Release" alt="Latest Release">
    </a>
    <a href="https://nexfly.xjd2020.com" target="_blank">
        <img alt="Static Badge" src="https://img.shields.io/badge/Online-Demo-4e6b99"></a>
    <a href="https://github.com/my-fastcms/nexfly/blob/main/LICENSE">
    <img height="21" src="https://img.shields.io/badge/License-Apache--2.0-ffffff?labelColor=d4eaf7&color=2e6cc4" alt="license">
  </a>
</p>

## 💡 Nexfly: What is it?

Nexfly is an open-source RAG (Retrieval-Augmented Generation) application project built with deep integration of the Spring AI framework. For more information, visit [Nexfly](https://nexfly.cc/).

## 🎮 Online Tryout

You can try it out by visiting [https://nexfly.xjd2020.com](https://nexfly.xjd2020.com).

<div align="center" style="margin-top:20px;margin-bottom:20px;">
![Nexfly](doc/images/nexfly.png)
</div>

## 🌟 Features

### 🍭 Seamless Integration

- Quickly integrate with third-party business systems to enhance AI capabilities.
- Through the integration of the Spring Security framework, easily connect to third-party systems like WeChat and Google that follow the OAuth2 standard.

### 🍱 Deep Document Parsing

- By integrating Spring AI and Tesseract OCR technologies, achieve accurate document segmentation through OCR visual recognition.
- Supports processing various unstructured data formats, including PDF, DOC, PPT, etc.

### 🛀 Flexible RAG Process Configuration

- Fully optimized RAG workflows can support various ecosystems of large enterprises.
- Configurable support for both large language models (LLM) and vector models.

## 🎬 Quick Start

### 📝 Prerequisites

- CPU >= 4 cores
- RAM >= 16 GB
- Docker >= 24.0.0 & Docker Compose >= v2.26.1

If Docker is not installed on your local machine (Windows, Mac, or Linux), refer to the documentation [Install Docker Engine](https://docs.docker.com/engine/install/) for installation instructions.

### 🚀 Start the Server

1. Clone the repository:

   ```bash
   $ git clone https://github.com/my-fastcms/nexfly.git
   or
   $ git clone https://gitee.com/xjd2020/nexfly.git
   ```

2. Navigate to the **docker** folder and start the server using pre-built Docker images:

   ```bash
   $ cd nexfly/docker
   $ docker compose up -d
   ```

   If you want to download and run a specific version of the Docker image, modify the `NEXFLY_VERSION` variable in the `docker/.env` file accordingly.

3. Once the server is successfully started, check the server status:

   ```bash
   $ docker logs -f nexfly-system
   $ docker logs -f nexfly-auth
   $ docker logs -f nexfly-gateway
   ```

   If you see the following output, the server has started successfully:

   ```bash
   Connected to the target VM, address: '127.0.0.1:65520', transport: 'socket'
     _   _                 ______   _
   | \ | |               |  ____| | |
   |  \| |   ___  __  __ | |__    | |  _   _
   | . ` |  / _ \ \ \/ / |  __|   | | | | | |
   | |\  | |  __/  >  <  | |      | | | |_| |
   |_| \_|  \___| /_/\_\ |_|      |_|  \__, |
                                       __/ |
                                       |___/
   ```

4. In your browser, enter the corresponding IP address of your server and log in to Nexfly.
   > In the above example, simply enter http://127.0.0.1 (no need to specify the port if the default HTTP service port 80 is unchanged).

## 🔧 System Configuration

System configuration involves the following files:

- [.env](./docker/.env): Contains basic system environment variables, such as `MYSQL_PASSWORD`, `MINIO_PASSWORD`, Nginx port, etc.
- [docker-compose.yml](./docker/docker-compose.yml): This file is essential for system startup.

If you need to change the default HTTP service port (80), modify the `nexfly-web` port configuration in [docker/docker-compose.yml] from `80:80` to `<YOUR_SERVING_PORT>:80`.

> All system configurations require a system restart to take effect:
>
> ```bash
> $ docker compose up -d
> ```

## 🛠️ Building and Installing Docker Images from Source

If you need to install Docker images from source:

```bash
$ git clone https://github.com/my-fastcms/nexfly.git 
or
$ git clone https://gitee.com/xjd2020/nexfly.git

$ cd nexfly
$ mvn install:install-file -Dfile=./nexfly-common/rocketmq-common/lib/canal-glue-core.jar -DgroupId=cn.throwx -DartifactId=canal-glue-core -Dversion=1.0 -Dpackaging=jar
$ mvn clean
$ mvn package

$ cd nexfly/nexfly-gateway
$ docker build -t wangjun/nexfly-gateway:0.0.1 .

$ cd nexfly/nexfly-auth
$ docker build -t wangjun/nexfly-auth:0.0.1 .

$ cd nexfly/nexfly-system
$ docker build -t wangjun/nexfly-system:0.0.1 .

$ git clone https://gitee.com/xjd2020/nexfly-ui.git
$ cd nexfly-ui
$ docker build -t wangjun/nexfly-web:0.0.1 .

$ cd nexfly/docker
$ docker compose up -d
```
> Note: 0.0.1 is the version number; you can tag your own version during the image build.

## 🛠️ Starting Services from Source

To start services from source, follow these steps:

1. Clone the repository:

```bash
$ git clone https://github.com/my-fastcms/nexfly.git
$ cd nexfly/docker
```

2. Install JDK (version 17 or higher).

3. Install Maven (version 3.6.3 or higher).

Use the following commands to compile the project:

```bash
$ cp nexfly
$ mvn clean
$ mvn package
```

4. Modify hosts (for Windows).

Open the hosts file and add the following configurations:

```bash
127.0.0.1 nexfly-minio
127.0.0.1 nexfly-redis
127.0.0.1 nexfly-elasticsearch
127.0.0.1 nexfly-mysql
127.0.0.1 nexfly-rocketmq-namesrv
127.0.0.1 nexfly-rocketmq-broker
```

5. Start the services.

Navigate to the `nexfly-gateway` directory and run the main method to start the gateway service:

```java
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {
   public static void main(String[] args) {
      SpringApplication.run(GatewayApplication.class, args);
   }
}
```

Then, navigate to the `nexfly-auth` directory and run the main method to start the auth service:

```java
@SpringBootApplication(scanBasePackages = { "com.nexfly" })
@EnableFeignClients(basePackages = {"com.nexfly.api.**.feign"})
public class AuthApplication {
   public static void main(String[] args) {
      SpringApplication.run(AuthApplication.class, args);
   }
}
```

Next, go to the `nexfly-system` directory and run the main method to start the system service:

```java
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.nexfly"})
@MapperScan({ "com.nexfly.**.mapper" })
public class SystemApplication {
   public static void main(String[] args) {
      SpringApplication.run(SystemApplication.class, args);
   }
}
```

6. Start the UI service.

```bash
$ git clone https://gitee.com/xjd2020/nexfly-ui.git
$ cd nexfly-ui
$ npm install --registry=https://registry.npmmirror.com --force
$ npm run dev 
```

## 🏄 Acknowledgements

- [RAGFlow](https://github.com/my-fastcms/ragflow.git)

## 🙌 Open Source Contributions

Nexfly can thrive only through open collaboration. We welcome various contributions from the community.

## 👥 Communication

Scan the QR code to add WeChat and join the Nexfly group. Please note "nexfly" when adding.

<p align="center">
  <img src="doc/images/wechat.jpg" width=50% height=50%>
</p>
