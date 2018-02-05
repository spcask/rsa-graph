// Plot of (p + q) vs. (p * q) for primes p and q.

// Author: Susam Pal
// Date: 2007-11-03
// License: MIT

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;

public class RSAGraph extends JFrame
{
  public static void main(String[] args)
  {
    GraphFrame frame = new GraphFrame();
  }
}

class GraphFrame extends JFrame
{
  public GraphFrame()
  {
    Toolkit kit = Toolkit.getDefaultToolkit();
    int height = kit.getScreenSize().height;
    int width = kit.getScreenSize().width;

    setBounds(10, 25, width - 20, height - 50);
    getContentPane().add(new GraphPanel());

    setTitle("RSA Graph");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    show();
  }
}

class GraphPanel extends JPanel
{
  private Color thin = new Color(0xAAAAAA);
  private Color dark = new Color(0x555555);
  private Color label = new Color(0x000000);
  private Color black = new Color(0x000000);

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    setBackground(new Color(0xFFFFFF));
    int height = getHeight();
    int width = getWidth();
    Graphics2D g2 = (Graphics2D)g;

    Line2D.Double xx;
    Line2D.Double yy;

    // Calculate font sizes, margins, position of axes
    Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
    g2.setFont(labelFont);

    FontRenderContext context = g2.getFontRenderContext();
    Rectangle2D bounds = labelFont.getStringBounds("8888", context);

    int offsetX = (int)bounds.getHeight() + 10;
    int offsetY = (int)bounds.getWidth() + 10;
    int labelX = offsetX - (int)bounds.getHeight();
    int labelY = offsetY - (int)bounds.getWidth() - 2;
    int margin = 2;

    // Origin and Scale
    int originX = 0;
    int originY = 0;
    double scaleX = 1;
    double scaleY = 1;

    // Create thin grid
    g2.setColor(thin);
    for(int i = 0; i < height - offsetX; i += 10)
    {
      xx = new Line2D.Double(0, height - offsetX - i, width,
              height - offsetX - i);
      g2.draw(xx);
    }
    for(int i = 0; i < width - offsetY; i += 10)
    {
      yy = new Line2D.Double(offsetY + i, 0, offsetY + i, height);
      g2.draw(yy);
    }

    // Create dark grid
    int i;
    for(i = 0; i < height - offsetX; i += 50)
    {
      g2.setColor(dark);
      xx = new Line2D.Double(0, height - offsetX - i, width,
              height - offsetX - i);
      g2.draw(xx);

      g2.setColor(label);
      g2.drawString((int)(originY + i * scaleY) + "", labelY,
              height - offsetX - i - margin);
    }
    for(i = 0; i < width - offsetY; i += 50)
    {
      g2.setColor(dark);
      yy = new Line2D.Double(offsetY + i, 0, offsetY + i, height);
      g2.draw(yy);

      g2.setColor(label);
      g2.drawString((int)(originX + i * scaleX) + "", offsetY + i + margin,
              height - labelX);
    }

    // Determine suitable limits for p and q
    int pMax, qMax;
    pMax = qMax = (int)(originX + i * scaleX)/2;

    // Draw axes
    g2.setColor(black);
    xx = new Line2D.Double(0, height - offsetX, width, height - offsetX);
    yy = new Line2D.Double(offsetY, 0, offsetY, width);
    g2.draw(xx);
    g2.draw(yy);

    // Double the axes
    xx = new Line2D.Double(0, height - (offsetX - 1), width,
            height - (offsetX - 1));
    yy = new Line2D.Double(offsetY - 1, 0, offsetY - 1, height);
    g2.draw(xx);
    g2.draw(yy);

    // Draw graph
    g2.setColor(new Color(0x0000FF));
    for(int p = 2; p <= pMax; p++)
    {
      if(!isPrime(p))
        continue;
      for(int q = 2; q <= qMax; q++)
      {
        if(!isPrime(q))
          continue;

        // Equations for (x, y) co-ordinate
        int x = p * q;
        int y = p + q;

        System.out.println("p: " + p + "; q: " + q + "; " +
                           "x: " + x + "; y: " + y);

        // Translate (x, y) co-ordinate to (x, y) on the JPanel
        x = (int)((x - originX) / scaleX + offsetY);
        y = (int)((height - offsetX) - (y - originY) / scaleY);

        // Plot the point
        int thickness = 4;
        Ellipse2D point = new Ellipse2D.Double(
            x - thickness/2, y - thickness/2, thickness, thickness);
        g2.fill(point);
      }
    }
  }

  public static boolean isPrime(int n)
  {
    if(n < 0)
      n = -n;
    if(n == 0 || n == 1)
      return false;

    int limit = (int)Math.sqrt(n);

    for(int i = 2; i <= limit; i++)
    {
      if(n % i == 0)
        return false;
    }

    return true;
  }

}
