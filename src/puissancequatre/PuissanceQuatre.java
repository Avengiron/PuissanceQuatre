package puissancequatre;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

/**
 * Class PuissanceQuatre pour projet PuissanceQuatre. Les joueurs jouent a tour
 * de role, en placant des jetons jusqu'a en aligner 4 consecutifs (en ligne,
 * colonne, ou diagonale). Un nouveau jeton ne peut pas etre place pendant
 * l'animation du jeton precedent. Un message apparait pour annoncer la fin du
 * jeu, annonce le gagnant s'il y en a un, ou un match nul. Propose de regenerer
 * le plateau pour une nouvelle partie.
 * @see https://fr.wikipedia.org/wiki/Puissance_4
 * @author Xavier
 */
public class PuissanceQuatre extends PApplet {
  /** Jeton dynamique pour animation de chute */
  private Jeton token;
  /** Liste des jetons places apres leur animation de chute */
  private Jeton[][] placed;
  /** Forme pour voir les jetons a travers le plateau */
  private PShape emptyCircle;
  /** Nombre de ligne sur le plateau de jeu */
  private final int nbRows = 6;
  /** Nombre de colonnes sur le plateau de jeu */
  private final int nbCols = 7;
  /** Taille de chaque cellule */
  private int size;
  /** Repere si c'est aux verts ou aux roses de jouer */
  private boolean player1;
  /** Autorise la verification de fin du jeu */
  private boolean checkEndGame;
  /** Repere la fin du jeu */
  private boolean gameOver;
  /** Gestion de rebond sur clic de souris */
  private boolean doOnce;
  /** Couleur du joueur 1 */
  private int colorPlayer1;
  /** Couleur du joueur 2 */
  private int colorPlayer2;

  /** Point d'entree de l'application */
  public static void main(String[] args) {
    PApplet.main(PuissanceQuatre.class.getName());
  }

  /** Setup du PApplet */
  @Override
  public void settings() {
    size(700, 700);
  }

  /** Setup de la fenetre */
  @Override
  public void setup() {
    surface.setLocation(1040, 180);
    placed = new Jeton[nbCols][nbRows];
    size = width / nbCols;
    emptyCircle = buildShape(size);
    player1 = true;
    checkEndGame = false;
    gameOver = false;
    doOnce = false;
    colorPlayer1 = color(191, 231, 0); // Vert
    colorPlayer2 = color(231, 0, 191); // Rose
  }

