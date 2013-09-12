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
define(["jquery", "t5/core/console"], function($, console) {
	return function(parameters) {
		if (console.debugEnabled) {
			console.debug('wymeditor.js options: ' + JSON.stringify(parameters.options));
		}
		var textarea = $('#' + parameters.id);
		textarea.wymeditor(parameters.options);
		textarea.parents('form').find('input[type="submit"]').addClass('wymupdate');
	};
});