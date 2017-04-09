# CSCE-315-Project2 <br />
Authors: Andrew Lam, Nathaniel Leake, Tony Huynh

## Executables
The game application comes with a few different JARs (Mains)
* KalahGame - The primary user inferface, allows a player to join or create a game or edit settings
* EndlessServer(NonGUI) - A server that repeatedly accepts client connections and plays against them using an AI
* MultiClientEndlessServer - Same as EndlessServer except can handle up to 100 games simultaneously

## Configuration
### General Settings
These are the settings used by both the server and the client
* ai-name: MinMaxAI2
* use-GUI: true
* button-image: Circle
* port: 42374

### Game Settings
These settings are used by the server to determine characteristics of a game against a client
* holes-per-side: 6
* seeds-per-hole: 4
* time-limit: 5000 #Time limit in milliseconds
* starting-player: S #S=You First, S=Me First
* game-type: S #S=Standard,R=Random,C=Custom
* custom-board: 8 6 4 2 0 1 1 0 #This setting is used if game-type==C
* use-BEGIN: true

### Miscellaneous Settings
* empty-capture: false
* count-leftover-seeds: true
* last-host: localhost
* max-threads: 100

### KalahPlayer Options
There are many AIs (including some min-max AIs) which can be played against. A dropdown menu in the settings window allows the user to select one of the primary AIs from the following list:
* HumanGUI #Takes imput from GUI
* HumanConsole #Takes input from console
* RandomAI #Chooses random valid moves
* DumbAI #Chooses moves that land in its Kalah
* StrategicAI #Always gets the most seeds possible from its turn
* MinMaxAI #Has basic pruning
* MinMaxAI2 #Has multithreading
* MinMaxAI2WithPruning #Has multithreading and basic pruning
* MinMaxAI3 #Uses a tree instead of a recursive function, has pruning and multithreading.
<br /><br />
So far, MinMaxAI2 has been the most throughly tested and is recommended for optimal results.