  /** Gere l'animation */
  @Override
  public void draw() {
    background(39, 39, 34);
    translate(0, 100);

    // Gestion de l'animation de chute du jeton
    if (token != null) {
      if (token.animate()) {
        // Si le jeton a fini son deplacement, il est inclus
        // dans le tableau des jetons places, et la victoire peut
        // etre verifiee
        placed[floor(token.getX() / size)][floor(token.getY() / size)] = token;
        token = null;
        checkEndGame = true;
      }
    }

    // Affichage de tous les jetons places 
    // et la grille de jeu
    showTokens();
    showBoard();

    // Ajouts graphiques
    if (token == null && !gameOver) {
      // Montre le prochain jeton a etre 
      // place au dessus de la grille de jeu
      int xIndex = mouseX / size;
      if (xIndex >= nbCols) {
        xIndex = nbCols - 1;
      }
      if (xIndex < 0) {
        xIndex = 0;
      }
      int x = xIndex * size + size / 2;
      int y = -size / 2;
      noStroke();
      if (player1) {
        fill(colorPlayer1);
      } else {
        fill(colorPlayer2);
      }
      ellipse(x, y, size, size);

      // Previsualise l'emplacement 
      // vise par le prochain jeton
      int yIndex = 5;
      while (placed[xIndex][yIndex] != null) {
        yIndex--;
        if (yIndex == -1) {
          break;
        }
      }
      // Si l'emplacement est valide, 
      // montre l'emplacement
      if (yIndex != -1) {
        noFill();
        if (player1) {
          stroke(colorPlayer1);
        } else {
          stroke(colorPlayer2);
        }
        strokeWeight(2);
        y = yIndex * size + size / 2;
        ellipse(x, y, size * .9f, size * .9f);
        strokeWeight(1);
        noStroke();
      } 
      // Sinon, grise le jeton au dessus de la grille
      else {
        noStroke();
        fill(0, 150);
        ellipse(x, y, size + 1, size + 1);
      }
    }

    // Quand le jeton a fini son animation, 
    // on peut verifier si le jeu est termine
    if (checkEndGame) {
      // Verification de la victoire
      if (victory()) {
        fill(30, 227);
        rect(0, -100, width, height);
        fill(255);
        textSize(60);
        textAlign(CENTER);
        text("Fin du jeu !", width / 2, height / 2 - 105);
        textSize(30);
        if (!player1) {
          fill(colorPlayer1);
          text("Victoire des verts", width / 2, height / 2 - 60);
        } else {
          fill(colorPlayer2);
          text("Victoire des roses", width / 2, height / 2 - 60);
        }
        fill(255);
        textSize(15);
        text("Cliquez pour rejouer", width / 2, height - 120);
        gameOver = true;
      } 
      // Si aucun vainqueur n'a ete detecte, 
      // verifie si le plateau est plein. Auquel cas, match nul
      else if (tie()) {
        fill(30, 227);
        rect(0, -100, width, height);
        fill(255);
        textSize(60);
        textAlign(CENTER);
        text("Fin du jeu !", width / 2, height / 2 - 105);
        textSize(30);
        text("Match nul", width / 2, height / 2 - 60);
        textSize(15);
        text("Cliquez pour rejouer", width / 2, height - 120);
        gameOver = true;
      }
    }
  }

  /**
   * Si le jeu est en cours, genere un nouveau jeton dans la colonne 
   * selectionnee. Si le jeu est termine et que le joueur clic a nouveau, 
   * prepare le plateau pour une nouvelle partie
   */
  @Override
  public void mousePressed() {
    // Un jeton est cree sur un clic de souris
    if (!doOnce && token == null && !gameOver) {
      doOnce = true;
      token = new Jeton(this, size, placed, player1);

      // Si la colonne selectionnee est deja pleine
      // le joueur est autorise a rejouer
      if (token.getYMax() == 0) {
        token = null;
        player1 = !player1;
      }
      player1 = !player1;
    }

    // Reset le plateau apres un match nul ou une victoire
    if (!doOnce && gameOver) {
      doOnce = true;
      player1 = true;
      checkEndGame = false;
      gameOver = false;
      placed = new Jeton[nbCols][nbRows];
    }
  }

  /** Relache la securite rebonds */
  @Override
  public void mouseReleased() {
    if (doOnce) {
      doOnce = false;
    }
  }
  
  /** Affiche les jetons places */
  public void showTokens() {
    for (int i = 0; i < nbCols; i++) {
      for (int j = 0; j < nbRows; j++) {
        if (placed[i][j] != null) {
          placed[i][j].show();
        }
      }
    }
  }

  /** Affiche le plateau */
  public void showBoard() {
    //noStroke();
    for (int i = 0; i < nbCols; i++) {
      for (int j = 0; j < nbRows; j++) {
        pushMatrix();
        translate(i * size, j * size);
        shape(emptyCircle);
        popMatrix();
      }
    }
  }

