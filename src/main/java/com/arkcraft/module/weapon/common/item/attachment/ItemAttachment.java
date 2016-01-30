package com.arkcraft.module.weapon.common.item.attachment;

import com.arkcraft.module.blocks.common.items.ItemARKBase;

/**
 * @author Lewis_McReu
 *
 */
public class ItemAttachment extends ItemARKBase
{
	private final AttachmentType type;

	public ItemAttachment(String name, AttachmentType type)
	{
		super(name);
		this.type = type;
	}

	public AttachmentType getType()
	{
		return this.type;
	}
}
