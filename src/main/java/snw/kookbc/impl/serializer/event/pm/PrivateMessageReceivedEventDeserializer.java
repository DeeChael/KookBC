/*
 *     KookBC -- The Kook Bot Client & JKook API standard implementation for Java.
 *     Copyright (C) 2022 - 2023 KookBC contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package snw.kookbc.impl.serializer.event.pm;

import com.google.gson.*;
import snw.jkook.event.pm.PrivateMessageReceivedEvent;
import snw.jkook.message.PrivateMessage;
import snw.kookbc.impl.KBCClient;
import snw.kookbc.impl.serializer.event.BaseEventDeserializer;

import java.lang.reflect.Type;

public class PrivateMessageReceivedEventDeserializer extends BaseEventDeserializer<PrivateMessageReceivedEvent> {

    public PrivateMessageReceivedEventDeserializer(KBCClient client) {
        super(client);
    }

    @Override
    protected PrivateMessageReceivedEvent deserialize(JsonObject object, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        PrivateMessage privateMessage = client.getMessageBuilder().buildPrivateMessage(object);
        return new PrivateMessageReceivedEvent(
            privateMessage.getTimeStamp(),
            privateMessage.getSender(),
            privateMessage
        );
    }

    @Override
    protected void beforeReturn(PrivateMessageReceivedEvent event) {
        client.getStorage().addMessage(event.getMessage());
    }

}