  /**
   * Verifie les 69 combinaisons de victoire (Toutes les lignes, colonnes et
   * diagonales).
   * @return True si un vainqueur a ete detecte
   */
  public boolean victory() {
    // Verifie les lignes
    for (int i = 0; i < nbCols - 3; i++) {
      for (int j = 0; j < nbRows; j++) {
        if (placed[i][j] != null
          && placed[i + 1][j] != null
          && placed[i + 2][j] != null
          && placed[i + 3][j] != null) {
          if (placed[i][j].isPlayer1()
            && placed[i + 1][j].isPlayer1()
            && placed[i + 2][j].isPlayer1()
            && placed[i + 3][j].isPlayer1()) {
            return true;
          } else if (!placed[i][j].isPlayer1()
            && !placed[i + 1][j].isPlayer1()
            && !placed[i + 2][j].isPlayer1()
            && !placed[i + 3][j].isPlayer1()) {
            return true;
          }
        }
      }
    }

    // Verifie les colonnes
    for (int i = 0; i < nbCols; i++) {
      for (int j = 0; j < nbRows - 3; j++) {
        if (placed[i][j] != null
          && placed[i][j + 1] != null
          && placed[i][j + 2] != null
          && placed[i][j + 3] != null) {
          if (placed[i][j].isPlayer1()
            && placed[i][j + 1].isPlayer1()
            && placed[i][j + 2].isPlayer1()
            && placed[i][j + 3].isPlayer1()) {
            return true;
          } else if (!placed[i][j].isPlayer1()
            && !placed[i][j + 1].isPlayer1()
            && !placed[i][j + 2].isPlayer1()
            && !placed[i][j + 3].isPlayer1()) {
            return true;
          }
        }
      }
    }

    // Verifie les diagonales TopLeft BottomRight
    for (int i = 0; i < nbCols - 3; i++) {
      for (int j = 0; j < nbRows - 3; j++) {
        if (placed[i][j] != null
          && placed[i + 1][j + 1] != null
          && placed[i + 2][j + 2] != null
          && placed[i + 3][j + 3] != null) {
          if (placed[i][j].isPlayer1()
            && placed[i + 1][j + 1].isPlayer1()
            && placed[i + 2][j + 2].isPlayer1()
            && placed[i + 3][j + 3].isPlayer1()) {
            return true;
          } else if (!placed[i][j].isPlayer1()
            && !placed[i + 1][j + 1].isPlayer1()
            && !placed[i + 2][j + 2].isPlayer1()
            && !placed[i + 3][j + 3].isPlayer1()) {
            return true;
          }
        }
      }
    }

    // Verifie les diagonales TopRight BottomLeft
    for (int i = 3; i < nbCols; i++) {
      for (int j = 0; j < nbRows - 3; j++) {
        if (placed[i][j] != null
          && placed[i - 1][j + 1] != null
          && placed[i - 2][j + 2] != null
          && placed[i - 3][j + 3] != null) {
          if (placed[i][j].isPlayer1()
            && placed[i - 1][j + 1].isPlayer1()
            && placed[i - 2][j + 2].isPlayer1()
            && placed[i - 3][j + 3].isPlayer1()) {
            return true;
          } else if (!placed[i][j].isPlayer1()
            && !placed[i - 1][j + 1].isPlayer1()
            && !placed[i - 2][j + 2].isPlayer1()
            && !placed[i - 3][j + 3].isPlayer1()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Verifie si la grille de jeu est pleine.
   * SIDE NOTE : Cette methode aurait du s'appeler draw, puisqu'elle juge s'il
   * y a match nul, mais c'est deja le nom d'une methode heritee de PApplet
   * @return True si la grille n'a plus d'espace disponible
   */
  public boolean tie() {
    for (int i = 0; i < nbCols; i++) {
      for (int j = 0; j < nbRows; j++) {
        if (placed[i][j] == null) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Creation d'une forme carree avec un cercle vide au centre, pour laisser
   * voir les jetons a travers
   * @param size Taille de la forme
   * @return Forme custom pour voir les jetons
   */
  public PShape buildShape(int size) {
    // N'est execute qu'une fois en debut de programme
    PShape shape = createShape();
    shape.setFill(color(19, 19, 17));
    shape.setStroke(false);
    shape.beginShape();
    // Carre exterieur
    shape.vertex(size, 0);
    shape.vertex(0, 0);
    shape.vertex(0, size);
    shape.vertex(size, size);
    shape.vertex(size, 0);
    // Cercle interieur
    for (int i = 0; i <= 360; i++) {
      PVector v = PVector.fromAngle(i * PI / 180).setMag((size / 2) * 0.9f);
      shape.vertex(v.x + size / 2, v.y + size / 2);
    }
    shape.endShape(CLOSE);
    return shape;
  }

}
