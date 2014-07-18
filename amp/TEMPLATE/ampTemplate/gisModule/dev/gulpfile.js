/*
 * AMP Frontend Gulpfile
 *
 * Usage:
 *
 * $ gulp
 * or
 * $ gulp dev
 *    Unoptimized build of js/css, runs a dev server on :3000 with livereload
 *
 * $ gulp test
 *    Run unit tests -- currently broken
 *
 * $ gulp lint
 *    Lint javascript and css -- currently only js
 *
 * $ gulp build
 *    Build, optimize, copy everything needed to ../dist, revision files.
 *
 * $ gulp preview
 *    Builds and then launches a local server on :3000 to check it out
 *
 */

var rimraf = require('rimraf');
var source = require('vinyl-source-stream');
var browserify = require('browserify');
var watchify = require('watchify');
var gulp = require('gulp');
var g = require('gulp-load-plugins')();


var paths = {
  app: {
    root: './app',
    rootstuff: './app/*.{png,jpg,ico,html,txt,xml}',
    scripts: {
      top: './app/js/*.js',
      amp: './app/js/amp/**/*.js',
      entry: './app/js/amp/app.js',
      libs: './app/js/libs/',
      buildDest: './app/compiled-js/',
      built: './app/compiled-js/app.js'
    },
    stylesheets: {
      all: './app/less/**/*.less',
      entry: './app/less/main.less',
      libs: './node_modules/leaflet/dist/**/*.css',
      compiledDest: './app/compiled-css/',
      compiled: './app/compiled-css/main.css'
    },
    mockAPI: {
      entry: './app/mock-api/fakeServer.js',
      compiledDest:'./app/compiled-js/',
      compiled:'./app/compiled-js/mock-api.js',
    },    
    images: './app/img/**/*.{png,jpg, gif}',
    fonts: './app/fonts/**/*.{eot,svg,ttf,woff}'
  },
  dist: {
    root: '../dist/',  // USE CAUTION IF CHANGING (watch out for clean)
    scripts: '../dist/compiled-js/',
    stylesheets: '../dist/compiled-css/',
    images: '../dist/img/',
    fonts: '../dist/fonts/',
    mockAPI: '../dist/mock-api/'
  },
  tests: {
    root: './app/test',
    css: './node_modules/qunitjs/qunit/qunit.css',
    qunit: './node_modules/qunitjs/qunit/*.*',
    compiled: './app/test/compiled/compiled-test.js',
    compiledDest: './app/test/compiled/',
    entry: './app/test/entry.js',
    scripts: './app/test/scripts/*.js'
  }
};


//------------------------------------
// helpers 
//------------------------------------

function _bundlify(ifyer, entry, destFolder, destName) {
  var bundler = ifyer(entry);
  bundler.transform('brfs');

  var rebundle = function() {
    g.util.log('rebrowserifying ' + entry + '....');
    return bundler.bundle({debug: true})
      .on('error', function(e) { g.util.log('Browserify error: ', e); })
      .pipe(source(destName))
      .pipe(gulp.dest(destFolder));
  };

  bundler.on('update', rebundle);

  return rebundle();
}

gulp.task('bundle-mock',  function() {
  return _bundlify(browserify, paths.app.mockAPI.entry,
                   paths.app.mockAPI.compiledDest, 'mock-api.js');
});


gulp.task('watchify', function() {
  // recompile browserify modules
  return _bundlify(watchify, paths.app.scripts.entry,
                   paths.app.scripts.buildDest, 'app.js');
});


gulp.task('browserify', function() {
  return _bundlify(browserify, paths.app.scripts.entry,
                   paths.app.scripts.buildDest, 'app.js');
});


gulp.task('less', function() {
  return gulp.src([paths.app.stylesheets.libs, paths.app.stylesheets.entry])
    .pipe(g.plumber())
    .pipe(g.less())
      .on('error', g.util.log)
      .on('error', g.util.beep)
    .pipe(g.concat('main.css'))
    .pipe(gulp.dest(paths.app.stylesheets.compiledDest));
});


gulp.task('watch', ['watchify'], function() {
  gulp.watch([paths.app.scripts.top, paths.app.scripts.amp], ['lint']);
  gulp.watch(paths.app.stylesheets.all, ['less']);
});


