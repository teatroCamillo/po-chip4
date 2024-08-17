package edu.uj.po.simulation.interfaces;

public interface CircuitDesign {
	/**
	 * Metoda zleca utworzenie układu scalonego o podanym kodzie numerycznym.
	 * Przykładowe kody to np. 7400, 7402 itd. Metoda zwraca unikalny identyfikator,
	 * za pomocą którego układ ten będzie identyfikowany. Metoda musi zwrócić
	 * identyfikator unikalny globalnie - każdy utworzony element musi mieć inny.
	 * 
	 * @param code typ układu scalonego
	 * @return unikalny globalnie identyfikator elementu.
	 * @throws UnknownChip code nie odpowiada żadnemu z zaimplementowanych układów
	 */
	public int createChip(int code) throws UnknownChip, CloneNotSupportedException;

	/**
	 * Tworzy wejściową listwę kołkową o podanym rozmiarze. Możliwe jest użycie w
	 * projekcie wielu listew. Metoda musi zwrócić identyfikator unikalny globalnie
	 * - każdy utworzony element musi mieć inny.
	 * 
	 * @param size liczba pinów listwy kołkowej
	 * @return unikalny globalnie identyfikator elementu
	 */
	public int createInputPinHeader(int size);

	/**
	 * Tworzy wyjściową listwę kołkową o podanym rozmiarze. Możliwe jest użycie w
	 * projekcie wielu listew. Metoda musi zwrócić identyfikator unikalny globalnie
	 * - każdy utworzony element musi mieć inny.
	 * 
	 * @param size liczba pinów listwy kołkowej
	 * @return unikalny globalnie identyfikator elementu
	 */
	public int createOutputPinHeader(int size);

	/**
	 * Metoda zleca połączenie pinu pin1 elementu component1 z pinem pin2 elementu
	 * component2. Elementem może być zarówno układ scalony jaki i listwa kołkowa.
	 * Metoda sprawdza poprawność zlecenia, w przypadku błędnych danych lub
	 * naruszenia zasad łączenia układów zgłasza odpowiedni wyjątek.
	 * 
	 * @param component1 identyfikator pierwszego z elementów
	 * @param pin1       numer pinu pierwszego z elementów
	 * @param component2 identyfikator drugiego z elementów
	 * @param pin2       numer pinu drugiego z elementów
	 * @throws UnknownComponent      komponent nieznany (błędny identyfikator
	 *                               komponentu)
	 * @throws UnknownPin            nieznany pin dla danego komponentu
	 * @throws ShortCircuitException błąd w połączeniach - złączono dwa różne
	 *                               wyjścia.
	 */
	public void connect(int component1, int pin1, int component2, int pin2)
			throws UnknownComponent, UnknownPin, ShortCircuitException;

}
