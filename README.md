# Experiments with GraalVM behaviors

This is just a simple project to test behaviors with GraalVM in regard to `resource://path/to/blah` Path access.

## To build

```
$ mvn clean package
```

## To Run (Classpath)

Normal run: Adoptium 17.0.5 (Classpath)

```shell
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

```shell
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

### Prerequisites

Download and unpack GraalVM

https://www.graalvm.org/downloads/

Make it your default JVM

```shell
$ echo $JAVA_HOME
/home/joakim/java/jvm/graalvm-17
$ java --version
openjdk 17.0.5 2022-10-18
OpenJDK Runtime Environment GraalVM CE 22.3.0 (build 17.0.5+8-jvmci-22.3-b08)
OpenJDK 64-Bit Server VM GraalVM CE 22.3.0 (build 17.0.5+8-jvmci-22.3-b08, mixed mode, sharing)
```

Install the `native-image` support

```shell
$ gu install native-image
Downloading: Component catalog from www.graalvm.org
Processing Component: Native Image
Downloading: Component native-image: Native Image from github.com
Installing new component: Native Image (org.graalvm.native-image, version 22.3.0)
```

Install Native Build Tool Prerequisites

https://www.graalvm.org/latest/reference-manual/native-image/#prerequisites

### Build a native image (Classpath)

```shell
$ native-image --class-path target/graalvm-demo.jar:target/dependency/jetty-util-11.0.13.jar:target/dependency/slf4j-api-2.0.5.jar graaldemo.Demo
========================================================================================================================
GraalVM Native Image: Generating 'graaldemo.demo' (executable)...
========================================================================================================================
[1/7] Initializing...                                                                                    (3.4s @ 0.20GB)
 Version info: 'GraalVM 22.3.0 Java 17 CE'
 Java version info: '17.0.5+8-jvmci-22.3-b08'
 C compiler: gcc (linux, x86_64, 11.3.0)
 Garbage collector: Serial GC
[2/7] Performing analysis...  [*****]                                                                    (5.5s @ 0.59GB)
   2,853 (73.32%) of  3,891 classes reachable
   3,432 (50.87%) of  6,746 fields reachable
  12,564 (42.34%) of 29,671 methods reachable
      28 classes,     0 fields, and   314 methods registered for reflection
      58 classes,    58 fields, and    52 methods registered for JNI access
       4 native libraries: dl, pthread, rt, z
[3/7] Building universe...                                                                               (1.0s @ 1.09GB)

Warning: Resource access method java.lang.ClassLoader.getResource invoked at graaldemo.Demo.main(Demo.java:14)
Warning: Aborting stand-alone image build due to accessing resources without configuration.
Warning: Use -H:+ReportExceptionStackTraces to print stacktrace of underlying exception
------------------------------------------------------------------------------------------------------------------------
                        0.3s (3.1% of total time) in 14 GCs | Peak RSS: 1.84GB | CPU load: 12.78
========================================================================================================================
Failed generating 'graaldemo.demo' after 10.2s.
Generating fallback image...
Warning: Image 'graaldemo.demo' is a fallback image that requires a JDK for execution (use --no-fallback to suppress fallback image generation and to print more detailed information why a fallback image was necessary).
```

Not sure how to get past this fallback image behavior.

TODO: Document how to package/run this demo so that GraalVM `resource://path/to/blah` is available.

