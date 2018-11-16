/*
 * Copyright 2018 Vaadin Community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.easyuploads.demoandtestapp;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.easyuploads.UploadField;

/**
 *
 * @author mstahv
 */
public class ByteArrayFieldTest extends AbstractTest {

    @Override
    public Component getTestComponent() {
        UploadField byteArrayField = new UploadField();
        
        byteArrayField.addValueChangeListener((HasValue.ValueChangeEvent<byte[]> event) -> {
            Notification.show("Value changed!");
        });
        
        return byteArrayField;
    }
    
}
