package gp.e3.autheo.authorization.domain.entities;

import gp.e3.autheo.authorization.infrastructure.validators.HttpVerbValidator;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

public class Permission implements Comparable<Permission> {
	
	private long id;
	private final String name;
	private final String httpVerb;
	private final String url;
	
	public Permission(String name, String httpVerb, String url) {
		
		id = -1;
		this.name = name;
		this.httpVerb = httpVerb;
		this.url = url;
	}
	
	@JsonCreator
	public Permission(@JsonProperty("id") long id, @JsonProperty("name") String name, 
			@JsonProperty("httpVerb") String httpVerb, @JsonProperty("url") String url) {
		
		this.id = id;
		this.name = name;
		this.httpVerb = httpVerb;
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getHttpVerb() {
		return httpVerb;
	}

	public String getUrl() {
		return url;
	}
	
	public static boolean isValidPermission(Permission permission) {
		
		return (permission != null) &&
				(!StringUtils.isBlank(permission.getName())) &&
				(HttpVerbValidator.isValidHttpVerb(permission.getHttpVerb())) && 
				(!StringUtils.isBlank(permission.getUrl()));
	}
	
	@Override
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	@Override
	public int compareTo(Permission permission) {
		
		int answer = 0;
		
		answer += this.name.compareTo(permission.getName());
		answer += this.httpVerb.compareTo(permission.getHttpVerb());
		answer += this.url.compareTo(permission.getUrl());
		
		return answer;
	}
}