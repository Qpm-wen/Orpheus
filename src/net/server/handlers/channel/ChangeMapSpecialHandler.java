/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.server.handlers.channel;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import server.MaplePortal;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public final class ChangeMapSpecialHandler extends AbstractMaplePacketHandler {
	public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
		slea.readByte();
		String startwp = slea.readMapleAsciiString();
		slea.readShort();
		MaplePortal portal = c.getPlayer().getMap().getPortal(startwp);
		if ((c.getPlayer().portalDelay() > System.currentTimeMillis()) || c.getPlayer().getBlockedPortals().contains(portal.getScriptName())) {
			c.announce(MaplePacketCreator.enableActions());
			return;
		}
		if (portal != null) {
			portal.enterPortal(c);
		} else {
			c.announce(MaplePacketCreator.enableActions());
		}
	}
}