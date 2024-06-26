package miniprojet;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import miniprojet.WeightedGraph.Graphe;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		List<Character> res = new ArrayList<Character>();

		// Initialisation du nombre d'instances
		int instances;
		System.out.println("Entrez le nombre d'instances : ");
		instances = Integer.parseInt(sc.nextLine());

		char[][] lab = null;

		// initialisation du nombre de ligne et de colonne
		int nbLigne = 0;
		int nbColonne = 0;
		for (int i = 0; i < instances; i++) {
			System.out.println("Entrez le nombre de ligne et nombre de colonne, séparés par un espace");
			StringTokenizer tokenizer = new StringTokenizer(sc.nextLine(), " ");

			System.out.print("Rappel : \n");
			System.out.println("'.' = libre");
			System.out.println("'#' = mur");
			System.out.println("'F' = feu");
			System.out.println("'D' = début");
			System.out.println("'S' = sortie");

			System.out.println("Veuillez construire le labyrinthe ");

			nbLigne = (tokenizer.hasMoreTokens()) ? Integer.parseInt(tokenizer.nextToken()) : 0;
			nbColonne = (tokenizer.hasMoreTokens()) ? Integer.parseInt(tokenizer.nextToken()) : 0;

			// pour chaque ligne, on initialise le labyrinthe
			lab = new char[nbLigne][nbColonne];
			for (int j = 0; j < nbLigne; j++) {
				String entree = sc.nextLine();
				for (int k = 0; k < nbColonne; k++) {
					lab[j][k] = entree.charAt(k);
				}
			}

			Graphe graphe = new Graphe();
			int debutSommet = 0, finSommet = 0;
			for (int j = 0; j < nbLigne; j++) {
				for (int k = 0; k < nbColonne; k++) {
					graphe.ajouterSommet(lab[j][k], 1, j, k);
					if (lab[j][k] == 'D') {
						debutSommet = j * nbColonne + k;
					}
					if (lab[j][k] == 'S') {
						finSommet = j * nbColonne + k;
					}
				}
			}

			// Ajout des arrêtes
			for (int line = 0; line < nbLigne; line++) {
				for (int col = 0; col < nbColonne; col++) {
					int source = line * nbColonne + col;
					int dest;
					double weight;
					for (int j = -1; j <= 1; j++) {
						for (int k = -1; k <= 1; k++) {
							if ((j != 0) || (k != 0)) {
								if ((line + j) >= 0 && (line + j) < nbLigne && (col + k) >= 0
										&& (col + k) < nbColonne) {
									dest = (line + j) * nbColonne + col + k;
									weight = 1;
									if (Math.abs(source - dest) == 1 || Math.abs(source - dest) == nbColonne) {
										graphe.ajouterArete(source, dest, weight);
									}
								}
							}
						}
					}
				}
			}

			res.add(Labyrinthe.run_instance(graphe, debutSommet, finSommet, nbLigne, nbColonne));
		}

		for (Character character : res)
			System.out.println(character);

		sc.close();
	}
}
