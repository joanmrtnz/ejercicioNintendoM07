package com.project;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class UtilsViews {

    public static StackPane parentContainer = new StackPane();
    public static ArrayList<Object> controllers = new ArrayList<>();

    // Add one view to the list
    public static void addView(Class<?> cls, String name, String path) throws Exception {
        boolean defaultView = false;
        FXMLLoader loader = new FXMLLoader(cls.getResource(path));
        Pane view = loader.load();
        ObservableList<Node> children = parentContainer.getChildren();

        // First view is the default view
        if (children.isEmpty()) {
            defaultView = true;
        }

        view.setId(name);
        view.setVisible(defaultView);
        view.setManaged(defaultView);

        children.add(view);
        controllers.add(loader.getController());
    }

    // Get controller by view id (viewId)
    public static Object getController(String viewId) {
        int index = 0;
        for (Node n : parentContainer.getChildren()) {
            if (n.getId().equals(viewId)) {
                return controllers.get(index);
            }
            index++;
        }
        return null;
    }

    // Set visible view by its id (viewId)
    public static void setView(String viewId) {

        for (Node n : parentContainer.getChildren()) {
            if (n.getId().equals(viewId)) {
                n.setVisible(true);
                n.setManaged(true);
            } else {
                n.setVisible(false);
                n.setManaged(false);
            }
        }

        // Remove focus from buttons
        parentContainer.requestFocus();
    }

    // Set visible view by its id (viewId) with an animation
    public static void setViewAnimating(String viewId) {
        final Node[] curView = { null };
        final Node[] nxtView = { null };

        for (Node n : parentContainer.getChildren()) {
            if (n.isVisible()) {
                curView[0] = n;
            }
            if (n.getId().equals(viewId)) {
                nxtView[0] = n;
            }
        }

        if (curView[0] == null || nxtView[0] == null || curView[0].getId().equals(viewId)) {
            return; // Do nothing if current view is the same as the next view
        }

        // Set nxtView visible
        nxtView[0].setVisible(true);
        nxtView[0].setManaged(true);

        double width = parentContainer.getScene().getWidth();
        double fromXCurView = 0;
        double toXCurView = -width;
        double fromXNxtView = width;
        double toXNxtView = 0;

        if (parentContainer.getChildren().indexOf(curView[0]) > parentContainer.getChildren().indexOf(nxtView[0])) {
            fromXCurView = 0;
            toXCurView = width;
            fromXNxtView = -width;
            toXNxtView = 0;
        }

        curView[0].translateXProperty().set(fromXCurView);
        nxtView[0].translateXProperty().set(fromXNxtView);

        Timeline timeline = new Timeline();

        KeyValue kvCurView = new KeyValue(curView[0].translateXProperty(), toXCurView, Interpolator.EASE_BOTH);
        KeyFrame kfCurView = new KeyFrame(Duration.seconds(0.4), kvCurView);

        KeyValue kvNxtView = new KeyValue(nxtView[0].translateXProperty(), toXNxtView, Interpolator.EASE_BOTH);
        KeyFrame kfNxtView = new KeyFrame(Duration.seconds(0.4), kvNxtView);

        timeline.getKeyFrames().addAll(kfCurView, kfNxtView);
        timeline.setOnFinished(t -> {
            curView[0].setVisible(false);
            curView[0].setManaged(false);
            curView[0].translateXProperty().set(0);
            nxtView[0].translateXProperty().set(0);
        });
        timeline.play();

        // Remove focus from buttons
        parentContainer.requestFocus();
    }
}
