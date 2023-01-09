# Experiments with GraalVM behaviors

This is just a simple project to test behaviors with GraalVM in regard to `resource://path/to/blah` Path access.

## To build

```
$ mvn clean package
```

## To Run (Classpath)

Normal run: Adoptium 17.0.5 (Classpath)

```
$ java -cp target/graalvm-demo.jar:target/dependency/jetty-util-11.0.13.jar:target/dependency/slf4j-api-2.0.5.jar \
  graaldemo.Demo
```

Example Output:

```
-- Looking for: org/eclipse/jetty/version/build.properties
FOUND: jar:file:/home/joakim/code/jetty/github/graalvm-experiments/target/dependency/jetty-util-11.0.13.jar!/org/eclipse/jetty/version/build.properties
-- Looking for: PathResource.class
FOUND: jar:file:/home/joakim/code/jetty/github/graalvm-experiments/target/dependency/jetty-util-11.0.13.jar!/org/eclipse/jetty/util/resource/PathResource.class
```

## To Run (Modulepath)

Normal run: Adoptium 17.0.5 (Modulepath)

```
$ java -p target/graalvm-demo.jar:target/dependency/jetty-util-11.0.13.jar:target/dependency/slf4j-api-2.0.5.jar \
  -m graalvm.experiments/graaldemo.Dem
```

Example Output ...

```
-- Looking for: org/eclipse/jetty/version/build.properties
WARNING: Unable to find resource: org/eclipse/jetty/version/build.properties
-- Looking for: PathResource.class
FOUND: jar:file:///home/joakim/code/jetty/github/graalvm-experiments/target/dependency/jetty-util-11.0.13.jar!/org/eclipse/jetty/util/resource/PathResource.class
```

Under module-path, it cannot find the `build.properties` due to the `module-info.class` in the `jetty-util-11.0.13.jar` rules.
This is expected behavior in this mode.

## To Run (GraalVM)

TODO: Document how to package/run this demo so that GraalVM `resource://path/to/blah` is available.

