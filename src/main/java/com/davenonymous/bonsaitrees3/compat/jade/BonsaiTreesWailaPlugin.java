package com.davenonymous.bonsaitrees3.compat.jade;

import com.davenonymous.bonsaitrees3.blocks.BonsaiPotBlock;
import com.davenonymous.bonsaitrees3.blocks.BonsaiPotBlockEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class BonsaiTreesWailaPlugin implements IWailaPlugin {
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
    public void register(IWailaCommonRegistration registration) {
		LOGGER.info("Registering jade item storage:");
		registration.registerItemStorage(new BonsaiPotProvider(), BonsaiPotBlockEntity.class);
    }

	@Override
    public void registerClient(IWailaClientRegistration registration) {
		LOGGER.info("Registering jade block component:");
		registration.registerBlockComponent(new BonsaiPotProvider(), BonsaiPotBlock.class);
    }
}