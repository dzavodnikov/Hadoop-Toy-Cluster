#!/usr/bin/env bash

source VARS
source ../env.sh

hbase_run pro.zavodnikov.hbase.CreateTable --table tests:users --family personal
hbase_run pro.zavodnikov.hbase.ListTables
sleep 5

hbase_run pro.zavodnikov.hbase.PutRow --table tests:users --key jsmith --family personal --column name --value John Smith
hbase_run pro.zavodnikov.hbase.GetRow --table tests:users --key jsmith --family personal --column name
sleep 5

hbase_run pro.zavodnikov.hbase.ScanTable --table tests:users
sleep 5

hbase_run pro.zavodnikov.hbase.AddTableFamily --table tests:users --family tasks --max-versions 3 --ttl-seconds 10
hbase_run pro.zavodnikov.hbase.PutRow --table tests:users --key jsmith --family tasks --column title --value Task 1
hbase_run pro.zavodnikov.hbase.PutRow --table tests:users --key jsmith --family tasks --column title --value Task 2
hbase_run pro.zavodnikov.hbase.PutRow --table tests:users --key jsmith --family tasks --column title --value Task 3
hbase_run pro.zavodnikov.hbase.ScanTable --table tests:users # We are have 3 tasks.
sleep 4
hbase_run pro.zavodnikov.hbase.PutRow --table tests:users --key jsmith --family tasks --column title --value Task 4
hbase_run pro.zavodnikov.hbase.ScanTable --table tests:users # "Task 1" was removed by versions limit.
sleep 5
hbase_run pro.zavodnikov.hbase.ScanTable --table tests:users # "Task 2" and "Task 3" were removed by TTL.
sleep 5

hbase_run pro.zavodnikov.hbase.ScanTable --table tests:users # "Task 4" also was removed by TTL.
sleep 5

hbase_run pro.zavodnikov.hbase.DeleteRow --table tests:users --key jsmith
hbase_run pro.zavodnikov.hbase.ScanTable --table tests:users
sleep 5

hbase_run pro.zavodnikov.hbase.DropTable --table tests:users
