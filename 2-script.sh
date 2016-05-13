#!/bin/bash

./server-script.sh --port 6000 &
./server-script.sh --port 6001 &

# give the servers time to start up
sleep 1

./client-script.sh localhost:6000 localhost:6001 --delay 500
