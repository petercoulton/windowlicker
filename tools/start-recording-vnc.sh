#!/bin/sh

PROG_PATH=$1
OUTPUT_PATH=$2
SERVER=$3

mydir=`dirname $0`
tempdir=`dirname $OUTPUT_PATH`

PASSWD_FILE=$mydir/vnc-password-file

if [ ! -e $tempdir ]
then
    mkdir $tempdir
fi

nohup $PROG_PATH -n -o $OUTPUT_PATH -P $PASSWD_FILE $SERVER > /dev/null 2>&1 &

pid=$!

echo $pid > $tempdir/pid




