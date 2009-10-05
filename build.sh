#!/bin/bash

mydir=`dirname $0`

export ANT_HOME=$mydir/tools/apache-ant-1.7.0
export ANT_OPTS="-Xmx1024M"

if [ -z "$env" ]
then
    env=localdev
fi

$ANT_HOME/bin/ant $BUILD_OPTS -f $mydir/build.xml "$@"

