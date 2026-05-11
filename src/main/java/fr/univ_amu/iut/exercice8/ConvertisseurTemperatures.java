package fr.univ_amu.iut.exercice8;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

/**
 * Exercice 8 (capstone) - Convertisseur de températures.
 *
 * <p>Cet exercice synthétise tous les types de bindings vus dans le TP :
 *
 * <ul>
 *   <li>Binding unidirectionnel (Labels de lecture)
 *   <li>Binding bidirectionnel (TextField ↔ Slider via {@link NumberStringConverter})
 *   <li>{@code ChangeListener} pour la conversion avec formule (C = (F-32)*5/9)
 *   <li>Sliders verticaux ({@code Orientation.VERTICAL})
 * </ul>
 *
 * <p>L'application affiche deux panneaux côte à côte : un pour Celsius, un pour Fahrenheit.
 * Modifier l'un met à jour l'autre automatiquement.
 */
public class ConvertisseurTemperatures extends Application {

  private boolean updating = false;

  @Override
  public void start(Stage primaryStage) {
    // TODO exercice 8 : construire le convertisseur de températures.
    //
    // 1. Créer le panneau Celsius (VBox) :
    // - Label "°C" (style bold, 16px)
    // - Slider vertical [0, 100], valeur initiale 0, id "slider-celsius"
    // - TextField, id "tf-celsius", maxWidth 50
    //
    // 2. Créer le panneau Fahrenheit (VBox) :
    // - Label "°F" (style bold, 16px)
    // - Slider vertical [0, 212], valeur initiale 32, id "slider-fahrenheit"
    // - TextField, id "tf-fahrenheit", maxWidth 50
    //
    // 3. Ajouter un ChangeListener sur le slider Celsius :
    // fahrenheit = celsius * 9/5 + 32
    // (utiliser un flag "updating" pour éviter les boucles infinies)
    //
    // 4. Ajouter un ChangeListener sur le slider Fahrenheit :
    // celsius = (fahrenheit - 32) * 5/9
    //
    // 5. Lier chaque TextField à son slider via
    // Bindings.bindBidirectional(tf.textProperty(), slider.valueProperty(),
    // new NumberStringConverter())
    //
    // 6. Composer les panneaux dans un HBox, créer la Scene, afficher.
    Label labelCelsius = new Label("°C");
    labelCelsius.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

    Slider sliderCelsius = new Slider(0, 100, 0);
    sliderCelsius.setOrientation(Orientation.VERTICAL);
    sliderCelsius.setId("slider-celsius");

    TextField tfCelsius = new TextField();
    tfCelsius.setId("tf-celsius");
    tfCelsius.setMaxWidth(50);

    Label labelFahrenheit = new Label("°F");
    labelFahrenheit.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

    Slider sliderFahrenheit = new Slider(0, 212, 32);
    sliderFahrenheit.setOrientation(Orientation.VERTICAL);
    sliderFahrenheit.setId("slider-fahrenheit");

    TextField tfFahrenheit = new TextField();
    tfFahrenheit.setId("tf-fahrenheit");
    tfFahrenheit.setMaxWidth(50);

    sliderCelsius
        .valueProperty()
        .addListener(
            (obs, old, newVal) -> {
              if (!updating) {
                updating = true;
                sliderFahrenheit.setValue(newVal.doubleValue() * 9.0 / 5.0 + 32);
                updating = false;
              }
            });
    sliderFahrenheit
        .valueProperty()
        .addListener(
            (obs, old, newVal) -> {
              if (!updating) {
                updating = true;
                sliderCelsius.setValue((newVal.doubleValue() - 32) * 5.0 / 9.0);
                updating = false;
              }
            });

    Bindings.bindBidirectional(
        tfCelsius.textProperty(), sliderCelsius.valueProperty(), new NumberStringConverter());
    Bindings.bindBidirectional(
        tfFahrenheit.textProperty(), sliderFahrenheit.valueProperty(), new NumberStringConverter());

    VBox celsiusPane = new VBox(10, labelCelsius, sliderCelsius, tfCelsius);
    celsiusPane.setAlignment(Pos.CENTER);

    VBox fahrenheitPane = new VBox(10, labelFahrenheit, sliderFahrenheit, tfFahrenheit);
    fahrenheitPane.setAlignment(Pos.CENTER);

    HBox root = new HBox(20, celsiusPane, fahrenheitPane);
    root.setAlignment(Pos.CENTER);

    Scene scene = new Scene(root, 300, 300);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
