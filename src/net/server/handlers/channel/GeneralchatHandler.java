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

import client.MapleCharacter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleClient;
import client.command.AdminCommands;
import client.command.Commands;
import client.command.DeveloperCommands;
import client.command.DonorCommands;
import client.command.GMCommands;
import client.command.PlayerCommands;
import client.command.SupportCommands;

public final class GeneralChatHandler extends net.AbstractMaplePacketHandler {

	public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
		String s = slea.readMapleAsciiString();
		MapleCharacter chr = c.getPlayer();
		char heading = s.charAt(0);
		if (heading == '/' || heading == '!' || heading == '@') {
			String[] sp = s.split(" ");
			sp[0] = sp[0].toLowerCase().substring(1);
			if (heading == '@' || heading == '/') {
				if (chr.gmLevel() != 0) {
					DonorCommands.execute(c, sp, heading);
				} else {
					PlayerCommands.execute(c, sp, heading);
				}
			} else if (heading == '!') {
				switch (chr.gmLevel()) {
					default:
						Commands.execute(c, sp, heading);
						break;
					case 2:
						SupportCommands.execute(c, sp, heading);
						break;
					case 3:
						GMCommands.execute(c, sp, heading);
						break;
					case 4:
						DeveloperCommands.execute(c, sp, heading);
						break;
					case 5:
						AdminCommands.execute(c, sp, heading);
						break;
				}
			}
		} else {
			if (!chr.isHidden())
				chr.getMap().broadcastMessage(MaplePacketCreator.getChatText(chr.getId(), s, (chr.isGM() && chr.getGMText()), slea.readByte()));
			else
				chr.getMap().broadcastGMMessage(MaplePacketCreator.getChatText(chr.getId(), s, (chr.isGM() && chr.getGMText()), slea.readByte()));
		}
	}
}
