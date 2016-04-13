## Quoridor - Team Murka

To run 
- open terminal in <project directory>/build/classes/main 
- enter java com/tmquoridor/Server/ManualInputServer --port 6000 --name Srv01
- enter java com/tmquoridor/Server/ManualInputServer --port 6001 --name Srv02
For 3 & 4 player repeat steps appendind #s

For the clients
- enter java com/tmquoridor/Client/GameClient localhost:6000 localhost 6001 [add more here] 

You can use any port/name you want
But the server <-> client port pairs must match 

Follow the ManualInputServer for instruction