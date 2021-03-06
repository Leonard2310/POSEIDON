package poseidon.entity;

public class Cliente extends Utente {
	
	private int codiceCliente;
	
	public Cliente(String cognome, String nome, String password, int codiceCliente) {
		super(cognome, nome, password);
		this.codiceCliente = codiceCliente;
	}
	
	public int getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(int codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	
	@Override
	public boolean equals(Object o) {
		Cliente c = (Cliente)o;
		return (super.equals(c) && this.codiceCliente == c.codiceCliente);
	}

}
