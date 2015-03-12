package rcp.manticora.dialogs;

public class ConnectionData {
	private String usuario;
	private String password;
	private String servidor;

	public ConnectionData(String usuario, String password, String servidor) {
		super();
		this.usuario = usuario;
		this.password = password;
		this.servidor = servidor;
	}

	public String getPassword() {
		return password;
	}

	public String getServidor() {
		return servidor;
	}

	public String getUsuario() {
		return usuario;
	}

}
