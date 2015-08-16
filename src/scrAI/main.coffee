console.log("Start Tick: " + Game.getUsedCpu())

room = Game.rooms["sim"]
roomController = new RoomController room
roomController.step()

console.log("Finished Tick: " + Game.getUsedCpu())
