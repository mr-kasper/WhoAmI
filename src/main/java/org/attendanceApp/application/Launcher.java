package org.attendanceApp.application;

/**
 * Launcher class for the JavaFX application.
 * This class is needed when running JavaFX from a shaded/fat JAR
 * because of Java module system restrictions.
 */
public class Launcher {
  public static void main(String[] args) {
    Main.main(args);
  }
}
