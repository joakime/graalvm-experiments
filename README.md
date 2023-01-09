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
$ native-image \
  --no-fallback \
  -H:IncludeResources=".*properties$" \
  --class-path target/graalvm-demo.jar:target/dependency/jetty-util-11.0.13.jar:target/dependency/slf4j-api-2.0.5.jar \
  graaldemo.Demo
========================================================================================================================
GraalVM Native Image: Generating 'graaldemo.demo' (executable)...
========================================================================================================================
[1/7] Initializing...                                                                                    (3.5s @ 0.19GB)
 Version info: 'GraalVM 22.3.0 Java 17 CE'
 Java version info: '17.0.5+8-jvmci-22.3-b08'
 C compiler: gcc (linux, x86_64, 11.3.0)
 Garbage collector: Serial GC
[2/7] Performing analysis...  [*****]                                                                    (5.7s @ 0.58GB)
   2,853 (73.32%) of  3,891 classes reachable
   3,432 (50.87%) of  6,746 fields reachable
  12,564 (42.34%) of 29,671 methods reachable
      28 classes,     0 fields, and   314 methods registered for reflection
      58 classes,    58 fields, and    52 methods registered for JNI access
       4 native libraries: dl, pthread, rt, z
[3/7] Building universe...                                                                               (1.1s @ 1.09GB)
[4/7] Parsing methods...      [*]                                                                        (0.6s @ 1.72GB)
[5/7] Inlining methods...     [***]                                                                      (0.3s @ 0.59GB)
[6/7] Compiling methods...    [**]                                                                       (3.3s @ 1.20GB)
[7/7] Creating image...                                                                                  (1.3s @ 1.60GB)
   3.98MB (32.02%) for code area:     7,194 compilation units
   7.92MB (63.67%) for image heap:  100,551 objects and 115 resources
 549.34KB ( 4.31%) for other data
  12.44MB in total
------------------------------------------------------------------------------------------------------------------------
Top 10 packages in code area:                               Top 10 object types in image heap:
 657.63KB java.util                                            1.16MB byte[] for embedded resources
 325.94KB java.lang                                          928.63KB java.lang.String
 264.68KB java.text                                          889.07KB byte[] for code metadata
 216.40KB java.util.regex                                    849.71KB byte[] for general heap data
 194.60KB java.util.concurrent                               625.96KB java.lang.Class
 146.96KB java.math                                          560.70KB byte[] for java.lang.String
 116.72KB java.lang.invoke                                   482.39KB java.util.HashMap$Node
 114.85KB com.oracle.svm.core.genscavenge                    231.13KB java.util.HashMap$Node[]
  94.83KB java.util.stream                                   222.89KB com.oracle.svm.core.hub.DynamicHubCompanion
  94.03KB java.util.logging                                  164.20KB java.lang.String[]
   1.76MB for 121 more packages                                1.59MB for 771 more object types
------------------------------------------------------------------------------------------------------------------------
                        0.4s (2.2% of total time) in 17 GCs | Peak RSS: 3.34GB | CPU load: 16.67
------------------------------------------------------------------------------------------------------------------------
Produced artifacts:
 /home/joakim/code/jetty/github/graalvm-experiments/graaldemo.demo (executable)
 /home/joakim/code/jetty/github/graalvm-experiments/graaldemo.demo.build_artifacts.txt (txt)
========================================================================================================================
```

TIP: You can use `./build-native-image.sh` as well.

This produces a binary at `/graaldemo.demo`, executing it results in ...

```shell
$ ./graaldemo.demo
-- Looking for: org/eclipse/jetty/version/build.properties
FOUND: resource:/org/eclipse/jetty/version/build.properties
java.nio.file.FileSystemNotFoundException: Unable to open URI directly, switching to mount version.
Mounting: resource:/org/eclipse/jetty/version/build.properties
-- root of mount
Path: /
Path.toURI: resource:file:///resources!/
Path.getFileSystem.className: (com.oracle.svm.core.jdk.resources.NativeImageResourceFileSystem)
Files.isDirectory(path): true
Files.isRegularFile(path): false
Files.isReadable(path): true
Files.exists(path): true
Files.size(path): 0
Files.getLastModifiedTime(path): 2023-01-09T17:32:01.829Z
-- Specific URI
Path: /org/eclipse/jetty/version/build.properties
Path.toURI: resource:file:///resources!/org/eclipse/jetty/version/build.properties
Path.getFileSystem.className: (com.oracle.svm.core.jdk.resources.NativeImageResourceFileSystem)
Files.isDirectory(path): false
Files.isRegularFile(path): true
Files.isReadable(path): true
Files.exists(path): true
Files.size(path): 163
Files.getLastModifiedTime(path): 2023-01-09T17:32:01.829Z
-- Looking for: PathResource.class
WARNING: Unable to find PathResource.class
```

This is the `resource:/` based URL that we are looking for.