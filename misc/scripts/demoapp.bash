#!/usr/bin/env bash

#  author dingbinthu@163.com
#  time  2019-02-26
#  program : check program running ..

#if [ $# -ne 1 ]; then
#   echo "Usage:$0 "
#   exit 1
#fi

WORKDIR=`dirname $0`
cd $WORKDIR
echo "=====work dir is:"`pwd`",date is:`date`"
runCmd="date && time java -jar -Djava.library.path=`pwd`/lib demoapp.jar "

function check(){
    count=`ps -ef |grep "java.*demoapp" |grep -v "grep"|wc -l`
    if [ 0 == $count ] ; then
       echo ready to execute $runCmd
       eval $runCmd
   else
      echo "$runCmd is RUNNING!!!"
    fi
}
check
