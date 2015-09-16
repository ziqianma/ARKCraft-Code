package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTBlendFuncSeparate;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

@SideOnly(Side.CLIENT)
public class OpenGlHelper
{
    public static boolean nvidia;
    public static int GL_FRAMEBUFFER;
    public static int GL_RENDERBUFFER;
    public static int GL_COLOR_ATTACHMENT0;
    public static int GL_DEPTH_ATTACHMENT;
    public static int GL_FRAMEBUFFER_COMPLETE;
    public static int GL_FB_INCOMPLETE_ATTACHMENT;
    public static int GL_FB_INCOMPLETE_MISS_ATTACH;
    public static int GL_FB_INCOMPLETE_DRAW_BUFFER;
    public static int GL_FB_INCOMPLETE_READ_BUFFER;
    private static int framebufferType;
    public static boolean framebufferSupported;
    private static boolean shadersAvailable;
    private static boolean arbShaders;
    public static int GL_LINK_STATUS;
    public static int GL_COMPILE_STATUS;
    public static int GL_VERTEX_SHADER;
    public static int GL_FRAGMENT_SHADER;
    private static boolean arbMultitexture;
    /**
     * An OpenGL constant corresponding to GL_TEXTURE0, used when setting data pertaining to auxiliary OpenGL texture
     * units.
     */
    public static int defaultTexUnit;
    /**
     * An OpenGL constant corresponding to GL_TEXTURE1, used when setting data pertaining to auxiliary OpenGL texture
     * units.
     */
    public static int lightmapTexUnit;
    public static int GL_TEXTURE2;
    private static boolean arbTextureEnvCombine;
    public static int GL_COMBINE;
    public static int GL_INTERPOLATE;
    public static int GL_PRIMARY_COLOR;
    public static int GL_CONSTANT;
    public static int GL_PREVIOUS;
    public static int GL_COMBINE_RGB;
    public static int GL_SOURCE0_RGB;
    public static int GL_SOURCE1_RGB;
    public static int GL_SOURCE2_RGB;
    public static int GL_OPERAND0_RGB;
    public static int GL_OPERAND1_RGB;
    public static int GL_OPERAND2_RGB;
    public static int GL_COMBINE_ALPHA;
    public static int GL_SOURCE0_ALPHA;
    public static int GL_SOURCE1_ALPHA;
    public static int GL_SOURCE2_ALPHA;
    public static int GL_OPERAND0_ALPHA;
    public static int GL_OPERAND1_ALPHA;
    public static int GL_OPERAND2_ALPHA;
    private static boolean openGL14;
    public static boolean extBlendFuncSeparate;
    public static boolean openGL21;
    public static boolean shadersSupported;
    private static String logText = "";
    public static boolean vboSupported;
    private static boolean arbVbo;
    public static int GL_ARRAY_BUFFER;
    public static int GL_STATIC_DRAW;
    private static final String __OBFID = "CL_00001179";

    /* Stores the last values sent into setLightmapTextureCoords */
    public static float lastBrightnessX = 0.0f;
    public static float lastBrightnessY = 0.0f;

