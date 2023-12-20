package dominio;

public class Entity {
	private double area;
	private double perimeter;
	private double circulaty;
	private double medianRed;
	private double medianBlue;
	private double medianGreen;
	private String classification;
	private String image;
	
	
	public Entity(double area, double perimeter, double circulaty,double medianRed, double medianBlue, double medianGreen, String image, String classification) {
		this.area = area;
		this.circulaty = circulaty;
		this.medianRed = medianRed;
		this.medianBlue = medianBlue;
		this.medianGreen = medianGreen;
		this.setImage(image);
		this.setClassification(classification);
		this.perimeter = perimeter;
	}
	
	public double getCirculaty() {
		return circulaty;
	}

	public void setCirculaty(double circulaty) {
		this.circulaty = circulaty;
	}


	public double getMedianBlue() {
		return medianBlue;
	}

	public void setMedianBlue(double medianBlue) {
		this.medianBlue = medianBlue;
	}

	public double getMedianGreen() {
		return medianGreen;
	}

	public void setMedianGreen(double medianGreen) {
		this.medianGreen = medianGreen;
	}

	public double getMedianRed() {
		return medianRed;
	}

	public void setmedianRed(double medianRed) {
		this.medianRed = medianRed;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public double getPerimeter() {
		return perimeter;
	}

	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
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
