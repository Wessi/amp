/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;
/******/
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	
	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }
	
	var _indexJsx = __webpack_require__(1);
	
	var _ampArchitecture = __webpack_require__(3);
	
	var _ampTemplateNode_modulesAmpBoilerplateDistAmpBoilerplateJs = __webpack_require__(50);
	
	var _ampTemplateNode_modulesAmpBoilerplateDistAmpBoilerplateJs2 = _interopRequireDefault(_ampTemplateNode_modulesAmpBoilerplateDistAmpBoilerplateJs);
	
	new _ampTemplateNode_modulesAmpBoilerplateDistAmpBoilerplateJs2["default"].layout({});
	
	(0, _indexJsx.init)().then(function (model) {
	  return (0, _ampArchitecture.run)({
	    model: model,
	    view: _indexJsx.view,
	    update: _indexJsx.update,
	    element: document.getElementById('main-container')
	  });
	});

/***/ },
/* 1 */
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(fetch) {/** @jsx h */
	"use strict";
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	
	var _slicedToArray = (function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; })();
	
	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();
	
	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; desc = parent = getter = undefined; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; continue _function; } } else if ("value" in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };
	
	exports.view = view;
	exports.update = update;
	
	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }
	
	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj["default"] = obj; return newObj; } }
	
	function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) arr2[i] = arr[i]; return arr2; } else { return Array.from(arr); } }
	
	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }
	
	function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }
	
	var _ampArchitecture = __webpack_require__(3);
	
	var AMP = _interopRequireWildcard(_ampArchitecture);
	
	var _ampModulesTranslate = __webpack_require__(40);
	
	var _ampModulesTranslate2 = _interopRequireDefault(_ampModulesTranslate);
	
	var _rate = __webpack_require__(42);
	
	var Rate = _interopRequireWildcard(_rate);
	
	var _newRate = __webpack_require__(49);
	
	var NewRate = _interopRequireWildcard(_newRate);
	
	var _classnames = __webpack_require__(47);
	
	var _classnames2 = _interopRequireDefault(_classnames);
	
	var _ampToolsValidate = __webpack_require__(48);
	
	var h = AMP.h;
	
	var Action = (function (_AMP$Action) {
	  _inherits(Action, _AMP$Action);
	
	  function Action() {
	    _classCallCheck(this, Action);
	
	    _get(Object.getPrototypeOf(Action.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Action;
	})(AMP.Action);
	
	exports.Action = Action;
	
	var Save = (function (_Action) {
	  _inherits(Save, _Action);
	
	  function Save() {
	    _classCallCheck(this, Save);
	
	    _get(Object.getPrototypeOf(Save.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Save;
	})(Action);
	
	var RateAction = (function (_AMP$Package) {
	  _inherits(RateAction, _AMP$Package);
	
	  function RateAction() {
	    _classCallCheck(this, RateAction);
	
	    _get(Object.getPrototypeOf(RateAction.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return RateAction;
	})(AMP.Package);
	
	var SaveSideEffect = (function (_AMP$SideEffect) {
	  _inherits(SaveSideEffect, _AMP$SideEffect);
	
	  function SaveSideEffect(model) {
	    _classCallCheck(this, SaveSideEffect);
	
	    _get(Object.getPrototypeOf(SaveSideEffect.prototype), "constructor", this).call(this, model, function (address) {
	      var currentCurrency = model.current().currentCurrency();
	      var currencyCode = currentCurrency.code();
	      fetch("/rest/currencies/setInflationRate/" + currencyCode, {
	        method: 'post',
	        credentials: 'same-origin',
	        headers: {
	          'Accept': 'application/json',
	          'Content-Type': 'application/json'
	        },
	        body: JSON.stringify({
	          rates: currentCurrency.inflationRates().map(function (entry) {
	            return entry.year(parseInt).inflationRate(parseFloat).toJS();
	          })
	        })
	      });
	    });
	  }
	
	  return SaveSideEffect;
	})(AMP.SideEffect);
	
	var InflationRates = (function (_AMP$Model) {
	  _inherits(InflationRates, _AMP$Model);
	
	  function InflationRates(mutationOrData) {
	    _classCallCheck(this, InflationRates);
	
	    _get(Object.getPrototypeOf(InflationRates.prototype), "constructor", this).call(this, mutationOrData);
	    var startYear = Math.min.apply(Math, _toConsumableArray(_get(Object.getPrototypeOf(InflationRates.prototype), "keys", this).call(this)));
	    var endYear = Math.max.apply(Math, _toConsumableArray(_get(Object.getPrototypeOf(InflationRates.prototype), "keys", this).call(this)));
	    this.startYear = function () {
	      return startYear;
	    };
	    this.endYear = function () {
	      return endYear;
	    };
	  }
	
	  _createClass(InflationRates, [{
	    key: "get",
	    value: function get(key) {
	      return !_get(Object.getPrototypeOf(InflationRates.prototype), "get", this).call(this, key) && this.startYear() < key && key < this.endYear() ? Rate.model.set('year', key) : _get(Object.getPrototypeOf(InflationRates.prototype), "get", this).call(this, key);
	    }
	  }, {
	    key: "keys",
	    value: function keys() {
	      var arr = [];
	      for (var year = this.startYear(); year <= this.endYear(); year++) {
	        arr.push(year);
	      }
	      return arr;
	    }
	  }]);
	
	  return InflationRates;
	})(AMP.Model);
	
	var Currency = (function (_AMP$Model2) {
	  _inherits(Currency, _AMP$Model2);
	
	  function Currency() {
	    _classCallCheck(this, Currency);
	
	    _get(Object.getPrototypeOf(Currency.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Currency;
	})(AMP.Model);
	
	var Currencies = (function (_AMP$Model3) {
	  _inherits(Currencies, _AMP$Model3);
	
	  function Currencies() {
	    _classCallCheck(this, Currencies);
	
	    _get(Object.getPrototypeOf(Currencies.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Currencies;
	})(AMP.Model);
	
	var State = (function (_AMP$Model4) {
	  _inherits(State, _AMP$Model4);
	
	  function State() {
	    _classCallCheck(this, State);
	
	    _get(Object.getPrototypeOf(State.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  _createClass(State, [{
	    key: "currentCurrency",
	    value: function currentCurrency() {
	      return this.currencies().get(this.currentCurrencyCode());
	    }
	  }]);
	
	  return State;
	})(AMP.Model);
	
	var Model = (function (_AMP$Model5) {
	  _inherits(Model, _AMP$Model5);
	
	  function Model() {
	    _classCallCheck(this, Model);
	
	    _get(Object.getPrototypeOf(Model.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Model;
	})(AMP.Model);
	
	var model = new Model();
	
	var callFunc = function callFunc(func) {
	  return function (obj) {
	    return obj[func]();
	  };
	};
	
	var fetchJson = function fetchJson(url) {
	  return fetch(url, { credentials: 'same-origin' }).then(callFunc('json'));
	};
	
	var ensureArray = function ensureArray(maybeArray) {
	  return Array.isArray(maybeArray) ? maybeArray : [];
	};
	
	//We need to talk to endpoints in order to figure out our model, so this function will return a promise that will resolve
	//with the model once we have all the data we need
	var init = function init() {
	  return Promise.all([fetchJson('/rest/currencies/inflatableCurrencies'), fetchJson('/rest/currencies/getInflationRates')]).then(function (_ref) {
	    var _ref2 = _slicedToArray(_ref, 2);
	
	    var currencies = _ref2[0];
	    var inflationRates = _ref2[1];
	
	    var getInflationRatesFor = function getInflationRatesFor(code) {
	      return ensureArray(inflationRates[code]).map(function (inflationRate) {
	        return new Rate.Model(inflationRate);
	      }).reduce(function (inflationRates, inflationRate) {
	        return inflationRates.set(inflationRate.year(), inflationRate);
	      }, new InflationRates());
	    };
	    var state = new State({
	      newRate: NewRate.model,
	      currentCurrencyCode: currencies[0]['code'],
	      currencies: currencies.map(function (currency) {
	        return new Currency(currency);
	      }).map(function (currency) {
	        return currency.set('inflationRates', getInflationRatesFor(currency.code()));
	      }).reduce(function (currencies, currency) {
	        return currencies.set(currency.code(), currency);
	      }, new Currencies())
	    });
	    return new Model({
	      saved: state,
	      current: state
	    });
	  });
	};
	
	exports.init = init;
	
	function view(address, model) {
	  if (!(model instanceof Model)) throw new TypeError("Value of argument 'model' violates contract, expected Model got " + (model === null ? "null" : model instanceof Object && model.constructor ? model.constructor.name : typeof model));
	
	  var state = model.current();
	  var haveChanges = !model.saved().equals(state);
	  var currentCurrency = state.currentCurrency();
	  var currentInflationRates = currentCurrency.inflationRates();
	  return h(
	    "div",
	    { className: "container" },
	    h(
	      "div",
	      { className: "row" },
	      h(
	        "div",
	        { className: "col-md-12" },
	        h(
	          "table",
	          { className: "table table-striped" },
	          h(
	            "caption",
	            null,
	            h(
	              "h2",
	              null,
	              (0, _ampModulesTranslate2["default"])('Inflation rates for'),
	              " ",
	              currentCurrency.name(),
	              "(",
	              currentCurrency.code(),
	              ")"
	            )
	          ),
	          h(
	            "thead",
	            null,
	            h(
	              "tr",
	              null,
	              h(
	                "th",
	                null,
	                (0, _ampModulesTranslate2["default"])('Year')
	              ),
	              h(
	                "th",
	                null,
	                (0, _ampModulesTranslate2["default"])('Inflation(%)')
	              ),
	              h(
	                "th",
	                null,
	                (0, _ampModulesTranslate2["default"])('Constant currency')
	              ),
	              h(
	                "th",
	                null,
	                (0, _ampModulesTranslate2["default"])('Delete')
	              )
	            )
	          ),
	          h(
	            "tbody",
	            null,
	            currentInflationRates.map(function (rate) {
	              var isFirstOrLast = rate.year() == currentInflationRates.startYear() || rate.year() == currentInflationRates.endYear();
	              var inflationRate = rate.inflationRate();
	              var isValid = parseFloat(inflationRate) == inflationRate;
	              var model = rate.set('deletable', false).deletable(isFirstOrLast).set('valid', isValid);
	              return Rate.view(address.usePackage(RateAction, rate.get('year')), model);
	            })
	          ),
	          h(
	            "tfoot",
	            null,
	            h(
	              "tr",
	              null,
	              NewRate.view(address, state.newRate()),
	              h(
	                "td",
	                { colSpan: "3", className: "text-right" },
	                h(
	                  "button",
	                  {
	                    className: (0, _classnames2["default"])("btn btn-success", { disabled: !haveChanges }),
	                    disabled: !haveChanges,
	                    onclick: function (e) {
	                      return address.send(new Save());
	                    }
	                  },
	                  (0, _ampModulesTranslate2["default"])('Save')
	                )
	              )
	            )
	          )
	        )
	      )
	    )
	  );
	}
	
	function update(action, model) {
	  if (!action) throw new TypeError("Value of argument 'action' violates contract, expected any got " + (action === null ? "null" : action instanceof Object && action.constructor ? action.constructor.name : typeof action));
	  if (!(model instanceof Model)) throw new TypeError("Value of argument 'model' violates contract, expected Model got " + (model === null ? "null" : model instanceof Object && model.constructor ? model.constructor.name : typeof model));
	
	  if (action instanceof Save) {
	    return new SaveSideEffect(model);
	  }
	  if (action instanceof RateAction) {
	    var path = ['current', 'currencies', model.current().currentCurrencyCode(), 'inflationRates', action.getTag()];
	    var originalAction = action.unpack();
	    if (originalAction instanceof Rate.Delete) {
	      return model.unsetIn(path);
	    }
	    return AMP.updateSubmodel(path, Rate.update, originalAction, model);
	  }
	  if (action instanceof NewRate.Action) {
	    if (action instanceof NewRate.YearSubmitted) {
	      var path = ['current', 'currencies', model.current().currentCurrencyCode(), 'inflationRates', action.year()];
	      return !model.hasIn(path) && _ampToolsValidate.MIN_YEAR <= action.year() && action.year() <= _ampToolsValidate.MAX_YEAR ? model.setIn(path, Rate.model.year(action.year())) : model;
	    }
	    return AMP.updateSubmodel(['current', 'newRate'], NewRate.update, action, model);
	  }
	}
	/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(2)))

/***/ },
/* 2 */
/***/ function(module, exports) {

	/* WEBPACK VAR INJECTION */(function(global) {/*** IMPORTS FROM imports-loader ***/
	(function() {
	
	(function() {
	  'use strict';
	
	  if (self.fetch) {
	    return
	  }
	
	  function normalizeName(name) {
	    if (typeof name !== 'string') {
	      name = name.toString();
	    }
	    if (/[^a-z0-9\-#$%&'*+.\^_`|~]/i.test(name)) {
	      throw new TypeError('Invalid character in header field name')
	    }
	    return name.toLowerCase()
	  }
	
	  function normalizeValue(value) {
	    if (typeof value !== 'string') {
	      value = value.toString();
	    }
	    return value
	  }
	
	  function Headers(headers) {
	    this.map = {}
	
	    if (headers instanceof Headers) {
	      headers.forEach(function(value, name) {
	        this.append(name, value)
	      }, this)
	
	    } else if (headers) {
	      Object.getOwnPropertyNames(headers).forEach(function(name) {
	        this.append(name, headers[name])
	      }, this)
	    }
	  }
	
	  Headers.prototype.append = function(name, value) {
	    name = normalizeName(name)
	    value = normalizeValue(value)
	    var list = this.map[name]
	    if (!list) {
	      list = []
	      this.map[name] = list
	    }
	    list.push(value)
	  }
	
	  Headers.prototype['delete'] = function(name) {
	    delete this.map[normalizeName(name)]
	  }
	
	  Headers.prototype.get = function(name) {
	    var values = this.map[normalizeName(name)]
	    return values ? values[0] : null
	  }
	
	  Headers.prototype.getAll = function(name) {
	    return this.map[normalizeName(name)] || []
	  }
	
	  Headers.prototype.has = function(name) {
	    return this.map.hasOwnProperty(normalizeName(name))
	  }
	
	  Headers.prototype.set = function(name, value) {
	    this.map[normalizeName(name)] = [normalizeValue(value)]
	  }
	
	  Headers.prototype.forEach = function(callback, thisArg) {
	    Object.getOwnPropertyNames(this.map).forEach(function(name) {
	      this.map[name].forEach(function(value) {
	        callback.call(thisArg, value, name, this)
	      }, this)
	    }, this)
	  }
	
	  function consumed(body) {
	    if (body.bodyUsed) {
	      return Promise.reject(new TypeError('Already read'))
	    }
	    body.bodyUsed = true
	  }
	
	  function fileReaderReady(reader) {
	    return new Promise(function(resolve, reject) {
	      reader.onload = function() {
	        resolve(reader.result)
	      }
	      reader.onerror = function() {
	        reject(reader.error)
	      }
	    })
	  }
	
	  function readBlobAsArrayBuffer(blob) {
	    var reader = new FileReader()
	    reader.readAsArrayBuffer(blob)
	    return fileReaderReady(reader)
	  }
	
	  function readBlobAsText(blob) {
	    var reader = new FileReader()
	    reader.readAsText(blob)
	    return fileReaderReady(reader)
	  }
	
	  var support = {
	    blob: 'FileReader' in self && 'Blob' in self && (function() {
	      try {
	        new Blob();
	        return true
	      } catch(e) {
	        return false
	      }
	    })(),
	    formData: 'FormData' in self
	  }
	
	  function Body() {
	    this.bodyUsed = false
	
	
	    this._initBody = function(body) {
	      this._bodyInit = body
	      if (typeof body === 'string') {
	        this._bodyText = body
	      } else if (support.blob && Blob.prototype.isPrototypeOf(body)) {
	        this._bodyBlob = body
	      } else if (support.formData && FormData.prototype.isPrototypeOf(body)) {
	        this._bodyFormData = body
	      } else if (!body) {
	        this._bodyText = ''
	      } else {
	        throw new Error('unsupported BodyInit type')
	      }
	    }
	
	    if (support.blob) {
	      this.blob = function() {
	        var rejected = consumed(this)
	        if (rejected) {
	          return rejected
	        }
	
	        if (this._bodyBlob) {
	          return Promise.resolve(this._bodyBlob)
	        } else if (this._bodyFormData) {
	          throw new Error('could not read FormData body as blob')
	        } else {
	          return Promise.resolve(new Blob([this._bodyText]))
	        }
	      }
	
	      this.arrayBuffer = function() {
	        return this.blob().then(readBlobAsArrayBuffer)
	      }
	
	      this.text = function() {
	        var rejected = consumed(this)
	        if (rejected) {
	          return rejected
	        }
	
	        if (this._bodyBlob) {
	          return readBlobAsText(this._bodyBlob)
	        } else if (this._bodyFormData) {
	          throw new Error('could not read FormData body as text')
	        } else {
	          return Promise.resolve(this._bodyText)
	        }
	      }
	    } else {
	      this.text = function() {
	        var rejected = consumed(this)
	        return rejected ? rejected : Promise.resolve(this._bodyText)
	      }
	    }
	
	    if (support.formData) {
	      this.formData = function() {
	        return this.text().then(decode)
	      }
	    }
	
	    this.json = function() {
	      return this.text().then(JSON.parse)
	    }
	
	    return this
	  }
	
	  // HTTP methods whose capitalization should be normalized
	  var methods = ['DELETE', 'GET', 'HEAD', 'OPTIONS', 'POST', 'PUT']
	
	  function normalizeMethod(method) {
	    var upcased = method.toUpperCase()
	    return (methods.indexOf(upcased) > -1) ? upcased : method
	  }
	
	  function Request(url, options) {
	    options = options || {}
	    this.url = url
	
	    this.credentials = options.credentials || 'omit'
	    this.headers = new Headers(options.headers)
	    this.method = normalizeMethod(options.method || 'GET')
	    this.mode = options.mode || null
	    this.referrer = null
	
	    if ((this.method === 'GET' || this.method === 'HEAD') && options.body) {
	      throw new TypeError('Body not allowed for GET or HEAD requests')
	    }
	    this._initBody(options.body)
	  }
	
	  function decode(body) {
	    var form = new FormData()
	    body.trim().split('&').forEach(function(bytes) {
	      if (bytes) {
	        var split = bytes.split('=')
	        var name = split.shift().replace(/\+/g, ' ')
	        var value = split.join('=').replace(/\+/g, ' ')
	        form.append(decodeURIComponent(name), decodeURIComponent(value))
	      }
	    })
	    return form
	  }
	
	  function headers(xhr) {
	    var head = new Headers()
	    var pairs = xhr.getAllResponseHeaders().trim().split('\n')
	    pairs.forEach(function(header) {
	      var split = header.trim().split(':')
	      var key = split.shift().trim()
	      var value = split.join(':').trim()
	      head.append(key, value)
	    })
	    return head
	  }
	
	  Body.call(Request.prototype)
	
	  function Response(bodyInit, options) {
	    if (!options) {
	      options = {}
	    }
	
	    this._initBody(bodyInit)
	    this.type = 'default'
	    this.url = null
	    this.status = options.status
	    this.ok = this.status >= 200 && this.status < 300
	    this.statusText = options.statusText
	    this.headers = options.headers instanceof Headers ? options.headers : new Headers(options.headers)
	    this.url = options.url || ''
	  }
	
	  Body.call(Response.prototype)
	
	  self.Headers = Headers;
	  self.Request = Request;
	  self.Response = Response;
	
	  self.fetch = function(input, init) {
	    // TODO: Request constructor should accept input, init
	    var request
	    if (Request.prototype.isPrototypeOf(input) && !init) {
	      request = input
	    } else {
	      request = new Request(input, init)
	    }
	
	    return new Promise(function(resolve, reject) {
	      var xhr = new XMLHttpRequest()
	
	      function responseURL() {
	        if ('responseURL' in xhr) {
	          return xhr.responseURL
	        }
	
	        // Avoid security warnings on getResponseHeader when not allowed by CORS
	        if (/^X-Request-URL:/m.test(xhr.getAllResponseHeaders())) {
	          return xhr.getResponseHeader('X-Request-URL')
	        }
	
	        return;
	      }
	
	      xhr.onload = function() {
	        var status = (xhr.status === 1223) ? 204 : xhr.status
	        if (status < 100 || status > 599) {
	          reject(new TypeError('Network request failed'))
	          return
	        }
	        var options = {
	          status: status,
	          statusText: xhr.statusText,
	          headers: headers(xhr),
	          url: responseURL()
	        }
	        var body = 'response' in xhr ? xhr.response : xhr.responseText;
	        resolve(new Response(body, options))
	      }
	
	      xhr.onerror = function() {
	        reject(new TypeError('Network request failed'))
	      }
	
	      xhr.open(request.method, request.url, true)
	
	      if (request.credentials === 'include') {
	        xhr.withCredentials = true
	      }
	
	      if ('responseType' in xhr && support.blob) {
	        xhr.responseType = 'blob'
	      }
	
	      request.headers.forEach(function(value, name) {
	        xhr.setRequestHeader(name, value)
	      })
	
	      xhr.send(typeof request._bodyInit === 'undefined' ? null : request._bodyInit)
	    })
	  }
	  self.fetch.polyfill = true
	})();
	
	
	/*** EXPORTS FROM exports-loader ***/
	module.exports = global.fetch}.call(global));
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },
/* 3 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	
	var _slicedToArray = (function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; })();
	
	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; desc = parent = getter = undefined; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; continue _function; } } else if ("value" in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };
	
	exports.run = run;
	exports.updateSubmodel = updateSubmodel;
	
	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }
	
	function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }
	
	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }
	
	var _virtualDom = __webpack_require__(4);
	
	var _virtualDomCreateElement = __webpack_require__(38);
	
	var _virtualDomCreateElement2 = _interopRequireDefault(_virtualDomCreateElement);
	
	var _model = __webpack_require__(39);
	
	var _model2 = _interopRequireDefault(_model);
	
	function hWithSpread(el, props) {
	  for (var _len = arguments.length, children = Array(_len > 2 ? _len - 2 : 0), _key = 2; _key < _len; _key++) {
	    children[_key - 2] = arguments[_key];
	  }
	
	  return (0, _virtualDom.h)(el, props, children);
	}
	
	exports.h = hWithSpread;
	
	var Action = function Action() {
	  _classCallCheck(this, Action);
	};
	
	exports.Action = Action;
	
	var Package = (function (_Action) {
	  _inherits(Package, _Action);
	
	  /**
	   *
	   * @param tag {any}
	   * @param originalAction {Action}
	   */
	
	  function Package(tag, originalAction) {
	    _classCallCheck(this, Package);
	
	    if (!(originalAction instanceof Action)) throw new TypeError("Value of argument 'originalAction' violates contract, expected Action got " + (originalAction === null ? "null" : originalAction instanceof Object && originalAction.constructor ? originalAction.constructor.name : typeof originalAction));
	
	    _get(Object.getPrototypeOf(Package.prototype), "constructor", this).call(this);
	    this.getTag = function () {
	      return tag;
	    };
	    this.unpack = function () {
	      return originalAction;
	    };
	  }
	
	  return Package;
	})(Action);
	
	exports.Package = Package;
	
	var SideEffect = function SideEffect(model, cb) {
	  _classCallCheck(this, SideEffect);
	
	  if (!(model instanceof _model2["default"])) throw new TypeError("Value of argument 'model' violates contract, expected Model got " + (model === null ? "null" : model instanceof Object && model.constructor ? model.constructor.name : typeof model));
	
	  var ran = false;
	  this.model = model;
	  this.unleash = function (address) {
	    if (ran) {
	      throw {
	        name: "SideEffectException",
	        message: "Side effect already unleashed!"
	      };
	    } else {
	      ran = true;
	      cb(address);
	    }
	  };
	};
	
	exports.SideEffect = SideEffect;
	
	function run(_ref) {
	  var model = _ref.model;
	  var view = _ref.view;
	  var update = _ref.update;
	  var element = _ref.element;
	
	  function makeAddress(packages) {
	    if (!Array.isArray(packages)) throw new TypeError("Value of argument 'packages' violates contract, expected array got " + (packages === null ? "null" : packages instanceof Object && packages.constructor ? packages.constructor.name : typeof packages));
	
	    return {
	      send: function send(_action) {
	        var action = packages.reduceRight(function (prev, curr) {
	          var _curr = _slicedToArray(curr, 2);
	
	          var PackageClass = _curr[0];
	          var tag = _curr[1];
	
	          return new PackageClass(tag, prev);
	        }, _action);
	        var response = update(action, state);
	        var newState = response instanceof SideEffect ? response.model : response;
	        if (state != newState) {
	          state = newState;
	          var newTree = view(address, state);
	          var patches = (0, _virtualDom.diff)(tree, newTree);
	          node = (0, _virtualDom.patch)(node, patches);
	          tree = newTree;
	        }
	        if (response instanceof SideEffect) {
	          response.unleash(address);
	        }
	      },
	      /**
	       * @param PackageClass {Package}
	       * @param tag {any}
	       */
	      usePackage: function usePackage(PackageClass, tag) {
	        return makeAddress(packages.concat([[PackageClass, tag]]));
	      }
	    };
	  }
	  var state = model;
	  var address = makeAddress([]);
	  var elementParent = element.parentNode;
	  var tree = view(address, state);
	  var node = (0, _virtualDomCreateElement2["default"])(tree);
	  elementParent.replaceChild(node, element);
	  return address;
	}
	
	function updateSubmodel(path, update, action, model) {
	  if (!Array.isArray(path)) throw new TypeError("Value of argument 'path' violates contract, expected array got " + (path === null ? "null" : path instanceof Object && path.constructor ? path.constructor.name : typeof path));
	
	  var response = update(action, model.getIn(path));
	  if (response instanceof SideEffect) {
	    response.model = model.setIn(path, response.model);
	    return response;
	  } else if (response instanceof _model2["default"]) {
	    return model.setIn(path, response);
	  }
	}
	
	exports.Model = _model2["default"];

/***/ },
/* 4 */
/***/ function(module, exports, __webpack_require__) {

	var diff = __webpack_require__(5)
	var patch = __webpack_require__(18)
	var h = __webpack_require__(27)
	var create = __webpack_require__(38)
	var VNode = __webpack_require__(29)
	var VText = __webpack_require__(30)
	
	module.exports = {
	    diff: diff,
	    patch: patch,
	    h: h,
	    create: create,
	    VNode: VNode,
	    VText: VText
	}


/***/ },
/* 5 */
/***/ function(module, exports, __webpack_require__) {

	var diff = __webpack_require__(6)
	
	module.exports = diff


/***/ },
/* 6 */
/***/ function(module, exports, __webpack_require__) {

	var isArray = __webpack_require__(7)
	
	var VPatch = __webpack_require__(8)
	var isVNode = __webpack_require__(10)
	var isVText = __webpack_require__(11)
	var isWidget = __webpack_require__(12)
	var isThunk = __webpack_require__(13)
	var handleThunk = __webpack_require__(14)
	
	var diffProps = __webpack_require__(15)
	
	module.exports = diff
	
	function diff(a, b) {
	    var patch = { a: a }
	    walk(a, b, patch, 0)
	    return patch
	}
	
	function walk(a, b, patch, index) {
	    if (a === b) {
	        return
	    }
	
	    var apply = patch[index]
	    var applyClear = false
	
	    if (isThunk(a) || isThunk(b)) {
	        thunks(a, b, patch, index)
	    } else if (b == null) {
	
	        // If a is a widget we will add a remove patch for it
	        // Otherwise any child widgets/hooks must be destroyed.
	        // This prevents adding two remove patches for a widget.
	        if (!isWidget(a)) {
	            clearState(a, patch, index)
	            apply = patch[index]
	        }
	
	        apply = appendPatch(apply, new VPatch(VPatch.REMOVE, a, b))
	    } else if (isVNode(b)) {
	        if (isVNode(a)) {
	            if (a.tagName === b.tagName &&
	                a.namespace === b.namespace &&
	                a.key === b.key) {
	                var propsPatch = diffProps(a.properties, b.properties)
	                if (propsPatch) {
	                    apply = appendPatch(apply,
	                        new VPatch(VPatch.PROPS, a, propsPatch))
	                }
	                apply = diffChildren(a, b, patch, apply, index)
	            } else {
	                apply = appendPatch(apply, new VPatch(VPatch.VNODE, a, b))
	                applyClear = true
	            }
	        } else {
	            apply = appendPatch(apply, new VPatch(VPatch.VNODE, a, b))
	            applyClear = true
	        }
	    } else if (isVText(b)) {
	        if (!isVText(a)) {
	            apply = appendPatch(apply, new VPatch(VPatch.VTEXT, a, b))
	            applyClear = true
	        } else if (a.text !== b.text) {
	            apply = appendPatch(apply, new VPatch(VPatch.VTEXT, a, b))
	        }
	    } else if (isWidget(b)) {
	        if (!isWidget(a)) {
	            applyClear = true
	        }
	
	        apply = appendPatch(apply, new VPatch(VPatch.WIDGET, a, b))
	    }
	
	    if (apply) {
	        patch[index] = apply
	    }
	
	    if (applyClear) {
	        clearState(a, patch, index)
	    }
	}
	
	function diffChildren(a, b, patch, apply, index) {
	    var aChildren = a.children
	    var orderedSet = reorder(aChildren, b.children)
	    var bChildren = orderedSet.children
	
	    var aLen = aChildren.length
	    var bLen = bChildren.length
	    var len = aLen > bLen ? aLen : bLen
	
	    for (var i = 0; i < len; i++) {
	        var leftNode = aChildren[i]
	        var rightNode = bChildren[i]
	        index += 1
	
	        if (!leftNode) {
	            if (rightNode) {
	                // Excess nodes in b need to be added
	                apply = appendPatch(apply,
	                    new VPatch(VPatch.INSERT, null, rightNode))
	            }
	        } else {
	            walk(leftNode, rightNode, patch, index)
	        }
	
	        if (isVNode(leftNode) && leftNode.count) {
	            index += leftNode.count
	        }
	    }
	
	    if (orderedSet.moves) {
	        // Reorder nodes last
	        apply = appendPatch(apply, new VPatch(
	            VPatch.ORDER,
	            a,
	            orderedSet.moves
	        ))
	    }
	
	    return apply
	}
	
	function clearState(vNode, patch, index) {
	    // TODO: Make this a single walk, not two
	    unhook(vNode, patch, index)
	    destroyWidgets(vNode, patch, index)
	}
	
	// Patch records for all destroyed widgets must be added because we need
	// a DOM node reference for the destroy function
	function destroyWidgets(vNode, patch, index) {
	    if (isWidget(vNode)) {
	        if (typeof vNode.destroy === "function") {
	            patch[index] = appendPatch(
	                patch[index],
	                new VPatch(VPatch.REMOVE, vNode, null)
	            )
	        }
	    } else if (isVNode(vNode) && (vNode.hasWidgets || vNode.hasThunks)) {
	        var children = vNode.children
	        var len = children.length
	        for (var i = 0; i < len; i++) {
	            var child = children[i]
	            index += 1
	
	            destroyWidgets(child, patch, index)
	
	            if (isVNode(child) && child.count) {
	                index += child.count
	            }
	        }
	    } else if (isThunk(vNode)) {
	        thunks(vNode, null, patch, index)
	    }
	}
	
	// Create a sub-patch for thunks
	function thunks(a, b, patch, index) {
	    var nodes = handleThunk(a, b)
	    var thunkPatch = diff(nodes.a, nodes.b)
	    if (hasPatches(thunkPatch)) {
	        patch[index] = new VPatch(VPatch.THUNK, null, thunkPatch)
	    }
	}
	
	function hasPatches(patch) {
	    for (var index in patch) {
	        if (index !== "a") {
	            return true
	        }
	    }
	
	    return false
	}
	
	// Execute hooks when two nodes are identical
	function unhook(vNode, patch, index) {
	    if (isVNode(vNode)) {
	        if (vNode.hooks) {
	            patch[index] = appendPatch(
	                patch[index],
	                new VPatch(
	                    VPatch.PROPS,
	                    vNode,
	                    undefinedKeys(vNode.hooks)
	                )
	            )
	        }
	
	        if (vNode.descendantHooks || vNode.hasThunks) {
	            var children = vNode.children
	            var len = children.length
	            for (var i = 0; i < len; i++) {
	                var child = children[i]
	                index += 1
	
	                unhook(child, patch, index)
	
	                if (isVNode(child) && child.count) {
	                    index += child.count
	                }
	            }
	        }
	    } else if (isThunk(vNode)) {
	        thunks(vNode, null, patch, index)
	    }
	}
	
	function undefinedKeys(obj) {
	    var result = {}
	
	    for (var key in obj) {
	        result[key] = undefined
	    }
	
	    return result
	}
	
	// List diff, naive left to right reordering
	function reorder(aChildren, bChildren) {
	    // O(M) time, O(M) memory
	    var bChildIndex = keyIndex(bChildren)
	    var bKeys = bChildIndex.keys
	    var bFree = bChildIndex.free
	
	    if (bFree.length === bChildren.length) {
	        return {
	            children: bChildren,
	            moves: null
	        }
	    }
	
	    // O(N) time, O(N) memory
	    var aChildIndex = keyIndex(aChildren)
	    var aKeys = aChildIndex.keys
	    var aFree = aChildIndex.free
	
	    if (aFree.length === aChildren.length) {
	        return {
	            children: bChildren,
	            moves: null
	        }
	    }
	
	    // O(MAX(N, M)) memory
	    var newChildren = []
	
	    var freeIndex = 0
	    var freeCount = bFree.length
	    var deletedItems = 0
	
	    // Iterate through a and match a node in b
	    // O(N) time,
	    for (var i = 0 ; i < aChildren.length; i++) {
	        var aItem = aChildren[i]
	        var itemIndex
	
	        if (aItem.key) {
	            if (bKeys.hasOwnProperty(aItem.key)) {
	                // Match up the old keys
	                itemIndex = bKeys[aItem.key]
	                newChildren.push(bChildren[itemIndex])
	
	            } else {
	                // Remove old keyed items
	                itemIndex = i - deletedItems++
	                newChildren.push(null)
	            }
	        } else {
	            // Match the item in a with the next free item in b
	            if (freeIndex < freeCount) {
	                itemIndex = bFree[freeIndex++]
	                newChildren.push(bChildren[itemIndex])
	            } else {
	                // There are no free items in b to match with
	                // the free items in a, so the extra free nodes
	                // are deleted.
	                itemIndex = i - deletedItems++
	                newChildren.push(null)
	            }
	        }
	    }
	
	    var lastFreeIndex = freeIndex >= bFree.length ?
	        bChildren.length :
	        bFree[freeIndex]
	
	    // Iterate through b and append any new keys
	    // O(M) time
	    for (var j = 0; j < bChildren.length; j++) {
	        var newItem = bChildren[j]
	
	        if (newItem.key) {
	            if (!aKeys.hasOwnProperty(newItem.key)) {
	                // Add any new keyed items
	                // We are adding new items to the end and then sorting them
	                // in place. In future we should insert new items in place.
	                newChildren.push(newItem)
	            }
	        } else if (j >= lastFreeIndex) {
	            // Add any leftover non-keyed items
	            newChildren.push(newItem)
	        }
	    }
	
	    var simulate = newChildren.slice()
	    var simulateIndex = 0
	    var removes = []
	    var inserts = []
	    var simulateItem
	
	    for (var k = 0; k < bChildren.length;) {
	        var wantedItem = bChildren[k]
	        simulateItem = simulate[simulateIndex]
	
	        // remove items
	        while (simulateItem === null && simulate.length) {
	            removes.push(remove(simulate, simulateIndex, null))
	            simulateItem = simulate[simulateIndex]
	        }
	
	        if (!simulateItem || simulateItem.key !== wantedItem.key) {
	            // if we need a key in this position...
	            if (wantedItem.key) {
	                if (simulateItem && simulateItem.key) {
	                    // if an insert doesn't put this key in place, it needs to move
	                    if (bKeys[simulateItem.key] !== k + 1) {
	                        removes.push(remove(simulate, simulateIndex, simulateItem.key))
	                        simulateItem = simulate[simulateIndex]
	                        // if the remove didn't put the wanted item in place, we need to insert it
	                        if (!simulateItem || simulateItem.key !== wantedItem.key) {
	                            inserts.push({key: wantedItem.key, to: k})
	                        }
	                        // items are matching, so skip ahead
	                        else {
	                            simulateIndex++
	                        }
	                    }
	                    else {
	                        inserts.push({key: wantedItem.key, to: k})
	                    }
	                }
	                else {
	                    inserts.push({key: wantedItem.key, to: k})
	                }
	                k++
	            }
	            // a key in simulate has no matching wanted key, remove it
	            else if (simulateItem && simulateItem.key) {
	                removes.push(remove(simulate, simulateIndex, simulateItem.key))
	            }
	        }
	        else {
	            simulateIndex++
	            k++
	        }
	    }
	
	    // remove all the remaining nodes from simulate
	    while(simulateIndex < simulate.length) {
	        simulateItem = simulate[simulateIndex]
	        removes.push(remove(simulate, simulateIndex, simulateItem && simulateItem.key))
	    }
	
	    // If the only moves we have are deletes then we can just
	    // let the delete patch remove these items.
	    if (removes.length === deletedItems && !inserts.length) {
	        return {
	            children: newChildren,
	            moves: null
	        }
	    }
	
	    return {
	        children: newChildren,
	        moves: {
	            removes: removes,
	            inserts: inserts
	        }
	    }
	}
	
	function remove(arr, index, key) {
	    arr.splice(index, 1)
	
	    return {
	        from: index,
	        key: key
	    }
	}
	
	function keyIndex(children) {
	    var keys = {}
	    var free = []
	    var length = children.length
	
	    for (var i = 0; i < length; i++) {
	        var child = children[i]
	
	        if (child.key) {
	            keys[child.key] = i
	        } else {
	            free.push(i)
	        }
	    }
	
	    return {
	        keys: keys,     // A hash of key name to index
	        free: free      // An array of unkeyed item indices
	    }
	}
	
	function appendPatch(apply, patch) {
	    if (apply) {
	        if (isArray(apply)) {
	            apply.push(patch)
	        } else {
	            apply = [apply, patch]
	        }
	
	        return apply
	    } else {
	        return patch
	    }
	}


/***/ },
/* 7 */
/***/ function(module, exports) {

	var nativeIsArray = Array.isArray
	var toString = Object.prototype.toString
	
	module.exports = nativeIsArray || isArray
	
	function isArray(obj) {
	    return toString.call(obj) === "[object Array]"
	}


/***/ },
/* 8 */
/***/ function(module, exports, __webpack_require__) {

	var version = __webpack_require__(9)
	
	VirtualPatch.NONE = 0
	VirtualPatch.VTEXT = 1
	VirtualPatch.VNODE = 2
	VirtualPatch.WIDGET = 3
	VirtualPatch.PROPS = 4
	VirtualPatch.ORDER = 5
	VirtualPatch.INSERT = 6
	VirtualPatch.REMOVE = 7
	VirtualPatch.THUNK = 8
	
	module.exports = VirtualPatch
	
	function VirtualPatch(type, vNode, patch) {
	    this.type = Number(type)
	    this.vNode = vNode
	    this.patch = patch
	}
	
	VirtualPatch.prototype.version = version
	VirtualPatch.prototype.type = "VirtualPatch"


/***/ },
/* 9 */
/***/ function(module, exports) {

	module.exports = "2"


/***/ },
/* 10 */
/***/ function(module, exports, __webpack_require__) {

	var version = __webpack_require__(9)
	
	module.exports = isVirtualNode
	
	function isVirtualNode(x) {
	    return x && x.type === "VirtualNode" && x.version === version
	}


/***/ },
/* 11 */
/***/ function(module, exports, __webpack_require__) {

	var version = __webpack_require__(9)
	
	module.exports = isVirtualText
	
	function isVirtualText(x) {
	    return x && x.type === "VirtualText" && x.version === version
	}


/***/ },
/* 12 */
/***/ function(module, exports) {

	module.exports = isWidget
	
	function isWidget(w) {
	    return w && w.type === "Widget"
	}


/***/ },
/* 13 */
/***/ function(module, exports) {

	module.exports = isThunk
	
	function isThunk(t) {
	    return t && t.type === "Thunk"
	}


/***/ },
/* 14 */
/***/ function(module, exports, __webpack_require__) {

	var isVNode = __webpack_require__(10)
	var isVText = __webpack_require__(11)
	var isWidget = __webpack_require__(12)
	var isThunk = __webpack_require__(13)
	
	module.exports = handleThunk
	
	function handleThunk(a, b) {
	    var renderedA = a
	    var renderedB = b
	
	    if (isThunk(b)) {
	        renderedB = renderThunk(b, a)
	    }
	
	    if (isThunk(a)) {
	        renderedA = renderThunk(a, null)
	    }
	
	    return {
	        a: renderedA,
	        b: renderedB
	    }
	}
	
	function renderThunk(thunk, previous) {
	    var renderedThunk = thunk.vnode
	
	    if (!renderedThunk) {
	        renderedThunk = thunk.vnode = thunk.render(previous)
	    }
	
	    if (!(isVNode(renderedThunk) ||
	            isVText(renderedThunk) ||
	            isWidget(renderedThunk))) {
	        throw new Error("thunk did not return a valid node");
	    }
	
	    return renderedThunk
	}


/***/ },
/* 15 */
/***/ function(module, exports, __webpack_require__) {

	var isObject = __webpack_require__(16)
	var isHook = __webpack_require__(17)
	
	module.exports = diffProps
	
	function diffProps(a, b) {
	    var diff
	
	    for (var aKey in a) {
	        if (!(aKey in b)) {
	            diff = diff || {}
	            diff[aKey] = undefined
	        }
	
	        var aValue = a[aKey]
	        var bValue = b[aKey]
	
	        if (aValue === bValue) {
	            continue
	        } else if (isObject(aValue) && isObject(bValue)) {
	            if (getPrototype(bValue) !== getPrototype(aValue)) {
	                diff = diff || {}
	                diff[aKey] = bValue
	            } else if (isHook(bValue)) {
	                 diff = diff || {}
	                 diff[aKey] = bValue
	            } else {
	                var objectDiff = diffProps(aValue, bValue)
	                if (objectDiff) {
	                    diff = diff || {}
	                    diff[aKey] = objectDiff
	                }
	            }
	        } else {
	            diff = diff || {}
	            diff[aKey] = bValue
	        }
	    }
	
	    for (var bKey in b) {
	        if (!(bKey in a)) {
	            diff = diff || {}
	            diff[bKey] = b[bKey]
	        }
	    }
	
	    return diff
	}
	
	function getPrototype(value) {
	  if (Object.getPrototypeOf) {
	    return Object.getPrototypeOf(value)
	  } else if (value.__proto__) {
	    return value.__proto__
	  } else if (value.constructor) {
	    return value.constructor.prototype
	  }
	}


/***/ },
/* 16 */
/***/ function(module, exports) {

	"use strict";
	
	module.exports = function isObject(x) {
		return typeof x === "object" && x !== null;
	};


/***/ },
/* 17 */
/***/ function(module, exports) {

	module.exports = isHook
	
	function isHook(hook) {
	    return hook &&
	      (typeof hook.hook === "function" && !hook.hasOwnProperty("hook") ||
	       typeof hook.unhook === "function" && !hook.hasOwnProperty("unhook"))
	}


/***/ },
/* 18 */
/***/ function(module, exports, __webpack_require__) {

	var patch = __webpack_require__(19)
	
	module.exports = patch


/***/ },
/* 19 */
/***/ function(module, exports, __webpack_require__) {

	var document = __webpack_require__(20)
	var isArray = __webpack_require__(7)
	
	var render = __webpack_require__(22)
	var domIndex = __webpack_require__(24)
	var patchOp = __webpack_require__(25)
	module.exports = patch
	
	function patch(rootNode, patches, renderOptions) {
	    renderOptions = renderOptions || {}
	    renderOptions.patch = renderOptions.patch && renderOptions.patch !== patch
	        ? renderOptions.patch
	        : patchRecursive
	    renderOptions.render = renderOptions.render || render
	
	    return renderOptions.patch(rootNode, patches, renderOptions)
	}
	
	function patchRecursive(rootNode, patches, renderOptions) {
	    var indices = patchIndices(patches)
	
	    if (indices.length === 0) {
	        return rootNode
	    }
	
	    var index = domIndex(rootNode, patches.a, indices)
	    var ownerDocument = rootNode.ownerDocument
	
	    if (!renderOptions.document && ownerDocument !== document) {
	        renderOptions.document = ownerDocument
	    }
	
	    for (var i = 0; i < indices.length; i++) {
	        var nodeIndex = indices[i]
	        rootNode = applyPatch(rootNode,
	            index[nodeIndex],
	            patches[nodeIndex],
	            renderOptions)
	    }
	
	    return rootNode
	}
	
	function applyPatch(rootNode, domNode, patchList, renderOptions) {
	    if (!domNode) {
	        return rootNode
	    }
	
	    var newNode
	
	    if (isArray(patchList)) {
	        for (var i = 0; i < patchList.length; i++) {
	            newNode = patchOp(patchList[i], domNode, renderOptions)
	
	            if (domNode === rootNode) {
	                rootNode = newNode
	            }
	        }
	    } else {
	        newNode = patchOp(patchList, domNode, renderOptions)
	
	        if (domNode === rootNode) {
	            rootNode = newNode
	        }
	    }
	
	    return rootNode
	}
	
	function patchIndices(patches) {
	    var indices = []
	
	    for (var key in patches) {
	        if (key !== "a") {
	            indices.push(Number(key))
	        }
	    }
	
	    return indices
	}


/***/ },
/* 20 */
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(global) {var topLevel = typeof global !== 'undefined' ? global :
	    typeof window !== 'undefined' ? window : {}
	var minDoc = __webpack_require__(21);
	
	if (typeof document !== 'undefined') {
	    module.exports = document;
	} else {
	    var doccy = topLevel['__GLOBAL_DOCUMENT_CACHE@4'];
	
	    if (!doccy) {
	        doccy = topLevel['__GLOBAL_DOCUMENT_CACHE@4'] = minDoc;
	    }
	
	    module.exports = doccy;
	}
	
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },
/* 21 */
/***/ function(module, exports) {

	/* (ignored) */

/***/ },
/* 22 */
/***/ function(module, exports, __webpack_require__) {

	var document = __webpack_require__(20)
	
	var applyProperties = __webpack_require__(23)
	
	var isVNode = __webpack_require__(10)
	var isVText = __webpack_require__(11)
	var isWidget = __webpack_require__(12)
	var handleThunk = __webpack_require__(14)
	
	module.exports = createElement
	
	function createElement(vnode, opts) {
	    var doc = opts ? opts.document || document : document
	    var warn = opts ? opts.warn : null
	
	    vnode = handleThunk(vnode).a
	
	    if (isWidget(vnode)) {
	        return vnode.init()
	    } else if (isVText(vnode)) {
	        return doc.createTextNode(vnode.text)
	    } else if (!isVNode(vnode)) {
	        if (warn) {
	            warn("Item is not a valid virtual dom node", vnode)
	        }
	        return null
	    }
	
	    var node = (vnode.namespace === null) ?
	        doc.createElement(vnode.tagName) :
	        doc.createElementNS(vnode.namespace, vnode.tagName)
	
	    var props = vnode.properties
	    applyProperties(node, props)
	
	    var children = vnode.children
	
	    for (var i = 0; i < children.length; i++) {
	        var childNode = createElement(children[i], opts)
	        if (childNode) {
	            node.appendChild(childNode)
	        }
	    }
	
	    return node
	}


/***/ },
/* 23 */
/***/ function(module, exports, __webpack_require__) {

	var isObject = __webpack_require__(16)
	var isHook = __webpack_require__(17)
	
	module.exports = applyProperties
	
	function applyProperties(node, props, previous) {
	    for (var propName in props) {
	        var propValue = props[propName]
	
	        if (propValue === undefined) {
	            removeProperty(node, propName, propValue, previous);
	        } else if (isHook(propValue)) {
	            removeProperty(node, propName, propValue, previous)
	            if (propValue.hook) {
	                propValue.hook(node,
	                    propName,
	                    previous ? previous[propName] : undefined)
	            }
	        } else {
	            if (isObject(propValue)) {
	                patchObject(node, props, previous, propName, propValue);
	            } else {
	                node[propName] = propValue
	            }
	        }
	    }
	}
	
	function removeProperty(node, propName, propValue, previous) {
	    if (previous) {
	        var previousValue = previous[propName]
	
	        if (!isHook(previousValue)) {
	            if (propName === "attributes") {
	                for (var attrName in previousValue) {
	                    node.removeAttribute(attrName)
	                }
	            } else if (propName === "style") {
	                for (var i in previousValue) {
	                    node.style[i] = ""
	                }
	            } else if (typeof previousValue === "string") {
	                node[propName] = ""
	            } else {
	                node[propName] = null
	            }
	        } else if (previousValue.unhook) {
	            previousValue.unhook(node, propName, propValue)
	        }
	    }
	}
	
	function patchObject(node, props, previous, propName, propValue) {
	    var previousValue = previous ? previous[propName] : undefined
	
	    // Set attributes
	    if (propName === "attributes") {
	        for (var attrName in propValue) {
	            var attrValue = propValue[attrName]
	
	            if (attrValue === undefined) {
	                node.removeAttribute(attrName)
	            } else {
	                node.setAttribute(attrName, attrValue)
	            }
	        }
	
	        return
	    }
	
	    if(previousValue && isObject(previousValue) &&
	        getPrototype(previousValue) !== getPrototype(propValue)) {
	        node[propName] = propValue
	        return
	    }
	
	    if (!isObject(node[propName])) {
	        node[propName] = {}
	    }
	
	    var replacer = propName === "style" ? "" : undefined
	
	    for (var k in propValue) {
	        var value = propValue[k]
	        node[propName][k] = (value === undefined) ? replacer : value
	    }
	}
	
	function getPrototype(value) {
	    if (Object.getPrototypeOf) {
	        return Object.getPrototypeOf(value)
	    } else if (value.__proto__) {
	        return value.__proto__
	    } else if (value.constructor) {
	        return value.constructor.prototype
	    }
	}


/***/ },
/* 24 */
/***/ function(module, exports) {

	// Maps a virtual DOM tree onto a real DOM tree in an efficient manner.
	// We don't want to read all of the DOM nodes in the tree so we use
	// the in-order tree indexing to eliminate recursion down certain branches.
	// We only recurse into a DOM node if we know that it contains a child of
	// interest.
	
	var noChild = {}
	
	module.exports = domIndex
	
	function domIndex(rootNode, tree, indices, nodes) {
	    if (!indices || indices.length === 0) {
	        return {}
	    } else {
	        indices.sort(ascending)
	        return recurse(rootNode, tree, indices, nodes, 0)
	    }
	}
	
	function recurse(rootNode, tree, indices, nodes, rootIndex) {
	    nodes = nodes || {}
	
	
	    if (rootNode) {
	        if (indexInRange(indices, rootIndex, rootIndex)) {
	            nodes[rootIndex] = rootNode
	        }
	
	        var vChildren = tree.children
	
	        if (vChildren) {
	
	            var childNodes = rootNode.childNodes
	
	            for (var i = 0; i < tree.children.length; i++) {
	                rootIndex += 1
	
	                var vChild = vChildren[i] || noChild
	                var nextIndex = rootIndex + (vChild.count || 0)
	
	                // skip recursion down the tree if there are no nodes down here
	                if (indexInRange(indices, rootIndex, nextIndex)) {
	                    recurse(childNodes[i], vChild, indices, nodes, rootIndex)
	                }
	
	                rootIndex = nextIndex
	            }
	        }
	    }
	
	    return nodes
	}
	
	// Binary search for an index in the interval [left, right]
	function indexInRange(indices, left, right) {
	    if (indices.length === 0) {
	        return false
	    }
	
	    var minIndex = 0
	    var maxIndex = indices.length - 1
	    var currentIndex
	    var currentItem
	
	    while (minIndex <= maxIndex) {
	        currentIndex = ((maxIndex + minIndex) / 2) >> 0
	        currentItem = indices[currentIndex]
	
	        if (minIndex === maxIndex) {
	            return currentItem >= left && currentItem <= right
	        } else if (currentItem < left) {
	            minIndex = currentIndex + 1
	        } else  if (currentItem > right) {
	            maxIndex = currentIndex - 1
	        } else {
	            return true
	        }
	    }
	
	    return false;
	}
	
	function ascending(a, b) {
	    return a > b ? 1 : -1
	}


/***/ },
/* 25 */
/***/ function(module, exports, __webpack_require__) {

	var applyProperties = __webpack_require__(23)
	
	var isWidget = __webpack_require__(12)
	var VPatch = __webpack_require__(8)
	
	var updateWidget = __webpack_require__(26)
	
	module.exports = applyPatch
	
	function applyPatch(vpatch, domNode, renderOptions) {
	    var type = vpatch.type
	    var vNode = vpatch.vNode
	    var patch = vpatch.patch
	
	    switch (type) {
	        case VPatch.REMOVE:
	            return removeNode(domNode, vNode)
	        case VPatch.INSERT:
	            return insertNode(domNode, patch, renderOptions)
	        case VPatch.VTEXT:
	            return stringPatch(domNode, vNode, patch, renderOptions)
	        case VPatch.WIDGET:
	            return widgetPatch(domNode, vNode, patch, renderOptions)
	        case VPatch.VNODE:
	            return vNodePatch(domNode, vNode, patch, renderOptions)
	        case VPatch.ORDER:
	            reorderChildren(domNode, patch)
	            return domNode
	        case VPatch.PROPS:
	            applyProperties(domNode, patch, vNode.properties)
	            return domNode
	        case VPatch.THUNK:
	            return replaceRoot(domNode,
	                renderOptions.patch(domNode, patch, renderOptions))
	        default:
	            return domNode
	    }
	}
	
	function removeNode(domNode, vNode) {
	    var parentNode = domNode.parentNode
	
	    if (parentNode) {
	        parentNode.removeChild(domNode)
	    }
	
	    destroyWidget(domNode, vNode);
	
	    return null
	}
	
	function insertNode(parentNode, vNode, renderOptions) {
	    var newNode = renderOptions.render(vNode, renderOptions)
	
	    if (parentNode) {
	        parentNode.appendChild(newNode)
	    }
	
	    return parentNode
	}
	
	function stringPatch(domNode, leftVNode, vText, renderOptions) {
	    var newNode
	
	    if (domNode.nodeType === 3) {
	        domNode.replaceData(0, domNode.length, vText.text)
	        newNode = domNode
	    } else {
	        var parentNode = domNode.parentNode
	        newNode = renderOptions.render(vText, renderOptions)
	
	        if (parentNode && newNode !== domNode) {
	            parentNode.replaceChild(newNode, domNode)
	        }
	    }
	
	    return newNode
	}
	
	function widgetPatch(domNode, leftVNode, widget, renderOptions) {
	    var updating = updateWidget(leftVNode, widget)
	    var newNode
	
	    if (updating) {
	        newNode = widget.update(leftVNode, domNode) || domNode
	    } else {
	        newNode = renderOptions.render(widget, renderOptions)
	    }
	
	    var parentNode = domNode.parentNode
	
	    if (parentNode && newNode !== domNode) {
	        parentNode.replaceChild(newNode, domNode)
	    }
	
	    if (!updating) {
	        destroyWidget(domNode, leftVNode)
	    }
	
	    return newNode
	}
	
	function vNodePatch(domNode, leftVNode, vNode, renderOptions) {
	    var parentNode = domNode.parentNode
	    var newNode = renderOptions.render(vNode, renderOptions)
	
	    if (parentNode && newNode !== domNode) {
	        parentNode.replaceChild(newNode, domNode)
	    }
	
	    return newNode
	}
	
	function destroyWidget(domNode, w) {
	    if (typeof w.destroy === "function" && isWidget(w)) {
	        w.destroy(domNode)
	    }
	}
	
	function reorderChildren(domNode, moves) {
	    var childNodes = domNode.childNodes
	    var keyMap = {}
	    var node
	    var remove
	    var insert
	
	    for (var i = 0; i < moves.removes.length; i++) {
	        remove = moves.removes[i]
	        node = childNodes[remove.from]
	        if (remove.key) {
	            keyMap[remove.key] = node
	        }
	        domNode.removeChild(node)
	    }
	
	    var length = childNodes.length
	    for (var j = 0; j < moves.inserts.length; j++) {
	        insert = moves.inserts[j]
	        node = keyMap[insert.key]
	        // this is the weirdest bug i've ever seen in webkit
	        domNode.insertBefore(node, insert.to >= length++ ? null : childNodes[insert.to])
	    }
	}
	
	function replaceRoot(oldRoot, newRoot) {
	    if (oldRoot && newRoot && oldRoot !== newRoot && oldRoot.parentNode) {
	        oldRoot.parentNode.replaceChild(newRoot, oldRoot)
	    }
	
	    return newRoot;
	}


/***/ },
/* 26 */
/***/ function(module, exports, __webpack_require__) {

	var isWidget = __webpack_require__(12)
	
	module.exports = updateWidget
	
	function updateWidget(a, b) {
	    if (isWidget(a) && isWidget(b)) {
	        if ("name" in a && "name" in b) {
	            return a.id === b.id
	        } else {
	            return a.init === b.init
	        }
	    }
	
	    return false
	}


/***/ },
/* 27 */
/***/ function(module, exports, __webpack_require__) {

	var h = __webpack_require__(28)
	
	module.exports = h


/***/ },
/* 28 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	
	var isArray = __webpack_require__(7);
	
	var VNode = __webpack_require__(29);
	var VText = __webpack_require__(30);
	var isVNode = __webpack_require__(10);
	var isVText = __webpack_require__(11);
	var isWidget = __webpack_require__(12);
	var isHook = __webpack_require__(17);
	var isVThunk = __webpack_require__(13);
	
	var parseTag = __webpack_require__(31);
	var softSetHook = __webpack_require__(33);
	var evHook = __webpack_require__(34);
	
	module.exports = h;
	
	function h(tagName, properties, children) {
	    var childNodes = [];
	    var tag, props, key, namespace;
	
	    if (!children && isChildren(properties)) {
	        children = properties;
	        props = {};
	    }
	
	    props = props || properties || {};
	    tag = parseTag(tagName, props);
	
	    // support keys
	    if (props.hasOwnProperty('key')) {
	        key = props.key;
	        props.key = undefined;
	    }
	
	    // support namespace
	    if (props.hasOwnProperty('namespace')) {
	        namespace = props.namespace;
	        props.namespace = undefined;
	    }
	
	    // fix cursor bug
	    if (tag === 'INPUT' &&
	        !namespace &&
	        props.hasOwnProperty('value') &&
	        props.value !== undefined &&
	        !isHook(props.value)
	    ) {
	        props.value = softSetHook(props.value);
	    }
	
	    transformProperties(props);
	
	    if (children !== undefined && children !== null) {
	        addChild(children, childNodes, tag, props);
	    }
	
	
	    return new VNode(tag, props, childNodes, key, namespace);
	}
	
	function addChild(c, childNodes, tag, props) {
	    if (typeof c === 'string') {
	        childNodes.push(new VText(c));
	    } else if (typeof c === 'number') {
	        childNodes.push(new VText(String(c)));
	    } else if (isChild(c)) {
	        childNodes.push(c);
	    } else if (isArray(c)) {
	        for (var i = 0; i < c.length; i++) {
	            addChild(c[i], childNodes, tag, props);
	        }
	    } else if (c === null || c === undefined) {
	        return;
	    } else {
	        throw UnexpectedVirtualElement({
	            foreignObject: c,
	            parentVnode: {
	                tagName: tag,
	                properties: props
	            }
	        });
	    }
	}
	
	function transformProperties(props) {
	    for (var propName in props) {
	        if (props.hasOwnProperty(propName)) {
	            var value = props[propName];
	
	            if (isHook(value)) {
	                continue;
	            }
	
	            if (propName.substr(0, 3) === 'ev-') {
	                // add ev-foo support
	                props[propName] = evHook(value);
	            }
	        }
	    }
	}
	
	function isChild(x) {
	    return isVNode(x) || isVText(x) || isWidget(x) || isVThunk(x);
	}
	
	function isChildren(x) {
	    return typeof x === 'string' || isArray(x) || isChild(x);
	}
	
	function UnexpectedVirtualElement(data) {
	    var err = new Error();
	
	    err.type = 'virtual-hyperscript.unexpected.virtual-element';
	    err.message = 'Unexpected virtual child passed to h().\n' +
	        'Expected a VNode / Vthunk / VWidget / string but:\n' +
	        'got:\n' +
	        errorString(data.foreignObject) +
	        '.\n' +
	        'The parent vnode is:\n' +
	        errorString(data.parentVnode)
	        '\n' +
	        'Suggested fix: change your `h(..., [ ... ])` callsite.';
	    err.foreignObject = data.foreignObject;
	    err.parentVnode = data.parentVnode;
	
	    return err;
	}
	
	function errorString(obj) {
	    try {
	        return JSON.stringify(obj, null, '    ');
	    } catch (e) {
	        return String(obj);
	    }
	}


/***/ },
/* 29 */
/***/ function(module, exports, __webpack_require__) {

	var version = __webpack_require__(9)
	var isVNode = __webpack_require__(10)
	var isWidget = __webpack_require__(12)
	var isThunk = __webpack_require__(13)
	var isVHook = __webpack_require__(17)
	
	module.exports = VirtualNode
	
	var noProperties = {}
	var noChildren = []
	
	function VirtualNode(tagName, properties, children, key, namespace) {
	    this.tagName = tagName
	    this.properties = properties || noProperties
	    this.children = children || noChildren
	    this.key = key != null ? String(key) : undefined
	    this.namespace = (typeof namespace === "string") ? namespace : null
	
	    var count = (children && children.length) || 0
	    var descendants = 0
	    var hasWidgets = false
	    var hasThunks = false
	    var descendantHooks = false
	    var hooks
	
	    for (var propName in properties) {
	        if (properties.hasOwnProperty(propName)) {
	            var property = properties[propName]
	            if (isVHook(property) && property.unhook) {
	                if (!hooks) {
	                    hooks = {}
	                }
	
	                hooks[propName] = property
	            }
	        }
	    }
	
	    for (var i = 0; i < count; i++) {
	        var child = children[i]
	        if (isVNode(child)) {
	            descendants += child.count || 0
	
	            if (!hasWidgets && child.hasWidgets) {
	                hasWidgets = true
	            }
	
	            if (!hasThunks && child.hasThunks) {
	                hasThunks = true
	            }
	
	            if (!descendantHooks && (child.hooks || child.descendantHooks)) {
	                descendantHooks = true
	            }
	        } else if (!hasWidgets && isWidget(child)) {
	            if (typeof child.destroy === "function") {
	                hasWidgets = true
	            }
	        } else if (!hasThunks && isThunk(child)) {
	            hasThunks = true;
	        }
	    }
	
	    this.count = count + descendants
	    this.hasWidgets = hasWidgets
	    this.hasThunks = hasThunks
	    this.hooks = hooks
	    this.descendantHooks = descendantHooks
	}
	
	VirtualNode.prototype.version = version
	VirtualNode.prototype.type = "VirtualNode"


/***/ },
/* 30 */
/***/ function(module, exports, __webpack_require__) {

	var version = __webpack_require__(9)
	
	module.exports = VirtualText
	
	function VirtualText(text) {
	    this.text = String(text)
	}
	
	VirtualText.prototype.version = version
	VirtualText.prototype.type = "VirtualText"


/***/ },
/* 31 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	
	var split = __webpack_require__(32);
	
	var classIdSplit = /([\.#]?[a-zA-Z0-9\u007F-\uFFFF_:-]+)/;
	var notClassId = /^\.|#/;
	
	module.exports = parseTag;
	
	function parseTag(tag, props) {
	    if (!tag) {
	        return 'DIV';
	    }
	
	    var noId = !(props.hasOwnProperty('id'));
	
	    var tagParts = split(tag, classIdSplit);
	    var tagName = null;
	
	    if (notClassId.test(tagParts[1])) {
	        tagName = 'DIV';
	    }
	
	    var classes, part, type, i;
	
	    for (i = 0; i < tagParts.length; i++) {
	        part = tagParts[i];
	
	        if (!part) {
	            continue;
	        }
	
	        type = part.charAt(0);
	
	        if (!tagName) {
	            tagName = part;
	        } else if (type === '.') {
	            classes = classes || [];
	            classes.push(part.substring(1, part.length));
	        } else if (type === '#' && noId) {
	            props.id = part.substring(1, part.length);
	        }
	    }
	
	    if (classes) {
	        if (props.className) {
	            classes.push(props.className);
	        }
	
	        props.className = classes.join(' ');
	    }
	
	    return props.namespace ? tagName : tagName.toUpperCase();
	}


/***/ },
/* 32 */
/***/ function(module, exports) {

	/*!
	 * Cross-Browser Split 1.1.1
	 * Copyright 2007-2012 Steven Levithan <stevenlevithan.com>
	 * Available under the MIT License
	 * ECMAScript compliant, uniform cross-browser split method
	 */
	
	/**
	 * Splits a string into an array of strings using a regex or string separator. Matches of the
	 * separator are not included in the result array. However, if `separator` is a regex that contains
	 * capturing groups, backreferences are spliced into the result each time `separator` is matched.
	 * Fixes browser bugs compared to the native `String.prototype.split` and can be used reliably
	 * cross-browser.
	 * @param {String} str String to split.
	 * @param {RegExp|String} separator Regex or string to use for separating the string.
	 * @param {Number} [limit] Maximum number of items to include in the result array.
	 * @returns {Array} Array of substrings.
	 * @example
	 *
	 * // Basic use
	 * split('a b c d', ' ');
	 * // -> ['a', 'b', 'c', 'd']
	 *
	 * // With limit
	 * split('a b c d', ' ', 2);
	 * // -> ['a', 'b']
	 *
	 * // Backreferences in result array
	 * split('..word1 word2..', /([a-z]+)(\d+)/i);
	 * // -> ['..', 'word', '1', ' ', 'word', '2', '..']
	 */
	module.exports = (function split(undef) {
	
	  var nativeSplit = String.prototype.split,
	    compliantExecNpcg = /()??/.exec("")[1] === undef,
	    // NPCG: nonparticipating capturing group
	    self;
	
	  self = function(str, separator, limit) {
	    // If `separator` is not a regex, use `nativeSplit`
	    if (Object.prototype.toString.call(separator) !== "[object RegExp]") {
	      return nativeSplit.call(str, separator, limit);
	    }
	    var output = [],
	      flags = (separator.ignoreCase ? "i" : "") + (separator.multiline ? "m" : "") + (separator.extended ? "x" : "") + // Proposed for ES6
	      (separator.sticky ? "y" : ""),
	      // Firefox 3+
	      lastLastIndex = 0,
	      // Make `global` and avoid `lastIndex` issues by working with a copy
	      separator = new RegExp(separator.source, flags + "g"),
	      separator2, match, lastIndex, lastLength;
	    str += ""; // Type-convert
	    if (!compliantExecNpcg) {
	      // Doesn't need flags gy, but they don't hurt
	      separator2 = new RegExp("^" + separator.source + "$(?!\\s)", flags);
	    }
	    /* Values for `limit`, per the spec:
	     * If undefined: 4294967295 // Math.pow(2, 32) - 1
	     * If 0, Infinity, or NaN: 0
	     * If positive number: limit = Math.floor(limit); if (limit > 4294967295) limit -= 4294967296;
	     * If negative number: 4294967296 - Math.floor(Math.abs(limit))
	     * If other: Type-convert, then use the above rules
	     */
	    limit = limit === undef ? -1 >>> 0 : // Math.pow(2, 32) - 1
	    limit >>> 0; // ToUint32(limit)
	    while (match = separator.exec(str)) {
	      // `separator.lastIndex` is not reliable cross-browser
	      lastIndex = match.index + match[0].length;
	      if (lastIndex > lastLastIndex) {
	        output.push(str.slice(lastLastIndex, match.index));
	        // Fix browsers whose `exec` methods don't consistently return `undefined` for
	        // nonparticipating capturing groups
	        if (!compliantExecNpcg && match.length > 1) {
	          match[0].replace(separator2, function() {
	            for (var i = 1; i < arguments.length - 2; i++) {
	              if (arguments[i] === undef) {
	                match[i] = undef;
	              }
	            }
	          });
	        }
	        if (match.length > 1 && match.index < str.length) {
	          Array.prototype.push.apply(output, match.slice(1));
	        }
	        lastLength = match[0].length;
	        lastLastIndex = lastIndex;
	        if (output.length >= limit) {
	          break;
	        }
	      }
	      if (separator.lastIndex === match.index) {
	        separator.lastIndex++; // Avoid an infinite loop
	      }
	    }
	    if (lastLastIndex === str.length) {
	      if (lastLength || !separator.test("")) {
	        output.push("");
	      }
	    } else {
	      output.push(str.slice(lastLastIndex));
	    }
	    return output.length > limit ? output.slice(0, limit) : output;
	  };
	
	  return self;
	})();


/***/ },
/* 33 */
/***/ function(module, exports) {

	'use strict';
	
	module.exports = SoftSetHook;
	
	function SoftSetHook(value) {
	    if (!(this instanceof SoftSetHook)) {
	        return new SoftSetHook(value);
	    }
	
	    this.value = value;
	}
	
	SoftSetHook.prototype.hook = function (node, propertyName) {
	    if (node[propertyName] !== this.value) {
	        node[propertyName] = this.value;
	    }
	};


/***/ },
/* 34 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	
	var EvStore = __webpack_require__(35);
	
	module.exports = EvHook;
	
	function EvHook(value) {
	    if (!(this instanceof EvHook)) {
	        return new EvHook(value);
	    }
	
	    this.value = value;
	}
	
	EvHook.prototype.hook = function (node, propertyName) {
	    var es = EvStore(node);
	    var propName = propertyName.substr(3);
	
	    es[propName] = this.value;
	};
	
	EvHook.prototype.unhook = function(node, propertyName) {
	    var es = EvStore(node);
	    var propName = propertyName.substr(3);
	
	    es[propName] = undefined;
	};


/***/ },
/* 35 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	
	var OneVersionConstraint = __webpack_require__(36);
	
	var MY_VERSION = '7';
	OneVersionConstraint('ev-store', MY_VERSION);
	
	var hashKey = '__EV_STORE_KEY@' + MY_VERSION;
	
	module.exports = EvStore;
	
	function EvStore(elem) {
	    var hash = elem[hashKey];
	
	    if (!hash) {
	        hash = elem[hashKey] = {};
	    }
	
	    return hash;
	}


/***/ },
/* 36 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	
	var Individual = __webpack_require__(37);
	
	module.exports = OneVersion;
	
	function OneVersion(moduleName, version, defaultValue) {
	    var key = '__INDIVIDUAL_ONE_VERSION_' + moduleName;
	    var enforceKey = key + '_ENFORCE_SINGLETON';
	
	    var versionValue = Individual(enforceKey, version);
	
	    if (versionValue !== version) {
	        throw new Error('Can only have one copy of ' +
	            moduleName + '.\n' +
	            'You already have version ' + versionValue +
	            ' installed.\n' +
	            'This means you cannot install version ' + version);
	    }
	
	    return Individual(key, defaultValue);
	}


/***/ },
/* 37 */
/***/ function(module, exports) {

	/* WEBPACK VAR INJECTION */(function(global) {'use strict';
	
	/*global window, global*/
	
	var root = typeof window !== 'undefined' ?
	    window : typeof global !== 'undefined' ?
	    global : {};
	
	module.exports = Individual;
	
	function Individual(key, value) {
	    if (key in root) {
	        return root[key];
	    }
	
	    root[key] = value;
	
	    return value;
	}
	
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },
/* 38 */
/***/ function(module, exports, __webpack_require__) {

	var createElement = __webpack_require__(22)
	
	module.exports = createElement


/***/ },
/* 39 */
/***/ function(module, exports) {

	"use strict";
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	
	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();
	
	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }
	
	var shallowCopy = function shallowCopy(obj) {
	  var dest = {};
	  Object.keys(obj).forEach(function (key) {
	    return dest[key] = obj[key];
	  });
	  return dest;
	};
	
	var Model = (function () {
	  function Model() {
	    var _this = this;
	
	    var _data = arguments.length <= 0 || arguments[0] === undefined ? {} : arguments[0];
	
	    _classCallCheck(this, Model);
	
	    this.__data = shallowCopy(_data);
	
	    Object.keys(this.__data).forEach(function (key) {
	      return _this[key] = function (valOrUpdate) {
	        if (undefined === valOrUpdate) return _this.get(key);
	        if ("function" == typeof valOrUpdate) return _this.update(key, valOrUpdate);
	        return _this.set(key, valOrUpdate);
	      };
	    });
	  }
	
	  _createClass(Model, [{
	    key: "get",
	    value: function get(key) {
	      return this.__data[key];
	    }
	  }, {
	    key: "set",
	    value: function set(key, val) {
	      if (this.get(key) === val) return this;
	      var clone = shallowCopy(this.__data);
	      clone[key] = val;
	      return new this.constructor(clone);
	    }
	  }, {
	    key: "unset",
	    value: function unset(key) {
	      var clone = shallowCopy(this.__data);
	      delete clone[key];
	      return new this.constructor(clone);
	    }
	  }, {
	    key: "update",
	    value: function update(key, cb) {
	      return this.set(key, cb(this.get(key)));
	    }
	  }, {
	    key: "getIn",
	    value: function getIn(path) {
	      var _path$reduce;
	
	      if (!Array.isArray(path)) throw new TypeError("Value of argument 'path' violates contract, expected array got " + (path === null ? "null" : path instanceof Object && path.constructor ? path.constructor.name : typeof path));
	      _path$reduce = path.reduce(function (model, key) {
	        return model.get(key);
	      }, this);
	      if (!(_path$reduce instanceof Model)) throw new TypeError("Function return value violates contract, expected Model got " + (_path$reduce === null ? "null" : _path$reduce instanceof Object && _path$reduce.constructor ? _path$reduce.constructor.name : typeof _path$reduce));
	      return _path$reduce;
	    }
	  }, {
	    key: "setIn",
	    value: function setIn(path, value) {
	      var _ref;
	
	      if (!Array.isArray(path)) throw new TypeError("Value of argument 'path' violates contract, expected array got " + (path === null ? "null" : path instanceof Object && path.constructor ? path.constructor.name : typeof path));
	
	      var head = path[0],
	          tail = path.slice(1);
	      _ref = !tail.length ? this.set(head, value) : this.update(head, function (model) {
	        return model.setIn(tail, value);
	      });
	      if (!(_ref instanceof Model)) throw new TypeError("Function return value violates contract, expected Model got " + (_ref === null ? "null" : _ref instanceof Object && _ref.constructor ? _ref.constructor.name : typeof _ref));
	      return _ref;
	    }
	  }, {
	    key: "unsetIn",
	    value: function unsetIn(path) {
	      var _ref2;
	
	      if (!Array.isArray(path)) throw new TypeError("Value of argument 'path' violates contract, expected array got " + (path === null ? "null" : path instanceof Object && path.constructor ? path.constructor.name : typeof path));
	
	      var head = path[0],
	          tail = path.slice(1);
	      _ref2 = !tail.length ? this.unset(head) : this.update(head, function (model) {
	        return model.unsetIn(tail);
	      });
	      if (!(_ref2 instanceof Model)) throw new TypeError("Function return value violates contract, expected Model got " + (_ref2 === null ? "null" : _ref2 instanceof Object && _ref2.constructor ? _ref2.constructor.name : typeof _ref2));
	      return _ref2;
	    }
	  }, {
	    key: "keys",
	    value: function keys() {
	      var _Object$keys;
	
	      _Object$keys = Object.keys(this.__data);
	      if (!Array.isArray(_Object$keys)) throw new TypeError("Function return value violates contract, expected array got " + (_Object$keys === null ? "null" : _Object$keys instanceof Object && _Object$keys.constructor ? _Object$keys.constructor.name : typeof _Object$keys));
	      return _Object$keys;
	    }
	  }, {
	    key: "has",
	    value: function has(key) {
	      var _ref3;
	
	      _ref3 = "undefined" != typeof this.get(key);
	      if (typeof _ref3 !== "boolean") throw new TypeError("Function return value violates contract, expected boolean got " + (_ref3 === null ? "null" : _ref3 instanceof Object && _ref3.constructor ? _ref3.constructor.name : typeof _ref3));
	      return _ref3;
	    }
	  }, {
	    key: "hasIn",
	    value: function hasIn(path) {
	      var _ref4;
	
	      if (!Array.isArray(path)) throw new TypeError("Value of argument 'path' violates contract, expected array got " + (path === null ? "null" : path instanceof Object && path.constructor ? path.constructor.name : typeof path));
	
	      var head = path[0];
	      var tail = path.slice(1);
	      _ref4 = tail.length ? this.get(head).hasIn(tail) : this.has(head);
	      if (typeof _ref4 !== "boolean") throw new TypeError("Function return value violates contract, expected boolean got " + (_ref4 === null ? "null" : _ref4 instanceof Object && _ref4.constructor ? _ref4.constructor.name : typeof _ref4));
	      return _ref4;
	    }
	  }, {
	    key: "entries",
	    value: function entries() {
	      return this.keys().map(this.get.bind(this));
	    }
	  }, {
	    key: "map",
	    value: function map() {
	      var _entries;
	
	      return (_entries = this.entries()).map.apply(_entries, arguments);
	    }
	  }, {
	    key: "equals",
	    value: function equals(b) {
	      var _ref5,
	          _this2 = this;
	
	      if (!(b instanceof Model)) throw new TypeError("Value of argument 'b' violates contract, expected Model got " + (b === null ? "null" : b instanceof Object && b.constructor ? b.constructor.name : typeof b));
	      _ref5 = this.keys().length == b.keys().length && this.keys().every(function (key) {
	        return b.has(key) && (_this2.get(key) instanceof Model ? _this2.get(key).equals(b.get(key)) : _this2.get(key) == b.get(key));
	      });
	      if (typeof _ref5 !== "boolean") throw new TypeError("Function return value violates contract, expected boolean got " + (_ref5 === null ? "null" : _ref5 instanceof Object && _ref5.constructor ? _ref5.constructor.name : typeof _ref5));
	      return _ref5;
	    }
	  }, {
	    key: "toJS",
	    value: function toJS() {
	      var _this3 = this;
	
	      var obj = {};
	      this.keys().forEach(function (key) {
	        var entry = _this3.get(key);
	        obj[key] = entry instanceof Model ? entry.toJS() : entry;
	      });
	      return obj;
	    }
	  }, {
	    key: "toJSON",
	    value: function toJSON() {
	      return JSON.stringify(this.toJS());
	    }
	  }]);
	
	  return Model;
	})();
	
	exports["default"] = Model;
	module.exports = exports["default"];

/***/ },
/* 40 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	exports["default"] = __;
	
	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }
	
	var _ampToolsObj2arr = __webpack_require__(41);
	
	var _ampToolsObj2arr2 = _interopRequireDefault(_ampToolsObj2arr);
	
	function __(_text) {
	  var text = _text;
	  (0, _ampToolsObj2arr2["default"])(arguments).slice(1).forEach(function (param) {
	    return text = text.replace('#$', param);
	  });
	  return text;
	}
	
	module.exports = exports["default"];

/***/ },
/* 41 */
/***/ function(module, exports) {

	"use strict";
	
	Object.defineProperty(exports, "__esModule", {
	    value: true
	});
	exports["default"] = obj2arr;
	
	function obj2arr(obj) {
	    return Object.keys(obj).map(function (key) {
	        return obj[key];
	    });
	}
	
	;
	module.exports = exports["default"];

/***/ },
/* 42 */
/***/ function(module, exports, __webpack_require__) {

	/** @jsx h */
	"use strict";
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	
	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; desc = parent = getter = undefined; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; continue _function; } } else if ("value" in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };
	
	exports.view = view;
	exports.update = update;
	
	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }
	
	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj["default"] = obj; return newObj; } }
	
	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }
	
	function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }
	
	var _ampArchitecture = __webpack_require__(3);
	
	var AMP = _interopRequireWildcard(_ampArchitecture);
	
	var _styleLess = __webpack_require__(43);
	
	var _styleLess2 = _interopRequireDefault(_styleLess);
	
	var _ampModulesTranslate = __webpack_require__(40);
	
	var _ampModulesTranslate2 = _interopRequireDefault(_ampModulesTranslate);
	
	var _classnames = __webpack_require__(47);
	
	var _classnames2 = _interopRequireDefault(_classnames);
	
	var _ampToolsValidate = __webpack_require__(48);
	
	var h = AMP.h;
	
	var Model = (function (_AMP$Model) {
	  _inherits(Model, _AMP$Model);
	
	  function Model() {
	    _classCallCheck(this, Model);
	
	    _get(Object.getPrototypeOf(Model.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Model;
	})(AMP.Model);
	
	exports.Model = Model;
	var model = new Model({
	  constantCurrency: false,
	  inflationRate: "",
	  year: "",
	  deletable: false,
	  valid: false
	});
	
	exports.model = model;
	
	var Action = (function (_AMP$Action) {
	  _inherits(Action, _AMP$Action);
	
	  function Action() {
	    _classCallCheck(this, Action);
	
	    _get(Object.getPrototypeOf(Action.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Action;
	})(AMP.Action);
	
	exports.Action = Action;
	
	var Delete = (function (_Action) {
	  _inherits(Delete, _Action);
	
	  function Delete() {
	    _classCallCheck(this, Delete);
	
	    _get(Object.getPrototypeOf(Delete.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Delete;
	})(Action);
	
	exports.Delete = Delete;
	
	var Change = (function (_Action2) {
	  _inherits(Change, _Action2);
	
	  function Change(newInflationRate) {
	    _classCallCheck(this, Change);
	
	    if (typeof newInflationRate !== "string") throw new TypeError("Value of argument 'newInflationRate' violates contract, expected string got " + (newInflationRate === null ? "null" : newInflationRate instanceof Object && newInflationRate.constructor ? newInflationRate.constructor.name : typeof newInflationRate));
	
	    _get(Object.getPrototypeOf(Change.prototype), "constructor", this).call(this);
	    this.inflationRate = newInflationRate;
	  }
	
	  return Change;
	})(Action);
	
	exports.Change = Change;
	
	var ToggleConstant = (function (_Action3) {
	  _inherits(ToggleConstant, _Action3);
	
	  function ToggleConstant(constant) {
	    _classCallCheck(this, ToggleConstant);
	
	    if (typeof constant !== "boolean") throw new TypeError("Value of argument 'constant' violates contract, expected boolean got " + (constant === null ? "null" : constant instanceof Object && constant.constructor ? constant.constructor.name : typeof constant));
	
	    _get(Object.getPrototypeOf(ToggleConstant.prototype), "constructor", this).call(this);
	    this.constant = constant;
	  }
	
	  return ToggleConstant;
	})(Action);
	
	exports.ToggleConstant = ToggleConstant;
	
	function view(address, model) {
	  if (!(model instanceof Model)) throw new TypeError("Value of argument 'model' violates contract, expected Model got " + (model === null ? "null" : model instanceof Object && model.constructor ? model.constructor.name : typeof model));
	
	  var onDelete = function onDelete(e) {
	    return address.send(new Delete());
	  };
	  var onChange = function onChange(e) {
	    return address.send(new Change(e.target.value));
	  };
	  var onToggleConstant = function onToggleConstant(e) {
	    return address.send(new ToggleConstant(e.target.checked));
	  };
	  var inflationRate = model.inflationRate();
	  return h(
	    "tr",
	    { className: (0, _classnames2["default"])('inflation-rate-entry', { "danger has-error": !model.valid() }) },
	    h(
	      "td",
	      null,
	      h(
	        "span",
	        { className: "form-control input-sm view" },
	        model.get('year')
	      )
	    ),
	    h(
	      "td",
	      { className: "edit-on-hover inflation-rate" },
	      h("input", { className: "form-control input-sm edit", required: true,
	        onkeypress: (0, _ampToolsValidate.allow)((0, _ampToolsValidate.negative)((0, _ampToolsValidate.point)(_ampToolsValidate.number))), value: inflationRate, onkeyup: onChange
	      }),
	      h(
	        "span",
	        { className: "form-control input-sm view" },
	        inflationRate
	      )
	    ),
	    h(
	      "td",
	      null,
	      h("input", { type: "checkbox", checked: model.constantCurrency(), onchange: onToggleConstant })
	    ),
	    h(
	      "td",
	      null,
	      model.deletable() ? h("i", { onclick: onDelete, className: "glyphicon glyphicon-trash" }) : null
	    )
	  );
	}
	
	function update(action, model) {
	  if (!(action instanceof Action)) throw new TypeError("Value of argument 'action' violates contract, expected Action got " + (action === null ? "null" : action instanceof Object && action.constructor ? action.constructor.name : typeof action));
	  if (!(model instanceof Model)) throw new TypeError("Value of argument 'model' violates contract, expected Model got " + (model === null ? "null" : model instanceof Object && model.constructor ? model.constructor.name : typeof model));
	
	  if (action instanceof Change) {
	    return model.inflationRate(action.inflationRate);
	  }
	  if (action instanceof ToggleConstant) {
	    return model.constantCurrency(action.constant);
	  }
	}

/***/ },
/* 43 */
/***/ function(module, exports, __webpack_require__) {

	// style-loader: Adds some css to the DOM by adding a <style> tag
	
	// load the styles
	var content = __webpack_require__(44);
	if(typeof content === 'string') content = [[module.id, content, '']];
	// add the styles to the DOM
	var update = __webpack_require__(46)(content, {});
	if(content.locals) module.exports = content.locals;
	// Hot Module Replacement
	if(false) {
		// When the styles change, update the <style> tags
		if(!content.locals) {
			module.hot.accept("!!./../../../../../node_modules/css-loader/index.js!./../../../../../node_modules/less-loader/index.js!./style.less", function() {
				var newContent = require("!!./../../../../../node_modules/css-loader/index.js!./../../../../../node_modules/less-loader/index.js!./style.less");
				if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
				update(newContent);
			});
		}
		// When the module is disposed, remove the <style> tags
		module.hot.dispose(function() { update(); });
	}

/***/ },
/* 44 */
/***/ function(module, exports, __webpack_require__) {

	exports = module.exports = __webpack_require__(45)();
	// imports
	
	
	// module
	exports.push([module.id, ".glyphicon-trash {\n  cursor: pointer;\n}\ntd,\nth {\n  width: 25%;\n}\n.form-control.view {\n  background: transparent;\n  border-color: transparent;\n  border-radius: 0;\n  box-shadow: none;\n}\n.edit-on-hover .edit {\n  display: none;\n}\n.edit-on-hover .edit:focus {\n  display: block;\n}\n.edit-on-hover .edit:focus + .view {\n  display: none;\n}\n.edit-on-hover:hover .edit {\n  display: block;\n}\n.edit-on-hover:hover .view {\n  display: none;\n}\n", ""]);
	
	// exports


/***/ },
/* 45 */
/***/ function(module, exports) {

	/*
		MIT License http://www.opensource.org/licenses/mit-license.php
		Author Tobias Koppers @sokra
	*/
	// css base code, injected by the css-loader
	module.exports = function() {
		var list = [];
	
		// return the list of modules as css string
		list.toString = function toString() {
			var result = [];
			for(var i = 0; i < this.length; i++) {
				var item = this[i];
				if(item[2]) {
					result.push("@media " + item[2] + "{" + item[1] + "}");
				} else {
					result.push(item[1]);
				}
			}
			return result.join("");
		};
	
		// import a list of modules into the list
		list.i = function(modules, mediaQuery) {
			if(typeof modules === "string")
				modules = [[null, modules, ""]];
			var alreadyImportedModules = {};
			for(var i = 0; i < this.length; i++) {
				var id = this[i][0];
				if(typeof id === "number")
					alreadyImportedModules[id] = true;
			}
			for(i = 0; i < modules.length; i++) {
				var item = modules[i];
				// skip already imported module
				// this implementation is not 100% perfect for weird media query combinations
				//  when a module is imported multiple times with different media queries.
				//  I hope this will never occur (Hey this way we have smaller bundles)
				if(typeof item[0] !== "number" || !alreadyImportedModules[item[0]]) {
					if(mediaQuery && !item[2]) {
						item[2] = mediaQuery;
					} else if(mediaQuery) {
						item[2] = "(" + item[2] + ") and (" + mediaQuery + ")";
					}
					list.push(item);
				}
			}
		};
		return list;
	};


/***/ },
/* 46 */
/***/ function(module, exports, __webpack_require__) {

	/*
		MIT License http://www.opensource.org/licenses/mit-license.php
		Author Tobias Koppers @sokra
	*/
	var stylesInDom = {},
		memoize = function(fn) {
			var memo;
			return function () {
				if (typeof memo === "undefined") memo = fn.apply(this, arguments);
				return memo;
			};
		},
		isOldIE = memoize(function() {
			return /msie [6-9]\b/.test(window.navigator.userAgent.toLowerCase());
		}),
		getHeadElement = memoize(function () {
			return document.head || document.getElementsByTagName("head")[0];
		}),
		singletonElement = null,
		singletonCounter = 0;
	
	module.exports = function(list, options) {
		if(false) {
			if(typeof document !== "object") throw new Error("The style-loader cannot be used in a non-browser environment");
		}
	
		options = options || {};
		// Force single-tag solution on IE6-9, which has a hard limit on the # of <style>
		// tags it will allow on a page
		if (typeof options.singleton === "undefined") options.singleton = isOldIE();
	
		var styles = listToStyles(list);
		addStylesToDom(styles, options);
	
		return function update(newList) {
			var mayRemove = [];
			for(var i = 0; i < styles.length; i++) {
				var item = styles[i];
				var domStyle = stylesInDom[item.id];
				domStyle.refs--;
				mayRemove.push(domStyle);
			}
			if(newList) {
				var newStyles = listToStyles(newList);
				addStylesToDom(newStyles, options);
			}
			for(var i = 0; i < mayRemove.length; i++) {
				var domStyle = mayRemove[i];
				if(domStyle.refs === 0) {
					for(var j = 0; j < domStyle.parts.length; j++)
						domStyle.parts[j]();
					delete stylesInDom[domStyle.id];
				}
			}
		};
	}
	
	function addStylesToDom(styles, options) {
		for(var i = 0; i < styles.length; i++) {
			var item = styles[i];
			var domStyle = stylesInDom[item.id];
			if(domStyle) {
				domStyle.refs++;
				for(var j = 0; j < domStyle.parts.length; j++) {
					domStyle.parts[j](item.parts[j]);
				}
				for(; j < item.parts.length; j++) {
					domStyle.parts.push(addStyle(item.parts[j], options));
				}
			} else {
				var parts = [];
				for(var j = 0; j < item.parts.length; j++) {
					parts.push(addStyle(item.parts[j], options));
				}
				stylesInDom[item.id] = {id: item.id, refs: 1, parts: parts};
			}
		}
	}
	
	function listToStyles(list) {
		var styles = [];
		var newStyles = {};
		for(var i = 0; i < list.length; i++) {
			var item = list[i];
			var id = item[0];
			var css = item[1];
			var media = item[2];
			var sourceMap = item[3];
			var part = {css: css, media: media, sourceMap: sourceMap};
			if(!newStyles[id])
				styles.push(newStyles[id] = {id: id, parts: [part]});
			else
				newStyles[id].parts.push(part);
		}
		return styles;
	}
	
	function createStyleElement() {
		var styleElement = document.createElement("style");
		var head = getHeadElement();
		styleElement.type = "text/css";
		head.appendChild(styleElement);
		return styleElement;
	}
	
	function createLinkElement() {
		var linkElement = document.createElement("link");
		var head = getHeadElement();
		linkElement.rel = "stylesheet";
		head.appendChild(linkElement);
		return linkElement;
	}
	
	function addStyle(obj, options) {
		var styleElement, update, remove;
	
		if (options.singleton) {
			var styleIndex = singletonCounter++;
			styleElement = singletonElement || (singletonElement = createStyleElement());
			update = applyToSingletonTag.bind(null, styleElement, styleIndex, false);
			remove = applyToSingletonTag.bind(null, styleElement, styleIndex, true);
		} else if(obj.sourceMap &&
			typeof URL === "function" &&
			typeof URL.createObjectURL === "function" &&
			typeof URL.revokeObjectURL === "function" &&
			typeof Blob === "function" &&
			typeof btoa === "function") {
			styleElement = createLinkElement();
			update = updateLink.bind(null, styleElement);
			remove = function() {
				styleElement.parentNode.removeChild(styleElement);
				if(styleElement.href)
					URL.revokeObjectURL(styleElement.href);
			};
		} else {
			styleElement = createStyleElement();
			update = applyToTag.bind(null, styleElement);
			remove = function() {
				styleElement.parentNode.removeChild(styleElement);
			};
		}
	
		update(obj);
	
		return function updateStyle(newObj) {
			if(newObj) {
				if(newObj.css === obj.css && newObj.media === obj.media && newObj.sourceMap === obj.sourceMap)
					return;
				update(obj = newObj);
			} else {
				remove();
			}
		};
	}
	
	var replaceText = (function () {
		var textStore = [];
	
		return function (index, replacement) {
			textStore[index] = replacement;
			return textStore.filter(Boolean).join('\n');
		};
	})();
	
	function applyToSingletonTag(styleElement, index, remove, obj) {
		var css = remove ? "" : obj.css;
	
		if (styleElement.styleSheet) {
			styleElement.styleSheet.cssText = replaceText(index, css);
		} else {
			var cssNode = document.createTextNode(css);
			var childNodes = styleElement.childNodes;
			if (childNodes[index]) styleElement.removeChild(childNodes[index]);
			if (childNodes.length) {
				styleElement.insertBefore(cssNode, childNodes[index]);
			} else {
				styleElement.appendChild(cssNode);
			}
		}
	}
	
	function applyToTag(styleElement, obj) {
		var css = obj.css;
		var media = obj.media;
		var sourceMap = obj.sourceMap;
	
		if(media) {
			styleElement.setAttribute("media", media)
		}
	
		if(styleElement.styleSheet) {
			styleElement.styleSheet.cssText = css;
		} else {
			while(styleElement.firstChild) {
				styleElement.removeChild(styleElement.firstChild);
			}
			styleElement.appendChild(document.createTextNode(css));
		}
	}
	
	function updateLink(linkElement, obj) {
		var css = obj.css;
		var media = obj.media;
		var sourceMap = obj.sourceMap;
	
		if(sourceMap) {
			// http://stackoverflow.com/a/26603875
			css += "\n/*# sourceMappingURL=data:application/json;base64," + btoa(unescape(encodeURIComponent(JSON.stringify(sourceMap)))) + " */";
		}
	
		var blob = new Blob([css], { type: "text/css" });
	
		var oldSrc = linkElement.href;
	
		linkElement.href = URL.createObjectURL(blob);
	
		if(oldSrc)
			URL.revokeObjectURL(oldSrc);
	}


/***/ },
/* 47 */
/***/ function(module, exports, __webpack_require__) {

	var __WEBPACK_AMD_DEFINE_RESULT__;/*!
	  Copyright (c) 2015 Jed Watson.
	  Licensed under the MIT License (MIT), see
	  http://jedwatson.github.io/classnames
	*/
	
	(function () {
		'use strict';
	
		function classNames () {
	
			var classes = '';
	
			for (var i = 0; i < arguments.length; i++) {
				var arg = arguments[i];
				if (!arg) continue;
	
				var argType = typeof arg;
	
				if ('string' === argType || 'number' === argType) {
					classes += ' ' + arg;
	
				} else if (Array.isArray(arg)) {
					classes += ' ' + classNames.apply(null, arg);
	
				} else if ('object' === argType) {
					for (var key in arg) {
						if (arg.hasOwnProperty(key) && arg[key]) {
							classes += ' ' + key;
						}
					}
				}
			}
	
			return classes.substr(1);
		}
	
		if (typeof module !== 'undefined' && module.exports) {
			module.exports = classNames;
		} else if (true){
			// AMD. Register as an anonymous module.
			!(__WEBPACK_AMD_DEFINE_RESULT__ = function () {
				return classNames;
			}.call(exports, __webpack_require__, exports, module), __WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
		} else {
			window.classNames = classNames;
		}
	
	}());


/***/ },
/* 48 */
/***/ function(module, exports) {

	"use strict";
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	var MIN_YEAR = 1970;
	exports.MIN_YEAR = MIN_YEAR;
	var MAX_YEAR = 2050;
	exports.MAX_YEAR = MAX_YEAR;
	var KEY_DELETE = 0;
	var KEY_BACKSPACE = 8;
	var KEY_ENTER = 13;
	
	var keyCode = function keyCode(e) {
	  return "undefined" != typeof e.which ? e.which : e.keyCode;
	};
	
	var isSpecialKey = function isSpecialKey(e) {
	  return e.altKey || e.shiftKey || e.ctrlKey || keyCode(e) == KEY_ENTER || keyCode(e) == KEY_BACKSPACE || keyCode(e) == KEY_DELETE;
	};
	
	var char = function char(e) {
	  return String.fromCharCode(keyCode(e));
	};
	
	var keyPressEventToString = function keyPressEventToString(e) {
	  var theChar = char(e),
	      input = e.target,
	      str = input.value;
	  return str.slice(0, input.selectionStart) + theChar + str.slice(input.selectionEnd);
	};
	
	exports.keyPressEventToString = keyPressEventToString;
	var allow = function allow(validator) {
	  return function (e) {
	    if (!isSpecialKey(e) && !validator(keyPressEventToString(e))) e.preventDefault();
	  };
	};
	
	exports.allow = allow;
	var number = function number(maybeNumber) {
	  return parseFloat(maybeNumber) == maybeNumber;
	};
	
	exports.number = number;
	var negative = function negative(validator) {
	  return function (maybeMinusSign) {
	    return validator(maybeMinusSign) || "-" == maybeMinusSign;
	  };
	};
	
	exports.negative = negative;
	var point = function point(validator) {
	  return function (maybePoint) {
	    return validator(maybePoint) || "." == maybePoint;
	  };
	};
	
	exports.point = point;
	var takeDigits = function takeDigits(n) {
	  return function (number) {
	    return String(number).substr(0, n + 1);
	  };
	};
	
	var inBounds = function inBounds(_from) {
	  return function (to) {
	    return function (inputVal) {
	      var nrDigits = inputVal.length - 1;
	      var floatVal = parseFloat(inputVal);
	      return takeDigits(nrDigits)(_from) <= floatVal && floatVal <= takeDigits(nrDigits)(to);
	    };
	  };
	};
	
	exports.inBounds = inBounds;
	var year = inBounds(MIN_YEAR)(MAX_YEAR);
	exports.year = year;

/***/ },
/* 49 */
/***/ function(module, exports, __webpack_require__) {

	
	/** @jsx h */
	"use strict";
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	
	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; desc = parent = getter = undefined; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; continue _function; } } else if ("value" in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };
	
	exports.view = view;
	exports.update = update;
	
	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }
	
	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj["default"] = obj; return newObj; } }
	
	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }
	
	function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }
	
	var _ampArchitecture = __webpack_require__(3);
	
	var AMP = _interopRequireWildcard(_ampArchitecture);
	
	var _ampModulesTranslate = __webpack_require__(40);
	
	var _ampModulesTranslate2 = _interopRequireDefault(_ampModulesTranslate);
	
	var _ampToolsValidate = __webpack_require__(48);
	
	var h = AMP.h;
	
	var Action = (function (_AMP$Action) {
	  _inherits(Action, _AMP$Action);
	
	  function Action() {
	    _classCallCheck(this, Action);
	
	    _get(Object.getPrototypeOf(Action.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Action;
	})(AMP.Action);
	
	exports.Action = Action;
	
	var YearChanged = (function (_Action) {
	  _inherits(YearChanged, _Action);
	
	  function YearChanged(year) {
	    _classCallCheck(this, YearChanged);
	
	    if (!(year instanceof integer)) throw new TypeError("Value of argument 'year' violates contract, expected integer got " + (year === null ? "null" : year instanceof Object && year.constructor ? year.constructor.name : typeof year));
	
	    _get(Object.getPrototypeOf(YearChanged.prototype), "constructor", this).call(this);
	    this.year = year;
	  }
	
	  return YearChanged;
	})(Action);
	
	exports.YearChanged = YearChanged;
	
	var YearSubmitted = (function (_Action2) {
	  _inherits(YearSubmitted, _Action2);
	
	  function YearSubmitted(year) {
	    _classCallCheck(this, YearSubmitted);
	
	    if (typeof year !== "number") throw new TypeError("Value of argument 'year' violates contract, expected number got " + (year === null ? "null" : year instanceof Object && year.constructor ? year.constructor.name : typeof year));
	
	    _get(Object.getPrototypeOf(YearSubmitted.prototype), "constructor", this).call(this);
	    this.year = function () {
	      return year;
	    };
	  }
	
	  return YearSubmitted;
	})(Action);
	
	exports.YearSubmitted = YearSubmitted;
	
	var Model = (function (_AMP$Model) {
	  _inherits(Model, _AMP$Model);
	
	  function Model() {
	    _classCallCheck(this, Model);
	
	    _get(Object.getPrototypeOf(Model.prototype), "constructor", this).apply(this, arguments);
	  }
	
	  return Model;
	})(AMP.Model);
	
	exports.Model = Model;
	var model = new Model().set('year', '');
	
	exports.model = model;
	function getValidationError(model) {
	  var year = parseInt(model.year());
	  if (!year) return;
	  if (!(year >= MIN_YEAR && year <= MAX_YEAR)) {
	    return h(
	      "span",
	      { className: "help-block" },
	      (0, _ampModulesTranslate2["default"])("The year must be between #$ and #$", MIN_YEAR, MAX_YEAR)
	    );
	  }
	
	  if (!(year < model.getIn(['except', 'from']) || year > model.getIn(['except', 'to']))) {
	    return h(
	      "span",
	      { className: "help-block" },
	      (0, _ampModulesTranslate2["default"])("There is already an entry for #$", year)
	    );
	  }
	
	  return null;
	}
	
	function view(address, model) {
	  if (!(model instanceof Model)) throw new TypeError("Value of argument 'model' violates contract, expected Model got " + (model === null ? "null" : model instanceof Object && model.constructor ? model.constructor.name : typeof model));
	
	  var submitYear = function submitYear(e) {
	    e.preventDefault();
	    address.send(new YearSubmitted(parseInt(e.target.querySelector('input').value)));
	  };
	  return h(
	    "td",
	    { className: "edit-on-hover add-new-rate" },
	    h(
	      "form",
	      { onsubmit: submitYear, action: "#" },
	      h("input", {
	        className: "form-control edit",
	        placeholder: (0, _ampModulesTranslate2["default"])('Enter the year and press Enter'),
	        onkeypress: (0, _ampToolsValidate.allow)(_ampToolsValidate.year),
	        value: model.year()
	      }),
	      h(
	        "button",
	        { className: "btn btn-primary view" },
	        (0, _ampModulesTranslate2["default"])('Add inflation rate')
	      )
	    )
	  );
	}
	
	function update(action, model) {
	  if (!(action instanceof Action)) throw new TypeError("Value of argument 'action' violates contract, expected Action got " + (action === null ? "null" : action instanceof Object && action.constructor ? action.constructor.name : typeof action));
	  if (!(model instanceof Model)) throw new TypeError("Value of argument 'model' violates contract, expected Model got " + (model === null ? "null" : model instanceof Object && model.constructor ? model.constructor.name : typeof model));
	
	  if (action instanceof YearChanged) {
	    return model.year(action.year);
	  }
	}

/***/ },
/* 50 */
/***/ function(module, exports, __webpack_require__) {

	var require;var require;!function(t){if(true)module.exports=t();else if("function"==typeof define&&define.amd)define([],t);else{var e;e="undefined"!=typeof window?window:"undefined"!=typeof global?global:"undefined"!=typeof self?self:this,e.ampBoilerplate=t()}}(function(){var t;return function e(t,n,i){function r(s,a){if(!n[s]){if(!t[s]){var l="function"==typeof require&&require;if(!a&&l)return require(s,!0);if(o)return o(s,!0);var u=new Error("Cannot find module '"+s+"'");throw u.code="MODULE_NOT_FOUND",u}var c=n[s]={exports:{}};t[s][0].call(c.exports,function(e){var n=t[s][1][e];return r(n?n:e)},c,c.exports,e,t,n,i)}return n[s].exports}for(var o="function"==typeof require&&require,s=0;s<i.length;s++)r(i[s]);return r}({1:[function(t,e,n){function i(){this.initialize.apply(this,arguments)}var r=t("underscore"),o=t("backbone"),s=t("jquery");(void 0==window.$||$.fn.jquery.split(" ")[0].split(".")[0]<2)&&(window.jQuery=window.$=o.$=s);var a="function"==typeof $().modal;a&&t("bootstrap/dist/js/bootstrap");var l=t("./src/views/menu-view.js"),u=t("./src/views/header-footer-view.js"),c=t("amp-translate");r.extend(i.prototype,o.Events,{initialize:function(t){t=r.defaults(t,{showFooterAdmin:!0,showDGFooter:!0,showLogin:!0}),r.has(t,"sync")&&(o.sync=t.sync);var e=JSON.parse('{\n"amp.common:footer": "Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and DGF",\n"amp.common:title": "AMP Toolbar",\n"amp.common:title-help": "Help",\n"amp.common:subtitle-amp-help": "AMP Help",\n"amp.common:subtitle-glossary": "Glossary",\n"amp.common:subtitle-email-support-team": "Email Support Team",\n"amp.common:title-logout": "Log Out",\n"amp.common:platform": "Aid Management Platform (AMP)",\n"[title]amp.common:platform": "Aid Management Platform",\n"amp.common:platform-short": "AMP",\n"amp.common:title-login": "Login",\n"amp.dashboard:close": "Close",\n"amp.about:modal.title": "About AMP",\n"amp.about:credits": "Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and Development Gateway Foundation.",\n"amp.about:trademark": "The Development Gateway and the The Development Gateway logo are trademarks for The Development Gateway Foundation",\n"amp.about:rights": "All Rights Reserved",\n"amp.about:version": "Version"\n}\n\n');this.translator=new c({defaultKeys:e}),t.translator=this.translator,this.menu=new l(t),this.headerFooter=new u(t),this.listenTo(this.headerFooter,"all",function(){this.trigger.apply(this,arguments)}),this.listenTo(this.menu,"all",function(){this.trigger.apply(this,arguments)});var n=this;$.when(this.menu.menuRendered,this.headerFooter.layoutFetched).then(function(){n.headerFooter.refreshUserSection(),n.translator.translateDOM(document),void 0!==$.fn.dropdown&&$(".dropdown-toggle").dropdown()})}}),e.exports={layout:i},window.boilerplate=i},{"./src/views/header-footer-view.js":11,"./src/views/menu-view.js":12,"amp-translate":14,backbone:2,"bootstrap/dist/js/bootstrap":4,jquery:5,underscore:6}],2:[function(e,n,i){!function(n,r){if("function"==typeof t&&t.amd)t(["underscore","jquery","exports"],function(t,e,i){n.Backbone=r(n,i,t,e)});else if("undefined"!=typeof i){var o=e("underscore");r(n,i,o)}else n.Backbone=r(n,{},n._,n.jQuery||n.Zepto||n.ender||n.$)}(this,function(t,e,n,i){{var r=t.Backbone,o=[],s=(o.push,o.slice);o.splice}e.VERSION="1.1.2",e.$=i,e.noConflict=function(){return t.Backbone=r,this},e.emulateHTTP=!1,e.emulateJSON=!1;var a=e.Events={on:function(t,e,n){if(!u(this,"on",t,[e,n])||!e)return this;this._events||(this._events={});var i=this._events[t]||(this._events[t]=[]);return i.push({callback:e,context:n,ctx:n||this}),this},once:function(t,e,i){if(!u(this,"once",t,[e,i])||!e)return this;var r=this,o=n.once(function(){r.off(t,o),e.apply(this,arguments)});return o._callback=e,this.on(t,o,i)},off:function(t,e,i){var r,o,s,a,l,c,h,p;if(!this._events||!u(this,"off",t,[e,i]))return this;if(!t&&!e&&!i)return this._events=void 0,this;for(a=t?[t]:n.keys(this._events),l=0,c=a.length;c>l;l++)if(t=a[l],s=this._events[t]){if(this._events[t]=r=[],e||i)for(h=0,p=s.length;p>h;h++)o=s[h],(e&&e!==o.callback&&e!==o.callback._callback||i&&i!==o.context)&&r.push(o);r.length||delete this._events[t]}return this},trigger:function(t){if(!this._events)return this;var e=s.call(arguments,1);if(!u(this,"trigger",t,e))return this;var n=this._events[t],i=this._events.all;return n&&c(n,e),i&&c(i,arguments),this},stopListening:function(t,e,i){var r=this._listeningTo;if(!r)return this;var o=!e&&!i;i||"object"!=typeof e||(i=this),t&&((r={})[t._listenId]=t);for(var s in r)t=r[s],t.off(e,i,this),(o||n.isEmpty(t._events))&&delete this._listeningTo[s];return this}},l=/\s+/,u=function(t,e,n,i){if(!n)return!0;if("object"==typeof n){for(var r in n)t[e].apply(t,[r,n[r]].concat(i));return!1}if(l.test(n)){for(var o=n.split(l),s=0,a=o.length;a>s;s++)t[e].apply(t,[o[s]].concat(i));return!1}return!0},c=function(t,e){var n,i=-1,r=t.length,o=e[0],s=e[1],a=e[2];switch(e.length){case 0:for(;++i<r;)(n=t[i]).callback.call(n.ctx);return;case 1:for(;++i<r;)(n=t[i]).callback.call(n.ctx,o);return;case 2:for(;++i<r;)(n=t[i]).callback.call(n.ctx,o,s);return;case 3:for(;++i<r;)(n=t[i]).callback.call(n.ctx,o,s,a);return;default:for(;++i<r;)(n=t[i]).callback.apply(n.ctx,e);return}},h={listenTo:"on",listenToOnce:"once"};n.each(h,function(t,e){a[e]=function(e,i,r){var o=this._listeningTo||(this._listeningTo={}),s=e._listenId||(e._listenId=n.uniqueId("l"));return o[s]=e,r||"object"!=typeof i||(r=this),e[t](i,r,this),this}}),a.bind=a.on,a.unbind=a.off,n.extend(e,a);var p=e.Model=function(t,e){var i=t||{};e||(e={}),this.cid=n.uniqueId("c"),this.attributes={},e.collection&&(this.collection=e.collection),e.parse&&(i=this.parse(i,e)||{}),i=n.defaults({},i,n.result(this,"defaults")),this.set(i,e),this.changed={},this.initialize.apply(this,arguments)};n.extend(p.prototype,a,{changed:null,validationError:null,idAttribute:"id",initialize:function(){},toJSON:function(t){return n.clone(this.attributes)},sync:function(){return e.sync.apply(this,arguments)},get:function(t){return this.attributes[t]},escape:function(t){return n.escape(this.get(t))},has:function(t){return null!=this.get(t)},set:function(t,e,i){var r,o,s,a,l,u,c,h;if(null==t)return this;if("object"==typeof t?(o=t,i=e):(o={})[t]=e,i||(i={}),!this._validate(o,i))return!1;s=i.unset,l=i.silent,a=[],u=this._changing,this._changing=!0,u||(this._previousAttributes=n.clone(this.attributes),this.changed={}),h=this.attributes,c=this._previousAttributes,this.idAttribute in o&&(this.id=o[this.idAttribute]);for(r in o)e=o[r],n.isEqual(h[r],e)||a.push(r),n.isEqual(c[r],e)?delete this.changed[r]:this.changed[r]=e,s?delete h[r]:h[r]=e;if(!l){a.length&&(this._pending=i);for(var p=0,d=a.length;d>p;p++)this.trigger("change:"+a[p],this,h[a[p]],i)}if(u)return this;if(!l)for(;this._pending;)i=this._pending,this._pending=!1,this.trigger("change",this,i);return this._pending=!1,this._changing=!1,this},unset:function(t,e){return this.set(t,void 0,n.extend({},e,{unset:!0}))},clear:function(t){var e={};for(var i in this.attributes)e[i]=void 0;return this.set(e,n.extend({},t,{unset:!0}))},hasChanged:function(t){return null==t?!n.isEmpty(this.changed):n.has(this.changed,t)},changedAttributes:function(t){if(!t)return this.hasChanged()?n.clone(this.changed):!1;var e,i=!1,r=this._changing?this._previousAttributes:this.attributes;for(var o in t)n.isEqual(r[o],e=t[o])||((i||(i={}))[o]=e);return i},previous:function(t){return null!=t&&this._previousAttributes?this._previousAttributes[t]:null},previousAttributes:function(){return n.clone(this._previousAttributes)},fetch:function(t){t=t?n.clone(t):{},void 0===t.parse&&(t.parse=!0);var e=this,i=t.success;return t.success=function(n){return e.set(e.parse(n,t),t)?(i&&i(e,n,t),void e.trigger("sync",e,n,t)):!1},F(this,t),this.sync("read",this,t)},save:function(t,e,i){var r,o,s,a=this.attributes;if(null==t||"object"==typeof t?(r=t,i=e):(r={})[t]=e,i=n.extend({validate:!0},i),r&&!i.wait){if(!this.set(r,i))return!1}else if(!this._validate(r,i))return!1;r&&i.wait&&(this.attributes=n.extend({},a,r)),void 0===i.parse&&(i.parse=!0);var l=this,u=i.success;return i.success=function(t){l.attributes=a;var e=l.parse(t,i);return i.wait&&(e=n.extend(r||{},e)),n.isObject(e)&&!l.set(e,i)?!1:(u&&u(l,t,i),void l.trigger("sync",l,t,i))},F(this,i),o=this.isNew()?"create":i.patch?"patch":"update","patch"===o&&(i.attrs=r),s=this.sync(o,this,i),r&&i.wait&&(this.attributes=a),s},destroy:function(t){t=t?n.clone(t):{};var e=this,i=t.success,r=function(){e.trigger("destroy",e,e.collection,t)};if(t.success=function(n){(t.wait||e.isNew())&&r(),i&&i(e,n,t),e.isNew()||e.trigger("sync",e,n,t)},this.isNew())return t.success(),!1;F(this,t);var o=this.sync("delete",this,t);return t.wait||r(),o},url:function(){var t=n.result(this,"urlRoot")||n.result(this.collection,"url")||P();return this.isNew()?t:t.replace(/([^\/])$/,"$1/")+encodeURIComponent(this.id)},parse:function(t,e){return t},clone:function(){return new this.constructor(this.attributes)},isNew:function(){return!this.has(this.idAttribute)},isValid:function(t){return this._validate({},n.extend(t||{},{validate:!0}))},_validate:function(t,e){if(!e.validate||!this.validate)return!0;t=n.extend({},this.attributes,t);var i=this.validationError=this.validate(t,e)||null;return i?(this.trigger("invalid",this,i,n.extend(e,{validationError:i})),!1):!0}});var d=["keys","values","pairs","invert","pick","omit"];n.each(d,function(t){p.prototype[t]=function(){var e=s.call(arguments);return e.unshift(this.attributes),n[t].apply(n,e)}});var f=e.Collection=function(t,e){e||(e={}),e.model&&(this.model=e.model),void 0!==e.comparator&&(this.comparator=e.comparator),this._reset(),this.initialize.apply(this,arguments),t&&this.reset(t,n.extend({silent:!0},e))},g={add:!0,remove:!0,merge:!0},m={add:!0,remove:!1};n.extend(f.prototype,a,{model:p,initialize:function(){},toJSON:function(t){return this.map(function(e){return e.toJSON(t)})},sync:function(){return e.sync.apply(this,arguments)},add:function(t,e){return this.set(t,n.extend({merge:!1},e,m))},remove:function(t,e){var i=!n.isArray(t);t=i?[t]:n.clone(t),e||(e={});var r,o,s,a;for(r=0,o=t.length;o>r;r++)a=t[r]=this.get(t[r]),a&&(delete this._byId[a.id],delete this._byId[a.cid],s=this.indexOf(a),this.models.splice(s,1),this.length--,e.silent||(e.index=s,a.trigger("remove",a,this,e)),this._removeReference(a,e));return i?t[0]:t},set:function(t,e){e=n.defaults({},e,g),e.parse&&(t=this.parse(t,e));var i=!n.isArray(t);t=i?t?[t]:[]:n.clone(t);var r,o,s,a,l,u,c,h=e.at,d=this.model,f=this.comparator&&null==h&&e.sort!==!1,m=n.isString(this.comparator)?this.comparator:null,v=[],y=[],b={},w=e.add,x=e.merge,T=e.remove,C=!f&&w&&T?[]:!1;for(r=0,o=t.length;o>r;r++){if(l=t[r]||{},s=l instanceof p?a=l:l[d.prototype.idAttribute||"id"],u=this.get(s))T&&(b[u.cid]=!0),x&&(l=l===a?a.attributes:l,e.parse&&(l=u.parse(l,e)),u.set(l,e),f&&!c&&u.hasChanged(m)&&(c=!0)),t[r]=u;else if(w){if(a=t[r]=this._prepareModel(l,e),!a)continue;v.push(a),this._addReference(a,e)}a=u||a,!C||!a.isNew()&&b[a.id]||C.push(a),b[a.id]=!0}if(T){for(r=0,o=this.length;o>r;++r)b[(a=this.models[r]).cid]||y.push(a);y.length&&this.remove(y,e)}if(v.length||C&&C.length)if(f&&(c=!0),this.length+=v.length,null!=h)for(r=0,o=v.length;o>r;r++)this.models.splice(h+r,0,v[r]);else{C&&(this.models.length=0);var k=C||v;for(r=0,o=k.length;o>r;r++)this.models.push(k[r])}if(c&&this.sort({silent:!0}),!e.silent){for(r=0,o=v.length;o>r;r++)(a=v[r]).trigger("add",a,this,e);(c||C&&C.length)&&this.trigger("sort",this,e)}return i?t[0]:t},reset:function(t,e){e||(e={});for(var i=0,r=this.models.length;r>i;i++)this._removeReference(this.models[i],e);return e.previousModels=this.models,this._reset(),t=this.add(t,n.extend({silent:!0},e)),e.silent||this.trigger("reset",this,e),t},push:function(t,e){return this.add(t,n.extend({at:this.length},e))},pop:function(t){var e=this.at(this.length-1);return this.remove(e,t),e},unshift:function(t,e){return this.add(t,n.extend({at:0},e))},shift:function(t){var e=this.at(0);return this.remove(e,t),e},slice:function(){return s.apply(this.models,arguments)},get:function(t){return null==t?void 0:this._byId[t]||this._byId[t.id]||this._byId[t.cid]},at:function(t){return this.models[t]},where:function(t,e){return n.isEmpty(t)?e?void 0:[]:this[e?"find":"filter"](function(e){for(var n in t)if(t[n]!==e.get(n))return!1;return!0})},findWhere:function(t){return this.where(t,!0)},sort:function(t){if(!this.comparator)throw new Error("Cannot sort a set without a comparator");return t||(t={}),n.isString(this.comparator)||1===this.comparator.length?this.models=this.sortBy(this.comparator,this):this.models.sort(n.bind(this.comparator,this)),t.silent||this.trigger("sort",this,t),this},pluck:function(t){return n.invoke(this.models,"get",t)},fetch:function(t){t=t?n.clone(t):{},void 0===t.parse&&(t.parse=!0);var e=t.success,i=this;return t.success=function(n){var r=t.reset?"reset":"set";i[r](n,t),e&&e(i,n,t),i.trigger("sync",i,n,t)},F(this,t),this.sync("read",this,t)},create:function(t,e){if(e=e?n.clone(e):{},!(t=this._prepareModel(t,e)))return!1;e.wait||this.add(t,e);var i=this,r=e.success;return e.success=function(t,n){e.wait&&i.add(t,e),r&&r(t,n,e)},t.save(null,e),t},parse:function(t,e){return t},clone:function(){return new this.constructor(this.models)},_reset:function(){this.length=0,this.models=[],this._byId={}},_prepareModel:function(t,e){if(t instanceof p)return t;e=e?n.clone(e):{},e.collection=this;var i=new this.model(t,e);return i.validationError?(this.trigger("invalid",this,i.validationError,e),!1):i},_addReference:function(t,e){this._byId[t.cid]=t,null!=t.id&&(this._byId[t.id]=t),t.collection||(t.collection=this),t.on("all",this._onModelEvent,this)},_removeReference:function(t,e){this===t.collection&&delete t.collection,t.off("all",this._onModelEvent,this)},_onModelEvent:function(t,e,n,i){("add"!==t&&"remove"!==t||n===this)&&("destroy"===t&&this.remove(e,i),e&&t==="change:"+e.idAttribute&&(delete this._byId[e.previous(e.idAttribute)],null!=e.id&&(this._byId[e.id]=e)),this.trigger.apply(this,arguments))}});var v=["forEach","each","map","collect","reduce","foldl","inject","reduceRight","foldr","find","detect","filter","select","reject","every","all","some","any","include","contains","invoke","max","min","toArray","size","first","head","take","initial","rest","tail","drop","last","without","difference","indexOf","shuffle","lastIndexOf","isEmpty","chain","sample"];n.each(v,function(t){f.prototype[t]=function(){var e=s.call(arguments);return e.unshift(this.models),n[t].apply(n,e)}});var y=["groupBy","countBy","sortBy","indexBy"];n.each(y,function(t){f.prototype[t]=function(e,i){var r=n.isFunction(e)?e:function(t){return t.get(e)};return n[t](this.models,r,i)}});var b=e.View=function(t){this.cid=n.uniqueId("view"),t||(t={}),n.extend(this,n.pick(t,x)),this._ensureElement(),this.initialize.apply(this,arguments),this.delegateEvents()},w=/^(\S+)\s*(.*)$/,x=["model","collection","el","id","attributes","className","tagName","events"];n.extend(b.prototype,a,{tagName:"div",$:function(t){return this.$el.find(t)},initialize:function(){},render:function(){return this},remove:function(){return this.$el.remove(),this.stopListening(),this},setElement:function(t,n){return this.$el&&this.undelegateEvents(),this.$el=t instanceof e.$?t:e.$(t),this.el=this.$el[0],n!==!1&&this.delegateEvents(),this},delegateEvents:function(t){if(!t&&!(t=n.result(this,"events")))return this;this.undelegateEvents();for(var e in t){var i=t[e];if(n.isFunction(i)||(i=this[t[e]]),i){var r=e.match(w),o=r[1],s=r[2];i=n.bind(i,this),o+=".delegateEvents"+this.cid,""===s?this.$el.on(o,i):this.$el.on(o,s,i)}}return this},undelegateEvents:function(){return this.$el.off(".delegateEvents"+this.cid),this},_ensureElement:function(){if(this.el)this.setElement(n.result(this,"el"),!1);else{var t=n.extend({},n.result(this,"attributes"));this.id&&(t.id=n.result(this,"id")),this.className&&(t["class"]=n.result(this,"className"));var i=e.$("<"+n.result(this,"tagName")+">").attr(t);this.setElement(i,!1)}}}),e.sync=function(t,i,r){var o=C[t];n.defaults(r||(r={}),{emulateHTTP:e.emulateHTTP,emulateJSON:e.emulateJSON});var s={type:o,dataType:"json"};if(r.url||(s.url=n.result(i,"url")||P()),null!=r.data||!i||"create"!==t&&"update"!==t&&"patch"!==t||(s.contentType="application/json",s.data=JSON.stringify(r.attrs||i.toJSON(r))),r.emulateJSON&&(s.contentType="application/x-www-form-urlencoded",s.data=s.data?{model:s.data}:{}),r.emulateHTTP&&("PUT"===o||"DELETE"===o||"PATCH"===o)){s.type="POST",r.emulateJSON&&(s.data._method=o);var a=r.beforeSend;r.beforeSend=function(t){return t.setRequestHeader("X-HTTP-Method-Override",o),a?a.apply(this,arguments):void 0}}"GET"===s.type||r.emulateJSON||(s.processData=!1),"PATCH"===s.type&&T&&(s.xhr=function(){return new ActiveXObject("Microsoft.XMLHTTP")});var l=r.xhr=e.ajax(n.extend(s,r));return i.trigger("request",i,l,r),l};var T=!("undefined"==typeof window||!window.ActiveXObject||window.XMLHttpRequest&&(new XMLHttpRequest).dispatchEvent),C={create:"POST",update:"PUT",patch:"PATCH","delete":"DELETE",read:"GET"};e.ajax=function(){return e.$.ajax.apply(e.$,arguments)};var k=e.Router=function(t){t||(t={}),t.routes&&(this.routes=t.routes),this._bindRoutes(),this.initialize.apply(this,arguments)},E=/\((.*?)\)/g,A=/(\(\?)?:\w+/g,$=/\*\w+/g,S=/[\-{}\[\]+?.,\\\^$|#\s]/g;n.extend(k.prototype,a,{initialize:function(){},route:function(t,i,r){n.isRegExp(t)||(t=this._routeToRegExp(t)),n.isFunction(i)&&(r=i,i=""),r||(r=this[i]);var o=this;return e.history.route(t,function(n){var s=o._extractParameters(t,n);o.execute(r,s),o.trigger.apply(o,["route:"+i].concat(s)),o.trigger("route",i,s),e.history.trigger("route",o,i,s)}),this},execute:function(t,e){t&&t.apply(this,e)},navigate:function(t,n){return e.history.navigate(t,n),this},_bindRoutes:function(){if(this.routes){this.routes=n.result(this,"routes");for(var t,e=n.keys(this.routes);null!=(t=e.pop());)this.route(t,this.routes[t])}},_routeToRegExp:function(t){return t=t.replace(S,"\\$&").replace(E,"(?:$1)?").replace(A,function(t,e){return e?t:"([^/?]+)"}).replace($,"([^?]*?)"),new RegExp("^"+t+"(?:\\?([\\s\\S]*))?$")},_extractParameters:function(t,e){var i=t.exec(e).slice(1);return n.map(i,function(t,e){return e===i.length-1?t||null:t?decodeURIComponent(t):null})}});var N=e.History=function(){this.handlers=[],n.bindAll(this,"checkUrl"),"undefined"!=typeof window&&(this.location=window.location,this.history=window.history)},j=/^[#\/]|\s+$/g,D=/^\/+|\/+$/g,_=/msie [\w.]+/,O=/\/$/,L=/#.*$/;N.started=!1,n.extend(N.prototype,a,{interval:50,atRoot:function(){return this.location.pathname.replace(/[^\/]$/,"$&/")===this.root},getHash:function(t){var e=(t||this).location.href.match(/#(.*)$/);return e?e[1]:""},getFragment:function(t,e){if(null==t)if(this._hasPushState||!this._wantsHashChange||e){t=decodeURI(this.location.pathname+this.location.search);var n=this.root.replace(O,"");t.indexOf(n)||(t=t.slice(n.length))}else t=this.getHash();return t.replace(j,"")},start:function(t){if(N.started)throw new Error("Backbone.history has already been started");N.started=!0,this.options=n.extend({root:"/"},this.options,t),this.root=this.options.root,this._wantsHashChange=this.options.hashChange!==!1,this._wantsPushState=!!this.options.pushState,this._hasPushState=!!(this.options.pushState&&this.history&&this.history.pushState);var i=this.getFragment(),r=document.documentMode,o=_.exec(navigator.userAgent.toLowerCase())&&(!r||7>=r);if(this.root=("/"+this.root+"/").replace(D,"/"),o&&this._wantsHashChange){var s=e.$('<iframe src="javascript:0" tabindex="-1">');this.iframe=s.hide().appendTo("body")[0].contentWindow,this.navigate(i)}this._hasPushState?e.$(window).on("popstate",this.checkUrl):this._wantsHashChange&&"onhashchange"in window&&!o?e.$(window).on("hashchange",this.checkUrl):this._wantsHashChange&&(this._checkUrlInterval=setInterval(this.checkUrl,this.interval)),this.fragment=i;var a=this.location;if(this._wantsHashChange&&this._wantsPushState){if(!this._hasPushState&&!this.atRoot())return this.fragment=this.getFragment(null,!0),this.location.replace(this.root+"#"+this.fragment),!0;this._hasPushState&&this.atRoot()&&a.hash&&(this.fragment=this.getHash().replace(j,""),this.history.replaceState({},document.title,this.root+this.fragment))}return this.options.silent?void 0:this.loadUrl()},stop:function(){e.$(window).off("popstate",this.checkUrl).off("hashchange",this.checkUrl),this._checkUrlInterval&&clearInterval(this._checkUrlInterval),N.started=!1},route:function(t,e){this.handlers.unshift({route:t,callback:e})},checkUrl:function(t){var e=this.getFragment();return e===this.fragment&&this.iframe&&(e=this.getFragment(this.getHash(this.iframe))),e===this.fragment?!1:(this.iframe&&this.navigate(e),void this.loadUrl())},loadUrl:function(t){return t=this.fragment=this.getFragment(t),n.any(this.handlers,function(e){return e.route.test(t)?(e.callback(t),!0):void 0})},navigate:function(t,e){if(!N.started)return!1;e&&e!==!0||(e={trigger:!!e});var n=this.root+(t=this.getFragment(t||""));if(t=t.replace(L,""),this.fragment!==t){if(this.fragment=t,""===t&&"/"!==n&&(n=n.slice(0,-1)),this._hasPushState)this.history[e.replace?"replaceState":"pushState"]({},document.title,n);else{if(!this._wantsHashChange)return this.location.assign(n);this._updateHash(this.location,t,e.replace),this.iframe&&t!==this.getFragment(this.getHash(this.iframe))&&(e.replace||this.iframe.document.open().close(),this._updateHash(this.iframe.location,t,e.replace))}return e.trigger?this.loadUrl(t):void 0}},_updateHash:function(t,e,n){if(n){var i=t.href.replace(/(javascript:|#).*$/,"");t.replace(i+"#"+e)}else t.hash="#"+e}}),e.history=new N;var I=function(t,e){var i,r=this;i=t&&n.has(t,"constructor")?t.constructor:function(){return r.apply(this,arguments)},n.extend(i,r,e);var o=function(){this.constructor=i};return o.prototype=r.prototype,i.prototype=new o,t&&n.extend(i.prototype,t),i.__super__=r.prototype,i};p.extend=f.extend=k.extend=b.extend=N.extend=I;var P=function(){throw new Error('A "url" property or function must be specified')},F=function(t,e){var n=e.error;e.error=function(i){n&&n(t,i,e),t.trigger("error",t,i,e)}};return e})},{underscore:3}],3:[function(e,n,i){(function(){var e=this,r=e._,o=Array.prototype,s=Object.prototype,a=Function.prototype,l=o.push,u=o.slice,c=o.concat,h=s.toString,p=s.hasOwnProperty,d=Array.isArray,f=Object.keys,g=a.bind,m=function(t){return t instanceof m?t:this instanceof m?void(this._wrapped=t):new m(t)};"undefined"!=typeof i?("undefined"!=typeof n&&n.exports&&(i=n.exports=m),i._=m):e._=m,m.VERSION="1.7.0";var v=function(t,e,n){if(void 0===e)return t;switch(null==n?3:n){case 1:return function(n){return t.call(e,n)};case 2:return function(n,i){return t.call(e,n,i)};case 3:return function(n,i,r){return t.call(e,n,i,r)};case 4:return function(n,i,r,o){return t.call(e,n,i,r,o)}}return function(){return t.apply(e,arguments)}};m.iteratee=function(t,e,n){return null==t?m.identity:m.isFunction(t)?v(t,e,n):m.isObject(t)?m.matches(t):m.property(t)},m.each=m.forEach=function(t,e,n){if(null==t)return t;e=v(e,n);var i,r=t.length;if(r===+r)for(i=0;r>i;i++)e(t[i],i,t);else{var o=m.keys(t);for(i=0,r=o.length;r>i;i++)e(t[o[i]],o[i],t)}return t},m.map=m.collect=function(t,e,n){if(null==t)return[];e=m.iteratee(e,n);for(var i,r=t.length!==+t.length&&m.keys(t),o=(r||t).length,s=Array(o),a=0;o>a;a++)i=r?r[a]:a,s[a]=e(t[i],i,t);return s};var y="Reduce of empty array with no initial value";m.reduce=m.foldl=m.inject=function(t,e,n,i){null==t&&(t=[]),e=v(e,i,4);var r,o=t.length!==+t.length&&m.keys(t),s=(o||t).length,a=0;if(arguments.length<3){if(!s)throw new TypeError(y);n=t[o?o[a++]:a++]}for(;s>a;a++)r=o?o[a]:a,n=e(n,t[r],r,t);return n},m.reduceRight=m.foldr=function(t,e,n,i){null==t&&(t=[]),e=v(e,i,4);var r,o=t.length!==+t.length&&m.keys(t),s=(o||t).length;if(arguments.length<3){if(!s)throw new TypeError(y);n=t[o?o[--s]:--s]}for(;s--;)r=o?o[s]:s,n=e(n,t[r],r,t);return n},m.find=m.detect=function(t,e,n){var i;return e=m.iteratee(e,n),m.some(t,function(t,n,r){return e(t,n,r)?(i=t,!0):void 0}),i},m.filter=m.select=function(t,e,n){var i=[];return null==t?i:(e=m.iteratee(e,n),m.each(t,function(t,n,r){e(t,n,r)&&i.push(t)}),i)},m.reject=function(t,e,n){return m.filter(t,m.negate(m.iteratee(e)),n)},m.every=m.all=function(t,e,n){if(null==t)return!0;e=m.iteratee(e,n);var i,r,o=t.length!==+t.length&&m.keys(t),s=(o||t).length;for(i=0;s>i;i++)if(r=o?o[i]:i,!e(t[r],r,t))return!1;return!0},m.some=m.any=function(t,e,n){if(null==t)return!1;e=m.iteratee(e,n);var i,r,o=t.length!==+t.length&&m.keys(t),s=(o||t).length;for(i=0;s>i;i++)if(r=o?o[i]:i,e(t[r],r,t))return!0;return!1},m.contains=m.include=function(t,e){return null==t?!1:(t.length!==+t.length&&(t=m.values(t)),m.indexOf(t,e)>=0)},m.invoke=function(t,e){var n=u.call(arguments,2),i=m.isFunction(e);return m.map(t,function(t){return(i?e:t[e]).apply(t,n)})},m.pluck=function(t,e){return m.map(t,m.property(e))},m.where=function(t,e){return m.filter(t,m.matches(e))},m.findWhere=function(t,e){return m.find(t,m.matches(e))},m.max=function(t,e,n){var i,r,o=-(1/0),s=-(1/0);if(null==e&&null!=t){t=t.length===+t.length?t:m.values(t);for(var a=0,l=t.length;l>a;a++)i=t[a],i>o&&(o=i)}else e=m.iteratee(e,n),m.each(t,function(t,n,i){r=e(t,n,i),(r>s||r===-(1/0)&&o===-(1/0))&&(o=t,s=r)});return o},m.min=function(t,e,n){var i,r,o=1/0,s=1/0;if(null==e&&null!=t){t=t.length===+t.length?t:m.values(t);for(var a=0,l=t.length;l>a;a++)i=t[a],o>i&&(o=i)}else e=m.iteratee(e,n),m.each(t,function(t,n,i){r=e(t,n,i),(s>r||r===1/0&&o===1/0)&&(o=t,s=r)});return o},m.shuffle=function(t){for(var e,n=t&&t.length===+t.length?t:m.values(t),i=n.length,r=Array(i),o=0;i>o;o++)e=m.random(0,o),e!==o&&(r[o]=r[e]),r[e]=n[o];return r},m.sample=function(t,e,n){return null==e||n?(t.length!==+t.length&&(t=m.values(t)),t[m.random(t.length-1)]):m.shuffle(t).slice(0,Math.max(0,e))},m.sortBy=function(t,e,n){return e=m.iteratee(e,n),m.pluck(m.map(t,function(t,n,i){return{value:t,index:n,criteria:e(t,n,i)}}).sort(function(t,e){var n=t.criteria,i=e.criteria;if(n!==i){if(n>i||void 0===n)return 1;if(i>n||void 0===i)return-1}return t.index-e.index}),"value")};var b=function(t){return function(e,n,i){var r={};return n=m.iteratee(n,i),m.each(e,function(i,o){var s=n(i,o,e);t(r,i,s)}),r}};m.groupBy=b(function(t,e,n){m.has(t,n)?t[n].push(e):t[n]=[e]}),m.indexBy=b(function(t,e,n){t[n]=e}),m.countBy=b(function(t,e,n){m.has(t,n)?t[n]++:t[n]=1}),m.sortedIndex=function(t,e,n,i){n=m.iteratee(n,i,1);for(var r=n(e),o=0,s=t.length;s>o;){var a=o+s>>>1;n(t[a])<r?o=a+1:s=a}return o},m.toArray=function(t){return t?m.isArray(t)?u.call(t):t.length===+t.length?m.map(t,m.identity):m.values(t):[]},m.size=function(t){return null==t?0:t.length===+t.length?t.length:m.keys(t).length},m.partition=function(t,e,n){e=m.iteratee(e,n);var i=[],r=[];return m.each(t,function(t,n,o){(e(t,n,o)?i:r).push(t)}),[i,r]},m.first=m.head=m.take=function(t,e,n){return null==t?void 0:null==e||n?t[0]:0>e?[]:u.call(t,0,e)},m.initial=function(t,e,n){return u.call(t,0,Math.max(0,t.length-(null==e||n?1:e)))},m.last=function(t,e,n){return null==t?void 0:null==e||n?t[t.length-1]:u.call(t,Math.max(t.length-e,0))},m.rest=m.tail=m.drop=function(t,e,n){return u.call(t,null==e||n?1:e)},m.compact=function(t){return m.filter(t,m.identity)};var w=function(t,e,n,i){if(e&&m.every(t,m.isArray))return c.apply(i,t);for(var r=0,o=t.length;o>r;r++){var s=t[r];m.isArray(s)||m.isArguments(s)?e?l.apply(i,s):w(s,e,n,i):n||i.push(s)}return i};m.flatten=function(t,e){return w(t,e,!1,[])},m.without=function(t){return m.difference(t,u.call(arguments,1))},m.uniq=m.unique=function(t,e,n,i){if(null==t)return[];m.isBoolean(e)||(i=n,n=e,e=!1),null!=n&&(n=m.iteratee(n,i));for(var r=[],o=[],s=0,a=t.length;a>s;s++){var l=t[s];if(e)s&&o===l||r.push(l),o=l;else if(n){var u=n(l,s,t);m.indexOf(o,u)<0&&(o.push(u),r.push(l))}else m.indexOf(r,l)<0&&r.push(l)}return r},m.union=function(){return m.uniq(w(arguments,!0,!0,[]))},m.intersection=function(t){if(null==t)return[];for(var e=[],n=arguments.length,i=0,r=t.length;r>i;i++){var o=t[i];if(!m.contains(e,o)){for(var s=1;n>s&&m.contains(arguments[s],o);s++);s===n&&e.push(o)}}return e},m.difference=function(t){var e=w(u.call(arguments,1),!0,!0,[]);return m.filter(t,function(t){return!m.contains(e,t)})},m.zip=function(t){if(null==t)return[];for(var e=m.max(arguments,"length").length,n=Array(e),i=0;e>i;i++)n[i]=m.pluck(arguments,i);return n},m.object=function(t,e){if(null==t)return{};for(var n={},i=0,r=t.length;r>i;i++)e?n[t[i]]=e[i]:n[t[i][0]]=t[i][1];return n},m.indexOf=function(t,e,n){if(null==t)return-1;var i=0,r=t.length;if(n){if("number"!=typeof n)return i=m.sortedIndex(t,e),t[i]===e?i:-1;i=0>n?Math.max(0,r+n):n}for(;r>i;i++)if(t[i]===e)return i;return-1},m.lastIndexOf=function(t,e,n){if(null==t)return-1;var i=t.length;for("number"==typeof n&&(i=0>n?i+n+1:Math.min(i,n+1));--i>=0;)if(t[i]===e)return i;return-1},m.range=function(t,e,n){arguments.length<=1&&(e=t||0,t=0),n=n||1;for(var i=Math.max(Math.ceil((e-t)/n),0),r=Array(i),o=0;i>o;o++,t+=n)r[o]=t;return r};var x=function(){};m.bind=function(t,e){var n,i;if(g&&t.bind===g)return g.apply(t,u.call(arguments,1));if(!m.isFunction(t))throw new TypeError("Bind must be called on a function");return n=u.call(arguments,2),i=function(){if(!(this instanceof i))return t.apply(e,n.concat(u.call(arguments)));x.prototype=t.prototype;var r=new x;x.prototype=null;var o=t.apply(r,n.concat(u.call(arguments)));return m.isObject(o)?o:r}},m.partial=function(t){var e=u.call(arguments,1);return function(){for(var n=0,i=e.slice(),r=0,o=i.length;o>r;r++)i[r]===m&&(i[r]=arguments[n++]);for(;n<arguments.length;)i.push(arguments[n++]);return t.apply(this,i)}},m.bindAll=function(t){var e,n,i=arguments.length;if(1>=i)throw new Error("bindAll must be passed function names");for(e=1;i>e;e++)n=arguments[e],t[n]=m.bind(t[n],t);return t},m.memoize=function(t,e){var n=function(i){var r=n.cache,o=e?e.apply(this,arguments):i;return m.has(r,o)||(r[o]=t.apply(this,arguments)),r[o]};return n.cache={},n},m.delay=function(t,e){var n=u.call(arguments,2);return setTimeout(function(){return t.apply(null,n)},e)},m.defer=function(t){return m.delay.apply(m,[t,1].concat(u.call(arguments,1)))},m.throttle=function(t,e,n){var i,r,o,s=null,a=0;n||(n={});var l=function(){a=n.leading===!1?0:m.now(),s=null,o=t.apply(i,r),s||(i=r=null)};return function(){var u=m.now();a||n.leading!==!1||(a=u);var c=e-(u-a);return i=this,r=arguments,0>=c||c>e?(clearTimeout(s),s=null,a=u,o=t.apply(i,r),s||(i=r=null)):s||n.trailing===!1||(s=setTimeout(l,c)),o}},m.debounce=function(t,e,n){var i,r,o,s,a,l=function(){var u=m.now()-s;e>u&&u>0?i=setTimeout(l,e-u):(i=null,n||(a=t.apply(o,r),i||(o=r=null)))};return function(){o=this,r=arguments,s=m.now();var u=n&&!i;return i||(i=setTimeout(l,e)),u&&(a=t.apply(o,r),o=r=null),a}},m.wrap=function(t,e){return m.partial(e,t)},m.negate=function(t){return function(){return!t.apply(this,arguments)}},m.compose=function(){var t=arguments,e=t.length-1;return function(){for(var n=e,i=t[e].apply(this,arguments);n--;)i=t[n].call(this,i);return i}},m.after=function(t,e){return function(){return--t<1?e.apply(this,arguments):void 0}},m.before=function(t,e){var n;return function(){return--t>0?n=e.apply(this,arguments):e=null,n}},m.once=m.partial(m.before,2),m.keys=function(t){if(!m.isObject(t))return[];if(f)return f(t);var e=[];for(var n in t)m.has(t,n)&&e.push(n);return e},m.values=function(t){for(var e=m.keys(t),n=e.length,i=Array(n),r=0;n>r;r++)i[r]=t[e[r]];return i},m.pairs=function(t){for(var e=m.keys(t),n=e.length,i=Array(n),r=0;n>r;r++)i[r]=[e[r],t[e[r]]];return i},m.invert=function(t){for(var e={},n=m.keys(t),i=0,r=n.length;r>i;i++)e[t[n[i]]]=n[i];return e},m.functions=m.methods=function(t){var e=[];for(var n in t)m.isFunction(t[n])&&e.push(n);return e.sort()},m.extend=function(t){if(!m.isObject(t))return t;for(var e,n,i=1,r=arguments.length;r>i;i++){e=arguments[i];
	
	for(n in e)p.call(e,n)&&(t[n]=e[n])}return t},m.pick=function(t,e,n){var i,r={};if(null==t)return r;if(m.isFunction(e)){e=v(e,n);for(i in t){var o=t[i];e(o,i,t)&&(r[i]=o)}}else{var s=c.apply([],u.call(arguments,1));t=new Object(t);for(var a=0,l=s.length;l>a;a++)i=s[a],i in t&&(r[i]=t[i])}return r},m.omit=function(t,e,n){if(m.isFunction(e))e=m.negate(e);else{var i=m.map(c.apply([],u.call(arguments,1)),String);e=function(t,e){return!m.contains(i,e)}}return m.pick(t,e,n)},m.defaults=function(t){if(!m.isObject(t))return t;for(var e=1,n=arguments.length;n>e;e++){var i=arguments[e];for(var r in i)void 0===t[r]&&(t[r]=i[r])}return t},m.clone=function(t){return m.isObject(t)?m.isArray(t)?t.slice():m.extend({},t):t},m.tap=function(t,e){return e(t),t};var T=function(t,e,n,i){if(t===e)return 0!==t||1/t===1/e;if(null==t||null==e)return t===e;t instanceof m&&(t=t._wrapped),e instanceof m&&(e=e._wrapped);var r=h.call(t);if(r!==h.call(e))return!1;switch(r){case"[object RegExp]":case"[object String]":return""+t==""+e;case"[object Number]":return+t!==+t?+e!==+e:0===+t?1/+t===1/e:+t===+e;case"[object Date]":case"[object Boolean]":return+t===+e}if("object"!=typeof t||"object"!=typeof e)return!1;for(var o=n.length;o--;)if(n[o]===t)return i[o]===e;var s=t.constructor,a=e.constructor;if(s!==a&&"constructor"in t&&"constructor"in e&&!(m.isFunction(s)&&s instanceof s&&m.isFunction(a)&&a instanceof a))return!1;n.push(t),i.push(e);var l,u;if("[object Array]"===r){if(l=t.length,u=l===e.length)for(;l--&&(u=T(t[l],e[l],n,i)););}else{var c,p=m.keys(t);if(l=p.length,u=m.keys(e).length===l)for(;l--&&(c=p[l],u=m.has(e,c)&&T(t[c],e[c],n,i)););}return n.pop(),i.pop(),u};m.isEqual=function(t,e){return T(t,e,[],[])},m.isEmpty=function(t){if(null==t)return!0;if(m.isArray(t)||m.isString(t)||m.isArguments(t))return 0===t.length;for(var e in t)if(m.has(t,e))return!1;return!0},m.isElement=function(t){return!(!t||1!==t.nodeType)},m.isArray=d||function(t){return"[object Array]"===h.call(t)},m.isObject=function(t){var e=typeof t;return"function"===e||"object"===e&&!!t},m.each(["Arguments","Function","String","Number","Date","RegExp"],function(t){m["is"+t]=function(e){return h.call(e)==="[object "+t+"]"}}),m.isArguments(arguments)||(m.isArguments=function(t){return m.has(t,"callee")}),"function"!=typeof/./&&(m.isFunction=function(t){return"function"==typeof t||!1}),m.isFinite=function(t){return isFinite(t)&&!isNaN(parseFloat(t))},m.isNaN=function(t){return m.isNumber(t)&&t!==+t},m.isBoolean=function(t){return t===!0||t===!1||"[object Boolean]"===h.call(t)},m.isNull=function(t){return null===t},m.isUndefined=function(t){return void 0===t},m.has=function(t,e){return null!=t&&p.call(t,e)},m.noConflict=function(){return e._=r,this},m.identity=function(t){return t},m.constant=function(t){return function(){return t}},m.noop=function(){},m.property=function(t){return function(e){return e[t]}},m.matches=function(t){var e=m.pairs(t),n=e.length;return function(t){if(null==t)return!n;t=new Object(t);for(var i=0;n>i;i++){var r=e[i],o=r[0];if(r[1]!==t[o]||!(o in t))return!1}return!0}},m.times=function(t,e,n){var i=Array(Math.max(0,t));e=v(e,n,1);for(var r=0;t>r;r++)i[r]=e(r);return i},m.random=function(t,e){return null==e&&(e=t,t=0),t+Math.floor(Math.random()*(e-t+1))},m.now=Date.now||function(){return(new Date).getTime()};var C={"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#x27;","`":"&#x60;"},k=m.invert(C),E=function(t){var e=function(e){return t[e]},n="(?:"+m.keys(t).join("|")+")",i=RegExp(n),r=RegExp(n,"g");return function(t){return t=null==t?"":""+t,i.test(t)?t.replace(r,e):t}};m.escape=E(C),m.unescape=E(k),m.result=function(t,e){if(null==t)return void 0;var n=t[e];return m.isFunction(n)?t[e]():n};var A=0;m.uniqueId=function(t){var e=++A+"";return t?t+e:e},m.templateSettings={evaluate:/<%([\s\S]+?)%>/g,interpolate:/<%=([\s\S]+?)%>/g,escape:/<%-([\s\S]+?)%>/g};var $=/(.)^/,S={"'":"'","\\":"\\","\r":"r","\n":"n","\u2028":"u2028","\u2029":"u2029"},N=/\\|'|\r|\n|\u2028|\u2029/g,j=function(t){return"\\"+S[t]};m.template=function(t,e,n){!e&&n&&(e=n),e=m.defaults({},e,m.templateSettings);var i=RegExp([(e.escape||$).source,(e.interpolate||$).source,(e.evaluate||$).source].join("|")+"|$","g"),r=0,o="__p+='";t.replace(i,function(e,n,i,s,a){return o+=t.slice(r,a).replace(N,j),r=a+e.length,n?o+="'+\n((__t=("+n+"))==null?'':_.escape(__t))+\n'":i?o+="'+\n((__t=("+i+"))==null?'':__t)+\n'":s&&(o+="';\n"+s+"\n__p+='"),e}),o+="';\n",e.variable||(o="with(obj||{}){\n"+o+"}\n"),o="var __t,__p='',__j=Array.prototype.join,print=function(){__p+=__j.call(arguments,'');};\n"+o+"return __p;\n";try{var s=new Function(e.variable||"obj","_",o)}catch(a){throw a.source=o,a}var l=function(t){return s.call(this,t,m)},u=e.variable||"obj";return l.source="function("+u+"){\n"+o+"}",l},m.chain=function(t){var e=m(t);return e._chain=!0,e};var D=function(t){return this._chain?m(t).chain():t};m.mixin=function(t){m.each(m.functions(t),function(e){var n=m[e]=t[e];m.prototype[e]=function(){var t=[this._wrapped];return l.apply(t,arguments),D.call(this,n.apply(m,t))}})},m.mixin(m),m.each(["pop","push","reverse","shift","sort","splice","unshift"],function(t){var e=o[t];m.prototype[t]=function(){var n=this._wrapped;return e.apply(n,arguments),"shift"!==t&&"splice"!==t||0!==n.length||delete n[0],D.call(this,n)}}),m.each(["concat","join","slice"],function(t){var e=o[t];m.prototype[t]=function(){return D.call(this,e.apply(this._wrapped,arguments))}}),m.prototype.value=function(){return this._wrapped},"function"==typeof t&&t.amd&&t("underscore",[],function(){return m})}).call(this)},{}],4:[function(t,e,n){if("undefined"==typeof jQuery)throw new Error("Bootstrap's JavaScript requires jQuery");+function(t){var e=t.fn.jquery.split(" ")[0].split(".");if(e[0]<2&&e[1]<9||1==e[0]&&9==e[1]&&e[2]<1)throw new Error("Bootstrap's JavaScript requires jQuery version 1.9.1 or higher")}(jQuery),+function(t){"use strict";function e(){var t=document.createElement("bootstrap"),e={WebkitTransition:"webkitTransitionEnd",MozTransition:"transitionend",OTransition:"oTransitionEnd otransitionend",transition:"transitionend"};for(var n in e)if(void 0!==t.style[n])return{end:e[n]};return!1}t.fn.emulateTransitionEnd=function(e){var n=!1,i=this;t(this).one("bsTransitionEnd",function(){n=!0});var r=function(){n||t(i).trigger(t.support.transition.end)};return setTimeout(r,e),this},t(function(){t.support.transition=e(),t.support.transition&&(t.event.special.bsTransitionEnd={bindType:t.support.transition.end,delegateType:t.support.transition.end,handle:function(e){return t(e.target).is(this)?e.handleObj.handler.apply(this,arguments):void 0}})})}(jQuery),+function(t){"use strict";function e(e){return this.each(function(){var n=t(this),r=n.data("bs.alert");r||n.data("bs.alert",r=new i(this)),"string"==typeof e&&r[e].call(n)})}var n='[data-dismiss="alert"]',i=function(e){t(e).on("click",n,this.close)};i.VERSION="3.3.0",i.TRANSITION_DURATION=150,i.prototype.close=function(e){function n(){s.detach().trigger("closed.bs.alert").remove()}var r=t(this),o=r.attr("data-target");o||(o=r.attr("href"),o=o&&o.replace(/.*(?=#[^\s]*$)/,""));var s=t(o);e&&e.preventDefault(),s.length||(s=r.closest(".alert")),s.trigger(e=t.Event("close.bs.alert")),e.isDefaultPrevented()||(s.removeClass("in"),t.support.transition&&s.hasClass("fade")?s.one("bsTransitionEnd",n).emulateTransitionEnd(i.TRANSITION_DURATION):n())};var r=t.fn.alert;t.fn.alert=e,t.fn.alert.Constructor=i,t.fn.alert.noConflict=function(){return t.fn.alert=r,this},t(document).on("click.bs.alert.data-api",n,i.prototype.close)}(jQuery),+function(t){"use strict";function e(e){return this.each(function(){var i=t(this),r=i.data("bs.button"),o="object"==typeof e&&e;r||i.data("bs.button",r=new n(this,o)),"toggle"==e?r.toggle():e&&r.setState(e)})}var n=function(e,i){this.$element=t(e),this.options=t.extend({},n.DEFAULTS,i),this.isLoading=!1};n.VERSION="3.3.0",n.DEFAULTS={loadingText:"loading..."},n.prototype.setState=function(e){var n="disabled",i=this.$element,r=i.is("input")?"val":"html",o=i.data();e+="Text",null==o.resetText&&i.data("resetText",i[r]()),setTimeout(t.proxy(function(){i[r](null==o[e]?this.options[e]:o[e]),"loadingText"==e?(this.isLoading=!0,i.addClass(n).attr(n,n)):this.isLoading&&(this.isLoading=!1,i.removeClass(n).removeAttr(n))},this),0)},n.prototype.toggle=function(){var t=!0,e=this.$element.closest('[data-toggle="buttons"]');if(e.length){var n=this.$element.find("input");"radio"==n.prop("type")&&(n.prop("checked")&&this.$element.hasClass("active")?t=!1:e.find(".active").removeClass("active")),t&&n.prop("checked",!this.$element.hasClass("active")).trigger("change")}else this.$element.attr("aria-pressed",!this.$element.hasClass("active"));t&&this.$element.toggleClass("active")};var i=t.fn.button;t.fn.button=e,t.fn.button.Constructor=n,t.fn.button.noConflict=function(){return t.fn.button=i,this},t(document).on("click.bs.button.data-api",'[data-toggle^="button"]',function(n){var i=t(n.target);i.hasClass("btn")||(i=i.closest(".btn")),e.call(i,"toggle"),n.preventDefault()}).on("focus.bs.button.data-api blur.bs.button.data-api",'[data-toggle^="button"]',function(e){t(e.target).closest(".btn").toggleClass("focus","focus"==e.type)})}(jQuery),+function(t){"use strict";function e(e){return this.each(function(){var i=t(this),r=i.data("bs.carousel"),o=t.extend({},n.DEFAULTS,i.data(),"object"==typeof e&&e),s="string"==typeof e?e:o.slide;r||i.data("bs.carousel",r=new n(this,o)),"number"==typeof e?r.to(e):s?r[s]():o.interval&&r.pause().cycle()})}var n=function(e,n){this.$element=t(e),this.$indicators=this.$element.find(".carousel-indicators"),this.options=n,this.paused=this.sliding=this.interval=this.$active=this.$items=null,this.options.keyboard&&this.$element.on("keydown.bs.carousel",t.proxy(this.keydown,this)),"hover"==this.options.pause&&!("ontouchstart"in document.documentElement)&&this.$element.on("mouseenter.bs.carousel",t.proxy(this.pause,this)).on("mouseleave.bs.carousel",t.proxy(this.cycle,this))};n.VERSION="3.3.0",n.TRANSITION_DURATION=600,n.DEFAULTS={interval:5e3,pause:"hover",wrap:!0,keyboard:!0},n.prototype.keydown=function(t){switch(t.which){case 37:this.prev();break;case 39:this.next();break;default:return}t.preventDefault()},n.prototype.cycle=function(e){return e||(this.paused=!1),this.interval&&clearInterval(this.interval),this.options.interval&&!this.paused&&(this.interval=setInterval(t.proxy(this.next,this),this.options.interval)),this},n.prototype.getItemIndex=function(t){return this.$items=t.parent().children(".item"),this.$items.index(t||this.$active)},n.prototype.getItemForDirection=function(t,e){var n="prev"==t?-1:1,i=this.getItemIndex(e),r=(i+n)%this.$items.length;return this.$items.eq(r)},n.prototype.to=function(t){var e=this,n=this.getItemIndex(this.$active=this.$element.find(".item.active"));return t>this.$items.length-1||0>t?void 0:this.sliding?this.$element.one("slid.bs.carousel",function(){e.to(t)}):n==t?this.pause().cycle():this.slide(t>n?"next":"prev",this.$items.eq(t))},n.prototype.pause=function(e){return e||(this.paused=!0),this.$element.find(".next, .prev").length&&t.support.transition&&(this.$element.trigger(t.support.transition.end),this.cycle(!0)),this.interval=clearInterval(this.interval),this},n.prototype.next=function(){return this.sliding?void 0:this.slide("next")},n.prototype.prev=function(){return this.sliding?void 0:this.slide("prev")},n.prototype.slide=function(e,i){var r=this.$element.find(".item.active"),o=i||this.getItemForDirection(e,r),s=this.interval,a="next"==e?"left":"right",l="next"==e?"first":"last",u=this;if(!o.length){if(!this.options.wrap)return;o=this.$element.find(".item")[l]()}if(o.hasClass("active"))return this.sliding=!1;var c=o[0],h=t.Event("slide.bs.carousel",{relatedTarget:c,direction:a});if(this.$element.trigger(h),!h.isDefaultPrevented()){if(this.sliding=!0,s&&this.pause(),this.$indicators.length){this.$indicators.find(".active").removeClass("active");var p=t(this.$indicators.children()[this.getItemIndex(o)]);p&&p.addClass("active")}var d=t.Event("slid.bs.carousel",{relatedTarget:c,direction:a});return t.support.transition&&this.$element.hasClass("slide")?(o.addClass(e),o[0].offsetWidth,r.addClass(a),o.addClass(a),r.one("bsTransitionEnd",function(){o.removeClass([e,a].join(" ")).addClass("active"),r.removeClass(["active",a].join(" ")),u.sliding=!1,setTimeout(function(){u.$element.trigger(d)},0)}).emulateTransitionEnd(n.TRANSITION_DURATION)):(r.removeClass("active"),o.addClass("active"),this.sliding=!1,this.$element.trigger(d)),s&&this.cycle(),this}};var i=t.fn.carousel;t.fn.carousel=e,t.fn.carousel.Constructor=n,t.fn.carousel.noConflict=function(){return t.fn.carousel=i,this};var r=function(n){var i,r=t(this),o=t(r.attr("data-target")||(i=r.attr("href"))&&i.replace(/.*(?=#[^\s]+$)/,""));if(o.hasClass("carousel")){var s=t.extend({},o.data(),r.data()),a=r.attr("data-slide-to");a&&(s.interval=!1),e.call(o,s),a&&o.data("bs.carousel").to(a),n.preventDefault()}};t(document).on("click.bs.carousel.data-api","[data-slide]",r).on("click.bs.carousel.data-api","[data-slide-to]",r),t(window).on("load",function(){t('[data-ride="carousel"]').each(function(){var n=t(this);e.call(n,n.data())})})}(jQuery),+function(t){"use strict";function e(e){var n,i=e.attr("data-target")||(n=e.attr("href"))&&n.replace(/.*(?=#[^\s]+$)/,"");return t(i)}function n(e){return this.each(function(){var n=t(this),r=n.data("bs.collapse"),o=t.extend({},i.DEFAULTS,n.data(),"object"==typeof e&&e);!r&&o.toggle&&"show"==e&&(o.toggle=!1),r||n.data("bs.collapse",r=new i(this,o)),"string"==typeof e&&r[e]()})}var i=function(e,n){this.$element=t(e),this.options=t.extend({},i.DEFAULTS,n),this.$trigger=t(this.options.trigger).filter('[href="#'+e.id+'"], [data-target="#'+e.id+'"]'),this.transitioning=null,this.options.parent?this.$parent=this.getParent():this.addAriaAndCollapsedClass(this.$element,this.$trigger),this.options.toggle&&this.toggle()};i.VERSION="3.3.0",i.TRANSITION_DURATION=350,i.DEFAULTS={toggle:!0,trigger:'[data-toggle="collapse"]'},i.prototype.dimension=function(){var t=this.$element.hasClass("width");return t?"width":"height"},i.prototype.show=function(){if(!this.transitioning&&!this.$element.hasClass("in")){var e,r=this.$parent&&this.$parent.find("> .panel").children(".in, .collapsing");if(!(r&&r.length&&(e=r.data("bs.collapse"),e&&e.transitioning))){var o=t.Event("show.bs.collapse");if(this.$element.trigger(o),!o.isDefaultPrevented()){r&&r.length&&(n.call(r,"hide"),e||r.data("bs.collapse",null));var s=this.dimension();this.$element.removeClass("collapse").addClass("collapsing")[s](0).attr("aria-expanded",!0),this.$trigger.removeClass("collapsed").attr("aria-expanded",!0),this.transitioning=1;var a=function(){this.$element.removeClass("collapsing").addClass("collapse in")[s](""),this.transitioning=0,this.$element.trigger("shown.bs.collapse")};if(!t.support.transition)return a.call(this);var l=t.camelCase(["scroll",s].join("-"));this.$element.one("bsTransitionEnd",t.proxy(a,this)).emulateTransitionEnd(i.TRANSITION_DURATION)[s](this.$element[0][l])}}}},i.prototype.hide=function(){if(!this.transitioning&&this.$element.hasClass("in")){var e=t.Event("hide.bs.collapse");if(this.$element.trigger(e),!e.isDefaultPrevented()){var n=this.dimension();this.$element[n](this.$element[n]())[0].offsetHeight,this.$element.addClass("collapsing").removeClass("collapse in").attr("aria-expanded",!1),this.$trigger.addClass("collapsed").attr("aria-expanded",!1),this.transitioning=1;var r=function(){this.transitioning=0,this.$element.removeClass("collapsing").addClass("collapse").trigger("hidden.bs.collapse")};return t.support.transition?void this.$element[n](0).one("bsTransitionEnd",t.proxy(r,this)).emulateTransitionEnd(i.TRANSITION_DURATION):r.call(this)}}},i.prototype.toggle=function(){this[this.$element.hasClass("in")?"hide":"show"]()},i.prototype.getParent=function(){return t(this.options.parent).find('[data-toggle="collapse"][data-parent="'+this.options.parent+'"]').each(t.proxy(function(n,i){var r=t(i);this.addAriaAndCollapsedClass(e(r),r)},this)).end()},i.prototype.addAriaAndCollapsedClass=function(t,e){var n=t.hasClass("in");t.attr("aria-expanded",n),e.toggleClass("collapsed",!n).attr("aria-expanded",n)};var r=t.fn.collapse;t.fn.collapse=n,t.fn.collapse.Constructor=i,t.fn.collapse.noConflict=function(){return t.fn.collapse=r,this},t(document).on("click.bs.collapse.data-api",'[data-toggle="collapse"]',function(i){var r=t(this);r.attr("data-target")||i.preventDefault();var o=e(r),s=o.data("bs.collapse"),a=s?"toggle":t.extend({},r.data(),{trigger:this});n.call(o,a)})}(jQuery),+function(t){"use strict";function e(e){e&&3===e.which||(t(r).remove(),t(o).each(function(){var i=t(this),r=n(i),o={relatedTarget:this};r.hasClass("open")&&(r.trigger(e=t.Event("hide.bs.dropdown",o)),e.isDefaultPrevented()||(i.attr("aria-expanded","false"),r.removeClass("open").trigger("hidden.bs.dropdown",o)))}))}function n(e){var n=e.attr("data-target");n||(n=e.attr("href"),n=n&&/#[A-Za-z]/.test(n)&&n.replace(/.*(?=#[^\s]*$)/,""));var i=n&&t(n);return i&&i.length?i:e.parent()}function i(e){return this.each(function(){var n=t(this),i=n.data("bs.dropdown");i||n.data("bs.dropdown",i=new s(this)),"string"==typeof e&&i[e].call(n)})}var r=".dropdown-backdrop",o='[data-toggle="dropdown"]',s=function(e){t(e).on("click.bs.dropdown",this.toggle)};s.VERSION="3.3.0",s.prototype.toggle=function(i){var r=t(this);if(!r.is(".disabled, :disabled")){var o=n(r),s=o.hasClass("open");if(e(),!s){"ontouchstart"in document.documentElement&&!o.closest(".navbar-nav").length&&t('<div class="dropdown-backdrop"/>').insertAfter(t(this)).on("click",e);var a={relatedTarget:this};if(o.trigger(i=t.Event("show.bs.dropdown",a)),i.isDefaultPrevented())return;r.trigger("focus").attr("aria-expanded","true"),o.toggleClass("open").trigger("shown.bs.dropdown",a)}return!1}},s.prototype.keydown=function(e){if(/(38|40|27|32)/.test(e.which)){var i=t(this);if(e.preventDefault(),e.stopPropagation(),!i.is(".disabled, :disabled")){var r=n(i),s=r.hasClass("open");if(!s&&27!=e.which||s&&27==e.which)return 27==e.which&&r.find(o).trigger("focus"),i.trigger("click");var a=" li:not(.divider):visible a",l=r.find('[role="menu"]'+a+', [role="listbox"]'+a);if(l.length){var u=l.index(e.target);38==e.which&&u>0&&u--,40==e.which&&u<l.length-1&&u++,~u||(u=0),l.eq(u).trigger("focus")}}}};var a=t.fn.dropdown;t.fn.dropdown=i,t.fn.dropdown.Constructor=s,t.fn.dropdown.noConflict=function(){return t.fn.dropdown=a,this},t(document).on("click.bs.dropdown.data-api",e).on("click.bs.dropdown.data-api",".dropdown form",function(t){t.stopPropagation()}).on("click.bs.dropdown.data-api",o,s.prototype.toggle).on("keydown.bs.dropdown.data-api",o,s.prototype.keydown).on("keydown.bs.dropdown.data-api",'[role="menu"]',s.prototype.keydown).on("keydown.bs.dropdown.data-api",'[role="listbox"]',s.prototype.keydown)}(jQuery),+function(t){"use strict";function e(e,i){return this.each(function(){var r=t(this),o=r.data("bs.modal"),s=t.extend({},n.DEFAULTS,r.data(),"object"==typeof e&&e);o||r.data("bs.modal",o=new n(this,s)),"string"==typeof e?o[e](i):s.show&&o.show(i)})}var n=function(e,n){this.options=n,this.$body=t(document.body),this.$element=t(e),this.$backdrop=this.isShown=null,this.scrollbarWidth=0,this.options.remote&&this.$element.find(".modal-content").load(this.options.remote,t.proxy(function(){this.$element.trigger("loaded.bs.modal")},this))};n.VERSION="3.3.0",n.TRANSITION_DURATION=300,n.BACKDROP_TRANSITION_DURATION=150,n.DEFAULTS={backdrop:!0,keyboard:!0,show:!0},n.prototype.toggle=function(t){return this.isShown?this.hide():this.show(t)},n.prototype.show=function(e){var i=this,r=t.Event("show.bs.modal",{relatedTarget:e});this.$element.trigger(r),this.isShown||r.isDefaultPrevented()||(this.isShown=!0,this.checkScrollbar(),this.$body.addClass("modal-open"),this.setScrollbar(),this.escape(),this.$element.on("click.dismiss.bs.modal",'[data-dismiss="modal"]',t.proxy(this.hide,this)),this.backdrop(function(){var r=t.support.transition&&i.$element.hasClass("fade");i.$element.parent().length||i.$element.appendTo(i.$body),i.$element.show().scrollTop(0),r&&i.$element[0].offsetWidth,i.$element.addClass("in").attr("aria-hidden",!1),i.enforceFocus();var o=t.Event("shown.bs.modal",{relatedTarget:e});r?i.$element.find(".modal-dialog").one("bsTransitionEnd",function(){i.$element.trigger("focus").trigger(o)}).emulateTransitionEnd(n.TRANSITION_DURATION):i.$element.trigger("focus").trigger(o)}))},n.prototype.hide=function(e){e&&e.preventDefault(),e=t.Event("hide.bs.modal"),this.$element.trigger(e),this.isShown&&!e.isDefaultPrevented()&&(this.isShown=!1,this.escape(),t(document).off("focusin.bs.modal"),this.$element.removeClass("in").attr("aria-hidden",!0).off("click.dismiss.bs.modal"),t.support.transition&&this.$element.hasClass("fade")?this.$element.one("bsTransitionEnd",t.proxy(this.hideModal,this)).emulateTransitionEnd(n.TRANSITION_DURATION):this.hideModal())},n.prototype.enforceFocus=function(){t(document).off("focusin.bs.modal").on("focusin.bs.modal",t.proxy(function(t){this.$element[0]===t.target||this.$element.has(t.target).length||this.$element.trigger("focus")},this))},n.prototype.escape=function(){this.isShown&&this.options.keyboard?this.$element.on("keydown.dismiss.bs.modal",t.proxy(function(t){27==t.which&&this.hide()},this)):this.isShown||this.$element.off("keydown.dismiss.bs.modal")},n.prototype.hideModal=function(){var t=this;this.$element.hide(),this.backdrop(function(){t.$body.removeClass("modal-open"),t.resetScrollbar(),t.$element.trigger("hidden.bs.modal")})},n.prototype.removeBackdrop=function(){this.$backdrop&&this.$backdrop.remove(),this.$backdrop=null},n.prototype.backdrop=function(e){var i=this,r=this.$element.hasClass("fade")?"fade":"";if(this.isShown&&this.options.backdrop){var o=t.support.transition&&r;if(this.$backdrop=t('<div class="modal-backdrop '+r+'" />').prependTo(this.$element).on("click.dismiss.bs.modal",t.proxy(function(t){t.target===t.currentTarget&&("static"==this.options.backdrop?this.$element[0].focus.call(this.$element[0]):this.hide.call(this))},this)),o&&this.$backdrop[0].offsetWidth,this.$backdrop.addClass("in"),!e)return;o?this.$backdrop.one("bsTransitionEnd",e).emulateTransitionEnd(n.BACKDROP_TRANSITION_DURATION):e()}else if(!this.isShown&&this.$backdrop){this.$backdrop.removeClass("in");var s=function(){i.removeBackdrop(),e&&e()};t.support.transition&&this.$element.hasClass("fade")?this.$backdrop.one("bsTransitionEnd",s).emulateTransitionEnd(n.BACKDROP_TRANSITION_DURATION):s()}else e&&e()},n.prototype.checkScrollbar=function(){this.scrollbarWidth=this.measureScrollbar()},n.prototype.setScrollbar=function(){var t=parseInt(this.$body.css("padding-right")||0,10);this.scrollbarWidth&&this.$body.css("padding-right",t+this.scrollbarWidth)},n.prototype.resetScrollbar=function(){this.$body.css("padding-right","")},n.prototype.measureScrollbar=function(){if(document.body.clientWidth>=window.innerWidth)return 0;var t=document.createElement("div");t.className="modal-scrollbar-measure",this.$body.append(t);var e=t.offsetWidth-t.clientWidth;return this.$body[0].removeChild(t),e};var i=t.fn.modal;t.fn.modal=e,t.fn.modal.Constructor=n,t.fn.modal.noConflict=function(){return t.fn.modal=i,this},t(document).on("click.bs.modal.data-api",'[data-toggle="modal"]',function(n){var i=t(this),r=i.attr("href"),o=t(i.attr("data-target")||r&&r.replace(/.*(?=#[^\s]+$)/,"")),s=o.data("bs.modal")?"toggle":t.extend({remote:!/#/.test(r)&&r},o.data(),i.data());i.is("a")&&n.preventDefault(),o.one("show.bs.modal",function(t){t.isDefaultPrevented()||o.one("hidden.bs.modal",function(){i.is(":visible")&&i.trigger("focus")})}),e.call(o,s,this)})}(jQuery),+function(t){"use strict";function e(e){return this.each(function(){var i=t(this),r=i.data("bs.tooltip"),o="object"==typeof e&&e,s=o&&o.selector;(r||"destroy"!=e)&&(s?(r||i.data("bs.tooltip",r={}),r[s]||(r[s]=new n(this,o))):r||i.data("bs.tooltip",r=new n(this,o)),"string"==typeof e&&r[e]())})}var n=function(t,e){this.type=this.options=this.enabled=this.timeout=this.hoverState=this.$element=null,this.init("tooltip",t,e)};n.VERSION="3.3.0",n.TRANSITION_DURATION=150,n.DEFAULTS={animation:!0,placement:"top",selector:!1,template:'<div class="tooltip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>',trigger:"hover focus",title:"",delay:0,html:!1,container:!1,viewport:{selector:"body",padding:0}},n.prototype.init=function(e,n,i){this.enabled=!0,this.type=e,this.$element=t(n),this.options=this.getOptions(i),this.$viewport=this.options.viewport&&t(this.options.viewport.selector||this.options.viewport);for(var r=this.options.trigger.split(" "),o=r.length;o--;){var s=r[o];if("click"==s)this.$element.on("click."+this.type,this.options.selector,t.proxy(this.toggle,this));else if("manual"!=s){var a="hover"==s?"mouseenter":"focusin",l="hover"==s?"mouseleave":"focusout";this.$element.on(a+"."+this.type,this.options.selector,t.proxy(this.enter,this)),this.$element.on(l+"."+this.type,this.options.selector,t.proxy(this.leave,this))}}this.options.selector?this._options=t.extend({},this.options,{trigger:"manual",selector:""}):this.fixTitle()},n.prototype.getDefaults=function(){return n.DEFAULTS},n.prototype.getOptions=function(e){return e=t.extend({},this.getDefaults(),this.$element.data(),e),e.delay&&"number"==typeof e.delay&&(e.delay={show:e.delay,hide:e.delay}),e},n.prototype.getDelegateOptions=function(){var e={},n=this.getDefaults();return this._options&&t.each(this._options,function(t,i){n[t]!=i&&(e[t]=i)}),e},n.prototype.enter=function(e){var n=e instanceof this.constructor?e:t(e.currentTarget).data("bs."+this.type);return n&&n.$tip&&n.$tip.is(":visible")?void(n.hoverState="in"):(n||(n=new this.constructor(e.currentTarget,this.getDelegateOptions()),t(e.currentTarget).data("bs."+this.type,n)),clearTimeout(n.timeout),n.hoverState="in",n.options.delay&&n.options.delay.show?void(n.timeout=setTimeout(function(){"in"==n.hoverState&&n.show()},n.options.delay.show)):n.show())},n.prototype.leave=function(e){var n=e instanceof this.constructor?e:t(e.currentTarget).data("bs."+this.type);return n||(n=new this.constructor(e.currentTarget,this.getDelegateOptions()),t(e.currentTarget).data("bs."+this.type,n)),clearTimeout(n.timeout),n.hoverState="out",n.options.delay&&n.options.delay.hide?void(n.timeout=setTimeout(function(){"out"==n.hoverState&&n.hide()},n.options.delay.hide)):n.hide()},n.prototype.show=function(){var e=t.Event("show.bs."+this.type);if(this.hasContent()&&this.enabled){this.$element.trigger(e);var i=t.contains(this.$element[0].ownerDocument.documentElement,this.$element[0]);if(e.isDefaultPrevented()||!i)return;var r=this,o=this.tip(),s=this.getUID(this.type);this.setContent(),o.attr("id",s),this.$element.attr("aria-describedby",s),this.options.animation&&o.addClass("fade");var a="function"==typeof this.options.placement?this.options.placement.call(this,o[0],this.$element[0]):this.options.placement,l=/\s?auto?\s?/i,u=l.test(a);u&&(a=a.replace(l,"")||"top"),o.detach().css({top:0,left:0,display:"block"}).addClass(a).data("bs."+this.type,this),this.options.container?o.appendTo(this.options.container):o.insertAfter(this.$element);var c=this.getPosition(),h=o[0].offsetWidth,p=o[0].offsetHeight;if(u){var d=a,f=this.options.container?t(this.options.container):this.$element.parent(),g=this.getPosition(f);a="bottom"==a&&c.bottom+p>g.bottom?"top":"top"==a&&c.top-p<g.top?"bottom":"right"==a&&c.right+h>g.width?"left":"left"==a&&c.left-h<g.left?"right":a,o.removeClass(d).addClass(a)}var m=this.getCalculatedOffset(a,c,h,p);this.applyPlacement(m,a);var v=function(){var t=r.hoverState;r.$element.trigger("shown.bs."+r.type),r.hoverState=null,"out"==t&&r.leave(r)};t.support.transition&&this.$tip.hasClass("fade")?o.one("bsTransitionEnd",v).emulateTransitionEnd(n.TRANSITION_DURATION):v()}},n.prototype.applyPlacement=function(e,n){var i=this.tip(),r=i[0].offsetWidth,o=i[0].offsetHeight,s=parseInt(i.css("margin-top"),10),a=parseInt(i.css("margin-left"),10);isNaN(s)&&(s=0),isNaN(a)&&(a=0),e.top=e.top+s,e.left=e.left+a,t.offset.setOffset(i[0],t.extend({using:function(t){i.css({top:Math.round(t.top),left:Math.round(t.left)})}},e),0),i.addClass("in");var l=i[0].offsetWidth,u=i[0].offsetHeight;"top"==n&&u!=o&&(e.top=e.top+o-u);var c=this.getViewportAdjustedDelta(n,e,l,u);c.left?e.left+=c.left:e.top+=c.top;var h=/top|bottom/.test(n),p=h?2*c.left-r+l:2*c.top-o+u,d=h?"offsetWidth":"offsetHeight";i.offset(e),this.replaceArrow(p,i[0][d],h)},n.prototype.replaceArrow=function(t,e,n){this.arrow().css(n?"left":"top",50*(1-t/e)+"%").css(n?"top":"left","")},n.prototype.setContent=function(){var t=this.tip(),e=this.getTitle();t.find(".tooltip-inner")[this.options.html?"html":"text"](e),t.removeClass("fade in top bottom left right")},n.prototype.hide=function(e){function i(){"in"!=r.hoverState&&o.detach(),r.$element.removeAttr("aria-describedby").trigger("hidden.bs."+r.type),e&&e()}var r=this,o=this.tip(),s=t.Event("hide.bs."+this.type);return this.$element.trigger(s),s.isDefaultPrevented()?void 0:(o.removeClass("in"),t.support.transition&&this.$tip.hasClass("fade")?o.one("bsTransitionEnd",i).emulateTransitionEnd(n.TRANSITION_DURATION):i(),this.hoverState=null,this)},n.prototype.fixTitle=function(){var t=this.$element;(t.attr("title")||"string"!=typeof t.attr("data-original-title"))&&t.attr("data-original-title",t.attr("title")||"").attr("title","")},n.prototype.hasContent=function(){return this.getTitle()},n.prototype.getPosition=function(e){e=e||this.$element;var n=e[0],i="BODY"==n.tagName,r=n.getBoundingClientRect();null==r.width&&(r=t.extend({},r,{width:r.right-r.left,height:r.bottom-r.top}));var o=i?{top:0,left:0}:e.offset(),s={scroll:i?document.documentElement.scrollTop||document.body.scrollTop:e.scrollTop()},a=i?{width:t(window).width(),height:t(window).height()}:null;return t.extend({},r,s,a,o)},n.prototype.getCalculatedOffset=function(t,e,n,i){return"bottom"==t?{top:e.top+e.height,left:e.left+e.width/2-n/2}:"top"==t?{top:e.top-i,left:e.left+e.width/2-n/2}:"left"==t?{top:e.top+e.height/2-i/2,left:e.left-n}:{top:e.top+e.height/2-i/2,left:e.left+e.width}},n.prototype.getViewportAdjustedDelta=function(t,e,n,i){var r={top:0,left:0};if(!this.$viewport)return r;var o=this.options.viewport&&this.options.viewport.padding||0,s=this.getPosition(this.$viewport);if(/right|left/.test(t)){var a=e.top-o-s.scroll,l=e.top+o-s.scroll+i;a<s.top?r.top=s.top-a:l>s.top+s.height&&(r.top=s.top+s.height-l)}else{var u=e.left-o,c=e.left+o+n;u<s.left?r.left=s.left-u:c>s.width&&(r.left=s.left+s.width-c)}return r},n.prototype.getTitle=function(){var t,e=this.$element,n=this.options;return t=e.attr("data-original-title")||("function"==typeof n.title?n.title.call(e[0]):n.title)},n.prototype.getUID=function(t){do t+=~~(1e6*Math.random());while(document.getElementById(t));return t},n.prototype.tip=function(){return this.$tip=this.$tip||t(this.options.template)},n.prototype.arrow=function(){return this.$arrow=this.$arrow||this.tip().find(".tooltip-arrow")},n.prototype.enable=function(){this.enabled=!0},n.prototype.disable=function(){this.enabled=!1},n.prototype.toggleEnabled=function(){this.enabled=!this.enabled},n.prototype.toggle=function(e){var n=this;e&&(n=t(e.currentTarget).data("bs."+this.type),n||(n=new this.constructor(e.currentTarget,this.getDelegateOptions()),t(e.currentTarget).data("bs."+this.type,n))),n.tip().hasClass("in")?n.leave(n):n.enter(n)},n.prototype.destroy=function(){var t=this;clearTimeout(this.timeout),this.hide(function(){t.$element.off("."+t.type).removeData("bs."+t.type)})};var i=t.fn.tooltip;t.fn.tooltip=e,t.fn.tooltip.Constructor=n,t.fn.tooltip.noConflict=function(){return t.fn.tooltip=i,this}}(jQuery),+function(t){"use strict";function e(e){return this.each(function(){var i=t(this),r=i.data("bs.popover"),o="object"==typeof e&&e,s=o&&o.selector;(r||"destroy"!=e)&&(s?(r||i.data("bs.popover",r={}),r[s]||(r[s]=new n(this,o))):r||i.data("bs.popover",r=new n(this,o)),
	"string"==typeof e&&r[e]())})}var n=function(t,e){this.init("popover",t,e)};if(!t.fn.tooltip)throw new Error("Popover requires tooltip.js");n.VERSION="3.3.0",n.DEFAULTS=t.extend({},t.fn.tooltip.Constructor.DEFAULTS,{placement:"right",trigger:"click",content:"",template:'<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'}),n.prototype=t.extend({},t.fn.tooltip.Constructor.prototype),n.prototype.constructor=n,n.prototype.getDefaults=function(){return n.DEFAULTS},n.prototype.setContent=function(){var t=this.tip(),e=this.getTitle(),n=this.getContent();t.find(".popover-title")[this.options.html?"html":"text"](e),t.find(".popover-content").children().detach().end()[this.options.html?"string"==typeof n?"html":"append":"text"](n),t.removeClass("fade top bottom left right in"),t.find(".popover-title").html()||t.find(".popover-title").hide()},n.prototype.hasContent=function(){return this.getTitle()||this.getContent()},n.prototype.getContent=function(){var t=this.$element,e=this.options;return t.attr("data-content")||("function"==typeof e.content?e.content.call(t[0]):e.content)},n.prototype.arrow=function(){return this.$arrow=this.$arrow||this.tip().find(".arrow")},n.prototype.tip=function(){return this.$tip||(this.$tip=t(this.options.template)),this.$tip};var i=t.fn.popover;t.fn.popover=e,t.fn.popover.Constructor=n,t.fn.popover.noConflict=function(){return t.fn.popover=i,this}}(jQuery),+function(t){"use strict";function e(n,i){var r=t.proxy(this.process,this);this.$body=t("body"),this.$scrollElement=t(t(n).is("body")?window:n),this.options=t.extend({},e.DEFAULTS,i),this.selector=(this.options.target||"")+" .nav li > a",this.offsets=[],this.targets=[],this.activeTarget=null,this.scrollHeight=0,this.$scrollElement.on("scroll.bs.scrollspy",r),this.refresh(),this.process()}function n(n){return this.each(function(){var i=t(this),r=i.data("bs.scrollspy"),o="object"==typeof n&&n;r||i.data("bs.scrollspy",r=new e(this,o)),"string"==typeof n&&r[n]()})}e.VERSION="3.3.0",e.DEFAULTS={offset:10},e.prototype.getScrollHeight=function(){return this.$scrollElement[0].scrollHeight||Math.max(this.$body[0].scrollHeight,document.documentElement.scrollHeight)},e.prototype.refresh=function(){var e="offset",n=0;t.isWindow(this.$scrollElement[0])||(e="position",n=this.$scrollElement.scrollTop()),this.offsets=[],this.targets=[],this.scrollHeight=this.getScrollHeight();var i=this;this.$body.find(this.selector).map(function(){var i=t(this),r=i.data("target")||i.attr("href"),o=/^#./.test(r)&&t(r);return o&&o.length&&o.is(":visible")&&[[o[e]().top+n,r]]||null}).sort(function(t,e){return t[0]-e[0]}).each(function(){i.offsets.push(this[0]),i.targets.push(this[1])})},e.prototype.process=function(){var t,e=this.$scrollElement.scrollTop()+this.options.offset,n=this.getScrollHeight(),i=this.options.offset+n-this.$scrollElement.height(),r=this.offsets,o=this.targets,s=this.activeTarget;if(this.scrollHeight!=n&&this.refresh(),e>=i)return s!=(t=o[o.length-1])&&this.activate(t);if(s&&e<r[0])return this.activeTarget=null,this.clear();for(t=r.length;t--;)s!=o[t]&&e>=r[t]&&(!r[t+1]||e<=r[t+1])&&this.activate(o[t])},e.prototype.activate=function(e){this.activeTarget=e,this.clear();var n=this.selector+'[data-target="'+e+'"],'+this.selector+'[href="'+e+'"]',i=t(n).parents("li").addClass("active");i.parent(".dropdown-menu").length&&(i=i.closest("li.dropdown").addClass("active")),i.trigger("activate.bs.scrollspy")},e.prototype.clear=function(){t(this.selector).parentsUntil(this.options.target,".active").removeClass("active")};var i=t.fn.scrollspy;t.fn.scrollspy=n,t.fn.scrollspy.Constructor=e,t.fn.scrollspy.noConflict=function(){return t.fn.scrollspy=i,this},t(window).on("load.bs.scrollspy.data-api",function(){t('[data-spy="scroll"]').each(function(){var e=t(this);n.call(e,e.data())})})}(jQuery),+function(t){"use strict";function e(e){return this.each(function(){var i=t(this),r=i.data("bs.tab");r||i.data("bs.tab",r=new n(this)),"string"==typeof e&&r[e]()})}var n=function(e){this.element=t(e)};n.VERSION="3.3.0",n.TRANSITION_DURATION=150,n.prototype.show=function(){var e=this.element,n=e.closest("ul:not(.dropdown-menu)"),i=e.data("target");if(i||(i=e.attr("href"),i=i&&i.replace(/.*(?=#[^\s]*$)/,"")),!e.parent("li").hasClass("active")){var r=n.find(".active:last a"),o=t.Event("hide.bs.tab",{relatedTarget:e[0]}),s=t.Event("show.bs.tab",{relatedTarget:r[0]});if(r.trigger(o),e.trigger(s),!s.isDefaultPrevented()&&!o.isDefaultPrevented()){var a=t(i);this.activate(e.closest("li"),n),this.activate(a,a.parent(),function(){r.trigger({type:"hidden.bs.tab",relatedTarget:e[0]}),e.trigger({type:"shown.bs.tab",relatedTarget:r[0]})})}}},n.prototype.activate=function(e,i,r){function o(){s.removeClass("active").find("> .dropdown-menu > .active").removeClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded",!1),e.addClass("active").find('[data-toggle="tab"]').attr("aria-expanded",!0),a?(e[0].offsetWidth,e.addClass("in")):e.removeClass("fade"),e.parent(".dropdown-menu")&&e.closest("li.dropdown").addClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded",!0),r&&r()}var s=i.find("> .active"),a=r&&t.support.transition&&(s.length&&s.hasClass("fade")||!!i.find("> .fade").length);s.length&&a?s.one("bsTransitionEnd",o).emulateTransitionEnd(n.TRANSITION_DURATION):o(),s.removeClass("in")};var i=t.fn.tab;t.fn.tab=e,t.fn.tab.Constructor=n,t.fn.tab.noConflict=function(){return t.fn.tab=i,this};var r=function(n){n.preventDefault(),e.call(t(this),"show")};t(document).on("click.bs.tab.data-api",'[data-toggle="tab"]',r).on("click.bs.tab.data-api",'[data-toggle="pill"]',r)}(jQuery),+function(t){"use strict";function e(e){return this.each(function(){var i=t(this),r=i.data("bs.affix"),o="object"==typeof e&&e;r||i.data("bs.affix",r=new n(this,o)),"string"==typeof e&&r[e]()})}var n=function(e,i){this.options=t.extend({},n.DEFAULTS,i),this.$target=t(this.options.target).on("scroll.bs.affix.data-api",t.proxy(this.checkPosition,this)).on("click.bs.affix.data-api",t.proxy(this.checkPositionWithEventLoop,this)),this.$element=t(e),this.affixed=this.unpin=this.pinnedOffset=null,this.checkPosition()};n.VERSION="3.3.0",n.RESET="affix affix-top affix-bottom",n.DEFAULTS={offset:0,target:window},n.prototype.getState=function(t,e,n,i){var r=this.$target.scrollTop(),o=this.$element.offset(),s=this.$target.height();if(null!=n&&"top"==this.affixed)return n>r?"top":!1;if("bottom"==this.affixed)return null!=n?r+this.unpin<=o.top?!1:"bottom":t-i>=r+s?!1:"bottom";var a=null==this.affixed,l=a?r:o.top,u=a?s:e;return null!=n&&n>=l?"top":null!=i&&l+u>=t-i?"bottom":!1},n.prototype.getPinnedOffset=function(){if(this.pinnedOffset)return this.pinnedOffset;this.$element.removeClass(n.RESET).addClass("affix");var t=this.$target.scrollTop(),e=this.$element.offset();return this.pinnedOffset=e.top-t},n.prototype.checkPositionWithEventLoop=function(){setTimeout(t.proxy(this.checkPosition,this),1)},n.prototype.checkPosition=function(){if(this.$element.is(":visible")){var e=this.$element.height(),i=this.options.offset,r=i.top,o=i.bottom,s=t("body").height();"object"!=typeof i&&(o=r=i),"function"==typeof r&&(r=i.top(this.$element)),"function"==typeof o&&(o=i.bottom(this.$element));var a=this.getState(s,e,r,o);if(this.affixed!=a){null!=this.unpin&&this.$element.css("top","");var l="affix"+(a?"-"+a:""),u=t.Event(l+".bs.affix");if(this.$element.trigger(u),u.isDefaultPrevented())return;this.affixed=a,this.unpin="bottom"==a?this.getPinnedOffset():null,this.$element.removeClass(n.RESET).addClass(l).trigger(l.replace("affix","affixed")+".bs.affix")}"bottom"==a&&this.$element.offset({top:s-e-o})}};var i=t.fn.affix;t.fn.affix=e,t.fn.affix.Constructor=n,t.fn.affix.noConflict=function(){return t.fn.affix=i,this},t(window).on("load",function(){t('[data-spy="affix"]').each(function(){var n=t(this),i=n.data();i.offset=i.offset||{},null!=i.offsetBottom&&(i.offset.bottom=i.offsetBottom),null!=i.offsetTop&&(i.offset.top=i.offsetTop),e.call(n,i)})})}(jQuery)},{}],5:[function(e,n,i){!function(t,e){"object"==typeof n&&"object"==typeof n.exports?n.exports=t.document?e(t,!0):function(t){if(!t.document)throw new Error("jQuery requires a window with a document");return e(t)}:e(t)}("undefined"!=typeof window?window:this,function(e,n){function i(t){var e=t.length,n=tt.type(t);return"function"===n||tt.isWindow(t)?!1:1===t.nodeType&&e?!0:"array"===n||0===e||"number"==typeof e&&e>0&&e-1 in t}function r(t,e,n){if(tt.isFunction(e))return tt.grep(t,function(t,i){return!!e.call(t,i,t)!==n});if(e.nodeType)return tt.grep(t,function(t){return t===e!==n});if("string"==typeof e){if(lt.test(e))return tt.filter(e,t,n);e=tt.filter(e,t)}return tt.grep(t,function(t){return G.call(e,t)>=0!==n})}function o(t,e){for(;(t=t[e])&&1!==t.nodeType;);return t}function s(t){var e=gt[t]={};return tt.each(t.match(ft)||[],function(t,n){e[n]=!0}),e}function a(){Y.removeEventListener("DOMContentLoaded",a,!1),e.removeEventListener("load",a,!1),tt.ready()}function l(){Object.defineProperty(this.cache={},0,{get:function(){return{}}}),this.expando=tt.expando+Math.random()}function u(t,e,n){var i;if(void 0===n&&1===t.nodeType)if(i="data-"+e.replace(xt,"-$1").toLowerCase(),n=t.getAttribute(i),"string"==typeof n){try{n="true"===n?!0:"false"===n?!1:"null"===n?null:+n+""===n?+n:wt.test(n)?tt.parseJSON(n):n}catch(r){}bt.set(t,e,n)}else n=void 0;return n}function c(){return!0}function h(){return!1}function p(){try{return Y.activeElement}catch(t){}}function d(t,e){return tt.nodeName(t,"table")&&tt.nodeName(11!==e.nodeType?e:e.firstChild,"tr")?t.getElementsByTagName("tbody")[0]||t.appendChild(t.ownerDocument.createElement("tbody")):t}function f(t){return t.type=(null!==t.getAttribute("type"))+"/"+t.type,t}function g(t){var e=Ft.exec(t.type);return e?t.type=e[1]:t.removeAttribute("type"),t}function m(t,e){for(var n=0,i=t.length;i>n;n++)yt.set(t[n],"globalEval",!e||yt.get(e[n],"globalEval"))}function v(t,e){var n,i,r,o,s,a,l,u;if(1===e.nodeType){if(yt.hasData(t)&&(o=yt.access(t),s=yt.set(e,o),u=o.events)){delete s.handle,s.events={};for(r in u)for(n=0,i=u[r].length;i>n;n++)tt.event.add(e,r,u[r][n])}bt.hasData(t)&&(a=bt.access(t),l=tt.extend({},a),bt.set(e,l))}}function y(t,e){var n=t.getElementsByTagName?t.getElementsByTagName(e||"*"):t.querySelectorAll?t.querySelectorAll(e||"*"):[];return void 0===e||e&&tt.nodeName(t,e)?tt.merge([t],n):n}function b(t,e){var n=e.nodeName.toLowerCase();"input"===n&&Et.test(t.type)?e.checked=t.checked:("input"===n||"textarea"===n)&&(e.defaultValue=t.defaultValue)}function w(t,n){var i,r=tt(n.createElement(t)).appendTo(n.body),o=e.getDefaultComputedStyle&&(i=e.getDefaultComputedStyle(r[0]))?i.display:tt.css(r[0],"display");return r.detach(),o}function x(t){var e=Y,n=Mt[t];return n||(n=w(t,e),"none"!==n&&n||(Ht=(Ht||tt("<iframe frameborder='0' width='0' height='0'/>")).appendTo(e.documentElement),e=Ht[0].contentDocument,e.write(),e.close(),n=w(t,e),Ht.detach()),Mt[t]=n),n}function T(t,e,n){var i,r,o,s,a=t.style;return n=n||Wt(t),n&&(s=n.getPropertyValue(e)||n[e]),n&&(""!==s||tt.contains(t.ownerDocument,t)||(s=tt.style(t,e)),Bt.test(s)&&Ut.test(e)&&(i=a.width,r=a.minWidth,o=a.maxWidth,a.minWidth=a.maxWidth=a.width=s,s=n.width,a.width=i,a.minWidth=r,a.maxWidth=o)),void 0!==s?s+"":s}function C(t,e){return{get:function(){return t()?void delete this.get:(this.get=e).apply(this,arguments)}}}function k(t,e){if(e in t)return e;for(var n=e[0].toUpperCase()+e.slice(1),i=e,r=Jt.length;r--;)if(e=Jt[r]+n,e in t)return e;return i}function E(t,e,n){var i=Vt.exec(e);return i?Math.max(0,i[1]-(n||0))+(i[2]||"px"):e}function A(t,e,n,i,r){for(var o=n===(i?"border":"content")?4:"width"===e?1:0,s=0;4>o;o+=2)"margin"===n&&(s+=tt.css(t,n+Ct[o],!0,r)),i?("content"===n&&(s-=tt.css(t,"padding"+Ct[o],!0,r)),"margin"!==n&&(s-=tt.css(t,"border"+Ct[o]+"Width",!0,r))):(s+=tt.css(t,"padding"+Ct[o],!0,r),"padding"!==n&&(s+=tt.css(t,"border"+Ct[o]+"Width",!0,r)));return s}function $(t,e,n){var i=!0,r="width"===e?t.offsetWidth:t.offsetHeight,o=Wt(t),s="border-box"===tt.css(t,"boxSizing",!1,o);if(0>=r||null==r){if(r=T(t,e,o),(0>r||null==r)&&(r=t.style[e]),Bt.test(r))return r;i=s&&(K.boxSizingReliable()||r===t.style[e]),r=parseFloat(r)||0}return r+A(t,e,n||(s?"border":"content"),i,o)+"px"}function S(t,e){for(var n,i,r,o=[],s=0,a=t.length;a>s;s++)i=t[s],i.style&&(o[s]=yt.get(i,"olddisplay"),n=i.style.display,e?(o[s]||"none"!==n||(i.style.display=""),""===i.style.display&&kt(i)&&(o[s]=yt.access(i,"olddisplay",x(i.nodeName)))):(r=kt(i),"none"===n&&r||yt.set(i,"olddisplay",r?n:tt.css(i,"display"))));for(s=0;a>s;s++)i=t[s],i.style&&(e&&"none"!==i.style.display&&""!==i.style.display||(i.style.display=e?o[s]||"":"none"));return t}function N(t,e,n,i,r){return new N.prototype.init(t,e,n,i,r)}function j(){return setTimeout(function(){Kt=void 0}),Kt=tt.now()}function D(t,e){var n,i=0,r={height:t};for(e=e?1:0;4>i;i+=2-e)n=Ct[i],r["margin"+n]=r["padding"+n]=t;return e&&(r.opacity=r.width=t),r}function _(t,e,n){for(var i,r=(ie[e]||[]).concat(ie["*"]),o=0,s=r.length;s>o;o++)if(i=r[o].call(n,e,t))return i}function O(t,e,n){var i,r,o,s,a,l,u,c,h=this,p={},d=t.style,f=t.nodeType&&kt(t),g=yt.get(t,"fxshow");n.queue||(a=tt._queueHooks(t,"fx"),null==a.unqueued&&(a.unqueued=0,l=a.empty.fire,a.empty.fire=function(){a.unqueued||l()}),a.unqueued++,h.always(function(){h.always(function(){a.unqueued--,tt.queue(t,"fx").length||a.empty.fire()})})),1===t.nodeType&&("height"in e||"width"in e)&&(n.overflow=[d.overflow,d.overflowX,d.overflowY],u=tt.css(t,"display"),c="none"===u?yt.get(t,"olddisplay")||x(t.nodeName):u,"inline"===c&&"none"===tt.css(t,"float")&&(d.display="inline-block")),n.overflow&&(d.overflow="hidden",h.always(function(){d.overflow=n.overflow[0],d.overflowX=n.overflow[1],d.overflowY=n.overflow[2]}));for(i in e)if(r=e[i],Zt.exec(r)){if(delete e[i],o=o||"toggle"===r,r===(f?"hide":"show")){if("show"!==r||!g||void 0===g[i])continue;f=!0}p[i]=g&&g[i]||tt.style(t,i)}else u=void 0;if(tt.isEmptyObject(p))"inline"===("none"===u?x(t.nodeName):u)&&(d.display=u);else{g?"hidden"in g&&(f=g.hidden):g=yt.access(t,"fxshow",{}),o&&(g.hidden=!f),f?tt(t).show():h.done(function(){tt(t).hide()}),h.done(function(){var e;yt.remove(t,"fxshow");for(e in p)tt.style(t,e,p[e])});for(i in p)s=_(f?g[i]:0,i,h),i in g||(g[i]=s.start,f&&(s.end=s.start,s.start="width"===i||"height"===i?1:0))}}function L(t,e){var n,i,r,o,s;for(n in t)if(i=tt.camelCase(n),r=e[i],o=t[n],tt.isArray(o)&&(r=o[1],o=t[n]=o[0]),n!==i&&(t[i]=o,delete t[n]),s=tt.cssHooks[i],s&&"expand"in s){o=s.expand(o),delete t[i];for(n in o)n in t||(t[n]=o[n],e[n]=r)}else e[i]=r}function I(t,e,n){var i,r,o=0,s=ne.length,a=tt.Deferred().always(function(){delete l.elem}),l=function(){if(r)return!1;for(var e=Kt||j(),n=Math.max(0,u.startTime+u.duration-e),i=n/u.duration||0,o=1-i,s=0,l=u.tweens.length;l>s;s++)u.tweens[s].run(o);return a.notifyWith(t,[u,o,n]),1>o&&l?n:(a.resolveWith(t,[u]),!1)},u=a.promise({elem:t,props:tt.extend({},e),opts:tt.extend(!0,{specialEasing:{}},n),originalProperties:e,originalOptions:n,startTime:Kt||j(),duration:n.duration,tweens:[],createTween:function(e,n){var i=tt.Tween(t,u.opts,e,n,u.opts.specialEasing[e]||u.opts.easing);return u.tweens.push(i),i},stop:function(e){var n=0,i=e?u.tweens.length:0;if(r)return this;for(r=!0;i>n;n++)u.tweens[n].run(1);return e?a.resolveWith(t,[u,e]):a.rejectWith(t,[u,e]),this}}),c=u.props;for(L(c,u.opts.specialEasing);s>o;o++)if(i=ne[o].call(u,t,c,u.opts))return i;return tt.map(c,_,u),tt.isFunction(u.opts.start)&&u.opts.start.call(t,u),tt.fx.timer(tt.extend(l,{elem:t,anim:u,queue:u.opts.queue})),u.progress(u.opts.progress).done(u.opts.done,u.opts.complete).fail(u.opts.fail).always(u.opts.always)}function P(t){return function(e,n){"string"!=typeof e&&(n=e,e="*");var i,r=0,o=e.toLowerCase().match(ft)||[];if(tt.isFunction(n))for(;i=o[r++];)"+"===i[0]?(i=i.slice(1)||"*",(t[i]=t[i]||[]).unshift(n)):(t[i]=t[i]||[]).push(n)}}function F(t,e,n,i){function r(a){var l;return o[a]=!0,tt.each(t[a]||[],function(t,a){var u=a(e,n,i);return"string"!=typeof u||s||o[u]?s?!(l=u):void 0:(e.dataTypes.unshift(u),r(u),!1)}),l}var o={},s=t===Te;return r(e.dataTypes[0])||!o["*"]&&r("*")}function R(t,e){var n,i,r=tt.ajaxSettings.flatOptions||{};for(n in e)void 0!==e[n]&&((r[n]?t:i||(i={}))[n]=e[n]);return i&&tt.extend(!0,t,i),t}function q(t,e,n){for(var i,r,o,s,a=t.contents,l=t.dataTypes;"*"===l[0];)l.shift(),void 0===i&&(i=t.mimeType||e.getResponseHeader("Content-Type"));if(i)for(r in a)if(a[r]&&a[r].test(i)){l.unshift(r);break}if(l[0]in n)o=l[0];else{for(r in n){if(!l[0]||t.converters[r+" "+l[0]]){o=r;break}s||(s=r)}o=o||s}return o?(o!==l[0]&&l.unshift(o),n[o]):void 0}function H(t,e,n,i){var r,o,s,a,l,u={},c=t.dataTypes.slice();if(c[1])for(s in t.converters)u[s.toLowerCase()]=t.converters[s];for(o=c.shift();o;)if(t.responseFields[o]&&(n[t.responseFields[o]]=e),!l&&i&&t.dataFilter&&(e=t.dataFilter(e,t.dataType)),l=o,o=c.shift())if("*"===o)o=l;else if("*"!==l&&l!==o){if(s=u[l+" "+o]||u["* "+o],!s)for(r in u)if(a=r.split(" "),a[1]===o&&(s=u[l+" "+a[0]]||u["* "+a[0]])){s===!0?s=u[r]:u[r]!==!0&&(o=a[0],c.unshift(a[1]));break}if(s!==!0)if(s&&t["throws"])e=s(e);else try{e=s(e)}catch(h){return{state:"parsererror",error:s?h:"No conversion from "+l+" to "+o}}}return{state:"success",data:e}}function M(t,e,n,i){var r;if(tt.isArray(e))tt.each(e,function(e,r){n||Ae.test(t)?i(t,r):M(t+"["+("object"==typeof r?e:"")+"]",r,n,i)});else if(n||"object"!==tt.type(e))i(t,e);else for(r in e)M(t+"["+r+"]",e[r],n,i)}function U(t){return tt.isWindow(t)?t:9===t.nodeType&&t.defaultView}var B=[],W=B.slice,z=B.concat,V=B.push,G=B.indexOf,X={},Q=X.toString,J=X.hasOwnProperty,K={},Y=e.document,Z="2.1.1",tt=function(t,e){return new tt.fn.init(t,e)},et=/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,nt=/^-ms-/,it=/-([\da-z])/gi,rt=function(t,e){return e.toUpperCase()};tt.fn=tt.prototype={jquery:Z,constructor:tt,selector:"",length:0,toArray:function(){return W.call(this)},get:function(t){return null!=t?0>t?this[t+this.length]:this[t]:W.call(this)},pushStack:function(t){var e=tt.merge(this.constructor(),t);return e.prevObject=this,e.context=this.context,e},each:function(t,e){return tt.each(this,t,e)},map:function(t){return this.pushStack(tt.map(this,function(e,n){return t.call(e,n,e)}))},slice:function(){return this.pushStack(W.apply(this,arguments))},first:function(){return this.eq(0)},last:function(){return this.eq(-1)},eq:function(t){var e=this.length,n=+t+(0>t?e:0);return this.pushStack(n>=0&&e>n?[this[n]]:[])},end:function(){return this.prevObject||this.constructor(null)},push:V,sort:B.sort,splice:B.splice},tt.extend=tt.fn.extend=function(){var t,e,n,i,r,o,s=arguments[0]||{},a=1,l=arguments.length,u=!1;for("boolean"==typeof s&&(u=s,s=arguments[a]||{},a++),"object"==typeof s||tt.isFunction(s)||(s={}),a===l&&(s=this,a--);l>a;a++)if(null!=(t=arguments[a]))for(e in t)n=s[e],i=t[e],s!==i&&(u&&i&&(tt.isPlainObject(i)||(r=tt.isArray(i)))?(r?(r=!1,o=n&&tt.isArray(n)?n:[]):o=n&&tt.isPlainObject(n)?n:{},s[e]=tt.extend(u,o,i)):void 0!==i&&(s[e]=i));return s},tt.extend({expando:"jQuery"+(Z+Math.random()).replace(/\D/g,""),isReady:!0,error:function(t){throw new Error(t)},noop:function(){},isFunction:function(t){return"function"===tt.type(t)},isArray:Array.isArray,isWindow:function(t){return null!=t&&t===t.window},isNumeric:function(t){return!tt.isArray(t)&&t-parseFloat(t)>=0},isPlainObject:function(t){return"object"!==tt.type(t)||t.nodeType||tt.isWindow(t)?!1:t.constructor&&!J.call(t.constructor.prototype,"isPrototypeOf")?!1:!0},isEmptyObject:function(t){var e;for(e in t)return!1;return!0},type:function(t){return null==t?t+"":"object"==typeof t||"function"==typeof t?X[Q.call(t)]||"object":typeof t},globalEval:function(t){var e,n=eval;t=tt.trim(t),t&&(1===t.indexOf("use strict")?(e=Y.createElement("script"),e.text=t,Y.head.appendChild(e).parentNode.removeChild(e)):n(t))},camelCase:function(t){return t.replace(nt,"ms-").replace(it,rt)},nodeName:function(t,e){return t.nodeName&&t.nodeName.toLowerCase()===e.toLowerCase()},each:function(t,e,n){var r,o=0,s=t.length,a=i(t);if(n){if(a)for(;s>o&&(r=e.apply(t[o],n),r!==!1);o++);else for(o in t)if(r=e.apply(t[o],n),r===!1)break}else if(a)for(;s>o&&(r=e.call(t[o],o,t[o]),r!==!1);o++);else for(o in t)if(r=e.call(t[o],o,t[o]),r===!1)break;return t},trim:function(t){return null==t?"":(t+"").replace(et,"")},makeArray:function(t,e){var n=e||[];return null!=t&&(i(Object(t))?tt.merge(n,"string"==typeof t?[t]:t):V.call(n,t)),n},inArray:function(t,e,n){return null==e?-1:G.call(e,t,n)},merge:function(t,e){for(var n=+e.length,i=0,r=t.length;n>i;i++)t[r++]=e[i];return t.length=r,t},grep:function(t,e,n){for(var i,r=[],o=0,s=t.length,a=!n;s>o;o++)i=!e(t[o],o),i!==a&&r.push(t[o]);return r},map:function(t,e,n){var r,o=0,s=t.length,a=i(t),l=[];if(a)for(;s>o;o++)r=e(t[o],o,n),null!=r&&l.push(r);else for(o in t)r=e(t[o],o,n),null!=r&&l.push(r);return z.apply([],l)},guid:1,proxy:function(t,e){var n,i,r;return"string"==typeof e&&(n=t[e],e=t,t=n),tt.isFunction(t)?(i=W.call(arguments,2),r=function(){return t.apply(e||this,i.concat(W.call(arguments)))},r.guid=t.guid=t.guid||tt.guid++,r):void 0},now:Date.now,support:K}),tt.each("Boolean Number String Function Array Date RegExp Object Error".split(" "),function(t,e){X["[object "+e+"]"]=e.toLowerCase()});var ot=function(t){function e(t,e,n,i){var r,o,s,a,l,u,h,d,f,g;if((e?e.ownerDocument||e:H)!==_&&D(e),e=e||_,n=n||[],!t||"string"!=typeof t)return n;if(1!==(a=e.nodeType)&&9!==a)return[];if(L&&!i){if(r=yt.exec(t))if(s=r[1]){if(9===a){if(o=e.getElementById(s),!o||!o.parentNode)return n;if(o.id===s)return n.push(o),n}else if(e.ownerDocument&&(o=e.ownerDocument.getElementById(s))&&R(e,o)&&o.id===s)return n.push(o),n}else{if(r[2])return Z.apply(n,e.getElementsByTagName(t)),n;if((s=r[3])&&x.getElementsByClassName&&e.getElementsByClassName)return Z.apply(n,e.getElementsByClassName(s)),n}if(x.qsa&&(!I||!I.test(t))){if(d=h=q,f=e,g=9===a&&t,1===a&&"object"!==e.nodeName.toLowerCase()){for(u=E(t),(h=e.getAttribute("id"))?d=h.replace(wt,"\\$&"):e.setAttribute("id",d),d="[id='"+d+"'] ",l=u.length;l--;)u[l]=d+p(u[l]);f=bt.test(t)&&c(e.parentNode)||e,g=u.join(",")}if(g)try{return Z.apply(n,f.querySelectorAll(g)),n}catch(m){}finally{h||e.removeAttribute("id")}}}return $(t.replace(lt,"$1"),e,n,i)}function n(){function t(n,i){return e.push(n+" ")>T.cacheLength&&delete t[e.shift()],t[n+" "]=i}var e=[];return t}function i(t){return t[q]=!0,t}function r(t){var e=_.createElement("div");try{return!!t(e)}catch(n){return!1}finally{e.parentNode&&e.parentNode.removeChild(e),e=null}}function o(t,e){for(var n=t.split("|"),i=t.length;i--;)T.attrHandle[n[i]]=e}function s(t,e){var n=e&&t,i=n&&1===t.nodeType&&1===e.nodeType&&(~e.sourceIndex||X)-(~t.sourceIndex||X);if(i)return i;if(n)for(;n=n.nextSibling;)if(n===e)return-1;return t?1:-1}function a(t){return function(e){var n=e.nodeName.toLowerCase();return"input"===n&&e.type===t}}function l(t){return function(e){var n=e.nodeName.toLowerCase();return("input"===n||"button"===n)&&e.type===t}}function u(t){return i(function(e){return e=+e,i(function(n,i){for(var r,o=t([],n.length,e),s=o.length;s--;)n[r=o[s]]&&(n[r]=!(i[r]=n[r]))})})}function c(t){return t&&typeof t.getElementsByTagName!==G&&t}function h(){}function p(t){for(var e=0,n=t.length,i="";n>e;e++)i+=t[e].value;return i}function d(t,e,n){var i=e.dir,r=n&&"parentNode"===i,o=U++;return e.first?function(e,n,o){for(;e=e[i];)if(1===e.nodeType||r)return t(e,n,o)}:function(e,n,s){var a,l,u=[M,o];if(s){for(;e=e[i];)if((1===e.nodeType||r)&&t(e,n,s))return!0}else for(;e=e[i];)if(1===e.nodeType||r){if(l=e[q]||(e[q]={}),(a=l[i])&&a[0]===M&&a[1]===o)return u[2]=a[2];if(l[i]=u,u[2]=t(e,n,s))return!0}}}function f(t){return t.length>1?function(e,n,i){for(var r=t.length;r--;)if(!t[r](e,n,i))return!1;return!0}:t[0]}function g(t,n,i){for(var r=0,o=n.length;o>r;r++)e(t,n[r],i);return i}function m(t,e,n,i,r){for(var o,s=[],a=0,l=t.length,u=null!=e;l>a;a++)(o=t[a])&&(!n||n(o,i,r))&&(s.push(o),u&&e.push(a));return s}function v(t,e,n,r,o,s){return r&&!r[q]&&(r=v(r)),o&&!o[q]&&(o=v(o,s)),i(function(i,s,a,l){var u,c,h,p=[],d=[],f=s.length,v=i||g(e||"*",a.nodeType?[a]:a,[]),y=!t||!i&&e?v:m(v,p,t,a,l),b=n?o||(i?t:f||r)?[]:s:y;if(n&&n(y,b,a,l),r)for(u=m(b,d),r(u,[],a,l),c=u.length;c--;)(h=u[c])&&(b[d[c]]=!(y[d[c]]=h));if(i){if(o||t){if(o){for(u=[],c=b.length;c--;)(h=b[c])&&u.push(y[c]=h);o(null,b=[],u,l)}for(c=b.length;c--;)(h=b[c])&&(u=o?et.call(i,h):p[c])>-1&&(i[u]=!(s[u]=h))}}else b=m(b===s?b.splice(f,b.length):b),o?o(null,s,b,l):Z.apply(s,b)})}function y(t){for(var e,n,i,r=t.length,o=T.relative[t[0].type],s=o||T.relative[" "],a=o?1:0,l=d(function(t){return t===e},s,!0),u=d(function(t){return et.call(e,t)>-1},s,!0),c=[function(t,n,i){return!o&&(i||n!==S)||((e=n).nodeType?l(t,n,i):u(t,n,i))}];r>a;a++)if(n=T.relative[t[a].type])c=[d(f(c),n)];else{if(n=T.filter[t[a].type].apply(null,t[a].matches),n[q]){for(i=++a;r>i&&!T.relative[t[i].type];i++);return v(a>1&&f(c),a>1&&p(t.slice(0,a-1).concat({value:" "===t[a-2].type?"*":""})).replace(lt,"$1"),n,i>a&&y(t.slice(a,i)),r>i&&y(t=t.slice(i)),r>i&&p(t))}c.push(n)}return f(c)}function b(t,n){var r=n.length>0,o=t.length>0,s=function(i,s,a,l,u){var c,h,p,d=0,f="0",g=i&&[],v=[],y=S,b=i||o&&T.find.TAG("*",u),w=M+=null==y?1:Math.random()||.1,x=b.length;for(u&&(S=s!==_&&s);f!==x&&null!=(c=b[f]);f++){if(o&&c){for(h=0;p=t[h++];)if(p(c,s,a)){l.push(c);break}u&&(M=w)}r&&((c=!p&&c)&&d--,i&&g.push(c))}if(d+=f,r&&f!==d){for(h=0;p=n[h++];)p(g,v,s,a);if(i){if(d>0)for(;f--;)g[f]||v[f]||(v[f]=K.call(l));v=m(v)}Z.apply(l,v),u&&!i&&v.length>0&&d+n.length>1&&e.uniqueSort(l)}return u&&(M=w,S=y),g};return r?i(s):s}var w,x,T,C,k,E,A,$,S,N,j,D,_,O,L,I,P,F,R,q="sizzle"+-new Date,H=t.document,M=0,U=0,B=n(),W=n(),z=n(),V=function(t,e){return t===e&&(j=!0),0},G="undefined",X=1<<31,Q={}.hasOwnProperty,J=[],K=J.pop,Y=J.push,Z=J.push,tt=J.slice,et=J.indexOf||function(t){for(var e=0,n=this.length;n>e;e++)if(this[e]===t)return e;return-1},nt="checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped",it="[\\x20\\t\\r\\n\\f]",rt="(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+",ot=rt.replace("w","w#"),st="\\["+it+"*("+rt+")(?:"+it+"*([*^$|!~]?=)"+it+"*(?:'((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\"|("+ot+"))|)"+it+"*\\]",at=":("+rt+")(?:\\((('((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\")|((?:\\\\.|[^\\\\()[\\]]|"+st+")*)|.*)\\)|)",lt=new RegExp("^"+it+"+|((?:^|[^\\\\])(?:\\\\.)*)"+it+"+$","g"),ut=new RegExp("^"+it+"*,"+it+"*"),ct=new RegExp("^"+it+"*([>+~]|"+it+")"+it+"*"),ht=new RegExp("="+it+"*([^\\]'\"]*?)"+it+"*\\]","g"),pt=new RegExp(at),dt=new RegExp("^"+ot+"$"),ft={ID:new RegExp("^#("+rt+")"),CLASS:new RegExp("^\\.("+rt+")"),TAG:new RegExp("^("+rt.replace("w","w*")+")"),ATTR:new RegExp("^"+st),PSEUDO:new RegExp("^"+at),CHILD:new RegExp("^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\("+it+"*(even|odd|(([+-]|)(\\d*)n|)"+it+"*(?:([+-]|)"+it+"*(\\d+)|))"+it+"*\\)|)","i"),bool:new RegExp("^(?:"+nt+")$","i"),needsContext:new RegExp("^"+it+"*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\("+it+"*((?:-\\d)?\\d*)"+it+"*\\)|)(?=[^-]|$)","i")},gt=/^(?:input|select|textarea|button)$/i,mt=/^h\d$/i,vt=/^[^{]+\{\s*\[native \w/,yt=/^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/,bt=/[+~]/,wt=/'|\\/g,xt=new RegExp("\\\\([\\da-f]{1,6}"+it+"?|("+it+")|.)","ig"),Tt=function(t,e,n){var i="0x"+e-65536;return i!==i||n?e:0>i?String.fromCharCode(i+65536):String.fromCharCode(i>>10|55296,1023&i|56320)};try{Z.apply(J=tt.call(H.childNodes),H.childNodes),J[H.childNodes.length].nodeType}catch(Ct){Z={apply:J.length?function(t,e){Y.apply(t,tt.call(e))}:function(t,e){for(var n=t.length,i=0;t[n++]=e[i++];);t.length=n-1}}}x=e.support={},k=e.isXML=function(t){var e=t&&(t.ownerDocument||t).documentElement;return e?"HTML"!==e.nodeName:!1},D=e.setDocument=function(t){var e,n=t?t.ownerDocument||t:H,i=n.defaultView;return n!==_&&9===n.nodeType&&n.documentElement?(_=n,O=n.documentElement,L=!k(n),i&&i!==i.top&&(i.addEventListener?i.addEventListener("unload",function(){D()},!1):i.attachEvent&&i.attachEvent("onunload",function(){D()})),x.attributes=r(function(t){return t.className="i",!t.getAttribute("className")}),x.getElementsByTagName=r(function(t){return t.appendChild(n.createComment("")),!t.getElementsByTagName("*").length}),x.getElementsByClassName=vt.test(n.getElementsByClassName)&&r(function(t){return t.innerHTML="<div class='a'></div><div class='a i'></div>",t.firstChild.className="i",2===t.getElementsByClassName("i").length}),x.getById=r(function(t){return O.appendChild(t).id=q,!n.getElementsByName||!n.getElementsByName(q).length}),x.getById?(T.find.ID=function(t,e){if(typeof e.getElementById!==G&&L){var n=e.getElementById(t);return n&&n.parentNode?[n]:[]}},T.filter.ID=function(t){var e=t.replace(xt,Tt);return function(t){return t.getAttribute("id")===e}}):(delete T.find.ID,T.filter.ID=function(t){var e=t.replace(xt,Tt);return function(t){var n=typeof t.getAttributeNode!==G&&t.getAttributeNode("id");return n&&n.value===e}}),T.find.TAG=x.getElementsByTagName?function(t,e){return typeof e.getElementsByTagName!==G?e.getElementsByTagName(t):void 0}:function(t,e){var n,i=[],r=0,o=e.getElementsByTagName(t);if("*"===t){for(;n=o[r++];)1===n.nodeType&&i.push(n);return i}return o},T.find.CLASS=x.getElementsByClassName&&function(t,e){return typeof e.getElementsByClassName!==G&&L?e.getElementsByClassName(t):void 0},P=[],I=[],(x.qsa=vt.test(n.querySelectorAll))&&(r(function(t){t.innerHTML="<select msallowclip=''><option selected=''></option></select>",t.querySelectorAll("[msallowclip^='']").length&&I.push("[*^$]="+it+"*(?:''|\"\")"),t.querySelectorAll("[selected]").length||I.push("\\["+it+"*(?:value|"+nt+")"),t.querySelectorAll(":checked").length||I.push(":checked")}),r(function(t){var e=n.createElement("input");e.setAttribute("type","hidden"),t.appendChild(e).setAttribute("name","D"),t.querySelectorAll("[name=d]").length&&I.push("name"+it+"*[*^$|!~]?="),t.querySelectorAll(":enabled").length||I.push(":enabled",":disabled"),t.querySelectorAll("*,:x"),I.push(",.*:")})),(x.matchesSelector=vt.test(F=O.matches||O.webkitMatchesSelector||O.mozMatchesSelector||O.oMatchesSelector||O.msMatchesSelector))&&r(function(t){x.disconnectedMatch=F.call(t,"div"),F.call(t,"[s!='']:x"),P.push("!=",at)}),I=I.length&&new RegExp(I.join("|")),P=P.length&&new RegExp(P.join("|")),e=vt.test(O.compareDocumentPosition),R=e||vt.test(O.contains)?function(t,e){var n=9===t.nodeType?t.documentElement:t,i=e&&e.parentNode;return t===i||!(!i||1!==i.nodeType||!(n.contains?n.contains(i):t.compareDocumentPosition&&16&t.compareDocumentPosition(i)))}:function(t,e){if(e)for(;e=e.parentNode;)if(e===t)return!0;return!1},V=e?function(t,e){if(t===e)return j=!0,0;var i=!t.compareDocumentPosition-!e.compareDocumentPosition;return i?i:(i=(t.ownerDocument||t)===(e.ownerDocument||e)?t.compareDocumentPosition(e):1,1&i||!x.sortDetached&&e.compareDocumentPosition(t)===i?t===n||t.ownerDocument===H&&R(H,t)?-1:e===n||e.ownerDocument===H&&R(H,e)?1:N?et.call(N,t)-et.call(N,e):0:4&i?-1:1)}:function(t,e){if(t===e)return j=!0,0;var i,r=0,o=t.parentNode,a=e.parentNode,l=[t],u=[e];if(!o||!a)return t===n?-1:e===n?1:o?-1:a?1:N?et.call(N,t)-et.call(N,e):0;if(o===a)return s(t,e);for(i=t;i=i.parentNode;)l.unshift(i);for(i=e;i=i.parentNode;)u.unshift(i);for(;l[r]===u[r];)r++;return r?s(l[r],u[r]):l[r]===H?-1:u[r]===H?1:0},n):_},e.matches=function(t,n){return e(t,null,null,n)},e.matchesSelector=function(t,n){if((t.ownerDocument||t)!==_&&D(t),n=n.replace(ht,"='$1']"),!(!x.matchesSelector||!L||P&&P.test(n)||I&&I.test(n)))try{var i=F.call(t,n);if(i||x.disconnectedMatch||t.document&&11!==t.document.nodeType)return i}catch(r){}return e(n,_,null,[t]).length>0},e.contains=function(t,e){return(t.ownerDocument||t)!==_&&D(t),R(t,e)},e.attr=function(t,e){(t.ownerDocument||t)!==_&&D(t);var n=T.attrHandle[e.toLowerCase()],i=n&&Q.call(T.attrHandle,e.toLowerCase())?n(t,e,!L):void 0;
	
	return void 0!==i?i:x.attributes||!L?t.getAttribute(e):(i=t.getAttributeNode(e))&&i.specified?i.value:null},e.error=function(t){throw new Error("Syntax error, unrecognized expression: "+t)},e.uniqueSort=function(t){var e,n=[],i=0,r=0;if(j=!x.detectDuplicates,N=!x.sortStable&&t.slice(0),t.sort(V),j){for(;e=t[r++];)e===t[r]&&(i=n.push(r));for(;i--;)t.splice(n[i],1)}return N=null,t},C=e.getText=function(t){var e,n="",i=0,r=t.nodeType;if(r){if(1===r||9===r||11===r){if("string"==typeof t.textContent)return t.textContent;for(t=t.firstChild;t;t=t.nextSibling)n+=C(t)}else if(3===r||4===r)return t.nodeValue}else for(;e=t[i++];)n+=C(e);return n},T=e.selectors={cacheLength:50,createPseudo:i,match:ft,attrHandle:{},find:{},relative:{">":{dir:"parentNode",first:!0}," ":{dir:"parentNode"},"+":{dir:"previousSibling",first:!0},"~":{dir:"previousSibling"}},preFilter:{ATTR:function(t){return t[1]=t[1].replace(xt,Tt),t[3]=(t[3]||t[4]||t[5]||"").replace(xt,Tt),"~="===t[2]&&(t[3]=" "+t[3]+" "),t.slice(0,4)},CHILD:function(t){return t[1]=t[1].toLowerCase(),"nth"===t[1].slice(0,3)?(t[3]||e.error(t[0]),t[4]=+(t[4]?t[5]+(t[6]||1):2*("even"===t[3]||"odd"===t[3])),t[5]=+(t[7]+t[8]||"odd"===t[3])):t[3]&&e.error(t[0]),t},PSEUDO:function(t){var e,n=!t[6]&&t[2];return ft.CHILD.test(t[0])?null:(t[3]?t[2]=t[4]||t[5]||"":n&&pt.test(n)&&(e=E(n,!0))&&(e=n.indexOf(")",n.length-e)-n.length)&&(t[0]=t[0].slice(0,e),t[2]=n.slice(0,e)),t.slice(0,3))}},filter:{TAG:function(t){var e=t.replace(xt,Tt).toLowerCase();return"*"===t?function(){return!0}:function(t){return t.nodeName&&t.nodeName.toLowerCase()===e}},CLASS:function(t){var e=B[t+" "];return e||(e=new RegExp("(^|"+it+")"+t+"("+it+"|$)"))&&B(t,function(t){return e.test("string"==typeof t.className&&t.className||typeof t.getAttribute!==G&&t.getAttribute("class")||"")})},ATTR:function(t,n,i){return function(r){var o=e.attr(r,t);return null==o?"!="===n:n?(o+="","="===n?o===i:"!="===n?o!==i:"^="===n?i&&0===o.indexOf(i):"*="===n?i&&o.indexOf(i)>-1:"$="===n?i&&o.slice(-i.length)===i:"~="===n?(" "+o+" ").indexOf(i)>-1:"|="===n?o===i||o.slice(0,i.length+1)===i+"-":!1):!0}},CHILD:function(t,e,n,i,r){var o="nth"!==t.slice(0,3),s="last"!==t.slice(-4),a="of-type"===e;return 1===i&&0===r?function(t){return!!t.parentNode}:function(e,n,l){var u,c,h,p,d,f,g=o!==s?"nextSibling":"previousSibling",m=e.parentNode,v=a&&e.nodeName.toLowerCase(),y=!l&&!a;if(m){if(o){for(;g;){for(h=e;h=h[g];)if(a?h.nodeName.toLowerCase()===v:1===h.nodeType)return!1;f=g="only"===t&&!f&&"nextSibling"}return!0}if(f=[s?m.firstChild:m.lastChild],s&&y){for(c=m[q]||(m[q]={}),u=c[t]||[],d=u[0]===M&&u[1],p=u[0]===M&&u[2],h=d&&m.childNodes[d];h=++d&&h&&h[g]||(p=d=0)||f.pop();)if(1===h.nodeType&&++p&&h===e){c[t]=[M,d,p];break}}else if(y&&(u=(e[q]||(e[q]={}))[t])&&u[0]===M)p=u[1];else for(;(h=++d&&h&&h[g]||(p=d=0)||f.pop())&&((a?h.nodeName.toLowerCase()!==v:1!==h.nodeType)||!++p||(y&&((h[q]||(h[q]={}))[t]=[M,p]),h!==e)););return p-=r,p===i||p%i===0&&p/i>=0}}},PSEUDO:function(t,n){var r,o=T.pseudos[t]||T.setFilters[t.toLowerCase()]||e.error("unsupported pseudo: "+t);return o[q]?o(n):o.length>1?(r=[t,t,"",n],T.setFilters.hasOwnProperty(t.toLowerCase())?i(function(t,e){for(var i,r=o(t,n),s=r.length;s--;)i=et.call(t,r[s]),t[i]=!(e[i]=r[s])}):function(t){return o(t,0,r)}):o}},pseudos:{not:i(function(t){var e=[],n=[],r=A(t.replace(lt,"$1"));return r[q]?i(function(t,e,n,i){for(var o,s=r(t,null,i,[]),a=t.length;a--;)(o=s[a])&&(t[a]=!(e[a]=o))}):function(t,i,o){return e[0]=t,r(e,null,o,n),!n.pop()}}),has:i(function(t){return function(n){return e(t,n).length>0}}),contains:i(function(t){return function(e){return(e.textContent||e.innerText||C(e)).indexOf(t)>-1}}),lang:i(function(t){return dt.test(t||"")||e.error("unsupported lang: "+t),t=t.replace(xt,Tt).toLowerCase(),function(e){var n;do if(n=L?e.lang:e.getAttribute("xml:lang")||e.getAttribute("lang"))return n=n.toLowerCase(),n===t||0===n.indexOf(t+"-");while((e=e.parentNode)&&1===e.nodeType);return!1}}),target:function(e){var n=t.location&&t.location.hash;return n&&n.slice(1)===e.id},root:function(t){return t===O},focus:function(t){return t===_.activeElement&&(!_.hasFocus||_.hasFocus())&&!!(t.type||t.href||~t.tabIndex)},enabled:function(t){return t.disabled===!1},disabled:function(t){return t.disabled===!0},checked:function(t){var e=t.nodeName.toLowerCase();return"input"===e&&!!t.checked||"option"===e&&!!t.selected},selected:function(t){return t.parentNode&&t.parentNode.selectedIndex,t.selected===!0},empty:function(t){for(t=t.firstChild;t;t=t.nextSibling)if(t.nodeType<6)return!1;return!0},parent:function(t){return!T.pseudos.empty(t)},header:function(t){return mt.test(t.nodeName)},input:function(t){return gt.test(t.nodeName)},button:function(t){var e=t.nodeName.toLowerCase();return"input"===e&&"button"===t.type||"button"===e},text:function(t){var e;return"input"===t.nodeName.toLowerCase()&&"text"===t.type&&(null==(e=t.getAttribute("type"))||"text"===e.toLowerCase())},first:u(function(){return[0]}),last:u(function(t,e){return[e-1]}),eq:u(function(t,e,n){return[0>n?n+e:n]}),even:u(function(t,e){for(var n=0;e>n;n+=2)t.push(n);return t}),odd:u(function(t,e){for(var n=1;e>n;n+=2)t.push(n);return t}),lt:u(function(t,e,n){for(var i=0>n?n+e:n;--i>=0;)t.push(i);return t}),gt:u(function(t,e,n){for(var i=0>n?n+e:n;++i<e;)t.push(i);return t})}},T.pseudos.nth=T.pseudos.eq;for(w in{radio:!0,checkbox:!0,file:!0,password:!0,image:!0})T.pseudos[w]=a(w);for(w in{submit:!0,reset:!0})T.pseudos[w]=l(w);return h.prototype=T.filters=T.pseudos,T.setFilters=new h,E=e.tokenize=function(t,n){var i,r,o,s,a,l,u,c=W[t+" "];if(c)return n?0:c.slice(0);for(a=t,l=[],u=T.preFilter;a;){(!i||(r=ut.exec(a)))&&(r&&(a=a.slice(r[0].length)||a),l.push(o=[])),i=!1,(r=ct.exec(a))&&(i=r.shift(),o.push({value:i,type:r[0].replace(lt," ")}),a=a.slice(i.length));for(s in T.filter)!(r=ft[s].exec(a))||u[s]&&!(r=u[s](r))||(i=r.shift(),o.push({value:i,type:s,matches:r}),a=a.slice(i.length));if(!i)break}return n?a.length:a?e.error(t):W(t,l).slice(0)},A=e.compile=function(t,e){var n,i=[],r=[],o=z[t+" "];if(!o){for(e||(e=E(t)),n=e.length;n--;)o=y(e[n]),o[q]?i.push(o):r.push(o);o=z(t,b(r,i)),o.selector=t}return o},$=e.select=function(t,e,n,i){var r,o,s,a,l,u="function"==typeof t&&t,h=!i&&E(t=u.selector||t);if(n=n||[],1===h.length){if(o=h[0]=h[0].slice(0),o.length>2&&"ID"===(s=o[0]).type&&x.getById&&9===e.nodeType&&L&&T.relative[o[1].type]){if(e=(T.find.ID(s.matches[0].replace(xt,Tt),e)||[])[0],!e)return n;u&&(e=e.parentNode),t=t.slice(o.shift().value.length)}for(r=ft.needsContext.test(t)?0:o.length;r--&&(s=o[r],!T.relative[a=s.type]);)if((l=T.find[a])&&(i=l(s.matches[0].replace(xt,Tt),bt.test(o[0].type)&&c(e.parentNode)||e))){if(o.splice(r,1),t=i.length&&p(o),!t)return Z.apply(n,i),n;break}}return(u||A(t,h))(i,e,!L,n,bt.test(t)&&c(e.parentNode)||e),n},x.sortStable=q.split("").sort(V).join("")===q,x.detectDuplicates=!!j,D(),x.sortDetached=r(function(t){return 1&t.compareDocumentPosition(_.createElement("div"))}),r(function(t){return t.innerHTML="<a href='#'></a>","#"===t.firstChild.getAttribute("href")})||o("type|href|height|width",function(t,e,n){return n?void 0:t.getAttribute(e,"type"===e.toLowerCase()?1:2)}),x.attributes&&r(function(t){return t.innerHTML="<input/>",t.firstChild.setAttribute("value",""),""===t.firstChild.getAttribute("value")})||o("value",function(t,e,n){return n||"input"!==t.nodeName.toLowerCase()?void 0:t.defaultValue}),r(function(t){return null==t.getAttribute("disabled")})||o(nt,function(t,e,n){var i;return n?void 0:t[e]===!0?e.toLowerCase():(i=t.getAttributeNode(e))&&i.specified?i.value:null}),e}(e);tt.find=ot,tt.expr=ot.selectors,tt.expr[":"]=tt.expr.pseudos,tt.unique=ot.uniqueSort,tt.text=ot.getText,tt.isXMLDoc=ot.isXML,tt.contains=ot.contains;var st=tt.expr.match.needsContext,at=/^<(\w+)\s*\/?>(?:<\/\1>|)$/,lt=/^.[^:#\[\.,]*$/;tt.filter=function(t,e,n){var i=e[0];return n&&(t=":not("+t+")"),1===e.length&&1===i.nodeType?tt.find.matchesSelector(i,t)?[i]:[]:tt.find.matches(t,tt.grep(e,function(t){return 1===t.nodeType}))},tt.fn.extend({find:function(t){var e,n=this.length,i=[],r=this;if("string"!=typeof t)return this.pushStack(tt(t).filter(function(){for(e=0;n>e;e++)if(tt.contains(r[e],this))return!0}));for(e=0;n>e;e++)tt.find(t,r[e],i);return i=this.pushStack(n>1?tt.unique(i):i),i.selector=this.selector?this.selector+" "+t:t,i},filter:function(t){return this.pushStack(r(this,t||[],!1))},not:function(t){return this.pushStack(r(this,t||[],!0))},is:function(t){return!!r(this,"string"==typeof t&&st.test(t)?tt(t):t||[],!1).length}});var ut,ct=/^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]*))$/,ht=tt.fn.init=function(t,e){var n,i;if(!t)return this;if("string"==typeof t){if(n="<"===t[0]&&">"===t[t.length-1]&&t.length>=3?[null,t,null]:ct.exec(t),!n||!n[1]&&e)return!e||e.jquery?(e||ut).find(t):this.constructor(e).find(t);if(n[1]){if(e=e instanceof tt?e[0]:e,tt.merge(this,tt.parseHTML(n[1],e&&e.nodeType?e.ownerDocument||e:Y,!0)),at.test(n[1])&&tt.isPlainObject(e))for(n in e)tt.isFunction(this[n])?this[n](e[n]):this.attr(n,e[n]);return this}return i=Y.getElementById(n[2]),i&&i.parentNode&&(this.length=1,this[0]=i),this.context=Y,this.selector=t,this}return t.nodeType?(this.context=this[0]=t,this.length=1,this):tt.isFunction(t)?"undefined"!=typeof ut.ready?ut.ready(t):t(tt):(void 0!==t.selector&&(this.selector=t.selector,this.context=t.context),tt.makeArray(t,this))};ht.prototype=tt.fn,ut=tt(Y);var pt=/^(?:parents|prev(?:Until|All))/,dt={children:!0,contents:!0,next:!0,prev:!0};tt.extend({dir:function(t,e,n){for(var i=[],r=void 0!==n;(t=t[e])&&9!==t.nodeType;)if(1===t.nodeType){if(r&&tt(t).is(n))break;i.push(t)}return i},sibling:function(t,e){for(var n=[];t;t=t.nextSibling)1===t.nodeType&&t!==e&&n.push(t);return n}}),tt.fn.extend({has:function(t){var e=tt(t,this),n=e.length;return this.filter(function(){for(var t=0;n>t;t++)if(tt.contains(this,e[t]))return!0})},closest:function(t,e){for(var n,i=0,r=this.length,o=[],s=st.test(t)||"string"!=typeof t?tt(t,e||this.context):0;r>i;i++)for(n=this[i];n&&n!==e;n=n.parentNode)if(n.nodeType<11&&(s?s.index(n)>-1:1===n.nodeType&&tt.find.matchesSelector(n,t))){o.push(n);break}return this.pushStack(o.length>1?tt.unique(o):o)},index:function(t){return t?"string"==typeof t?G.call(tt(t),this[0]):G.call(this,t.jquery?t[0]:t):this[0]&&this[0].parentNode?this.first().prevAll().length:-1},add:function(t,e){return this.pushStack(tt.unique(tt.merge(this.get(),tt(t,e))))},addBack:function(t){return this.add(null==t?this.prevObject:this.prevObject.filter(t))}}),tt.each({parent:function(t){var e=t.parentNode;return e&&11!==e.nodeType?e:null},parents:function(t){return tt.dir(t,"parentNode")},parentsUntil:function(t,e,n){return tt.dir(t,"parentNode",n)},next:function(t){return o(t,"nextSibling")},prev:function(t){return o(t,"previousSibling")},nextAll:function(t){return tt.dir(t,"nextSibling")},prevAll:function(t){return tt.dir(t,"previousSibling")},nextUntil:function(t,e,n){return tt.dir(t,"nextSibling",n)},prevUntil:function(t,e,n){return tt.dir(t,"previousSibling",n)},siblings:function(t){return tt.sibling((t.parentNode||{}).firstChild,t)},children:function(t){return tt.sibling(t.firstChild)},contents:function(t){return t.contentDocument||tt.merge([],t.childNodes)}},function(t,e){tt.fn[t]=function(n,i){var r=tt.map(this,e,n);return"Until"!==t.slice(-5)&&(i=n),i&&"string"==typeof i&&(r=tt.filter(i,r)),this.length>1&&(dt[t]||tt.unique(r),pt.test(t)&&r.reverse()),this.pushStack(r)}});var ft=/\S+/g,gt={};tt.Callbacks=function(t){t="string"==typeof t?gt[t]||s(t):tt.extend({},t);var e,n,i,r,o,a,l=[],u=!t.once&&[],c=function(s){for(e=t.memory&&s,n=!0,a=r||0,r=0,o=l.length,i=!0;l&&o>a;a++)if(l[a].apply(s[0],s[1])===!1&&t.stopOnFalse){e=!1;break}i=!1,l&&(u?u.length&&c(u.shift()):e?l=[]:h.disable())},h={add:function(){if(l){var n=l.length;!function s(e){tt.each(e,function(e,n){var i=tt.type(n);"function"===i?t.unique&&h.has(n)||l.push(n):n&&n.length&&"string"!==i&&s(n)})}(arguments),i?o=l.length:e&&(r=n,c(e))}return this},remove:function(){return l&&tt.each(arguments,function(t,e){for(var n;(n=tt.inArray(e,l,n))>-1;)l.splice(n,1),i&&(o>=n&&o--,a>=n&&a--)}),this},has:function(t){return t?tt.inArray(t,l)>-1:!(!l||!l.length)},empty:function(){return l=[],o=0,this},disable:function(){return l=u=e=void 0,this},disabled:function(){return!l},lock:function(){return u=void 0,e||h.disable(),this},locked:function(){return!u},fireWith:function(t,e){return!l||n&&!u||(e=e||[],e=[t,e.slice?e.slice():e],i?u.push(e):c(e)),this},fire:function(){return h.fireWith(this,arguments),this},fired:function(){return!!n}};return h},tt.extend({Deferred:function(t){var e=[["resolve","done",tt.Callbacks("once memory"),"resolved"],["reject","fail",tt.Callbacks("once memory"),"rejected"],["notify","progress",tt.Callbacks("memory")]],n="pending",i={state:function(){return n},always:function(){return r.done(arguments).fail(arguments),this},then:function(){var t=arguments;return tt.Deferred(function(n){tt.each(e,function(e,o){var s=tt.isFunction(t[e])&&t[e];r[o[1]](function(){var t=s&&s.apply(this,arguments);t&&tt.isFunction(t.promise)?t.promise().done(n.resolve).fail(n.reject).progress(n.notify):n[o[0]+"With"](this===i?n.promise():this,s?[t]:arguments)})}),t=null}).promise()},promise:function(t){return null!=t?tt.extend(t,i):i}},r={};return i.pipe=i.then,tt.each(e,function(t,o){var s=o[2],a=o[3];i[o[1]]=s.add,a&&s.add(function(){n=a},e[1^t][2].disable,e[2][2].lock),r[o[0]]=function(){return r[o[0]+"With"](this===r?i:this,arguments),this},r[o[0]+"With"]=s.fireWith}),i.promise(r),t&&t.call(r,r),r},when:function(t){var e,n,i,r=0,o=W.call(arguments),s=o.length,a=1!==s||t&&tt.isFunction(t.promise)?s:0,l=1===a?t:tt.Deferred(),u=function(t,n,i){return function(r){n[t]=this,i[t]=arguments.length>1?W.call(arguments):r,i===e?l.notifyWith(n,i):--a||l.resolveWith(n,i)}};if(s>1)for(e=new Array(s),n=new Array(s),i=new Array(s);s>r;r++)o[r]&&tt.isFunction(o[r].promise)?o[r].promise().done(u(r,i,o)).fail(l.reject).progress(u(r,n,e)):--a;return a||l.resolveWith(i,o),l.promise()}});var mt;tt.fn.ready=function(t){return tt.ready.promise().done(t),this},tt.extend({isReady:!1,readyWait:1,holdReady:function(t){t?tt.readyWait++:tt.ready(!0)},ready:function(t){(t===!0?--tt.readyWait:tt.isReady)||(tt.isReady=!0,t!==!0&&--tt.readyWait>0||(mt.resolveWith(Y,[tt]),tt.fn.triggerHandler&&(tt(Y).triggerHandler("ready"),tt(Y).off("ready"))))}}),tt.ready.promise=function(t){return mt||(mt=tt.Deferred(),"complete"===Y.readyState?setTimeout(tt.ready):(Y.addEventListener("DOMContentLoaded",a,!1),e.addEventListener("load",a,!1))),mt.promise(t)},tt.ready.promise();var vt=tt.access=function(t,e,n,i,r,o,s){var a=0,l=t.length,u=null==n;if("object"===tt.type(n)){r=!0;for(a in n)tt.access(t,e,a,n[a],!0,o,s)}else if(void 0!==i&&(r=!0,tt.isFunction(i)||(s=!0),u&&(s?(e.call(t,i),e=null):(u=e,e=function(t,e,n){return u.call(tt(t),n)})),e))for(;l>a;a++)e(t[a],n,s?i:i.call(t[a],a,e(t[a],n)));return r?t:u?e.call(t):l?e(t[0],n):o};tt.acceptData=function(t){return 1===t.nodeType||9===t.nodeType||!+t.nodeType},l.uid=1,l.accepts=tt.acceptData,l.prototype={key:function(t){if(!l.accepts(t))return 0;var e={},n=t[this.expando];if(!n){n=l.uid++;try{e[this.expando]={value:n},Object.defineProperties(t,e)}catch(i){e[this.expando]=n,tt.extend(t,e)}}return this.cache[n]||(this.cache[n]={}),n},set:function(t,e,n){var i,r=this.key(t),o=this.cache[r];if("string"==typeof e)o[e]=n;else if(tt.isEmptyObject(o))tt.extend(this.cache[r],e);else for(i in e)o[i]=e[i];return o},get:function(t,e){var n=this.cache[this.key(t)];return void 0===e?n:n[e]},access:function(t,e,n){var i;return void 0===e||e&&"string"==typeof e&&void 0===n?(i=this.get(t,e),void 0!==i?i:this.get(t,tt.camelCase(e))):(this.set(t,e,n),void 0!==n?n:e)},remove:function(t,e){var n,i,r,o=this.key(t),s=this.cache[o];if(void 0===e)this.cache[o]={};else{tt.isArray(e)?i=e.concat(e.map(tt.camelCase)):(r=tt.camelCase(e),e in s?i=[e,r]:(i=r,i=i in s?[i]:i.match(ft)||[])),n=i.length;for(;n--;)delete s[i[n]]}},hasData:function(t){return!tt.isEmptyObject(this.cache[t[this.expando]]||{})},discard:function(t){t[this.expando]&&delete this.cache[t[this.expando]]}};var yt=new l,bt=new l,wt=/^(?:\{[\w\W]*\}|\[[\w\W]*\])$/,xt=/([A-Z])/g;tt.extend({hasData:function(t){return bt.hasData(t)||yt.hasData(t)},data:function(t,e,n){return bt.access(t,e,n)},removeData:function(t,e){bt.remove(t,e)},_data:function(t,e,n){return yt.access(t,e,n)},_removeData:function(t,e){yt.remove(t,e)}}),tt.fn.extend({data:function(t,e){var n,i,r,o=this[0],s=o&&o.attributes;if(void 0===t){if(this.length&&(r=bt.get(o),1===o.nodeType&&!yt.get(o,"hasDataAttrs"))){for(n=s.length;n--;)s[n]&&(i=s[n].name,0===i.indexOf("data-")&&(i=tt.camelCase(i.slice(5)),u(o,i,r[i])));yt.set(o,"hasDataAttrs",!0)}return r}return"object"==typeof t?this.each(function(){bt.set(this,t)}):vt(this,function(e){var n,i=tt.camelCase(t);if(o&&void 0===e){if(n=bt.get(o,t),void 0!==n)return n;if(n=bt.get(o,i),void 0!==n)return n;if(n=u(o,i,void 0),void 0!==n)return n}else this.each(function(){var n=bt.get(this,i);bt.set(this,i,e),-1!==t.indexOf("-")&&void 0!==n&&bt.set(this,t,e)})},null,e,arguments.length>1,null,!0)},removeData:function(t){return this.each(function(){bt.remove(this,t)})}}),tt.extend({queue:function(t,e,n){var i;return t?(e=(e||"fx")+"queue",i=yt.get(t,e),n&&(!i||tt.isArray(n)?i=yt.access(t,e,tt.makeArray(n)):i.push(n)),i||[]):void 0},dequeue:function(t,e){e=e||"fx";var n=tt.queue(t,e),i=n.length,r=n.shift(),o=tt._queueHooks(t,e),s=function(){tt.dequeue(t,e)};"inprogress"===r&&(r=n.shift(),i--),r&&("fx"===e&&n.unshift("inprogress"),delete o.stop,r.call(t,s,o)),!i&&o&&o.empty.fire()},_queueHooks:function(t,e){var n=e+"queueHooks";return yt.get(t,n)||yt.access(t,n,{empty:tt.Callbacks("once memory").add(function(){yt.remove(t,[e+"queue",n])})})}}),tt.fn.extend({queue:function(t,e){var n=2;return"string"!=typeof t&&(e=t,t="fx",n--),arguments.length<n?tt.queue(this[0],t):void 0===e?this:this.each(function(){var n=tt.queue(this,t,e);tt._queueHooks(this,t),"fx"===t&&"inprogress"!==n[0]&&tt.dequeue(this,t)})},dequeue:function(t){return this.each(function(){tt.dequeue(this,t)})},clearQueue:function(t){return this.queue(t||"fx",[])},promise:function(t,e){var n,i=1,r=tt.Deferred(),o=this,s=this.length,a=function(){--i||r.resolveWith(o,[o])};for("string"!=typeof t&&(e=t,t=void 0),t=t||"fx";s--;)n=yt.get(o[s],t+"queueHooks"),n&&n.empty&&(i++,n.empty.add(a));return a(),r.promise(e)}});var Tt=/[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source,Ct=["Top","Right","Bottom","Left"],kt=function(t,e){return t=e||t,"none"===tt.css(t,"display")||!tt.contains(t.ownerDocument,t)},Et=/^(?:checkbox|radio)$/i;!function(){var t=Y.createDocumentFragment(),e=t.appendChild(Y.createElement("div")),n=Y.createElement("input");n.setAttribute("type","radio"),n.setAttribute("checked","checked"),n.setAttribute("name","t"),e.appendChild(n),K.checkClone=e.cloneNode(!0).cloneNode(!0).lastChild.checked,e.innerHTML="<textarea>x</textarea>",K.noCloneChecked=!!e.cloneNode(!0).lastChild.defaultValue}();var At="undefined";K.focusinBubbles="onfocusin"in e;var $t=/^key/,St=/^(?:mouse|pointer|contextmenu)|click/,Nt=/^(?:focusinfocus|focusoutblur)$/,jt=/^([^.]*)(?:\.(.+)|)$/;tt.event={global:{},add:function(t,e,n,i,r){var o,s,a,l,u,c,h,p,d,f,g,m=yt.get(t);if(m)for(n.handler&&(o=n,n=o.handler,r=o.selector),n.guid||(n.guid=tt.guid++),(l=m.events)||(l=m.events={}),(s=m.handle)||(s=m.handle=function(e){return typeof tt!==At&&tt.event.triggered!==e.type?tt.event.dispatch.apply(t,arguments):void 0}),e=(e||"").match(ft)||[""],u=e.length;u--;)a=jt.exec(e[u])||[],d=g=a[1],f=(a[2]||"").split(".").sort(),d&&(h=tt.event.special[d]||{},d=(r?h.delegateType:h.bindType)||d,h=tt.event.special[d]||{},c=tt.extend({type:d,origType:g,data:i,handler:n,guid:n.guid,selector:r,needsContext:r&&tt.expr.match.needsContext.test(r),namespace:f.join(".")},o),(p=l[d])||(p=l[d]=[],p.delegateCount=0,h.setup&&h.setup.call(t,i,f,s)!==!1||t.addEventListener&&t.addEventListener(d,s,!1)),h.add&&(h.add.call(t,c),c.handler.guid||(c.handler.guid=n.guid)),r?p.splice(p.delegateCount++,0,c):p.push(c),tt.event.global[d]=!0)},remove:function(t,e,n,i,r){var o,s,a,l,u,c,h,p,d,f,g,m=yt.hasData(t)&&yt.get(t);if(m&&(l=m.events)){for(e=(e||"").match(ft)||[""],u=e.length;u--;)if(a=jt.exec(e[u])||[],d=g=a[1],f=(a[2]||"").split(".").sort(),d){for(h=tt.event.special[d]||{},d=(i?h.delegateType:h.bindType)||d,p=l[d]||[],a=a[2]&&new RegExp("(^|\\.)"+f.join("\\.(?:.*\\.|)")+"(\\.|$)"),s=o=p.length;o--;)c=p[o],!r&&g!==c.origType||n&&n.guid!==c.guid||a&&!a.test(c.namespace)||i&&i!==c.selector&&("**"!==i||!c.selector)||(p.splice(o,1),c.selector&&p.delegateCount--,h.remove&&h.remove.call(t,c));s&&!p.length&&(h.teardown&&h.teardown.call(t,f,m.handle)!==!1||tt.removeEvent(t,d,m.handle),delete l[d])}else for(d in l)tt.event.remove(t,d+e[u],n,i,!0);tt.isEmptyObject(l)&&(delete m.handle,yt.remove(t,"events"))}},trigger:function(t,n,i,r){var o,s,a,l,u,c,h,p=[i||Y],d=J.call(t,"type")?t.type:t,f=J.call(t,"namespace")?t.namespace.split("."):[];if(s=a=i=i||Y,3!==i.nodeType&&8!==i.nodeType&&!Nt.test(d+tt.event.triggered)&&(d.indexOf(".")>=0&&(f=d.split("."),d=f.shift(),f.sort()),u=d.indexOf(":")<0&&"on"+d,t=t[tt.expando]?t:new tt.Event(d,"object"==typeof t&&t),t.isTrigger=r?2:3,t.namespace=f.join("."),t.namespace_re=t.namespace?new RegExp("(^|\\.)"+f.join("\\.(?:.*\\.|)")+"(\\.|$)"):null,t.result=void 0,t.target||(t.target=i),n=null==n?[t]:tt.makeArray(n,[t]),h=tt.event.special[d]||{},r||!h.trigger||h.trigger.apply(i,n)!==!1)){if(!r&&!h.noBubble&&!tt.isWindow(i)){for(l=h.delegateType||d,Nt.test(l+d)||(s=s.parentNode);s;s=s.parentNode)p.push(s),a=s;a===(i.ownerDocument||Y)&&p.push(a.defaultView||a.parentWindow||e)}for(o=0;(s=p[o++])&&!t.isPropagationStopped();)t.type=o>1?l:h.bindType||d,c=(yt.get(s,"events")||{})[t.type]&&yt.get(s,"handle"),c&&c.apply(s,n),c=u&&s[u],c&&c.apply&&tt.acceptData(s)&&(t.result=c.apply(s,n),t.result===!1&&t.preventDefault());return t.type=d,r||t.isDefaultPrevented()||h._default&&h._default.apply(p.pop(),n)!==!1||!tt.acceptData(i)||u&&tt.isFunction(i[d])&&!tt.isWindow(i)&&(a=i[u],a&&(i[u]=null),tt.event.triggered=d,i[d](),tt.event.triggered=void 0,a&&(i[u]=a)),t.result}},dispatch:function(t){t=tt.event.fix(t);var e,n,i,r,o,s=[],a=W.call(arguments),l=(yt.get(this,"events")||{})[t.type]||[],u=tt.event.special[t.type]||{};if(a[0]=t,t.delegateTarget=this,!u.preDispatch||u.preDispatch.call(this,t)!==!1){for(s=tt.event.handlers.call(this,t,l),e=0;(r=s[e++])&&!t.isPropagationStopped();)for(t.currentTarget=r.elem,n=0;(o=r.handlers[n++])&&!t.isImmediatePropagationStopped();)(!t.namespace_re||t.namespace_re.test(o.namespace))&&(t.handleObj=o,t.data=o.data,i=((tt.event.special[o.origType]||{}).handle||o.handler).apply(r.elem,a),void 0!==i&&(t.result=i)===!1&&(t.preventDefault(),t.stopPropagation()));return u.postDispatch&&u.postDispatch.call(this,t),t.result}},handlers:function(t,e){var n,i,r,o,s=[],a=e.delegateCount,l=t.target;if(a&&l.nodeType&&(!t.button||"click"!==t.type))for(;l!==this;l=l.parentNode||this)if(l.disabled!==!0||"click"!==t.type){for(i=[],n=0;a>n;n++)o=e[n],r=o.selector+" ",void 0===i[r]&&(i[r]=o.needsContext?tt(r,this).index(l)>=0:tt.find(r,this,null,[l]).length),i[r]&&i.push(o);i.length&&s.push({elem:l,handlers:i})}return a<e.length&&s.push({elem:this,handlers:e.slice(a)}),s},props:"altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),fixHooks:{},keyHooks:{props:"char charCode key keyCode".split(" "),filter:function(t,e){return null==t.which&&(t.which=null!=e.charCode?e.charCode:e.keyCode),t}},mouseHooks:{props:"button buttons clientX clientY offsetX offsetY pageX pageY screenX screenY toElement".split(" "),filter:function(t,e){var n,i,r,o=e.button;return null==t.pageX&&null!=e.clientX&&(n=t.target.ownerDocument||Y,i=n.documentElement,r=n.body,t.pageX=e.clientX+(i&&i.scrollLeft||r&&r.scrollLeft||0)-(i&&i.clientLeft||r&&r.clientLeft||0),t.pageY=e.clientY+(i&&i.scrollTop||r&&r.scrollTop||0)-(i&&i.clientTop||r&&r.clientTop||0)),t.which||void 0===o||(t.which=1&o?1:2&o?3:4&o?2:0),t}},fix:function(t){if(t[tt.expando])return t;var e,n,i,r=t.type,o=t,s=this.fixHooks[r];for(s||(this.fixHooks[r]=s=St.test(r)?this.mouseHooks:$t.test(r)?this.keyHooks:{}),i=s.props?this.props.concat(s.props):this.props,t=new tt.Event(o),e=i.length;e--;)n=i[e],t[n]=o[n];return t.target||(t.target=Y),3===t.target.nodeType&&(t.target=t.target.parentNode),s.filter?s.filter(t,o):t},special:{load:{noBubble:!0},focus:{trigger:function(){return this!==p()&&this.focus?(this.focus(),!1):void 0},delegateType:"focusin"},blur:{trigger:function(){return this===p()&&this.blur?(this.blur(),!1):void 0},delegateType:"focusout"},click:{trigger:function(){return"checkbox"===this.type&&this.click&&tt.nodeName(this,"input")?(this.click(),!1):void 0},_default:function(t){return tt.nodeName(t.target,"a")}},beforeunload:{postDispatch:function(t){void 0!==t.result&&t.originalEvent&&(t.originalEvent.returnValue=t.result)}}},simulate:function(t,e,n,i){var r=tt.extend(new tt.Event,n,{type:t,isSimulated:!0,originalEvent:{}});i?tt.event.trigger(r,null,e):tt.event.dispatch.call(e,r),r.isDefaultPrevented()&&n.preventDefault()}},tt.removeEvent=function(t,e,n){t.removeEventListener&&t.removeEventListener(e,n,!1)},tt.Event=function(t,e){return this instanceof tt.Event?(t&&t.type?(this.originalEvent=t,this.type=t.type,this.isDefaultPrevented=t.defaultPrevented||void 0===t.defaultPrevented&&t.returnValue===!1?c:h):this.type=t,e&&tt.extend(this,e),this.timeStamp=t&&t.timeStamp||tt.now(),void(this[tt.expando]=!0)):new tt.Event(t,e)},tt.Event.prototype={isDefaultPrevented:h,isPropagationStopped:h,isImmediatePropagationStopped:h,preventDefault:function(){var t=this.originalEvent;this.isDefaultPrevented=c,t&&t.preventDefault&&t.preventDefault()},stopPropagation:function(){var t=this.originalEvent;this.isPropagationStopped=c,t&&t.stopPropagation&&t.stopPropagation()},stopImmediatePropagation:function(){var t=this.originalEvent;this.isImmediatePropagationStopped=c,t&&t.stopImmediatePropagation&&t.stopImmediatePropagation(),this.stopPropagation()}},tt.each({mouseenter:"mouseover",mouseleave:"mouseout",pointerenter:"pointerover",pointerleave:"pointerout"},function(t,e){tt.event.special[t]={delegateType:e,bindType:e,handle:function(t){var n,i=this,r=t.relatedTarget,o=t.handleObj;return(!r||r!==i&&!tt.contains(i,r))&&(t.type=o.origType,n=o.handler.apply(this,arguments),t.type=e),n}}}),K.focusinBubbles||tt.each({focus:"focusin",blur:"focusout"},function(t,e){var n=function(t){tt.event.simulate(e,t.target,tt.event.fix(t),!0)};tt.event.special[e]={setup:function(){var i=this.ownerDocument||this,r=yt.access(i,e);r||i.addEventListener(t,n,!0),yt.access(i,e,(r||0)+1)},teardown:function(){var i=this.ownerDocument||this,r=yt.access(i,e)-1;r?yt.access(i,e,r):(i.removeEventListener(t,n,!0),yt.remove(i,e))}}}),tt.fn.extend({on:function(t,e,n,i,r){var o,s;if("object"==typeof t){"string"!=typeof e&&(n=n||e,e=void 0);for(s in t)this.on(s,e,n,t[s],r);return this}if(null==n&&null==i?(i=e,n=e=void 0):null==i&&("string"==typeof e?(i=n,n=void 0):(i=n,n=e,e=void 0)),i===!1)i=h;else if(!i)return this;return 1===r&&(o=i,i=function(t){return tt().off(t),o.apply(this,arguments)},i.guid=o.guid||(o.guid=tt.guid++)),this.each(function(){tt.event.add(this,t,i,n,e)})},one:function(t,e,n,i){return this.on(t,e,n,i,1)},off:function(t,e,n){var i,r;if(t&&t.preventDefault&&t.handleObj)return i=t.handleObj,tt(t.delegateTarget).off(i.namespace?i.origType+"."+i.namespace:i.origType,i.selector,i.handler),this;if("object"==typeof t){for(r in t)this.off(r,e,t[r]);return this}return(e===!1||"function"==typeof e)&&(n=e,e=void 0),n===!1&&(n=h),this.each(function(){tt.event.remove(this,t,n,e)})},trigger:function(t,e){return this.each(function(){tt.event.trigger(t,e,this)})},triggerHandler:function(t,e){var n=this[0];return n?tt.event.trigger(t,e,n,!0):void 0}});var Dt=/<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi,_t=/<([\w:]+)/,Ot=/<|&#?\w+;/,Lt=/<(?:script|style|link)/i,It=/checked\s*(?:[^=]|=\s*.checked.)/i,Pt=/^$|\/(?:java|ecma)script/i,Ft=/^true\/(.*)/,Rt=/^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g,qt={option:[1,"<select multiple='multiple'>","</select>"],thead:[1,"<table>","</table>"],col:[2,"<table><colgroup>","</colgroup></table>"],tr:[2,"<table><tbody>","</tbody></table>"],td:[3,"<table><tbody><tr>","</tr></tbody></table>"],_default:[0,"",""]};qt.optgroup=qt.option,qt.tbody=qt.tfoot=qt.colgroup=qt.caption=qt.thead,qt.th=qt.td,tt.extend({clone:function(t,e,n){var i,r,o,s,a=t.cloneNode(!0),l=tt.contains(t.ownerDocument,t);if(!(K.noCloneChecked||1!==t.nodeType&&11!==t.nodeType||tt.isXMLDoc(t)))for(s=y(a),o=y(t),i=0,r=o.length;r>i;i++)b(o[i],s[i]);if(e)if(n)for(o=o||y(t),s=s||y(a),i=0,r=o.length;r>i;i++)v(o[i],s[i]);else v(t,a);return s=y(a,"script"),s.length>0&&m(s,!l&&y(t,"script")),a},buildFragment:function(t,e,n,i){for(var r,o,s,a,l,u,c=e.createDocumentFragment(),h=[],p=0,d=t.length;d>p;p++)if(r=t[p],r||0===r)if("object"===tt.type(r))tt.merge(h,r.nodeType?[r]:r);else if(Ot.test(r)){for(o=o||c.appendChild(e.createElement("div")),s=(_t.exec(r)||["",""])[1].toLowerCase(),a=qt[s]||qt._default,o.innerHTML=a[1]+r.replace(Dt,"<$1></$2>")+a[2],u=a[0];u--;)o=o.lastChild;tt.merge(h,o.childNodes),o=c.firstChild,o.textContent=""}else h.push(e.createTextNode(r));for(c.textContent="",p=0;r=h[p++];)if((!i||-1===tt.inArray(r,i))&&(l=tt.contains(r.ownerDocument,r),o=y(c.appendChild(r),"script"),l&&m(o),n))for(u=0;r=o[u++];)Pt.test(r.type||"")&&n.push(r);return c},cleanData:function(t){for(var e,n,i,r,o=tt.event.special,s=0;void 0!==(n=t[s]);s++){if(tt.acceptData(n)&&(r=n[yt.expando],r&&(e=yt.cache[r]))){if(e.events)for(i in e.events)o[i]?tt.event.remove(n,i):tt.removeEvent(n,i,e.handle);yt.cache[r]&&delete yt.cache[r]}delete bt.cache[n[bt.expando]]}}}),tt.fn.extend({text:function(t){return vt(this,function(t){return void 0===t?tt.text(this):this.empty().each(function(){(1===this.nodeType||11===this.nodeType||9===this.nodeType)&&(this.textContent=t)})},null,t,arguments.length)},append:function(){return this.domManip(arguments,function(t){if(1===this.nodeType||11===this.nodeType||9===this.nodeType){var e=d(this,t);e.appendChild(t)}})},prepend:function(){return this.domManip(arguments,function(t){if(1===this.nodeType||11===this.nodeType||9===this.nodeType){var e=d(this,t);e.insertBefore(t,e.firstChild)}})},before:function(){return this.domManip(arguments,function(t){this.parentNode&&this.parentNode.insertBefore(t,this)})},after:function(){return this.domManip(arguments,function(t){this.parentNode&&this.parentNode.insertBefore(t,this.nextSibling)})},remove:function(t,e){for(var n,i=t?tt.filter(t,this):this,r=0;null!=(n=i[r]);r++)e||1!==n.nodeType||tt.cleanData(y(n)),n.parentNode&&(e&&tt.contains(n.ownerDocument,n)&&m(y(n,"script")),n.parentNode.removeChild(n));return this},empty:function(){for(var t,e=0;null!=(t=this[e]);e++)1===t.nodeType&&(tt.cleanData(y(t,!1)),t.textContent="");return this},clone:function(t,e){return t=null==t?!1:t,e=null==e?t:e,this.map(function(){return tt.clone(this,t,e)})},html:function(t){return vt(this,function(t){var e=this[0]||{},n=0,i=this.length;if(void 0===t&&1===e.nodeType)return e.innerHTML;if("string"==typeof t&&!Lt.test(t)&&!qt[(_t.exec(t)||["",""])[1].toLowerCase()]){t=t.replace(Dt,"<$1></$2>");try{for(;i>n;n++)e=this[n]||{},1===e.nodeType&&(tt.cleanData(y(e,!1)),e.innerHTML=t);e=0}catch(r){}}e&&this.empty().append(t)},null,t,arguments.length)},replaceWith:function(){var t=arguments[0];return this.domManip(arguments,function(e){t=this.parentNode,tt.cleanData(y(this)),t&&t.replaceChild(e,this)}),t&&(t.length||t.nodeType)?this:this.remove()},detach:function(t){return this.remove(t,!0)},domManip:function(t,e){t=z.apply([],t);var n,i,r,o,s,a,l=0,u=this.length,c=this,h=u-1,p=t[0],d=tt.isFunction(p);if(d||u>1&&"string"==typeof p&&!K.checkClone&&It.test(p))return this.each(function(n){
	var i=c.eq(n);d&&(t[0]=p.call(this,n,i.html())),i.domManip(t,e)});if(u&&(n=tt.buildFragment(t,this[0].ownerDocument,!1,this),i=n.firstChild,1===n.childNodes.length&&(n=i),i)){for(r=tt.map(y(n,"script"),f),o=r.length;u>l;l++)s=n,l!==h&&(s=tt.clone(s,!0,!0),o&&tt.merge(r,y(s,"script"))),e.call(this[l],s,l);if(o)for(a=r[r.length-1].ownerDocument,tt.map(r,g),l=0;o>l;l++)s=r[l],Pt.test(s.type||"")&&!yt.access(s,"globalEval")&&tt.contains(a,s)&&(s.src?tt._evalUrl&&tt._evalUrl(s.src):tt.globalEval(s.textContent.replace(Rt,"")))}return this}}),tt.each({appendTo:"append",prependTo:"prepend",insertBefore:"before",insertAfter:"after",replaceAll:"replaceWith"},function(t,e){tt.fn[t]=function(t){for(var n,i=[],r=tt(t),o=r.length-1,s=0;o>=s;s++)n=s===o?this:this.clone(!0),tt(r[s])[e](n),V.apply(i,n.get());return this.pushStack(i)}});var Ht,Mt={},Ut=/^margin/,Bt=new RegExp("^("+Tt+")(?!px)[a-z%]+$","i"),Wt=function(t){return t.ownerDocument.defaultView.getComputedStyle(t,null)};!function(){function t(){s.style.cssText="-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;display:block;margin-top:1%;top:1%;border:1px;padding:1px;width:4px;position:absolute",s.innerHTML="",r.appendChild(o);var t=e.getComputedStyle(s,null);n="1%"!==t.top,i="4px"===t.width,r.removeChild(o)}var n,i,r=Y.documentElement,o=Y.createElement("div"),s=Y.createElement("div");s.style&&(s.style.backgroundClip="content-box",s.cloneNode(!0).style.backgroundClip="",K.clearCloneStyle="content-box"===s.style.backgroundClip,o.style.cssText="border:0;width:0;height:0;top:0;left:-9999px;margin-top:1px;position:absolute",o.appendChild(s),e.getComputedStyle&&tt.extend(K,{pixelPosition:function(){return t(),n},boxSizingReliable:function(){return null==i&&t(),i},reliableMarginRight:function(){var t,n=s.appendChild(Y.createElement("div"));return n.style.cssText=s.style.cssText="-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:0",n.style.marginRight=n.style.width="0",s.style.width="1px",r.appendChild(o),t=!parseFloat(e.getComputedStyle(n,null).marginRight),r.removeChild(o),t}}))}(),tt.swap=function(t,e,n,i){var r,o,s={};for(o in e)s[o]=t.style[o],t.style[o]=e[o];r=n.apply(t,i||[]);for(o in e)t.style[o]=s[o];return r};var zt=/^(none|table(?!-c[ea]).+)/,Vt=new RegExp("^("+Tt+")(.*)$","i"),Gt=new RegExp("^([+-])=("+Tt+")","i"),Xt={position:"absolute",visibility:"hidden",display:"block"},Qt={letterSpacing:"0",fontWeight:"400"},Jt=["Webkit","O","Moz","ms"];tt.extend({cssHooks:{opacity:{get:function(t,e){if(e){var n=T(t,"opacity");return""===n?"1":n}}}},cssNumber:{columnCount:!0,fillOpacity:!0,flexGrow:!0,flexShrink:!0,fontWeight:!0,lineHeight:!0,opacity:!0,order:!0,orphans:!0,widows:!0,zIndex:!0,zoom:!0},cssProps:{"float":"cssFloat"},style:function(t,e,n,i){if(t&&3!==t.nodeType&&8!==t.nodeType&&t.style){var r,o,s,a=tt.camelCase(e),l=t.style;return e=tt.cssProps[a]||(tt.cssProps[a]=k(l,a)),s=tt.cssHooks[e]||tt.cssHooks[a],void 0===n?s&&"get"in s&&void 0!==(r=s.get(t,!1,i))?r:l[e]:(o=typeof n,"string"===o&&(r=Gt.exec(n))&&(n=(r[1]+1)*r[2]+parseFloat(tt.css(t,e)),o="number"),null!=n&&n===n&&("number"!==o||tt.cssNumber[a]||(n+="px"),K.clearCloneStyle||""!==n||0!==e.indexOf("background")||(l[e]="inherit"),s&&"set"in s&&void 0===(n=s.set(t,n,i))||(l[e]=n)),void 0)}},css:function(t,e,n,i){var r,o,s,a=tt.camelCase(e);return e=tt.cssProps[a]||(tt.cssProps[a]=k(t.style,a)),s=tt.cssHooks[e]||tt.cssHooks[a],s&&"get"in s&&(r=s.get(t,!0,n)),void 0===r&&(r=T(t,e,i)),"normal"===r&&e in Qt&&(r=Qt[e]),""===n||n?(o=parseFloat(r),n===!0||tt.isNumeric(o)?o||0:r):r}}),tt.each(["height","width"],function(t,e){tt.cssHooks[e]={get:function(t,n,i){return n?zt.test(tt.css(t,"display"))&&0===t.offsetWidth?tt.swap(t,Xt,function(){return $(t,e,i)}):$(t,e,i):void 0},set:function(t,n,i){var r=i&&Wt(t);return E(t,n,i?A(t,e,i,"border-box"===tt.css(t,"boxSizing",!1,r),r):0)}}}),tt.cssHooks.marginRight=C(K.reliableMarginRight,function(t,e){return e?tt.swap(t,{display:"inline-block"},T,[t,"marginRight"]):void 0}),tt.each({margin:"",padding:"",border:"Width"},function(t,e){tt.cssHooks[t+e]={expand:function(n){for(var i=0,r={},o="string"==typeof n?n.split(" "):[n];4>i;i++)r[t+Ct[i]+e]=o[i]||o[i-2]||o[0];return r}},Ut.test(t)||(tt.cssHooks[t+e].set=E)}),tt.fn.extend({css:function(t,e){return vt(this,function(t,e,n){var i,r,o={},s=0;if(tt.isArray(e)){for(i=Wt(t),r=e.length;r>s;s++)o[e[s]]=tt.css(t,e[s],!1,i);return o}return void 0!==n?tt.style(t,e,n):tt.css(t,e)},t,e,arguments.length>1)},show:function(){return S(this,!0)},hide:function(){return S(this)},toggle:function(t){return"boolean"==typeof t?t?this.show():this.hide():this.each(function(){kt(this)?tt(this).show():tt(this).hide()})}}),tt.Tween=N,N.prototype={constructor:N,init:function(t,e,n,i,r,o){this.elem=t,this.prop=n,this.easing=r||"swing",this.options=e,this.start=this.now=this.cur(),this.end=i,this.unit=o||(tt.cssNumber[n]?"":"px")},cur:function(){var t=N.propHooks[this.prop];return t&&t.get?t.get(this):N.propHooks._default.get(this)},run:function(t){var e,n=N.propHooks[this.prop];return this.pos=e=this.options.duration?tt.easing[this.easing](t,this.options.duration*t,0,1,this.options.duration):t,this.now=(this.end-this.start)*e+this.start,this.options.step&&this.options.step.call(this.elem,this.now,this),n&&n.set?n.set(this):N.propHooks._default.set(this),this}},N.prototype.init.prototype=N.prototype,N.propHooks={_default:{get:function(t){var e;return null==t.elem[t.prop]||t.elem.style&&null!=t.elem.style[t.prop]?(e=tt.css(t.elem,t.prop,""),e&&"auto"!==e?e:0):t.elem[t.prop]},set:function(t){tt.fx.step[t.prop]?tt.fx.step[t.prop](t):t.elem.style&&(null!=t.elem.style[tt.cssProps[t.prop]]||tt.cssHooks[t.prop])?tt.style(t.elem,t.prop,t.now+t.unit):t.elem[t.prop]=t.now}}},N.propHooks.scrollTop=N.propHooks.scrollLeft={set:function(t){t.elem.nodeType&&t.elem.parentNode&&(t.elem[t.prop]=t.now)}},tt.easing={linear:function(t){return t},swing:function(t){return.5-Math.cos(t*Math.PI)/2}},tt.fx=N.prototype.init,tt.fx.step={};var Kt,Yt,Zt=/^(?:toggle|show|hide)$/,te=new RegExp("^(?:([+-])=|)("+Tt+")([a-z%]*)$","i"),ee=/queueHooks$/,ne=[O],ie={"*":[function(t,e){var n=this.createTween(t,e),i=n.cur(),r=te.exec(e),o=r&&r[3]||(tt.cssNumber[t]?"":"px"),s=(tt.cssNumber[t]||"px"!==o&&+i)&&te.exec(tt.css(n.elem,t)),a=1,l=20;if(s&&s[3]!==o){o=o||s[3],r=r||[],s=+i||1;do a=a||".5",s/=a,tt.style(n.elem,t,s+o);while(a!==(a=n.cur()/i)&&1!==a&&--l)}return r&&(s=n.start=+s||+i||0,n.unit=o,n.end=r[1]?s+(r[1]+1)*r[2]:+r[2]),n}]};tt.Animation=tt.extend(I,{tweener:function(t,e){tt.isFunction(t)?(e=t,t=["*"]):t=t.split(" ");for(var n,i=0,r=t.length;r>i;i++)n=t[i],ie[n]=ie[n]||[],ie[n].unshift(e)},prefilter:function(t,e){e?ne.unshift(t):ne.push(t)}}),tt.speed=function(t,e,n){var i=t&&"object"==typeof t?tt.extend({},t):{complete:n||!n&&e||tt.isFunction(t)&&t,duration:t,easing:n&&e||e&&!tt.isFunction(e)&&e};return i.duration=tt.fx.off?0:"number"==typeof i.duration?i.duration:i.duration in tt.fx.speeds?tt.fx.speeds[i.duration]:tt.fx.speeds._default,(null==i.queue||i.queue===!0)&&(i.queue="fx"),i.old=i.complete,i.complete=function(){tt.isFunction(i.old)&&i.old.call(this),i.queue&&tt.dequeue(this,i.queue)},i},tt.fn.extend({fadeTo:function(t,e,n,i){return this.filter(kt).css("opacity",0).show().end().animate({opacity:e},t,n,i)},animate:function(t,e,n,i){var r=tt.isEmptyObject(t),o=tt.speed(e,n,i),s=function(){var e=I(this,tt.extend({},t),o);(r||yt.get(this,"finish"))&&e.stop(!0)};return s.finish=s,r||o.queue===!1?this.each(s):this.queue(o.queue,s)},stop:function(t,e,n){var i=function(t){var e=t.stop;delete t.stop,e(n)};return"string"!=typeof t&&(n=e,e=t,t=void 0),e&&t!==!1&&this.queue(t||"fx",[]),this.each(function(){var e=!0,r=null!=t&&t+"queueHooks",o=tt.timers,s=yt.get(this);if(r)s[r]&&s[r].stop&&i(s[r]);else for(r in s)s[r]&&s[r].stop&&ee.test(r)&&i(s[r]);for(r=o.length;r--;)o[r].elem!==this||null!=t&&o[r].queue!==t||(o[r].anim.stop(n),e=!1,o.splice(r,1));(e||!n)&&tt.dequeue(this,t)})},finish:function(t){return t!==!1&&(t=t||"fx"),this.each(function(){var e,n=yt.get(this),i=n[t+"queue"],r=n[t+"queueHooks"],o=tt.timers,s=i?i.length:0;for(n.finish=!0,tt.queue(this,t,[]),r&&r.stop&&r.stop.call(this,!0),e=o.length;e--;)o[e].elem===this&&o[e].queue===t&&(o[e].anim.stop(!0),o.splice(e,1));for(e=0;s>e;e++)i[e]&&i[e].finish&&i[e].finish.call(this);delete n.finish})}}),tt.each(["toggle","show","hide"],function(t,e){var n=tt.fn[e];tt.fn[e]=function(t,i,r){return null==t||"boolean"==typeof t?n.apply(this,arguments):this.animate(D(e,!0),t,i,r)}}),tt.each({slideDown:D("show"),slideUp:D("hide"),slideToggle:D("toggle"),fadeIn:{opacity:"show"},fadeOut:{opacity:"hide"},fadeToggle:{opacity:"toggle"}},function(t,e){tt.fn[t]=function(t,n,i){return this.animate(e,t,n,i)}}),tt.timers=[],tt.fx.tick=function(){var t,e=0,n=tt.timers;for(Kt=tt.now();e<n.length;e++)t=n[e],t()||n[e]!==t||n.splice(e--,1);n.length||tt.fx.stop(),Kt=void 0},tt.fx.timer=function(t){tt.timers.push(t),t()?tt.fx.start():tt.timers.pop()},tt.fx.interval=13,tt.fx.start=function(){Yt||(Yt=setInterval(tt.fx.tick,tt.fx.interval))},tt.fx.stop=function(){clearInterval(Yt),Yt=null},tt.fx.speeds={slow:600,fast:200,_default:400},tt.fn.delay=function(t,e){return t=tt.fx?tt.fx.speeds[t]||t:t,e=e||"fx",this.queue(e,function(e,n){var i=setTimeout(e,t);n.stop=function(){clearTimeout(i)}})},function(){var t=Y.createElement("input"),e=Y.createElement("select"),n=e.appendChild(Y.createElement("option"));t.type="checkbox",K.checkOn=""!==t.value,K.optSelected=n.selected,e.disabled=!0,K.optDisabled=!n.disabled,t=Y.createElement("input"),t.value="t",t.type="radio",K.radioValue="t"===t.value}();var re,oe,se=tt.expr.attrHandle;tt.fn.extend({attr:function(t,e){return vt(this,tt.attr,t,e,arguments.length>1)},removeAttr:function(t){return this.each(function(){tt.removeAttr(this,t)})}}),tt.extend({attr:function(t,e,n){var i,r,o=t.nodeType;if(t&&3!==o&&8!==o&&2!==o)return typeof t.getAttribute===At?tt.prop(t,e,n):(1===o&&tt.isXMLDoc(t)||(e=e.toLowerCase(),i=tt.attrHooks[e]||(tt.expr.match.bool.test(e)?oe:re)),void 0===n?i&&"get"in i&&null!==(r=i.get(t,e))?r:(r=tt.find.attr(t,e),null==r?void 0:r):null!==n?i&&"set"in i&&void 0!==(r=i.set(t,n,e))?r:(t.setAttribute(e,n+""),n):void tt.removeAttr(t,e))},removeAttr:function(t,e){var n,i,r=0,o=e&&e.match(ft);if(o&&1===t.nodeType)for(;n=o[r++];)i=tt.propFix[n]||n,tt.expr.match.bool.test(n)&&(t[i]=!1),t.removeAttribute(n)},attrHooks:{type:{set:function(t,e){if(!K.radioValue&&"radio"===e&&tt.nodeName(t,"input")){var n=t.value;return t.setAttribute("type",e),n&&(t.value=n),e}}}}}),oe={set:function(t,e,n){return e===!1?tt.removeAttr(t,n):t.setAttribute(n,n),n}},tt.each(tt.expr.match.bool.source.match(/\w+/g),function(t,e){var n=se[e]||tt.find.attr;se[e]=function(t,e,i){var r,o;return i||(o=se[e],se[e]=r,r=null!=n(t,e,i)?e.toLowerCase():null,se[e]=o),r}});var ae=/^(?:input|select|textarea|button)$/i;tt.fn.extend({prop:function(t,e){return vt(this,tt.prop,t,e,arguments.length>1)},removeProp:function(t){return this.each(function(){delete this[tt.propFix[t]||t]})}}),tt.extend({propFix:{"for":"htmlFor","class":"className"},prop:function(t,e,n){var i,r,o,s=t.nodeType;if(t&&3!==s&&8!==s&&2!==s)return o=1!==s||!tt.isXMLDoc(t),o&&(e=tt.propFix[e]||e,r=tt.propHooks[e]),void 0!==n?r&&"set"in r&&void 0!==(i=r.set(t,n,e))?i:t[e]=n:r&&"get"in r&&null!==(i=r.get(t,e))?i:t[e]},propHooks:{tabIndex:{get:function(t){return t.hasAttribute("tabindex")||ae.test(t.nodeName)||t.href?t.tabIndex:-1}}}}),K.optSelected||(tt.propHooks.selected={get:function(t){var e=t.parentNode;return e&&e.parentNode&&e.parentNode.selectedIndex,null}}),tt.each(["tabIndex","readOnly","maxLength","cellSpacing","cellPadding","rowSpan","colSpan","useMap","frameBorder","contentEditable"],function(){tt.propFix[this.toLowerCase()]=this});var le=/[\t\r\n\f]/g;tt.fn.extend({addClass:function(t){var e,n,i,r,o,s,a="string"==typeof t&&t,l=0,u=this.length;if(tt.isFunction(t))return this.each(function(e){tt(this).addClass(t.call(this,e,this.className))});if(a)for(e=(t||"").match(ft)||[];u>l;l++)if(n=this[l],i=1===n.nodeType&&(n.className?(" "+n.className+" ").replace(le," "):" ")){for(o=0;r=e[o++];)i.indexOf(" "+r+" ")<0&&(i+=r+" ");s=tt.trim(i),n.className!==s&&(n.className=s)}return this},removeClass:function(t){var e,n,i,r,o,s,a=0===arguments.length||"string"==typeof t&&t,l=0,u=this.length;if(tt.isFunction(t))return this.each(function(e){tt(this).removeClass(t.call(this,e,this.className))});if(a)for(e=(t||"").match(ft)||[];u>l;l++)if(n=this[l],i=1===n.nodeType&&(n.className?(" "+n.className+" ").replace(le," "):"")){for(o=0;r=e[o++];)for(;i.indexOf(" "+r+" ")>=0;)i=i.replace(" "+r+" "," ");s=t?tt.trim(i):"",n.className!==s&&(n.className=s)}return this},toggleClass:function(t,e){var n=typeof t;return"boolean"==typeof e&&"string"===n?e?this.addClass(t):this.removeClass(t):this.each(tt.isFunction(t)?function(n){tt(this).toggleClass(t.call(this,n,this.className,e),e)}:function(){if("string"===n)for(var e,i=0,r=tt(this),o=t.match(ft)||[];e=o[i++];)r.hasClass(e)?r.removeClass(e):r.addClass(e);else(n===At||"boolean"===n)&&(this.className&&yt.set(this,"__className__",this.className),this.className=this.className||t===!1?"":yt.get(this,"__className__")||"")})},hasClass:function(t){for(var e=" "+t+" ",n=0,i=this.length;i>n;n++)if(1===this[n].nodeType&&(" "+this[n].className+" ").replace(le," ").indexOf(e)>=0)return!0;return!1}});var ue=/\r/g;tt.fn.extend({val:function(t){var e,n,i,r=this[0];{if(arguments.length)return i=tt.isFunction(t),this.each(function(n){var r;1===this.nodeType&&(r=i?t.call(this,n,tt(this).val()):t,null==r?r="":"number"==typeof r?r+="":tt.isArray(r)&&(r=tt.map(r,function(t){return null==t?"":t+""})),e=tt.valHooks[this.type]||tt.valHooks[this.nodeName.toLowerCase()],e&&"set"in e&&void 0!==e.set(this,r,"value")||(this.value=r))});if(r)return e=tt.valHooks[r.type]||tt.valHooks[r.nodeName.toLowerCase()],e&&"get"in e&&void 0!==(n=e.get(r,"value"))?n:(n=r.value,"string"==typeof n?n.replace(ue,""):null==n?"":n)}}}),tt.extend({valHooks:{option:{get:function(t){var e=tt.find.attr(t,"value");return null!=e?e:tt.trim(tt.text(t))}},select:{get:function(t){for(var e,n,i=t.options,r=t.selectedIndex,o="select-one"===t.type||0>r,s=o?null:[],a=o?r+1:i.length,l=0>r?a:o?r:0;a>l;l++)if(n=i[l],!(!n.selected&&l!==r||(K.optDisabled?n.disabled:null!==n.getAttribute("disabled"))||n.parentNode.disabled&&tt.nodeName(n.parentNode,"optgroup"))){if(e=tt(n).val(),o)return e;s.push(e)}return s},set:function(t,e){for(var n,i,r=t.options,o=tt.makeArray(e),s=r.length;s--;)i=r[s],(i.selected=tt.inArray(i.value,o)>=0)&&(n=!0);return n||(t.selectedIndex=-1),o}}}}),tt.each(["radio","checkbox"],function(){tt.valHooks[this]={set:function(t,e){return tt.isArray(e)?t.checked=tt.inArray(tt(t).val(),e)>=0:void 0}},K.checkOn||(tt.valHooks[this].get=function(t){return null===t.getAttribute("value")?"on":t.value})}),tt.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "),function(t,e){tt.fn[e]=function(t,n){return arguments.length>0?this.on(e,null,t,n):this.trigger(e)}}),tt.fn.extend({hover:function(t,e){return this.mouseenter(t).mouseleave(e||t)},bind:function(t,e,n){return this.on(t,null,e,n)},unbind:function(t,e){return this.off(t,null,e)},delegate:function(t,e,n,i){return this.on(e,t,n,i)},undelegate:function(t,e,n){return 1===arguments.length?this.off(t,"**"):this.off(e,t||"**",n)}});var ce=tt.now(),he=/\?/;tt.parseJSON=function(t){return JSON.parse(t+"")},tt.parseXML=function(t){var e,n;if(!t||"string"!=typeof t)return null;try{n=new DOMParser,e=n.parseFromString(t,"text/xml")}catch(i){e=void 0}return(!e||e.getElementsByTagName("parsererror").length)&&tt.error("Invalid XML: "+t),e};var pe,de,fe=/#.*$/,ge=/([?&])_=[^&]*/,me=/^(.*?):[ \t]*([^\r\n]*)$/gm,ve=/^(?:about|app|app-storage|.+-extension|file|res|widget):$/,ye=/^(?:GET|HEAD)$/,be=/^\/\//,we=/^([\w.+-]+:)(?:\/\/(?:[^\/?#]*@|)([^\/?#:]*)(?::(\d+)|)|)/,xe={},Te={},Ce="*/".concat("*");try{de=location.href}catch(ke){de=Y.createElement("a"),de.href="",de=de.href}pe=we.exec(de.toLowerCase())||[],tt.extend({active:0,lastModified:{},etag:{},ajaxSettings:{url:de,type:"GET",isLocal:ve.test(pe[1]),global:!0,processData:!0,async:!0,contentType:"application/x-www-form-urlencoded; charset=UTF-8",accepts:{"*":Ce,text:"text/plain",html:"text/html",xml:"application/xml, text/xml",json:"application/json, text/javascript"},contents:{xml:/xml/,html:/html/,json:/json/},responseFields:{xml:"responseXML",text:"responseText",json:"responseJSON"},converters:{"* text":String,"text html":!0,"text json":tt.parseJSON,"text xml":tt.parseXML},flatOptions:{url:!0,context:!0}},ajaxSetup:function(t,e){return e?R(R(t,tt.ajaxSettings),e):R(tt.ajaxSettings,t)},ajaxPrefilter:P(xe),ajaxTransport:P(Te),ajax:function(t,e){function n(t,e,n,s){var l,c,v,y,w,T=e;2!==b&&(b=2,a&&clearTimeout(a),i=void 0,o=s||"",x.readyState=t>0?4:0,l=t>=200&&300>t||304===t,n&&(y=q(h,x,n)),y=H(h,y,x,l),l?(h.ifModified&&(w=x.getResponseHeader("Last-Modified"),w&&(tt.lastModified[r]=w),w=x.getResponseHeader("etag"),w&&(tt.etag[r]=w)),204===t||"HEAD"===h.type?T="nocontent":304===t?T="notmodified":(T=y.state,c=y.data,v=y.error,l=!v)):(v=T,(t||!T)&&(T="error",0>t&&(t=0))),x.status=t,x.statusText=(e||T)+"",l?f.resolveWith(p,[c,T,x]):f.rejectWith(p,[x,T,v]),x.statusCode(m),m=void 0,u&&d.trigger(l?"ajaxSuccess":"ajaxError",[x,h,l?c:v]),g.fireWith(p,[x,T]),u&&(d.trigger("ajaxComplete",[x,h]),--tt.active||tt.event.trigger("ajaxStop")))}"object"==typeof t&&(e=t,t=void 0),e=e||{};var i,r,o,s,a,l,u,c,h=tt.ajaxSetup({},e),p=h.context||h,d=h.context&&(p.nodeType||p.jquery)?tt(p):tt.event,f=tt.Deferred(),g=tt.Callbacks("once memory"),m=h.statusCode||{},v={},y={},b=0,w="canceled",x={readyState:0,getResponseHeader:function(t){var e;if(2===b){if(!s)for(s={};e=me.exec(o);)s[e[1].toLowerCase()]=e[2];e=s[t.toLowerCase()]}return null==e?null:e},getAllResponseHeaders:function(){return 2===b?o:null},setRequestHeader:function(t,e){var n=t.toLowerCase();return b||(t=y[n]=y[n]||t,v[t]=e),this},overrideMimeType:function(t){return b||(h.mimeType=t),this},statusCode:function(t){var e;if(t)if(2>b)for(e in t)m[e]=[m[e],t[e]];else x.always(t[x.status]);return this},abort:function(t){var e=t||w;return i&&i.abort(e),n(0,e),this}};if(f.promise(x).complete=g.add,x.success=x.done,x.error=x.fail,h.url=((t||h.url||de)+"").replace(fe,"").replace(be,pe[1]+"//"),h.type=e.method||e.type||h.method||h.type,h.dataTypes=tt.trim(h.dataType||"*").toLowerCase().match(ft)||[""],null==h.crossDomain&&(l=we.exec(h.url.toLowerCase()),h.crossDomain=!(!l||l[1]===pe[1]&&l[2]===pe[2]&&(l[3]||("http:"===l[1]?"80":"443"))===(pe[3]||("http:"===pe[1]?"80":"443")))),h.data&&h.processData&&"string"!=typeof h.data&&(h.data=tt.param(h.data,h.traditional)),F(xe,h,e,x),2===b)return x;u=h.global,u&&0===tt.active++&&tt.event.trigger("ajaxStart"),h.type=h.type.toUpperCase(),h.hasContent=!ye.test(h.type),r=h.url,h.hasContent||(h.data&&(r=h.url+=(he.test(r)?"&":"?")+h.data,delete h.data),h.cache===!1&&(h.url=ge.test(r)?r.replace(ge,"$1_="+ce++):r+(he.test(r)?"&":"?")+"_="+ce++)),h.ifModified&&(tt.lastModified[r]&&x.setRequestHeader("If-Modified-Since",tt.lastModified[r]),tt.etag[r]&&x.setRequestHeader("If-None-Match",tt.etag[r])),(h.data&&h.hasContent&&h.contentType!==!1||e.contentType)&&x.setRequestHeader("Content-Type",h.contentType),x.setRequestHeader("Accept",h.dataTypes[0]&&h.accepts[h.dataTypes[0]]?h.accepts[h.dataTypes[0]]+("*"!==h.dataTypes[0]?", "+Ce+"; q=0.01":""):h.accepts["*"]);for(c in h.headers)x.setRequestHeader(c,h.headers[c]);if(h.beforeSend&&(h.beforeSend.call(p,x,h)===!1||2===b))return x.abort();w="abort";for(c in{success:1,error:1,complete:1})x[c](h[c]);if(i=F(Te,h,e,x)){x.readyState=1,u&&d.trigger("ajaxSend",[x,h]),h.async&&h.timeout>0&&(a=setTimeout(function(){x.abort("timeout")},h.timeout));try{b=1,i.send(v,n)}catch(T){if(!(2>b))throw T;n(-1,T)}}else n(-1,"No Transport");return x},getJSON:function(t,e,n){return tt.get(t,e,n,"json")},getScript:function(t,e){return tt.get(t,void 0,e,"script")}}),tt.each(["get","post"],function(t,e){tt[e]=function(t,n,i,r){return tt.isFunction(n)&&(r=r||i,i=n,n=void 0),tt.ajax({url:t,type:e,dataType:r,data:n,success:i})}}),tt.each(["ajaxStart","ajaxStop","ajaxComplete","ajaxError","ajaxSuccess","ajaxSend"],function(t,e){tt.fn[e]=function(t){return this.on(e,t)}}),tt._evalUrl=function(t){return tt.ajax({url:t,type:"GET",dataType:"script",async:!1,global:!1,"throws":!0})},tt.fn.extend({wrapAll:function(t){var e;return tt.isFunction(t)?this.each(function(e){tt(this).wrapAll(t.call(this,e))}):(this[0]&&(e=tt(t,this[0].ownerDocument).eq(0).clone(!0),this[0].parentNode&&e.insertBefore(this[0]),e.map(function(){for(var t=this;t.firstElementChild;)t=t.firstElementChild;return t}).append(this)),this)},wrapInner:function(t){return this.each(tt.isFunction(t)?function(e){tt(this).wrapInner(t.call(this,e))}:function(){var e=tt(this),n=e.contents();n.length?n.wrapAll(t):e.append(t)})},wrap:function(t){var e=tt.isFunction(t);return this.each(function(n){tt(this).wrapAll(e?t.call(this,n):t)})},unwrap:function(){return this.parent().each(function(){tt.nodeName(this,"body")||tt(this).replaceWith(this.childNodes)}).end()}}),tt.expr.filters.hidden=function(t){return t.offsetWidth<=0&&t.offsetHeight<=0},tt.expr.filters.visible=function(t){return!tt.expr.filters.hidden(t)};var Ee=/%20/g,Ae=/\[\]$/,$e=/\r?\n/g,Se=/^(?:submit|button|image|reset|file)$/i,Ne=/^(?:input|select|textarea|keygen)/i;tt.param=function(t,e){var n,i=[],r=function(t,e){e=tt.isFunction(e)?e():null==e?"":e,i[i.length]=encodeURIComponent(t)+"="+encodeURIComponent(e)};if(void 0===e&&(e=tt.ajaxSettings&&tt.ajaxSettings.traditional),tt.isArray(t)||t.jquery&&!tt.isPlainObject(t))tt.each(t,function(){r(this.name,this.value)});else for(n in t)M(n,t[n],e,r);return i.join("&").replace(Ee,"+")},tt.fn.extend({serialize:function(){return tt.param(this.serializeArray())},serializeArray:function(){return this.map(function(){var t=tt.prop(this,"elements");return t?tt.makeArray(t):this}).filter(function(){var t=this.type;return this.name&&!tt(this).is(":disabled")&&Ne.test(this.nodeName)&&!Se.test(t)&&(this.checked||!Et.test(t))}).map(function(t,e){var n=tt(this).val();return null==n?null:tt.isArray(n)?tt.map(n,function(t){return{name:e.name,value:t.replace($e,"\r\n")}}):{name:e.name,value:n.replace($e,"\r\n")}}).get()}}),tt.ajaxSettings.xhr=function(){try{return new XMLHttpRequest}catch(t){}};var je=0,De={},_e={0:200,1223:204},Oe=tt.ajaxSettings.xhr();e.ActiveXObject&&tt(e).on("unload",function(){for(var t in De)De[t]()}),K.cors=!!Oe&&"withCredentials"in Oe,K.ajax=Oe=!!Oe,tt.ajaxTransport(function(t){var e;return K.cors||Oe&&!t.crossDomain?{send:function(n,i){var r,o=t.xhr(),s=++je;if(o.open(t.type,t.url,t.async,t.username,t.password),t.xhrFields)for(r in t.xhrFields)o[r]=t.xhrFields[r];t.mimeType&&o.overrideMimeType&&o.overrideMimeType(t.mimeType),t.crossDomain||n["X-Requested-With"]||(n["X-Requested-With"]="XMLHttpRequest");for(r in n)o.setRequestHeader(r,n[r]);e=function(t){return function(){e&&(delete De[s],e=o.onload=o.onerror=null,"abort"===t?o.abort():"error"===t?i(o.status,o.statusText):i(_e[o.status]||o.status,o.statusText,"string"==typeof o.responseText?{text:o.responseText}:void 0,o.getAllResponseHeaders()))}},o.onload=e(),o.onerror=e("error"),e=De[s]=e("abort");try{o.send(t.hasContent&&t.data||null)}catch(a){if(e)throw a}},abort:function(){e&&e()}}:void 0}),tt.ajaxSetup({accepts:{script:"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},contents:{script:/(?:java|ecma)script/},converters:{"text script":function(t){return tt.globalEval(t),t}}}),tt.ajaxPrefilter("script",function(t){void 0===t.cache&&(t.cache=!1),t.crossDomain&&(t.type="GET")}),tt.ajaxTransport("script",function(t){if(t.crossDomain){var e,n;return{send:function(i,r){e=tt("<script>").prop({async:!0,charset:t.scriptCharset,src:t.url}).on("load error",n=function(t){e.remove(),n=null,t&&r("error"===t.type?404:200,t.type)}),Y.head.appendChild(e[0])},abort:function(){n&&n()}}}});var Le=[],Ie=/(=)\?(?=&|$)|\?\?/;tt.ajaxSetup({jsonp:"callback",jsonpCallback:function(){var t=Le.pop()||tt.expando+"_"+ce++;return this[t]=!0,t}}),tt.ajaxPrefilter("json jsonp",function(t,n,i){var r,o,s,a=t.jsonp!==!1&&(Ie.test(t.url)?"url":"string"==typeof t.data&&!(t.contentType||"").indexOf("application/x-www-form-urlencoded")&&Ie.test(t.data)&&"data");return a||"jsonp"===t.dataTypes[0]?(r=t.jsonpCallback=tt.isFunction(t.jsonpCallback)?t.jsonpCallback():t.jsonpCallback,a?t[a]=t[a].replace(Ie,"$1"+r):t.jsonp!==!1&&(t.url+=(he.test(t.url)?"&":"?")+t.jsonp+"="+r),t.converters["script json"]=function(){return s||tt.error(r+" was not called"),s[0]},t.dataTypes[0]="json",o=e[r],e[r]=function(){s=arguments},i.always(function(){e[r]=o,t[r]&&(t.jsonpCallback=n.jsonpCallback,Le.push(r)),s&&tt.isFunction(o)&&o(s[0]),s=o=void 0}),"script"):void 0}),tt.parseHTML=function(t,e,n){if(!t||"string"!=typeof t)return null;"boolean"==typeof e&&(n=e,e=!1),e=e||Y;var i=at.exec(t),r=!n&&[];return i?[e.createElement(i[1])]:(i=tt.buildFragment([t],e,r),r&&r.length&&tt(r).remove(),tt.merge([],i.childNodes))};var Pe=tt.fn.load;tt.fn.load=function(t,e,n){if("string"!=typeof t&&Pe)return Pe.apply(this,arguments);var i,r,o,s=this,a=t.indexOf(" ");return a>=0&&(i=tt.trim(t.slice(a)),t=t.slice(0,a)),tt.isFunction(e)?(n=e,e=void 0):e&&"object"==typeof e&&(r="POST"),s.length>0&&tt.ajax({url:t,type:r,dataType:"html",data:e}).done(function(t){o=arguments,s.html(i?tt("<div>").append(tt.parseHTML(t)).find(i):t)}).complete(n&&function(t,e){s.each(n,o||[t.responseText,e,t])}),this},tt.expr.filters.animated=function(t){return tt.grep(tt.timers,function(e){return t===e.elem}).length};var Fe=e.document.documentElement;tt.offset={setOffset:function(t,e,n){var i,r,o,s,a,l,u,c=tt.css(t,"position"),h=tt(t),p={};"static"===c&&(t.style.position="relative"),a=h.offset(),o=tt.css(t,"top"),l=tt.css(t,"left"),u=("absolute"===c||"fixed"===c)&&(o+l).indexOf("auto")>-1,u?(i=h.position(),s=i.top,r=i.left):(s=parseFloat(o)||0,r=parseFloat(l)||0),tt.isFunction(e)&&(e=e.call(t,n,a)),null!=e.top&&(p.top=e.top-a.top+s),null!=e.left&&(p.left=e.left-a.left+r),"using"in e?e.using.call(t,p):h.css(p)}},tt.fn.extend({offset:function(t){if(arguments.length)return void 0===t?this:this.each(function(e){tt.offset.setOffset(this,t,e)});var e,n,i=this[0],r={top:0,left:0},o=i&&i.ownerDocument;if(o)return e=o.documentElement,tt.contains(e,i)?(typeof i.getBoundingClientRect!==At&&(r=i.getBoundingClientRect()),n=U(o),{top:r.top+n.pageYOffset-e.clientTop,left:r.left+n.pageXOffset-e.clientLeft}):r},position:function(){if(this[0]){var t,e,n=this[0],i={top:0,left:0};return"fixed"===tt.css(n,"position")?e=n.getBoundingClientRect():(t=this.offsetParent(),e=this.offset(),tt.nodeName(t[0],"html")||(i=t.offset()),i.top+=tt.css(t[0],"borderTopWidth",!0),i.left+=tt.css(t[0],"borderLeftWidth",!0)),{top:e.top-i.top-tt.css(n,"marginTop",!0),left:e.left-i.left-tt.css(n,"marginLeft",!0)}}},offsetParent:function(){return this.map(function(){for(var t=this.offsetParent||Fe;t&&!tt.nodeName(t,"html")&&"static"===tt.css(t,"position");)t=t.offsetParent;return t||Fe})}}),tt.each({scrollLeft:"pageXOffset",scrollTop:"pageYOffset"},function(t,n){var i="pageYOffset"===n;tt.fn[t]=function(r){return vt(this,function(t,r,o){var s=U(t);return void 0===o?s?s[n]:t[r]:void(s?s.scrollTo(i?e.pageXOffset:o,i?o:e.pageYOffset):t[r]=o)},t,r,arguments.length,null)}}),tt.each(["top","left"],function(t,e){tt.cssHooks[e]=C(K.pixelPosition,function(t,n){return n?(n=T(t,e),Bt.test(n)?tt(t).position()[e]+"px":n):void 0})}),tt.each({Height:"height",Width:"width"},function(t,e){tt.each({padding:"inner"+t,content:e,"":"outer"+t},function(n,i){tt.fn[i]=function(i,r){var o=arguments.length&&(n||"boolean"!=typeof i),s=n||(i===!0||r===!0?"margin":"border");return vt(this,function(e,n,i){var r;return tt.isWindow(e)?e.document.documentElement["client"+t]:9===e.nodeType?(r=e.documentElement,Math.max(e.body["scroll"+t],r["scroll"+t],e.body["offset"+t],r["offset"+t],r["client"+t])):void 0===i?tt.css(e,n,s):tt.style(e,n,i,s)},e,o?i:void 0,o,null)}})}),tt.fn.size=function(){return this.length},tt.fn.andSelf=tt.fn.addBack,"function"==typeof t&&t.amd&&t("jquery",[],function(){return tt});var Re=e.jQuery,qe=e.$;return tt.noConflict=function(t){return e.$===tt&&(e.$=qe),t&&e.jQuery===tt&&(e.jQuery=Re),tt},typeof n===At&&(e.jQuery=e.$=tt),tt})},{}],6:[function(t,e,n){arguments[4][3][0].apply(n,arguments)},{dup:3}],7:[function(t,e,n){var i=t("backbone"),r=t("../models/amp-menus-model.js");e.exports=i.Collection.extend({url:"/rest/security/menus",model:r,fetch:function(t){return t=t||{},t.cache=!1,i.Model.prototype.fetch.call(this,t)}})},{"../models/amp-menus-model.js":9,backbone:2}],8:[function(t,e,n){var i=t("backbone");e.exports=i.Model.extend({url:"/rest/security/layout",defaults:{email:void 0},fetch:function(t){return t=t||{},t.cache=!1,i.Model.prototype.fetch.call(this,t)}})},{backbone:2}],9:[function(t,e,n){var i=t("backbone");e.exports=i.Model.extend({url:"/rest/security/menus",defaults:{name:"Default",children:[]},fetch:function(t){return t=t||{},t.cache=!1,i.Model.prototype.fetch.call(this,t)}})},{backbone:2}],10:[function(t,e,n){var i=(t("jquery"),t("backbone")),r=t("underscore"),o='<div class="modal fade" id="about-popup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\r\n<div class="modal-dialog">\r\n <div class="modal-content">\r\n <div class="modal-header">\r\n        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span data-i18n="amp.dashboard:close" class="sr-only">Close</span></button>\r\n        <h4 class="modal-title text-primary" data-i18n="amp.about:modal.title">About AMP</h4>\r\n </div>\r\n<table width="474" border="0" style="margin:15px;">\r\n	<tbody><tr>\r\n		<td width="257">\r\n		<p align="center" style="font-size: 16px;"><strong>&nbsp;<span data-i18n="amp.common:platform">Aid Management Platform (AMP)</span></strong></p>\r\n		<p align="center"> Version <%= ampVersion %></p>\r\n		</td>\r\n		<td width="220"><img width="220" height="100" src="/TEMPLATE/ampTemplate/img_2/logo-development-gateway.png"></td>\r\n	</tr>\r\n	<tr>\r\n		<td colspan="2">\r\n		<p  style="font-size: 12px;">&nbsp;<span data-i18n="amp.common:platform">Aid Management Platform (AMP)</span>\r\n		 <span data-i18n="amp.about:version">Version</span> <%= ampVersion %> <%= buildDate %>\r\n		<span data-i18n="amp.about:credits">Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and Development Gateway Foundation</span>.\r\n		</p>\r\n		</td>\r\n	</tr>\r\n	<tr>\r\n		<td colspan="2">\r\n		<p style="font-size: 12px;">\r\n		<span data-i18n="amp.about:trademark">The Development Gateway and the The Development Gateway logo are trademarks for The Development Gateway Foundation</span>.<span data-i18n="amp.about:rights">All Rights Reserved</span>.</p>\r\n		</td>\r\n	</tr>\r\n</tbody></table>\r\n</div>\r\n</div>\r\n</div>';e.exports=i.View.extend({template:r.template(o),id:"aboutModal",initialize:function(t){this.app=t.app,r.bindAll(this,"render")},render:function(){var t=this;return r.defaults(window,{ampVersion:"0",buildDate:"0"}),this.$el.html(t.template({ampVersion:window.ampVersion,buildDate:window.buildDate})),this}})},{backbone:2,jquery:5,underscore:6}],11:[function(t,e,n){var i=t("jquery"),r=t("backbone"),o=t("underscore"),s='<style>\r\n    .footerText {\r\n    padding: 0;\r\n    font-family: arial;\r\n    font-size: 11px;\r\n    border: 0px;\r\n    }\r\n    .footer {\r\n    background-color: #8B8B8B;\r\n    color: #FFF;\r\n    margin-top: 0;\r\n    padding-bottom: 10px;\r\n    padding-top: 10px;\r\n    text-align: center;\r\n    }\r\n    .dgf_footer {\r\n    color: #8B8B8B;\r\n    line-height: 18px;\r\n    text-align: center;\r\n    background-color:white;\r\n    padding-top:10px;\r\n    }\r\n    .dgf_footer img {\r\n    line-height: 18px;\r\n    margin-bottom: 5px;\r\n    }\r\n</style>\r\n\r\n<div class="footer footerText">\r\n    AMP <b><%=  properties.ampVersion %></b> build <b><%=  properties.buildDate %></b> - <%= properties.footerText %>\r\n    <% if(showAdminLinks == true  && properties.adminLinks != undefined) { %>\r\n    <a href=\'<%=  properties.adminLinks[0].url %>\'><%=  properties.adminLinks[0].name %></a>\r\n    <a href=\'<%=  properties.adminLinks[1].url %>\'><%=  properties.adminLinks[1].name %></digi:trn></a>\r\n    <% } %>\r\n</div>\r\n<% if(showDGFooter == true)  { %>\r\n<div class="dgf_footer footerText">\r\n    <img src="/TEMPLATE/ampTemplate/img_2/dgf_logo_bottom.gif" class="dgf_logo_footer">\r\n    <br/>\r\n    Development Gateway\r\n    <br/>\r\n    1110 Vermont Ave, NW, Suite 500\r\n    <br/>\r\n    Washington, DC 20005 USA\r\n    <br/>\r\n    info@developmentgateway.org, Tel: +1.202.572.9200, Fax: +1 202.572.9290\r\n</div>\r\n<% } %>\r\n\r\n<% if(properties.trackingEnabled == true) { %>\r\n<!-- Piwik\r\nSite id can be checked here: http://stats.ampsite.net/index.php?module=SitesManager&action=index&idSite=1&period=range&date=last30\r\nAlso,the wiki for piwik: https://wiki.dgfoundation.org/display/AMPDOC/Integrating+AMP+with+Piwik\r\n-->\r\n<script type="text/javascript">\r\n	  var _paq = _paq || [];\r\n	  _paq.push(["trackPageView"]);\r\n	  _paq.push(["enableLinkTracking"]);\r\n	\r\n	  (function() {\r\n	    var u="<%=  properties.trackingUrl %>";\r\n	    _paq.push(["setTrackerUrl", u+"piwik.php"]);\r\n	    _paq.push(["setSiteId", "<%=properties.siteId %>"]);\r\n	    var d=document, g=d.createElement("script"), s=d.getElementsByTagName("script")[0]; g.type="text/javascript";\r\n	    g.defer=true; g.async=true; g.src=u+"piwik.js"; s.parentNode.insertBefore(g,s);\r\n	  })();\r\n	</script>\r\n<!-- End Piwik Code -->\r\n<% } %>\r\n',a=t("../models/amp-layout-model.js");
	
	e.exports=r.View.extend({model:null,template:o.template(s),el:"#amp-footer",layoutFetched:new i.Deferred,showAdminFooter:!0,showDGFooter:!0,initialize:function(t){this.showAdminFooter=t.showAdminFooter,this.showDGFooter=t.showDGFooter;var e=new a,n=this;e.fetch().then(function(t){n.model=t,window.buildDate=t.buildDate,window.ampVersion=t.ampVersion,n.render(),n.layoutFetched.resolve()}),this.layoutFetched.done(function(){n.render()}),o.bindAll(this,"render","refreshUserSection")},render:function(){if(this.model){this.refreshUserSection();var t=this;this.$el.html(this.template({properties:t.model,showAdminLinks:t.showAdminFooter,showDGFooter:t.showDGFooter}))}return this},refreshUserSection:function(){this.model.logged===!0&&i(".container-fluid",i("#amp-header")).toggleClass("ampUserLoggedIn"),this.model.email&&(i(".user-url").attr("href","javascript:showUserProfile("+this.model.userId+")"),i("#header-workspace",i("#amp-header")).text(this.model.workspace),i("#header-workspace",i("#amp-header")).prop("title",this.model.workspace),i("#header-name #header-first-name",i("#amp-header")).text(this.model.firstName),i("#header-name #header-last-name",i("#amp-header")).text(this.model.lastName))}})},{"../models/amp-layout-model.js":8,backbone:2,jquery:5,underscore:6}],12:[function(t,e,n){var i=t("backbone");t("bootstrap/dist/js/bootstrap");var r=t("underscore"),o='\n<style>\n  #amp-header .navbar-header>button.navbar-toggle {\n    /* fix contracted menu icon to not block map*/\n    padding: 2px;\n    margin: 4px;\n  }\n\n  #amp-header .container-fluid.ampUserLoggedIn ul.ampPublic {\n    display: none !important;\n  }\n\n  #amp-header .container-fluid.ampUserLoggedIn ul.ampUserLoggedIn {\n    display: inherit !important;\n  }\n\n  #amp-header .container-fluid ul.ampPublic {\n    display: inherit !important;\n  }\n\n  #amp-header .container-fluid ul.ampUserLoggedIn {\n    display: none !important;\n  }\n\n  div#amp-header nav div.container-fluid, div#amp-header nav div.navbar-collapse a {\n    font-size: 12px;\n  }\n\n  @media ( max-width : 1151px) {\n    div#amp-header ul.navbar-right #header-workspace-li {\n      overflow-y: hidden !important;\n      max-width: 11em;\n      max-height: 30px;\n      overflow-wrap: break-word;\n      min-width: 10em;\n    }\n    div#amp-header ul.navbar-right #header-name {\n      overflow-y: hidden !important;\n      max-width: 7em;\n      max-height: 30px;\n      overflow-wrap: break-word;\n      min-width: 5em;\n    }\n  }\n  /*If there is slightly more space, allow name and Workspace to be written out.*/\n  @media ( min-width : 1152px) {\n    div#amp-header ul.navbar-right #header-workspace-li {\n      overflow-y: hidden !important;\n      max-width: 20em;\n      max-height: 30px;\n      overflow-wrap: break-word;\n      min-width: 10em;\n    }\n    div#amp-header ul.navbar-right #header-name {\n      overflow-y: hidden !important;\n      max-width: 20em;\n      max-height: 30px;\n      overflow-wrap: break-word;\n      min-width: 5em;\n    }\n  }\n  /******/\n  .scrollable-menu {\n    height: auto;\n    max-height: 400px;\n    overflow-x: hidden;\n  }\n\n  .dropdown-submenu {\n    position: relative;\n  }\n\n  .dropdown-submenu>.dropdown-menu {\n    top: 0;\n    left: 100%;\n    margin-top: -6px;\n    margin-left: -1px;\n    -webkit-border-radius: 0 6px 6px 6px;\n    -moz-border-radius: 0 6px 6px 6px;\n    border-radius: 0 6px 6px 6px;\n  }\n\n  .dropdown-submenu:hover>.dropdown-menu {\n    display: block;\n  }\n\n  .dropdown-submenu>a:after {\n    display: block;\n    content: " ";\n    float: right;\n    width: 0;\n    height: 0;\n    border-color: transparent;\n    border-style: solid;\n    border-width: 5px 0 5px 5px;\n    border-left-color: #cccccc;\n    margin-top: 5px;\n    margin-right: -10px;\n  }\n\n  .dropdown-submenu:hover>a:after {\n    border-left-color: #ffffff;\n  }\n\n  .dropdown-submenu.pull-left {\n    float: none;\n  }\n\n  .dropdown-submenu.pull-left>.dropdown-menu {\n    left: -100%;\n    margin-left: 10px;\n    -webkit-border-radius: 6px 0 6px 6px;\n    -moz-border-radius: 6px 0 6px 6px;\n    border-radius: 6px 0 6px 6px;\n  }\n\n  @media ( max-width : 991px) {\n    #amp-header #header-name {\n      display: none;\n    }\n  }\n  .workspace-name {\n    max-width: 140px;\n    white-space: nowrap;\n    overflow: hidden;\n    text-overflow: ellipsis;\n  }\n</style>\n<script type="text/javascript">\n  function switchTranslation (url) {\n    $(\'#backUrl\').val(document.location.href);\n    document.modeSwitchForm.action = url;\n    document.modeSwitchForm.submit();\n  }\n</script>\n<form name="modeSwitchForm" method="post" action="/translation/switchMode.do" style="display:none;">\n  <input type="hidden" name="backUrl" id="backUrl" value="">\n</form>\n<nav class="navbar navbar-default" role="navigation">\n  <div class="container-fluid">\n    <div class="row">\n      <!-- .ampUserLoggedIn hides public version-->\n\n      <!-- Brand and toggle get grouped for better mobile display -->\n      <div class="navbar-header col-sm-3">\n        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">\n          <span class="sr-only">Toggle navigation</span>\n          <span class="icon-bar"></span>\n          <span class="icon-bar"></span>\n          <span class="icon-bar"></span>\n        </button>\n        <a class="navbar-brand" href="/" data-i18n="[title]amp.common:platform">\n          <span>\n            <img class="flag" src="/aim/default/displayFlag.do" height="20" width="30">&nbsp; &nbsp;\n          </span>\n          <span data-i18n="amp.common:platform-short">AMP</span></a>\n      </div>\n\n      <div class="col-sm-9">\n        <div class="collapse navbar-collapse row" id="bs-example-navbar-collapse-1">\n          <div class="col-sm-9">\n            <ul id="AmpMenus" class="nav navbar-nav menus">\n\n              <!-- File -->\n\n            </ul>\n            <div class="clearfix"></div>\n          </div>\n          <div class="col-sm-3">\n            <ul class="nav navbar-nav ampUserLoggedIn">\n              <li id="header-workspace-li"><a title="Workspace" class="workspace-name" id="header-workspace">&nbsp;</a></li>\n              <li id="header-name">\n                <a class="user-url">\n                  <span id="header-first-name">&nbsp;</span>&nbsp;<span id="header-last-name">&nbsp;</span>\n                </a>\n              </li>\n              <li id="header-logout"><a data-i18n="amp.common:title-logout" href="/aim/j_spring_logout" >Logout</a></li>\n            </ul>\n            <% if(showLogin) { %>\n              <% if(loginDropdown){ %>\n                <div class="login_here" id="show_login_pop">\n                  <div class="login_here_cont">\n                    <a data-i18n="amp.common:title-login" href="javascript:void(0)">Login</a>\n                  </div>\n                </div>\n              <% } else { %>\n                <ul class="nav navbar-nav ampPublic navbar-right">\n                  <li id="header-login"><a data-i18n="amp.common:title-login" href="/login.do" >Login</a></li>\n                </ul>\n              <% } %>\n            <% } %>\n          </div>\n        </div>\n      </div>\n\n    </div>\n    <!-- Collect the nav links, forms, and other content for toggling -->\n  </div><!-- /.container-fluid -->\n</nav>\n\n',s=t("../collections/amp-menus-collection.js"),a=(t("../models/amp-menus-model.js"),t("./submenu-compositeview.js")),l=t("./about-view.js");e.exports=i.View.extend({el:"#amp-header",appendEl:"#AmpMenus",template:r.template(o),menuRendered:new $.Deferred,events:{"click #show_login_pop":"openLoginBox"},initialize:function(t){this.collection=new s,this.translator=t.translator,this.showLogin=t.showLogin,this.loginDropdown=!!t.loginDropdown;var e=this;this.collection.fetch().then(function(){e.render()}),this.firstRender=!0,this.about=new l(t),r.bindAll(this,"addOne","addAll","showAbout")},addAll:function(){this.collection.each(this.addOne),this.menuRendered.resolve()},addOne:function(t){view=new a({model:t}),this.listenTo(view,"showAbout",this.showAbout);var e=this;this.listenTo(view,"switchLanguage",function(t){e.translator.setLanguage(t.language).always(function(){location.reload()})}),view.render(),$(this.appendEl).append(view.el)},render:function(){var t=this;return this.firstRender&&(this.$el.html(this.template({showLogin:t.showLogin,loginDropdown:t.loginDropdown})),this.addAll(),this.firstRender=!1,$("#show_login_pop_box").insertBefore("#show_login_pop"),$("#logincontainer").insertAfter("#show_login_pop")),this},showAbout:function(){return 0==$("#about-popup").length&&this.$el.parent().append(this.about.render().el),"function"!=typeof $().modal&&$.noConflict(),$("#about-popup").modal({show:!0,backdrop:!1}),this.translator.translateDOM($("#about-popup")[0]),!1},openLoginBox:function(){$("div#show_login_pop_box").show(),$("#j_username").focus()}})},{"../collections/amp-menus-collection.js":7,"../models/amp-menus-model.js":9,"./about-view.js":10,"./submenu-compositeview.js":13,backbone:2,"bootstrap/dist/js/bootstrap":4,underscore:6}],13:[function(t,e,n){var i=t("backbone"),r=t("underscore"),o='<%\n  /* Prepare the A tag at top level menus */\n  if (!obj.url) {\n      obj.url = \'#\';\n      obj.className = \'dropdown-toggle\';\n      obj.dataToggle = \'data-toggle="dropdown"\';\n    }\n    else {\n      obj.className = \'\';\n      obj.dataToggle = \'\';\n    }\n\n    if (obj.tab) {\n      obj.tabTarget = \'target="_blank"\';\n    }\n    else {\n      obj.tabTarget = \'\';\n    }\n    %>\n\n<a href="<%= url %>" class="<%= className %>" <%= dataToggle %> <%= obj.tabTarget %> >\n<%= obj.name.trim() %><% if (obj.children && obj.children.length > 0) { %>&nbsp;<span class="caret"></span><% } %>\n</a>\n<% if (obj.children && obj.children.length !== 0) { %>\n  <ul class="children dropdown-menu" role="menu">\n  <% _.each(obj.children, function(model) { %>\n\n    <%\n      /* Prepare the A tag at second level menus */\n      if (model.children && model.children.length !== 0) {\n        model.dropdownSubmenu = \'dropdown-submenu\';\n       };\n\n       if (!model.tooltip) {\n         model.tooltip = \'\';\n       };\n       var elementClass = \'2nd-level-item\';\n       if (model.language) {\n			 elementClass = elementClass+ \' language\';\n	   }\n       if (model.popup) {\n       	 elementClass = elementClass + \' popup\';\n       }\n       if (model.tab) {\n         model.tabTarget = \'target="_blank"\';\n       } else {\n         model.tabTarget = \'\';\n       }\n       var elementUrl = model.url;\n       if (model.post === true) {\n         elementUrl = \'javascript:switchTranslation ("\'+elementUrl+\'")\';\n      \n       }\n       if (!model.url) {\n        elementUrl = \'javascript:;\';\n       }\n       /* if it doesn\'t have children, nor url. then it is the About */\n       if (!model.url && !model.children) {\n       elementClass = elementClass + \' about-amp\';\n       }\n    %>\n\n    <li class=\'dropdown <%= model.dropdownSubmenu %>\' >\n    <a href=\'<%=  elementUrl %>\' title="<%= model.tooltip %>" onclick="return canExit()" class="<%= elementClass %>" <%= model.tabTarget %> ><%= model.name %></a>\n\n    <%\n      if (model.children && model.children.length !== 0) {\n     %>\n\n      <ul class="children dropdown-menu scrollable-menu" role="menu">\n      <% _.each(model.children, function(submodel) { %>\n\n        <%\n          /* Prepare the A tag at third level menus */\n          if (!submodel.url) {\n            submodel.url = \'#\';\n          };\n		  var itemClass = \'3rd-level-item\';\n		  if (submodel.language) {\n			 itemClass = itemClass+ \' language\';\n		   }\n		  if (!submodel.tooltip) {\n            submodel.tooltip = \'\';\n          };\n\n          if (submodel.tab) {\n            submodel.tabTarget=\'target="_blank"\';\n          } else {\n            submodel.tabTarget = \'\';\n          };\n        %>\n\n        <li class="menu-item"><a href="<%= submodel.url %>" onclick="return canExit()" class="<%= itemClass %>" <%= submodel.tabTarget %> title="<%= submodel.tooltip %>"><%= submodel.name %></a></li>\n\n      <% }) %>\n      </ul>\n    <% } %>\n\n    </li>\n\n  <% }); %>\n  </ul>\n<% }%>\n<!--\n<li class="menu-item dropdown dropdown-submenu"><a class="dropdown-toggle" data-toggle="dropdown">\n<ul class="children dropdown-menu scrollable-menu" role="menu">\n      <li class="menu-item first-of-type" id="yui-gen4" groupindex="0" index="0">\n  <a class="yuiampmenuitemlabel yuimenuitemlabel" href="/selectTeam.do?id=680" onclick="return canExit()">\n    Academy of Sciences\n  </a>\n</li>--!>\n\n';e.exports=i.View.extend({tagName:"li",className:"dropdown",events:{"click .language":"switchLanguage","click .popup":"openPopup","click .about-amp":"about"},template:r.template(o),initialize:function(t){r.bindAll(this,"render","switchLanguage","about")},render:function(){var t=this;return this.$el.html(this.template(t.model.attributes)),this},switchLanguage:function(t){var e=t.currentTarget.href.lastIndexOf("/"),n=t.currentTarget.href.substr(e+1);if("undefined"!=typeof SwitchLanguageMenu&&$.isFunction(SwitchLanguageMenu)){t.preventDefault();var i="/translation/switchLanguage.do?code="+n+"&rfr=";SwitchLanguageMenu(i)}else this.trigger("switchLanguage",{language:n})},openPopup:function(t){window.name="opener"+(new Date).getTime();var e=768,n=1024,i=(screen.width-768)/2,r=(screen.height-1024)/2,o="height="+n+",width="+e+",top="+r+",left="+i+",menubar=no,scrollbars=yes";return popupPointer=window.open(t.currentTarget.href,"forumPopup",o),t.preventDefault(),popupPointer},about:function(t){return this.trigger("showAbout",{}),!1}})},{backbone:2,underscore:6}],14:[function(t,e,n){function i(t){"use strict";if(!(this instanceof i))throw new Error("Translator needs to be created with the `new` keyword.");var e={defaultKeys:{},availableLanguages:null,translations:{locales:{en:null}},ajax:o.ajax};t&&r.defaults(this,t,e),this._promise=null,this._currentLng="tmp",this._firstGet=null,this.initTranslations=function(){var t=this;this._promise=this.getTranslations(t.defaultKeys).fail(function(t,e,n){console.error("failed ",t,e,n)}),this.promise=this._promise.promise()},this.addTranslatorOptions=function(t){r.defaults(this.defaultKeys,t.defaultKeys),this._firstGet=null},this.getAvailableLanguages=function(){var t=o.Deferred();return this.availableLanguages?t.resolve(this.availableLanguages):this._initAvailableLanguages().then(function(){t.resolve(this.availableLanguages)}),t},this._initAvailableLanguages=function(){return this.availableLanguages=new s.Collection([]),this.availableLanguages.url="/rest/translations/languages",this.availableLanguages.fetch()},this.setLanguage=function(t){return this._currentLng=t,this._apiCall("/rest/translations/languages/"+t,null,"GET")},this.translateDOM=function(t){var e=o(t);return this.getTranslations().then(function(t){return o.each(t,function(t,n){t.indexOf("[placeholder]")>-1?o('[data-i18n="'+t+'"]',e).attr("placeholder",n):t.indexOf("[title]")>-1?o('[data-i18n="'+t+'"]',e).attr("title",n):o('[data-i18n="'+t+'"]',e).text(n)}),e})},this.translateList=function(t){var e=function(t,e){return r.each(t,function(t,n,i){i[n]=e[n]?e[n]:n[t]}),t};return this.getTranslations().then(function(n){var i=e(t,n);return i})},this.translateSync=function(t,e){"resolved"!==this.getTranslations().state()&&console.error("translateSync was called when getTranslations is not successfully resolved");var n=this.translations.locales[this._currentLng][t];return void 0===n&&(console.warn("No translation has been loaded for",t),n=e||t),n},this.getTranslations=function(){return this._firstGet||(this._firstGet=this._getTranslationsFromAPI(this.defaultKeys,this._currentLng)),this._firstGet},this._getTranslationsFromAPI=function(t,e){var n=this,i="/rest/translations/label-translations";return this._apiCall(i,t,"POST").then(function(t){return e?n.translations.locales[e]=t:(e=this._currentLng,n.translations.locales[e]=t),t})},this._apiCall=function(t,e,n){var i={headers:{Accept:"application/json","Content-Type":"application/json"},type:n,url:t,dataType:"json"};return e&&(i.data=JSON.stringify(e)),this.ajax(i)},this.initTranslations()}var r=t("underscore"),o=t("jquery"),s=t("backbone");e.exports=i},{backbone:15,jquery:16,underscore:17}],15:[function(t,e,n){arguments[4][2][0].apply(n,arguments)},{dup:2,underscore:17}],16:[function(t,e,n){arguments[4][5][0].apply(n,arguments)},{dup:5}],17:[function(t,e,n){arguments[4][3][0].apply(n,arguments)},{dup:3}]},{},[1])(1)});

/***/ }
/******/ ]);
//# sourceMappingURL=script.js.map