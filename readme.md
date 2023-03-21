## 测试启动命令（256MB堆内存）：
java -jar -server -Xms256M -Xmx256M -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails target/demo-0.0.1-SNAPSHOT.jar