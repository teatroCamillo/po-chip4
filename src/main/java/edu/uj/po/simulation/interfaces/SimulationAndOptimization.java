package edu.uj.po.simulation.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * Interfejs dla funkcjonalności Symulacji. 
 * Podstawowa funkcjonalność to Symulacja układu. Optymalizacja to jednak także symulacja, 
 * ale przeprowadzona w nieco innym celu, którym tu jest usunięcie niepotrzebnych
 * elementów.
 */
public interface SimulationAndOptimization {
	/**
	 * Metoda ustanawia stany wejściowych listw kołkowych. Wykonanie symulacji
	 * układu prowadzi do ustalenia stanu wszystkich istotnych wejść/wyjść układu.
	 * 
	 * @param states zbiór stanów wejściowych listw kołkowych
	 * @throws UnknownStateException pin używany w symulacji jest w stanie
	 *                               nieokreślonym
	 */
	public void stationaryState(Set<ComponentPinState> states) throws UnknownStateException;

	/**
	 * Metoda zleca wykonanie symulacji obwodu poprzez wskazanie stanu wejściowych
	 * listew kołkowych w chwili 0 oraz liczby kroków czasowych do wykonania.
	 * Wynikiem symulacji jest mapa zawierająca dla kolejnych kroków czasowych (od 0
	 * do ticks włącznie) zbiory zawierające informacje o stanie wszystkich pinów
	 * wyjściowych listw kołkowych. Niepodłączone piny list wyjściowych są w stanie
	 * UNKNOWN.
	 * 
	 * @param states0 zbiór stanów wejściowych listw kołkowych w chwili 0
	 * @param ticks   liczba kroków symulacji
	 * @return mapa, której kluczem jest numer kroku czasowego (od stanu w chwili 0
	 *         po ticks włącznie).
	 * @throws UnknownStateException pin używany w symulacji jest w stanie
	 *                               nieokreślonym
	 */
	public Map<Integer, Set<ComponentPinState>> simulation(Set<ComponentPinState> states0, int ticks)
			throws UnknownStateException;

	/**
	 * Metoda optymalizuje układ wykrywając te elementy, których brak nie wpłynie na
	 * wynik działania układu. Stan wejściowych listw kołkowych w chwili zero oraz
	 * liczba kroków czasowych, przez którą należy przeanalizować działanie układu
	 * jest podawana przez użytkownika. Jeśli usunięcie jakiegoś elementu układu nie
	 * wpłynie na żaden ze stanów listwy wyjściowej, to element taki dodawany jest
	 * do wynikowego zbioru identyfikatorów elementów możliwych do usunięcia.
	 * 
	 * @param states0 zbiór stanów wejściowych listw kołkowych w chwili 0
	 * @param ticks   liczba kroków symulacji
	 * @return zbiór identyfikatorów elementów, których usunięcie nie wpłynie na
	 *         obserwowane w trakcie symulacji stany wyjściowych listw kołkowych
	 * @throws UnknownStateException pin używany w symulacji jest w stanie
	 *                               nieokreślonym
	 */
	public Set<Integer> optimize(Set<ComponentPinState> states0, int ticks) throws UnknownStateException;

}
