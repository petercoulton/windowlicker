#!/bin/sh

OUTPUT_PATH=$1
tempdir=`dirname $OUTPUT_PATH`
pid=`cat $tempdir/pid`
kill -INT $pid
