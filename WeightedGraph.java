package miniprojet;

import java.util.LinkedList;
import java.util.ArrayList;

public class WeightedGraph {

	static class Arete {
		int source;
		int destination;
		double weight;

		public Arete(int source, int destination, double weight) {
			this.source = source;
			this.destination = destination;
			this.weight = weight;
		}
	}

	static class Sommet {
		double indivTime, timeFromSource, heuristic;
		int num, i, j;
		char position;
		Sommet prev;
		LinkedList<Arete> listeAdjacence;

		public Sommet(int num, char position, int i, int j) {
			this.indivTime = Double.POSITIVE_INFINITY;
			this.timeFromSource = Double.POSITIVE_INFINITY;
			this.heuristic = -1;
			this.num = num;
			this.i = i;
			this.j = j;
			this.position = position;
			this.prev = null;
			this.listeAdjacence = new LinkedList<Arete>();
		}

		public void ajouterVoisin(Arete e) {
			this.listeAdjacence.addFirst(e);
		}
	}

	static class Graphe {
		ArrayList<Sommet> listeSommet;
		int num_s = 0;

		Graphe() {
			listeSommet = new ArrayList<Sommet>();
		}

		public void ajouterSommet(char position, double indivTime, int i, int j) {
			Sommet v = new Sommet(num_s, position, i, j);
			v.indivTime = indivTime;
			listeSommet.add(v);
			num_s++;
		}

		public void ajouterArete(int source, int destination, double weight) {
			Arete arete = new Arete(source, destination, weight);
			listeSommet.get(source).ajouterVoisin(arete);
		}
	}
}