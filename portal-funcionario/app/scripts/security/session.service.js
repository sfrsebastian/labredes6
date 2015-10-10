'use strict';

securityModule.factory('SessionService', ['$cookies', function($cookies) {

  var json = {};

  json.PFauthorizationToken = '';

  json.removeCookie = function(){
    $cookies.PFauthorizationToken='';
    $cookies.PFauthorizationUserName='';
    $cookies.PFauthorizationSystemRole='';
    $cookies.PFauthorizationUserRole='';
    json.PFauthorizationImage='';
  };

  json.setAuthorizationToken = function(pToken) {
    $cookies.PFauthorizationToken = pToken;
  };

  json.setAuthorizationUserName = function(pUserName) {
    $cookies.PFauthorizationUserName = pUserName;
  };

  json.removeAuthorizationToken = function() {
    $cookies.PFauthorizationToken = '';
  };

  json.getAuthorizationToken = function() {
    return $cookies.PFauthorizationToken;
  };

  json.getAuthorizationUserName = function() {
    return $cookies.PFauthorizationUserName;
  };

  json.hasAuthorizationToken = function() {

    return ($cookies.PFauthorizationToken || false);
  };

  json.getUserOrganization = function(){
    return $cookies.PFauthorizationOrganization;
  };

  json.setAuthorizationUserOrganization = function(organization){
    $cookies.PFauthorizationOrganization = organization;
  };

  json.setAuthorizationSystemRole= function(systemRole){
    $cookies.PFauthorizationSystemRole= systemRole;
  };

  json.getAuthorizationSystemRole= function(){
    return $cookies.PFauthorizationSystemRole;
  }

  json.getAuthorizationmUserRole = function(){
    return $cookies.PFauthorizationUserRole;
  }

  json.setAuthorizationUserRole = function(role){
    return $cookies.PFauthorizationUserRole = role;
  }

  json.getAuthorizationImage = function(){
    return json.PFauthorizationImage;
  }

  json.setAuthorizationImage = function(image){
    json.PFauthorizationImage = image;
  }

  return json;

}]);
