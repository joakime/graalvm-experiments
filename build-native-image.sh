#!/bin/bash

native-image --no-fallback -H:IncludeResources=".*properties$" --class-path target/graalvm-demo.jar:target/dependency/jetty-util-11.0.13.jar:target/dependency/slf4j-api-2.0.5.jar graaldemo.Demo
