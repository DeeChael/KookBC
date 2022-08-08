/*
 *     KookBC -- The Kook Bot Client & JKook API standard implementation for Java.
 *     Copyright (C) 2022 KookBC contributors
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

package snw.kookbc.impl.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import snw.jkook.plugin.Plugin;
import snw.jkook.plugin.PluginClassLoader;
import snw.jkook.plugin.PluginDescription;
import snw.kookbc.impl.KBCClient;

import java.io.File;
import java.lang.reflect.Constructor;

public class SimplePluginClassLoader extends PluginClassLoader {
    private final KBCClient client;

    public SimplePluginClassLoader(KBCClient client) {
        this.client = client;
    }

    @Override
    protected <T extends Plugin> T construct(final Class<T> cls, final PluginDescription description) throws Exception {
        Constructor<T> constructor = cls.getDeclaredConstructor(
                File.class,
                File.class,
                PluginDescription.class,
                File.class,
                Logger.class
        );
        boolean prev = constructor.isAccessible(); // if other reflect operation turn this to true?
        constructor.setAccessible(true);
        File dataFolder = new File(client.getPluginsFolder(), description.getName());
        T plugin = constructor.newInstance(
                new File(dataFolder, "config.yml"),
                dataFolder,
                description,
                new File(cls.getProtectionDomain().getCodeSource().getLocation().toURI()),
                new PluginLogger(description.getName(), LoggerFactory.getLogger(cls))
        );
        constructor.setAccessible(prev);
        return plugin;
    }
}