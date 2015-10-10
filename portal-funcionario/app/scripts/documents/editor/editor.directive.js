'use strict';

documentsModule.directive('lfEditor', function() {

    return {

        scope: {
            'close': '&onClose',
            docName: '=',
            templateName: '='
        },
        controller: 'EditorController',
        restrict: 'E',
        templateUrl: '../../../views/documents/editor/editor.html'
    };
});
