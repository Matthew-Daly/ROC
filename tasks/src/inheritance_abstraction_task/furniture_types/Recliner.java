package inheritance_abstraction_task.furniture_types;

import inheritance_abstraction_task.Furniture;

public class Recliner extends Furniture {

	private String material;

	// Chain constructor to add properties of recliner.
	public Recliner(int seatCount, double price, String name, String material) {
		super(seatCount, price, name);
		this.material = material;
	}
	
	// Print properties of a recliner.
	@Override
	public void DescribeFurniture() {
		super.DescribeFurniture();
		System.out.println("Material: "+material);
	}

	// Getters and Setters
	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}
}
