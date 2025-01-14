/*
 * Copyright 2017-2019 Baidu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baidu.openrasp.hook.server.jetty;

import com.baidu.openrasp.hook.AbstractClassHook;
import com.baidu.openrasp.hook.server.ServerRequestHook;
import com.baidu.openrasp.tool.annotation.HookAnnotation;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * Created by tyy on 9/22/17.
 *
 * jetty请求的hook点
 */
@HookAnnotation
public class JettyServerHandleHook extends ServerRequestHook {

    public JettyServerHandleHook() {
        couldIgnore = false;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractClassHook#isClassMatched(String)
     */
    @Override
    public boolean isClassMatched(String className) {
        return className.equals("org/eclipse/jetty/server/handler/HandlerWrapper");
    }

    /**
     * (none-javadoc)
     *
     * @see com.baidu.openrasp.hook.AbstractClassHook#hookMethod(CtClass)
     */
    @Override
    protected void hookMethod(CtClass ctClass) throws IOException, CannotCompileException, NotFoundException {
        try{
            String src = getInvokeStaticSrc(ServerRequestHook.class, "checkRequest",
                    "$0,$3,$4", Object.class, Object.class, Object.class);
            insertBefore(ctClass, "handle", null, src);
        }catch (Exception e){
            // jetty7.0.0.m0 版本
            String src = getInvokeStaticSrc(ServerRequestHook.class, "checkRequest",
                    "$0,$2,$3", Object.class, Object.class, Object.class);
            insertBefore(ctClass, "handle", null, src);
        }
    }

}
