# 第一阶段：构建阶段
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# 设置工作目录
WORKDIR /build

# 配置阿里云 Maven 镜像源
RUN echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd"> \
  <mirrors> \
    <mirror> \
      <id>aliyun-maven</id> \
      <mirrorOf>*</mirrorOf> \
      <name>阿里云公共仓库</name> \
      <url>https://maven.aliyun.com/repository/public</url> \
    </mirror> \
  </mirrors> \
</settings>' > /usr/share/maven/conf/settings.xml

# 将 Maven 项目的 pom.xml 和源码复制到容器中
COPY pom.xml ./
COPY src ./src

# 使用 Maven 打包项目，生成 JAR 文件
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段
FROM eclipse-temurin:21-jre

# 创建目录，并使用它作为工作目录
WORKDIR /app

# 从构建阶段复制生成的 JAR 文件到运行阶段镜像中
COPY --from=builder /build/target/app.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai

# 设置 JAVA_OPTS 环境变量，可通过 docker run -e "JAVA_OPTS=" 覆盖
ENV JAVA_OPTS="-Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# 应用参数
ENV ARGS=""

# 暴露后端项目的 8080 端口
EXPOSE 8080

# 启动后端项目
CMD java ${JAVA_OPTS} -jar app.jar $ARGS