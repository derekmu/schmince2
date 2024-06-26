package texample;

import android.opengl.GLES20;
import android.opengl.Matrix;
import dopengl.programs.BatchTextProgram;

class SpriteBatch {
	final static int VERTEX_SIZE = 5; // Vertex Size (in Components) ie. (X,Y,U,V,M), M is MVP matrix index
	final static int VERTICES_PER_SPRITE = 4; // Vertices Per Sprite
	final static int INDICES_PER_SPRITE = 6; // Indices Per Sprite

	private BatchTextProgram program;
	private Vertices vertices; // Vertices Instance Used for Rendering
	private float[] vertexBuffer; // Vertex Buffer
	private int bufferIndex; // Vertex Buffer Start Index
	private int maxSprites; // Maximum Sprites Allowed in Buffer
	private int numSprites; // Number of Sprites Currently in Buffer
	private float[] mVPMatrix; // View and projection matrix specified at begin
	private float[] uMVPMatrices = new float[GLText.CHAR_BATCH_SIZE * 16]; // MVP matrix array to pass to shader
	private float[] mMVPMatrix = new float[16]; // used to calculate MVP matrix of each sprite

	/**
	 * Prepare the sprite batcher for specified maximum number of sprites
	 *
	 * @param maxSprites the maximum allowed sprites per batch
	 * @param program    program to use when drawing
	 */
	public SpriteBatch(int maxSprites, BatchTextProgram program) {
		this.program = program;

		this.vertexBuffer = new float[maxSprites * VERTICES_PER_SPRITE * VERTEX_SIZE]; // Create Vertex Buffer
		this.vertices = new Vertices(maxSprites * VERTICES_PER_SPRITE, maxSprites
				* INDICES_PER_SPRITE, program); // Create Rendering Vertices
		this.bufferIndex = 0; // Reset Buffer Index
		this.maxSprites = maxSprites; // Save Maximum Sprites
		this.numSprites = 0; // Clear Sprite Counter

		short[] indices = new short[maxSprites * INDICES_PER_SPRITE]; // Create Temp Index Buffer
		int len = indices.length; // Get Index Buffer Length
		short j = 0; // Counter
		for (int i = 0; i < len; i += INDICES_PER_SPRITE, j += VERTICES_PER_SPRITE) { // FOR Each Index Set (Per Sprite)
			indices[i + 0] = (short) (j + 0); // Calculate Index 0
			indices[i + 1] = (short) (j + 1); // Calculate Index 1
			indices[i + 2] = (short) (j + 2); // Calculate Index 2
			indices[i + 3] = (short) (j + 2); // Calculate Index 3
			indices[i + 4] = (short) (j + 3); // Calculate Index 4
			indices[i + 5] = (short) (j + 0); // Calculate Index 5
		}
		vertices.setIndices(indices, 0, len); // Set Index Buffer for Rendering
	}

	public void beginBatch(float[] vpMatrix) {
		numSprites = 0; // Empty Sprite Counter
		bufferIndex = 0; // Reset Buffer Index (Empty)
		mVPMatrix = vpMatrix;
	}

	/**
	 * Signal the end of a batch. render the batched sprites
	 */
	public void endBatch() {
		if (numSprites > 0) { // IF Any Sprites to Render
			program.useMVPMatrices(numSprites, uMVPMatrices);

			vertices.setVertices(vertexBuffer, 0, bufferIndex); // Set Vertices from Buffer
			vertices.bind(); // Bind Vertices
			vertices.draw(GLES20.GL_TRIANGLES, 0, numSprites * INDICES_PER_SPRITE); // Render Batched Sprites
		}
	}

	/**
	 * Add specified sprite to batch. Adds vertices for sprite to vertex buffer.
	 * NOTE: MUST be called after beginBatch(), and before endBatch()!
	 * NOTE: If the batch overflows, this will render the current batch, restart it,  and then batch this sprite.
	 *
	 * @param x           the position of the sprite (center)
	 * @param y           the position of the sprite (center)
	 * @param width       the width of the sprite
	 * @param height      the height of the sprite
	 * @param region      the texture region to use for sprite
	 * @param modelMatrix the model matrix to assign to the sprite
	 */
	public void drawSprite(float x, float y, float width, float height, TextureRegion region,
						   float[] modelMatrix) {
		if (numSprites == maxSprites) { // IF Sprite Buffer is Full
			endBatch(); // End Batch
			// NOTE: leave current texture bound!!
			numSprites = 0; // Empty Sprite Counter
			bufferIndex = 0; // Reset Buffer Index (Empty)
		}

		float halfWidth = width / 2.0f; // Calculate Half Width
		float halfHeight = height / 2.0f; // Calculate Half Height
		float x1 = x - halfWidth; // Calculate Left X
		float y1 = y - halfHeight; // Calculate Bottom Y
		float x2 = x + halfWidth; // Calculate Right X
		float y2 = y + halfHeight; // Calculate Top Y

		vertexBuffer[bufferIndex++] = x1; // Add X for Vertex 0
		vertexBuffer[bufferIndex++] = y1; // Add Y for Vertex 0
		vertexBuffer[bufferIndex++] = region.u1; // Add U for Vertex 0
		vertexBuffer[bufferIndex++] = region.v2; // Add V for Vertex 0
		vertexBuffer[bufferIndex++] = numSprites;

		vertexBuffer[bufferIndex++] = x2; // Add X for Vertex 1
		vertexBuffer[bufferIndex++] = y1; // Add Y for Vertex 1
		vertexBuffer[bufferIndex++] = region.u2; // Add U for Vertex 1
		vertexBuffer[bufferIndex++] = region.v2; // Add V for Vertex 1
		vertexBuffer[bufferIndex++] = numSprites;

		vertexBuffer[bufferIndex++] = x2; // Add X for Vertex 2
		vertexBuffer[bufferIndex++] = y2; // Add Y for Vertex 2
		vertexBuffer[bufferIndex++] = region.u2; // Add U for Vertex 2
		vertexBuffer[bufferIndex++] = region.v1; // Add V for Vertex 2
		vertexBuffer[bufferIndex++] = numSprites;

		vertexBuffer[bufferIndex++] = x1; // Add X for Vertex 3
		vertexBuffer[bufferIndex++] = y2; // Add Y for Vertex 3
		vertexBuffer[bufferIndex++] = region.u1; // Add U for Vertex 3
		vertexBuffer[bufferIndex++] = region.v1; // Add V for Vertex 3
		vertexBuffer[bufferIndex++] = numSprites;

		// add the sprite mvp matrix to uMVPMatrices array
		Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, modelMatrix, 0);
		for (int i = 0; i < 16; ++i)
			uMVPMatrices[numSprites * 16 + i] = mMVPMatrix[i];

		numSprites++; // Increment Sprite Count
	}
}
