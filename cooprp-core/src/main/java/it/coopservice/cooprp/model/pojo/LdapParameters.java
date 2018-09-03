package it.coopservice.cooprp.model.pojo;

import java.io.Serializable;

public class LdapParameters implements Serializable {

	private static final long serialVersionUID = 1L;

	private String host;
	private int port;
	private String password;
	private String username;
	private boolean withAuth;
	private boolean withSsl;
	private String keystore;

	public LdapParameters() {

	}

	public LdapParameters(String host, int port, String username,
                          String password, boolean withAuth, boolean withSsl, String keystore) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.withAuth = withAuth;
		this.withSsl = withSsl;
		this.keystore = keystore;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isWithAuth() {
		return withAuth;
	}

	public void setWithAuth(boolean withAuth) {
		this.withAuth = withAuth;
	}

	public boolean isWithSsl() {
		return withSsl;
	}

	public void setWithSsl(boolean withSsl) {
		this.withSsl = withSsl;
	}

	public String getKeystore() {
		return keystore;
	}

	public void setKeystore(String keystore) {
		this.keystore = keystore;
	}
}
