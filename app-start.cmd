@REM @REM %redis启动及其配置 % /k 会打开一个新窗口后执行指令，原窗口不会关闭
@REM start cmd /k "E:\Redis-x64-3.2.100\redis-server.exe E:\Redis-x64-3.2.100\redis.windows.conf"
@REM @REM %zk启动%  /d 程序启动后就会把这个目录当作是默认目录
@REM start /d "C:\Users\tk\Desktop\kafka\kafka\zookeeper-3.4.12\bin" zkServer.cmd
@REM %kafka启动%
@REM start cmd /k "C:\Users\tk\Desktop\kafka\kafka\kafka_2.12-1.1.0\bin\windows\kafka-server-start.bat C:\Users\tk\Desktop\kafka\kafka\kafka_2.12-1.1.0\config\server.properties"


start cmd /k redis-cli
start cmd /k elasticsearch.bat
start cmd /k zookeeper-server-start.bat C:/Environment/Kafka/config/zookeeper.properties
(start cmd /k ping 127.0.0.1:9092 -n 4 > null) && (kafka-server-start.bat C:/Environment/Kafka/config/server.properties)

@REM start cmd /k zookeeper
@REM
@REM start cmd /k "D:\zookeeper-3.4.6\bin\zkServer.cmd"
@REM start cmd /k "ping 127.1 -n "4">nul&&D:\kafka_2.12-0.11.0.0\bin\windows\kafka-server-start.bat D:\kafka_2.12-0.11.0.0\config\server.properties"