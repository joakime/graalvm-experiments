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

## To Run (GraalVM Native-Image)

Prerequisites

	# Obtain GraalVM 22.3 or newer
	# adjust path to GraalVM JVM
	export JAVA_HOME=/Library/Java/JavaVirtualMachines/graalvm-ce-java19-22.3.0/Contents/Home
	export PATH=$JAVA_HOME/bin:$PATH
	# Install native-image support; you may also need to install a C compiler (gcc)
	gu install native-image

Building and running

	mvn clean package -Pnative
	./target/graalvm-experiments-1.0-SNAPSHOT

Resources can only be found if they're included (= off by default).

Best practice is to store a `resource-config.json` file under `src/main/resources/META-INF/native-image/groupId/artifactId/resource-config.json`.

In this demo, the default `resource-config.json` only exports resources under `graaldemo/*`. Replace this file with the one named `resource-config-ALTERNATIVE.json` to include the `build.properties` files from above.

### Remarks

1. Note that URI.toPath.toURI.toPath results in a bogus, non-existant path. Graal bug report [here](https://github.com/oracle/graal/issues/5720)
2. The first call to `Path.of` (or `Paths.get`) with a native-image `resource:` URL may trigger a `FileSystemNotFoundException`, very much like with resources in a jar file. The demo code handles this situation
