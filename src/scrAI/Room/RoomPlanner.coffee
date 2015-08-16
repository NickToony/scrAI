class RoomPlanner extends MemoryHandler
  init: ->
    this.memory['setup'] = false
    super

  plan: ->
    this.planMiners()

  planMiners: ->
    sources = this.roomController.room.find(global.FIND_SOURCES)
    console.log(this.roomController.room + " , " + sources.length)