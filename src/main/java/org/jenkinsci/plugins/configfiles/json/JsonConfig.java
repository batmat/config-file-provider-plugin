/*
 The MIT License

 Copyright (c) 2012, Dominik Bartholdi

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package org.jenkinsci.plugins.configfiles.json;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;

import jenkins.model.Jenkins;
import org.jenkinsci.lib.configprovider.AbstractConfigProviderImpl;
import org.jenkinsci.lib.configprovider.ConfigProvider;
import org.jenkinsci.lib.configprovider.model.Config;
import org.jenkinsci.lib.configprovider.model.ContentType;
import org.jenkinsci.plugins.configfiles.Messages;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * A config/provider to handle the special case of a Json file
 *
 * @author Dominik Bartholdi (imod)
 */
public class JsonConfig extends Config {
    private static final long serialVersionUID = 1L;

    @DataBoundConstructor
    public JsonConfig(String id, String name, String comment, String content) {
        super(id, name, comment, fixJsonContent(content));
    }

    public JsonConfig(String id, String name, String comment, String content, String providerId) {
        super(id, name, comment, fixJsonContent(content), providerId);
    }

    /**
     * as the form submission with stapler is done in json too, we have to do "deescape" the formated content of the json file.
     *
     * @param content the json body of the file
     * @return deescaped json
     */
    private static String fixJsonContent(String content) {
        final String c = content.trim();
        if (c.startsWith("\"") && c.endsWith("\"")) {
            return c.substring(1, c.length() - 1);
        }
        return c;
    }

    @Extension(ordinal = 180)
    public static class JsonConfigProvider extends AbstractConfigProviderImpl {

        public JsonConfigProvider() {
            load();
        }

        @Override
        public ContentType getContentType() {
            return new ContentType() {
                public String getCmMode() {
                    return "javascript";
                }

                public String getMime() {
                    return "application/json";
                }
            };
        }

        @Override
        public String getDisplayName() {
            return Messages.json_provider_name();
        }

        @NonNull
        @Override
        public Config newConfig(@NonNull String id) {
            return new JsonConfig(id,
                Messages.JsonConfig_SettingsName(),
                Messages.JsonConfig_SettingsComment(),
                "{}",
                getProviderId());
        }

    }

}
