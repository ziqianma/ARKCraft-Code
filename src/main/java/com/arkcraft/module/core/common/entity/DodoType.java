package com.arkcraft.module.core.common.entity;

import java.util.Random;

/**
 * @author wildbill22
 */
public enum DodoType
{
    PINK_BROWN(0),
    BREEN_WHITE(1),
    CYAN_LGREEN(2),
    RAINBOW(3),
    GREEN_GREY(4),
    GREEN_TAN(5),
    GREEN_WHITE(6),
    GREY_GREY(7),
    LBROWN_TAN(8),
    RED_TAN(9),
    TAN_WHITE(10);

    private int type;
    public static final int numDodos = 11;

    DodoType(int id)
    {
        this.type = id;
    }

    public int getRaptorId()
    {
        return type;
    }

    public static int getRandomRaptorType()
    {
        int type = new Random().nextInt(DodoType.numDodos);
        return type;
    }

    //	public void setRaptorTypeId(int id) {
//        this.type = id;
//	}
//	
    public String toString()
    {
        switch (type)
        {
            case 1:
                return "Pink Brown";
            case 2:
                return "Cyan Light Green";
            case 3:
                return "Rainbow";
            case 4:
                return "Green Grey";
            case 5:
                return "Green Tan";
            case 6:
                return "Green White";
            case 7:
                return "Grey Grey";
            case 8:
                return "Light Brown Tan";
            case 9:
                return "Red Tan";
            case 10:
                return "Tan White";
            case 0:
            default:
                return "Albino";
        }
    }
}