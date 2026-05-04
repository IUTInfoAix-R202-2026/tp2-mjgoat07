package fr.univ_amu.iut.exercice3;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringExpression;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Exercice 3 - Palette réactive (pont avec le TP1).
 *
 * <p>Cet exercice reprend la Palette du TP1 (exercice 6) et la refactorise avec des propriétés
 * JavaFX. Le comportement est identique, mais l'implémentation est supérieure :
 *
 * <ul>
 *   <li>TP1 : {@code int[] compteurs} + {@code setText()} dans chaque handler (3x le meme code)
 *   <li>TP2 : {@code IntegerProperty nbClics} dans chaque {@link BoutonCouleur} + 1 binding
 * </ul>
 *
 * <p>Comportement attendu :
 *
 * <pre>
 * +------------------------------+
 * | [Rouge] [Vert] [Bleu]        |  HBox de 3 BoutonCouleur
 * +------------------------------+
 * |                              |
 * |     (zone de couleur)        |  Pane #zone dont le fond change
 * |                              |
 * +------------------------------+
 * | Rouge: 0  Vert: 0  Bleu: 0  |  Label #compteurs (bind)
 * +------------------------------+
 * </pre>
 *
 * @see BoutonCouleur
 */
public class PaletteReactive extends Application {

  @Override
  public void start(Stage primaryStage) {
    // TODO exercice 3 : réimplémenter la Palette du TP1 avec des propriétés.
    //
    // 1. Créer un BorderPane comme racine.
    //
    // 2. Top : un HBox avec trois BoutonCouleur :
    // - new BoutonCouleur("Rouge", "red") id: "btn-rouge"
    // - new BoutonCouleur("Vert", "green") id: "btn-vert"
    // - new BoutonCouleur("Bleu", "blue") id: "btn-bleu"
    //
    // 3. Center : un Pane avec l'id "zone", taille minimale 300x200.
    //
    // 4. Bottom : un Label avec l'id "compteurs".
    //
    // 5. Appeler createBindings() pour lier le label et la zone aux boutons.
    //
    // 6. Créer la Scene, l'attacher au Stage, afficher.
    BorderPane root = new BorderPane();
    BoutonCouleur btnRouge = new BoutonCouleur("Rouge", "red");
    btnRouge.setId("btn-rouge");
    BoutonCouleur btnVert = new BoutonCouleur("Vert", "green");
    btnVert.setId("btn-vert");
    BoutonCouleur btnBleu = new BoutonCouleur("Bleu", "blue");
    btnBleu.setId("btn-bleu");
    HBox hbox = new HBox(btnRouge, btnVert, btnBleu);
    root.setTop(hbox);
    Pane zone = new Pane();
    zone.setId("zone");
    zone.setMinSize(300, 200);
    root.setCenter(zone);
    Label labelCompteurs = new Label("Bienvenue !");
    labelCompteurs.setId("compteurs");
    root.setBottom(labelCompteurs);
    createBindings(btnRouge, btnVert, btnBleu, zone, labelCompteurs);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  /**
   * Crée les bindings entre les boutons, la zone de couleur et le label compteurs.
   *
   * <p>Cette méthode remplace les 3 handlers {@code setOnAction} du TP1 par des bindings
   * déclaratifs. Après cette méthode, plus aucun {@code setText()} n'est nécessaire : le label se
   * met à jour automatiquement quand un compteur change.
   */
  void createBindings(
      BoutonCouleur btnRouge,
      BoutonCouleur btnVert,
      BoutonCouleur btnBleu,
      Pane zone,
      Label labelCompteurs) {
    // TODO exercice 3 : créer les bindings.
    //
    // 1. Pour chaque bouton, ajouter un handler setOnAction (en plus de celui
    // du BoutonCouleur) qui change le style de la zone :
    // zone.setStyle("-fx-background-color: " + btn.getCouleur() + ";")
    // Note : le BoutonCouleur incrémente déjà nbClics dans son propre handler.
    // L'ajout d'un 2e handler via addEventHandler(ActionEvent.ACTION, ...) ou
    // en encapsulant l'ancien fonctionne aussi, mais le plus simple est
    // d'utiliser un ChangeListener sur nbClicsProperty() pour changer la couleur.
    //
    // 2. Créer une StringExpression avec Bindings.concat() :
    // "Rouge: " + btnRouge.nbClicsProperty().asString()
    // + " Vert: " + btnVert.nbClicsProperty().asString()
    // + " Bleu: " + btnBleu.nbClicsProperty().asString()
    //
    // 3. Lier labelCompteurs.textProperty() à cette expression via bind().
    //
    // 4. (Optionnel) Utiliser Bindings.when() pour afficher "Bienvenue !"
    // quand aucun bouton n'a été cliqué, et le texte des compteurs sinon.
    btnRouge
        .nbClicsProperty()
        .addListener((obs, o, n) -> zone.setStyle("-fx-background-color: red;"));
    btnVert
        .nbClicsProperty()
        .addListener((obs, o, n) -> zone.setStyle("-fx-background-color: green;"));
    btnBleu
        .nbClicsProperty()
        .addListener((obs, o, n) -> zone.setStyle("-fx-background-color: blue;"));

    StringExpression texteCompteurs =
        Bindings.concat(
            "Rouge: ", btnRouge.nbClicsProperty().asString(),
            "  Vert: ", btnVert.nbClicsProperty().asString(),
            "  Bleu: ", btnBleu.nbClicsProperty().asString());

    NumberBinding total =
        btnRouge.nbClicsProperty().add(btnVert.nbClicsProperty()).add(btnBleu.nbClicsProperty());

    labelCompteurs
        .textProperty()
        .bind(Bindings.when(total.isEqualTo(0)).then("Bienvenue !").otherwise(texteCompteurs));
  }

  public static void main(String[] args) {
    launch(args);
  }
}
