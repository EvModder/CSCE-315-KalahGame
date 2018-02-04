# CSCE-315-Project2 <br />
Nathaniel Leake

## Executables
The game application comes with a few different JARs (Mains)
* KalahGame - The primary user inferface, allows a player to join or create a game or edit settings
* EndlessServer(NonGUI) - A server that continually accepts connections and plays against them using an AI
* MultiClientEndlessServer - Same as EndlessServer except can handle playing up to 100+ games simultaneously

## Configuration
### General Settings
These settings are used by both the host and the client
* ai-name: MinMaxAI2
* use-GUI: true
* button-image: Circle
* port: 42374

### Game Settings
These settings are used by the host to determine characteristics of a game
* holes-per-side: 6
* seeds-per-hole: 4
* time-limit: 5000 #Time limit in milliseconds
* starting-player: S #F=Me First, S=Me Second
* game-type: S #S=Standard,R=Random,C=Custom
* custom-board: 8 6 4 2 0 1 1 0 #Only used when `game-type: C`
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
* StrategicAI #Always gets the most seeds possible from the current turn
* MinMaxAI #Has basic pruning and a clever utility function
* MinMaxAI2 #Has multithreading and basic pruning
* MinMaxAI3 #Uses a tree instead of a recursive function, has pruning and multithreading
* MinMaxAIFinal
<br /><br />
So far, MinMaxAIFinal has been the most throughly tested and is recommended for optimal results.

## Additional Features
### AI
Features supported by the AI
* Multiple difficulties
* Alpha-Beta Pruning
* Iterative Deepening
* Advanced Utility Function
* Mutli-threading
* Branch ordering (explores favorable branches sooner)
* Hax

### GUI
Details included in the GUI
* Custom board setup and seed placement
* Ability to edit the game
* Custom graphics for each individual game window
* Dynamicly updated timer on board
* Settings window to edit settings directly from within the game
* Automatically loads and saves settings to a easily configurable file (yml)
* Can by played using either GUI or a terminal

### Client/Server
Details of the Client/Server setup
* The client and the server can both be run from the same menu
* Server can connect to multiple clients
* Game keeps track of last server successfully connected to
