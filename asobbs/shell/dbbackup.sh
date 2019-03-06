#!/bin/bash

#######################################
##
## データベースバックアップバッチ
##
#######################################

mysqldump -uroot -pAbcc123.# -r /var/asobbs/dbbk/asobbs`date "+%Y%m%d_%H%M%S"`.bakcup --single-transaction asobbs