package dominio;

public class Entity {
	private double area;
	private double color;
	private double circulaty;
	private double vermelho;
	private double azul;
	private double verde;
	private String classification;
	private String image;
	
	
	public Entity(double area, double color, double circulaty,double vermelho, double azul, double verde, String image, String classification) {
		this.area = area;
		this.circulaty = circulaty;
		this.vermelho = vermelho;
		this.azul = azul;
		this.verde = verde;
		this.setImage(image);
		this.setClassification(classification);
		this.color = color;
	}
	
	public double getCirculaty() {
		return circulaty;
	}

	public void setCirculaty(double circulaty) {
		this.circulaty = circulaty;
	}


	public double getAzul() {
		return azul;
	}

	public void setAzul(double azul) {
		this.azul = azul;
	}

	public double getVerde() {
		return verde;
	}

	public void setVerde(double verde) {
		this.verde = verde;
	}

	public double getVermelho() {
		return vermelho;
	}

	public void setVermelho(double vermelho) {
		this.vermelho = vermelho;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public double getColor() {
		return color;
	}

	public void setColor(double color) {
		this.color = color;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
}
