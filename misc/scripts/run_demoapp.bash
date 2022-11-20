#!/usr/bin/env bash

#  author dingbinthu@163.com
#  time  2019-02-26
#  program : check program running ..

WORKDIR=`dirname $0`
cd $WORKDIR
nohup bash $PWD/demoapp.bash $1 &>> nohup.out&
