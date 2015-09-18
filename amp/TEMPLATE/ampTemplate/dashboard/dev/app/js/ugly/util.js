// hopefully not that ugly, but seemed as good a place as any for this stuff...

var d3 = require('d3-browserify');


var formatKMB = function(precision, decimalSeparator) {
  var formatSI = d3.format('.' + (precision || 3) + 's');
  decimalSeparator = decimalSeparator || '.';
  return function(value) {
    return formatSI(value)
      .replace('k', app.translator.translateSync('amp.dashboard:chart-thousand'))
      .replace('M', app.translator.translateSync('amp.dashboard:chart-million'))
      .replace('G', app.translator.translateSync('amp.dashboard:chart-billion'))  // now just need to convert G Gigia -> B Billion
      .replace('T', app.translator.translateSync('amp.dashboard:chart-trillion'))
      .replace('P', app.translator.translateSync('amp.dashboard:chart-peta'))
	  .replace('E', app.translator.translateSync('amp.dashboard:chart-exa'))
      .replace('.', decimalSeparator);
  };
};

var translateLanguage = function(value) {
    return value
      .replace('k', app.translator.translateSync('amp.dashboard:chart-thousand'))
      .replace('M', app.translator.translateSync('amp.dashboard:chart-million'))
      .replace('B', app.translator.translateSync('amp.dashboard:chart-billion'))
      .replace('T', app.translator.translateSync('amp.dashboard:chart-trillion'))
      .replace('P', app.translator.translateSync('amp.dashboard:chart-peta'))
      .replace('E', app.translator.translateSync('amp.dashboard:chart-exa'));
};

var formatShortText = function(maxWidth) {
  var ellipseWidth = 1;
  return function(text) {
    if (text.length - ellipseWidth > maxWidth) {
      text = text.slice(0, maxWidth - ellipseWidth) + '...';
    }
    return text;
  };
};


var categoryColours = function(cats) {
  // get an appropriate colour scale for the number of categories we are
  // dealing with
  var colours = d3.scale['category20']().range();
  return function(d, i) {
    return d.color || (d.data && d.data.color) || colours[i % colours.length];
  };
};


var u16le64 = function(str) {
  // base64-encode a string as UTF-16-LE (for MS Excel, probably). It will only
  // work for 2-byte-wide utf-16 characters, and will break at the first hint
  // of any 4-byte char. Two bytes covers the Basic Multiningual Plane, so we
  // should be good.
  var u16num,
      asciiBytePairString = String.fromCharCode(0xFF) + String.fromCharCode(0xFE);
  asciiBytePairString += Array.prototype.reduce.call(str, function(acc, chr) {
    u16num = chr.charCodeAt(0);
    /* jshint bitwise:false */
    return acc + String.fromCharCode(u16num & 0xFF) + String.fromCharCode(u16num >> 8);
    /* jshint bitwise:true */
  }, '');
  return btoa(asciiBytePairString);
};


var textAsDataURL = function(str) {
  return 'data:text/plain;base64,' + u16le64(str);
};


function transformArgs(transformer, wrapped) {
  return function(/* arguments */) {
    var transformedArgs = transformer.apply(null, arguments);
    return wrapped.apply(null, transformedArgs);
  };
}


function toDashed(name) {
  // transform namesLikeThis to names-like-this
  return name.replace(/([A-Z])/g, function(u) {
    return '-' + u.toLowerCase();
  });
}


function data(el, name, newValue) {
  if (newValue === void 0) {
    return el.getAttribute('data-' + toDashed(name));
  }
  el.setAttribute('data-' + toDashed(name), newValue);
}


module.exports = {
  formatKMB: formatKMB,
  translateLanguage: translateLanguage,
  formatShortText: formatShortText,
  categoryColours: categoryColours,
  u16le64: u16le64,
  textAsDataURL: textAsDataURL,
  transformArgs: transformArgs,
  data: data
};
