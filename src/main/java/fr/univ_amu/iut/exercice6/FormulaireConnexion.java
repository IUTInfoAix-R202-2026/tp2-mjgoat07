package fr.univ_amu.iut.exercice6;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Exercice 6 - Formulaire de connexion avec bindings de validation.
 *
 * <p>Cet exercice montre comment les bindings permettent de gérer l'état des contrôles (éditable,
 * disable) de manière déclarative. C'est un exemple concret d'<b>affordance</b> (concept CM2) : les
 * contrôles désactivés communiquent visuellement les exigences à l'utilisateur.
 *
 * <p>Règles de validation :
 *
 * <ul>
 *   <li>Le champ mot de passe n'est éditable que si l'identifiant contient au moins 6 caractères
 *   <li>Le bouton OK n'est actif que si le mot de passe est valide (>= 8 chars, 1 majuscule, 1
 *       chiffre)
 *   <li>Le bouton Annuler est désactivé si les deux champs sont vides
 * </ul>
 *
 * <p>Concepts :
 *
 * <ul>
 *   <li>{@code editableProperty().bind(...)}
 *   <li>{@code disableProperty().bind(...)}
 *   <li>Low-level {@link BooleanBinding} avec {@code computeValue()} personnalisé
 *   <li>Pattern {@code createBindings()}
 * </ul>
 */
public class FormulaireConnexion extends Application {

  private TextField userId;
  private PasswordField pwd;
  private Button okBtn;
  private Button cancelBtn;
  private Label message;

  @Override
  public void start(Stage primaryStage) {
    // TODO exercice 6 : construire le formulaire et créer les bindings.
    //
    // 1. Créer un GridPane avec padding 20, hgap 10, vgap 10.
    //
    // 2. Ajouter les composants :
    // (0,0) Label "Identifiant :" (1,0) TextField userId (id: "user-id")
    // (0,1) Label "Mot de passe :" (1,1) PasswordField pwd (id: "pwd")
    // (0,2) Button okBtn "OK" (id: "btn-ok")
    // (1,2) Button cancelBtn "Annuler" (id: "btn-cancel")
    // (0,3) Label message (id: "message", colspan 2)
    //
    // 3. Appeler createBindings().
    //
    // 4. Ajouter les handlers okClicked() et cancelClicked().
    //
    // 5. Créer la Scene, l'attacher au Stage, afficher.
    GridPane root = new GridPane();
    userId = new TextField();
    userId.setId("user-id");
    pwd = new PasswordField();
    pwd.setId("pwd");
    okBtn = new Button("OK");
    okBtn.setId("btn-ok");
    cancelBtn = new Button("Annuler");
    cancelBtn.setId("btn-cancel");
    message = new Label();
    message.setId("message");
    root.add(new Label("Identifiant"), 0, 0);
    root.add(userId, 1, 0);
    root.add(new Label("Mot de passe"), 0, 1);
    root.add(pwd, 1, 1);
    root.add(okBtn, 0, 2);
    root.add(cancelBtn, 1, 2);
    GridPane.setColumnSpan(message, 2);
    root.add(message, 0, 3);
    createBindings();
    okBtn.setOnAction(e -> okClicked());
    cancelBtn.setOnAction(e -> cancelClicked());
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  /** Crée les bindings de validation. */
  void createBindings() {
    // TODO exercice 6 : créer les bindings de validation.
    //
    // 1. Le mot de passe n'est éditable que si userId >= 6 caractères :
    // pwd.editableProperty().bind(
    // Bindings.greaterThanOrEqual(userId.textProperty().length(), 6))
    //
    // 2. Le bouton Annuler est désactivé si les deux champs sont vides :
    // cancelBtn.disableProperty().bind(
    // Bindings.and(
    // Bindings.equal(0, pwd.textProperty().length()),
    // Bindings.equal(0, userId.textProperty().length())))
    //
    // 3. Le bouton OK est désactivé par un BooleanBinding personnalisé :
    // Créer une classe anonyme new BooleanBinding() { ... }
    // - dans le bloc d'initialisation : super.bind(pwd.textProperty())
    // - dans computeValue() : retourner true si le mot de passe est
    // trop court (< 8) OU ne contient pas de majuscule OU pas de chiffre
    // okBtn.disableProperty().bind(binding)
    pwd.editableProperty().bind(userId.textProperty().length().greaterThanOrEqualTo(6));
    cancelBtn
        .disableProperty()
        .bind(userId.textProperty().isEmpty().and(pwd.textProperty().isEmpty()));
    BooleanBinding formValide =
        new BooleanBinding() {
          {
            super.bind(userId.textProperty(), pwd.textProperty());
          }

          @Override
          protected boolean computeValue() {
            String p = pwd.getText();
            return userId.getText().length() >= 6
                && p.length() >= 8
                && p.chars().anyMatch(Character::isUpperCase)
                && p.chars().anyMatch(Character::isDigit);
          }
        };
    okBtn.disableProperty().bind(formValide.not());
  }

  void okClicked() {
    // TODO exercice 6 : afficher l'identifiant et le mot de passe masqué.
    message.setText(
        "Bienvenue " + userId.getText() + " (" + "*".repeat(pwd.getText().length()) + ")");
  }

  void cancelClicked() {
    // TODO exercice 6 : vider les deux champs et le label message.
    userId.clear();
    pwd.clear();
    message.setText("");
  }

  public static void main(String[] args) {
    launch(args);
  }
}
