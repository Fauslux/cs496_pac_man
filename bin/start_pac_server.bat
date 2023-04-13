:: This file is used to compile and run a Pac-Man game server
@echo off
javac common/*.java server/*.java --module-path "C:\Users\Dillon Orr\Documents\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml
java server.PacmanServerDriver 5699