package miniprojet;

import java.util.ArrayList;
import java.util.HashSet;

import miniprojet.WeightedGraph.*;

public class Algo {

	public static ArrayList<Sommet> AStar(Graphe graphe, int debut, int fin, int ncols, int numS) {
		ArrayList<Sommet> chemin = new ArrayList<Sommet>();

		graphe.listeSommet.get(debut).timeFromSource = 0;

		// tous les noeuds du graphe a visiter:
		HashSet<Integer> a_visiter = new HashSet<Integer>();
		for (Sommet s : graphe.listeSommet)
			a_visiter.add(s.num);

		// om remplit l'attribut graphe.listeSommet.get(v).heuristic pour tous les
		// noeuds s du graphe:
		// heuristic donne une distance estimée au nœud d’arriver.
		int i = 0;
		for (Sommet s : graphe.listeSommet) {
			// distance(int Xa, int Ya, int Xb, int Yb)
			s.heuristic = distance(i % ncols, i / ncols, fin % ncols, fin / ncols);
			i++;
		}

		while (a_visiter.contains(fin)) {

			// Trouver le noeud min_s parmis tous les noeuds s ayant la distance temporaire
			// (graphe.listeSommet.get(s).timeFromSource + heuristic) minimale.
			int min_s = 0;
			double timeFromSourceHeuristicMinimale = Double.POSITIVE_INFINITY;
			for (Integer sommetNum : a_visiter) {
				if ((graphe.listeSommet.get(sommetNum).timeFromSource
						+ graphe.listeSommet.get(sommetNum).heuristic) <= timeFromSourceHeuristicMinimale) {
					min_s = sommetNum;
					timeFromSourceHeuristicMinimale = graphe.listeSommet.get(sommetNum).timeFromSource
							+ graphe.listeSommet.get(sommetNum).heuristic;
				}
			}

			// Ajouter à la liste des nœuds de l'itinéraire.
			chemin.add(graphe.listeSommet.get(min_s));

			// On l'enlève des noeuds à visiter
			a_visiter.remove(min_s);

			// Pour tous ses voisins, on vérifie si on est plus rapide en passant par ce
			// noeud.
			for (i = 0; i < graphe.listeSommet.get(min_s).listeAdjacence.size(); i++) {
				if (a_visiter.contains(graphe.listeSommet.get(min_s).listeAdjacence.get(i).destination)) {
					int to_try = graphe.listeSommet.get(min_s).listeAdjacence.get(i).destination;
					boolean peut_bouger = Labyrinthe.move(to_try, graphe.listeSommet, numS); // si le prisonnier peut se
																								// déplacer vers la
																								// position sommet_num.

					if (peut_bouger) { // true si le prisonnier peut se déplacer vers la position vertex_num.
						if (((graphe.listeSommet.get(min_s).timeFromSource
								+ graphe.listeSommet.get(min_s).listeAdjacence.get(i).weight) < (graphe.listeSommet
										.get(to_try).timeFromSource))) { // si la distance en passant par le nœud
																			// courant (donc distance temporaire plus
																			// distance du nœud courant au voisin) est
																			// plus petite que la distance temporaire
							graphe.listeSommet
									.get(to_try).timeFromSource = (graphe.listeSommet.get(min_s).timeFromSource
											+ graphe.listeSommet.get(min_s).listeAdjacence.get(i).weight); // on mets à
																											// jour la
																											// distance
																											// temporaire
							graphe.listeSommet.get(to_try).prev = graphe.listeSommet.get(min_s); // on enregistre le
																									// nœud courant
																									// comme nœud parent
																									// du voisin
						}
					}
				}
			}
		}
		return chemin; // chemin : Une liste de sommets formant le chemin.
	}

	static double distance(int Xa, int Ya, int Xb, int Yb) {
		double resX = Math.pow((Xb - Xa), 2);
		double resY = Math.pow((Yb - Ya), 2);
		return (Math.sqrt(resX + resY));
	}

}