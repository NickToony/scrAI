scrAI by NickToony
===================

About
-------------
**scrAI** is a Screeps AI written in Java. Through the use of ST-JS (http://st-js.github.io/) and some Maven magic, I can write my AI implementation in pure **Java**, compile it to **Javascript**, concatenate into one large file, and push it to Screeps using **Grunt**.

By using a strongly typed and class based language, this project sets out to develop a more complex and maintainable AI.

Status
---------
 - TaskManager manages a rooms current tasks, prioritising them
	- Intelligently assigns tasks to workers by predicting the future state of the task, preventing units swarming tasks. e.g. don't assign a deposit energy task if it's expected to be filled anyway
 - EconomyAdvisor suggests units to create
	 - Attempts to produce an idea balance of Workers to Miners
 - Optimum Miner Logic
	 - Will determine the optimum amount of miners per source, and create specialised creeps for the task

This project is a work in progress. However, the Java->Javascript works if you're interested in that.

Dependencies
----------

**Not Provided**

The following are external dependencies you must install to get the most out of this setup

 - Maven
	 - (optional) with M2_HOME environment variable defined
 - Screeps Grunt Task
	 - http://support.screeps.com/hc/en-us/articles/203022512-Commiting-local-scripts-using-Grunt
	 - requires Grunt and NPM

**Provided**

The following are provided by the Maven pom.xml file.

 - ST-JS
	 - http://st-js.github.io/
 - ST-JS Javascript Bridge
 - Google's maven-replacer-plugin
	 - https://code.google.com/p/maven-replacer-plugin/
 - YUI Compressor Maven plugin
	- http://davidb.github.io/yuicompressor-maven-plugin/


Setup
-------------------

**Adding your Screeps login**

The compiled code is automatically uploaded to your Screeps account. I recommend using this, as it'll handle flattening the packages for you. It also includes the custom stjs.js file.

1. Duplicate the *Gruntfile.example.js* file to *Gruntfile.js*
2. Replace the *email* and *password* fields with your details
3. *Gruntfile.js* is already added to the .gitignore

**Compiling and Deploying**

1. Build the java project.
2. Executing the Maven task *prepare-package* will run the remaining required steps.

**Tip**: You can add a new Build Configuration in Intellij for example, that runs the command line *prepare-package*. Add a "Before launch" parameter that runs "Make".

The 4 steps performed by the Maven task are as follows:

1. Execute ST-JS with the Javascript bridge, compiling all Java classes to Javascript.
2. Run the maven-replacer-plugin to modify the generated Javascript to be Screeps friendly. 
3. Run the YUI-compressor to combine all the generated javascript files into one file. **Note:** this step also includes the custom *stjs.js* found under */src/main/javascript*. This is a Screeps-friendly version of the script.
4. Run the grunt task to upload the correct files, without directories/packages.

Modifiying
-------
**Module Importing, Exporting and STJS**

Since I changed the setup to instead combine all scripts into one single javascript file, you no longer need to import/export modules. This means the previous dirty hacks to implement the unsupported Javascript syntax in Java is not necessary.

STJS still requires its methods though, so a custom STJS implementation is provided that will work when included inside the final script.

The advantages of using a single script are:
- Importing/Exporting not necessary, don't need to create dirty hacks in Java to implement them
- Requiring a module in screeps could take an unpredictable amount of time. Performance is a lot better if you just have one script.

**Lodash**

Screeps likes to use javascript which is just plain difficult for ST-JS to bridge. An example of this is the *Game.rooms* hashmap - which doesn't bridge at all well! To get around this, you can import the Lodash module, and use that to iterate over Javascript collections.

Architecture
-------

**GlobalController**

The GlobalController looks at the map as a whole. It doesn't worry about managing each rooms contents, but the interactions between rooms. It decides where to reinforce, where to expand (or assault) and the parameters affecting each room (such as alert status).

**RoomController**

A layer below GlobalController, this controller is given the responsibility of managing one specific room. It relies on Managers (which organise and act upon room contents) and Advisors (which take the organised data of the Managers and suggests the next move). Which advisor it listens to is decided by the RoomController's threat and alert levels.

**MilitaryAdvisor**

Decides on military matters; units to produce, where to position and defensive structures.

**EconomyAdvisor**

Decides on economy matters: units to produce, which sources to gather, buildings to maintain/build.

**SourcesManager**

Stores all Sources in the room, and determines which are safe to gather from.

**SpawnsManager**

Stores all Spawns in the room, and determines which are available to spawn from.

**TasksManager**

The task manager tracks the memory of each Task currently in the room. Tasks are distributed to Worker creeps in an intelligent manner, which ensures the most important tasks are prioritised and carried out efficiently.

**PopulationManager**

Stores all creeps, and sorts them by type into an array. It creates the correct CreepWrapper class for each, which is useful for allowing the creep to perform its designated function.

**CreepWrapper**

A wrapper for the built-in creep object. Each creep type extends from this class, which tracks the creeps memory and status. It contains the creeps logic.
