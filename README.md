# MicroJSON

JSON tiny library written in modern Java.

![Build](https://github.com/AlmazKo/microjson/workflows/Build/badge.svg)
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
var content = Files.readString(Path.of("my.json")
var user = Json.parseObject(content);
var name = user.getString("name");
var age = user.getNumber("age").intValue();
var tag = user.getArray("tags").getString(0);
```
