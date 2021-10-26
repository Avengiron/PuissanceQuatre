# PuissanceQuatre
Jeu du puissance 4

(version java d'un projet Processing, nécessite la bibliothèque processing core.jar)

### Déroulement du jeu

Les joueurs jouent à tour de rôle, en plaçant des jetons jusqu'à en aligner 4 consécutifs (en ligne, colonne, ou diagonale).
Avant que le joueur ne place son jeton, on lui montre dans quel colonne son jeton sera placé, et à quel emplacement il finira.
La prévisualisation du jeton est grisée si le joueur vise une colonne déjà remplie. 
Un nouveau jeton ne peut pas etre placé pendant l'animation du jeton précédent.

![Exemple](https://github.com/Avengiron/HostReadMeImages/blob/main/PuissanceQuatre/Exemple.png)


Un message apparaît pour annoncer la fin du jeu, annonce le gagnant s'il y en a un, ou un match nul. 
On propose également de regenerer le plateau pour une nouvelle partie.

![Victoire d'un joueur](https://github.com/Avengiron/HostReadMeImages/blob/main/PuissanceQuatre/FinVictoire.png)

![Match nul](https://github.com/Avengiron/HostReadMeImages/blob/main/PuissanceQuatre/FinMatchNul.png)


### Grille de jeu

Le petit plus de ce code, c'est l'affichage de la grille de jeu qui laisse voir les jetons tomber à travers.
Dans Processing, on ne peut faire faire de formes totalement transparente qui laisseraient voir ce qu'il y a derrière. 
Tout ce qui est affiché est cumulatif, donc si on trace un cercle par dessus un rectangle, le rectangle sera caché. 
Pour passer outre cette limitation, on crée nous-même une `PShape` qui ressemble à un rectangle dans lequel on creuse un cercle.

```java 
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
```

Le résultat fait que l'on peut afficher un jeton, puis la grille par dessus, et on verra quand même le jeton derrière 

![Grille](https://github.com/Avengiron/HostReadMeImages/blob/main/PuissanceQuatre/EmptyCircle.gif)
