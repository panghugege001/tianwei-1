/*!
 * Gulp 插件
 * $ npm install gulp-ruby-sass gulp-autoprefixer gulp-minify-css gulp-jshint gulp-concat gulp-uglify gulp-imagemin gulp-notify gulp-rename gulp-livereload gulp-cache gulp-clean --save-dev
 * */
var gulp = require('gulp'),
    //livereload = require('gulp-livereload'),   // npm install  gulp-livereload --save-dev
    child_process=require('child_process'),  // nodejs 调用命令模块
    browserSync = require('browser-sync').create(),
    uglify = require('gulp-uglify'), //npm install --save-dev gulp-uglify
    jsonminify = require('gulp-jsonminify'),
    gulpif = require('gulp-if'),
    sprity = require('sprity'),
    pump = require('pump'),
    cleanCSS=require('gulp-clean-css'),
    csscomb = require('gulp-csscomb'),
    configPath={
        basepath:'../bw/',
        css:'./css/**/*.css',
        js:'./js/**/*.js',
        images:'./images/**/*',
        bat:'/Users/wing/bat/lh.sh'
    };


/*自动部署到测试服务器
 * =================*/
var openApp=function(){
    child_process.execFile(configPath.bat,null,function(error,stdout,stderr){
        if(error!==null){
            console.log('exec error:'+error);
        }
        else{
            console.log('【====================执行====================】\n'+stdout);
            //console.log('【====================结果====================】\n'+stderr);
            console.log('【====================执行成功=================】');
        }

    });
};

// 文件监听
gulp.task('watch', function() {
    openApp();

    // 看守所有位在 dist/  目录下的档案，一旦有更动，便进行重整
    gulp.watch([
        '!node_modules/**/*',
        configPath.basepath+'*.jsp',
        './WEB-INF/pages/**/*.jsp',
        './tpl/**/*',
        configPath.css,
        //configPath.images,
        configPath.js
    ]).on('change', function(file) {
        //server.changed(file.path);
        console.log(file.path);
        openApp();
        browserSync.reload();
    });
});


gulp.task('server', function() {
    browserSync.init({
        proxy: "http://10.0.0.202:6832"
    });

    // 看守所有位在 dist/  目录下的档案，一旦有更动，便进行重整
    gulp.watch([
        '!node_modules/**/*',
        configPath.basepath+'*.jsp',
        './WEB-INF/pages/!**!/!*.jsp',
        './tpl/!**/!*',
        configPath.css,
        //configPath.images,
        configPath.js
    ]).on('change', function(file) {
        //server.changed(file.path);
        console.log(file.path);
        //openApp();
        browserSync.reload();
    });
});

gulp.task('minidata',['minijs','minijson']);

//压缩js文件
gulp.task('minijs',function (cb) {
    pump([
        gulp.src(['./data/slot/*.js']),
        uglify(),
        gulp.dest('./data/slot')
    ],cb);
});

//压缩json数据
gulp.task('minijson',function(){
    gulp.src('./data/slot/*.json')
        .pipe(jsonminify())
        .pipe(gulp.dest('./data/slot'));

    //压缩json数据
    gulp.task('minijson',function(){
        gulp.src('./data/slot/*.json')
            .pipe(jsonminify())
            .pipe(gulp.dest('./data/slot'));

        gulp.src('./mobile/json/game/*.json')
            .pipe(jsonminify())
            .pipe(gulp.dest('./mobile/json/game/'));

    });

});


// 创建sprites任务
gulp.task('sprite', function () {

    return sprity.src({
        src: './images/.tmp/*.{png,jpg}', //需要进行合并的图片路径
        style: './sprite.css',              //生成的样式文件名和格式，可以生成scss
        // 还有很多其他参数，如指定模板、图片格式、前缀、名称和图片合并的方向等，具体可以查看sprity的github内容
        processor: 'css', // 生成的样式格式
        format: 'png',  //
        // orientation: 'left-right',
        //template: './sprity-css.hbs', 模板文件
        prefix: 'sp', //前缀
        name: 'sprite'
    })
        .pipe(gulpif('*.png', gulp.dest('./dist/img/')))
        .pipe(gulpif('*.css', cleanCSS({keepBreaks: true})))
        //.pipe(cleanCSS({keepBreaks: true}))
        .pipe(gulp.dest('./dist/css/'));

});


gulp.task('minify-css', function() {
    return gulp.src('css/test.css')
        .pipe(cleanCSS({keepBreaks: true}))
        .pipe(gulp.dest('dist'));
});

gulp.task('csscomb', function() {
    return gulp.src('css/index.css')
        .pipe(csscomb())
        .pipe(gulp.dest('dist/css'));
});

gulp.task('deploy',function(){
    openApp();
});

gulp.task('default',['sprites']);
