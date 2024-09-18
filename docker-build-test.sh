#!/bin/bash

build_dir="target"
if [ ! -d $build_dir ]; then
  mvn clean install package -P release || (echo "构建失败" && exit 1)
fi

# ls 列出 target 目录下的文件，提取出.jar 文件，该类型文件只存在一个
jarFile=$(ls target | grep ^xnote-springboot-v.*\.jar$)
if [ -z "$jarFile" ]; then
  echo "未在 target 目录下找到 jar 包"
  exit 1
fi
echo "找到 jar 包：$jarFile"

# 从 jar 文件名读取版本号，jar 文件名格式为 xnote-springboot-v{版本号}.jar
version=$(echo $jarFile | sed -E 's/^xnote-springboot-v([0-9.]+)\.jar$/\1/')
if [ -z "$version" ]; then
  echo "未从 jar 包文件名中读取到版本号"
  exit 1
fi
echo "版本号：$version"

conainerName="xnote-springboot-test"
tag="xnote-springboot:0.0.0"
docker rm -f $conainerName
docker rmi -f $tag
docker build -t $tag . --build-arg JAR_FILE=$jarFile
docker run -d -p 8080:8080 --name $conainerName -v $conainerName:/app $tag