# MicroJSON

JSON tiny library written in modern Java.

![Build](https://github.com/AlmazKo/microjson/workflows/Build/badge.svg)
[![codecov](https://codecov.io/gh/AlmazKo/microjson/branch/master/graph/badge.svg)](https://codecov.io/gh/AlmazKo/microjson)
[ ![Download](https://api.bintray.com/packages/almazko/micro/microjson/images/download.svg) ](https://bintray.com/almazko/micro/microjson/_latestVersion)

### Abilities:
- Tiny library(3 small classes only!), ~6KB in a jar
- Parse JSON faster than all exist libraries. See [benchmarks](https://github.com/AlmazKo/microjson/wiki/Compare-performance-of-JSON-parsers)
- No strict JSON validation, optimistic parsing, fail-fast 
- Bundled as Java 9 module without dependencies
- No garbage, allocate only data
- Dynamically adopt json data to the closest java analogues
- Required Java 9+


### Example:

```java
var content = """
{ 
  "name": "Bob",
  "age": 29,
  "traits": ["lazy", "optimistic", "developer"] 
}
""";
var user = Json.parseObject(content);
var name = user.getString("name");
var age = user.getNumber("age").intValue();
var tag = user.getArray("traits").getString(0);
```
