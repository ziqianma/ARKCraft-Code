package com.arkcraft.mod.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * Apatosaurus - Mature.tcn - TechneToTabulaImporter
 * Created using Tabula 5.1.0
 */
public class ModelBrontosaurus extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer toplegleft;
    public ModelRenderer toplegright;
    public ModelRenderer neck1;
    public ModelRenderer Stomach;
    public ModelRenderer hips;
    public ModelRenderer frontlefttopleg;
    public ModelRenderer frontrighttopleg;
    public ModelRenderer neck2;
    public ModelRenderer neck3;
    public ModelRenderer neck4;
    public ModelRenderer neck5;
    public ModelRenderer neck6;
    public ModelRenderer head;
    public ModelRenderer snout;
    public ModelRenderer jaw;
    public ModelRenderer tail1;
    public ModelRenderer tail2;
    public ModelRenderer tail3;
    public ModelRenderer tail4;
    public ModelRenderer tail5;
    public ModelRenderer tail6;
    public ModelRenderer tail7;
    public ModelRenderer bottomfrontleftleg;
    public ModelRenderer frontleftfoot;
    public ModelRenderer bottomfrontrightleg;
    public ModelRenderer frontrightfoot;
    public ModelRenderer bottomlegleft;
    public ModelRenderer leftbackfoot;
    public ModelRenderer bottomlegright;
    public ModelRenderer rightbackfoot;

    public ModelBrontosaurus() {
    	
        this.textureWidth = 512;
        this.textureHeight = 512;
        this.tail7 = new ModelRenderer(this, 94, 325);
        this.tail7.setRotationPoint(-2.0F, 0.5F, 32.0F);
        this.tail7.addBox(0.0F, 0.0F, 0.0F, 4, 4, 50, 0.0F);
        this.setRotateAngle(tail7, 0.136659280431156F, 0.0F, 0.0F);
        this.tail1 = new ModelRenderer(this, 94, 95);
        this.tail1.setRotationPoint(0.0F, -0.4F, 10.61F);
        this.tail1.addBox(-8.0F, 0.0F, 0.0F, 16, 17, 13, 0.0F);
        this.setRotateAngle(tail1, 0.091106186954104F, 0.0F, 0.0F);
        this.tail5 = new ModelRenderer(this, 94, 243);
        this.tail5.setRotationPoint(0.0F, 1.1156839565008987F, 15.387178081415918F);
        this.tail5.addBox(-3.0F, 0.0F, 0.0F, 6, 7, 20, 0.0F);
        this.setRotateAngle(tail5, 0.028972465583105962F, 0.0F, 0.0F);
        this.neck6 = new ModelRenderer(this, 39, 86);
        this.neck6.setRotationPoint(0.0F, -7.4F, -9.1F);
        this.neck6.addBox(-3.0F, -0.3F, -21.0F, 6, 7, 7, 0.0F);
        this.setRotateAngle(neck6, 0.5918411493512771F, -0.0F, 0.0F);
        this.tail4 = new ModelRenderer(this, 94, 213);
        this.tail4.setRotationPoint(0.0F, 1.023252759565029F, 12.767261013625529F);
        this.tail4.addBox(-3.5F, 0.0F, 0.0F, 7, 10, 17, 0.0F);
        this.setRotateAngle(tail4, 0.06422811647339133F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 94, 0);
        this.body.setRotationPoint(0.0F, -18.0F, 0.0F);
        this.body.addBox(-9.0F, -3.9F, -23.5F, 18, 23, 28, 0.0F);
        this.setRotateAngle(body, -0.091106186954104F, -0.0F, 0.0F);
        this.neck4 = new ModelRenderer(this, 0, 86);
        this.neck4.setRotationPoint(0.0F, 1.5F, -11.9F);
        this.neck4.addBox(-3.0F, 0.0F, -21.0F, 6, 8, 14, 0.0F);
        this.setRotateAngle(neck4, -0.091106186954104F, -0.0F, 0.0F);
        this.neck1 = new ModelRenderer(this, 0, 183);
        this.neck1.setRotationPoint(0.0F, -4.9F, -19.4F);
        this.neck1.addBox(-7.0F, 0.0F, -14.0F, 14, 14, 17, 0.0F);
        this.setRotateAngle(neck1, -0.27314402793711257F, -0.0F, 0.0F);
        this.leftbackfoot = new ModelRenderer(this, 230, 70);
        this.leftbackfoot.setRotationPoint(0.0F, 18.91909593769347F, 6.990551401738673F);
        this.leftbackfoot.addBox(-3.0F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
        this.setRotateAngle(leftbackfoot, -0.1762782544514274F, 0.0F, 0.0F);
        this.frontrightfoot = new ModelRenderer(this, 200, 50);
        this.frontrightfoot.mirror = true;
        this.frontrightfoot.setRotationPoint(0.0F, 17.65F, -3.0F);
        this.frontrightfoot.addBox(-2.5F, 0.0F, 0.0F, 6, 7, 6, 0.0F);
        this.setRotateAngle(frontrightfoot, 0.136659280431156F, 0.0F, 0.0F);
        this.hips = new ModelRenderer(this, 94, 51);
        this.hips.setRotationPoint(0.0F, -0.5F, 11.8F);
        this.hips.addBox(-11.0F, -1.9F, -8.0F, 22, 24, 20, 1.0F);
        this.setRotateAngle(hips, -0.24312436480281008F, 0.0F, 0.0F);
        this.jaw = new ModelRenderer(this, 0, 74);
        this.jaw.setRotationPoint(0.5F, 2.7F, -1.8F);
        this.jaw.addBox(-2.0F, -1.6F, -6.8F, 4, 2, 7, 1.0F);
        this.setRotateAngle(jaw, -0.0307177948351002F, -0.0F, 0.0F);
        this.neck2 = new ModelRenderer(this, 0, 146);
        this.neck2.setRotationPoint(0.0F, 2.3F, -6.7F);
        this.neck2.addBox(-6.0F, 0.0F, -21.0F, 12, 12, 17, 0.0F);
        this.setRotateAngle(neck2, -0.31869712141416456F, -0.0F, 0.0F);
        this.snout = new ModelRenderer(this, 0, 61);
        this.snout.setRotationPoint(0.0F, 1.5F, -3.9F);
        this.snout.addBox(-2.0F, -3.9F, -5.0F, 5, 4, 6, 1.0F);
        this.setRotateAngle(snout, 0.11135200627723822F, -0.0F, 0.0F);
        this.frontrighttopleg = new ModelRenderer(this, 200, 0);
        this.frontrighttopleg.mirror = true;
        this.frontrighttopleg.setRotationPoint(-10.9F, 7.52F, -17.01F);
        this.frontrighttopleg.addBox(-1.0F, 0.0F, -5.0F, 7, 16, 7, 0.0F);
        this.setRotateAngle(frontrighttopleg, 0.27314402793711257F, 0.0F, 0.0F);
        this.tail2 = new ModelRenderer(this, 94, 132);
        this.tail2.setRotationPoint(0.0F, 0.9284366545563394F, 11.971549831933881F);
        this.tail2.addBox(-6.0F, 0.0F, 0.0F, 12, 15, 12, 0.0F);
        this.setRotateAngle(tail2, -0.0764454212373516F, 0.0F, 0.0F);
        this.toplegleft = new ModelRenderer(this, 230, 0);
        this.toplegleft.setRotationPoint(10.5F, -18.9F, 11.0F);
        this.toplegleft.addBox(-4.0F, 0.0F, 0.0F, 9, 21, 10, 0.0F);
        this.setRotateAngle(toplegleft, -0.22671826983406343F, -0.0F, 0.0F);
        this.bottomfrontleftleg = new ModelRenderer(this, 200, 30);
        this.bottomfrontleftleg.setRotationPoint(-1.0F, 12.98F, -0.91F);
        this.bottomfrontleftleg.addBox(-3.5F, 0.0F, -3.0F, 6, 18, 6, 0.0F);
        this.setRotateAngle(bottomfrontleftleg, -0.3181735226385663F, 0.0F, 0.0F);
        this.tail6 = new ModelRenderer(this, 94, 276);
        this.tail6.setRotationPoint(0.0F, 0.7F, 18.76F);
        this.tail6.addBox(-2.5F, 0.0F, 0.0F, 5, 5, 34, 0.0F);
        this.setRotateAngle(tail6, 0.08377580409572781F, 0.0F, 0.0F);
        this.rightbackfoot = new ModelRenderer(this, 230, 70);
        this.rightbackfoot.mirror = true;
        this.rightbackfoot.setRotationPoint(0.0F, 18.92F, 6.99F);
        this.rightbackfoot.addBox(-3.0F, 0.0F, -7.0F, 7, 8, 7, 0.0F);
        this.setRotateAngle(rightbackfoot, -0.17627825445142728F, 0.0F, 0.0F);
        this.Stomach = new ModelRenderer(this, 161, 79);
        this.Stomach.setRotationPoint(-1.0F, 1.3F, -4.1F);
        this.Stomach.addBox(-11.0F, -3.0F, -8.0F, 24, 23, 20, 1.0F);
        this.setRotateAngle(Stomach, -0.14713125594312196F, -0.0F, 0.0F);
        this.bottomlegleft = new ModelRenderer(this, 230, 40);
        this.bottomlegleft.setRotationPoint(0.0F, 17.951286912969845F, 0.036030101825357264F);
        this.bottomlegleft.addBox(-3.0F, 0.0F, 0.0F, 7, 19, 7, 0.0F);
        this.setRotateAngle(bottomlegleft, 0.40299652428549076F, 0.0F, 0.0F);
        this.neck3 = new ModelRenderer(this, 0, 115);
        this.neck3.setRotationPoint(0.0F, 1.8F, -13.1F);
        this.neck3.addBox(-4.0F, 0.0F, -21.0F, 8, 10, 16, 0.0F);
        this.setRotateAngle(neck3, -0.136659280431156F, -0.0F, 0.0F);
        this.bottomfrontrightleg = new ModelRenderer(this, 200, 30);
        this.bottomfrontrightleg.mirror = true;
        this.bottomfrontrightleg.setRotationPoint(1.9F, 11.98F, -0.91F);
        this.bottomfrontrightleg.addBox(-2.5F, 0.0F, -3.0F, 6, 18, 6, 0.0F);
        this.setRotateAngle(bottomfrontrightleg, -0.3181735226385663F, 0.0F, 0.0F);
        this.bottomlegright = new ModelRenderer(this, 230, 40);
        this.bottomlegright.mirror = true;
        this.bottomlegright.setRotationPoint(0.0F, 17.951286912969845F, 0.036030101825357264F);
        this.bottomlegright.addBox(-3.0F, 0.0F, 0.0F, 7, 19, 7, 0.0F);
        this.setRotateAngle(bottomlegright, 0.40299652428549076F, 0.0F, 0.0F);
        this.frontlefttopleg = new ModelRenderer(this, 200, 0);
        this.frontlefttopleg.setRotationPoint(10.0F, 7.52F, -17.01F);
        this.frontlefttopleg.addBox(-5.0F, 0.0F, -5.0F, 7, 16, 7, 0.0F);
        this.setRotateAngle(frontlefttopleg, 0.27314402793711257F, 0.0F, 0.0F);
        this.frontleftfoot = new ModelRenderer(this, 200, 50);
        this.frontleftfoot.setRotationPoint(0.0F, 17.65F, -3.0F);
        this.frontleftfoot.addBox(-3.5F, 0.0F, 0.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(frontleftfoot, 0.136659280431156F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 43);
        this.head.setRotationPoint(-0.5F, 4.1F, -21.9F);
        this.head.addBox(-3.0F, -2.8F, -4.0F, 7, 6, 8, 0.0F);
        this.setRotateAngle(head, 0.6373942428283291F, -0.0F, 0.0F);
        this.tail3 = new ModelRenderer(this, 94, 167);
        this.tail3.setRotationPoint(0.0F, 0.69F, 10.83F);
        this.tail3.addBox(-4.0F, 0.0F, 0.0F, 8, 13, 15, 0.0F);
        this.setRotateAngle(tail3, 0.136659280431156F, 0.0F, 0.0F);
        this.toplegright = new ModelRenderer(this, 230, 0);
        this.toplegright.mirror = true;
        this.toplegright.setRotationPoint(-11.3F, -18.9F, 11.0F);
        this.toplegright.addBox(-4.0F, 0.0F, 0.0F, 9, 21, 10, 0.0F);
        this.setRotateAngle(toplegright, -0.22671826983406343F, -0.0F, 0.0F);
        this.neck5 = new ModelRenderer(this, 48, 104);
        this.neck5.setRotationPoint(0.0F, -2.1F, -9.4F);
        this.neck5.addBox(-3.0F, 0.0F, -21.0F, 6, 8, 10, 0.0F);
        this.setRotateAngle(neck5, 0.18203784098300857F, -0.0F, 0.0F);
        this.tail6.addChild(this.tail7);
        this.hips.addChild(this.tail1);
        this.tail4.addChild(this.tail5);
        this.neck5.addChild(this.neck6);
        this.tail3.addChild(this.tail4);
        this.neck3.addChild(this.neck4);
        this.body.addChild(this.neck1);
        this.bottomlegleft.addChild(this.leftbackfoot);
        this.bottomfrontrightleg.addChild(this.frontrightfoot);
        this.body.addChild(this.hips);
        this.head.addChild(this.jaw);
        this.neck1.addChild(this.neck2);
        this.head.addChild(this.snout);
        this.body.addChild(this.frontrighttopleg);
        this.tail1.addChild(this.tail2);
        this.frontlefttopleg.addChild(this.bottomfrontleftleg);
        this.tail5.addChild(this.tail6);
        this.bottomlegright.addChild(this.rightbackfoot);
        this.body.addChild(this.Stomach);
        this.toplegleft.addChild(this.bottomlegleft);
        this.neck2.addChild(this.neck3);
        this.frontrighttopleg.addChild(this.bottomfrontrightleg);
        this.toplegright.addChild(this.bottomlegright);
        this.body.addChild(this.frontlefttopleg);
        this.bottomfrontleftleg.addChild(this.frontleftfoot);
        this.neck6.addChild(this.head);
        this.tail2.addChild(this.tail3);
        this.neck4.addChild(this.neck5);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
    	super.render(entity, f, f1, f2, f3, f4, f5);
    	setRotationAngles(entity, f, f1, f2, f3, f4, f5);
    	
    	if(this.isChild) {
    		float scaling = 3F;
            GlStateManager.pushMatrix();
            
            GlStateManager.scale(1.0F / scaling, 1.0F / scaling, 1.0F / scaling);
    		this.body.render(f5);
    		GlStateManager.pushMatrix();
    		GlStateManager.translate(this.toplegleft.offsetX, this.toplegleft.offsetY, this.toplegleft.offsetZ);
    		GlStateManager.translate(this.toplegleft.rotationPointX * f5, this.toplegleft.rotationPointY * f5, this.toplegleft.rotationPointZ * f5);
    		GlStateManager.scale(0.9D, 1.0D, 1.0D);
    		GlStateManager.translate(-this.toplegleft.offsetX, -this.toplegleft.offsetY, -this.toplegleft.offsetZ);
    		GlStateManager.translate(-this.toplegleft.rotationPointX * f5, -this.toplegleft.rotationPointY * f5, -this.toplegleft.rotationPointZ * f5);
    		this.toplegleft.render(f5);
    		GlStateManager.popMatrix();
    		GlStateManager.pushMatrix();
    		GlStateManager.translate(this.toplegright.offsetX, this.toplegright.offsetY, this.toplegright.offsetZ);
    		GlStateManager.translate(this.toplegright.rotationPointX * f5, this.toplegright.rotationPointY * f5, this.toplegright.rotationPointZ * f5);
    		GlStateManager.scale(0.9D, 1.0D, 1.0D);
    		GlStateManager.translate(-this.toplegright.offsetX, -this.toplegright.offsetY, -this.toplegright.offsetZ);
    		GlStateManager.translate(-this.toplegright.rotationPointX * f5, -this.toplegright.rotationPointY * f5, -this.toplegright.rotationPointZ * f5);
    		this.toplegright.render(f5);
    		GlStateManager.popMatrix();
    		
    		GlStateManager.popMatrix();
    	}
    	else {
    		this.body.render(f5);
    		GlStateManager.pushMatrix();
    		GlStateManager.translate(this.toplegleft.offsetX, this.toplegleft.offsetY, this.toplegleft.offsetZ);
    		GlStateManager.translate(this.toplegleft.rotationPointX * f5, this.toplegleft.rotationPointY * f5, this.toplegleft.rotationPointZ * f5);
    		GlStateManager.scale(0.9D, 1.0D, 1.0D);
    		GlStateManager.translate(-this.toplegleft.offsetX, -this.toplegleft.offsetY, -this.toplegleft.offsetZ);
    		GlStateManager.translate(-this.toplegleft.rotationPointX * f5, -this.toplegleft.rotationPointY * f5, -this.toplegleft.rotationPointZ * f5);
    		this.toplegleft.render(f5);
    		GlStateManager.popMatrix();
    		GlStateManager.pushMatrix();
    		GlStateManager.translate(this.toplegright.offsetX, this.toplegright.offsetY, this.toplegright.offsetZ);
    		GlStateManager.translate(this.toplegright.rotationPointX * f5, this.toplegright.rotationPointY * f5, this.toplegright.rotationPointZ * f5);
    		GlStateManager.scale(0.9D, 1.0D, 1.0D);
    		GlStateManager.translate(-this.toplegright.offsetX, -this.toplegright.offsetY, -this.toplegright.offsetZ);
    		GlStateManager.translate(-this.toplegright.rotationPointX * f5, -this.toplegright.rotationPointY * f5, -this.toplegright.rotationPointZ * f5);
    		this.toplegright.render(f5);
    		GlStateManager.popMatrix();
    	}
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
	public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
		
		this.toplegright.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.9F * f1;
		this.toplegleft.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 0.9F * f1;
		this.frontrighttopleg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 0.9F * f1;
		this.frontlefttopleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 0.9F * f1;
	}
}