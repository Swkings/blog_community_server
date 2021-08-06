# Bolg Community
## Swagger API 访问路径
+ `Swagger-ui`: http://localhost:8080/api/swagger-ui.html
+ `bootstrap-ui`: http://localhost:8080/api/doc.html
+ `layer-ui`: http://localhost:8080/api/docs.html
+ `mg-ui`: http://localhost:8080/api/document.html

## 启动流程
### 启动Redis
```shell
redis-cli
```
### 启动Kafka
> 在Kafka根目录下启动
#### 启动`zookeeper-server`服务
```shell
zookeeper-server-start.bat config/zookeeper.properties
```
#### 启动`kafka-server`服务
```shell
kafka-server-start.bat config/server.properties
```
### 启动ES服务器
#### linux系统运行
```shell
elasticsearch
```
#### win系统运行
```shell
# 需要加入环境变量
elasticsearch.bat
```