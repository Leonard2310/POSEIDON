package poseidon.boundary;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

import poseidon.DAO.*;
import poseidon.control.gestisciCorsa;
import poseidon.entity.*;

public class DipendenteConsoleBoundary {
	public static void showDipendenteConsoleBoundary(int codiceImpiegato) {
		// PRECONDITIONS: il dipendente ha premuto il pulsante per visualizzare le operazioni che può effettuare
		// POSTCONDITIONS: le operazioni che il dipendente pu� effettuare sono state mostrate a schermo

		inputReader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		int option = 0;
		do {
			System.out.println("Le operazioni disponibili sono: \n" + "\t1) Inserimento Corsa\n"
					+ "\t2) Modifica Corsa\n" + "\t3) Cancellazione Corsa\n" + "\t4) Emissione Biglietto\n"
					+ "\t5) Verifica Acquisti\n" + "\t6) Logout");
			System.out.flush();

			try {
				option = Integer.parseInt(inputReader.readLine());
			} catch (NumberFormatException e) {
				option = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}

			switch (option) {
			case 1: {
				inserimentoCorsa(codiceImpiegato);
				break;
			}
			case 2: {
				modificaCorsa(codiceImpiegato);
				break;
			}
			case 3: {
				cancellaCorsa(codiceImpiegato);
				break;
			}
			case 4: {
				emissioneBiglietto(codiceImpiegato);
				break;
			}
			case 5: {
				verificaAcquisti(codiceImpiegato);
				break;
			}
			case 6: {
				ApplicationConsoleBoundary.logout();
				break;
			}
			default: {
				System.out.println("Carattere inserito non riconosciuto!\n");
			}
			}
		} while (option != 6);
	}

