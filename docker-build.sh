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

tag=ghcr.io/xclhove/xnote-springboot:latest
docker rmi -f $tag
docker build -t $tag . --build-arg JAR_FILE=$jarFile
echo $GH_PAT | docker push $tag