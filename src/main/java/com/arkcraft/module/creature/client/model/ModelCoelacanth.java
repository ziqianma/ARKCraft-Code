package com.arkcraft.module.creature.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCoelacanth extends ModelBase
{

    ModelRenderer head1;
    ModelRenderer head2;
    ModelRenderer upperBody;
    ModelRenderer lowerBody;
    ModelRenderer lowerBody2;
    ModelRenderer tailBase;
    ModelRenderer jaw1;
    ModelRenderer jaw2;
    ModelRenderer tailFin;
    ModelRenderer dorsalFin;
    ModelRenderer pectoralFin1; //upperMainBodyFin
    ModelRenderer pectoralFin2;
    ModelRenderer pelvicFin1;
    ModelRenderer pelvicFin2;
    ModelRenderer backFinTop;
    ModelRenderer backFinBot;
    ModelRenderer eye1;
    ModelRenderer eye2;
    ModelRenderer mouth1;
    ModelRenderer mouth2;

    //TODO do the children thingy
    public ModelCoelacanth()
    {
        textureWidth = 128;
        textureHeight = 128;

        head1 = new ModelRenderer(this, 0, 0);
        head1.addBox(-1.2F, -2.8F, -4F, 2, 3, 4);
        head1.setRotationPoint(0F, 0F, 0F);
        head1.setTextureSize(128, 128);
        head1.mirror = true;
        setRotation(head1, 0.4363323F, 0F, 0F);

        head2 = new ModelRenderer(this, 0, 0);
        head2.addBox(-0.8F, -2.8F, -4F, 2, 3, 4);
        head2.setRotationPoint(0F, 0F, 0F);
        head2.setTextureSize(128, 128);
        head2.mirror = true;
        setRotation(head2, 0.4363323F, 0F, 0F);

        jaw1 = new ModelRenderer(this, 0, 7);
        jaw1.addBox(-1.1F, 0.5F, -3.5F, 2, 2, 3);
        jaw1.setRotationPoint(0F, 0F, 0F);
        jaw1.setTextureSize(128, 128);
        jaw1.mirror = true;
        setRotation(jaw1, 0.0872665F, 0F, 0F);

        upperBody = new ModelRenderer(this, 0, 13);
        upperBody.addBox(-1F, -2.4F, -1F, 2, 5, 6);
        upperBody.setRotationPoint(0F, 0F, 0F);
        upperBody.setTextureSize(128, 128);
        upperBody.mirror = true;
        setRotation(upperBody, 0F, 0F, 0F);

        lowerBody = new ModelRenderer(this, 0, 24);
        lowerBody.addBox(-0.7F, -2.3F, 4.3F, 1, 4, 5);
        lowerBody.setRotationPoint(0F, 0F, 0F);
        lowerBody.setTextureSize(128, 128);
        lowerBody.mirror = true;
        setRotation(lowerBody, 0F, 0F, 0F);

        lowerBody2 = new ModelRenderer(this, 0, 24);
        lowerBody2.addBox(-0.3F, -2.3F, 4.3F, 1, 4, 5);
        lowerBody2.setRotationPoint(0F, 0F, 0F);
        lowerBody2.setTextureSize(128, 128);
        lowerBody2.mirror = true;
        setRotation(lowerBody2, 0F, 0F, 0F);

        tailBase = new ModelRenderer(this, 0, 33);
        tailBase.addBox(-0.5F, 4.9F, 5.5F, 1, 3, 3);
        tailBase.setRotationPoint(0F, 0F, 0F);
        tailBase.setTextureSize(128, 128);
        tailBase.mirror = true;
        setRotation(tailBase, 0.7853982F, 0F, 0F);

        tailFin = new ModelRenderer(this, 0, 34);
        tailFin.addBox(0F, 5.6F, 6.3F, 0, 5, 5);
        tailFin.setRotationPoint(0F, 0F, 0F);
        tailFin.setTextureSize(128, 128);
        tailFin.mirror = true;
        setRotation(tailFin, 0.7853982F, 0F, 0F);

        jaw2 = new ModelRenderer(this, 0, 7);
        jaw2.addBox(-0.9F, 0.5F, -3.5F, 2, 2, 3);
        jaw2.setRotationPoint(0F, 0F, 0F);
        jaw2.setTextureSize(128, 128);
        jaw2.mirror = true;
        setRotation(jaw2, 0.0872665F, 0F, 0F);

        dorsalFin = new ModelRenderer(this, 0, 45);
        dorsalFin.addBox(0F, -2.4F, 1F, 0, 2, 3);
        dorsalFin.setRotationPoint(0F, 0F, 0F);
        dorsalFin.setTextureSize(128, 128);
        dorsalFin.mirror = true;
        setRotation(dorsalFin, 0.5235988F, 0F, 0F);

        pectoralFin1 = new ModelRenderer(this, 0, 50);
        pectoralFin1.addBox(1F, -0.5F, 1F, 0, 2, 3);
        pectoralFin1.setRotationPoint(0F, 0F, 0F);
        pectoralFin1.setTextureSize(128, 128);
        pectoralFin1.mirror = true;
        setRotation(pectoralFin1, -0.5235988F, 0.3490659F, 0F);

        pectoralFin2 = new ModelRenderer(this, 0, 50);
        pectoralFin2.addBox(-1F, -0.5F, 1F, 0, 2, 3);
        pectoralFin2.setRotationPoint(0F, 0F, 0F);
        pectoralFin2.setTextureSize(128, 128);
        pectoralFin2.mirror = true;
        setRotation(pectoralFin2, -0.5235988F, -0.3490659F, 0F);

        backFinTop = new ModelRenderer(this, 0, 55);
        backFinTop.addBox(0F, 1.3F, 7.5F, 0, 1, 2);
        backFinTop.setRotationPoint(0F, 0F, 0F);
        backFinTop.setTextureSize(128, 128);
        backFinTop.mirror = true;
        setRotation(backFinTop, 0.5235988F, 0F, 0F);

        backFinBot = new ModelRenderer(this, 0, 55);
        backFinBot.addBox(0F, -3F, 7.5F, 0, 1, 2);
        backFinBot.setRotationPoint(0F, 0F, 0F);
        backFinBot.setTextureSize(128, 128);
        backFinBot.mirror = true;
        setRotation(backFinBot, -0.5235988F, 0F, 0F);

        pelvicFin1 = new ModelRenderer(this, 0, 58);
        pelvicFin1.addBox(-1F, -3.4F, 4F, 0, 1, 2);
        pelvicFin1.setRotationPoint(0F, 0F, 0F);
        pelvicFin1.setTextureSize(128, 128);
        pelvicFin1.mirror = true;
        setRotation(pelvicFin1, -0.8726646F, 0.3490659F, 0F);

        pelvicFin2 = new ModelRenderer(this, 0, 58);
        pelvicFin2.addBox(1F, -3.4F, 4F, 0, 1, 2);
        pelvicFin2.setRotationPoint(0F, 0F, 0F);
        pelvicFin2.setTextureSize(128, 128);
        pelvicFin2.mirror = true;
        setRotation(pelvicFin2, -0.8726646F, -0.3490659F, 0F);

        eye1 = new ModelRenderer(this, 10, 7);
        eye1.addBox(0.3F, -1.3F, -3.8F, 1, 1, 1);
        eye1.setRotationPoint(0F, 0F, 0F);
        eye1.setTextureSize(128, 128);
        eye1.mirror = true;
        setRotation(eye1, 0.4363323F, 0F, 0F);

        eye2 = new ModelRenderer(this, 10, 7);
        eye2.addBox(-1.3F, -1.3F, -3.8F, 1, 1, 1);
        eye2.setRotationPoint(0F, 0F, 0F);
        eye2.setTextureSize(128, 128);
        eye2.mirror = true;
        setRotation(eye2, 0.4363323F, 0F, 0F);

        mouth1 = new ModelRenderer(this, 12, 0);
        mouth1.addBox(-0.1F, -3.4F, -4F, 1, 3, 2);
        mouth1.setRotationPoint(0F, 0F, 0F);
        mouth1.setTextureSize(128, 128);
        mouth1.mirror = true;
        setRotation(mouth1, 0.8726646F, 0F, 0F);

        mouth2 = new ModelRenderer(this, 12, 0);
        mouth2.addBox(-0.9F, -3.4F, -4F, 1, 3, 2);
        mouth2.setRotationPoint(0F, 0F, 0F);
        mouth2.setTextureSize(128, 128);
        mouth2.mirror = true;
        setRotation(mouth2, 0.8726646F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3,
                       float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        head1.render(f5);
        head2.render(f5);
        upperBody.render(f5);
        lowerBody.render(f5);
        lowerBody2.render(f5);
        tailBase.render(f5);
        jaw1.render(f5);
        tailFin.render(f5);
        jaw2.render(f5);
        dorsalFin.render(f5);
        pectoralFin1.render(f5);
        pectoralFin2.render(f5);
        backFinTop.render(f5);
        backFinBot.render(f5);
        pelvicFin1.render(f5);
        pelvicFin2.render(f5);
        eye1.render(f5);
        eye2.render(f5);
        mouth1.render(f5);
        mouth2.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3,
                                  float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

    protected void convertToChild(ModelRenderer parParent, ModelRenderer parChild)
    {
        // move child rotation point to be relative to parent
        parChild.rotationPointX -= parParent.rotationPointX;
        parChild.rotationPointY -= parParent.rotationPointY;
        parChild.rotationPointZ -= parParent.rotationPointZ;
        // make rotations relative to parent
        parChild.rotateAngleX -= parParent.rotateAngleX;
        parChild.rotateAngleY -= parParent.rotateAngleY;
        parChild.rotateAngleZ -= parParent.rotateAngleZ;
        // create relationship
        parParent.addChild(parChild);
    }
}
