package com.arkcraft.module.item.common.items.weapons.attachments;

import com.arkcraft.module.item.common.items.ItemARKBase;

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
