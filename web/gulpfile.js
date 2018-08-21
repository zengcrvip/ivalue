/**
 * 自动化js,css文件压缩，加上MD5版本号脚本
 * Created by zengcr on 2017/7/17.
 */
var gulp = require('gulp'),
    rev = require('gulp-rev'),
    cleancss = require('gulp-clean-css'),
    uglify = require('gulp-uglify'),
    concat = require('gulp-concat'),
    sourcemaps = require('gulp-sourcemaps'),
    del = require('del'),
    revreplace = require('gulp-rev-replace'),
    runSequence = require('run-sequence');

var buildPath = {
    css:'target/icompaign/styles',
    js:'target/icompaign/js',
    html:'target/icompaign/pages',
    res:'target/icompaign'
}

var srcPath = {
    css:'src/main/webapp/styles',
    js:'src/main/webapp/js',
    html:'src/main/webapp/pages',
    res:['src/main/webapp/login.html','src/main/webapp/index.html']
}

gulp.task('clean',function(){
    del(['rev-manifest.json',
        buildPath.css + '/**/*.*',
        buildPath.js + '/**/*.*',
        buildPath.html + '/**/*.*'
    ],function(err, deletedFiles) {
        console.log('Files deleted:\n', deletedFiles.join('\n'));
    });
})

gulp.task('css-min', function() {
    return gulp.src(srcPath.css + '/**/*.*')
        //.pipe(sourcemaps.init())  //映射文件初始化
        //.pipe(concat({path:'juhe.min.css', cwd: ''}))  //合并文件
        .pipe(cleancss())  //压缩文件
        .pipe(rev())  //MD5版本号
        //.pipe(sourcemaps.write('.'))  //写入映射文件
        .pipe(gulp.dest(buildPath.css))  //写入build文件夹
        .pipe(rev.manifest())  //记录MD5版本号信息
        .pipe(gulp.dest(''));
});

gulp.task('js-min', function() {
    return gulp.src(srcPath.js + '/**/*.*')
        //.pipe(sourcemaps.init())
        //.pipe(concat({path:'juhe.min.js', cwd: ''}))
        .pipe(uglify())
        .pipe(rev())
        //.pipe(sourcemaps.write('.'))
        .pipe(gulp.dest(buildPath.js))
        .pipe(rev.manifest({base:'', merge: true}))
        .pipe(gulp.dest(''));
});

gulp.task("html-inject",function() {
    var manifest = gulp.src("rev-manifest.json");
    gulp.src( srcPath.html+"/**/*.html")
        .pipe(revreplace({replaceInExtensions: ['.html'], manifest: manifest}))
        .pipe(gulp.dest(buildPath.html));
});

gulp.task("res-inject",function() {
    var manifest = gulp.src("rev-manifest.json");
    gulp.src( srcPath.res)
        .pipe(revreplace({replaceInExtensions: ['.html'], manifest: manifest}))
        .pipe(gulp.dest(buildPath.res));
});

gulp.task('build',function(){
    runSequence('clean',['css-min','js-min'],['html-inject','res-inject']);
})
