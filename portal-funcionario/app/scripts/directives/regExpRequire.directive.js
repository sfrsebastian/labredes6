'use strict';

// forces keystrokes that satisfy the regExp passed (http://stackoverflow.com/questions/18241558/angularjs-validate-input-and-prevent-change-if-invalid)
directivesModule.directive('regExpRequire', function() {

    //http://stackoverflow.com/questions/72
    // 35813/javascript-regex-for-key-event-input-validations-troubleshooting-help

  function keyRestricted(evt, regexp) {

      var theEvent = evt || window.event;
      var rv = true;

      //var key = theEvent.keyCode || theEvent.which;
      var key = (typeof theEvent.which === 'undefined') ? theEvent.keyCode : theEvent.which;

      if (key && (key !== 8) && (key !== 13)){

        var keychar = String.fromCharCode(key);
        //var keycheck = /[a-zA-Z0-9]/;

        if (!regexp.test(keychar) )
        {
          rv = theEvent.returnValue = false;//for IE
          if (theEvent.preventDefault) {
            theEvent.preventDefault();//Firefox
          }
        }
      }

      return rv;
    }

    return {
        scope: {

        },
        restrict: 'A',
        link: function(scope, elem, attrs) {

          var regexp = eval(attrs.regExpRequire);

            elem.on('keypress', function(event) {

              keyRestricted(event , regexp);

            });
        }
    };

});
