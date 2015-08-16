class RoomController extends MemoryHandler
  room: null

  constructor: (@room) ->
    super this.room.memory, this

    this.roomPlanner = new RoomPlanner(this.getMemory("RoomPlanner"), this)


  init: ->
    super

  step: ->
    this.roomPlanner.plan();
