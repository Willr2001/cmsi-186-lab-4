import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RobotSoccerSimulation extends JPanel {
    private static final long serialVersionUID = -5228718339006830546L;

    // The soccer field
    private static double WIDTH = 400;
    private static double HEIGHT = 600;

    // Simulation parameters: in your lab, set these from command line arguments
    private static double PLAYER_RADIUS = 15;
    private static double ENEMY_RADIUS = 20;
    private static double PLAYER_SPEED = 1.3;
    private static double ENEMY_SPEED = 1.8;
    private static double FRICTION = 0.0009;

    // Initially null; Set this to a string to end the simulation
    private volatile String endMessage;

    static class Ball {
        private double x;
        private double y;
        private double radius;
        private double speed;
        private Color color;

        Ball(double x, double y, double radius, double speed, Color color) {
            // You know what to do here :)
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.speed = speed;
            this.color = color;

        }

        void moveToward(double targetX, double targetY) {
            // Fill this in
            var dx = targetX - this.x;
            var dy = targetY - this.y;
            var v = this.speed / Math.hypot(dx,dy);
            this.x = this.x + v * dx;


            this.x = constrain(this.x + v * dx, this.radius, Width - this.radius);

            this.y = constrain(this.y + v * dy, this.radius, HEIGHT - this.radius);

        }

        // Slow down the ball by FRICTION. Don't let it go negative, though!
        void applyFriction() {
            // Fill this in
          //  this.speed = constrain(this.speed - FRICTION, 0, double positive infinty)
        }

        // Returns whether the ball is *entirely* inside the goal
        boolean inside(Goal goal) {
            return false; // <--------- FIX THIS
        }
    }

    private static Ball[] balls = new Ball[] {
        new Ball(0.0, HEIGHT, PLAYER_RADIUS, PLAYER_SPEED, Color.BLUE),
        new Ball(WIDTH * 0.25, 40, ENEMY_RADIUS, ENEMY_SPEED, Color.RED),
        new Ball(WIDTH * 0.75, 40, ENEMY_RADIUS, ENEMY_SPEED, Color.RED),
        new Ball(WIDTH / 2, HEIGHT / 2, ENEMY_RADIUS, ENEMY_SPEED, Color.RED)
    };


    private static class Goal {
        double x = WIDTH / 2;
        double y = 0;
        double w = 100;
        double h = 100;
    }

    private static Goal goal = new Goal();

    // You don't need to touch this one.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (var ball : balls) {
            g.setColor(ball.color);
            g.fillOval((int) (ball.x - ball.radius), (int) (ball.y - ball.radius), (int) ball.radius * 2,
                    (int) ball.radius * 2);
        }
        g.setColor(new Color(255, 255, 255, 128));
        g.fillRect((int) (goal.x - goal.w / 2), (int) (goal.y - goal.h / 2), (int) goal.w, (int) goal.h);
        if (endMessage != null) {
            g.setFont(new Font("Arial", Font.PLAIN, 50));
            g.setColor(Color.RED);
            g.drawString(endMessage, 30, (int) HEIGHT / 2);
        }
    }

    private void runTheAnimation() {
        while (endMessage == null) {
          for (var i = 0; i < balls.length; i++) {
            balls[i].applyFriction();
            balls[i].moveToward(i == 0 ? goal.x : balls[0].x, i == 0 ? goal.y : balls[0].y );
          }



          repaint();
          try {
              Thread.sleep(10);
          } catch (InterruptedException e) {
          }
        }

    private static double constrain(double value, double low, double high){
      return Math.min(Math.max(low, value), high);
    }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var panel = new RobotSoccerSimulation();
            panel.setBackground(Color.GREEN.brighter());
            var frame = new JFrame("Robotic Soccer");
            frame.setSize(400, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
            new Thread(() -> panel.runTheAnimation()).start();
        });



    }
}
