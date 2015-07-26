scrAI by NickToony
===================

About
-------------
**scrAI** is a Screeps AI written in Java. Through the use of ST-JS (http://st-js.github.io/) and some Maven magic, I can write my AI implementation in pure **Java**, compile it to **Javascript**, and push it to Screeps using **Grunt**.

Status
---------
It can spawn miners. It cannot move the miners.

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

The 3 steps performed by the Maven task are as follows:

1. Execute ST-JS with the Javascript bridge, compiling all Java classes to Javascript.
2. Run the maven-replacer-plugin to modify the generated Javascript to be Screeps friendly. In particular, it will uncomment all requires.
3. Run the grunt task to upload the correct files, without directories/packages. **Note:** this step also uploads the custom *stjs.js* found under */src/main/javascript*. This is a Screeps-friendly version of the script.

Modifiying
-------
**Module Importing**

Screeps modules need to be imported before used. This is easy in Javascript, but the syntax in Java does not exist (and ST-JS cannot be told to add the required lines easily). Hence, I've added an ugly workaround: you can add the raw Javascript *requires* in the Class's defining comment.

```java
/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Advisor = require("Advisor");
 */
public class MilitaryAdvisor extends Advisor {
```
The Maven configuration is setup to crawl through and uncomment these in the final build for you. It's a very specific format, so add it exactly as shown above.

**Module Exporting**

In Screeps, when a module is imported, it needs to export whatever it's defining. In terms of this project, each module is a Java class. Hence, each class needs to export itself when imported, so that it can be used in other modules.

In simple terms: **every class requires the following line**:
```java
static { module.exports = MilitaryAdvisor.class; }
```
where you replace *MilitaryAdvisor* with the name of the class.

**ST-JS Module**

ST-JS relies on a helper file to deal with a lot of the Java features missing in Javascript. This is vital for implementing the OO style, and includes features such as inheritance. I've modified the stjs.js file generated to be a more Screeps-friendly module, which should be deployed along with the project.

Every class needs to import the STJS module, otherwise it will not be correctly defined. Refer to the Module Importing section, but the key point is that every class needs the following in its class documentation comment:

```java
/**
 * var stjs = require("stjs");
 * var Advisor = require("Advisor");
 */
public class MilitaryAdvisor extends Advisor {
```

Also note that if a class extends another, it needs to import the class it's extending.

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

**PopulationManager**

Stores all creeps, and sorts them by type into an array. It creates the correct CreepWrapper class for each, which is useful for allowing the creep to perform its designated function.

**CreepWrapper**

A wrapper for the built-in creep object. Each creep type extends from this class, which tracks the creeps memory and status. It contains the creeps logic.
