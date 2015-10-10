/**
 * Created by LeanFactory on 26/02/15.
 */

'use strict';

notificationsModule.factory('MessageService', function() {

  var messageService = {};

  var Message = '';

  var errorMessage = '';

  messageService.getErrorMessage = function() {
    return errorMessage;
  };

  messageService.setErrorMessage = function(message) {
    errorMessage = message;
  };

  messageService.getMessage = function() {
    return Message;
  };

  messageService.setMessage = function(message) {
    Message = message;
  };

  return messageService;
});

