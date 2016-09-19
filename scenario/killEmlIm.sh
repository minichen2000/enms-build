#!/bin/sh
NeGroupId=1

if [ $# -eq 1 ]
then
  NeGroupId=$1
else
  echo "Usage: killEmlIm <neGroupID>"
  exit -1
fi

pid=`ps -ef |grep bin\/emlim_$NeGroupId |grep -v grep|awk '{print $2}'`
if [ $pid ]
then
  kill -9 $pid
  echo "kill emlim_$NeGroupId successfully"
else
  echo "emlim_$NeGroupId has been killed already"
fi

