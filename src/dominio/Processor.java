package dominio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dominio.ImaJ.ImaJ;
import dominio.ImaJ.Properties;
import visao.ImageShow;
import dominio.ImaJ.Image;
import persistencia.ImageReader;

public class Processor {

	public List<Entity> process(File file) {
		ImageShow imageShow = new ImageShow();
		ArrayList<Entity> list = new ArrayList<>();
		int[][][] im = ImageReader.imRead(file.getPath());
		
		im = ImaJ._imResize(im);
//		Image.imWrite(im,  file.getPath().split("\\.")[0] + "_Resize.png");
		//int [][][] imResized = im;
//		int [][] filter = {{-1, 0 ,1},
//		{-2, 0, 2},
//		{-1, 0, 1}};
//		int [][] imFiltrada = ImaJ.imfilter(ImaJ.rgb2gray(im), filter);
//		Image.imWrite(imFiltrada, file.getPath().split("\\.")[0] + "_imFiltrada.png");
		
		int[][][] im_blur = ImaJ.imGaussian(im, 5);
//		int [][] imFiltradaBlur = ImaJ.imfilter(ImaJ.rgb2gray(im_blur), filter);
//		Image.imWrite(imFiltradaBlur, file.getPath().split("\\.")[0] + "_imFiltradaBlur.png");
	
		int[][] im_red = ImaJ.splitChannel(im_blur, 0);
//		Image.imWrite(im_blur, file.getPath().split("\\.")[0] + "_Gaussiano.png");
//		
//		imageShow.imShow(im_red, file.getPath(),"medianRed");
//		Image.imWrite(im_red,file.getPath().split("\\.")[0] + "_Vermelho.png");
		
		int[][] im_green = ImaJ.splitChannel(im_blur, 2);
//		imageShow.imShow(im_green, file.getPath(),"meidanGreen");
//		Image.imWrite(im_green, file.getPath().split("\\.")[0] + "_Verde.png");

		int[][] im_blue = ImaJ.splitChannel(im_blur, 1);
//		imageShow.imShow(im_blue, file.getPath(),"medianBlue");
//		Image.imWrite(im_blue,  file.getPath().split("\\.")[0] + "_Azul.png");


		int[][][] im_cmyk = ImaJ.rgb2cmyk(im_blur);
		
		int[][] im_cyan = ImaJ.splitChannel(im_cmyk, 0);
//		imageShow.imShow(im_cyan, file.getPath(), "Ciano");
//		Image.imWrite(im_cyan, file.getPath().split("\\.")[0] + "_Ciano.png");
	
		int[][] im_magenta = ImaJ.splitChannel(im_cmyk, 1);
//		imageShow.imShow(im_magenta, file.getPath(), "Magenta");
//		Image.imWrite(im_magenta, file.getPath().split("\\.")[0] + "_Magenta.png");

		int[][] im_yellow = ImaJ.splitChannel(im_cmyk, 2);
//		imageShow.imShow(im_yellow, file.getPath(), "Amarelo");
//		Image.imWrite(im_yellow, file.getPath().split("\\.")[0] + "_Amarelo.png");

		int[][] im_black = ImaJ.splitChannel(im_cmyk, 3);
//		imageShow.imShow(im_black, file.getPath(), "Preto");
//		Image.imWrite(im_black, file.getPath().split("\\.")[0] + "_Preto.png");
		
		boolean[][] im_mask = ImaJ.im2bw(im_yellow,30);
//		Image.imWrite(im_mask,  file.getPath().split("\\.")[0] + "_Mascara.png");
	
		boolean[][] imBordas = new boolean[im_mask.length][im_mask[0].length];
		
		boolean[][] imErode = ImaJ.bwErode(im_mask, 5);
//		Image.imWrite(imErode, file.getPath().split("\\.")[0] + "_Erosao.png");
//		Image.imWrite(im_mask, file.getPath().split("\\.")[0] + "_Bordas.png");

		ArrayList<Properties> objetos = ImaJ.regionProps(imErode);
		String classification = "";
		double medianRed=0, medianBlue=0, meidanGreen=0;
		
		for(int i = 0; i < objetos.size(); i++) {
			if(objetos.get(i).area > 500) {
				int[][][] im2 = ImaJ.imCrop(im, objetos.get(i).boundingBox[0], objetos.get(i).boundingBox[1], 
                        objetos.get(i).boundingBox[2], objetos.get(i).boundingBox[3]);

				int[][] imRecortemedianRed = ImaJ.imCrop(im_red, objetos.get(i).boundingBox[0], objetos.get(i).boundingBox[1], 
                        objetos.get(i).boundingBox[2], objetos.get(i).boundingBox[3]);
				
				int[][] imRecortemeidanGreen = ImaJ.imCrop(im_green, objetos.get(i).boundingBox[0], objetos.get(i).boundingBox[1], 
                        objetos.get(i).boundingBox[2], objetos.get(i).boundingBox[3]);
				
				int[][] imRecortemedianBlue = ImaJ.imCrop(im_blue, objetos.get(i).boundingBox[0], objetos.get(i).boundingBox[1], 
                        objetos.get(i).boundingBox[2], objetos.get(i).boundingBox[3]);
						
						// Aplicando máscara na imagem original
						for(int x = 0; x < im2.length; x++) {
							for(int y = 0; y < im2[0].length; y++) {
								//Se é pixel de fundo
								if(!objetos.get(i).image[x][y]) {
									im2[x][y] = new int[]{0,0,0};
								}else {
									medianRed += imRecortemedianRed[x][y];
									medianBlue += imRecortemeidanGreen[x][y];
									meidanGreen += imRecortemedianBlue[x][y];
								}
							}
						}
						
						medianRed /=  objetos.get(i).area;
						medianBlue /=  objetos.get(i).area;
						meidanGreen /=  objetos.get(i).area;
						classification = "desconhecido";
						 
						if(medianRed > 200 && medianBlue > 150 && meidanGreen > 185) {
							classification = "Folha de papel";
						}else if(medianBlue > 70 && medianBlue < 130) {
							classification = "Saboneteira";
						}else if(medianBlue < 70) {
							classification = "Tampa de Piloto";
						}
						
						ImageReader.imWrite(im2, file.getPath().split("\\.")[0] + "_" + i + classification+ ".png");
						list.add(new Entity(objetos.get(i).area,medianRed, medianBlue, meidanGreen, file.getPath().split("\\.")[0] + "_" + i + classification+ ".png", classification));			
						}
				
			}	
	
		return list;
	}
}