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

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;

/**
 * Tapestry-IoC module class for the Wymeditor integration.
 * 
 * @author Thiago H. de Paula Figueiredo (http://machina.com.br/thiago)
 */
public class WymeditorModule {

	/**
	 * Contributes this module's to Tapestry as a component library (even if it actually doesn't
	 * have any actual components, just one mixin).
	 */
	@Contribute(ComponentClassResolver.class)
	public static void addLibraryMapping(Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("wymeditor", "br.com.arsmachina.tapestry_wymeditor"));
	}
	
	/**
	 * Contributes the {@link WymeditorRequestFilter} to Tapestry.
	 */
    @Contribute(RequestHandler.class)
	public static void fixRelativeRequests(OrderedConfiguration<RequestFilter> configuration) {
		configuration.addInstance("wymeditor", WymeditorRequestFilter.class);
	}
	
}
