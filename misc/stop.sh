#!/bin/bash
app_dir=/app/apps_home/apollo
app_name=apollo-api.war
count=`ps -ef | grep ${app_name}| grep -v grep| wc -l|sed 's/ //g'`
pid=`ps -ef | grep -P ${app_name}| grep -v grep| awk '{ print $2}'`
if [ ${count} -gt 0 ]
then
        cd ${app_dir}
        nohup kill -15 `ps -ef | grep ssh| grep 1535|grep -v grep|awk '{ print $2}'
        nohup kill -9 ${pid} 1>>${app_logs}/${app_name}_`date +"%d%m%Y"`.log 2>>${app_logs}/${app_name}_`date +"%d%m%Y"`.err &

        while [ ${count} -gt 0 ]; do
            echo "Stopped psi [pid = ${pid}] waiting for a graceful shutdown to complete at `date +"%d-%m-%Y %H:%m:%S"`" 1>>${app_logs}/${app_name}_`date +"%d%m%Y"`.log 2>>${app_logs}/${app_name}_`date +"%d%m%Y"`.err
            sleep 1
            count=`ps -ef | grep ${app_name}| grep -v grep| wc -l|sed 's/ //g'`
        done
else
        echo "psi-portal is not running !" 1>>${app_logs}/${app_name}_`date +"%d%m%Y"`.log 2>>${app_logs}/${app_name}_`date +"%d%m%Y"`.err

fi