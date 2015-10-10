/**
 * Created by LeanFactory on 23/07/15.
 */

adminModule.factory('UndeliveredMessagesService', ['environment', '$resource', function(environment, $resource) {
  var url = environment.messageReinjector + '/undelivered-message';
  var defaultParams = {};

  var actions = {

    getUndeliveredMessages:{method:'GET',isArray:true},
    reinjectMessages:{method:'POST',isArray:false}
  };

  return $resource(url, defaultParams, actions);
}]);
