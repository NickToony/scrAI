class MemoryHandler
  roomController: null
  memory: null

  constructor: (@memory, @roomController) ->
    this.init() if !this.memory['init']?

  init: ->
    this.memory['init'] = true

  getMemory: (name) ->
    this.memory[name] = {} if !this.memory[name]?
    return this.memory[name]