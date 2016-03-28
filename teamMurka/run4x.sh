#!/bin/bash

./runServer.sh 6000
./runServer.sh 6001
./runServer.sh 6002
./runServer.sh 6003
sleep 2
./runClient4x.sh