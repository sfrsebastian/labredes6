// Generated on 2015-01-26 using generator-angular 0.10.0
'use strict';

// # Globbing
// for performance reasons we're only matching one level down:
// 'test/spec/{,*/}*.js'
// use this if you want to recursively match all subfolders:
// 'test/spec/**/*.js'

var fs = require('fs');

module.exports = function (grunt) {

  // Load grunt tasks automatically
  require('load-grunt-tasks')(grunt);

  // Time how long tasks take. Can help when optimizing build times
  require('time-grunt')(grunt);

  // Configurable paths for the application
  var appConfig = {
    app: require('./bower.json').appPath || 'app',
    dist: 'dist'
  };

  // Define the configuration for all the tasks
  grunt.initConfig({

    // Project settings
    yeoman: appConfig,

    // Watches files for changes and runs tasks based on the changed files
    watch: {
      bower: {
        files: ['bower.json'],
        tasks: ['wiredep']
      },
      js: {
        files: ['<%= yeoman.app %>/scripts/**/*.js'],
        tasks: ['newer:jshint:all'],
        options: {
          livereload: '<%= connect.options.livereload %>'
        }
      },
      jsTest: {
        files: ['test/spec/**/*.js'],
        tasks: ['newer:jshint:test', 'karma']
      },
      styles: {
        files: ['<%= yeoman.app %>/styles/**/*.css'],
        tasks: ['newer:copy:styles', 'autoprefixer']
      },
      gruntfile: {
        files: ['Gruntfile.js']
      },
      livereload: {
        options: {
          livereload: '<%= connect.options.livereload %>'
        },
        files: [
          '<%= yeoman.app %>/**/*.html',
          '.tmp/styles/**/*.css',
          '<%= yeoman.app %>/images/**/*.{png,jpg,jpeg,gif,webp,svg}'
        ]
      }
    },

    // The actual grunt server settings
    connect: {
      options: {
        port: 9001,
        // Change this to '0.0.0.0' to access the server from outside.
        hostname: 'localhost',
        livereload: 35729
      },
      livereload: {
        options: {
          open: true,
          debug: true,
          middleware: function (connect) {
            return [
              connect.static('.tmp'),
              connect().use(
                '/bower_components',
                connect.static('./bower_components')
              ),
              connect.static(appConfig.app)
            ];
          }
        }
      },
      test: {
        options: {
          port: 9002,
          middleware: function (connect) {
            return [
              connect.static('.tmp'),
              connect.static('test'),
              connect().use(
                '/bower_components',
                connect.static('./bower_components')
              ),
              connect.static(appConfig.app)
            ];
          }
        }
      },
      dist: {
        options: {
          open: true,
          base: '<%= yeoman.dist %>'
        }
      }
    },

    // Make sure code styles are up to par and there are no obvious mistakes
    jshint: {
      options: {
        jshintrc: '.jshintrc',
        reporter: require('jshint-stylish')
      },
      all: {
        src: [
          'Gruntfile.js',
          '<%= yeoman.app %>/scripts/**/*.js'
        ]
      },
      test: {
        options: {
          jshintrc: 'test/.jshintrc'
        },
        src: ['test/spec/**/*.js']
      }
    },

    // Empties folders to start fresh
    clean: {
      dist: {
        files: [{
          dot: true,
          src: [
            '.tmp',
            '<%= yeoman.dist %>/{,*/}*',
            '!<%= yeoman.dist %>/.git{,*/}*'
          ]
        }]
      },
      server: '.tmp'
    },

    // Add vendor prefixed styles
    autoprefixer: {
      options: {
        browsers: ['last 1 version']
      },
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '**/*.css',
          dest: '.tmp/styles/'
        }]
      }
    },

    // Automatically inject Bower components into the app
    wiredep: {
      pdfWorkerDependency: {
        src: ['<%= yeoman.app %>/index.html'],
        ignorePath:  /\.\.\//,
        exclude: [ /angular/, /bootstrap/, /es5-shim/, /jquery/, /json3/, /ng-table/, /ngDialog/, /sjcl/, 'bower_components/pdfjs-dist/build/pdf.js', /wodo.texteditor/ ],
        fileTypes: {
          html: {
            block: /(([ \t]*)<!--\s*bowerWorkerPdf:*(\S*)\s*-->)(\n|\r|.)*?(<!--\s*endBowerWorkerPdf\s*-->)/gi,
            detect: {
              js: /<script.*src=['"]([^'"]+)/gi,
              css: /<link.*href=['"]([^'"]+)/gi
            },
            replace: {
              js: '<script src="{{filePath}}"></script>',
              css: '<link rel="stylesheet" href="{{filePath}}" />'
            }
          }
        }
      },
      allBowerDependencies: {
        src: ['<%= yeoman.app %>/index.html'],
        ignorePath:  /\.\.\//,
        exclude: [ ],
        fileTypes: {
          html: {
            block: /(([ \t]*)<!--\s*bower:*(\S*)\s*-->)(\n|\r|.)*?(<!--\s*endbower\s*-->)/gi,
            detect: {
              js: /<script.*src=['"]([^'"]+)/gi,
              css: /<link.*href=['"]([^'"]+)/gi
            },
            replace: {
              js: '<script src="{{filePath}}"></script>',
              css: '<link rel="stylesheet" href="{{filePath}}" />'
            }
          }
        }
      }
    },

    // Renames files for browser caching purposes
    filerev: {
      dist: {
        src: [
          '<%= yeoman.dist %>/scripts/**/*.js',
          '<%= yeoman.dist %>/styles/**/*.css',
          '<%= yeoman.dist %>/images/**/*.{png,jpg,jpeg,gif,webp,svg}',
          '<%= yeoman.dist %>/styles/fonts/*'
        ]
      }
    },

    // Reads HTML for usemin blocks to enable smart builds that automatically
    // concat, minify and revision files. Creates configurations in memory so
    // additional tasks can operate on them
    useminPrepare: {
      html: '<%= yeoman.app %>/index.html',
      options: {
        dest: '<%= yeoman.dist %>',
        flow: {
          html: {
            steps: {
              js: ['concat', 'uglifyjs'],
              css: ['cssmin']
            },
            post: {}
          }
        }
      }
    },

    // Performs rewrites based on filerev and the useminPrepare configuration
    usemin: {
      html: ['<%= yeoman.dist %>/views/**/*.html',
              '<%= yeoman.dist %>/**/*.html'],
      css: ['<%= yeoman.dist %>/styles/**/*.css'],
      options: {
        assetsDirs: ['<%= yeoman.dist %>','<%= yeoman.dist %>/images']
      }
    },

    // The following *-min tasks will produce minified files in the dist folder
    // By default, your `index.html`'s <!-- Usemin block --> will take care of
    // minification. These next options are pre-configured if you do not wish
    // to use the Usemin blocks.
    // cssmin: {
    //   dist: {
    //     files: {
    //       '<%= yeoman.dist %>/styles/main.css': [
    //         '.tmp/styles/**/*.css'
    //       ]
    //     }
    //   }
    // },
    // uglify: {
    //   dist: {
    //     files: {
    //       '<%= yeoman.dist %>/scripts/scripts.js': [
    //         '<%= yeoman.dist %>/scripts/scripts.js'
    //       ]
    //     }
    //   }
    // },
    // concat: {
    //   dist: {}
    // },

    imagemin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '{,*/}*.{png,jpg,jpeg,gif}',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },

    svgmin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '{,*/}*.svg',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },

    htmlmin: {
      dist: {
        options: {
          collapseWhitespace: true,
          conservativeCollapse: true,
          collapseBooleanAttributes: false,
          removeCommentsFromCDATA: true,
          removeOptionalTags: true
        },
        files: [{
          expand: true,
          cwd: '<%= yeoman.dist %>',
          src: ['*.html', 'views/**/*.html', 'app_components/**/*.html'],
          dest: '<%= yeoman.dist %>'
        }]
      }
    },

    // ng-annotate tries to make the code safe for minification automatically
    // by using the Angular long form for dependency injection.
    ngAnnotate: {
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/concat/scripts',
          src: ['*.js', '!oldieshim.js'],
          dest: '.tmp/concat/scripts'
        }]
      }
    },

    // Replace Google CDN references
    cdnify: {
      dist: {
        html: ['<%= yeoman.dist %>/*.html']
      }
    },

    // Copies remaining files to places other tasks can use
    copy: {
      dist: {
        files: [{
          expand: true,
          dot: true,
          cwd: '<%= yeoman.app %>',
          dest: '<%= yeoman.dist %>',
          src: [
            '*.{ico,png,txt}',
            '.htaccess',
            '*.html',
            'views/**/*.html',
            'images/**/*.{webp}',
            'fonts/**/*.*',
            'lib/**/*.*',
            'files/**/*.*',
            'img/*.*'
          ]
        }, {
          expand: true,
          cwd: '.tmp/images',
          dest: '<%= yeoman.dist %>/images',
          src: ['generated/*']
        }, {
          expand: true,
          cwd: 'bower_components/bootstrap/dist',
          src: 'fonts/*',
          dest: '<%= yeoman.dist %>'
        }, {
          expand: true,
          cwd: 'bower_components/font-awesome',
          src: 'fonts/*',
          dest: '<%= yeoman.dist %>'
        }, {
          expand: true,
          cwd: 'bower_components/simple-line-icons',
          src: 'fonts/*',
          dest: '<%= yeoman.dist %>'
        }]
      },
      styles: {
        expand: true,
        cwd: '<%= yeoman.app %>/styles',
        dest: '.tmp/styles/',
        src: '{,*/}*.css'
      },
      copyWodoDependencies: {
        expand: true,
        cwd: 'bower_components/wodo.texteditor/wodotexteditor/',
        dest: '<%= yeoman.dist %>/scripts',
        src: ['**/*.*']
      }
    },

    // Add ngConstant
    ngconstant: {
      // Options for all targets
      options: {
        space: '  ',
        wrap: '"use strict";\n\n {%= __ngModule %}',
        name: 'config'
      },
      // Environment targets
      local: {
        options: {
          dest: '<%= yeoman.app %>/scripts/config.js'
        },
        constants: {
          environment: {
            name: 'local',
            autheo: 'http://localhost:9002/autheo/api',
            singleWindow: 'http://localhost:9054/single-window/api',
            documentManager: 'http://localhost:9056/document-manager/api',
            workFlowManager: 'http://localhost:9052/workflow-manager/api',
            customerSettings : 'http://localhost:9055/customer-settings/api',
            portalFunctionary: 'http://localhost:9058/portal-functionary/api',
            businessReports: 'http://localhost:9061/business-reports/api',
            messageReinjector: 'http://localhost:9062/message-reinjector/api'
          }
        }
      },
      development: {
        options: {
          dest: '<%= yeoman.app %>/scripts/config.js'
        },
        constants: {
          environment: {
            name: 'development',
            autheo: 'http://192.168.168.88/autheo/api',
            singleWindow: 'http://192.168.168.88/single-window/api',
            documentManager: 'http://192.168.168.88/document-manager/api',
            workFlowManager: 'http://192.168.168.88/workflow-manager/api',
            customerSettings : 'http://192.168.168.88/customer-settings/api',
            portalFunctionary: 'http://192.168.168.88/portal-functionary/api',
            businessReports: 'http://192.168.168.88/business-reports/api'
          }
        }
      },
      production: {
        options: {
          dest: '<%= yeoman.app %>/scripts/config.js'
        },
        constants: {
          environment: {
            name: 'production',
            autheo: 'http://192.168.168.186/autheo/api',
            singleWindow: 'http://192.168.168.186/single-window/api',
            documentManager: 'http://192.168.168.186/document-manager/api',
            workFlowManager: 'http://192.168.168.186/workflow-manager/api',
            customerSettings: 'http://192.168.168.186/customer-settings/api',
            portalFunctionary: 'http://192.168.168.186/portal-functionary/api',
            businessReports: 'http://192.168.168.186/business-reports/api'
          }
        }
      }
    },

    // Run some tasks in parallel to speed up the build process
    concurrent: {
      server: [
        'copy:styles'
      ],
      test: [
        'copy:styles'
      ],
      dist: [
        'copy:styles',
        'imagemin',
        'svgmin'
      ]
    },

    // Test settings
    karma: {
      unit: {
        configFile: 'test/karma.conf.js',
        singleRun: true
      }
    },

    changePdfWorkerFileName: {
      dist: {
        files: {
          src: ['<%= yeoman.dist %>/scripts/*']
        }
      }
    }
  });

  grunt.registerMultiTask('changePdfWorkerFileName', 'Change the worker file name', function () {

    var destinationFilePath = '';
    var sourceFilePath = '';

    //Finds the cache string used in the vendor file and the source worker file path
    this.files.forEach(function(file) {
      var contents = file.src.filter(function(filepath) {


        if(filepath.indexOf('vendor') > -1) {
          var splitName = filepath.split('.');
          if(splitName[1]!=='worker'){
            destinationFilePath = 'dist/scripts/vendor.' + splitName[1] + '.worker.js';
          } else {
            sourceFilePath = filepath;
          }

        }

        return true;
      });
    });

    //Using the source worker file path and the cache string, renames the worker file name
    fs.renameSync(sourceFilePath, destinationFilePath, function (err) {
      if (err) {
        grunt.log.error();
        grunt.verbose.error();
        grunt.fail.warn('Rename operation failed.');
      }
    });

    //Updates the mapping of the file rev summary for proper naming of the pdf worker js in the index.html file
    for(var key in grunt.filerev.summary){
      if(key.indexOf('worker') > -1) {
        grunt.filerev.summary[key] = destinationFilePath;
      }
    }

  });

  grunt.registerTask('serve', 'Compile then start a connect web server', function (target) {
    if (target === 'dist') {
      return grunt.task.run(['build', 'connect:dist:keepalive']);
    }

    grunt.task.run([
      'clean:server',
      'ngconstant:local',
      'wiredep',
      'concurrent:server',
      'autoprefixer',
      'connect:livereload',
      'watch'
    ]);
  });

  grunt.registerTask('server', 'DEPRECATED TASK. Use the "serve" task instead', function (target) {
    grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
    grunt.task.run(['serve:' + target]);
  });

  grunt.registerTask('test', [
    'clean:server',
    'concurrent:test',
    'autoprefixer',
    'connect:test',
    'karma'
  ]);

  grunt.registerTask('build', [
    'clean:dist',
    'ngconstant:development',
    'wiredep',
    'useminPrepare',
    'concurrent:dist',
    'autoprefixer',
    'concat',
    'ngAnnotate',
    'copy:dist',
    'cdnify',
    'cssmin',
    'uglify',
    'filerev',
    'changePdfWorkerFileName',
    'usemin',
    'htmlmin',
    'copy:copyWodoDependencies'

  ]);

  grunt.registerTask('production', [
    'clean:dist',
    'ngconstant:production',
    'wiredep',
    'useminPrepare',
    'concurrent:dist',
    'autoprefixer',
    'concat',
    'ngAnnotate',
    'copy:dist',
    'cdnify',
    'cssmin',
    'uglify',
    'filerev',
    'changePdfWorkerFileName',
    'usemin',
    'htmlmin',
    'copy:copyWodoDependencies'

  ]);

  grunt.registerTask('default', [
    'newer:jshint',
    'test',
    'build'
  ]);
};
