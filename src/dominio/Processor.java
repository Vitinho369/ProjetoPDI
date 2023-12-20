package dominio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dominio.ImaJ.ImaJ;
import dominio.ImaJ.Image;
import dominio.ImaJ.Properties;
import persistencia.ImageReader;
import visao.ImageShow;
import java.util.Arrays;

public class Processor {

	public List<Entity> process(File file) {
		ImageShow imageShow = new ImageShow();
		
		ArrayList<Entity> list = new ArrayList<>();
		int[][][] im = ImageReader.imRead(file.getPath());
		//Image.imWrite(im, "C:/Users/vitin/Documents/TADS/PDI/ProjetoPDI/Results/original.png");

		ArrayList<String> classifications = new ArrayList<String>(); 
		ArrayList<Double> areas = new ArrayList<Double>(); 
		
		im = ImaJ._imResize(im);
		//int [][][] imResized = im;
		//Image.imWrite(im, "C:/Users/vitin/Documents/TADS/PDI/ProjetoPDI/Results/reduzida.png");

		int[][][] im_blur = ImaJ.imGaussian(im, 5);
	
		//int[][] im_gray = ImaJ.rgb2gray(im_blur);


		int[][] im_red = ImaJ.splitChannel(im_blur, 0);
//		imageShow.imShow(im_red, file.getPath(),"medianRed");
		//Image.imWrite(im_blur, "C:/Users/vitin/Documents/TADS/PDI/ProjetoPDI/Results/blur.png");

		int[][] im_green = ImaJ.splitChannel(im_blur, 2);
//		imageShow.imShow(im_green, file.getPath(),"meidanGreen");

		int[][] im_blue = ImaJ.splitChannel(im_blur, 1);
//		imageShow.imShow(im_blue, file.getPath(),"medianBlue");

		int[][][] im_cmyk = ImaJ.rgb2cmyk(im_blur);
		
		int[][] im_cyan = ImaJ.splitChannel(im_cmyk, 0);
//		imageShow.imShow(im_cyan, file.getPath(), "Ciano");
		//Image.imWrite(im_cyan, "C:/Users/vitin/Documents/TADS/PDI/ProjetoPDI/Results/ciano.png");

		int[][] im_magenta = ImaJ.splitChannel(im_cmyk, 1);
//		imageShow.imShow(im_magenta, file.getPath(), "Magenta");
//		Image.imWrite(im_magenta, "C:\\Users\\vitin\\Documents\\TADS\\Banco de imagens - Projeto PDI\\Results\\magenta.png");

		int[][] im_yellow = ImaJ.splitChannel(im_cmyk, 2);
//		imageShow.imShow(im_yellow, file.getPath(), "Amarelo");
		//Image.imWrite(im_yellow, "C:/Users/vitin/Documents/TADS/PDI/ProjetoPDI/Results/amarelo.png");

		int[][] im_black = ImaJ.splitChannel(im_cmyk, 3);
//		imageShow.imShow(im_black, file.getPath(), "Preto");
		//Image.imWrite(im_black, "C:/Users/vitin/Documents/TADS/PDI/ProjetoPDI/Results/preto.png");

		
		boolean[][] im_mask = ImaJ.im2bw(im_yellow,30);
		//Image.imWrite(im_mask, "C:/Users/vitin/Documents/TADS/PDI/ProjetoPDI/Results/mascara.png");

		boolean[][] imBordas = new boolean[im_mask.length][im_mask[0].length];
		
		boolean[][] imErode = ImaJ.bwErode(im_mask, 5);
		boolean[][] imErode2 = ImaJ.bwErode(im_mask, 2);
		
		for(int i=0; i < im_mask.length; i++) {
			for(int j=0; j < im_mask[0].length;j++) {
				imBordas[i][j] = im_mask[i][j] && !(imErode2[i][j]);
			}
		}
//		imageShow.imShow(imBordas,file.getPath());
//		boolean[][] imDilate = ImaJ.bwDilate(imErode, 3);
//		imageShow.imShow(imErode, file.getPath());
		//Image.imWrite(imErode, "C:/Users/vitin/Documents/TADS/PDI/ProjetoPDI/Results/erodida.png");

		ArrayList<Properties> sementes = ImaJ.regionProps(imErode);
		double perimetro =0;
		double circulaty;
		String classification = "";
		double medianRed=0, medianBlue=0, meidanGreen=0;
		int qtdPixels=0;
		for(int i = 0; i < sementes.size(); i++) {
			perimetro=0;
			qtdPixels=0;
			if(sementes.get(i).area > 500) {
				int[][][] im2 = ImaJ.imCrop(im, sementes.get(i).boundingBox[0], sementes.get(i).boundingBox[1], 
                        sementes.get(i).boundingBox[2], sementes.get(i).boundingBox[3]);

				int[][] imRecortemedianRed = ImaJ.imCrop(im_red, sementes.get(i).boundingBox[0], sementes.get(i).boundingBox[1], 
                        sementes.get(i).boundingBox[2], sementes.get(i).boundingBox[3]);
				
				int[][] imRecortemeidanGreen = ImaJ.imCrop(im_green, sementes.get(i).boundingBox[0], sementes.get(i).boundingBox[1], 
                        sementes.get(i).boundingBox[2], sementes.get(i).boundingBox[3]);
				
				int[][] imRecortemedianBlue = ImaJ.imCrop(im_blue, sementes.get(i).boundingBox[0], sementes.get(i).boundingBox[1], 
                        sementes.get(i).boundingBox[2], sementes.get(i).boundingBox[3]);
				
				boolean[][] imMedian = ImaJ.imCrop(imBordas, sementes.get(i).boundingBox[0], sementes.get(i).boundingBox[1], 
                        sementes.get(i).boundingBox[2], sementes.get(i).boundingBox[3]);
						
						// Aplicando máscara na imagem original
						for(int x = 0; x < im2.length; x++) {
							for(int y = 0; y < im2[0].length; y++) {
								//Se é pixel de fundo
								if(!sementes.get(i).image[x][y]) {
									im2[x][y] = new int[]{0,0,0};
								}else {
									medianRed += imRecortemedianRed[x][y];
									medianBlue += imRecortemeidanGreen[x][y];
									meidanGreen += imRecortemedianBlue[x][y];
									qtdPixels++;
								}
								perimetro += imMedian[x][y] ? 1 : 0;
							}
						}
						
						medianRed /=  sementes.get(i).area;
						medianBlue /=  sementes.get(i).area;
						meidanGreen /=  sementes.get(i).area;
						classification = "desconhecido";
						
		
						circulaty =  (4* Math.PI * sementes.get(i).area)/(perimetro*perimetro); 
						if(medianRed > 200 && medianBlue > 150 && meidanGreen > 185) {
							classification = "Folha de papel";
						}else if(circulaty > 0.5 && circulaty < 1 && medianBlue > 70 && medianBlue < 130) {
							classification = "Saboneteira";
						}else if(circulaty >= 1 || medianBlue < 70) {
							classification = "Tampa de Piloto";
						}
						
						ImageReader.imWrite(im2, file.getPath().split("\\.")[0] + "_" + i + classification+ ".png");
						//ImageReader.imWrite(im2,"C:/Users/vitin/Documents/TADS/PDI/ProjetoPDI/Results/folha_" + i + ".png");
						list.add(new Entity(sementes.get(i).area, perimetro,circulaty,medianRed, medianBlue, meidanGreen, file.getPath().split("\\.")[0] + "_" + i + classification+ ".png", classification));			
						}
				
			}	
	
		return list;
	}
}