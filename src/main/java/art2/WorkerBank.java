package art2;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class WorkerBank {
	private ArrayList<Point2D> array;

	public WorkerBank(ArrayList<Point2D> data) {
		this.array = new ArrayList<Point2D>(data);
	}
	
	public ArrayList<Point2D> getWorkerBank() {
		return array;
	}

}

