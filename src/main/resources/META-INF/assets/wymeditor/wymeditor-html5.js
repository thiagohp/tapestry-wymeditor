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
//
// Inspired from https://github.com/wymeditor/wymeditor/blob/master/src/wymeditor/plugins/embed/jquery.wymeditor.embed.js
(function () {
    function removeItem(item, arr) {
        for (var i = arr.length; i--;) {
            if (arr[i] === item) {
                arr.splice(i, 1);
            }
        }
        return arr;
    }
    if (WYMeditor && WYMeditor.XhtmlValidator._tags.param.attributes) {

        WYMeditor.XhtmlValidator._tags.article = {
            "attributes":[
            ]
        };

        WYMeditor.XhtmlValidator._tags.time = {
            "attributes":[
            ]
        };

        var XhtmlSaxListener = WYMeditor.XhtmlSaxListener;
        WYMeditor.XhtmlSaxListener = function () {
            var listener = XhtmlSaxListener.call(this);
			listener.block_tags.push('article');
			listener.block_tags.push('time');
            return listener;
        };

        WYMeditor.XhtmlSaxListener.prototype = XhtmlSaxListener.prototype;
    }
})();