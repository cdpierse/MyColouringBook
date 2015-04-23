package tweetImage;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TwitterBot.BookCoverRetrieve;

 
public class image {
	
	private BufferedImage img = null;
	private String st = "resources"+File.separator+"tweetimages"+File.separator+"bookTemplate.png";
	private BookCoverRetrieve bcr = new BookCoverRetrieve();
	
	public image(String bookname, String author, String c, String colourname){ //Shane I added the page number so that I could save multiple page images.
		try {
		    img = ImageIO.read(new File(st));
		    if(c.charAt(0)=='#'){
		    	c = c.substring(1);
		    }
			String red=c.substring(0, 2);
			String green=c.substring(2, 4);
			String blue=c.substring(4, 6);
		    int r = Integer.parseInt(red, 16);
		    int g = Integer.parseInt(green, 16);
		    int b = Integer.parseInt(blue, 16);
		    String[] words = colourname.split("\\s+");
		    int linelength = 0;
		    ArrayList<String> para = new ArrayList<String>();
		    String line = "";
		    for(int x = 0; x<words.length;x++){
		    	String currentword = words[x];
		    	if(linelength+currentword.length()<63){
		    		line = line + currentword + " ";
		    		linelength = linelength +currentword.length()+1;
		    	}else{
		    		if(para.size()<32)para.add(line);
		    		line = currentword + " ";
		    		linelength = currentword.length()+1;
		    	}
		    }
		    
		   	for(String cur : para){
			    textImage text = new textImage(cur);
			    BufferedImage txt  = text.getTextImage();
			    txt = new TintImage(txt, new Color(r,g,b)).getTint();
			    for(int x = 0; x<txt.getWidth();x++){
			    	for(int y = 0;y<txt.getHeight();y++){
			    		int col1 = txt.getRGB(x, y);
			    		if(col1!=0){
			    			img.setRGB(x+455,y+(25+(txt.getHeight()*para.indexOf(cur))),col1);
			    		}
			    	}
			    }
		   	}

			BufferedImage cover = bcr.getBookImage(bookname, author);
			if(cover!=null){
				cover = getScaledImage(cover, 378, 493);
				for(int x = 58;x<436;x++){
					for(int y = 23;y<516;y++){
						int col2 = cover.getRGB(x-58, y-23);
						img.setRGB(x, y, col2);
					}
				}

		    
		    File f = new File("resources/tweetimages/tweetFile.png");
		    ImageIO.write(img, "PNG", f);
			}else{
				System.out.println("No mage found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getScaledImage(BufferedImage image, int width, int height) {
		try {
		    int imageWidth  = image.getWidth();
		    int imageHeight = image.getHeight();

		    double scaleX = (double)width/imageWidth;
		    double scaleY = (double)height/imageHeight;
		    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		    return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
}