gulp.task('lint', function() {
  gulp.src([paths.app.scripts.amp, paths.app.scripts.top])
    .pipe(g.jshint())
    .pipe(g.jshint.reporter('jshint-stylish'));

  // TODO: Lint CSS.
  //  * RECESS: Turns out bootstrap 3 dropped RECESS, which now fails.
  //  * csslint: kind of explodes on bootstrap's style... can it be made to
  //    ignore bootstrap stuff?
  //  * csscomb: not a linter so much as a property re-organizer; bootstrap now
  //    uses -- blog.getboostrap.com/2014/01/30/boostrap-3-1-0-released
  //    Unclear exactly how csscomb would fit in.
});


gulp.task('clean', function(done) {
  rimraf(paths.dist.root, done);
});




//------------------------------------
// build for dist 
//------------------------------------
gulp.task('build', ['clean', 'build-js', 'build-css', 'copy-stuff']);


gulp.task('build-js', ['clean', 'browserify'], function() {
  return gulp.src(paths.app.scripts.built)
    .pipe(g.streamify(g.uglify))
    .pipe(gulp.dest(paths.dist.scripts));
});


gulp.task('build-css', ['clean', 'less'], function() {
  return gulp.src(paths.app.stylesheets.compiled)
    .pipe(g.csso())
    .pipe(gulp.dest(paths.dist.stylesheets));
});


gulp.task('copy-stuff', ['clean', 'build-index'], function() {
  gulp.src(paths.app.rootstuff).pipe(gulp.dest(paths.dist.root));
  gulp.src(paths.app.images).pipe(gulp.dest(paths.dist.images));
  gulp.src(paths.app.fonts).pipe(gulp.dest(paths.dist.fonts));
});

gulp.task('build-index',  function(){
  return gulp.src('app/src-index.html')
            .pipe(g.rename('index.html'))
            .pipe(gulp.dest('app/'));
});

gulp.task('revision', ['clean', 'build-js', 'build-css'], function() {
  var versionableGlob = '**/*.{js,css}';
  var antiHtmlFilter =  g.filter(versionableGlob);  // so we can avoid versioning html
  gulp.src([ paths.dist.root + versionableGlob,
             paths.dist.root + '**/*.html' ])
    .pipe(antiHtmlFilter)
    .pipe(g.rimraf({ force: true }))
    .pipe(g.rev())
    .pipe(gulp.dest(paths.dist.root))
    .pipe(antiHtmlFilter.restore())
    .pipe(g.revReplace())
    .pipe(gulp.dest(paths.dist.root));
});


gulp.task('preview', ['build'], g.serve({
  root: [paths.dist.root],
  port: 3000
}));




//------------------------------------
// dev 
//------------------------------------
gulp.task('default', ['dev']);
gulp.task('dev', ['lint', 'less', 'dev-server', 'watch', 'reload', 'dev-index']);


gulp.task('dev-server', g.serve({
  root: [paths.app.root],
  port: 3000
}));


gulp.task('reload', ['dev-server', 'watch'], function() {
 g.livereload.listen();
 return gulp.watch([
    paths.app.rootstuff,
    paths.app.stylesheets.compiled,
    paths.app.scripts.built,
    paths.app.images,
    paths.app.fonts
  ]).on('change', g.livereload.changed);
});


// takes src-index.html, injects mockAPI 
gulp.task('dev-index', ['bundle-mock'], function(){
  return gulp.src('app/src-index.html')
            .pipe(
                g.inject(
                  gulp.src(paths.app.mockAPI.compiled, {read:false}), {
                        addRootSlash: false,  // ensures proper relative paths
                        ignorePath: 'app/'  // makes sure relative path is correct
                    }))
            .pipe(g.rename('index.html'))
            .pipe(gulp.dest('app/'));
});



//------------------------------------
// test 
//------------------------------------

gulp.task('dev-tests', ['test', 'serve-tests', 'reload-tests']);

gulp.task('test', function() {
  return _bundlify(browserify, paths.tests.entry,
                   paths.tests.compiledDest, 'compiled-test.js');
});

gulp.task('serve-tests', ['test'], g.serve({
    root: [paths.tests.root],
    port: 3000
}));


gulp.task('reload-tests', ['test'], function() {
  gulp.watch(paths.tests.scripts, ['test']);
  gulp.watch(paths.tests.compiled)
    .on('change', g.livereload.changed);
});

