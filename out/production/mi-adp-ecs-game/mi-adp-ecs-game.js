(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'mi-adp-ecs-game'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'mi-adp-ecs-game'.");
    }
    root['mi-adp-ecs-game'] = factory(typeof this['mi-adp-ecs-game'] === 'undefined' ? {} : this['mi-adp-ecs-game'], kotlin);
  }
}(this, function (_, Kotlin) {
  'use strict';
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var getKClass = Kotlin.getKClass;
  var get_js = Kotlin.kotlin.js.get_js_1yb8b7$;
  var Unit = Kotlin.kotlin.Unit;
  var listOf = Kotlin.kotlin.collections.listOf_i5x0yv$;
  var Kind_OBJECT = Kotlin.Kind.OBJECT;
  var math = Kotlin.kotlin.math;
  var Kind_INTERFACE = Kotlin.Kind.INTERFACE;
  var RuntimeException_init = Kotlin.kotlin.RuntimeException_init_pdl1vj$;
  var throwCCE = Kotlin.throwCCE;
  var HashSet_init = Kotlin.kotlin.collections.HashSet_init_ww73n8$;
  var toHashSet = Kotlin.kotlin.collections.toHashSet_us0mfu$;
  var equals = Kotlin.equals;
  var Enum = Kotlin.kotlin.Enum;
  var throwISE = Kotlin.throwISE;
  var toByte = Kotlin.toByte;
  var toString = Kotlin.kotlin.text.toString_dqglrj$;
  var repeat = Kotlin.kotlin.text.repeat_94bcnn$;
  var IllegalArgumentException_init = Kotlin.kotlin.IllegalArgumentException_init_pdl1vj$;
  GameState.prototype = Object.create(Enum.prototype);
  GameState.prototype.constructor = GameState;
  ComponentRequirement$ComponentInclusion.prototype = Object.create(Enum.prototype);
  ComponentRequirement$ComponentInclusion.prototype.constructor = ComponentRequirement$ComponentInclusion;
  function PositionComponent(x, y) {
    this.x = x;
    this.y = y;
  }
  PositionComponent.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'PositionComponent',
    interfaces: [IComponent]
  };
  PositionComponent.prototype.component1 = function () {
    return this.x;
  };
  PositionComponent.prototype.component2 = function () {
    return this.y;
  };
  PositionComponent.prototype.copy_lu1900$ = function (x, y) {
    return new PositionComponent(x === void 0 ? this.x : x, y === void 0 ? this.y : y);
  };
  PositionComponent.prototype.toString = function () {
    return 'PositionComponent(x=' + Kotlin.toString(this.x) + (', y=' + Kotlin.toString(this.y)) + ')';
  };
  PositionComponent.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.x) | 0;
    result = result * 31 + Kotlin.hashCode(this.y) | 0;
    return result;
  };
  PositionComponent.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.x, other.x) && Kotlin.equals(this.y, other.y)))));
  };
  function RenderCircleComponent(radius, color) {
    this.radius = radius;
    this.color = color;
  }
  RenderCircleComponent.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'RenderCircleComponent',
    interfaces: [IComponent]
  };
  RenderCircleComponent.prototype.component1 = function () {
    return this.radius;
  };
  RenderCircleComponent.prototype.component2 = function () {
    return this.color;
  };
  RenderCircleComponent.prototype.copy_cw7ic$ = function (radius, color) {
    return new RenderCircleComponent(radius === void 0 ? this.radius : radius, color === void 0 ? this.color : color);
  };
  RenderCircleComponent.prototype.toString = function () {
    return 'RenderCircleComponent(radius=' + Kotlin.toString(this.radius) + (', color=' + Kotlin.toString(this.color)) + ')';
  };
  RenderCircleComponent.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.radius) | 0;
    result = result * 31 + Kotlin.hashCode(this.color) | 0;
    return result;
  };
  RenderCircleComponent.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.radius, other.radius) && Kotlin.equals(this.color, other.color)))));
  };
  function SpriteComponent(image) {
    this.image = image;
  }
  SpriteComponent.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'SpriteComponent',
    interfaces: [IComponent]
  };
  SpriteComponent.prototype.component1 = function () {
    return this.image;
  };
  SpriteComponent.prototype.copy_8w7ot$ = function (image) {
    return new SpriteComponent(image === void 0 ? this.image : image);
  };
  SpriteComponent.prototype.toString = function () {
    return 'SpriteComponent(image=' + Kotlin.toString(this.image) + ')';
  };
  SpriteComponent.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.image) | 0;
    return result;
  };
  SpriteComponent.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && Kotlin.equals(this.image, other.image))));
  };
  function VelocityComponent(x, y) {
    this.x = x;
    this.y = y;
  }
  VelocityComponent.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'VelocityComponent',
    interfaces: [IComponent]
  };
  VelocityComponent.prototype.component1 = function () {
    return this.x;
  };
  VelocityComponent.prototype.component2 = function () {
    return this.y;
  };
  VelocityComponent.prototype.copy_lu1900$ = function (x, y) {
    return new VelocityComponent(x === void 0 ? this.x : x, y === void 0 ? this.y : y);
  };
  VelocityComponent.prototype.toString = function () {
    return 'VelocityComponent(x=' + Kotlin.toString(this.x) + (', y=' + Kotlin.toString(this.y)) + ')';
  };
  VelocityComponent.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.x) | 0;
    result = result * 31 + Kotlin.hashCode(this.y) | 0;
    return result;
  };
  VelocityComponent.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.x, other.x) && Kotlin.equals(this.y, other.y)))));
  };
  function MoveSystem() {
    MoveSystem$Companion_getInstance();
  }
  MoveSystem.prototype.update_o1ngyx$ = function (deltaTime, entities) {
    var tmp$;
    tmp$ = entities.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      var positionComponent = EntityManager_getInstance().getComponent_ft1pe3$(element, get_js(getKClass(PositionComponent)));
      var velocityComponent = EntityManager_getInstance().getComponent_ft1pe3$(element, get_js(getKClass(VelocityComponent)));
      positionComponent.x = positionComponent.x + velocityComponent.x * deltaTime;
      positionComponent.y = positionComponent.y + velocityComponent.y * deltaTime;
    }
  };
  MoveSystem.prototype.componentSpecification = function () {
    return MoveSystem$Companion_getInstance().requirement;
  };
  function MoveSystem$Companion() {
    MoveSystem$Companion_instance = this;
    this.requirement = listOf([new ComponentRequirement(get_js(getKClass(PositionComponent)), ComponentRequirement$ComponentInclusion$MustHave_getInstance()), new ComponentRequirement(get_js(getKClass(VelocityComponent)), ComponentRequirement$ComponentInclusion$MustHave_getInstance())]);
  }
  MoveSystem$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var MoveSystem$Companion_instance = null;
  function MoveSystem$Companion_getInstance() {
    if (MoveSystem$Companion_instance === null) {
      new MoveSystem$Companion();
    }
    return MoveSystem$Companion_instance;
  }
  MoveSystem.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'MoveSystem',
    interfaces: [ISystem]
  };
  function RenderSystem() {
    RenderSystem$Companion_getInstance();
    this.ctx_0 = Core_getInstance().canvasContext;
  }
  RenderSystem.prototype.update_o1ngyx$ = function (deltaTime, entities) {
    var tmp$;
    tmp$ = entities.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      var positionComponent = EntityManager_getInstance().getComponent_ft1pe3$(element, get_js(getKClass(PositionComponent)));
      var renderComponent = EntityManager_getInstance().getComponent_ft1pe3$(element, get_js(getKClass(RenderCircleComponent)));
      this.ctx_0.beginPath();
      this.ctx_0.arc(positionComponent.x, positionComponent.y, renderComponent.radius, 0.0, math.PI * 2.0);
      this.ctx_0.fillStyle = renderComponent.color.hex;
      console.log(renderComponent.color.hex);
      this.ctx_0.fill();
      this.ctx_0.stroke();
      this.ctx_0.closePath();
    }
  };
  RenderSystem.prototype.componentSpecification = function () {
    return RenderSystem$Companion_getInstance().requirement;
  };
  function RenderSystem$Companion() {
    RenderSystem$Companion_instance = this;
    this.requirement = listOf([new ComponentRequirement(get_js(getKClass(PositionComponent)), ComponentRequirement$ComponentInclusion$MustHave_getInstance()), new ComponentRequirement(get_js(getKClass(RenderCircleComponent)), ComponentRequirement$ComponentInclusion$MustHave_getInstance())]);
  }
  RenderSystem$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var RenderSystem$Companion_instance = null;
  function RenderSystem$Companion_getInstance() {
    if (RenderSystem$Companion_instance === null) {
      new RenderSystem$Companion();
    }
    return RenderSystem$Companion_instance;
  }
  RenderSystem.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'RenderSystem',
    interfaces: [ISystem]
  };
  function IComponent() {
  }
  IComponent.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'IComponent',
    interfaces: []
  };
  function Core() {
    Core_instance = this;
    this.state_vjg0r8$_0 = GameState$Stopped_getInstance();
    this.deltaTime_lyhsdk$_0 = 0.0;
    this.time_zdktrq$_0 = 0.0;
    this.requestId_0 = -1;
    var tmp$, tmp$_0;
    this.canvas = Kotlin.isType(tmp$ = document.getElementById('game'), HTMLCanvasElement) ? tmp$ : throwCCE();
    this.canvasContext = Kotlin.isType(tmp$_0 = this.canvas.getContext('2d'), CanvasRenderingContext2D) ? tmp$_0 : throwCCE();
  }
  Object.defineProperty(Core.prototype, 'state', {
    get: function () {
      return this.state_vjg0r8$_0;
    },
    set: function (state) {
      this.state_vjg0r8$_0 = state;
    }
  });
  Object.defineProperty(Core.prototype, 'deltaTime', {
    get: function () {
      return this.deltaTime_lyhsdk$_0;
    },
    set: function (deltaTime) {
      this.deltaTime_lyhsdk$_0 = deltaTime;
    }
  });
  Object.defineProperty(Core.prototype, 'time', {
    get: function () {
      return this.time_zdktrq$_0;
    },
    set: function (time) {
      this.time_zdktrq$_0 = time;
    }
  });
  function Core$requestUpdate$lambda(this$Core) {
    return function (it) {
      this$Core.deltaTime = (it - this$Core.time) / 1000.0;
      this$Core.time = it;
      this$Core.update_0(this$Core.deltaTime);
      this$Core.requestUpdate_0();
      return Unit;
    };
  }
  Core.prototype.requestUpdate_0 = function () {
    this.requestId_0 = window.requestAnimationFrame(Core$requestUpdate$lambda(this));
  };
  Core.prototype.update_0 = function (deltaTime) {
    this.canvasContext.clearRect(0.0, 0.0, this.canvas.width, this.canvas.height);
    SystemManager_getInstance().update_14dthe$(deltaTime);
  };
  Core.prototype.run = function () {
    if (this.state === GameState$Running_getInstance())
      throw RuntimeException_init('Already in running state');
    this.state = GameState$Running_getInstance();
    this.time = window.performance.now();
    this.deltaTime = 0.0;
    this.requestUpdate_0();
  };
  Core.prototype.stop = function () {
    if (this.state !== GameState$Running_getInstance())
      throw RuntimeException_init('Not in running state');
    window.cancelAnimationFrame(this.requestId_0);
  };
  Core.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Core',
    interfaces: []
  };
  var Core_instance = null;
  function Core_getInstance() {
    if (Core_instance === null) {
      new Core();
    }
    return Core_instance;
  }
  function Entity(id) {
    this.id = id;
  }
  Entity.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Entity',
    interfaces: []
  };
  Entity.prototype.component1 = function () {
    return this.id;
  };
  Entity.prototype.copy_za3lpa$ = function (id) {
    return new Entity(id === void 0 ? this.id : id);
  };
  Entity.prototype.toString = function () {
    return 'Entity(id=' + Kotlin.toString(this.id) + ')';
  };
  Entity.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.id) | 0;
    return result;
  };
  Entity.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && Kotlin.equals(this.id, other.id))));
  };
  var LinkedHashMap_init = Kotlin.kotlin.collections.LinkedHashMap_init_q3lmfv$;
  function EntityManager() {
    EntityManager_instance = this;
    this.nextId_0 = 0;
    this.entities_0 = LinkedHashMap_init();
  }
  EntityManager.prototype.createEntity_za3lpa$ = function (componentCount) {
    return this.createEntity_ogns11$(HashSet_init(componentCount));
  };
  EntityManager.prototype.createEntity_dicjod$ = function (components) {
    return this.createEntity_ogns11$(toHashSet(components));
  };
  EntityManager.prototype.createEntity_2o55ea$ = function (components) {
    return this.createEntity_ogns11$(toHashSet(components));
  };
  EntityManager.prototype.createEntity_ogns11$ = function (components) {
    var tmp$;
    var entity = new Entity((tmp$ = this.nextId_0, this.nextId_0 = tmp$ + 1 | 0, tmp$));
    this.entities_0.put_xwzc9p$(entity, components);
    SystemManager_getInstance().onEntityChanged_gojav6$(entity);
    return entity;
  };
  EntityManager.prototype.removeEntity_gojav6$ = function (entity) {
    this.entities_0.remove_11rb$(entity);
  };
  EntityManager.prototype.getComponents_gojav6$ = function (entity) {
    var tmp$;
    tmp$ = this.entities_0.get_11rb$(entity);
    if (tmp$ == null) {
      throw RuntimeException_init('entity ' + entity + ' is either destroyed or does not exist');
    }
    return tmp$;
  };
  EntityManager.prototype.removeComponent_8hjurr$ = function (entity, component) {
    var components = this.getComponents_gojav6$(entity);
    if (!components.remove_11rb$(component))
      throw RuntimeException_init('entity ' + entity + ' does not have component of type ' + get_js(Kotlin.getKClassFromExpression(component)).name);
  };
  var Collection = Kotlin.kotlin.collections.Collection;
  EntityManager.prototype.hasComponent_ihbluf$ = function (entity, component) {
    var $receiver = this.getComponents_gojav6$(entity);
    var any$result;
    any$break: do {
      var tmp$;
      if (Kotlin.isType($receiver, Collection) && $receiver.isEmpty()) {
        any$result = false;
        break any$break;
      }
      tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var element = tmp$.next();
        if (equals(get_js(Kotlin.getKClassFromExpression(element)).name, component.name)) {
          any$result = true;
          break any$break;
        }
      }
      any$result = false;
    }
     while (false);
    return any$result;
  };
  EntityManager.prototype.getComponent_ft1pe3$ = function (entity, component) {
    var components = this.getComponents_gojav6$(entity);
    var firstOrNull$result;
    firstOrNull$break: do {
      var tmp$;
      tmp$ = components.iterator();
      while (tmp$.hasNext()) {
        var element = tmp$.next();
        if (equals(get_js(Kotlin.getKClassFromExpression(element)).name, component.name)) {
          firstOrNull$result = element;
          break firstOrNull$break;
        }
      }
      firstOrNull$result = null;
    }
     while (false);
    return firstOrNull$result;
  };
  EntityManager.prototype.addComponent_8hjurr$ = function (entity, component) {
    if (!this.getComponents_gojav6$(entity).add_11rb$(component))
      throw RuntimeException_init('component ' + get_js(Kotlin.getKClassFromExpression(component)).name + ' is already added');
    SystemManager_getInstance().onEntityChanged_gojav6$(entity);
  };
  EntityManager.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'EntityManager',
    interfaces: []
  };
  var EntityManager_instance = null;
  function EntityManager_getInstance() {
    if (EntityManager_instance === null) {
      new EntityManager();
    }
    return EntityManager_instance;
  }
  function GameState(name, ordinal) {
    Enum.call(this);
    this.name$ = name;
    this.ordinal$ = ordinal;
  }
  function GameState_initFields() {
    GameState_initFields = function () {
    };
    GameState$Running_instance = new GameState('Running', 0);
    GameState$Stopping_instance = new GameState('Stopping', 1);
    GameState$Stopped_instance = new GameState('Stopped', 2);
  }
  var GameState$Running_instance;
  function GameState$Running_getInstance() {
    GameState_initFields();
    return GameState$Running_instance;
  }
  var GameState$Stopping_instance;
  function GameState$Stopping_getInstance() {
    GameState_initFields();
    return GameState$Stopping_instance;
  }
  var GameState$Stopped_instance;
  function GameState$Stopped_getInstance() {
    GameState_initFields();
    return GameState$Stopped_instance;
  }
  GameState.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'GameState',
    interfaces: [Enum]
  };
  function GameState$values() {
    return [GameState$Running_getInstance(), GameState$Stopping_getInstance(), GameState$Stopped_getInstance()];
  }
  GameState.values = GameState$values;
  function GameState$valueOf(name) {
    switch (name) {
      case 'Running':
        return GameState$Running_getInstance();
      case 'Stopping':
        return GameState$Stopping_getInstance();
      case 'Stopped':
        return GameState$Stopped_getInstance();
      default:throwISE('No enum constant engine.GameState.' + name);
    }
  }
  GameState.valueOf_61zpoe$ = GameState$valueOf;
  function ComponentRequirement(componentType, inclusion) {
    this.componentType = componentType;
    this.inclusion = inclusion;
  }
  ComponentRequirement.prototype.isMet_gojav6$ = function (entity) {
    var entityHasComponent = EntityManager_getInstance().hasComponent_ihbluf$(entity, this.componentType);
    return this.inclusion === ComponentRequirement$ComponentInclusion$MustHave_getInstance() && entityHasComponent ? true : this.inclusion === ComponentRequirement$ComponentInclusion$MustNotHave_getInstance() && !entityHasComponent;
  };
  function ComponentRequirement$ComponentInclusion(name, ordinal) {
    Enum.call(this);
    this.name$ = name;
    this.ordinal$ = ordinal;
  }
  function ComponentRequirement$ComponentInclusion_initFields() {
    ComponentRequirement$ComponentInclusion_initFields = function () {
    };
    ComponentRequirement$ComponentInclusion$MustHave_instance = new ComponentRequirement$ComponentInclusion('MustHave', 0);
    ComponentRequirement$ComponentInclusion$MustNotHave_instance = new ComponentRequirement$ComponentInclusion('MustNotHave', 1);
  }
  var ComponentRequirement$ComponentInclusion$MustHave_instance;
  function ComponentRequirement$ComponentInclusion$MustHave_getInstance() {
    ComponentRequirement$ComponentInclusion_initFields();
    return ComponentRequirement$ComponentInclusion$MustHave_instance;
  }
  var ComponentRequirement$ComponentInclusion$MustNotHave_instance;
  function ComponentRequirement$ComponentInclusion$MustNotHave_getInstance() {
    ComponentRequirement$ComponentInclusion_initFields();
    return ComponentRequirement$ComponentInclusion$MustNotHave_instance;
  }
  ComponentRequirement$ComponentInclusion.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'ComponentInclusion',
    interfaces: [Enum]
  };
  function ComponentRequirement$ComponentInclusion$values() {
    return [ComponentRequirement$ComponentInclusion$MustHave_getInstance(), ComponentRequirement$ComponentInclusion$MustNotHave_getInstance()];
  }
  ComponentRequirement$ComponentInclusion.values = ComponentRequirement$ComponentInclusion$values;
  function ComponentRequirement$ComponentInclusion$valueOf(name) {
    switch (name) {
      case 'MustHave':
        return ComponentRequirement$ComponentInclusion$MustHave_getInstance();
      case 'MustNotHave':
        return ComponentRequirement$ComponentInclusion$MustNotHave_getInstance();
      default:throwISE('No enum constant engine.system.ComponentRequirement.ComponentInclusion.' + name);
    }
  }
  ComponentRequirement$ComponentInclusion.valueOf_61zpoe$ = ComponentRequirement$ComponentInclusion$valueOf;
  ComponentRequirement.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'ComponentRequirement',
    interfaces: []
  };
  ComponentRequirement.prototype.component1 = function () {
    return this.componentType;
  };
  ComponentRequirement.prototype.component2 = function () {
    return this.inclusion;
  };
  ComponentRequirement.prototype.copy_6fap2d$ = function (componentType, inclusion) {
    return new ComponentRequirement(componentType === void 0 ? this.componentType : componentType, inclusion === void 0 ? this.inclusion : inclusion);
  };
  ComponentRequirement.prototype.toString = function () {
    return 'ComponentRequirement(componentType=' + Kotlin.toString(this.componentType) + (', inclusion=' + Kotlin.toString(this.inclusion)) + ')';
  };
  ComponentRequirement.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.componentType) | 0;
    result = result * 31 + Kotlin.hashCode(this.inclusion) | 0;
    return result;
  };
  ComponentRequirement.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.componentType, other.componentType) && Kotlin.equals(this.inclusion, other.inclusion)))));
  };
  function ISystem() {
  }
  ISystem.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'ISystem',
    interfaces: []
  };
  function SystemManager() {
    SystemManager_instance = this;
    this.systems_0 = LinkedHashMap_init();
  }
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  SystemManager.prototype.registerSystem_q3bkjf$ = function (system) {
    if (this.systems_0.get_11rb$(get_js(Kotlin.getKClassFromExpression(system)).name) != null)
      throw RuntimeException_init('system ' + get_js(Kotlin.getKClassFromExpression(system)).name + ' is already registered');
    var tmp$ = this.systems_0;
    var tmp$_0 = get_js(Kotlin.getKClassFromExpression(system)).name;
    var value = new SystemData(system, ArrayList_init());
    tmp$.put_xwzc9p$(tmp$_0, value);
  };
  SystemManager.prototype.unregisterSystem_q3bkjf$ = function (system) {
    this.unregisterSystem_mb383j$(get_js(Kotlin.getKClassFromExpression(system)));
  };
  SystemManager.prototype.unregisterSystem_mb383j$ = function (systemType) {
    if (this.systems_0.get_11rb$(systemType.name) == null)
      throw RuntimeException_init('system ' + systemType.name + ' is not registered');
    this.systems_0.remove_11rb$(systemType.name);
  };
  SystemManager.prototype.onEntityChanged_gojav6$ = function (entity) {
    var tmp$;
    tmp$ = this.systems_0.entries.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      element.value.onEntityChanged_gojav6$(entity);
    }
  };
  SystemManager.prototype.update_14dthe$ = function (deltaTime) {
    var tmp$;
    tmp$ = this.systems_0.entries.iterator();
    while (tmp$.hasNext()) {
      var element = tmp$.next();
      if (!element.value.entities.isEmpty())
        element.value.system.update_o1ngyx$(deltaTime, element.value.entities);
    }
  };
  SystemManager.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'SystemManager',
    interfaces: []
  };
  var SystemManager_instance = null;
  function SystemManager_getInstance() {
    if (SystemManager_instance === null) {
      new SystemManager();
    }
    return SystemManager_instance;
  }
  function SystemData(system, entityCollection) {
    this.system = system;
    this.entityCollection_0 = entityCollection;
  }
  Object.defineProperty(SystemData.prototype, 'entities', {
    get: function () {
      return this.entityCollection_0;
    }
  });
  SystemData.prototype.onEntityChanged_gojav6$ = function (entity) {
    var $receiver = this.system.componentSpecification();
    var all$result;
    all$break: do {
      var tmp$;
      if (Kotlin.isType($receiver, Collection) && $receiver.isEmpty()) {
        all$result = true;
        break all$break;
      }
      tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var element = tmp$.next();
        if (!element.isMet_gojav6$(entity)) {
          all$result = false;
          break all$break;
        }
      }
      all$result = true;
    }
     while (false);
    var meetsRequirements = all$result;
    if (this.entityCollection_0.contains_11rb$(entity)) {
      if (!meetsRequirements)
        this.entityCollection_0.remove_11rb$(entity);
    }
     else {
      if (meetsRequirements)
        this.entityCollection_0.add_11rb$(entity);
    }
  };
  SystemData.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'SystemData',
    interfaces: []
  };
  SystemData.prototype.component1 = function () {
    return this.system;
  };
  SystemData.prototype.component2_0 = function () {
    return this.entityCollection_0;
  };
  SystemData.prototype.copy_e6h2kl$ = function (system, entityCollection) {
    return new SystemData(system === void 0 ? this.system : system, entityCollection === void 0 ? this.entityCollection_0 : entityCollection);
  };
  SystemData.prototype.toString = function () {
    return 'SystemData(system=' + Kotlin.toString(this.system) + (', entityCollection=' + Kotlin.toString(this.entityCollection_0)) + ')';
  };
  SystemData.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.system) | 0;
    result = result * 31 + Kotlin.hashCode(this.entityCollection_0) | 0;
    return result;
  };
  SystemData.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.system, other.system) && Kotlin.equals(this.entityCollection_0, other.entityCollection_0)))));
  };
  function main(args) {
    var tmp$;
    var canvas = Kotlin.isType(tmp$ = document.getElementById('game'), HTMLCanvasElement) ? tmp$ : throwCCE();
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    SystemManager_getInstance().registerSystem_q3bkjf$(new MoveSystem());
    SystemManager_getInstance().registerSystem_q3bkjf$(new RenderSystem());
    EntityManager_getInstance().createEntity_2o55ea$([new PositionComponent(0.0, 0.0), new VelocityComponent(8.0, 8.0), new RenderCircleComponent(10.0, Rgba$Companion_getInstance().GREEN)]);
    Core_getInstance().run();
  }
  function Rgba(value) {
    Rgba$Companion_getInstance();
    this.value = value;
  }
  Object.defineProperty(Rgba.prototype, 'red', {
    get: function () {
      return toByte(this.value >> 24);
    },
    set: function (value) {
      this.setColorChannel_0(value, 24);
    }
  });
  Object.defineProperty(Rgba.prototype, 'green', {
    get: function () {
      return toByte(this.value >> 16 & 255);
    },
    set: function (value) {
      this.setColorChannel_0(value, 16);
    }
  });
  Object.defineProperty(Rgba.prototype, 'blue', {
    get: function () {
      return toByte(this.value >> 8 & 255);
    },
    set: function (value) {
      this.setColorChannel_0(value, 8);
    }
  });
  Object.defineProperty(Rgba.prototype, 'alpha', {
    get: function () {
      return toByte(this.value & 255);
    },
    set: function (value) {
      this.setColorChannel_0(value, 0);
    }
  });
  Object.defineProperty(Rgba.prototype, 'hex', {
    get: function () {
      var hexValue = toString(this.value, 16);
      return '#' + repeat('0', 8 - hexValue.length | 0) + hexValue;
    }
  });
  Rgba.prototype.setColorChannel_0 = function (value, offset) {
    if (offset > 24 || offset < 0 || offset % 8 !== 0)
      throw IllegalArgumentException_init('Offset must be 0, 8, 16 or 24. Was ' + offset);
    var channelMask = 255 << offset;
    this.value = this.value & ~channelMask | value << offset;
  };
  function Rgba$Companion() {
    Rgba$Companion_instance = this;
  }
  Object.defineProperty(Rgba$Companion.prototype, 'RED', {
    get: function () {
      return new Rgba(-16776961);
    }
  });
  Object.defineProperty(Rgba$Companion.prototype, 'GREEN', {
    get: function () {
      return new Rgba(-16776961);
    }
  });
  Object.defineProperty(Rgba$Companion.prototype, 'BLUE', {
    get: function () {
      return new Rgba(-16776961);
    }
  });
  Rgba$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var Rgba$Companion_instance = null;
  function Rgba$Companion_getInstance() {
    if (Rgba$Companion_instance === null) {
      new Rgba$Companion();
    }
    return Rgba$Companion_instance;
  }
  Rgba.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Rgba',
    interfaces: []
  };
  function Rgba_init(red, green, blue, alpha, $this) {
    if (alpha === void 0)
      alpha = 255;
    $this = $this || Object.create(Rgba.prototype);
    Rgba.call($this, ((red & 255) << 24) + ((green & 255) << 16) + ((blue & 255) << 8) + (alpha & 255) | 0);
    return $this;
  }
  Rgba.prototype.component1 = function () {
    return this.value;
  };
  Rgba.prototype.copy_za3lpa$ = function (value) {
    return new Rgba(value === void 0 ? this.value : value);
  };
  Rgba.prototype.toString = function () {
    return 'Rgba(value=' + Kotlin.toString(this.value) + ')';
  };
  Rgba.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.value) | 0;
    return result;
  };
  Rgba.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && Kotlin.equals(this.value, other.value))));
  };
  var package$ecs = _.ecs || (_.ecs = {});
  var package$component = package$ecs.component || (package$ecs.component = {});
  package$component.PositionComponent = PositionComponent;
  package$component.RenderCircleComponent = RenderCircleComponent;
  package$component.SpriteComponent = SpriteComponent;
  package$component.VelocityComponent = VelocityComponent;
  Object.defineProperty(MoveSystem, 'Companion', {
    get: MoveSystem$Companion_getInstance
  });
  var package$system = package$ecs.system || (package$ecs.system = {});
  package$system.MoveSystem = MoveSystem;
  Object.defineProperty(RenderSystem, 'Companion', {
    get: RenderSystem$Companion_getInstance
  });
  package$system.RenderSystem = RenderSystem;
  var package$engine = _.engine || (_.engine = {});
  var package$component_0 = package$engine.component || (package$engine.component = {});
  package$component_0.IComponent = IComponent;
  Object.defineProperty(package$engine, 'Core', {
    get: Core_getInstance
  });
  var package$entity = package$engine.entity || (package$engine.entity = {});
  package$entity.Entity = Entity;
  Object.defineProperty(package$entity, 'EntityManager', {
    get: EntityManager_getInstance
  });
  Object.defineProperty(GameState, 'Running', {
    get: GameState$Running_getInstance
  });
  Object.defineProperty(GameState, 'Stopping', {
    get: GameState$Stopping_getInstance
  });
  Object.defineProperty(GameState, 'Stopped', {
    get: GameState$Stopped_getInstance
  });
  package$engine.GameState = GameState;
  Object.defineProperty(ComponentRequirement$ComponentInclusion, 'MustHave', {
    get: ComponentRequirement$ComponentInclusion$MustHave_getInstance
  });
  Object.defineProperty(ComponentRequirement$ComponentInclusion, 'MustNotHave', {
    get: ComponentRequirement$ComponentInclusion$MustNotHave_getInstance
  });
  ComponentRequirement.ComponentInclusion = ComponentRequirement$ComponentInclusion;
  var package$system_0 = package$engine.system || (package$engine.system = {});
  package$system_0.ComponentRequirement = ComponentRequirement;
  package$system_0.ISystem = ISystem;
  Object.defineProperty(package$system_0, 'SystemManager', {
    get: SystemManager_getInstance
  });
  package$system_0.SystemData = SystemData;
  _.main_kand9s$ = main;
  Object.defineProperty(Rgba, 'Companion', {
    get: Rgba$Companion_getInstance
  });
  var package$utility = _.utility || (_.utility = {});
  package$utility.Rgba_init_tjonv8$ = Rgba_init;
  package$utility.Rgba = Rgba;
  main([]);
  Kotlin.defineModule('mi-adp-ecs-game', _);
  return _;
}));

//# sourceMappingURL=mi-adp-ecs-game.js.map
