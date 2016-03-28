#!/bin/bash

./runServer.sh 6000
./runServer.sh 6001
sleep 2
./runClient2x.sh