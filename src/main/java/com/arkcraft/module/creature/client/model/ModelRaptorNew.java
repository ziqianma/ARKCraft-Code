package com.arkcraft.module.creature.client.model;

import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelBase;
import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelRenderer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRaptorNew extends MowzieModelBase
{
	public MowzieModelRenderer LeftLegThigh;
	public MowzieModelRenderer RightLegThigh;
	public MowzieModelRenderer Waist;
	public MowzieModelRenderer LeftLeg;
	public MowzieModelRenderer LeftFoot;
	public MowzieModelRenderer LeftOutterFoot;
	public MowzieModelRenderer LeftMiddleFoot;
	public MowzieModelRenderer LeftInnerFoot;
	public MowzieModelRenderer BackLeftClaw;
	public MowzieModelRenderer LeftOutterClaw;
	public MowzieModelRenderer LeftMiddleClaw;
	public MowzieModelRenderer LeftInnerClaw1;
	public MowzieModelRenderer LeftInnerClaw2;
	public MowzieModelRenderer RightLeg;
	public MowzieModelRenderer RightFoot;
	public MowzieModelRenderer BackRightClaw;
	public MowzieModelRenderer RightOutterFoot;
	public MowzieModelRenderer RightMiddleFoot;
	public MowzieModelRenderer RightInnerFoot;
	public MowzieModelRenderer RightOutterClaw;
	public MowzieModelRenderer RightMiddleClaw;
	public MowzieModelRenderer RightInnerClaw1;
	public MowzieModelRenderer RightInnerClaw2;
	public MowzieModelRenderer Tail1;
	public MowzieModelRenderer UpperBoddy;
	public MowzieModelRenderer Tail2;
	public MowzieModelRenderer Tail3;
	public MowzieModelRenderer Tail4;
	public MowzieModelRenderer Tail5;
	public MowzieModelRenderer Tailfeathers4;
	public MowzieModelRenderer TailFeathers3;
	public MowzieModelRenderer TailFeathers2;
	public MowzieModelRenderer TailFeathers1;
	public MowzieModelRenderer Chest;
	public MowzieModelRenderer Neck;
	public MowzieModelRenderer LeftUpperArm;
	public MowzieModelRenderer RightUpperArm;
	public MowzieModelRenderer Head;
	public MowzieModelRenderer LowerSnout1;
	public MowzieModelRenderer Jaw;
	public MowzieModelRenderer HeadFeathers1;
	public MowzieModelRenderer HeadFeathers2;
	public MowzieModelRenderer HeadFeathers3;
	public MowzieModelRenderer HeadFeathers4;
	public MowzieModelRenderer HeadFeathers5;
	public MowzieModelRenderer HeadFeathers6;
	public MowzieModelRenderer HeadFeathers7;
	public MowzieModelRenderer RightEyeball;
	public MowzieModelRenderer LeftEyeball;
	public MowzieModelRenderer UpperSnout;
	public MowzieModelRenderer LowerSnout2;
	public MowzieModelRenderer RightPupill;
	public MowzieModelRenderer LeftPupill;
	public MowzieModelRenderer LeftLowerArm;
	public MowzieModelRenderer LeftArmFeathers1;
	public MowzieModelRenderer LeftArmFeathers2;
	public MowzieModelRenderer LeftArmFeathers3;
	public MowzieModelRenderer LeftArmFeathers4;
	public MowzieModelRenderer LeftArmFeathers5;
	public MowzieModelRenderer LeftHand1;
	public MowzieModelRenderer LeftHand2;
	public MowzieModelRenderer RightLowerArm;
	public MowzieModelRenderer RightArmFeathers2;
	public MowzieModelRenderer RghtArmFeathers1;
	public MowzieModelRenderer RightArmFeathers4;
	public MowzieModelRenderer RightArmFeathers5;
	public MowzieModelRenderer RightArmFeathers3;
	public MowzieModelRenderer RightHand1;
	public MowzieModelRenderer RightHand2;

	public ModelRaptorNew()
	{
		textureWidth = 128;
		textureHeight = 128;

		this.Tail2 = new MowzieModelRenderer(this, 0, 106);
		this.Tail2.setRotationPoint(0.0F, 0.23024359497401758F, 8.023024352625587F);
		this.Tail2.addBox(-2.5F, -6.091729809042101E-5F, -0.0365100503297499F, 5, 4, 9, 0.0F);
		this.setRotateAngle(Tail2, 0.03490658503988659F, 0.0F, 0.0F);
		this.HeadFeathers2 = new MowzieModelRenderer(this, 26, 41);
		this.HeadFeathers2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadFeathers2.addBox(0.0F, -5.0F, -4.0F, 0, 6, 1, 0.0F);
		this.setRotateAngle(HeadFeathers2, -1.1344640137963142F, 0.0F, 0.0F);
		this.LeftArmFeathers4 = new MowzieModelRenderer(this, 24, 32);
		this.LeftArmFeathers4.setRotationPoint(0.0F, 7.516910420957146E-4F, 0.003412681977403409F);
		this.LeftArmFeathers4.addBox(1.5F, 2.2F, 2.0F, 0, 1, 3, 0.0F);
		this.setRotateAngle(LeftArmFeathers4, -0.3839770968142266F, 0.0F, 0.0F);
		this.LeftLowerArm = new MowzieModelRenderer(this, 34, 23);
		this.LeftLowerArm.setRotationPoint(0.7012158291296555F, 4.058946910821383F,
				0.8504389079243522F);
		this.LeftLowerArm.addBox(-1.0F, 0.0F, -5.0F, 2, 2, 5, 0.0F);
		this.LeftMiddleClaw = new MowzieModelRenderer(this, 62, 8);
		this.LeftMiddleClaw.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftMiddleClaw.addBox(-0.4000000059604645F, 7.0F, -5.0F, 1, 1, 1, 0.0F);
		this.LeftInnerClaw2 = new MowzieModelRenderer(this, 62, 2);
		this.LeftInnerClaw2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftInnerClaw2.addBox(-0.800000011920929F, 4.5F, -5.5F, 1, 1, 2, 0.0F);
		this.RightUpperArm = new MowzieModelRenderer(this, 24, 23);
		this.RightUpperArm.setRotationPoint(-3.0F, 5.646995491906456F, -3.1025095241554586F);
		this.RightUpperArm.addBox(-2.0F, -1.5F, -1.5F, 2, 6, 3, 0.0F);
		this.setRotateAngle(RightUpperArm, 0.27942721324429215F, 0.0024434609527920616F,
				0.13962634015954636F);
		this.RghtArmFeathers1 = new MowzieModelRenderer(this, 24, 32);
		this.RghtArmFeathers1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RghtArmFeathers1.addBox(-1.5F, -0.5F, 0.5F, 0, 1, 3, 0.0F);
		this.RightOutterFoot = new MowzieModelRenderer(this, 56, 10);
		this.RightOutterFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightOutterFoot.addBox(-1.0F, 6.0F, -4.5F, 1, 2, 5, 0.0F);
		this.setRotateAngle(RightOutterFoot, 0.08920936674176302F, 0.21167289761824615F,
				-0.01624963763548582F);
		this.HeadFeathers5 = new MowzieModelRenderer(this, 26, 41);
		this.HeadFeathers5.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadFeathers5.addBox(0.0F, -5.0F, -1.0F, 0, 6, 1, 0.0F);
		this.setRotateAngle(HeadFeathers5, -1.6580627893946132F, 0.0F, 0.0F);
		this.RightArmFeathers2 = new MowzieModelRenderer(this, 24, 32);
		this.RightArmFeathers2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightArmFeathers2.addBox(-1.5F, 0.5F, 1.5F, 0, 1, 3, 0.0F);
		this.setRotateAngle(RightArmFeathers2, -0.08726646259971634F, 0.0F, 0.0F);
		this.BackRightClaw = new MowzieModelRenderer(this, 62, 5);
		this.BackRightClaw.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.BackRightClaw.addBox(-0.5F, 4.5F, -1.5F, 1, 2, 1, 0.0F);
		this.setRotateAngle(BackRightClaw, 0.4363323003053663F, 0.0F, 0.0F);
		this.Neck = new MowzieModelRenderer(this, 0, 32);
		this.Neck.setRotationPoint(0.0F, 3.843558310510619F, -3.267577009605857F);
		this.Neck.addBox(-2.5F, -8.5F, -2.5F, 5, 9, 5, 0.0F);
		this.setRotateAngle(Neck, 0.5061454830783555F, 0.0F, 0.0F);
		this.Jaw = new MowzieModelRenderer(this, 24, 14);
		this.Jaw.setRotationPoint(0.0F, 1.5F, -8.0F);
		this.Jaw.addBox(-2.0F, -0.7F, -6.8F, 4, 2, 7, 0.0F);
		this.setRotateAngle(Jaw, -0.05235987755982988F, 0.0F, 0.0F);
		this.HeadFeathers6 = new MowzieModelRenderer(this, 26, 41);
		this.HeadFeathers6.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadFeathers6.addBox(0.0F, -4.5F, 0.0F, 0, 6, 1, 0.0F);
		this.setRotateAngle(HeadFeathers6, -1.8325957145940461F, 0.0F, 0.0F);
		this.LeftHand2 = new MowzieModelRenderer(this, 34, 30);
		this.LeftHand2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftHand2.addBox(-1.1F, 1.0F, -5.0F, 2, 3, 1, 0.0F);
		this.RightArmFeathers4 = new MowzieModelRenderer(this, 24, 36);
		this.RightArmFeathers4.setRotationPoint(0.0F, 7.516910420957146E-4F, 0.003412681977403409F);
		this.RightArmFeathers4.addBox(-1.5F, 1.5F, 1.5F, 0, 1, 4, 0.0F);
		this.setRotateAngle(RightArmFeathers4, -0.20944417161479364F, 0.0F, 0.0F);
		this.LeftInnerFoot = new MowzieModelRenderer(this, 56, 17);
		this.LeftInnerFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftInnerFoot.addBox(-0.699999988079071F, 6.0F, -3.5F, 1, 2, 4, 0.0F);
		this.setRotateAngle(LeftInnerFoot, 0.08909425967226049F, 0.20559054771714225F,
				0.05327424499186149F);
		this.BackLeftClaw = new MowzieModelRenderer(this, 62, 5);
		this.BackLeftClaw.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.BackLeftClaw.addBox(-0.5F, 4.5F, -1.5F, 1, 2, 1, 0.0F);
		this.setRotateAngle(BackLeftClaw, 0.4363323003053663F, 0.0F, 0.0F);
		this.LeftArmFeathers5 = new MowzieModelRenderer(this, 24, 32);
		this.LeftArmFeathers5.setRotationPoint(0.0F, 7.516910420957146E-4F, 0.003412681977403409F);
		this.LeftArmFeathers5.addBox(1.5F, 3.2F, 2.0F, 0, 1, 3, 0.0F);
		this.setRotateAngle(LeftArmFeathers5, -0.5061501444538296F, 0.0F, 0.0F);
		this.Tail1 = new MowzieModelRenderer(this, 0, 92);
		this.Tail1.setRotationPoint(0.0F, -5.667564050259824F, 3.610243526255875F);
		this.Tail1.addBox(-3.0F, -0.0024359497401756913F, 0.20975647374412532F, 6, 6, 8, 0.0F);
		this.Tailfeathers4 = new MowzieModelRenderer(this, 30, 60);
		this.Tailfeathers4.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.Tailfeathers4.addBox(0.0F, -1.0F, 1.7F, 0, 2, 1, 0.0F);
		this.RightArmFeathers5 = new MowzieModelRenderer(this, 24, 32);
		this.RightArmFeathers5.setRotationPoint(0.0F, 7.516910420957146E-4F, 0.003412681977403409F);
		this.RightArmFeathers5.addBox(-1.5F, 2.2F, 2.0F, 0, 1, 3, 0.0F);
		this.setRotateAngle(RightArmFeathers5, -0.3839770968142266F, 0.0F, 0.0F);
		this.RightInnerClaw2 = new MowzieModelRenderer(this, 62, 2);
		this.RightInnerClaw2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightInnerClaw2.addBox(-0.4000000059604645F, 4.5F, -5.5F, 1, 1, 2, 0.0F);
		this.TailFeathers1 = new MowzieModelRenderer(this, 30, 71);
		this.TailFeathers1.setRotationPoint(0.0F, -0.09902680687415888F, 0.013917310096005053F);
		this.TailFeathers1.addBox(0.0F, -0.5F, 13.7F, 0, 1, 4, 0.0F);
		this.Head = new MowzieModelRenderer(this, 0, 0);
		this.Head.setRotationPoint(0.0F, -7.568395609140178F, 2.291154273188009F);
		this.Head.addBox(-3.0F, -3.0F, -8.0F, 6, 6, 8, 0.0F);
		this.setRotateAngle(Head, -0.47123889803846897F, 0.0F, 0.0F);
		this.Tail5 = new MowzieModelRenderer(this, 30, 84);
		this.Tail5.setRotationPoint(0.0F, 0.6275640502598243F, 8.980243526255876F);
		this.Tail5.addBox(-0.5F, -0.5F, -0.3F, 1, 1, 10, 0.0F);
		this.setRotateAngle(Tail5, 0.06981317007977318F, 0.0F, 0.0F);
		this.RightMiddleFoot = new MowzieModelRenderer(this, 56, 10);
		this.RightMiddleFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightMiddleFoot.addBox(-0.7F, 6.0F, -4.5F, 1, 2, 5, 0.0F);
		this.setRotateAngle(RightMiddleFoot, 0.08726646259971647F, 0.0029670597283903604F,
				-0.03473205211468716F);
		this.Waist = new MowzieModelRenderer(this, 0, 75);
		this.Waist.setRotationPoint(0.0F, 9.07F, 7.99F);
		this.Waist.addBox(-3.7F, -5.682435949740175F, -4.140243526255874F, 7, 9, 8, 0.0F);
		this.setRotateAngle(Waist, -0.06981317007977318F, 0.0F, 0.0F);
		this.LeftArmFeathers1 = new MowzieModelRenderer(this, 24, 32);
		this.LeftArmFeathers1.setRotationPoint(0.0F, 7.516910420957146E-4F, 0.003412681977403409F);
		this.LeftArmFeathers1.addBox(1.5F, -0.5F, 0.5F, 0, 1, 3, 0.0F);
		this.RightLegThigh = new MowzieModelRenderer(this, 28, 0);
		this.RightLegThigh.setRotationPoint(-3.0F, 10.004457613554232F, 6.99625315625953F);
		this.RightLegThigh.addBox(-2.0F, -3.0F, -2.5F, 3, 9, 5, 0.0F);
		this.setRotateAngle(RightLegThigh, -0.17453292012214658F, 0.0F, 0.1396263390779495F);
		this.TailFeathers2 = new MowzieModelRenderer(this, 30, 76);
		this.TailFeathers2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.TailFeathers2.addBox(0.0F, -1.0F, 7.7F, 0, 2, 6, 0.0F);
		this.LeftPupill = new MowzieModelRenderer(this, 0, 4);
		this.LeftPupill.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftPupill.addBox(4.5F, -1.5F, -2.0F, 1, 2, 1, 0.0F);
		this.setRotateAngle(LeftPupill, 0.0F, 0.7853981633974483F, 0.0F);
		this.RightInnerClaw1 = new MowzieModelRenderer(this, 62, 5);
		this.RightInnerClaw1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightInnerClaw1.addBox(-0.4000000059604645F, 5.0F, -4.5F, 1, 2, 1, 0.0F);
		this.setRotateAngle(RightInnerClaw1, 0.08726646006107329F, 0.0F, 0.0F);
		this.RightHand1 = new MowzieModelRenderer(this, 34, 30);
		this.RightHand1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightHand1.addBox(-1.1F, 1.0F, -5.0F, 2, 3, 1, 0.0F);
		this.setRotateAngle(RightHand1, -0.20943951023931945F, 0.0F, 0.0F);
		this.LeftOutterFoot = new MowzieModelRenderer(this, 56, 10);
		this.LeftOutterFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftOutterFoot.addBox(0.0F, 6.0F, -4.5F, 1, 2, 5, 0.0F);
		this.setRotateAngle(LeftOutterFoot, 0.08920936674176302F, -0.21167289761824615F,
				0.01624963763548582F);
		this.LeftOutterClaw = new MowzieModelRenderer(this, 62, 8);
		this.LeftOutterClaw.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftOutterClaw.addBox(-0.10000000149011612F, 7.0F, -5.0F, 1, 1, 1, 0.0F);
		this.LeftEyeball = new MowzieModelRenderer(this, 0, 0);
		this.LeftEyeball.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftEyeball.addBox(2.1F, -1.5F, -5.5F, 1, 2, 2, 0.0F);
		this.RightLeg = new MowzieModelRenderer(this, 44, 0);
		this.RightLeg.setRotationPoint(-0.4924559943469011F, 5.214237007402094F,
				-0.09601597006040485F);
		this.RightLeg.addBox(-1.0F, -1.0F, -0.5F, 2, 3, 7, 0.0F);
		this.Chest = new MowzieModelRenderer(this, 2, 61);
		this.Chest.setRotationPoint(0.0F, -0.2491731009600654F, -6.61026806874157F);
		this.Chest.addBox(-3.0F, 0.01745240643728348F, -5.560152304843609F, 6, 7, 7, 0.0F);
		this.setRotateAngle(Chest, -0.12217304763960307F, 0.0F, 0.0F);
		this.HeadFeathers1 = new MowzieModelRenderer(this, 26, 41);
		this.HeadFeathers1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadFeathers1.addBox(0.0F, -3.0F, -5.0F, 0, 6, 1, 0.0F);
		this.setRotateAngle(HeadFeathers1, -1.0471975511965976F, 0.0F, 0.0F);
		this.HeadFeathers3 = new MowzieModelRenderer(this, 26, 41);
		this.HeadFeathers3.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadFeathers3.addBox(0.0F, -6.0F, -3.0F, 0, 6, 1, 0.0F);
		this.setRotateAngle(HeadFeathers3, -1.3089969389957472F, 0.0F, 0.0F);
		this.RightInnerFoot = new MowzieModelRenderer(this, 56, 17);
		this.RightInnerFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightInnerFoot.addBox(-0.30000001192092896F, 6.0F, -3.5F, 1, 2, 4, 0.0F);
		this.setRotateAngle(RightInnerFoot, 0.08909425967226049F, -0.20559054771714225F,
				-0.05327424499186149F);
		this.LeftFoot = new MowzieModelRenderer(this, 46, 10);
		this.LeftFoot.setRotationPoint(-0.13917309988899418F, 0.10698280934454907F,
				5.095997010082719F);
		this.LeftFoot.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
		this.setRotateAngle(LeftFoot, 0.08632949453750462F, -0.0181521733426594F,
				0.10314017064577054F);
		this.LeftArmFeathers2 = new MowzieModelRenderer(this, 24, 32);
		this.LeftArmFeathers2.setRotationPoint(0.0F, 7.516910420957146E-4F, 0.003412681977403409F);
		this.LeftArmFeathers2.addBox(1.5F, 0.5F, 1.5F, 0, 1, 3, 0.0F);
		this.setRotateAngle(LeftArmFeathers2, -0.08727112397519053F, 0.0F, 0.0F);
		this.RightHand2 = new MowzieModelRenderer(this, 34, 30);
		this.RightHand2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightHand2.addBox(-0.9F, 1.0F, -5.0F, 2, 3, 1, 0.0F);
		this.HeadFeathers7 = new MowzieModelRenderer(this, 26, 41);
		this.HeadFeathers7.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadFeathers7.addBox(0.0F, -3.5F, 1.0F, 0, 6, 1, 0.0F);
		this.setRotateAngle(HeadFeathers7, -2.007128639793479F, 0.0F, 0.0F);
		this.RightLowerArm = new MowzieModelRenderer(this, 34, 23);
		this.RightLowerArm.setRotationPoint(-0.7012158291296555F, 4.058946910821383F,
				0.8504389079243522F);
		this.RightLowerArm.addBox(-1.0F, 0.0F, -5.0F, 2, 2, 5, 0.0F);
		this.LeftInnerClaw1 = new MowzieModelRenderer(this, 62, 5);
		this.LeftInnerClaw1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftInnerClaw1.addBox(-0.8F, 5.0F, -4.5F, 1, 2, 1, 0.0F);
		this.setRotateAngle(LeftInnerClaw1, 0.08726646259971647F, 0.0F, 0.0F);
		this.RightEyeball = new MowzieModelRenderer(this, 0, 0);
		this.RightEyeball.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightEyeball.addBox(-3.1F, -1.5F, -5.5F, 1, 2, 2, 0.0F);
		this.RightPupill = new MowzieModelRenderer(this, 0, 4);
		this.RightPupill.setRotationPoint(0.1F, 0.0F, 0.0F);
		this.RightPupill.addBox(0.9F, -1.5F, -5.5F, 1, 2, 1, 0.0F);
		this.setRotateAngle(RightPupill, 0.0F, 0.7853981633974483F, 0.0F);
		this.HeadFeathers4 = new MowzieModelRenderer(this, 26, 41);
		this.HeadFeathers4.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadFeathers4.addBox(0.0F, -6.0F, -2.0F, 0, 6, 1, 0.0F);
		this.setRotateAngle(HeadFeathers4, -1.48352986419518F, 0.0F, 0.0F);
		this.RightMiddleClaw = new MowzieModelRenderer(this, 62, 8);
		this.RightMiddleClaw.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightMiddleClaw.addBox(-0.6000000238418579F, 7.0F, -5.0F, 1, 1, 1, 0.0F);
		this.UpperBoddy = new MowzieModelRenderer(this, 0, 75);
		this.UpperBoddy.setRotationPoint(0.0F, -5.1863457317990225F, -3.727333198787372F);
		this.UpperBoddy.addBox(-3.5F, -0.41917310096006544F, -7.470268068741571F, 7, 8, 8, 0.0F);
		this.setRotateAngle(UpperBoddy, 0.20943951023931953F, 0.0F, 0.0F);
		this.UpperSnout = new MowzieModelRenderer(this, 0, 24);
		this.UpperSnout.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.UpperSnout.addBox(-2.5F, -3.7F, -6.6F, 5, 1, 7, 0.0F);
		this.setRotateAngle(UpperSnout, 0.08726646259971642F, 0.0F, 0.0F);
		this.LeftArmFeathers3 = new MowzieModelRenderer(this, 24, 36);
		this.LeftArmFeathers3.setRotationPoint(0.0F, 7.516910420957146E-4F, 0.003412681977403409F);
		this.LeftArmFeathers3.addBox(1.5F, 1.5F, 1.5F, 0, 1, 4, 0.0F);
		this.setRotateAngle(LeftArmFeathers3, -0.20944417161479364F, 0.0F, 0.0F);
		this.LeftHand1 = new MowzieModelRenderer(this, 34, 30);
		this.LeftHand1.setRotationPoint(0.0F, 7.669883586203241E-4F, 0.0033798929814690126F);
		this.LeftHand1.addBox(-0.9F, 1.0F, -5.0F, 2, 3, 1, 0.0F);
		this.setRotateAngle(LeftHand1, -0.20944417161479364F, 0.0F, 0.0F);
		this.RightArmFeathers3 = new MowzieModelRenderer(this, 24, 32);
		this.RightArmFeathers3.setRotationPoint(0.0F, 7.516910420957146E-4F, 0.003412681977403409F);
		this.RightArmFeathers3.addBox(-1.5F, 3.2F, 2.0F, 0, 1, 3, 0.0F);
		this.setRotateAngle(RightArmFeathers3, -0.5061501444538296F, 0.0F, 0.0F);
		this.LeftMiddleFoot = new MowzieModelRenderer(this, 56, 10);
		this.LeftMiddleFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftMiddleFoot.addBox(-0.30000001192092896F, 6.0F, -4.5F, 1, 2, 5, 0.0F);
		this.setRotateAngle(LeftMiddleFoot, 0.08721356892970994F, -0.003041696135063567F,
				0.03477386194254569F);
		this.LeftUpperArm = new MowzieModelRenderer(this, 24, 23);
		this.LeftUpperArm.setRotationPoint(3.0F, 5.646995491906456F, -3.1025095241554586F);
		this.LeftUpperArm.addBox(0.0F, -1.5F, -1.5F, 2, 6, 3, 0.0F);
		this.setRotateAngle(LeftUpperArm, 0.27942721324429215F, -0.0024434609527920616F,
				-0.13962634015954636F);
		this.LeftLegThigh = new MowzieModelRenderer(this, 28, 0);
		this.LeftLegThigh.setRotationPoint(3.0F, 9.999989795198115F, 6.991900489845442F);
		this.LeftLegThigh.addBox(-1.0F, -3.0F, -2.5F, 3, 9, 5, 0.0F);
		this.setRotateAngle(LeftLegThigh, -0.17455394296962073F, 0.0F, -0.13961756447654425F);
		this.LeftLeg = new MowzieModelRenderer(this, 44, 0);
		this.LeftLeg.setRotationPoint(0.4924559943469011F, 5.214237007402094F,
				-0.09601597006040485F);
		this.LeftLeg.addBox(-1.0F, -1.0F, -0.5F, 2, 3, 7, 0.0F);
		this.RightFoot = new MowzieModelRenderer(this, 46, 10);
		this.RightFoot.setRotationPoint(0.13917309988899418F, 0.10698280934454907F,
				5.095997010082719F);
		this.RightFoot.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
		this.setRotateAngle(RightFoot, 0.08632949453750462F, 0.0181521733426594F,
				-0.10314017064577054F);
		this.TailFeathers3 = new MowzieModelRenderer(this, 30, 63);
		this.TailFeathers3.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.TailFeathers3.addBox(0.0F, -1.5F, 2.7F, 0, 3, 5, 0.0F);
		this.LowerSnout1 = new MowzieModelRenderer(this, 0, 14);
		this.LowerSnout1.setRotationPoint(0.0F, 1.5F, -8.0F);
		this.LowerSnout1.addBox(-2.6F, -3.0F, -7.0F, 5, 3, 7, 0.0F);
		this.Tail3 = new MowzieModelRenderer(this, 28, 106);
		this.Tail3.setRotationPoint(0.0F, 0.18006091729809043F, 9.02651005032975F);
		this.Tail3.addBox(-1.5F, 0.0F, -0.1F, 3, 3, 9, 0.0F);
		this.setRotateAngle(Tail3, 0.03490658503988659F, 0.0F, 0.0F);
		this.Tail4 = new MowzieModelRenderer(this, 28, 95);
		this.Tail4.setRotationPoint(0.0F, 0.19999999999999996F, 9.0F);
		this.Tail4.addBox(-1.0F, -0.0024359497401756913F, -0.2697564737441253F, 2, 2, 9, 0.0F);
		this.setRotateAngle(Tail4, 0.06981317007977318F, 0.0F, 0.0F);
		this.RightOutterClaw = new MowzieModelRenderer(this, 62, 8);
		this.RightOutterClaw.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightOutterClaw.addBox(-0.8999999761581421F, 7.0F, -5.0F, 1, 1, 1, 0.0F);
		this.LowerSnout2 = new MowzieModelRenderer(this, 4, 14);
		this.LowerSnout2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LowerSnout2.addBox(1.6F, -3.0F, -7.0F, 1, 3, 7, 0.0F);
		this.Tail1.addChild(this.Tail2);
		this.Head.addChild(this.HeadFeathers2);
		this.LeftUpperArm.addChild(this.LeftArmFeathers4);
		this.LeftUpperArm.addChild(this.LeftLowerArm);
		this.LeftMiddleFoot.addChild(this.LeftMiddleClaw);
		this.LeftInnerClaw1.addChild(this.LeftInnerClaw2);
		this.Chest.addChild(this.RightUpperArm);
		this.RightUpperArm.addChild(this.RghtArmFeathers1);
		this.RightFoot.addChild(this.RightOutterFoot);
		this.Head.addChild(this.HeadFeathers5);
		this.RightUpperArm.addChild(this.RightArmFeathers2);
		this.RightFoot.addChild(this.BackRightClaw);
		this.Chest.addChild(this.Neck);
		this.Head.addChild(this.Jaw);
		this.Head.addChild(this.HeadFeathers6);
		this.LeftHand1.addChild(this.LeftHand2);
		this.RightUpperArm.addChild(this.RightArmFeathers4);
		this.LeftFoot.addChild(this.LeftInnerFoot);
		this.LeftFoot.addChild(this.BackLeftClaw);
		this.LeftUpperArm.addChild(this.LeftArmFeathers5);
		this.Waist.addChild(this.Tail1);
		this.Tail5.addChild(this.Tailfeathers4);
		this.RightUpperArm.addChild(this.RightArmFeathers5);
		this.RightInnerClaw1.addChild(this.RightInnerClaw2);
		this.TailFeathers2.addChild(this.TailFeathers1);
		this.Neck.addChild(this.Head);
		this.Tail4.addChild(this.Tail5);
		this.RightFoot.addChild(this.RightMiddleFoot);
		this.LeftUpperArm.addChild(this.LeftArmFeathers1);
		this.TailFeathers3.addChild(this.TailFeathers2);
		this.LeftEyeball.addChild(this.LeftPupill);
		this.RightInnerFoot.addChild(this.RightInnerClaw1);
		this.RightLowerArm.addChild(this.RightHand1);
		this.LeftFoot.addChild(this.LeftOutterFoot);
		this.LeftOutterFoot.addChild(this.LeftOutterClaw);
		this.Head.addChild(this.LeftEyeball);
		this.RightLegThigh.addChild(this.RightLeg);
		this.UpperBoddy.addChild(this.Chest);
		this.Head.addChild(this.HeadFeathers1);
		this.Head.addChild(this.HeadFeathers3);
		this.RightFoot.addChild(this.RightInnerFoot);
		this.LeftLeg.addChild(this.LeftFoot);
		this.LeftUpperArm.addChild(this.LeftArmFeathers2);
		this.RightHand1.addChild(this.RightHand2);
		this.Head.addChild(this.HeadFeathers7);
		this.RightUpperArm.addChild(this.RightLowerArm);
		this.LeftInnerFoot.addChild(this.LeftInnerClaw1);
		this.Head.addChild(this.RightEyeball);
		this.RightEyeball.addChild(this.RightPupill);
		this.Head.addChild(this.HeadFeathers4);
		this.RightMiddleFoot.addChild(this.RightMiddleClaw);
		this.Waist.addChild(this.UpperBoddy);
		this.LowerSnout1.addChild(this.UpperSnout);
		this.LeftUpperArm.addChild(this.LeftArmFeathers3);
		this.LeftLowerArm.addChild(this.LeftHand1);
		this.RightUpperArm.addChild(this.RightArmFeathers3);
		this.LeftFoot.addChild(this.LeftMiddleFoot);
		this.Chest.addChild(this.LeftUpperArm);
		this.LeftLegThigh.addChild(this.LeftLeg);
		this.RightLeg.addChild(this.RightFoot);
		this.Tailfeathers4.addChild(this.TailFeathers3);
		this.Head.addChild(this.LowerSnout1);
		this.Tail2.addChild(this.Tail3);
		this.Tail3.addChild(this.Tail4);
		this.RightOutterFoot.addChild(this.RightOutterClaw);
		this.LowerSnout1.addChild(this.LowerSnout2);

		setInitPose();
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(entity, f, f1, f2, f3, f4, f5);
		this.Waist.render(f5);
		this.RightLegThigh.render(f5);
		this.LeftLegThigh.render(f5);
	}

	private void setRotateAngle(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);

		setToInitPose();

		float globalSpeed = 1.0F;
		float globalDegree = 1.0F;
		float globalHeight = 1.0F;

		MowzieModelRenderer[] tail = new MowzieModelRenderer[] { Tail5, Tail4, Tail3, Tail2, Tail1 };
		MowzieModelRenderer[] body = new MowzieModelRenderer[] { Head, Neck, UpperBoddy, Waist };

		bob(Waist, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
		bob(RightLegThigh, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
		bob(LeftLegThigh, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

		walk(LeftLegThigh, globalSpeed * 0.5F, globalDegree * 0.7F, false, 3.14F, 0.2F, f, f1);
		walk(LeftLeg, globalSpeed * 0.5F, globalDegree * 0.6F, false, 1.5F, 0.3F, f, f1);
		walk(LeftFoot, globalSpeed * 0.5F, globalDegree * .8F, false, -1F, -0.1F, f, f1);

		walk(RightLegThigh, globalSpeed * 0.5F, globalDegree * 0.7F, true, 3.14F, 0.2F, f, f1);
		walk(RightLeg, globalSpeed * 0.5F, globalDegree * 0.6F, true, 1.5F, 0.3F, f, f1);
		walk(RightFoot, globalSpeed * 0.5F, globalDegree * 0.8F, true, -1F, -0.1F, f, f1);

		chainSwing(tail, globalSpeed * 0.5F, globalHeight * 0.1F, 2, f, f1);
		chainWave(tail, globalSpeed * 1.0F, globalHeight * 0.1F, 2.5, f, f1);
		chainWave(body, globalSpeed * 1.0F, globalHeight * 0.1F, 4, f, f1);

		int ticksExisted = e.ticksExisted;

		chainWave(tail, globalSpeed * 0.15F, globalHeight * 0.1F, 2, ticksExisted, 1.0F);
		chainWave(body, globalSpeed * 0.15F, globalHeight * 0.1F, 5, ticksExisted, 1.0F);
	}
}
