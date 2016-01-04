package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BuffPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel backgroundLabel;
	private JLabel substanceLabel;
	private BufferedImage substanceImg;

	public BuffPanel(String text) {
		setLayout(null);
		setOpaque(false);

		backgroundLabel = new JLabel();
		BufferedImage img = loadImage("images/hud/flask_empty.png");
		backgroundLabel.setIcon(new ImageIcon(img));
		backgroundLabel.setSize(img.getWidth(), img.getHeight());
		backgroundLabel.setLocation(img.getWidth() / 2, 0);
		add(backgroundLabel);

		substanceLabel = new JLabel();
		substanceImg = loadImage("images/hud/flask_substance.png");
		substanceLabel.setIcon(new ImageIcon(substanceImg));
		substanceLabel.setBounds(img.getWidth() / 2 + 3, 14, substanceImg.getWidth(), substanceImg.getHeight());
		substanceLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		add(substanceLabel);

		JLabel textLabel = new JLabel(text.split("\n")[0]);
		textLabel.setBounds(0, img.getHeight() - 20, 2 * img.getWidth(), 60);
		textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 9));
		textLabel.setForeground(Color.white);
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(textLabel);

		textLabel = new JLabel(text.split("\n")[1]);
		textLabel.setBounds(0, img.getHeight() - 10, 2 * img.getWidth(), 60);
		textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 9));
		textLabel.setForeground(Color.white);
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(textLabel);

		setComponentZOrder(backgroundLabel, 0);
		setComponentZOrder(substanceLabel, 0);
		setComponentZOrder(textLabel, 0);

		setSize(2 * img.getWidth(), img.getHeight() + 25);
	}

	public void updateFlask(double percent) {
		int height = (int) (percent * substanceImg.getHeight());
		if (height <= 0)
			height = 1;
		BufferedImage subImage = substanceImg.getSubimage(0, substanceImg.getHeight() - height, substanceImg.getWidth(),
				height);
		substanceLabel.setIcon(new ImageIcon(subImage));
	}

	private BufferedImage loadImage(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getClassLoader().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
}
