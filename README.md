## Quoridor - Team Murka

To run 
- open terminal in <project directory>
- enter java -cp build/libs/Quoridor-teamMurka-1.0.jar com.tmquoridor.Server.<>
Possible arguments you can add to command line are:
    --port <Followed by any valid port number>
    --name <Any String is legal and it will automatically add the prefix mur:>
    --delay <t>: Adds a delay of <t> ms.
    
For the client
- enter java -cp build/libs/Quoridor-teamMurka-1.0.jar com.tmquoridor.Client.GameClient localhost:6000 localhost:6001 [add more players here as the boot up protocol states] 

You can use any port/name you want
But the server <-> client port pairs must match 
