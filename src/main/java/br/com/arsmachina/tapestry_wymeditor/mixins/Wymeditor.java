// Copyright 2013 Thiago H. de Paula Figueiredo
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package br.com.arsmachina.tapestry_wymeditor.mixins;

import java.util.Locale;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.ClasspathProvider;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Mixin that integrates WYMeditor 1.0.0b5 to Apache Tapestry 5.4+. 
 * 
 * @author Thiago H. de Paula Figueiredo (http://machina.com.br/thiago)
 * @see http://wymeditor.readthedocs.org/en/latest/index.html
 * @see http://wymeditor.readthedocs.org/en/latest/customizing_wymeditor/index.html
 */
@MixinAfter
@Import(library = { 
		"classpath:/META-INF/assets/wymeditor/jquery-migrate.min.js",
		"classpath:/META-INF/assets/wymeditor/jquery.wymeditor.min.js",
		"classpath:/META-INF/assets/wymeditor/plugins/embed/jquery.wymeditor.embed.js",
		"classpath:/META-INF/assets/wymeditor/jquery.wymeditor.html5.js",
})
public class Wymeditor {

	/**
	 * A JSONObject containing the options to be passed to WYMeditor. If null or not provided,
	 * it automatically creates one with the <code>lang</code> option set to the user locale.
	 */
	@Parameter
	private JSONObject options;
	
	@InjectContainer
	private ClientElement clientElement;
	
	@Inject
	private JavaScriptSupport javaScriptSupport;
	
	@Inject
	@ClasspathProvider
	private AssetFactory classpathAssetFactory;
	
	@Inject
	private Locale locale;
	
	@Inject
	@Path("classpath:/META-INF/assets/wymeditor/iframe/default/wymiframe.html")
	private Asset iframeBasePath;
	
	/**
	 * Invokes the JavaScript initialization code.
	 */
	void afterRender(MarkupWriter writer) {
		
		final JSONObject jsonObject = new JSONObject("id", clientElement.getClientId());
		final String iframeUrl = iframeBasePath.toClientURL();
        options.put("iframeBasePath", iframeUrl.substring(0, iframeUrl.lastIndexOf("/") + 1));
		jsonObject.put("options", options);
		javaScriptSupport.require("wymeditor/wymeditor").with(jsonObject);
		
	}

	/**
	 * Creates the default value for the <code>options</code> parameter.
	 * 
	 * @return a {@link JSONObject} with the <code>lang</code> option set.
	 */
	JSONObject defaultOptions() {
		return new JSONObject("lang", locale.getLanguage(), "plugins", new JSONArray("list", "resizable", "rdfa"));
	}

}
