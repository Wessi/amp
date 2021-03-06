#!/bin/bash

JAVA_OPTS="-server -Xmx2g -Djava.awt.headless=true"
JAVA_OPTS+=" -DsmtpHost=sulfur -DsmtpFrom=system@digijava.org"
JAVA_OPTS+=" -XX:HeapDumpPath=/opt/heapdumps -XX:+HeapDumpOnOutOfMemoryError"

CATALINA_OPTS="-Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false"
CATALINA_OPTS+=" -Dorg.apache.jasper.compiler.Parser.STRICT_WHITESPACE=false"
CATALINA_OPTS+=" -DAMP_DEVELOPMENT=true"
CATALINA_OPTS+=" -Djavamelody.datasources=java:comp/env/ampDS,java:comp/env/jcrDS"
