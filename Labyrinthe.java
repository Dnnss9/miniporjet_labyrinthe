package miniprojet;

import java.util.ArrayList;

import miniprojet.WeightedGraph.*;

public class Labyrinthe {

	// le prisonnier peut se deplacer si on a un '.' ou 'S'

	public static boolean move(int sommet_num, ArrayList<Sommet> listeSommet, int nbS) {
		if (sommet_num < nbS) {
			return (listeSommet.get(sommet_num).position == '.' || listeSommet.get(sommet_num).position == 'S');
		} else {
			return false;
		}
	}

	// dans le code labyrinthe.c, la fonction retourne un entier,
	// cette methode retourne une arraylist qui contient les chemins possibles

	static ArrayList<Character> can_move(Graphe graph, int start, int end, int ncols, int numberV) {
		ArrayList<Sommet> listVertex = Algo.AStar(graph, start, end, ncols, numberV);// plus cours sommetListe
		ArrayList<Character> listDirections = new ArrayList<Character>();

		for (int i = 0; i < (listVertex.size() - 1); i++) {
			int mouvement = listVertex.get(i + 1).num - listVertex.get(i).num;
			if (mouvement == 1)
				listDirections.add('R'); // 'R' = right
			else if (mouvement == -1)
				listDirections.add('L'); // 'L' = left
			else if (mouvement == ncols)
				listDirections.add('B'); // 'B' = bottom
			else if (mouvement == (-1 * ncols))
				listDirections.add('T'); // 'T' = top
			else
				return listDirections;
		}
		return listDirections;
	}

	// retourne true si il y a du feu donc gameover
	static boolean burn_around(int sommet_num, ArrayList<Sommet> listeSommet, int nlignes, int ncols) {
		int tmpJ = listeSommet.get(sommet_num).j;
		int tmpI = listeSommet.get(sommet_num).i;
		// S = sortie, D = depart
		if (tmpJ != 0) {
			if (listeSommet.get(sommet_num - 1).position == '.')
				listeSommet.get(sommet_num - 1).position = 'A';
			else if (listeSommet.get(sommet_num - 1).position == 'S' || listeSommet.get(sommet_num - 1).position == 'D')
				return true;
		}

		if (tmpJ != (ncols - 1)) {
			if (listeSommet.get(sommet_num + 1).position == '.')
				listeSommet.get(sommet_num + 1).position = 'A';
			else if (listeSommet.get(sommet_num + 1).position == 'S' || listeSommet.get(sommet_num + 1).position == 'D')
				return true;
		}

		if (tmpI != 0) {
			if (listeSommet.get(sommet_num - ncols).position == '.')
				listeSommet.get(sommet_num - ncols).position = 'A';
			else if (listeSommet.get(sommet_num - ncols).position == 'S'
					|| listeSommet.get(sommet_num - ncols).position == 'D')
				return true;
		}

		if (tmpI != (nlignes - 1)) {
			if (listeSommet.get(sommet_num + ncols).position == '.')
				listeSommet.get(sommet_num + ncols).position = 'A';
			else if (listeSommet.get(sommet_num + ncols).position == 'S'
					|| listeSommet.get(sommet_num + ncols).position == 'D')
				return true;
		}
		return false;
	}

	// si au prochain chemin on a la sortie, donc 'S', on retourne true

	static boolean win_move(int debut, ArrayList<Sommet> listeSommet, int nlignes, int ncols) {
		// S : la sortie
		int tmpJ = listeSommet.get(debut).j;
		int tmpI = listeSommet.get(debut).i;
		boolean left = tmpJ != 0 && (listeSommet.get(debut - 1).position == 'S');
		boolean right = tmpJ != (ncols - 1) && (listeSommet.get(debut + 1).position == 'S');
		boolean top = tmpI != 0 && listeSommet.get(debut - ncols).position == 'S';
		boolean bottom = tmpI != (nlignes - 1) && listeSommet.get(debut + ncols).position == 'S';

		return top || left || right || bottom;
	}

	static boolean move_prisoner(char directionMouvementPossiblePourCeTour, ArrayList<Sommet> listeSommet, int nlignes,
			int ncols, int end) {
		int debut = 0;

		// position du prisonnier
		for (int i = 0; i < listeSommet.size(); i++) {
			if (listeSommet.get(i).position == 'D')
				debut = i;
		}

		boolean win = win_move(debut, listeSommet, nlignes, ncols);

		if (win)
			return true; // prisonier pardonné
		else {
			listeSommet.get(debut).position = 'L';

			if (directionMouvementPossiblePourCeTour == 'B')
				listeSommet.get(debut + ncols).position = 'D';
			else if (directionMouvementPossiblePourCeTour == 'T')
				listeSommet.get(debut - ncols).position = 'D';
			else if (directionMouvementPossiblePourCeTour == 'L')
				listeSommet.get(debut - 1).position = 'D';
			else if (directionMouvementPossiblePourCeTour == 'R')
				listeSommet.get(debut + 1).position = 'D';
		}
		return false;
	}

	// le prisonnier est pardonne si a chaque tour on obtient true

	static char run_instance(Graphe graphe, int debut, int fin, int nlignes, int ncols) {
		int tour = 0; // Le numéro du premier tour est 0.

		// liste des chemins du prisonier
		ArrayList<Character> listeDirections = can_move(graphe, debut, fin, ncols, nlignes * ncols);
		while (tour < listeDirections.size()) {
			for (int i = 0; i < graphe.listeSommet.size(); i++) {
				if (graphe.listeSommet.get(i).position == 'A')
					graphe.listeSommet.get(i).position = 'F'; // le feu se propage autour de lui
			}
			for (int i = 0; i < graphe.listeSommet.size(); i++) {
				if (graphe.listeSommet.get(i).position == 'F') {
					if (burn_around(i, graphe.listeSommet, nlignes, ncols))
						return 'N'; // le feu bloque la sortie donc gameover
				}
			}
			// prisonnier gagne si true
			if (move_prisoner(listeDirections.get(tour), graphe.listeSommet, nlignes, ncols, fin))
				return 'Y';
			tour++;// prochain tour
		}
		return 'N';
	}
}