	public static void inserimentoCorsa(int codiceImpiegato) {		
		// PRECONDITIONS: il dipendente ha selezionato l'opzione per inserire una nuova corsa
		// POSTCONDITIONS: se la corsa e' stata inserita correttamente viene notificato un messaggio 
		//		di successo che riporta il codice della corsa generato nella funzione del control. 
		//		Altrimenti viene notificato un errore. 
		
		String portoPartenza = null;
		String portoArrivo = null;
		String orarioPartenzaInput = null;
		LocalTime orarioPartenza;
		String orarioArrivoInput = null;
		LocalTime orarioArrivo;
		Double prezzo = 0.0;
		String nomeNave = null;
		Corsa corsa = null;
		Nave nave = null;
		Porto porto = null;
		String categoria = null;
		int capienzaPasseggeri = 0;
		int capienzaAutoveicoli = 0;
		String citta = null;
		char answer = 'n';
		int flag = 0;
		
		inputReader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		
		String[] listaPorti = {"Napoli", "Ischia", "Capri", "Ravenna", "Pozzuoli", "Catania", 
								"Bari", "Brindisi", "Palermo", "Cagliari", "Olbia", "Genova", 
								"Sorrento", "Amalfi", "Salerno", "Carlo Pisacane", "Messina", 
								"Ortona", "Pozzallo", "Maratea", "Livorno", "Civitavecchia", "Vasto",
								"Pesaro", "Savona", "Piombino", "Flavia", "Roseto degli Abruzzi"};
		
		List<Porto> lista_porto = new ArrayList<Porto>();

		try {
			lista_porto = PortoDAO.readallPorto();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.print("Porti registrati:");
		for(Porto p : lista_porto) {
			System.out.print(" " +p.getCitta());
		}
		do {
			System.out.println("\nRegistrare un nuovo porto [y/n]? "); 
	
			try {
				answer = inputReader.readLine().charAt(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (answer == 'y') {
				System.out.println("Inserire la citta' del porto:");
				try {
					citta = inputReader.readLine();
					for(int i = 0; i < listaPorti.length; i++) {
						if (citta.compareTo(listaPorti[i])==0) {
							flag = 1;
							porto = gestisciCorsa.inserimentoPorto(citta);
							System.out.println("Porto inserito correttamente.");
						}
					}
					if (flag == 0) {
						System.out.println("Impossibile inserire questo porto.");
					}
				} catch (IOException e) {
						e.printStackTrace();
				}	
			}
			else if(answer == 'n') {
				break;
			} 
			else {
				System.out.println("ERRORE: Carattere inserito non valido");
			}
		} while(porto == null);
		
		do {
			System.out.println("Inserire l'orario di partenza (hh:mm):");		
			try { 
				orarioPartenzaInput = inputReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			orarioPartenza = LocalTime.parse(orarioPartenzaInput);
			
			System.out.println("Inserire l'orario di arrivo (hh:mm):");		
			try { 
				orarioArrivoInput = inputReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			orarioArrivo = LocalTime.parse(orarioArrivoInput);
			
			if(orarioPartenza.compareTo(orarioArrivo) == 0) {
				System.out.println("ERRORE: l'orario inserito non e' valido!");
				flag=0;
			}
			else {
				boolean is = false;
				is = orarioPartenza.isAfter(orarioArrivo);
				if(is == true) {
					System.out.println("ERRORE: l'orario inserito non e' valido!");
					flag=0;
				}
				else if(is==false)
					flag=1;
			}		
		} while(flag==0);
		
		do {
			System.out.println("Inserire il porto di partenza:");
			try { 
				portoPartenza = inputReader.readLine();
				
				for(int i = 0; i < listaPorti.length; i++) {
					if (portoPartenza.compareTo(listaPorti[i])==0) {
						flag = 1;
					}
				}
				if (flag == 0) {
					System.out.println("Impossibile inserire questo porto: non e' stato registrato.");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while(flag==0);
		

		do {
			System.out.println("Inserire il porto di arrivo:");
			try { 
				portoArrivo = inputReader.readLine();
				
				for(int i = 0; i < listaPorti.length; i++) {
					if (portoArrivo.compareTo(listaPorti[i])==0) {
						flag = 1;
					}
				}
				if (flag == 0) {
					System.out.println("Impossibile inserire questo porto: non e' stato registrato.");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while(flag==0);
		
		System.out.println("Inserire il nome della nave che effettuera' la corsa:");
		try { 
			nomeNave = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Inserire il prezzo della corsa (nn.nn):");
		try { 
			prezzo = Double.parseDouble(inputReader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		corsa = gestisciCorsa.inserimentoCorsa(orarioPartenza, orarioArrivo, portoPartenza, portoArrivo, prezzo, nomeNave);
		
		if (corsa != null) {
			System.out.println("Corsa inserita correttamente con codice = "
								+ corsa.getCodiceCorsa());
			
			try {
				System.out.println("Inserire la categoria della nave (aliscafo/traghetto)");
				categoria = inputReader.readLine();
				
				if(categoria.equals("aliscafo")) {
					System.out.println("Inserisci la capienza dei passeggeri");
					capienzaPasseggeri = Integer.parseInt(inputReader.readLine());
				} 
				else if(categoria.equals("traghetto")) {
					System.out.println("Inserisci la capienza dei passeggeri");
					capienzaPasseggeri = Integer.parseInt(inputReader.readLine());
					System.out.println("Inserisci la capienza degli autoveicoli");
					capienzaAutoveicoli = Integer.parseInt(inputReader.readLine());
				} 
				else {
					System.out.println("Errore: valore inserito non valido!");
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			nave = gestisciCorsa.inserimentoNave(nomeNave, categoria, capienzaPasseggeri, capienzaAutoveicoli, corsa.getCodiceCorsa());
			
			if (nave != null) {
				System.out.println("Nave inserita correttamente");
				System.out.println("Operazione conclusa con successo!");
			} else {
				System.out.println("Si e' verificato un errore nell'inserimento della nave");
			}
		} else {
			System.out.println("Si e' verificato un errore nell'inserimento della corsa");
		}	

	}

	public static void emissioneBiglietto(int codiceImpiegato) {
		// PRECONDITIONS: il dipendente ha scelto di emettere un biglietto
		// POSTCONDITIONS: se il biglietto è stato emesso correttamente, viene
		// visualizzato un messaggio
		// che indica il codice del biglietto emesso; altrimenti, viene visualizzato un
		// messaggio che indica
		// che il biglietto non è stato emesso

		Biglietto biglietto = null;
		int codiceCorsa = 0;
		String tipoBiglietto = null;
		String targa = null;
		int codiceCliente = 0;
		char risposta = 'n';
		String ricevuta = null;

		inputReader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		try {
			System.out.println("Inserisci il codice della corsa");
			try {
				codiceCorsa = Integer.parseInt(inputReader.readLine());
			} catch (NumberFormatException e) {
				codiceCorsa = 0;
			}

			System.out.println("Inserisci il tipo del biglietto");
			tipoBiglietto = inputReader.readLine();

			if (tipoBiglietto.equals("veicolo")) {
				System.out.println("Inserisci la targa dell'autoveicolo");
				targa = inputReader.readLine();
			}

			System.out.println("Inserisci il codice del cliente che ha acquistato il biglietto");
			try {
				codiceCliente = Integer.parseInt(inputReader.readLine());
			} catch (NumberFormatException e) {
				codiceCliente = 0;
			}

			do {
				System.out.println("Generare una nuova ricevuta d'acquisto? [y/n]");
				risposta = inputReader.readLine().charAt(0);
				if (risposta != 'y' && risposta != 'n')
					System.out.println("Errore: carattere inserito non valido.");
			} while (risposta != 'y' && risposta != 'n');

			if (risposta == 'n') {
				System.out.println("Inserisci la ricevuta d'acquisto");
				try {
					ricevuta = inputReader.readLine();
				} catch (NumberFormatException e) {
					ricevuta = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		biglietto = gestisciCorsa.emissioneBiglietto(codiceImpiegato, codiceCorsa, targa, tipoBiglietto, codiceCliente,
				risposta, ricevuta);

		if (biglietto != null) {
			System.out.println("Biglietto emesso correttamente con codice = " + biglietto.getCodiceBiglietto());
		} else {
			System.out.println("Impossibile emettere il biglietto.");
		}
	}

	public static void verificaAcquisti(int codiceImpiegato) {
		// PRECONDITIONS: il dipendente ha scelto di verificare se ci sono acquisti per i quali ancora non è stato emesso un biglietto
		// POSTCONDITIONS: viene visualizzato un messaggio che indica la ricevuta, il codice della corsa
		// 		e il codice del cliente che ha effettuato l'acquisto, in caso non sia stato ancora emesso
		// 		il biglietto; altrimenti, viene visualizzato un messaggio che indica l'assenza di nuovi acquisti

		List<CronologiaAcquisti> lista = null;

		lista = gestisciCorsa.verificaAcquisti();

		if (lista != null && lista.size() > 0) {
			for (CronologiaAcquisti c : lista) {
				System.out.print("Trovato acquisto:  ricevuta = " + c.getRicevuta() + "  codice corsa = "
						+ c.getCorsa().getCodiceCorsa() + "  codice cliente = " + c.getCodiceCliente());
				if (c.getBiglietto() instanceof BigliettoVeicolo) {
					BigliettoVeicolo v = (BigliettoVeicolo)c.getBiglietto();
					System.out.println("  tipo = veicolo  targa = " + v.getTarga());
				}
				else {
					System.out.println("  tipo = passeggero");
				}
			}
		} else {
			System.out.println("Non ci sono nuovi acquisti.");
		}
	}

	public static void modificaCorsa(int codiceImpiegato) {
		// FUNZIONE NON IMPLEMENTATA
	}

	public static void cancellaCorsa(int codiceImpiegato) {
		// FUNZIONE NON IMPLEMENTATA
	}

	protected static java.io.BufferedReader inputReader;
}
