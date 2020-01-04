package exterior;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class DemoScrollBarUI extends BasicScrollBarUI {
    private static final int thumbWidth = 10;
    private static final int thumbHeight = 20;
    private static final float opaque = 0.5f;
    private static final Color thumbColor = new Color(51, 47, 154);
    private static final Color thumbColorFrom = new Color(51, 47, 154);
    private static final Color thumbColorTo = new Color(51, 47, 154);
    private static final Color backColorFrom = new Color(255, 255, 255);
    private static final Color backColorTo = new Color(255, 255, 255);

    @Override
    protected void configureScrollBarColors() {
        setThumbBounds(0, 0, 3, 10);
    }

    /**
     * 设置滚动条的宽度
     */
    @Override
    public Dimension getPreferredSize(JComponent c) {

        // TODO Auto-generated method stub

        //        c.setPreferredSize(new Dimension(thumbWidth, 0));
        c.setPreferredSize(new Dimension(thumbWidth, thumbWidth));
        return super.getPreferredSize(c);
    }

    // 重绘滑块的滑动区域背景

    @Override
    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {

        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gp = null;
        if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            //设置画笔
            // 颜色渐变
            gp = new GradientPaint(0, 0, backColorFrom, 0, trackBounds.height, backColorTo);

        }
        if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
            gp = new GradientPaint(0, 0, backColorFrom, trackBounds.width, 0, backColorTo);
        }
        g2.setPaint(gp);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        if (trackHighlight == BasicScrollBarUI.DECREASE_HIGHLIGHT) {
            this.paintDecreaseHighlight(g);
        }
        if (trackHighlight == BasicScrollBarUI.INCREASE_HIGHLIGHT) {
            this.paintIncreaseHighlight(g);
        }

    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {

        g.translate(thumbBounds.x, thumbBounds.y);
        g.setColor(thumbColor);
        g.drawRoundRect(0, 0, thumbBounds.width - 1, thumbBounds.height - 1, 5, 5);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.addRenderingHints(rh);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opaque));
        g2.setPaint(new GradientPaint(c.getWidth() / 2, 1, thumbColorFrom, c.getWidth() / 2, c.getHeight(),
                thumbColorTo));
        g2.fillRoundRect(0, 0, thumbBounds.width - 1, thumbBounds.height - 1, 5, 5);
    }

    /**
     * 创建滚动条上方的按钮
     */

    @Override
    protected JButton createIncreaseButton(int orientation) {

        JButton button = new JButton();
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setBorder(null);
        return button;

    }

    /**
     * 创建滚动条下方的按钮
     */

    @Override
    protected JButton createDecreaseButton(int orientation) {

        JButton button = new JButton();
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setFocusable(false);
        button.setBorder(null);

        return button;

    }

    public static void main(String[] args) {

        JFrame jf = new JFrame("测试窗口");
        jf.setSize(1500, 800);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel jp = new JPanel();

        GridLayout gridLayout = new GridLayout(4, 0, 0, 0);
        jp.setLayout(gridLayout);

        for (int index = 0; index < 1000; index++) {
            jp.add(new JButton("若水2019-12-23"));
        }

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getHorizontalScrollBar().setUI(new DemoScrollBarUI());
        scrollPane.setViewportView(jp);
        jf.add(scrollPane);
        jf.setVisible(true);

    }
}