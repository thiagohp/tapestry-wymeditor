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
package br.com.arsmachina.tapestry_wymeditor.services;

import java.io.IOException;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

/**
 * {@link RequestFilter} that redirects requests to Wymeditor assets to their correct URLs.
 * Ugly workaround, I agree, but at least it works and it doesn't interfere with other asset
 * URLs.
 * 
 * <p>
 * 	   Problem description:
 * </p>
 * <ul>
 * 		<li>Tapestry 5.4 generates an URL like this: /asset.gz/meta/wymeditor/f60dc6b/jquery-migrate.min.js</li>
 *     	<li>WYMeditor tries to load, for example, lang/en.js</li>
 *     	<li>
 *     		It takes its URL, /asset.gz/meta/wymeditor/f60dc6b/jquery-migrate.min.js,
 *     		removes the file, leaving with /asset.gz/meta/wymeditor/f60dc6b,
 *     		and requests /asset.gz/meta/wymeditor/f60dc6b/lang/en.js
 *     	</li>
 *     	<li>
 *     		Tapestry receives the requests, finds the file, checks the checksum,
 *     		it doesn't match (it was created for another file), and so returns a 404 HTTP error code.
 *     	</li>
 *     <li>An asset for the classpath file is created.</li>
 *     <li>The
 * </ul>
 * <p>
 * 	   Solution:
 * </p>
 * <ul>
 * 		<li>
 * 			This request filter checks for requests for the WYMeditor asset folders: iframe, lang, plugins, skins.
 * 			The main files (jquery-migrate.min.js and jquery.wymeditor.min.js) are outside these
 * 			folders, so this request filter ignores them.
 * 		</li>
 *     	<li>
 *     		For files in the folder, such as /asset.gz/meta/wymeditor/f60dc6b/lang/en.js,
 *     		this filter remove everything before and including the checksum, leaving us with /lang/en.js. 
 *     	</li>
 *     	<li>
 *     		From it, the location in the classpath is computed and an Asset for it is created.
 *     	</li>
 *     <li>
 *     		From the Asset, the filter get an URL with the correct checksum on it. We add
 *     		?redirected to avoid a redirection infinite loop. 
 *     </li>
 *     <li>
 *     		Finally, the filter redirects to the URL above.
 *     </li>
 * </ul>
 * 
 * @author Thiago H. de Paula Figueiredo (http://machina.com.br/thiago)
 */
public class WymeditorRequestFilter implements RequestFilter {

	/**
	 * Folders containing WYMeditor assets.
	 */
	final private static String[] FOLDERS = {"/iframe/", "/lang/", "/plugins/", "/skins/"};
	
	final private AssetSource assetSource;
	
	/**
	 * Single constructor of this class.
	 * 
	 * @param assetSource an {@link AssetSource}.
	 */
	public WymeditorRequestFilter(AssetSource assetSource) {
		super();
		this.assetSource = assetSource;
	}

	public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
		
		String path = request.getPath();
		boolean handled = false;
		
		// we only handle requests for wymeditor asset URLs and which weren't redirected already
		if (path.contains("/meta/wymeditor") && request.getParameter("redirected") == null) {
		
			for (String folder : FOLDERS) {
				
				if (path.contains(folder)) {

					// for some reason, the requests for wymiframe.css came with this wrong URL,
					// so we change it to the correct one.
					if (path.contains("/iframe/default/wymiframe.html/wymiframe.css")) {
						path = path.replace(
								"/iframe/default/wymiframe.html/wymiframe.css", 
								"/iframe/default/wymiframe.css");
					}
					
					// extract the WYMeditor asset being requested.
					path = path.substring(
							path.indexOf('/', path.indexOf("/wymeditor/") + "/wymeditor/".length() + 1));
					
					// find its location in the classpath.
					String location = "/META-INF/assets/wymeditor" + path;
					
					// create an Asset pointing to it. Its URL will contain the right checksum
					// for this file
					Asset asset = assetSource.getClasspathAsset(location);
					
					// we create the redirection target URL with "/?redirected" in the end
					// so we can spot and avoid redirection infinite loops.
					final String redirectionUrl = asset.toClientURL() + "/?redirected";
					
					// finally, we redirect.
					response.sendRedirect(redirectionUrl);
					handled = true;
					break;
					
				}
				
			}
			
		}
		
		// if we didn't redirect, we pass this request to the next RequestFilter in the pipeline
		return handled ? handled : handler.service(request, response);
		
	}

}