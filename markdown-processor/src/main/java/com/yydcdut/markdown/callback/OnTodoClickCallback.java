/*
 * Copyright (C) 2018 yydcdut (yuyidong2015@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.yydcdut.markdown.callback;

import android.view.View;

/**
 * the callback of _todo syntax
 * <p>
 * Created by yuyidong on 2018/5/17.
 */
public interface OnTodoClickCallback {
    /**
     * the click callback
     *
     * @param view the view
     * @param line the line text
     * @return the TextView
     */
    CharSequence onTodoClicked(View view, String line, int lineNumber);
}
