# MicroJSON

JSON tiny library written in modern Java.

![Java CI with Gradle](https://github.com/AlmazKo/microjson/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=ci-init)
[![codecov](https://codecov.io/gh/AlmazKo/microjson/branch/master/graph/badge.svg)](https://codecov.io/gh/AlmazKo/microjson)

### Pros:
- Tiny library(3 small classes only!), ~5KB in a jar
- Fast JSON parsing, it faster than Jackson more than 25%
- Bundled as java9 module without any JDK dependencies
- Support the full JSON specification

### Cons:
- Do not support data binding
- Do not validate JSON according spec, optimistic parsing, fail-fast
- No extensions



## Example:

```java
var user = JsonParser.parseObject(Files.readString(Path.of("my.json"));
String name = user.getString("name");
int age = user.getNumber("age").intValue();
JsArray tags = user.getArray("tags");
String tag = tags.getString(0);
```
