var _ = require('underscore');
var Qunit = require('qunitjs');
var State = require('../../js/amp/services/implementations/state');


QUnit.module('State Service')


function EventRecorder(listenTo) {
  // listens to things that implement the Backbone events API
  this._memory = {};
  listenTo.on('all', function(eventName) { this._memory[eventName] = true; }, this);
  this.heard = function(eventName) { return _.has(this._memory, eventName); };
  this.reset = function() { this._memory = {}; };
}

function Registrable() {
  // something that have its state saved and loaded
  this.message;
  this.getter = function() { return this.message; };
  this.setter = function(state) { this.message = state };
}


QUnit.test('Empty State', function(assert) {
  var emptyState = new State();
  var listener = new EventRecorder(emptyState);

  var frozenEmptyState = emptyState.freeze();
  assert.ok(listener.heard('freeze'), 'Freeze event triggered on freeze');
  assert.equal(frozenEmptyState, '{}', 'Empty frozen state is empty');
  listener.reset();

  emptyState.reset();
  assert.ok(listener.heard('reset'), 'Reset event fired on reset');
  assert.ok(! listener.heard('change'), 'Changed event did not fire on noop reset');
  listener.reset();

  emptyState.load("{}");
  assert.ok(listener.heard('load'), 'Load event fired on load');
  assert.ok(! listener.heard('change'), 'Changed event did not fire on noop load');
  listener.reset();

  emptyState.register({}, 'thing', {set: function() {}, get: function() {}});
  assert.ok(listener.heard('register'), 'Register event fired on state registration');
});


QUnit.test('Register and Reset', function(assert) {
  var state = new State(),
      thing = new Registrable(),
      listener = new EventRecorder(state);

  state.register(thing, 'thing', {get: thing.getter, set: thing.setter, empty: 'hello'});

  var frozenState = state.freeze();
  assert.equal(JSON.parse(frozenState).thing, 'hello', 'Empty state is frozen to default empty');

  thing.message = 'world';
  frozenState = state.freeze();
  assert.equal(JSON.parse(frozenState).thing, 'world', 'Updated state freezes to the new value');

  listener.reset();
  state.reset();
  assert.ok(listener.heard('change'), 'Changed event fired when a different state was applied from reset');
  var frozenState = state.freeze();
  assert.equal(JSON.parse(frozenState).thing, 'hello', 'Resetting reverts state to its empty value');
});


QUnit.test('Load and Preload', function(assert) {
  var state = new State(),
      thing = new Registrable(),
      listener = new EventRecorder(state);

  state.register(thing, 'thing', {get: thing.getter, set: thing.setter, empty: 'hello'});
  listener.reset();
  state.load('{"thing": "world"}');
  assert.ok(listener.heard('change'), 'load event triggered on load of a new state');
  assert.equal(thing.message, 'world', 'load event changed the registrable');

  var preloadedState = new State(),
      thing2 = new Registrable();
  preloadedState.load('{"thing2": "world"}');
  preloadedState.register(thing2, 'thing2', {get: thing2.getter, set: thing2.setter, empty: 'hello'});
  assert.equal(thing2.message, 'world', 'preloaded state was applied to the registrable');
});