    /**
     * Initializes the texture constants to be used when rendering lightmap values
     */
    public static void initializeTextures()
    {
        ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        arbMultitexture = contextcapabilities.GL_ARB_multitexture && !contextcapabilities.OpenGL13;
        arbTextureEnvCombine = contextcapabilities.GL_ARB_texture_env_combine && !contextcapabilities.OpenGL13;

        if (arbMultitexture)
        {
            logText = logText + "Using ARB_multitexture.\n";
            defaultTexUnit = 33984;
            lightmapTexUnit = 33985;
            GL_TEXTURE2 = 33986;
        }
        else
        {
            logText = logText + "Using GL 1.3 multitexturing.\n";
            defaultTexUnit = 33984;
            lightmapTexUnit = 33985;
            GL_TEXTURE2 = 33986;
        }

        if (arbTextureEnvCombine)
        {
            logText = logText + "Using ARB_texture_env_combine.\n";
            GL_COMBINE = 34160;
            GL_INTERPOLATE = 34165;
            GL_PRIMARY_COLOR = 34167;
            GL_CONSTANT = 34166;
            GL_PREVIOUS = 34168;
            GL_COMBINE_RGB = 34161;
            GL_SOURCE0_RGB = 34176;
            GL_SOURCE1_RGB = 34177;
            GL_SOURCE2_RGB = 34178;
            GL_OPERAND0_RGB = 34192;
            GL_OPERAND1_RGB = 34193;
            GL_OPERAND2_RGB = 34194;
            GL_COMBINE_ALPHA = 34162;
            GL_SOURCE0_ALPHA = 34184;
            GL_SOURCE1_ALPHA = 34185;
            GL_SOURCE2_ALPHA = 34186;
            GL_OPERAND0_ALPHA = 34200;
            GL_OPERAND1_ALPHA = 34201;
            GL_OPERAND2_ALPHA = 34202;
        }
        else
        {
            logText = logText + "Using GL 1.3 texture combiners.\n";
            GL_COMBINE = 34160;
            GL_INTERPOLATE = 34165;
            GL_PRIMARY_COLOR = 34167;
            GL_CONSTANT = 34166;
            GL_PREVIOUS = 34168;
            GL_COMBINE_RGB = 34161;
            GL_SOURCE0_RGB = 34176;
            GL_SOURCE1_RGB = 34177;
            GL_SOURCE2_RGB = 34178;
            GL_OPERAND0_RGB = 34192;
            GL_OPERAND1_RGB = 34193;
            GL_OPERAND2_RGB = 34194;
            GL_COMBINE_ALPHA = 34162;
            GL_SOURCE0_ALPHA = 34184;
            GL_SOURCE1_ALPHA = 34185;
            GL_SOURCE2_ALPHA = 34186;
            GL_OPERAND0_ALPHA = 34200;
            GL_OPERAND1_ALPHA = 34201;
            GL_OPERAND2_ALPHA = 34202;
        }

        extBlendFuncSeparate = contextcapabilities.GL_EXT_blend_func_separate && !contextcapabilities.OpenGL14;
        openGL14 = contextcapabilities.OpenGL14 || contextcapabilities.GL_EXT_blend_func_separate;
        framebufferSupported = openGL14 && (contextcapabilities.GL_ARB_framebuffer_object || contextcapabilities.GL_EXT_framebuffer_object || contextcapabilities.OpenGL30);

        if (framebufferSupported)
        {
            logText = logText + "Using framebuffer objects because ";

            if (contextcapabilities.OpenGL30)
            {
                logText = logText + "OpenGL 3.0 is supported and separate blending is supported.\n";
                framebufferType = 0;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
            else if (contextcapabilities.GL_ARB_framebuffer_object)
            {
                logText = logText + "ARB_framebuffer_object is supported and separate blending is supported.\n";
                framebufferType = 1;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
            else if (contextcapabilities.GL_EXT_framebuffer_object)
            {
                logText = logText + "EXT_framebuffer_object is supported.\n";
                framebufferType = 2;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
        }
        else
        {
            logText = logText + "Not using framebuffer objects because ";
            logText = logText + "OpenGL 1.4 is " + (contextcapabilities.OpenGL14 ? "" : "not ") + "supported, ";
            logText = logText + "EXT_blend_func_separate is " + (contextcapabilities.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
            logText = logText + "OpenGL 3.0 is " + (contextcapabilities.OpenGL30 ? "" : "not ") + "supported, ";
            logText = logText + "ARB_framebuffer_object is " + (contextcapabilities.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
            logText = logText + "EXT_framebuffer_object is " + (contextcapabilities.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
        }

        openGL21 = contextcapabilities.OpenGL21;
        shadersAvailable = openGL21 || contextcapabilities.GL_ARB_vertex_shader && contextcapabilities.GL_ARB_fragment_shader && contextcapabilities.GL_ARB_shader_objects;
        logText = logText + "Shaders are " + (shadersAvailable ? "" : "not ") + "available because ";

        if (shadersAvailable)
        {
            if (contextcapabilities.OpenGL21)
            {
                logText = logText + "OpenGL 2.1 is supported.\n";
                arbShaders = false;
                GL_LINK_STATUS = 35714;
                GL_COMPILE_STATUS = 35713;
                GL_VERTEX_SHADER = 35633;
                GL_FRAGMENT_SHADER = 35632;
            }
            else
            {
                logText = logText + "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
                arbShaders = true;
                GL_LINK_STATUS = 35714;
                GL_COMPILE_STATUS = 35713;
                GL_VERTEX_SHADER = 35633;
                GL_FRAGMENT_SHADER = 35632;
            }
        }
        else
        {
            logText = logText + "OpenGL 2.1 is " + (contextcapabilities.OpenGL21 ? "" : "not ") + "supported, ";
            logText = logText + "ARB_shader_objects is " + (contextcapabilities.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
            logText = logText + "ARB_vertex_shader is " + (contextcapabilities.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
            logText = logText + "ARB_fragment_shader is " + (contextcapabilities.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
        }

        shadersSupported = framebufferSupported && shadersAvailable;
        nvidia = GL11.glGetString(GL11.GL_VENDOR).toLowerCase().contains("nvidia");
        arbVbo = !contextcapabilities.OpenGL15 && contextcapabilities.GL_ARB_vertex_buffer_object;
        vboSupported = contextcapabilities.OpenGL15 || arbVbo;
        logText = logText + "VBOs are " + (vboSupported ? "" : "not ") + "available because ";

        if (vboSupported)
        {
            if (arbVbo)
            {
                logText = logText + "ARB_vertex_buffer_object is supported.\n";
                GL_STATIC_DRAW = 35044;
                GL_ARRAY_BUFFER = 34962;
            }
            else
            {
                logText = logText + "OpenGL 1.5 is supported.\n";
                GL_STATIC_DRAW = 35044;
                GL_ARRAY_BUFFER = 34962;
            }
        }
    }

    public static boolean areShadersSupported()
    {
        return shadersSupported;
    }

    public static String getLogText()
    {
        return logText;
    }

    public static int glGetProgrami(int p_153175_0_, int p_153175_1_)
    {
        return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(p_153175_0_, p_153175_1_) : GL20.glGetProgrami(p_153175_0_, p_153175_1_);
    }

    public static void glAttachShader(int p_153178_0_, int p_153178_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glAttachObjectARB(p_153178_0_, p_153178_1_);
        }
        else
        {
            GL20.glAttachShader(p_153178_0_, p_153178_1_);
        }
    }

    public static void glDeleteShader(int p_153180_0_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glDeleteObjectARB(p_153180_0_);
        }
        else
        {
            GL20.glDeleteShader(p_153180_0_);
        }
    }

    /**
     * creates a shader with the given mode and returns the GL id. params: mode
     */
    public static int glCreateShader(int p_153195_0_)
    {
        return arbShaders ? ARBShaderObjects.glCreateShaderObjectARB(p_153195_0_) : GL20.glCreateShader(p_153195_0_);
    }

    public static void glShaderSource(int p_153169_0_, ByteBuffer p_153169_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glShaderSourceARB(p_153169_0_, p_153169_1_);
        }
        else
        {
            GL20.glShaderSource(p_153169_0_, p_153169_1_);
        }
    }

    public static void glCompileShader(int p_153170_0_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glCompileShaderARB(p_153170_0_);
        }
        else
        {
            GL20.glCompileShader(p_153170_0_);
        }
    }

    public static int glGetShaderi(int p_153157_0_, int p_153157_1_)
    {
        return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(p_153157_0_, p_153157_1_) : GL20.glGetShaderi(p_153157_0_, p_153157_1_);
    }

    public static String glGetShaderInfoLog(int p_153158_0_, int p_153158_1_)
    {
        return arbShaders ? ARBShaderObjects.glGetInfoLogARB(p_153158_0_, p_153158_1_) : GL20.glGetShaderInfoLog(p_153158_0_, p_153158_1_);
    }

    public static String glGetProgramInfoLog(int p_153166_0_, int p_153166_1_)
    {
        return arbShaders ? ARBShaderObjects.glGetInfoLogARB(p_153166_0_, p_153166_1_) : GL20.glGetProgramInfoLog(p_153166_0_, p_153166_1_);
    }

    public static void glUseProgram(int p_153161_0_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUseProgramObjectARB(p_153161_0_);
        }
        else
        {
            GL20.glUseProgram(p_153161_0_);
        }
    }

    public static int glCreateProgram()
    {
        return arbShaders ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
    }

    public static void glDeleteProgram(int p_153187_0_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glDeleteObjectARB(p_153187_0_);
        }
        else
        {
            GL20.glDeleteProgram(p_153187_0_);
        }
    }

    public static void glLinkProgram(int p_153179_0_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glLinkProgramARB(p_153179_0_);
        }
        else
        {
            GL20.glLinkProgram(p_153179_0_);
        }
    }

    public static int glGetUniformLocation(int p_153194_0_, CharSequence p_153194_1_)
    {
        return arbShaders ? ARBShaderObjects.glGetUniformLocationARB(p_153194_0_, p_153194_1_) : GL20.glGetUniformLocation(p_153194_0_, p_153194_1_);
    }

    public static void glUniform1(int p_153181_0_, IntBuffer p_153181_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniform1ARB(p_153181_0_, p_153181_1_);
        }
        else
        {
            GL20.glUniform1(p_153181_0_, p_153181_1_);
        }
    }

    public static void glUniform1i(int p_153163_0_, int p_153163_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniform1iARB(p_153163_0_, p_153163_1_);
        }
        else
        {
            GL20.glUniform1i(p_153163_0_, p_153163_1_);
        }
    }

