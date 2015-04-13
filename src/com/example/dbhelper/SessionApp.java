package com.example.dbhelper;

public class SessionApp {
	
	private int id;
	private String email; 
	private String token;
	private String secret; 
	private String key;
	
	public SessionApp(){
		this.id = 0; 
		this.email = ""; 
		this.token = "";
		this.secret = "";
		this.key = "";
	}
	
	public SessionApp(String email, String secret, String key) {
		this.email = email;
		this.secret = secret;
		this.key = key;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	} 
		
	
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public boolean isLogin(){
		boolean isValidSecret 	= this.secret != null && !this.secret.equals("") && this.secret.length() >0;
		boolean isValidKey 		= this.key 	!= null && !this.key.equals("") && this.key.length() > 0; 
		if(isValidSecret && isValidKey){
			return true;
		}
		return false;
		
	}
	
	@Override
	public String toString() {		
		return "[Session => id : " + id + ", email : " + email + ", token : " + token + "]";
	}
}
