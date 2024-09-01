#! /bin/bash

# 设置脚本在执行过程中遇到错误时立即退出
set -e

# 下载gzhuoj-commons-parent到本地maven
cd gzhuoj-commons-parent
mvn clean install -DskipTests
cd ..

# 下载gzhuoj-sdk到本地maven
cd gzhuoj-sdk
mvn clean install -DskipTests
cd ..

# 打包 gateway
echo "Building gzhuoj-gateway..."
cd gzhuoj-gateway
mvn clean package -DskipTests
cd ..

# 打包4个微服务
for service in gzhuoj-services/*; do
  if [ -d "$service" ]; then
    echo "Building $service..."
    cd $service
    mvn clean package -DskipTests
    cd ../..
  fi
done

# 构建Docker 镜像并启动容器
echo "Building Docker images and starting services..."
docker-compose up --build -d

echo "All services are up and running"