    public static void glUniform1(int p_153168_0_, FloatBuffer p_153168_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniform1ARB(p_153168_0_, p_153168_1_);
        }
        else
        {
            GL20.glUniform1(p_153168_0_, p_153168_1_);
        }
    }

    public static void glUniform2(int p_153182_0_, IntBuffer p_153182_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniform2ARB(p_153182_0_, p_153182_1_);
        }
        else
        {
            GL20.glUniform2(p_153182_0_, p_153182_1_);
        }
    }

    public static void glUniform2(int p_153177_0_, FloatBuffer p_153177_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniform2ARB(p_153177_0_, p_153177_1_);
        }
        else
        {
            GL20.glUniform2(p_153177_0_, p_153177_1_);
        }
    }

    public static void glUniform3(int p_153192_0_, IntBuffer p_153192_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniform3ARB(p_153192_0_, p_153192_1_);
        }
        else
        {
            GL20.glUniform3(p_153192_0_, p_153192_1_);
        }
    }

    public static void glUniform3(int p_153191_0_, FloatBuffer p_153191_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniform3ARB(p_153191_0_, p_153191_1_);
        }
        else
        {
            GL20.glUniform3(p_153191_0_, p_153191_1_);
        }
    }

    public static void glUniform4(int p_153162_0_, IntBuffer p_153162_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniform4ARB(p_153162_0_, p_153162_1_);
        }
        else
        {
            GL20.glUniform4(p_153162_0_, p_153162_1_);
        }
    }

    public static void glUniform4(int p_153159_0_, FloatBuffer p_153159_1_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniform4ARB(p_153159_0_, p_153159_1_);
        }
        else
        {
            GL20.glUniform4(p_153159_0_, p_153159_1_);
        }
    }

    public static void glUniformMatrix2(int p_153173_0_, boolean p_153173_1_, FloatBuffer p_153173_2_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniformMatrix2ARB(p_153173_0_, p_153173_1_, p_153173_2_);
        }
        else
        {
            GL20.glUniformMatrix2(p_153173_0_, p_153173_1_, p_153173_2_);
        }
    }

    public static void glUniformMatrix3(int p_153189_0_, boolean p_153189_1_, FloatBuffer p_153189_2_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniformMatrix3ARB(p_153189_0_, p_153189_1_, p_153189_2_);
        }
        else
        {
            GL20.glUniformMatrix3(p_153189_0_, p_153189_1_, p_153189_2_);
        }
    }

    public static void glUniformMatrix4(int p_153160_0_, boolean p_153160_1_, FloatBuffer p_153160_2_)
    {
        if (arbShaders)
        {
            ARBShaderObjects.glUniformMatrix4ARB(p_153160_0_, p_153160_1_, p_153160_2_);
        }
        else
        {
            GL20.glUniformMatrix4(p_153160_0_, p_153160_1_, p_153160_2_);
        }
    }

    public static int glGetAttribLocation(int p_153164_0_, CharSequence p_153164_1_)
    {
        return arbShaders ? ARBVertexShader.glGetAttribLocationARB(p_153164_0_, p_153164_1_) : GL20.glGetAttribLocation(p_153164_0_, p_153164_1_);
    }

    public static int glGenBuffers()
    {
        return arbVbo ? ARBVertexBufferObject.glGenBuffersARB() : GL15.glGenBuffers();
    }

    public static void glBindBuffer(int p_176072_0_, int p_176072_1_)
    {
        if (arbVbo)
        {
            ARBVertexBufferObject.glBindBufferARB(p_176072_0_, p_176072_1_);
        }
        else
        {
            GL15.glBindBuffer(p_176072_0_, p_176072_1_);
        }
    }

    public static void glBufferData(int p_176071_0_, ByteBuffer p_176071_1_, int p_176071_2_)
    {
        if (arbVbo)
        {
            ARBVertexBufferObject.glBufferDataARB(p_176071_0_, p_176071_1_, p_176071_2_);
        }
        else
        {
            GL15.glBufferData(p_176071_0_, p_176071_1_, p_176071_2_);
        }
    }

    public static void glDeleteBuffers(int p_176074_0_)
    {
        if (arbVbo)
        {
            ARBVertexBufferObject.glDeleteBuffersARB(p_176074_0_);
        }
        else
        {
            GL15.glDeleteBuffers(p_176074_0_);
        }
    }

    public static boolean useVbo()
    {
        return vboSupported && Minecraft.getMinecraft().gameSettings.useVbo;
    }

    public static void glBindFramebuffer(int p_153171_0_, int p_153171_1_)
    {
        if (framebufferSupported)
        {
            switch (framebufferType)
            {
                case 0:
                    GL30.glBindFramebuffer(p_153171_0_, p_153171_1_);
                    break;
                case 1:
                    ARBFramebufferObject.glBindFramebuffer(p_153171_0_, p_153171_1_);
                    break;
                case 2:
                    EXTFramebufferObject.glBindFramebufferEXT(p_153171_0_, p_153171_1_);
            }
        }
    }

    public static void glBindRenderbuffer(int p_153176_0_, int p_153176_1_)
    {
        if (framebufferSupported)
        {
            switch (framebufferType)
            {
                case 0:
                    GL30.glBindRenderbuffer(p_153176_0_, p_153176_1_);
                    break;
                case 1:
                    ARBFramebufferObject.glBindRenderbuffer(p_153176_0_, p_153176_1_);
                    break;
                case 2:
                    EXTFramebufferObject.glBindRenderbufferEXT(p_153176_0_, p_153176_1_);
            }
        }
    }

    public static void glDeleteRenderbuffers(int p_153184_0_)
    {
        if (framebufferSupported)
        {
            switch (framebufferType)
            {
                case 0:
                    GL30.glDeleteRenderbuffers(p_153184_0_);
                    break;
                case 1:
                    ARBFramebufferObject.glDeleteRenderbuffers(p_153184_0_);
                    break;
                case 2:
                    EXTFramebufferObject.glDeleteRenderbuffersEXT(p_153184_0_);
            }
        }
    }

    public static void glDeleteFramebuffers(int p_153174_0_)
    {
        if (framebufferSupported)
        {
            switch (framebufferType)
            {
                case 0:
                    GL30.glDeleteFramebuffers(p_153174_0_);
                    break;
                case 1:
                    ARBFramebufferObject.glDeleteFramebuffers(p_153174_0_);
                    break;
                case 2:
                    EXTFramebufferObject.glDeleteFramebuffersEXT(p_153174_0_);
            }
        }
    }

    /**
     * Calls the appropriate glGenFramebuffers method and returns the newly created fbo, or returns -1 if not supported.
     */
    public static int glGenFramebuffers()
    {
        if (!framebufferSupported)
        {
            return -1;
        }
        else
        {
            switch (framebufferType)
            {
                case 0:
                    return GL30.glGenFramebuffers();
                case 1:
                    return ARBFramebufferObject.glGenFramebuffers();
                case 2:
                    return EXTFramebufferObject.glGenFramebuffersEXT();
                default:
                    return -1;
            }
        }
    }

    public static int glGenRenderbuffers()
    {
        if (!framebufferSupported)
        {
            return -1;
        }
        else
        {
            switch (framebufferType)
            {
                case 0:
                    return GL30.glGenRenderbuffers();
                case 1:
                    return ARBFramebufferObject.glGenRenderbuffers();
                case 2:
                    return EXTFramebufferObject.glGenRenderbuffersEXT();
                default:
                    return -1;
            }
        }
    }

    public static void glRenderbufferStorage(int p_153186_0_, int p_153186_1_, int p_153186_2_, int p_153186_3_)
    {
        if (framebufferSupported)
        {
            switch (framebufferType)
            {
                case 0:
                    GL30.glRenderbufferStorage(p_153186_0_, p_153186_1_, p_153186_2_, p_153186_3_);
                    break;
                case 1:
                    ARBFramebufferObject.glRenderbufferStorage(p_153186_0_, p_153186_1_, p_153186_2_, p_153186_3_);
                    break;
                case 2:
                    EXTFramebufferObject.glRenderbufferStorageEXT(p_153186_0_, p_153186_1_, p_153186_2_, p_153186_3_);
            }
        }
    }

    public static void glFramebufferRenderbuffer(int p_153190_0_, int p_153190_1_, int p_153190_2_, int p_153190_3_)
    {
        if (framebufferSupported)
        {
            switch (framebufferType)
            {
                case 0:
                    GL30.glFramebufferRenderbuffer(p_153190_0_, p_153190_1_, p_153190_2_, p_153190_3_);
                    break;
                case 1:
                    ARBFramebufferObject.glFramebufferRenderbuffer(p_153190_0_, p_153190_1_, p_153190_2_, p_153190_3_);
                    break;
                case 2:
                    EXTFramebufferObject.glFramebufferRenderbufferEXT(p_153190_0_, p_153190_1_, p_153190_2_, p_153190_3_);
            }
        }
    }

    public static int glCheckFramebufferStatus(int p_153167_0_)
    {
        if (!framebufferSupported)
        {
            return -1;
        }
        else
        {
            switch (framebufferType)
            {
                case 0:
                    return GL30.glCheckFramebufferStatus(p_153167_0_);
                case 1:
                    return ARBFramebufferObject.glCheckFramebufferStatus(p_153167_0_);
                case 2:
                    return EXTFramebufferObject.glCheckFramebufferStatusEXT(p_153167_0_);
                default:
                    return -1;
            }
        }
    }

    public static void glFramebufferTexture2D(int p_153188_0_, int p_153188_1_, int p_153188_2_, int p_153188_3_, int p_153188_4_)
    {
        if (framebufferSupported)
        {
            switch (framebufferType)
            {
                case 0:
                    GL30.glFramebufferTexture2D(p_153188_0_, p_153188_1_, p_153188_2_, p_153188_3_, p_153188_4_);
                    break;
                case 1:
                    ARBFramebufferObject.glFramebufferTexture2D(p_153188_0_, p_153188_1_, p_153188_2_, p_153188_3_, p_153188_4_);
                    break;
                case 2:
                    EXTFramebufferObject.glFramebufferTexture2DEXT(p_153188_0_, p_153188_1_, p_153188_2_, p_153188_3_, p_153188_4_);
            }
        }
    }

    /**
     * Sets the current lightmap texture to the specified OpenGL constant
     */
    public static void setActiveTexture(int p_77473_0_)
    {
        if (arbMultitexture)
        {
            ARBMultitexture.glActiveTextureARB(p_77473_0_);
        }
        else
        {
            GL13.glActiveTexture(p_77473_0_);
        }
    }

    /**
     * Sets the current lightmap texture to the specified OpenGL constant
     */
    public static void setClientActiveTexture(int p_77472_0_)
    {
        if (arbMultitexture)
        {
            ARBMultitexture.glClientActiveTextureARB(p_77472_0_);
        }
        else
        {
            GL13.glClientActiveTexture(p_77472_0_);
        }
    }

    /**
     * Sets the current coordinates of the given lightmap texture
     */
    public static void setLightmapTextureCoords(int p_77475_0_, float p_77475_1_, float p_77475_2_)
    {
        if (arbMultitexture)
        {
            ARBMultitexture.glMultiTexCoord2fARB(p_77475_0_, p_77475_1_, p_77475_2_);
        }
        else
        {
            GL13.glMultiTexCoord2f(p_77475_0_, p_77475_1_, p_77475_2_);
        }

        if (p_77475_0_ == lightmapTexUnit)
        {
            lastBrightnessX = p_77475_1_;
            lastBrightnessY = p_77475_2_;
        }
    }

    public static void glBlendFunc(int p_148821_0_, int p_148821_1_, int p_148821_2_, int p_148821_3_)
    {
        if (openGL14)
        {
            if (extBlendFuncSeparate)
            {
                EXTBlendFuncSeparate.glBlendFuncSeparateEXT(p_148821_0_, p_148821_1_, p_148821_2_, p_148821_3_);
            }
            else
            {
                GL14.glBlendFuncSeparate(p_148821_0_, p_148821_1_, p_148821_2_, p_148821_3_);
            }
        }
        else
        {
            GL11.glBlendFunc(p_148821_0_, p_148821_1_);
        }
    }

    public static boolean isFramebufferEnabled()
    {
        return framebufferSupported && Minecraft.getMinecraft().gameSettings.fboEnable;
    }
}