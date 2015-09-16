package net.minecraft.client.model;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.Vec3;

public class TexturedQuad
{
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;
    private static final String __OBFID = "CL_00000850";

    public TexturedQuad(PositionTextureVertex[] vertices)
    {
        this.vertexPositions = vertices;
        this.nVertices = vertices.length;
    }

    public TexturedQuad(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight)
    {
        this(vertices);
        float f2 = 0.0F / textureWidth;
        float f3 = 0.0F / textureHeight;
        vertices[0] = vertices[0].setTexturePosition((float)texcoordU2 / textureWidth - f2, (float)texcoordV1 / textureHeight + f3);
        vertices[1] = vertices[1].setTexturePosition((float)texcoordU1 / textureWidth + f2, (float)texcoordV1 / textureHeight + f3);
        vertices[2] = vertices[2].setTexturePosition((float)texcoordU1 / textureWidth + f2, (float)texcoordV2 / textureHeight - f3);
        vertices[3] = vertices[3].setTexturePosition((float)texcoordU2 / textureWidth - f2, (float)texcoordV2 / textureHeight - f3);
    }

    public void flipFace()
    {
        PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];

        for (int i = 0; i < this.vertexPositions.length; ++i)
        {
            apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
        }

        this.vertexPositions = apositiontexturevertex;
    }

    /**
     * Draw this primitve. This is typically called only once as the generated drawing instructions are saved by the
     * renderer and reused later.
     *  
     * @param renderer The renderer instance
     * @param scale The amount of scale to apply to this object
     */
    public void draw(WorldRenderer renderer, float scale)
    {
        Vec3 vec3 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
        Vec3 vec31 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
        Vec3 vec32 = vec31.crossProduct(vec3).normalize();
        renderer.startDrawingQuads();

        if (this.invertNormal)
        {
            renderer.setNormal(-((float)vec32.xCoord), -((float)vec32.yCoord), -((float)vec32.zCoord));
        }
        else
        {
            renderer.setNormal((float)vec32.xCoord, (float)vec32.yCoord, (float)vec32.zCoord);
        }

        for (int i = 0; i < 4; ++i)
        {
            PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
            renderer.addVertexWithUV(positiontexturevertex.vector3D.xCoord * (double)scale, positiontexturevertex.vector3D.yCoord * (double)scale, positiontexturevertex.vector3D.zCoord * (double)scale, (double)positiontexturevertex.texturePositionX, (double)positiontexturevertex.texturePositionY);
        }

        Tessellator.getInstance().draw();
    }
}