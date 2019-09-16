package com.dy.Util;

import java.io.File;
import java.util.LinkedList;

import com.dy.Util.Math.Point3D;
import com.dy.Util.Math.Triangle;

class OBJUtil {

	public static ObjectFile loadOBJFile(String path) {
		return OBJUtil.loadOBJFile(new File(path));
	}

	public static ObjectFile loadOBJFile(File f) {
		return ObjectFile.loadFromFile(f);
	}

	public static Double computeArea(ObjectFile target) throws Error {
		Point3D[] geometricVertices = target.getGeometricVertices();
		ModelFaceIndex[][] modelFaceIndex = target.getModelFaceIndex();
		Double ret = 0.0;
		for (int i = 0; i < modelFaceIndex.length; i++) {
			ModelFaceIndex[] tmp = modelFaceIndex[i];
			if (tmp.length != 3) {
				throw new Error("ObjectFile is not valid!");
			}
			ret += Triangle.computeArea(geometricVertices[tmp[0].verticeIndex], geometricVertices[tmp[1].verticeIndex],
					geometricVertices[tmp[2].verticeIndex]);

		}
		return ret;
	}

	public static void main(String[] args) {
		ObjectFile tmp = OBJUtil.loadOBJFile("C:\\Users\\hp\\Desktop\\Tile_+001_+006\\Tile_+001_+006.obj");
		System.out.println(OBJUtil.computeArea(tmp));
	}

}

class ObjectFile {
	private Point3D[] geometricVertices;
	/*
	 * 模型面 f verticeIndex/vtIndex/vnIndex verticeIndex/vtIndex/vnIndex
	 * verticeIndex/vtIndex/vnIndex 其中 vtIndex/vnIndex为可选配置
	 */
	private ModelFaceIndex[][] modelFaceIndex;

	public Point3D[] getGeometricVertices() {
		return geometricVertices;
	}

	public void setGeometricVertices(Point3D[] geometricVertices) {
		this.geometricVertices = geometricVertices;
	}

	public ModelFaceIndex[][] getModelFaceIndex() {
		return modelFaceIndex;
	}

	public void setModelFaceIndex(ModelFaceIndex[][] modelFaceIndex) {
		this.modelFaceIndex = modelFaceIndex;
	}

	public ObjectFile(LinkedList<Point3D> geometricVertices, LinkedList<ModelFaceIndex[]> modelFaceIndex) {
		this.geometricVertices = geometricVertices.toArray(new Point3D[geometricVertices.size()]);
		this.modelFaceIndex = modelFaceIndex.toArray(new ModelFaceIndex[modelFaceIndex.size()][]);
	}

	public ObjectFile() {
		this.geometricVertices = null;
		this.modelFaceIndex = null;
	}

	public static ObjectFile fromString(String objFile) {
		String spliter = File.separator.equals("/") ? "\n" : "\r\n";
		String[] splited = objFile.split(spliter);
		LinkedList<Point3D> geometricVertices = new LinkedList<Point3D>();
		LinkedList<ModelFaceIndex[]> modelFaceIndex = new LinkedList<ModelFaceIndex[]>();
		for (int i = 0; i < splited.length; i++) {
			String tmp = splited[i];
			String target = tmp.substring(0, tmp.indexOf(" "));
			switch (target) {
			case "v":
				geometricVertices.push(Point3D.fromString(tmp.substring(2)));
				break;
			case "f":
				modelFaceIndex.push(ModelFaceIndex.fromFaceIndexString(tmp));
				break;
			}
		}
		return new ObjectFile(geometricVertices, modelFaceIndex);
	}

	public static ObjectFile loadFromFile(File f) {
		return ObjectFile.fromString(FileUtil.readString(f));
	}
}

class ModelFaceIndex {
	public Integer verticeIndex;
	public Integer vtIndex;
	public Integer vnIndex;

	public ModelFaceIndex(int verticeIndex, int vtIndex, int vnIndex) {
		this.verticeIndex = verticeIndex;
		this.vtIndex = vtIndex;
		this.vnIndex = vnIndex;
	}

	public ModelFaceIndex(int verticeIndex) {
		this.verticeIndex = verticeIndex;
		this.vtIndex = null;
		this.vnIndex = null;
	}

	public ModelFaceIndex() {
		this.verticeIndex = null;
		this.vtIndex = null;
		this.vnIndex = null;
	}

	// verticeIndex/vtIndex/vnIndex
	public static ModelFaceIndex fromString(String faceIndex) throws Error {
		ModelFaceIndex ret = new ModelFaceIndex();
		String[] aim = faceIndex.split("/");
		int length = aim.length;
		if (length > 3 || length < 1) {
			throw new Error("Input String is not valid!");
		}
		ret.verticeIndex = Integer.parseUnsignedInt(aim[0]) - 1;
		if (length > 1) {
			ret.vtIndex = Integer.parseUnsignedInt(aim[1]) - 1;
			if (length > 2) {
				ret.vnIndex = Integer.parseUnsignedInt(aim[2]) - 1;
			}
		}

		return ret;
	}

	public static ModelFaceIndex[] fromFaceIndexString(String faceIndex) throws Error {
		char test = faceIndex.charAt(0);
		if (test != 'f') {
			throw new Error("Input String is not valid!");
		}
		faceIndex = faceIndex.substring(2);
		String[] aim = faceIndex.split(" ");
		if (aim.length != 3) {
			throw new Error("Input String is not valid!");
		}
		ModelFaceIndex[] ret = new ModelFaceIndex[3];
		for (int i = 0; i < aim.length; i++) {
			ret[i] = ModelFaceIndex.fromString(aim[i]);
		}
		return ret;
	}

}