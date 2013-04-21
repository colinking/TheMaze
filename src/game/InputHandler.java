/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();

    public InputHandler(Game game) {
        game.addKeyListener(this);
    }
    public class Key {

        private boolean pressed = false;

        public void toggle(boolean isPressed) {
            pressed = isPressed;
        }
        public boolean getPressed() {
            return pressed;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void toggleKey(int keyCode, boolean isPressed) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                up.toggle(isPressed);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                down.toggle(isPressed);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                left.toggle(isPressed);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                right.toggle(isPressed);
                break;
        }
    }
}
