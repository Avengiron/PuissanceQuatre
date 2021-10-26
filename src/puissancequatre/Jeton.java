package puissancequatre;

import processing.core.PApplet;

/**
 * Class Jeton pour projet PuissanceQuatre
 * @author Xavier
 */
public class Jeton {
  /** PApplet parent */
  private final PApplet app;
  /** Rayon du jeton */
  private final int radius;
  /** Position x */
  private final float x;
  /** Position y */
  private float y;
  /** Velocite (animation) */
  private float yVel;
  /** Acceleration (animation) */
  private final float yAcc;
  /** Position y max (animation) */
  private final float yMax;
  /** Couleur du joueur 1 */
  private final int colorPlayer1;
  /** Couleur du joueur 2 */
  private final int colorPlayer2;
  /** Repere si le jeton appartient aux verts ou aux roses */
  private final boolean player1;

  /**
   * Constructeur du jeton
   * @param app PApplet parent
   * @param size Taille d'une case
   * @param placed Tableau des jetons places
   * @param player1 Tour du joueur jaune
   */
  public Jeton(PApplet app, int size, Jeton[][] placed, boolean player1) {
    this.app = app;
    radius = size / 2;
    x = (app.mouseX / size) * size + radius;
    y = -radius;
    yVel = 0;
    yAcc = .8f;
    yMax = computeYMax(placed);
    this.player1 = player1;
    colorPlayer1 = app.color(191, 231, 0); // Vert
    colorPlayer2 = app.color(231, 0, 191); // Rose
  }

  /** 
   * Affiche le jeton pendant l'animation. L'animation est finie quand le jeton
   * a depasse sa position finale.
   * @return True si l'animation est finie 
   */
  public boolean animate() {
    app.noStroke();
    if(player1) {
      app.fill(colorPlayer1);
    } else {
      app.fill(colorPlayer2);
    }
    app.ellipse(x, y, radius * 2, radius * 2);
    y += yVel;
    yVel += yAcc;
    if (y >= yMax) {
      y = yMax;
      yVel = 0;
      return true;
    }
    return false;
  }

  /** Affiche le jeton quand il est place */
  public void show() {
    app.noStroke();
    if(player1) {
      app.fill(colorPlayer1);
    } else {
      app.fill(colorPlayer2);
    }
    app.ellipse(x, y, radius * 2, radius * 2);
  }

  /**
   * Calcul la position verticale max que le jeton peut atteindre en cas de
   * presence d'autres jetons dans la colonne
   * @param placed Tableau des jetons places
   * @return Position verticale maximale
   */
  private float computeYMax(Jeton[][] placed) {
    int xIndex = app.mouseX / (radius * 2);
    if (xIndex >= placed.length) {
      xIndex = placed.length - 1;
    }
    if (xIndex < 0) {
      xIndex = 0;
    }
    int yIndex = 5;
    while (placed[xIndex][yIndex] != null) {
      yIndex--;
      if (yIndex == -1) {
        break;
      }
    }
    if (yIndex != -1) {
      return yIndex * radius * 2 + radius;
    }
    return 0;
  }
  
  // Getters
  public float getX() { return x; }
  public float getY() { return y; }
  public float getYMax() { return yMax; }
  public boolean isPlayer1() { return player1; }
}
