var compression = require('compression'); //libreria para comprimir los datos cuando los envia
var serveStatic = require('serve-static');//Manejo de los archivos de forma estatica
var express = require("express");
var bodyParser = require('body-parser');
var lessMiddleware = require('less-middleware');
var fs = require('fs');
var app = express();

// configure app to use bodyParser()
// this will let us get the data from a POST
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());


var maxAge = 86400000; //Max age for caching, currently set to single day

// compress all requests
app.use(compression());
// Serve up content from public directory
app.use(serveStatic(__dirname + '/dist', {'maxAge': maxAge}));
//app.use(express.static(__dirname + '/dist'));

app.listen(process.env.PORT || 9051,
  function(){
    console.log('I am running on port ' + (process.env.PORT || 9051) + ' biatch');
  }
);

/*
 * POST service to assign colorStyle to tenant.
 * param tenant Name of the tenant.
 * Param color Color of the tenant.
 */
app.post('/portal-functionary/api/colorStyle', function (req, res){
  var message = req.body.color;

  fs.readFile(__dirname + '/app/styles/colorFull.less','utf8', function (err, data) {
    if (err) throw err;

    var newLessFile = '@color: '+ req.body.color +';\n \n'+ data ;

    if (!fs.existsSync(__dirname + '/app/styles/' + req.body.tenant)){

      fs.mkdirSync(__dirname + '/app/styles/' + req.body.tenant);

    }

    fs.writeFile(__dirname + '/app/styles/' + req.body.tenant + '/' + req.body.tenant + '.less',newLessFile);

    if (fs.existsSync(__dirname + '/app/styles/' + req.body.tenant+ '/' + req.body.tenant + '.css')){

      fs.unlinkSync(__dirname + '/app/styles/' + req.body.tenant+'/' + req.body.tenant + '.css')

    }

    app.use(lessMiddleware(__dirname + '/app/styles/' + req.body.tenant));
    app.use(express.static(__dirname + '/app/styles/' + req.body.tenant));

  });
  return res.send(message);
});




