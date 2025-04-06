package ru.magistu.siegemachines.client;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientEvents
{

	public static void register()
	{
		ClientGuiEvent.RENDER_HUD.register(ClientProxy::renderCrosshair);
		ClientGuiEvent.INIT_PRE.register(ClientProxy::guiInitPre);
		LifecycleEvent.SETUP.register(ClientProxy::setup);
		ClientRawInputEvent.KEY_PRESSED.register(ClientProxy::onKeyPressed);
	}
}