package com.arkcraft.module.creature.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelSabertooth extends ModelBase
{
	ModelRenderer mainbody;
	ModelRenderer legbaseFR;
	ModelRenderer legbaseFL;
	ModelRenderer secondbody;
	ModelRenderer neck;
	ModelRenderer legFR;
	ModelRenderer footFR;
	ModelRenderer legFL;
	ModelRenderer footFL;
	ModelRenderer thirdbody;
	ModelRenderer secondbody2;
	ModelRenderer secondbodyunder;
	ModelRenderer tailbase;
	ModelRenderer legbaseBL;
	ModelRenderer legbaseBR;
	ModelRenderer thirdbody2;
	ModelRenderer tail;
	ModelRenderer tail2;
	ModelRenderer tail3;
	ModelRenderer tail4;
	ModelRenderer legBL;
	ModelRenderer leg2BL;
	ModelRenderer footBL;
	ModelRenderer legBR;
	ModelRenderer leg2BR;
	ModelRenderer footBR;
	ModelRenderer neck2;
	ModelRenderer underneck;
	ModelRenderer head;
	ModelRenderer upjaw;
	ModelRenderer bottomjaw;
	ModelRenderer earL;
	ModelRenderer earR;
	ModelRenderer nose;
	ModelRenderer toothbaseL;
	ModelRenderer toothbaseR;
	ModelRenderer nose2;
	ModelRenderer toothL;
	ModelRenderer toothR;

	public ModelSabertooth()
	{
		this.textureWidth = 512;
		this.textureHeight = 512;
		this.footFR = new ModelRenderer(this, 466, 121);
		this.footFR.setRotationPoint(-1.0F, 16.71F, -2.86F);
		this.footFR.addBox(0.0F, 0.0F, 0.0F, 8, 4, 10, 0.0F);
		this.setRotateAngle(footFR, 0.14870205226991687F, 0.0F, 0.0F);
		this.secondbody2 = new ModelRenderer(this, 111, 7);
		this.secondbody2.setRotationPoint(1.5F, -2.7F, 7.37F);
		this.secondbody2.addBox(0.0F, 0.0F, 0.0F, 15, 18, 11, 0.0F);
		this.setRotateAngle(secondbody2, -0.26703537555513246F, 0.0F, 0.0F);
		this.toothbaseL = new ModelRenderer(this, 5, 78);
		this.toothbaseL.setRotationPoint(8.0F, 5.0F, 0.4F);
		this.toothbaseL.addBox(0.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F);
		this.setRotateAngle(toothbaseL, 0.30019663134302466F, 0.0F, 0.0F);
		this.legBL = new ModelRenderer(this, 417, 222);
		this.legBL.setRotationPoint(1.0F, 11.26F, 2.83F);
		this.legBL.addBox(0.0F, 0.0F, 0.0F, 6, 6, 14, 0.0F);
		this.setRotateAngle(legBL, -0.34609879067047555F, 0.0F, 0.0F);
		this.leg2BL = new ModelRenderer(this, 428, 250);
		this.leg2BL.setRotationPoint(0.0F, 3.86F, 9.47F);
		this.leg2BL.addBox(0.0F, 0.0F, 0.0F, 6, 11, 5, 0.0F);
		this.setRotateAngle(leg2BL, 0.43545964837258516F, 0.0F, 0.0F);
		this.footBR = new ModelRenderer(this, 417, 120);
		this.footBR.setRotationPoint(-1.0F, 9.95F, -5.34F);
		this.footBR.addBox(0.0F, 0.0F, 0.0F, 8, 4, 10, 0.0F);
		this.setRotateAngle(footBR, 0.14870205226991687F, 0.0F, 0.0F);
		this.legFL = new ModelRenderer(this, 423, 85);
		this.legFL.setRotationPoint(1.0F, 13.45F, 2.13F);
		this.legFL.addBox(0.0F, 0.0F, 0.0F, 6, 17, 8, 0.0F);
		this.setRotateAngle(legFL, -0.4754276882432553F, 0.0F, 0.0F);
		this.legbaseBL = new ModelRenderer(this, 417, 181);
		this.legbaseBL.setRotationPoint(12.0F, 1.42F, 13.5F);
		this.legbaseBL.addBox(0.0F, 0.0F, 0.0F, 8, 17, 12, 0.0F);
		this.setRotateAngle(legbaseBL, -0.11903145498601327F, 0.0F, 0.0F);
		this.thirdbody2 = new ModelRenderer(this, 182, 0);
		this.thirdbody2.setRotationPoint(1.0F, -1.79F, -0.19F);
		this.thirdbody2.addBox(0.0F, 0.0F, 0.0F, 14, 2, 27, 0.0F);
		this.setRotateAngle(thirdbody2, -0.06841690667817772F, 0.0F, 0.0F);
		this.tailbase = new ModelRenderer(this, 250, 60);
		this.tailbase.setRotationPoint(5.0F, 0.11F, 25.24F);
		this.tailbase.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6, 0.0F);
		this.setRotateAngle(tailbase, -0.3438298626428829F, 0.0F, 0.0F);
		this.toothR = new ModelRenderer(this, 17, 89);
		this.toothR.setRotationPoint(0.0F, 4.47F, 0.17F);
		this.toothR.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotateAngle(toothR, 0.44802601898694444F, 0.0F, 0.0F);
		this.earR = new ModelRenderer(this, 47, 79);
		this.earR.setRotationPoint(-9.1F, -0.4F, -6.09F);
		this.earR.addBox(0.0F, 0.0F, 0.0F, 5, 4, 2, 0.0F);
		this.setRotateAngle(earR, -0.5654866776461628F, 0.0F, -0.5454153912482279F);
		this.nose = new ModelRenderer(this, 49, 56);
		this.nose.setRotationPoint(2.0F, -1.9F, -0.4F);
		this.nose.addBox(0.0F, 0.0F, 0.0F, 6, 5, 11, 0.0F);
		this.setRotateAngle(nose, 0.09546951008408983F, 0.0F, 0.0F);
		this.footFL = new ModelRenderer(this, 417, 120);
		this.footFL.setRotationPoint(-1.0F, 16.71F, -2.86F);
		this.footFL.addBox(0.0F, 0.0F, 0.0F, 8, 4, 10, 0.0F);
		this.setRotateAngle(footFL, 0.14870205226991687F, 0.0F, 0.0F);
		this.toothbaseR = new ModelRenderer(this, 15, 78);
		this.toothbaseR.setRotationPoint(1.0F, 5.0F, 0.4F);
		this.toothbaseR.addBox(0.0F, 0.0F, 0.0F, 1, 5, 2, 0.0F);
		this.setRotateAngle(toothbaseR, 0.30019663134302466F, 0.0F, 0.0F);
		this.tail2 = new ModelRenderer(this, 250, 100);
		this.tail2.setRotationPoint(1.0F, 0.32F, 6.17F);
		this.tail2.addBox(0.0F, 0.0F, 0.0F, 2, 2, 16, 0.0F);
		this.setRotateAngle(tail2, -0.21362830044410594F, 0.0F, 0.0F);
		this.upjaw = new ModelRenderer(this, 0, 55);
		this.upjaw.setRotationPoint(-5.0F, 6.195234177486194F, -21.09181060122682F);
		this.upjaw.addBox(0.0F, 0.0F, 0.0F, 10, 5, 12, 0.0F);
		this.legFR = new ModelRenderer(this, 470, 84);
		this.legFR.setRotationPoint(1.0F, 13.45F, 2.13F);
		this.legFR.addBox(0.0F, 0.0F, 0.0F, 6, 17, 8, 0.0F);
		this.setRotateAngle(legFR, -0.4754276882432553F, 0.0F, 0.0F);
		this.nose2 = new ModelRenderer(this, 51, 57);
		this.nose2.setRotationPoint(1.0F, 0.39419750051090574F, 0.43140317924495264F);
		this.nose2.addBox(0.0F, 0.0F, 0.0F, 4, 5, 10, 0.0F);
		this.setRotateAngle(nose2, 0.36128315516282616F, 0.0F, 0.0F);
		this.head = new ModelRenderer(this, 353, 2);
		this.head.setRotationPoint(0.0F, -0.07F, -8.89F);
		this.head.addBox(-7.0F, 0.16F, -12.05F, 14, 14, 12, 0.0F);
		this.setRotateAngle(head, -0.07714355293814937F, 0.0F, 0.0F);
		this.secondbody = new ModelRenderer(this, 102, 0);
		this.secondbody.setRotationPoint(2.0F, 2.0F, 13.9F);
		this.secondbody.addBox(0.0F, 0.0F, 0.0F, 18, 18, 18, 0.0F);
		this.setRotateAngle(secondbody, -0.09442231253289324F, 0.0F, 0.0F);
		this.earL = new ModelRenderer(this, 29, 79);
		this.earL.setRotationPoint(4.8F, -2.8F, -6.09F);
		this.earL.addBox(0.0F, 0.0F, 0.0F, 5, 4, 2, 0.0F);
		this.setRotateAngle(earL, -0.5654866776461628F, 0.0F, 0.5454153912482279F);
		this.tail4 = new ModelRenderer(this, 250, 147);
		this.tail4.setRotationPoint(0.0F, 0.09F, 11.05F);
		this.tail4.addBox(0.0F, 0.0F, 0.0F, 2, 2, 12, 0.0F);
		this.setRotateAngle(tail4, 0.4482005519121438F, 0.0F, 0.0F);
		this.legBR = new ModelRenderer(this, 417, 222);
		this.legBR.setRotationPoint(1.0F, 11.26F, 2.83F);
		this.legBR.addBox(0.0F, 0.0F, 0.0F, 6, 6, 14, 0.0F);
		this.setRotateAngle(legBR, -0.34260813216648683F, 0.0F, 0.0F);
		this.mainbody = new ModelRenderer(this, 2, 0);
		this.mainbody.setRotationPoint(-11.0F, -15.0F, -21.0F);
		this.mainbody.addBox(1.5F, 0.0F, 0.0F, 19, 22, 22, 0.0F);
		this.setRotateAngle(mainbody, 0.03490658503988659F, -0.0F, 0.0F);
		this.secondbodyunder = new ModelRenderer(this, 116, 6);
		this.secondbodyunder.setRotationPoint(1.5F, 11.996339847402526F, 8.828806842695242F);
		this.secondbodyunder.addBox(0.0F, 0.0F, 0.0F, 15, 7, 8, 0.0F);
		this.setRotateAngle(secondbodyunder, 0.16403100040700766F, 0.0F, 0.0F);
		this.thirdbody = new ModelRenderer(this, 181, 0);
		this.thirdbody.setRotationPoint(1.0F, 0.5226256258925552F, 8.045300644174858F);
		this.thirdbody.addBox(0.0F, 0.0F, 0.0F, 16, 16, 27, 0.0F);
		this.setRotateAngle(thirdbody, -0.05948577821254731F, 0.0F, 0.0F);
		this.tail = new ModelRenderer(this, 250, 79);
		this.tail.setRotationPoint(1.0F, 0.58F, 5.17F);
		this.tail.addBox(0.0F, 0.0F, 0.0F, 4, 4, 8, 0.0F);
		this.setRotateAngle(tail, -0.40893064374227134F, 0.0F, 0.0F);
		this.leg2BR = new ModelRenderer(this, 396, 250);
		this.leg2BR.setRotationPoint(0.0F, 3.86F, 9.47F);
		this.leg2BR.addBox(0.0F, 0.0F, 0.0F, 6, 11, 5, 0.0F);
		this.setRotateAngle(leg2BR, 0.43545964837258516F, 0.0F, 0.0F);
		this.footBL = new ModelRenderer(this, 417, 120);
		this.footBL.setRotationPoint(-1.0F, 9.95F, -5.34F);
		this.footBL.addBox(0.0F, 0.0F, 0.0F, 8, 4, 10, 0.0F);
		this.setRotateAngle(footBL, 0.14870205226991687F, 0.0F, 0.0F);
		this.bottomjaw = new ModelRenderer(this, 94, 56);
		this.bottomjaw.setRotationPoint(0.0F, 10.695234177486201F, -11.691810601226814F);
		this.bottomjaw.addBox(-3.0F, 0.04F, -8.12F, 6, 3, 8, 0.0F);
		this.setRotateAngle(bottomjaw, -0.06359470714748088F, 0.0F, 0.0F);
		this.neck2 = new ModelRenderer(this, 280, 4);
		this.neck2.setRotationPoint(-0.5F, -1.22F, -4.02F);
		this.neck2.addBox(-6.0F, -0.2F, -12.12F, 13, 4, 12, 0.0F);
		this.setRotateAngle(neck2, 0.16580627893946132F, 0.0F, 0.0F);
		this.toothL = new ModelRenderer(this, 5, 89);
		this.toothL.setRotationPoint(0.0F, 4.47F, 0.17F);
		this.toothL.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.setRotateAngle(toothL, 0.44802601898694444F, 0.0F, 0.0F);
		this.legbaseFL = new ModelRenderer(this, 417, 48);
		this.legbaseFL.setRotationPoint(16.0F, 6.0F, 2.0F);
		this.legbaseFL.addBox(0.0F, 0.0F, 0.0F, 8, 17, 12, 0.0F);
		this.setRotateAngle(legbaseFL, 0.2918190509334519F, 0.0F, 0.0F);
		this.neck = new ModelRenderer(this, 278, 4);
		this.neck.setRotationPoint(11.0F, 0.1F, 4.5F);
		this.neck.addBox(-8.0F, 0.01F, -11.89F, 16, 16, 12, 0.0F);
		this.setRotateAngle(neck, 0.33545228223331014F, 0.0F, 0.0F);
		this.tail3 = new ModelRenderer(this, 250, 123);
		this.tail3.setRotationPoint(0.0F, 0.32F, 14.82F);
		this.tail3.addBox(0.0F, 0.0F, 0.0F, 2, 2, 12, 0.0F);
		this.setRotateAngle(tail3, 0.6379178416039274F, 0.0F, 0.0F);
		this.legbaseFR = new ModelRenderer(this, 463, 48);
		this.legbaseFR.setRotationPoint(-2.0F, 6.0F, 2.0F);
		this.legbaseFR.addBox(0.0F, 0.0F, 0.0F, 8, 17, 12, 0.0F);
		this.setRotateAngle(legbaseFR, 0.2918190509334519F, 0.0F, 0.0F);
		this.legbaseBR = new ModelRenderer(this, 417, 181);
		this.legbaseBR.setRotationPoint(-4.0F, 1.42F, 13.38F);
		this.legbaseBR.addBox(0.0F, 0.0F, 0.0F, 8, 18, 12, 0.0F);
		this.setRotateAngle(legbaseBR, -0.11903145498601327F, 0.0F, 0.0F);
		this.underneck = new ModelRenderer(this, 284, 3);
		this.underneck.setRotationPoint(1.0F, 15.335157874150148F, -5.433500986922754F);
		this.underneck.addBox(-7.0F, -0.18F, -12.07F, 12, 6, 12, 0.0F);
		this.setRotateAngle(underneck, -0.6771877497737998F, 0.0F, 0.0F);
		this.legFR.addChild(this.footFR);
		this.secondbody.addChild(this.secondbody2);
		this.upjaw.addChild(this.toothbaseL);
		this.legbaseBL.addChild(this.legBL);
		this.legBL.addChild(this.leg2BL);
		this.leg2BR.addChild(this.footBR);
		this.legbaseFL.addChild(this.legFL);
		this.thirdbody.addChild(this.legbaseBL);
		this.thirdbody.addChild(this.thirdbody2);
		this.thirdbody.addChild(this.tailbase);
		this.toothbaseR.addChild(this.toothR);
		this.head.addChild(this.earR);
		this.upjaw.addChild(this.nose);
		this.legFL.addChild(this.footFL);
		this.upjaw.addChild(this.toothbaseR);
		this.tail.addChild(this.tail2);
		this.head.addChild(this.upjaw);
		this.legbaseFR.addChild(this.legFR);
		this.nose.addChild(this.nose2);
		this.neck.addChild(this.head);
		this.mainbody.addChild(this.secondbody);
		this.head.addChild(this.earL);
		this.tail3.addChild(this.tail4);
		this.legbaseBR.addChild(this.legBR);
		this.secondbody.addChild(this.secondbodyunder);
		this.secondbody.addChild(this.thirdbody);
		this.tailbase.addChild(this.tail);
		this.legBR.addChild(this.leg2BR);
		this.leg2BL.addChild(this.footBL);
		this.head.addChild(this.bottomjaw);
		this.neck.addChild(this.neck2);
		this.toothbaseL.addChild(this.toothL);
		this.mainbody.addChild(this.legbaseFL);
		this.mainbody.addChild(this.neck);
		this.tail2.addChild(this.tail3);
		this.mainbody.addChild(this.legbaseFR);
		this.thirdbody.addChild(this.legbaseBR);
		this.neck.addChild(this.underneck);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		// For scaling the model
		double scale = 0.7;
		double offset = 0.725;
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.mainbody.offsetX, this.mainbody.offsetY + offset,
				this.mainbody.offsetZ);
		GlStateManager.translate(this.mainbody.rotationPointX * f5,
				this.mainbody.rotationPointY * f5, this.mainbody.rotationPointZ * f5);
		// TODO Scaling TEST
		GlStateManager.scale(1D * scale, 0.98D * scale, 1D * scale);
		GlStateManager.translate(-this.mainbody.offsetX, -this.mainbody.offsetY,
				-this.mainbody.offsetZ);
		GlStateManager.translate(-this.mainbody.rotationPointX * f5,
				-this.mainbody.rotationPointY * f5, -this.mainbody.rotationPointZ * f5);
		this.mainbody.render(f5);
		GlStateManager.popMatrix();
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
