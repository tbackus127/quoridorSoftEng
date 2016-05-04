#!/bin/sh

./server.sh 9996 | sed "s/^/PLAYER 1: /" &
./server.sh 9997 | sed "s/^/PLAYER 2: /" &
./server.sh 9998 | sed "s/^/PLAYER 3: /" &
./server.sh 9999 | sed "s/^/PLAYER 4: /" &

# give the servers time to start up
sleep 1

./client.sh 9996 9997 9998 9999