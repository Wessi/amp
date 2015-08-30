const MIN_YEAR = 1970;
const MAX_YEAR = 2050;
const KEY_ENTER = 13;

var keyCode = e => "undefined" != typeof e.which ? e.which : e.keyCode;

var isSpecialKey = e => e.altKey || e.shiftKey || e.ctrlKey || keyCode(e) == KEY_ENTER;

var char = e => String.fromCharCode(keyCode(e));

export var keyPressEventToString = e => {
  var theChar = char(e), input = e.target, str = input.value;
  return str.slice(0, input.selectionStart) + theChar + str.slice(input.selectionEnd);
};

export var allow = validator => e => {
  if(!isSpecialKey(e) && !validator(keyPressEventToString(e))) e.preventDefault();
};

export var number = maybeNumber => parseFloat(maybeNumber) == maybeNumber;

export var negative = validator => maybeMinusSign => validator(maybeMinusSign) || "-" == maybeMinusSign;

export var point = validator => maybePoint => validator(maybePoint) || "." == maybePoint;

var takeDigits = n => number => String(number).substr(0, n + 1);

export var inBounds = _from => to => inputVal => {
  var nrDigits = inputVal.length - 1;
  var floatVal = parseFloat(inputVal);
  return takeDigits(nrDigits)(_from) <= floatVal && floatVal <= takeDigits(nrDigits)(to);
};

export var year = inBounds(MIN_YEAR)(MAX_YEAR);