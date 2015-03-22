package nkarasch.chessgdx.logiccontroller.frontend;

import nkarasch.chessgdx.logiccontroller.frontend.VisibleBoardState.GridLocation;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

class ObjectPicker {

	private Array<GridLocation> mIntersectedBoxList = new Array<GridLocation>();;

	/**
	 * Sorts the grid spaces in ascending order based on their distance from the
	 * camera and returns the closest one that intersects with the mouse click
	 * ray.
	 * 
	 * @param cameraPosition
	 * @param ray
	 *            a ray from the camera position to the destination of the mouse
	 *            click
	 * @param pieceGridPairs
	 *            list of grid locations
	 * @return the clicked grid location
	 */
	public GridLocation getPickedGridSpace(Vector3 cameraPosition, Ray ray, Array<GridLocation> pieceGridPairs) {

		// sort list of grid/piece pairs in ascending order based on their
		// distance from the camera
		GridLocation temp;
		for (int i = 0; i < pieceGridPairs.size - 1; i++) {
			for (int j = 1; j < pieceGridPairs.size - i; j++) {
				if (pieceGridPairs.get(j - 1).gridSpace.getDistanceFrom(cameraPosition) > pieceGridPairs.get(j).gridSpace
						.getDistanceFrom(cameraPosition)) {
					temp = pieceGridPairs.get(j - 1);
					pieceGridPairs.set(j - 1, pieceGridPairs.get(j));
					pieceGridPairs.set(j, temp);
				}
			}
		}

		// check for intersections between the click ray and grid location
		// bounding box, because the list is sorted the first one detected is
		// the one the user wants. return it.
		GridLocation clickedPair = null;
		for (GridLocation gridSpace : pieceGridPairs) {
			if (Intersector.intersectRayBoundsFast(ray, gridSpace.gridSpace.getBoundingBox())) {
				clickedPair = gridSpace;
				if (clickedPair != null) {
					mIntersectedBoxList.clear();
					return clickedPair;
				}
			}
		}

		return null;
	}

	/**
	 * Finds the chess piece that is being clicked and returns its grid
	 * location.
	 * 
	 * 1) Fills a list with pieces whose bounding box intersects with the pick
	 * ray. 2) Sorts the list based on distance from camera in ascending order.
	 * 3) Tests for intersections between the pick ray and low poly models of
	 * the chess pieces in the previously sorted list. Returns the first hit,
	 * which will be the nearest.
	 * 
	 * @param cameraPosition
	 * @param ray
	 *            a ray from the camera position to the destination of the mouse
	 *            click.
	 * @param pieceGridPairs
	 *            list of grid locations
	 * @param playerWhite
	 *            is the user playing as white
	 * @return grid location referencing the mouse clicked chess piece
	 */
	GridLocation getPickedChessPiece(Vector3 cameraPosition, Ray ray, Array<GridLocation> pieceGridPairs, boolean playerWhite) {

		fillIntersectedBoxList(pieceGridPairs, ray);
		sortByDistance(cameraPosition);
		return getPieceFromList(ray, playerWhite);
	}

	/**
	 * Checks for a intersections between chess piece bounding boxes and the
	 * pick ray. Stores the ones that intersect in a list.
	 * 
	 * @param pieceGridPairs
	 *            list of grid locations
	 * @param ray
	 *            a ray from the camera position to the destination of the mouse
	 *            click
	 */
	private void fillIntersectedBoxList(Array<GridLocation> pieceGridPairs, Ray ray) {

		for (GridLocation pair : pieceGridPairs) {
			if (pair.chessPiece != null) {
				if (Intersector.intersectRayBoundsFast(ray, pair.chessPiece.getBoundingBox())) {
					mIntersectedBoxList.add(pair);
				}
			}
		}
	}

	/**
	 * Bubble sort the list of locations whose bounding boxes the mouse pick ray
	 * intersected with in ascending order based on their distance from the
	 * camera position. Bubble sort is fine here because the lists are very,
	 * very small.
	 * 
	 * @param cameraPosition
	 * @return list sorted by distance
	 */
	private Array<GridLocation> sortByDistance(Vector3 cameraPosition) {

		GridLocation temp;
		for (int i = 0; i < mIntersectedBoxList.size - 1; i++) {
			for (int j = 1; j < mIntersectedBoxList.size - i; j++) {
				if (mIntersectedBoxList.get(j - 1).chessPiece.getDistanceFrom(cameraPosition) > mIntersectedBoxList.get(j).chessPiece
						.getDistanceFrom(cameraPosition)) {
					temp = mIntersectedBoxList.get(j - 1);
					mIntersectedBoxList.set(j - 1, mIntersectedBoxList.get(j));
					mIntersectedBoxList.set(j, temp);
				}
			}
		}

		return mIntersectedBoxList;
	}

	/**
	 * Finds the closest clicked chess piece from the list of pieces that had an
	 * intersecting bounding box.
	 * 
	 * @param ray
	 *            a ray from the camera position to the destination of the mouse
	 *            click
	 * @param playerWhite
	 *            is the user playing as white
	 * @return grid location instance of clicked chess piece
	 */
	private GridLocation getPieceFromList(Ray ray, boolean playerWhite) {

		GridLocation clickedPiece = null;

		for (GridLocation location : mIntersectedBoxList) {

			ModelInstance modelInstance = location.chessPiece.getCollisionModelInstance();
			Mesh mesh = modelInstance.model.meshes.get(0);

			modelInstance.transform.rotate(1, 0, 0, -90);
			modelInstance.transform.rotate(0, 0, 1, 180);
			Matrix4 inverse = new Matrix4(modelInstance.transform).inv();

			mesh.transform(modelInstance.transform);

			float[] vertices = new float[mesh.getNumVertices() * mesh.getVertexSize() / 4];
			short[] indices = new short[mesh.getNumIndices()];

			mesh.getVertices(vertices);
			mesh.getIndices(indices);

			if (Intersector.intersectRayTriangles(ray, vertices, indices, mesh.getVertexSize() / 4, null)) {
				clickedPiece = location;
			}

			mesh.transform(inverse);
			modelInstance.transform.rotate(0, 0, 1, -180);
			modelInstance.transform.rotate(1, 0, 0, 90);

			if (clickedPiece != null) {
				mIntersectedBoxList.clear();
				if (clickedPiece.chessPiece.getDescription() < 0 && playerWhite || clickedPiece.chessPiece.getDescription() > 0
						&& !playerWhite) {

					return clickedPiece;
				}
			}
		}

		return null;
	}
}
